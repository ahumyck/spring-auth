package com.auth.framework.core.exceptions;

public class KillSessionException extends AuthenticationAbstractException {
    private static final long serialVersionUID = 3671060548078078379L;

    public KillSessionException(String message) {
        super(message);
    }

    public KillSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public KillSessionException(Throwable cause) {
        super(cause);
    }

    public KillSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public KillSessionException() {
    }

}
