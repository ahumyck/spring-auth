package com.auth.framework.core.tokens.password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@RedisHash(value = "PasswordToken")
public class PasswordToken implements Serializable {

    private static final long serialVersionUID = 1639228654224430235L;
    private final String owner;
    private final String token;

    @TimeToLive
    private final Integer duration;
}
