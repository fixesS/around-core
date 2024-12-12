package com.around.aroundcore.core.statemachine;

import com.around.aroundcore.config.RoundStateMachine;
import com.around.aroundcore.core.events.OnActivateRoundEvent;
import com.around.aroundcore.core.events.OnDeactivateRoundEvent;
import com.around.aroundcore.core.events.OnTeamRewardingEvent;
import com.around.aroundcore.core.services.TeamRewardingService;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.round.RoundService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@WithStateMachine
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoundStateActions {
    @Resource
    private final StateMachine<GameStates, RoundEvents> stateMachine;
    private final ApplicationEventPublisher eventPublisher;

    @OnStateChanged(source = {"ENDED","PAUSED"}, target = "ACTIVE")
    public void activateRound(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get(RoundStateMachine.ROUND_HEADER);
        eventPublisher.publishEvent(new OnActivateRoundEvent(round));
    }
    @OnStateChanged(source = "ACTIVE", target = "PAUSED")
    public void pauseRound(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get(RoundStateMachine.ROUND_HEADER);
        eventPublisher.publishEvent(new OnDeactivateRoundEvent(round));
    }
    @OnStateChanged(target = "ENDED")
    public void endedRoundFromStartup(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get(RoundStateMachine.ROUND_HEADER );
        eventPublisher.publishEvent(new OnDeactivateRoundEvent(round));
        eventPublisher.publishEvent(new OnTeamRewardingEvent(round));

        if(round.getNextRound() != null) {
            log.debug("Next round exist");

            Message<RoundEvents> newMessage  = MessageBuilder.withPayload(RoundEvents.ACTIVATE)
                    .setHeader(RoundStateMachine.ROUND_HEADER, round.getNextRound()).build();
            stateMachine.sendEvent(Mono.just(newMessage)).subscribe();
        }else{
            log.debug("Next round does not exist");

            Message<RoundEvents> newMessage  = MessageBuilder.withPayload(RoundEvents.PAUSE)
                    .setHeader(RoundStateMachine.ROUND_HEADER, round.getNextRound()).build();
            stateMachine.sendEvent(Mono.just(newMessage)).subscribe();
        }
    }
    @OnTransition
    public void onTransition(StateContext<GameStates, RoundEvents> stateContext) {
        GameStates sourceState = stateContext.getSource().getId();
        GameStates targetState = stateContext.getTarget().getId();
        log.info("Game state {} -> {}", sourceState, targetState);
    }
}
