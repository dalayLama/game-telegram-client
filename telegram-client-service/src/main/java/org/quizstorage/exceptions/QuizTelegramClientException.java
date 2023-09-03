package org.quizstorage.exceptions;

public class QuizTelegramClientException extends RuntimeException {

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
}
