package org.quizstorage.components.telegram.event.events;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleMessageEvent extends UpdateEvent {


    public SimpleMessageEvent(Object source, Update update) {
        super(source, update);
    }

    public Message getMessage() {
        return getUpdate().getMessage();
    }

}
