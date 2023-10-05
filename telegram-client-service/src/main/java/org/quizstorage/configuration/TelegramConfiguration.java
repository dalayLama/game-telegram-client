package org.quizstorage.configuration;

import lombok.RequiredArgsConstructor;
import org.quizstorage.QuizStorageTelegramBot;
import org.quizstorage.components.telegram.UserMessageHandler;
import org.quizstorage.configuration.properties.TelegramBotProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramConfiguration {

    private final TelegramBotProperties properties;

    @Bean
    public TelegramLongPollingBot telegramLongPollingBot(UserMessageHandler userMessageHandler) {
        return new QuizStorageTelegramBot(properties.getUsername(), properties.getToken(), userMessageHandler);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(LongPollingBot bot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
        return telegramBotsApi;
    }

}
