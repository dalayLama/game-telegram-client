package org.quizstorage.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@UtilityClass
public class TelegramKeyboardUtils {

    private static final String FULL_SELECT_MARKER_TEMPLATE = " %s";

    public static boolean isSelected(InlineKeyboardButton button, String selectMarker) {
        String fullSelectMarker = FULL_SELECT_MARKER_TEMPLATE.formatted(selectMarker);
        return button.getText().endsWith(fullSelectMarker);
    }

    private static boolean isSelected(String buttonText, String selectMarker) {
        String fullSelectMarker = FULL_SELECT_MARKER_TEMPLATE.formatted(selectMarker);
        return buttonText.endsWith(fullSelectMarker);
    }

    public static void toggleSelection(InlineKeyboardButton button, String selectMarker) {
        String fullSelectMarker = FULL_SELECT_MARKER_TEMPLATE.formatted(selectMarker);
        if (isSelected(button, selectMarker)) {
            button.setText(clearSelection(button.getText(), selectMarker));
        } else {
            button.setText(button.getText() + fullSelectMarker);
        }
    }

    public static String clearSelection(String buttonText, String selectMarker) {
        String fullSelectMarker = FULL_SELECT_MARKER_TEMPLATE.formatted(selectMarker);
        if (isSelected(buttonText, selectMarker)) {
            return buttonText.substring(0, buttonText.indexOf(fullSelectMarker));
        }
        return buttonText;


    }

    public static boolean isConfirmButton(InlineKeyboardButton button, String confirmText, String confirmData) {
        return Objects.equals(button.getText(), confirmText) && Objects.equals(button.getCallbackData(), confirmData);
    }

    public static <T> List<KeyboardRow> createKeyboardRows(Collection<? extends T> objects,
                                                           Function<T, String> textExtractor,
                                                           int rowSize) {
        List<KeyboardButton> buttons = objects.stream()
                .map(object -> toKeyboardButton(object, textExtractor))
                .toList();

        List<KeyboardRow> rows = new ArrayList<>();
        int cntBt = 0;
        KeyboardRow row = new KeyboardRow();
        for (KeyboardButton button : buttons) {
            if (cntBt < rowSize) {
                row.add(button);
                cntBt++;
            } else {
                rows.add(row);
                row = new KeyboardRow(List.of(button));
                cntBt = 1;
            }
        }
        if (!row.isEmpty()) {
            rows.add(row);
        }

        return rows;
    }

    public static <T> List<List<InlineKeyboardButton>> createInlineKeyboardRows(Collection<? extends T> objects,
                                                                    Function<T, String> textExtractor,
                                                                    Function<T, String> idExtractor,
                                                                    int rowSize) {
        List<InlineKeyboardButton> buttons = objects.stream()
                .map(object -> TelegramKeyboardUtils.toInlineKeyboardButton(object, textExtractor, idExtractor))
                .toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int cntBt = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (InlineKeyboardButton button : buttons) {
            if (cntBt < rowSize) {
                row.add(button);
                cntBt++;
            } else {
                rows.add(row);
                row = new ArrayList<>();
                row.add(button);
                cntBt = 1;
            }
        }
        if (!row.isEmpty()) {
            rows.add(row);
        }

        return rows;
    }

    public static <T>KeyboardButton toKeyboardButton(T object, Function<T, String> textExtractor) {
        String text = textExtractor.apply(object);
        return KeyboardButton.builder().text(text).build();
    }

    public static <T> InlineKeyboardButton toInlineKeyboardButton(T object,
                                                                  Function<T, String> textExtractor,
                                                                  Function<T, String> idExtractor) {
        String text = textExtractor.apply(object);
        String id = idExtractor.apply(object);
        return InlineKeyboardButton.builder().text(text).callbackData(id).build();
    }

}
