package org.quizstorage.components.telegram.event.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public abstract class UpdateEvent extends ApplicationEvent {

    private final Update update;

    protected UpdateEvent(Object source, Update update) {
        super(source);
        this.update = update;
    }

}
