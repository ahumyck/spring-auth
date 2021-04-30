package com.auth.framework.core.tokens.jwt.keys.readers.rsa;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.PrivateKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.readers.rsa.helpers.Asn1Object;
import com.auth.framework.core.tokens.jwt.keys.readers.rsa.helpers.DerParser;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

public class PrivateRsaKeyReaderProvider implements PrivateKeyProvider {

    private final String fileName;

    public PrivateRsaKeyReaderProvider(String fileName) {
        this.fileName = fileName;
    }

    public PrivateKey provide() throws ProviderException {
        PrivateKey key;
        FileInputStream fis = null;
        boolean isRSAKey = false;
        try {
            File f = new File(fileName);
            fis = new FileInputStream(f);

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") &&
                            line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                        isRSAKey = line.contains("RSA");
                    }
                } else {
                    if (line.startsWith("-----END ") &&
                            line.endsWith(" PRIVATE KEY-----")) {
                        isRSAKey = line.contains("RSA");
                        break;
                    }
                    builder.append(line);
                }
            }
            KeySpec keySpec;
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            if (isRSAKey) {
                keySpec = getRSAKeySpec(encoded);
            } else {
                keySpec = new PKCS8EncodedKeySpec(encoded);
            }
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new ProviderException(e);
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (Exception ignored) {
                }
        }
        return key;
    }


    private RSAPrivateCrtKeySpec getRSAKeySpec(byte[] keyBytes) throws IOException {

        DerParser parser = new DerParser(keyBytes);

        Asn1Object sequence = parser.read();
        if (sequence.getType() != DerParser.SEQUENCE)
            throw new IOException("Invalid DER: not a sequence"); //$NON-NLS-1$

        // Parse inside the sequence
        parser = sequence.getParser();

        parser.read(); // Skip version
        BigInteger modulus = parser.read().getInteger();
        BigInteger publicExp = parser.read().getInteger();
        BigInteger privateExp = parser.read().getInteger();
        BigInteger prime1 = parser.read().getInteger();
        BigInteger prime2 = parser.read().getInteger();
        BigInteger exp1 = parser.read().getInteger();
        BigInteger exp2 = parser.read().getInteger();
        BigInteger crtCoef = parser.read().getInteger();

        return new RSAPrivateCrtKeySpec(
                modulus, publicExp, privateExp, prime1, prime2,
                exp1, exp2, crtCoef);
    }
}
