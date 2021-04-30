package com.auth.framework.core.exceptions;

public class WrongTypeSigningKeyException extends AuthenticationAbstractException{
    private static final long serialVersionUID = 6561444564604330717L;

    public WrongTypeSigningKeyException(String message) {
        super(message);
    }

    public WrongTypeSigningKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTypeSigningKeyException(Throwable cause) {
        super(cause);
    }

    public WrongTypeSigningKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WrongTypeSigningKeyException() {
    }
}
