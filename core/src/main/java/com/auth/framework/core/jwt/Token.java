package com.auth.framework.core.jwt;

public interface Token {

    String getOwner();

    String getRawToken();

    Integer getDuration();
}
