package org.quizstorage.components.event.events;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackQueryEvent extends UpdateEvent {

    public CallbackQueryEvent(Object source, Update update) {
        super(source, update);
    }

    public CallbackQuery getCallbackQuery() {
        return getUpdate().getCallbackQuery();
    }

    @Override
    public Long getUserId() {
        return getUpdate().getCallbackQuery().getFrom().getId();
    }

}
