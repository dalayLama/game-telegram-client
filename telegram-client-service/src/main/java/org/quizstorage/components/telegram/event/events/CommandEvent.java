package org.quizstorage.components.telegram.event.events;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandEvent extends UpdateEvent {

    public CommandEvent(Object source, Update update) {
        super(source, update);
    }

    public Message getMessage() {
        return getUpdate().getMessage();
    }

    public String getCommand() {
        return getMessage().getText();
    }

}
