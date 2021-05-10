package com.auth.framework.registration.token.password;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Date;

@RedisHash(value = "RedisPasswordToken")
public class RedisPasswordToken extends BasePasswordToken {

    private static final long serialVersionUID = -501704820083737714L;

    public RedisPasswordToken(String owner, String token, Integer duration) {
        super(owner, token, duration);
    }

    public RedisPasswordToken(BasePasswordToken basePasswordToken) {
        super(basePasswordToken.owner, basePasswordToken.token, basePasswordToken.duration);
    }


    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    @TimeToLive
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Date getExpireDate() {
        return expireDate;
    }
}
