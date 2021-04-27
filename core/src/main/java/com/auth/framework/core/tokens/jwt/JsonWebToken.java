package com.auth.framework.core.tokens.jwt;

import java.io.Serializable;

public interface JsonWebToken extends Serializable {

    String getOwner();

    String getRawToken();

    Integer getDuration();

    String getSessionName();
}
