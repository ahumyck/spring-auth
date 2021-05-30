package com.auth.framework.twofactor.manager;

import com.auth.framework.exceptions.TwoStepAuthenticationException;
import com.auth.framework.twofactor.provider.AuthenticationCodeProvider;
import com.auth.framework.twofactor.repository.AuthenticationCodeRepository;
import com.auth.framework.twofactor.sender.CodeSender;

public class DefaultAuthenticationCodeManager implements AuthenticationCodeManager {

    private final AuthenticationCodeRepository codeRepository;
    private final AuthenticationCodeProvider codeProvider;

    public DefaultAuthenticationCodeManager(AuthenticationCodeRepository codeRepository, AuthenticationCodeProvider codeProvider) {
        this.codeRepository = codeRepository;
        this.codeProvider = codeProvider;
    }


    @Override
    public void generateCodeAndSendItToUsername(String username, CodeSender sender) {
        String oldCode = codeRepository.find(username);
        if (oldCode != null) {
            codeRepository.removeUserCode(username);
        }
        String actualCode = codeProvider.generateCode();
        codeRepository.save(username, actualCode);
        sender.send(actualCode);
    }

    @Override
    public boolean validateCodeForUsername(String username, String userInputCode) {
        String code = codeRepository.find(username);
        if (code == null)
            throw new TwoStepAuthenticationException("Username '" + username + "' doesn't have two step authorization code");
        if (code.equals(userInputCode)) return true;
        else {
            throw new TwoStepAuthenticationException("userInputCode = {" + userInputCode + "}, actualCode = {" + code + "}");
        }
    }
}
