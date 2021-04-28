package com.auth.framework.core.exceptions;

public class ActionExecutionException extends AuthenticationAbstractException {
    private static final long serialVersionUID = -1892193022495047010L;

    public ActionExecutionException(String message) {
        super(message);
    }

    public ActionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionExecutionException(Throwable cause) {
        super(cause);
    }

    public ActionExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ActionExecutionException() {
    }
}
