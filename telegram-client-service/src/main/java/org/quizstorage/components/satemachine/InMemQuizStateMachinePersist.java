package org.quizstorage.components.satemachine;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemQuizStateMachinePersist implements StateMachinePersist<QuizUserState, QuizUserEvent, Long> {

    private final ConcurrentMap<Long, StateMachineContext<QuizUserState, QuizUserEvent>> store =
            new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<QuizUserState, QuizUserEvent> context, Long contextObj) throws Exception {
        store.put(contextObj, context);
    }

    @Override
    public StateMachineContext<QuizUserState, QuizUserEvent> read(Long contextObj) throws Exception {
        return store.get(contextObj);
    }
}
