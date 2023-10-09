package org.quizstorage.components.satemachine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.quizstorage.objects.InitFieldValueHolder;
import org.quizstorage.objects.InitFieldsContainer;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccessor;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class QuizStateMachine implements StateMachine<QuizUserState, QuizUserEvent> {

    private static final String SOURCE_LIST_KEY = "sourcesList";

    private static final String INIT_FIELDS_KEY = "initFields";

    @Getter
    private final StateMachine<QuizUserState, QuizUserEvent> delegate;

    @Override
    public State<QuizUserState, QuizUserEvent> getInitialState() {
        return delegate.getInitialState();
    }

    @Override
    public ExtendedState getExtendedState() {
        return delegate.getExtendedState();
    }

    @Override
    public StateMachineAccessor<QuizUserState, QuizUserEvent> getStateMachineAccessor() {
        return delegate.getStateMachineAccessor();
    }

    @Override
    public void setStateMachineError(Exception exception) {
        delegate.setStateMachineError(exception);
    }

    @Override
    public boolean hasStateMachineError() {
        return delegate.hasStateMachineError();
    }

    @Override
    public UUID getUuid() {
        return delegate.getUuid();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public boolean sendEvent(Message<QuizUserEvent> event) {
        return delegate.sendEvent(event);
    }

    @Override
    public boolean sendEvent(QuizUserEvent event) {
        return delegate.sendEvent(event);
    }

    @Override
    public State<QuizUserState, QuizUserEvent> getState() {
        return delegate.getState();
    }

    @Override
    public Collection<State<QuizUserState, QuizUserEvent>> getStates() {
        return delegate.getStates();
    }

    @Override
    public Collection<Transition<QuizUserState, QuizUserEvent>> getTransitions() {
        return delegate.getTransitions();
    }

    @Override
    public boolean isComplete() {
        return delegate.isComplete();
    }

    @Override
    public void addStateListener(StateMachineListener<QuizUserState, QuizUserEvent> listener) {
        delegate.addStateListener(listener);
    }

    @Override
    public void removeStateListener(StateMachineListener<QuizUserState, QuizUserEvent> listener) {
        delegate.removeStateListener(listener);
    }

    public void setSourcesList(List<QuizSourceDto> sourcesList) {
        this.delegate.getExtendedState().getVariables().put(SOURCE_LIST_KEY, sourcesList);
    }

    public void setInitFieldsContainer(InitFieldsContainer initFieldsContainer) {
        this.delegate.getExtendedState().getVariables().put(INIT_FIELDS_KEY, initFieldsContainer);
    }

    @SuppressWarnings("unchecked")
    public Optional<List<QuizSourceDto>> getSourcesList() {
        List<QuizSourceDto> list = (List<QuizSourceDto>) this.delegate
                .getExtendedState().getVariables().get(SOURCE_LIST_KEY);
        return Optional.ofNullable(list);
    }

    public Optional<InitFieldsContainer> getInitFieldsContainer() {
        InitFieldsContainer container = (InitFieldsContainer) this.delegate
                .getExtendedState().getVariables().get(INIT_FIELDS_KEY);
        return Optional.ofNullable(container);
    }

    public Optional<QuizSourceDto> findSource(Predicate<QuizSourceDto> predicate) {
        return getSourcesList()
                .stream()
                .flatMap(Collection::stream)
                .filter(predicate)
                .findFirst();
    }

    public Optional<Pair<InitFieldsContainer, InitFieldValueHolder>> getCurrentValueHolder() {
        Optional<InitFieldsContainer> initFieldsContainer = getInitFieldsContainer();
        if (initFieldsContainer.isEmpty()) {
            return Optional.empty();
        }
        return initFieldsContainer.flatMap(this::getCurrentValueHolder);
    }

    private Optional<Pair<InitFieldsContainer, InitFieldValueHolder>> getCurrentValueHolder(
            InitFieldsContainer container) {
        return container.getCurrentInitFieldValueHolder()
                .map(valueHolder -> new ImmutablePair<>(container, valueHolder));
    }

}
