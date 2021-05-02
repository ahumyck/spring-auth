package com.auth.framework.core.exceptions;

public class ProviderException extends AuthenticationAbstractException{
    private static final long serialVersionUID = -7746353545427692866L;

    public ProviderException(String message) {
        super(message);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderException(Throwable cause) {
        super(cause);
    }

    public ProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProviderException() {
    }
}