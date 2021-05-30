package com.auth.framework.twofactor.manager;

import com.auth.framework.exceptions.TwoStepAuthenticationException;
import com.auth.framework.twofactor.ValueHolder;
import com.auth.framework.twofactor.provider.AuthenticationCodeProvider;
import com.auth.framework.twofactor.provider.DefaultAuthenticationCodeProvider;
import com.auth.framework.twofactor.repository.AuthenticationCodeRepository;
import com.auth.framework.twofactor.repository.DefaultAuthenticationCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthenticationCodeManagerTest {

    private final AuthenticationCodeRepository codeRepository = new DefaultAuthenticationCodeRepository();
    private final AuthenticationCodeProvider codeProvider = new DefaultAuthenticationCodeProvider();
    private final AuthenticationCodeManager codeManager = new DefaultAuthenticationCodeManager(codeRepository, codeProvider);

    private final static String TEST_USERNAME = "testUsername";
    private final ValueHolder<String> codeHolder = new ValueHolder<>();

    @Test
    public void correctCodeInput() {
        codeManager.generateCodeAndSendItToUsername(TEST_USERNAME, codeHolder::setValue);
        boolean result = codeManager.validateCodeForUsername(TEST_USERNAME, codeHolder.getValue());
        Assertions.assertTrue(result);
    }

    @Test
    public void incorrectCodeInput() {
        try {
            codeManager.generateCodeAndSendItToUsername(TEST_USERNAME, codeHolder::setValue);
            codeManager.validateCodeForUsername(TEST_USERNAME, "invalid value");
            Assertions.fail("validateCodeForUsername had to throw TwoStepAuthenticationException");
        } catch (TwoStepAuthenticationException exception) {
            Assertions.assertTrue(exception.getMessage().startsWith("userInputCode"));
        }
    }


    @Test
    public void nonExistingCodeInput() {
        try {
            codeManager.validateCodeForUsername(TEST_USERNAME, "invalid value");
            Assertions.fail("validateCodeForUsername had to throw TwoStepAuthenticationException");
        } catch (TwoStepAuthenticationException exception) {
            Assertions.assertTrue(exception.getMessage().startsWith("Username"));
        }
    }


}