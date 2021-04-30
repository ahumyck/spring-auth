package com.auth.framework.core.tokens.jwt.keys.readers;

import com.auth.framework.core.tokens.jwt.keys.readers.rsa.PublicRsaKeyReaderProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

class PublicKeyReaderTest {


    @Test
    @SneakyThrows
    public void test() {
        String path = "D:\\Program Files (x86)\\java\\java-diplom\\core\\src\\test\\resources\\publicKey.pem";
        PublicRsaKeyReaderProvider reader = new PublicRsaKeyReaderProvider(path);
        PublicKey publicKey = reader.provide();
        System.out.println(publicKey);
    }
}