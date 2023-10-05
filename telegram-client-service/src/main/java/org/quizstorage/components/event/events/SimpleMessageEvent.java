package org.quizstorage.components.event.events;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleMessageEvent extends UpdateEvent {


    public SimpleMessageEvent(Object source, Update update) {
        super(source, update);
    }

    public Message getMessage() {
        return getUpdate().getMessage();
    }

    @Override
    public Long getUserId() {
        return getUpdate().getMessage().getFrom().getId();
    }
}
