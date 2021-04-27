package com.auth.framework.core.exceptions;

public class EncryptionException extends AuthenticationAbstractException {
    private static final long serialVersionUID = -7333984499250470339L;

    public EncryptionException() {
    }

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptionException(Throwable cause) {
        super(cause);
    }
}
