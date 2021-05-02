package com.auth.framework.registration.token.password;

import com.auth.framework.registration.utils.DateUtils;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "PasswordToken")
public class RedisPasswordToken implements PasswordToken {

    private static final long serialVersionUID = 1639228654224430235L;
    private final String owner;
    private final String token;
    private final Date expireDate;

    @TimeToLive
    private final Integer duration;

    public RedisPasswordToken(String owner, String token, Integer duration) {
        this.owner = owner;
        this.token = token;
        this.duration = duration;
        this.expireDate = DateUtils.createDateFromNow(duration, TimeUnit.MINUTES);
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
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Date getExpireDate() {
        return expireDate;
    }
}
