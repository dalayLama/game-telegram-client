package org.quizstorage.exceptions;

import lombok.Getter;
import org.quizstorage.components.common.ErrorMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class UpdateEventResolvingException extends QuizTelegramClientException {

    private static final String MESSAGE_TEMPLATE = "Unable to resolve update event:\n%s";

    private final Update update;

    public UpdateEventResolvingException(Update update) {
        this(MESSAGE_TEMPLATE.formatted(update), update);
    }

    public UpdateEventResolvingException(String message, Update update) {
        super(message);
        this.update = update;
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.RESOLVING_UPDATE_EVENT.getCodes();
    }

}
