package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;
import org.quizstorage.utils.UserResolvable;
import org.springframework.context.MessageSourceResolvable;

public class MessageHandlingException extends QuizTelegramClientException implements UserResolvable {

    private final String[] codes;

    private final Object[] arguments;

    private final Long userId;

    public MessageHandlingException(Long userId, QuizTelegramClientException cause) {
        super(cause);
        this.userId = userId;
        this.codes = cause.getCodes();
        this.arguments = cause.getArguments();
    }

    public MessageHandlingException(Long userId, Throwable cause, MessageSourceResolvable resolvable) {
        super(cause);
        this.userId = userId;
        this.codes = resolvable.getCodes();
        this.arguments = resolvable.getArguments();
    }

    public MessageHandlingException(Long userId, Throwable cause, ErrorMessage errorMessage, Object... arguments) {
        super(cause);
        this.userId = userId;
        this.codes = errorMessage.getCodes();
        this.arguments = arguments;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String[] getCodes() {
        return codes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

}
