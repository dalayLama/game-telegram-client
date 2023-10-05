package org.quizstorage.exceptions;

import org.springframework.context.MessageSourceResolvable;

public abstract class QuizTelegramClientException extends RuntimeException implements MessageSourceResolvable {

    public QuizTelegramClientException() {
    }

    public QuizTelegramClientException(String message) {
        super(message);
    }

    public QuizTelegramClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizTelegramClientException(Throwable cause) {
        super(cause);
    }

    public QuizTelegramClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getDefaultMessage() {
        return getMessage();
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }
}
