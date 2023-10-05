package org.quizstorage.components.telegram;

import org.quizstorage.components.common.CoddedMessage;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.springframework.context.MessageSourceResolvable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Collection;
import java.util.List;

public interface DialogService {

    void askToSelectSource(Long userId, Collection<? extends QuizSourceDto> sources);

    void askToSetInitField(Long userId, InitField<?> initField);

    void updateSelection(CallbackQuery callbackQuery);

    boolean isSelectConfirmation(CallbackQuery callbackQuery);

    List<String> getSelectedValues(InlineKeyboardMarkup inlineKeyboardMarkup);

    void removeReplyKeyboard(Long userId, CoddedMessage coddedMessage, Object... args);

    void removeReplyKeyboard(Long userId, MessageSourceResolvable messageSourceResolvable);

    void editMessage(Long userId, Long chatId, Integer messageId, CoddedMessage coddedMessage, Object... args);

    void editMessage(Long userId, Long chatId, Integer messageId, MessageSourceResolvable messageSourceResolvable);

    void sendMessage(Long userId, MessageSourceResolvable resolvable);

    void sendMessage(Long userId, CoddedMessage coddedMessage, Object...args);

    void confirmInitField(CallbackQuery callbackQuery, InitField<?> initField);

    void confirmInitField(Message message, InitField<?> initField);

    void confirmSelectedOption(Message message);

    void confirmSelectedSource(Long userId, QuizSourceDto source);

    boolean isSkipping(CallbackQuery callbackQuery);

    void confirmSkipping(CallbackQuery callbackQuery, InitField<?> initField);

    boolean isSkipping(Message message);

    void confirmSkipping(Message message, InitField<?> initField);
}
