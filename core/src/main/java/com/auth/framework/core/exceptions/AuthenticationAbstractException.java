package com.auth.framework.core.exceptions;

public abstract class AuthenticationAbstractException extends Exception {
    private static final long serialVersionUID = 1975169751649845488L;

    public AuthenticationAbstractException(String message) {
        super(message);
    }

    public AuthenticationAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationAbstractException(Throwable cause) {
        super(cause);
    }

    public AuthenticationAbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AuthenticationAbstractException() {
    }
}
