package org.quizstorage.components.telegram.event.handlers;

import org.quizstorage.components.telegram.event.events.CommandEvent;
import org.quizstorage.components.telegram.event.handlers.executors.CommandExecutor;
import org.quizstorage.exceptions.NotFoundExecutorException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandsHandler {

    private final ConcurrentMap<String, CommandExecutor> executorsMap;

    public CommandsHandler(Collection<? extends CommandExecutor> executors) {
        Map<String, CommandExecutor> map = executors.stream()
                .collect(Collectors.toMap(e -> e.supportedCommand().getCommand(), Function.identity()));
        executorsMap = new ConcurrentHashMap<>(map);
    }

    @EventListener(CommandEvent.class)
    public void handleCommand(CommandEvent event) {
        CommandExecutor executor = Optional.ofNullable(executorsMap.get(event.getCommand()))
                .orElseThrow(() -> new NotFoundExecutorException(event.getCommand()));
        executor.execute(event.getMessage());
    }

}
