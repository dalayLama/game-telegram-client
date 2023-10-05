package org.quizstorage.components.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserMessageHandler {


    void handle(Update update);

}
