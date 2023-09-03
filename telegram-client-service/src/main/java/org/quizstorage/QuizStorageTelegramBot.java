package org.quizstorage;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.telegram.UpdateHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class QuizStorageTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    private final String botToken;

    private final UpdateHandler updateHandler;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handle(update);
    }

}
