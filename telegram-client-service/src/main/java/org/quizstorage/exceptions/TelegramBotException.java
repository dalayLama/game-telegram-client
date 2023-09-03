package org.quizstorage.exceptions;

public class TelegramBotException extends QuizTelegramClientException {

    public TelegramBotException() {
    }

    public TelegramBotException(String message) {
        super(message);
    }

    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramBotException(Throwable cause) {
        super(cause);
    }

    public TelegramBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
