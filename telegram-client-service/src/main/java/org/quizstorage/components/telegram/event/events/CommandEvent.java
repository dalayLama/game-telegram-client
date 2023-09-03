package org.quizstorage.components.telegram.event.events;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandEvent extends UpdateEvent {

    public CommandEvent(Object source, Update update) {
        super(source, update);
    }

}
