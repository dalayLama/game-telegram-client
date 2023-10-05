package org.quizstorage.components.satemachine.handlers;

import org.quizstorage.components.satemachine.QuizUserState;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MapStateHandlerResolver implements StateHandlerResolver {

    private final Map<QuizUserState, StateHandler> handlersMap;

    public MapStateHandlerResolver(Collection<? extends StateHandler> handlers) {
        Map<QuizUserState, StateHandler> map = handlers.stream()
                .collect(Collectors.toMap(StateHandler::handlingState, Function.identity()));
        handlersMap = new HashMap<>(map);
    }

    @Override
    public Optional<StateHandler> resolveByState(QuizUserState state) {
        return Optional.ofNullable(handlersMap.get(state));
    }

}
