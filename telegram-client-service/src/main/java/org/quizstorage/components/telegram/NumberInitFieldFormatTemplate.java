package org.quizstorage.components.telegram;

import org.quizstorage.generator.dto.NumberFormat;

public interface NumberInitFieldFormatTemplate {

    String format(Long userId, NumberFormat format);

}
