package com.auth.framework.twofactor.manager;

import com.auth.framework.twofactor.sender.CodeSender;

public interface AuthenticationCodeManager {

    void generateCodeAndSendItToUsername(String username, CodeSender sender);

    boolean validateCodeForUsername(String username, String userInputCode);
}
