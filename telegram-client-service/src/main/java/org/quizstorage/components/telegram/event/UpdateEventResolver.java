package org.quizstorage.components.telegram.event;

import org.quizstorage.components.telegram.event.events.UpdateEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateEventResolver {

    UpdateEvent resolve(Object source, Update update);

}
