package com.auth.framework.core.tokens.jwt.keys.readers.rsa;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.PublicKeyProvider;
import org.jose4j.keys.RsaKeyUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;

public class PublicRsaKeyReaderProvider implements PublicKeyProvider {

    private final String filename;

    public PublicRsaKeyReaderProvider(String filename) {
        this.filename = filename;
    }


    @Override
    public PublicKey provide() throws ProviderException {
        try {
            String pem = new String(Files.readAllBytes(Paths.get(filename)));
            RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();
            return rsaKeyUtil.fromPemEncoded(pem);
        } catch (Exception e) {
            throw new ProviderException(e);
        }
    }
}
