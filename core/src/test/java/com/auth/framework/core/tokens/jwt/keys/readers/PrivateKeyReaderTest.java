package com.auth.framework.core.tokens.jwt.keys.readers;

import com.auth.framework.core.tokens.jwt.keys.readers.rsa.PrivateRsaKeyReaderProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

class PrivateKeyReaderTest {

    @Test
    @SneakyThrows
    public void test() {
        String path = "D:\\Program Files (x86)\\java\\java-diplom\\core\\src\\test\\resources\\privateKey.pem";
        PrivateKey privateKey = new PrivateRsaKeyReaderProvider(path).provide();
        System.out.println(privateKey);
    }
}