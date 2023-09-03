package org.quizstorage.components.telegram.event.handlers.executors;

import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.telegram.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class NewGameExecutor implements CommandExecutor {

    @Override
    public void execute(Message message) {
        log.info("receive command to create a new game");
    }

    @Override
    public Command supportedCommand() {
        return Command.NEW_GAME;
    }

}
