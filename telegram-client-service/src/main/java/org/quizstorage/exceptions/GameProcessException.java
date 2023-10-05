package org.quizstorage.exceptions;

import org.quizstorage.utils.UserResolvable;

public class GameProcessException extends QuizTelegramClientException implements UserResolvable {

    private final QuizTelegramClientException cause;

    private final Long userId;

    public GameProcessException(QuizTelegramClientException exception, Long userId) {
        this.cause = exception;
        this.userId = userId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String[] getCodes() {
        return cause.getCodes();
    }

    @Override
    public Object[] getArguments() {
        return cause.getArguments();
    }

}
