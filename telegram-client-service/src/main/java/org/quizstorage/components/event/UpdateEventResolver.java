package org.quizstorage.components.event;

import org.quizstorage.components.event.events.UpdateEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateEventResolver {

    UpdateEvent resolve(Object source, Update update);

}
