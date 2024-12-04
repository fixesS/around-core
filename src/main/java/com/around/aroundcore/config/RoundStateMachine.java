package com.around.aroundcore.config;

import com.around.aroundcore.core.statemachine.GameStates;
import com.around.aroundcore.core.statemachine.RoundEvents;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Set;

@EnableStateMachineFactory
@Configuration
public class RoundStateMachine extends StateMachineConfigurerAdapter<GameStates, RoundEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<GameStates, RoundEvents> states) throws Exception {
        states.withStates()
                .initial(GameStates.STARTUP)
                .states(Set.of(GameStates.ACTIVE,GameStates.ENDED,GameStates.PAUSED))
                .end(GameStates.STOPPED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<GameStates, RoundEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(GameStates.STARTUP).target(GameStates.ACTIVE).event(RoundEvents.STARTUP_ACTIVATE)
                .and()
                .withExternal()
                .source(GameStates.STARTUP).target(GameStates.PAUSED).event(RoundEvents.STARTUP_PAUSE)
                .and()
                .withExternal()
                .source(GameStates.STARTUP).target(GameStates.ENDED).event(RoundEvents.STARTUP_END)
                .and()
                .withExternal()
                .source(GameStates.ACTIVE).target(GameStates.PAUSED).event(RoundEvents.PAUSE)
                .and()
                .withExternal()
                .source(GameStates.ACTIVE).target(GameStates.ENDED).event(RoundEvents.END)
                .and()
                .withExternal()
                .source(GameStates.PAUSED).target(GameStates.ACTIVE).event(RoundEvents.RESUME)
                .and()
                .withExternal()
                .source(GameStates.ENDED).target(GameStates.ACTIVE).event(RoundEvents.ACTIVATE)
                .and()
                .withExternal()
                .source(GameStates.ENDED).target(GameStates.PAUSED).event(RoundEvents.PAUSE);
    }
}
