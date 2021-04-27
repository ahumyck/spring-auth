package com.auth.framework.core.tokens.jwt;

import java.io.Serializable;
import java.util.Map;

public interface JsonWebToken extends Serializable {

    String getOwner();

    String getRawToken();

    Integer getDuration();

    String getSessionName();

    Object getParameter(String parameterName);

    void addParameter(String parameterName, Object parameterValue);

    Map<String, Object> getParameters();
}
