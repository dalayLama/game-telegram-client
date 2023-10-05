package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class InternalServerException extends QuizTelegramClientException {

    public InternalServerException() {
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    public InternalServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.INTERNAL_ERROR.getCodes();
    }
}
