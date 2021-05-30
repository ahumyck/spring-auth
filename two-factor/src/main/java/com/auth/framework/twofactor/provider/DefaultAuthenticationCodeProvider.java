package com.auth.framework.twofactor.provider;

import java.security.SecureRandom;

public class DefaultAuthenticationCodeProvider implements AuthenticationCodeProvider {

    private final SecureRandom secureRandom = new SecureRandom();
    private final static Integer THRESHOLD = 999999;

    @Override
    public String generateCode() {
        return String.format("%06d", synchronizedCodeGeneration());
    }

    private synchronized Integer synchronizedCodeGeneration() {
        return secureRandom.nextInt(THRESHOLD);
    }
}
