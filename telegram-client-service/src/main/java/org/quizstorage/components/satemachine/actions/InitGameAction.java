package org.quizstorage.components.satemachine.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.services.GameService;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class InitGameAction implements Action<QuizUserState, QuizUserEvent> {

    private final GameService gameService;

    @Override
    public void execute(StateContext<QuizUserState, QuizUserEvent> context) {
        getUserId(context)
                .ifPresentOrElse(
                        gameService::newGame,
                        () -> log.error("User id hasn't been found in context")
                );
    }
    
    private Optional<Long> getUserId(StateContext<QuizUserState, QuizUserEvent> context) {
        try {
            Long userId = context.getExtendedState().get("userId", Long.class);
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
