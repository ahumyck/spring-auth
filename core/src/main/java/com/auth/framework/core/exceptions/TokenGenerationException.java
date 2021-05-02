package com.auth.framework.core.exceptions;

public class TokenGenerationException extends AuthenticationAbstractException{
    private static final long serialVersionUID = -7308003175397513484L;

    public TokenGenerationException(String message) {
        super(message);
    }

    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenGenerationException(Throwable cause) {
        super(cause);
    }

    public TokenGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TokenGenerationException() {
    }
}
