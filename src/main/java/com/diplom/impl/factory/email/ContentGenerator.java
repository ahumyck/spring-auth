package com.diplom.impl.factory.email;

import com.diplom.impl.factory.PasswordTokenUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentGenerator {

    @Autowired
    private PasswordTokenUrlFactory urlFactory;

    public String generateMessageForUser(String username) {
        String url = urlFactory.generateUrl(username);
        return username + " please proceed to " + url + " to continue registration";
    }
}
