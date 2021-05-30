package com.auth.framework.core.tokens.jwt.password;


import com.auth.framework.core.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BasePasswordToken implements PasswordToken {

    protected final String owner;
    protected final String token;
    protected final Date expireDate;
    protected final Integer duration;


    private static final long serialVersionUID = 5431812333285399983L;

    public BasePasswordToken(String owner, String token, Integer duration) {
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
