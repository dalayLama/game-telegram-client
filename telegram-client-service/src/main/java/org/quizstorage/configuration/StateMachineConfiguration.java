package org.quizstorage.configuration;

import org.quizstorage.components.satemachine.InMemQuizStateMachinePersist;
import org.quizstorage.components.satemachine.QuizStateMachineListener;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<QuizUserState, QuizUserEvent> {


    @Override
    public void configure(final StateMachineConfigurationConfigurer<QuizUserState, QuizUserEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new QuizStateMachineListener());
    }


    @Override
    public void configure(StateMachineStateConfigurer<QuizUserState, QuizUserEvent> states) throws Exception {
        states
                .withStates()
                .initial(QuizUserState.NEW_GAME_AWAIT)
                .end(QuizUserState.GAME_FINISHED)
                .states(EnumSet.allOf(QuizUserState.class));
    }

    public StateMachineConfiguration() {
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<QuizUserState, QuizUserEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(QuizUserState.NEW_GAME_AWAIT).target(QuizUserState.SELECTING_SOURCE)
                    .event(QuizUserEvent.SELECT_SOURCE)
                .and().withExternal()
                    .source(QuizUserState.SELECTING_SOURCE).target(QuizUserState.SETTING_INIT_DATA)
                    .event(QuizUserEvent.FILL_INIT_DATA)
                .and().withExternal()
                    .source(QuizUserState.SETTING_INIT_DATA).target(QuizUserState.PLAY_GAME)
                    .event(QuizUserEvent.START_GAME)
                .and().withExternal()
                    .source(QuizUserState.PLAY_GAME).target(QuizUserState.GAME_FINISHED)
                    .event(QuizUserEvent.FINISH_GAME);
    }

    @Bean
    public StateMachinePersister<QuizUserState, QuizUserEvent, Long> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new InMemQuizStateMachinePersist());
    }


}
