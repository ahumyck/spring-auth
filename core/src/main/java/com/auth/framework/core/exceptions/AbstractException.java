package com.auth.framework.core.exceptions;

public abstract class AbstractException extends RuntimeException {
    private static final long serialVersionUID = 1975169751649845488L;

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractException(Throwable cause) {
        super(cause);
    }

    public AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractException() {
    }
}
