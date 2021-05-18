package com.auth.framework.exceptions;

public class HandlerException extends Exception {
    private static final long serialVersionUID = -8659274380031977820L;

    public HandlerException() {
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }

    public HandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
