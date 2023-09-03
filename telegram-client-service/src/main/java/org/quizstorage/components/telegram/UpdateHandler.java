package org.quizstorage.components.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {


    void handle(Update update);

}
