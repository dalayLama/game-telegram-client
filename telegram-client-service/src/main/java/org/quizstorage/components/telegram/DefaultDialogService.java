package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstoradge.director.dto.AnswerResult;
import org.quizstoradge.director.dto.GameQuestionDto;
import org.quizstoradge.director.dto.GameResult;
import org.quizstorage.components.common.CoddedMessage;
import org.quizstorage.components.common.InfoMessage;
import org.quizstorage.components.common.MessageProvider;
import org.quizstorage.generator.dto.FieldType;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultDialogService implements DialogService {

    private final TelegramBotFacade botFacade;

    private final BotApiMethodGenerator methodGenerator;

    private final KeyboardManager keyboardManager;

    private final MessageProvider messageProvider;

    @Override
    public void askToSelectSource(Long userId,
                                  Collection<? extends QuizSourceDto> sources) {
        String message = messageProvider.getLocaledMessageForUser(userId, InfoMessage.SELECT_SOURCE);
        BotApiMethod<? extends Serializable> botApiMethod = methodGenerator
                .selectSourceKeyboard(userId, message, sources);
        botFacade.execute(botApiMethod);
    }

    @Override
    public void askToSetInitField(Long userId, InitField<?> initField) {
        BotApiMethod<? extends Serializable> botApiMethod = methodGenerator.selectInitFieldKeyboard(userId, initField);
        botFacade.execute(botApiMethod);
    }

    @Override
    public void updateSelection(CallbackQuery callbackQuery) {
        InlineKeyboardMarkup newKeyboard = keyboardManager.updateSelection(callbackQuery);
        EditMessageReplyMarkup editReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(newKeyboard)
                .build();
        botFacade.execute(editReplyMarkup);
    }

    @Override
    public boolean isSelectConfirmation(CallbackQuery callbackQuery) {
        return keyboardManager.isSelectionConfirm(callbackQuery);
    }

    @Override
    public boolean isSkipping(CallbackQuery callbackQuery) {
        return keyboardManager.isSkipping(callbackQuery);
    }

    @Override
    public void confirmSkipping(CallbackQuery callbackQuery, InitField<?> initField) {
        editMessage(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getMessage().getText()
        );
        String message = messageProvider.getLocaledMessageForUser(
                callbackQuery.getFrom().getId(),
                InfoMessage.INIT_FIELD_SKIPPED,
                initField.name()
        );
        sendMessage(callbackQuery.getFrom().getId(), message);
    }

    @Override
    public boolean isSkipping(Message message) {
        return keyboardManager.isSkipping(message);
    }

    @Override
    public void confirmSkipping(Message message, InitField<?> initField) {
        Long userId = message.getFrom().getId();
        String confirmSkipMessage = messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.INIT_FIELD_SKIPPED,
                initField.name()
        );
        removeReplyKeyboard(userId, confirmSkipMessage);
    }

    @Override
    public List<String> getSelectedValues(InlineKeyboardMarkup inlineKeyboardMarkup) {
        return keyboardManager.getSelectedValues(inlineKeyboardMarkup);
    }

    @Override
    public void removeReplyKeyboard(Long userId, CoddedMessage coddedMessage, Object... args) {
        String message = messageProvider.getLocaledMessageForUser(userId, coddedMessage, args);
        removeReplyKeyboard(userId, message);
    }

    @Override
    public void removeReplyKeyboard(Long userId, MessageSourceResolvable messageSourceResolvable) {
        String message = messageProvider.getLocaledMessageForUser(userId, messageSourceResolvable);
        removeReplyKeyboard(userId, message);
    }

    @Override
    public void editMessage(Long userId, Long chatId, Integer messageId, CoddedMessage coddedMessage, Object... args) {
        String message = messageProvider.getLocaledMessageForUser(userId, coddedMessage, args);
        editMessage(chatId, messageId, message);
    }

    @Override
    public void editMessage(Long userId, Long chatId, Integer messageId, MessageSourceResolvable messageSourceResolvable) {
        String message = messageProvider.getLocaledMessageForUser(userId, messageSourceResolvable);
        editMessage(chatId, messageId, message);
    }

    @Override
    public void sendMessage(Long userId, MessageSourceResolvable resolvable) {
        String message = messageProvider.getLocaledMessageForUser(userId, resolvable);
        sendMessage(userId, message);
    }

    @Override
    public void sendMessage(Long userId, CoddedMessage coddedMessage, Object... args) {
        String message = messageProvider.getLocaledMessageForUser(userId, coddedMessage, args);
        sendMessage(userId, message);
    }

    @Override
    public void confirmInitField(CallbackQuery callbackQuery, InitField<?> initField) {
        editMessage(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getMessage().getText()
        );

        Long userId = callbackQuery.getFrom().getId();
        List<String> texts = keyboardManager.getSelectedTexts(callbackQuery.getMessage().getReplyMarkup());
        String stringValue = String.join(", ", texts);
        String msg = messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.INIT_FIELD_VALUE_ACCEPTED,
                initField.name(),
                stringValue
        );
        sendMessage(userId, msg);
    }

    @Override
    public void confirmInitField(Message message, InitField<?> initField) {
        Long userId = message.getFrom().getId();
        String msg = messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.INIT_FIELD_VALUE_ACCEPTED,
                initField.name(),
                message.getText()
        );

        if (initField.type() == FieldType.SELECT || !initField.required()) {
            removeReplyKeyboard(userId, msg);
        } else {
            sendMessage(userId, msg);
        }
    }

    @Override
    public void confirmSelectedOption(Message message) {
        removeReplyKeyboard(message.getFrom().getId(), InfoMessage.OPTION_SELECTED, message.getText());
    }

    @Override
    public void confirmSelectedSource(Long userId, QuizSourceDto source) {
        removeReplyKeyboard(userId, InfoMessage.SOURCE_SELECTED, source.name());
    }

    @Override
    public void askQuestion(Long userId, GameQuestionDto question) {
        BotApiMethod<? extends Serializable> botApiMethod = methodGenerator.questionKeyboard(userId, question);
        botFacade.execute(botApiMethod);
    }

    @Override
    public void gameOver(Long userId, GameResult gameResult) {
        String message = messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.GAME_OVER,
                gameResult.totalQuestions(),
                gameResult.correctAnswers()
        );
        sendMessage(userId, message);
    }

    @Override
    public void confirmAnswer(Message message, GameQuestionDto question, AnswerResult result) {
        Long userId = message.getFrom().getId();
        String text = messageProvider.getLocaledMessageForUser(userId, InfoMessage.ANSWER_ACCEPTED);
        removeReplyKeyboard(userId, text);
    }

    @Override
    public void confirmAnswer(CallbackQuery callbackQuery, GameQuestionDto question, AnswerResult answer) {
        editMessage(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getMessage().getText()
        );
        Long userId = callbackQuery.getFrom().getId();
        String text = messageProvider.getLocaledMessageForUser(userId, InfoMessage.ANSWER_ACCEPTED);
        sendMessage(userId, text);
    }

    private void sendMessage(Long userId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .text(text)
                .chatId(userId)
                .build();
        botFacade.execute(sendMessage);
    }

    private void removeReplyKeyboard(Long userId, String message) {
        ReplyKeyboardRemove remove = ReplyKeyboardRemove.builder().removeKeyboard(true).build();
        SendMessage removeKeyboardMessage = SendMessage.builder()
                .text(message)
                .chatId(userId)
                .replyMarkup(remove).build();
        botFacade.execute(removeKeyboardMessage);
    }

    private void editMessage(Long chatId, Integer messageId, String text) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .build();
        botFacade.execute(editMessageText);
    }

}
