package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.exceptions.InitFieldBotApiMethodFactoryNotFoundException;
import org.quizstorage.generator.dto.FieldType;
import org.quizstorage.generator.dto.InitField;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
public class InitFieldBotApiMethodManager implements InitFieldBotApiMethodFactory {

    private final Map<FieldType, InitFieldBotApiMethodFactory> factoryMap;

    @Override
    public BotApiMethod<? extends Serializable> create(Long userId, InitField<?> initField) {
        InitFieldBotApiMethodFactory factory = factoryMap.get(initField.type());
        if (factory == null) {
            throw new InitFieldBotApiMethodFactoryNotFoundException(initField.type());
        }
        return factory.create(userId, initField);
    }

}
