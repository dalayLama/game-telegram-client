package org.quizstorage;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class QuizStorageTelegramBotTemp extends TelegramLongPollingBot {

    private final String botUsername;

    private final String botToken;

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
        Message msg = update.getMessage();
        if(msg != null && msg.isCommand()) {
            String txt = msg.getText();
            if (txt.equals("/buttons")) {
                var firstBt = InlineKeyboardButton.builder()
                        .text(EmojiParser.parseToUnicode("first")).callbackData("first")
                        .build();
                var secondBt = InlineKeyboardButton.builder()
                        .text(EmojiParser.parseToUnicode("second")).callbackData("second")
                        .build();
                var closeBt = InlineKeyboardButton.builder()
                        .text("close").callbackData("close")
                        .build();


                InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(firstBt, secondBt))
                        .keyboardRow(List.of(closeBt))
                        .build();
                sendMenu(msg.getChatId(), "<b>Menu 1</b>", keyboard);
            }
            return;
        } else if (update.getCallbackQuery() != null) {
            String userAnswer = update.getCallbackQuery().getData();
            Message message = update.getCallbackQuery().getMessage();
            if (userAnswer.equals("close")) {
                AnswerCallbackQuery alert = AnswerCallbackQuery.builder()
                        .text("Menu was closed")
                        .callbackQueryId(update.getCallbackQuery().getId()).build();
                EditMessageText editText = EditMessageText.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId()).text("Menu was closed").build();
                try {
                    execute(alert);
                    execute(editText);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                InlineKeyboardMarkup newKeyboard = updateSelection(
                        update.getCallbackQuery().getMessage().getReplyMarkup(), userAnswer);
                EditMessageReplyMarkup newKeyboardCommand = EditMessageReplyMarkup.builder()
                        .messageId(message.getMessageId())
                        .inlineMessageId(update.getCallbackQuery().getInlineMessageId())
                        .replyMarkup(newKeyboard)
                        .build();

                try {
                    execute(newKeyboardCommand);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }

    private InlineKeyboardMarkup updateSelection(InlineKeyboardMarkup inlineKeyboardMarkup, String userAnswer) {
        List<List<InlineKeyboardButton>> keyboard = inlineKeyboardMarkup.getKeyboard();
        List<InlineKeyboardButton> newButtons = new ArrayList<>();
        for (List<InlineKeyboardButton> keyboardRow : keyboard) {
            for (InlineKeyboardButton button : keyboardRow) {
                if (button.getCallbackData().equals(userAnswer)) {
                    if (button.getCallbackData().endsWith(":selected")) {
                        String newtext = button.getText().substring(0, button.getText().indexOf(":selected"));
                        String newData = button.getCallbackData()
                                .substring(0, button.getCallbackData().indexOf(":selected"));
                        InlineKeyboardButton bt = InlineKeyboardButton.builder()
                                .text(newtext).callbackData(newData)
                                .build();
                        newButtons.add(bt);
                    } else {
                        String newText = button.getText() + ":selected";
                        String newData = button.getCallbackData() + ":selected";
                        InlineKeyboardButton bt = InlineKeyboardButton.builder()
                                .text(newText).callbackData(newData)
                                .build();
                        newButtons.add(bt);
                    }
                } else {
                    newButtons.add(button);
                }
            }


        }
        return InlineKeyboardMarkup.builder()
                .keyboardRow(newButtons)
                .build();
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            Message execute = execute(sm);//Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
