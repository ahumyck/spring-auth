package com.auth.framework.core.tokens.jwt;


import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface JsonWebToken extends Serializable {

    String getOwner();

    String getRawToken();

    Integer getTimeToLive();

    Date getExpireDate();

    Object getParameter(String parameterName);

    void addParameter(String parameterName, Object parameterValue);

    Map<String, Object> getTokenParameters();
}
