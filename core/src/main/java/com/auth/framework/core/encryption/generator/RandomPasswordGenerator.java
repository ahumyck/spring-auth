package com.auth.framework.core.encryption.generator;

public interface RandomPasswordGenerator {

    String generatePasswordThenEncodeAsBase64();

    byte[] generatePasswordAsRawBytes();
}
