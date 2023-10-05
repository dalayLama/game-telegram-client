package org.quizstorage.components.telegram.commands.executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.telegram.Command;
import org.quizstorage.services.GameService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewGameExecutor implements CommandExecutor {

    private final GameService gameService;

    @Override
    public void execute(Message message) {
        log.info("receive command to create a new game");
        gameService.newGame(message.getFrom().getId());
    }

    @Override
    public Command supportedCommand() {
        return Command.NEW_GAME;
    }

}
