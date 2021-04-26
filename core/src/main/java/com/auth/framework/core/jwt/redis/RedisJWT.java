package com.auth.framework.core.jwt.redis;

import com.auth.framework.core.jwt.Token;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash(value = "RedisJWT")
public class RedisJWT implements Token, Serializable {

    private static final long serialVersionUID = 7886494156759315674L;
    private final String owner;
    private final String rawToken;
    @TimeToLive
    private final Integer timeToLive;

    public RedisJWT(String owner, String rawToken, Integer duration) {
        this.owner = owner;
        this.rawToken = rawToken;
        this.timeToLive = duration;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getRawToken() {
        return rawToken;
    }

    @Override
    public Integer getDuration() {
        return timeToLive;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    @Override
    public int hashCode() {
        return (owner.hashCode() << 2
                & rawToken.hashCode() << 4)
                ^ timeToLive.hashCode() << 16;
    }

    @Override
    public String toString() {
        return "RedisJWT{" +
                "owner='" + owner + '\'' +
                ", rawToken='" + rawToken + '\'' +
                ", timeToLive=" + timeToLive +
                '}';
    }
}
