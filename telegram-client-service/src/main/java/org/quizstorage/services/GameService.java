package org.quizstorage.services;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface GameService {

    void newGame(Long userId);

    void selectSourceByName(Message message);

    void setCurrentInitFieldValue(Message message);

    void setCurrentInitFieldValues(CallbackQuery callbackQuery);

    void skipCurrentInitField(CallbackQuery callbackQuery);

    void skipCurrentInitField(Message message);
}
