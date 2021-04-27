package com.diplom.impl.factory.email;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender {

    public String sendMessage(String email, String content) {
        log.info("message with content {} has been sent to {}", content, email);
        return content;
    }
}
