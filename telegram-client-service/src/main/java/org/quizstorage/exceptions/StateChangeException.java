package org.quizstorage.exceptions;

public class StateChangeException extends InternalServerException {

    public StateChangeException() {
    }

    public StateChangeException(String message) {
        super(message);
    }

    public StateChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateChangeException(Throwable cause) {
        super(cause);
    }

    public StateChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
