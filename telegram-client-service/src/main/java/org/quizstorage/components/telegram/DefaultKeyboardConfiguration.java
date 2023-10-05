package org.quizstorage.components.telegram;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DefaultKeyboardConfiguration implements KeyboardConfiguration {

    @Override
    public String getSelectConfirmationData() {
        return "confirm";
    }

    @Override
    public String getSkipData() {
        return "skip";
    }

    @Override
    public String getSelectMarker() {
        return "selected";
    }

    @Override
    public String getSelectConfirmationText(Locale locale) {
        return "Confirm";
    }

    @Override
    public String getSelectConfirmationText(String languageCode) {
        return "Confirm";
    }

    @Override
    public String getSkipText(Locale locale) {
        return "Skip";
    }

    @Override
    public String getSkipText(String languageCode) {
        return "Skip";
    }

}
