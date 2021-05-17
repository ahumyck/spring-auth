package com.auth.framework.core.exceptions;

public class ResolveOwnerException extends AbstractException {
    private static final long serialVersionUID = 5894208332934430388L;

    public ResolveOwnerException(String message) {
        super(message);
    }

    public ResolveOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolveOwnerException(Throwable cause) {
        super(cause);
    }

    public ResolveOwnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResolveOwnerException() {
    }
}
