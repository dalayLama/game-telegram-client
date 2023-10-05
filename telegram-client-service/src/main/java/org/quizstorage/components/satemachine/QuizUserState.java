package org.quizstorage.components.satemachine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuizUserState {

    NEW_GAME_AWAIT(0),
    SELECTING_SOURCE(1),
    SETTING_INIT_DATA(2),
    PLAY_GAME(3),
    GAME_FINISHED(4);

    private final int order;

    /**
     * return true if given state is ahead
     * @param state given state
     * @return true or false
     */
    public boolean isAhead(QuizUserState state) {
        return this.getOrder() < state.getOrder();
    }

    /**
     * return true if given state is behind
     * @param state given state
     * @return true or false
     */
    public boolean isBehind(QuizUserState state) {
        return this.getOrder() > state.getOrder();
    }

}
