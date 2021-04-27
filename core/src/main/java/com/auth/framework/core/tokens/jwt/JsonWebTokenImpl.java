package com.auth.framework.core.tokens.jwt;

import com.auth.framework.core.constants.AuthenticationConstants;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class JsonWebTokenImpl implements JsonWebToken {
    private static final long serialVersionUID = -946953360423005898L;

    private final String owner;
    private final String rawToken;
    private final Integer timeToLive;
    private final Map<String, Object> objectMap = new ConcurrentHashMap<>();

    public JsonWebTokenImpl(String owner, String rawToken, Integer timeToLive, String sessionName) {
        this.owner = owner;
        this.rawToken = rawToken;
        this.timeToLive = timeToLive;
        this.objectMap.put(AuthenticationConstants.SESSION_PARAMETER, sessionName);
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
        return (String) objectMap.get(AuthenticationConstants.SESSION_PARAMETER);
    }

    @Override
    public Object getParameter(String parameterName) {
        return objectMap.get(parameterName);
    }

    @Override
    public void addParameter(String parameterName, Object parameterValue) {
        objectMap.put(parameterName, parameterValue);
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(objectMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonWebTokenImpl)) return false;
        JsonWebTokenImpl that = (JsonWebTokenImpl) o;
        return Objects.equals(owner, that.owner)
                && Objects.equals(rawToken, that.rawToken)
                && Objects.equals(timeToLive, that.timeToLive)
                && Objects.equals(objectMap, that.objectMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, rawToken, timeToLive, objectMap);
    }

    @Override
    public String toString() {
        return "JsonWebTokenImpl{" +
                "owner='" + owner + '\'' +
                ", rawToken='" + rawToken + '\'' +
                ", timeToLive=" + timeToLive +
                ", objectMap=" + objectMap +
                '}';
    }
}
