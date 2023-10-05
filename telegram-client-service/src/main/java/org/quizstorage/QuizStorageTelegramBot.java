package org.quizstorage;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.telegram.UserMessageHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class QuizStorageTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    private final String botToken;

    private final UserMessageHandler userMessageHandler;

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
        userMessageHandler.handle(update);
    }

}
