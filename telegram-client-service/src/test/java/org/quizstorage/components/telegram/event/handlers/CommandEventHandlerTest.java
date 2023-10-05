package org.quizstorage.components.telegram.event.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quizstorage.components.event.handlers.CommandEventHandler;
import org.quizstorage.components.telegram.Command;
import org.quizstorage.components.event.events.CommandEvent;
import org.quizstorage.components.telegram.commands.executors.CommandExecutor;
import org.quizstorage.exceptions.NotFoundExecutorException;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CommandEventHandlerTest {

    private final Map<Command, CommandExecutor> executorMap = Arrays.stream(Command.values())
            .collect(Collectors.toMap(Function.identity(), command -> mock(CommandExecutor.class)));

    private CommandEventHandler handler;


    @BeforeEach
    void setUp() {
        executorMap.forEach((key, value) -> given(value.supportedCommand()).willReturn(key));
        handler = new CommandEventHandler(executorMap.values());
    }

    @ParameterizedTest
    @EnumSource(Command.class)
    void shouldCallExecutor(Command command) {
        CommandEvent commandEvent = mock(CommandEvent.class);
        Message message = mock(Message.class);

        given(commandEvent.getCommand()).willReturn(command.getCommand());
        given(commandEvent.getMessage()).willReturn(message);


        handler.handleCommand(commandEvent);

        CommandExecutor executor = executorMap.get(command);
        then(executor).should().execute(message);
        executorMap.values().stream()
                .filter(e -> e != executor)
                .forEach(e -> then(e).should(never()).execute(any()));
    }

    @Test
    void shouldThrowNotFoundException() {
        CommandEvent commandEvent = mock(CommandEvent.class);

        String notExistedCommand = "/not_existed_command";
        given(commandEvent.getCommand()).willReturn(notExistedCommand);

        assertThatThrownBy(() -> handler.handleCommand(commandEvent)).isInstanceOf(NotFoundExecutorException.class);
    }

}