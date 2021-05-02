package com.auth.framework.core.tokens.jwt;


import com.auth.framework.core.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class JsonWebTokenImpl implements JsonWebToken {
    private static final long serialVersionUID = -946953360423005898L;

    private final String owner;
    private final String rawToken;
    private final Integer timeToLive;
    private final Date expireDate;
    private final Map<String, Object> tokenParameters;

    public JsonWebTokenImpl(String owner, String rawToken, Integer timeToLive, Map<String, Object> tokenParameters) {
        this.owner = owner;
        this.rawToken = rawToken;
        this.timeToLive = timeToLive;
        this.expireDate = DateUtils.createDateFromNow(timeToLive, TimeUnit.MINUTES);
        if (tokenParameters != null) {
            this.tokenParameters = tokenParameters;
        } else {
            this.tokenParameters = new HashMap<>();
        }
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
    public Integer getTimeToLive() {
        return timeToLive;
    }

    @Override
    public Date getExpireDate() {
        return expireDate;
    }


    @Override
    public Object getParameter(String parameterName) {
        return tokenParameters.get(parameterName);
    }

    @Override
    public void addParameter(String parameterName, Object parameterValue) {
        tokenParameters.put(parameterName, parameterValue);
    }

    @Override
    public Map<String, Object> getTokenParameters() {
        return tokenParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonWebTokenImpl)) return false;
        JsonWebTokenImpl that = (JsonWebTokenImpl) o;
        return Objects.equals(owner, that.owner)
                && Objects.equals(rawToken, that.rawToken)
                && Objects.equals(timeToLive, that.timeToLive)
                && Objects.equals(tokenParameters, that.tokenParameters);

    }

    @Override
    public String toString() {
        return "JsonWebTokenImpl{" +
                "owner='" + owner + '\'' +
                ", rawToken='" + rawToken + '\'' +
                ", timeToLive=" + timeToLive +
                ", tokenParameters=" + tokenParameters +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, rawToken, timeToLive, tokenParameters);
    }
}
