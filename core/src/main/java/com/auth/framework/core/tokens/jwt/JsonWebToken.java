package com.auth.framework.core.tokens.jwt;

import com.auth.framework.core.tokens.jwt.params.TokenParameters;

import java.io.Serializable;

public interface JsonWebToken extends Serializable {

    String getOwner();

    String getRawToken();

    Integer getDuration();

    Object getParameter(String parameterName);

    void addParameter(String parameterName, Object parameterValue);

    TokenParameters getTokenParameters();
}
