package com.diplom.impl.exceptions;

public class UserCreationException extends RuntimeException {
    private static final long serialVersionUID = 3030905431759505013L;

    public UserCreationException() {
        super();
    }

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationException(Throwable cause) {
        super(cause);
    }

    protected UserCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
