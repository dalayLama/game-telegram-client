package org.quizstorage.components.telegram;

import java.util.Locale;

public interface KeyboardConfiguration {

    String getSelectConfirmationData();

    String getSkipText(String languageCode);

    String getSkipData();

    String getSelectMarker();

    String getSelectConfirmationText(Locale locale);

    String getSelectConfirmationText(String languageCode);

    String getSkipText(Locale locale);

}
