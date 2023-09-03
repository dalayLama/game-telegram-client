package org.quizstorage.components.telegram.event;

import org.quizstorage.components.telegram.event.events.CallbackQueryEvent;
import org.quizstorage.components.telegram.event.events.CommandEvent;
import org.quizstorage.components.telegram.event.events.SimpleMessageEvent;
import org.quizstorage.components.telegram.event.events.UpdateEvent;
import org.quizstorage.exceptions.UpdateEventResolvingException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultUpdateEventResolver implements UpdateEventResolver {

    @Override
    public UpdateEvent resolve(Object source, Update update) {
        Message message = update.getMessage();
        if (message != null) {
            return message.isCommand()
                    ? new CommandEvent(source, update)
                    : new SimpleMessageEvent(source, update);
        } else if (update.getCallbackQuery() != null) {
            return new CallbackQueryEvent(source, update);
        }
        String errorMessage = "Unknown type of event for %s".formatted(update);
        throw new UpdateEventResolvingException(errorMessage);
    }

}
