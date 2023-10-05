package org.quizstorage.components.event.handlers;

import org.quizstorage.annotations.CatchException;
import org.quizstorage.annotations.WrapUserResolvableException;
import org.quizstorage.components.event.events.CommandEvent;
import org.quizstorage.components.telegram.commands.executors.CommandExecutor;
import org.quizstorage.exceptions.NotFoundExecutorException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandEventHandler {

    private final Map<String, CommandExecutor> executorsMap;

    public CommandEventHandler(Collection<? extends CommandExecutor> executors) {
        Map<String, CommandExecutor> map = executors.stream()
                .collect(Collectors.toMap(e -> e.supportedCommand().getCommand(), Function.identity()));
        executorsMap = new ConcurrentHashMap<>(map);
    }

    @EventListener(CommandEvent.class)
    @WrapUserResolvableException
    @CatchException
    public void handleCommand(CommandEvent event) {
        CommandExecutor executor = Optional.ofNullable(executorsMap.get(event.getCommand()))
                .orElseThrow(() -> new NotFoundExecutorException(event.getUpdate(), event.getCommand()));
        executor.execute(event.getMessage());
    }

}
