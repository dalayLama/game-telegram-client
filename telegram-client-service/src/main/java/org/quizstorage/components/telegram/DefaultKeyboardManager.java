package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.utils.TelegramKeyboardUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class DefaultKeyboardManager implements KeyboardManager {

    private final KeyboardConfiguration keyboardConfiguration;

    @Override
    public <T> ReplyKeyboardMarkup selectKeyboard(SelectKeyboardConfig<T> selectKeyboardConfig) {
        List<KeyboardRow> keyboardRows = TelegramKeyboardUtils.createKeyboardRows(
                selectKeyboardConfig.objects(),
                selectKeyboardConfig.textExtractor(),
                selectKeyboardConfig.rowSize()
        );
        if (selectKeyboardConfig.skippable()) {
            KeyboardButton skipButton = skipButton(selectKeyboardConfig.locale());
            keyboardRows.add(new KeyboardRow(List.of(skipButton)));
        }
        return ReplyKeyboardMarkup.builder()
                .isPersistent(true)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .keyboard(keyboardRows)
                .build();
    }

    @Override
    public <T> InlineKeyboardMarkup checkboxKeyboard(CheckboxKeyboardConfig<T> checkboxKeyboardConfig) {
        List<List<InlineKeyboardButton>> rows = TelegramKeyboardUtils.createInlineKeyboardRows(
                checkboxKeyboardConfig.objects(),
                checkboxKeyboardConfig.textExtractor(),
                checkboxKeyboardConfig.idExtractor(),
                checkboxKeyboardConfig.rowSize()
        );

        if (checkboxKeyboardConfig.skippable()) {
            InlineKeyboardButton skipButton = inlineSkipButton(checkboxKeyboardConfig.locale());
            rows.add(List.of(skipButton));
        }

        rows.add(List.of(inlineConfirmButton(checkboxKeyboardConfig.locale())));
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public boolean isSelectionConfirm(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return Objects.equals(data, keyboardConfiguration.getSelectConfirmationData());
    }

    @Override
    public boolean isSkipping(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return Objects.equals(data, keyboardConfiguration.getSkipData());
    }

    @Override
    public boolean isSkipping(Message message) {
        String languageCode = message.getFrom().getLanguageCode();
        String skipText = keyboardConfiguration.getSkipText(languageCode);
        return Objects.equals(skipText, message.getText());
    }

    @Override
    public ReplyKeyboardMarkup skipKeyboard(Locale locale) {
        return ReplyKeyboardMarkup.builder()
                .isPersistent(true)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .keyboardRow(new KeyboardRow(List.of(skipButton(locale))))
                .build();
    }

    @Override
    public InlineKeyboardMarkup updateSelection(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> keyboard = callbackQuery.getMessage().getReplyMarkup().getKeyboard();
        String userSelectOption = callbackQuery.getData();
        String confirmText = keyboardConfiguration.getSelectConfirmationText(callbackQuery.getFrom().getLanguageCode());
        String confirmData = keyboardConfiguration.getSelectConfirmationData();
        String selectMarker = keyboardConfiguration.getSelectMarker();
        keyboard.stream()
                .flatMap(Collection::stream)
                .filter(button -> !TelegramKeyboardUtils.isConfirmButton(button, confirmText, confirmData))
                .filter(button -> Objects.equals(button.getCallbackData(), userSelectOption))
                .findFirst()
                .ifPresent(button -> TelegramKeyboardUtils.toggleSelection(button, selectMarker));
        return new InlineKeyboardMarkup(keyboard);
    }

    @Override
    public List<String> getSelectedValues(InlineKeyboardMarkup inlineKeyboardMarkup) {
        return getSelectedButtons(inlineKeyboardMarkup)
                .map(InlineKeyboardButton::getCallbackData)
                .toList();
    }

    @Override
    public List<String> getSelectedTexts(InlineKeyboardMarkup inlineKeyboardMarkup) {
        return getSelectedButtons(inlineKeyboardMarkup)
                .map(InlineKeyboardButton::getText)
                .map(text -> TelegramKeyboardUtils.clearSelection(text, keyboardConfiguration.getSelectMarker()))
                .toList();
    }

    private InlineKeyboardButton inlineConfirmButton(Locale locale) {
        return InlineKeyboardButton.builder()
                .callbackData(keyboardConfiguration.getSelectConfirmationData())
                .text(keyboardConfiguration.getSelectConfirmationText(locale))
                .build();
    }

    private InlineKeyboardButton inlineSkipButton(Locale locale) {
        return InlineKeyboardButton.builder()
                .callbackData(keyboardConfiguration.getSkipData())
                .text(keyboardConfiguration.getSkipText(locale))
                .build();
    }

    private KeyboardButton skipButton(Locale locale) {
        String skipText = keyboardConfiguration.getSkipText(locale);
        return KeyboardButton.builder().text(skipText).build();
    }

    private Stream<InlineKeyboardButton> getSelectedButtons(InlineKeyboardMarkup markup) {
        List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
        String selectMarker = keyboardConfiguration.getSelectMarker();
        return keyboard.stream()
                .flatMap(Collection::stream)
                .filter(button -> TelegramKeyboardUtils.isSelected(button, selectMarker));

    }

}
