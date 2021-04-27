package com.auth.framework.core.tokens.jwt;

import java.util.Objects;

public class JsonWebTokenImpl implements JsonWebToken {
    private static final long serialVersionUID = -946953360423005898L;

    private final String owner;
    private final String rawToken;
    private final Integer timeToLive;
    private final String sessionName;

    public JsonWebTokenImpl(String owner, String rawToken, Integer timeToLive, String sessionName) {
        this.owner = owner;
        this.rawToken = rawToken;
        this.timeToLive = timeToLive;
        this.sessionName = sessionName;
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
    public String getSessionName() {
        return sessionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonWebTokenImpl)) return false;
        JsonWebTokenImpl that = (JsonWebTokenImpl) o;
        return Objects.equals(owner, that.owner) && Objects.equals(rawToken, that.rawToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, rawToken, timeToLive);
    }

    @Override
    public String toString() {
        return "JsonWebTokenImpl{" +
                "owner='" + owner + '\'' +
                ", rawToken='" + rawToken + '\'' +
                ", timeToLive=" + timeToLive +
                ", sessionName='" + sessionName + '\'' +
                '}';
    }
}
