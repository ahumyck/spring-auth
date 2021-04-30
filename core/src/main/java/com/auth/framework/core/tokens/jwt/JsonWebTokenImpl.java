package com.auth.framework.core.tokens.jwt;

import com.auth.framework.core.tokens.jwt.params.TokenParameters;

import java.util.Objects;

public class JsonWebTokenImpl implements JsonWebToken {
    private static final long serialVersionUID = -946953360423005898L;

    private final String owner;
    private final String rawToken;
    private final Integer timeToLive;
    private final TokenParameters tokenParameters;

    public JsonWebTokenImpl(String owner, String rawToken, Integer timeToLive, TokenParameters tokenParameters) {
        this.owner = owner;
        this.rawToken = rawToken;
        this.timeToLive = timeToLive;
        if (tokenParameters != null) {
            this.tokenParameters = tokenParameters;
        } else {
            this.tokenParameters = new TokenParameters();
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
    public Integer getDuration() {
        return timeToLive;
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
    public TokenParameters getTokenParameters() {
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
                && TokenParameters.equals(tokenParameters, that.tokenParameters);

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
