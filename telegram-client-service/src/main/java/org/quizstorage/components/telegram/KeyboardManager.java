package org.quizstorage.components.telegram;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Locale;

public interface KeyboardManager {

    <T> ReplyKeyboardMarkup selectKeyboard(SelectKeyboardConfig<T> selectKeyboardConfig);

    <T> InlineKeyboardMarkup checkboxKeyboard(CheckboxKeyboardConfig<T> checkboxKeyboardConfig);

    boolean isSelectionConfirm(CallbackQuery callbackQuery);

    InlineKeyboardMarkup updateSelection(CallbackQuery callbackQuery);

    List<String> getSelectedValues(InlineKeyboardMarkup inlineKeyboardMarkup);

    List<String> getSelectedTexts(InlineKeyboardMarkup inlineKeyboardMarkup);

    boolean isSkipping(CallbackQuery callbackQuery);

    boolean isSkipping(Message message);

    ReplyKeyboardMarkup skipKeyboard(Locale locale);

}
