package com.auth.framework.exceptions;

public class TwoStepAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1884369502702952484L;

    public TwoStepAuthenticationException() {
        super();
    }

    public TwoStepAuthenticationException(String message) {
        super(message);
    }

    public TwoStepAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwoStepAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected TwoStepAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
