package com.around.aroundcore.core.statemachine;

import com.around.aroundcore.core.exceptions.NoVictoryContenders;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.database.services.TeamService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@WithStateMachine
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoundStateActions {
    @Resource
    private final StateMachine<GameStates, RoundEvents> stateMachine;
    private final RoundService roundService;
    @Autowired
    private TeamService teamService;

    @OnStateChanged(source = {"ENDED","PAUSED"}, target = "ACTIVE")
    public void activateRound(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get("round");
        round.setActive(true);
        roundService.save(round);
    }
    @OnStateChanged(source = "ACTIVE", target = "PAUSED")
    public void pauseRound(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get("round");
        round.setActive(false);
        roundService.save(round);
    }
    @OnStateChanged(target = "ENDED")
    public void endedRoundFromStartup(Message<RoundEvents> message) {
        Round round = (Round) message.getHeaders().get("round");
        round.setActive(false);
        roundService.save(round);

        //reward winner team
        try{
            log.debug("Finding victory contenders...");
            List<Team> teams = teamService.getVictoryContendersForRound(round);
            log.debug("Rewarding victory contenders...");

            teamService.rewardTeamsForRound(teams, round);
        }catch (NoVictoryContenders ignored){

        }

        if(round.getNextRound() != null) {
            log.debug("Next round exist");

            Message<RoundEvents> newMessage  = MessageBuilder.withPayload(RoundEvents.ACTIVATE).setHeader("round", round.getNextRound()).build();
            stateMachine.sendEvent(Mono.just(newMessage)).subscribe();
        }else{
            log.debug("Next round does not exist");

            Message<RoundEvents> newMessage  = MessageBuilder.withPayload(RoundEvents.PAUSE).setHeader("round", round.getNextRound()).build();
            stateMachine.sendEvent(Mono.just(newMessage)).subscribe();
        }
    }
    @OnTransition
    public void onTransition(StateContext<GameStates, RoundEvents> stateContext) {
        GameStates sourceState = stateContext.getSource().getId();
        GameStates targetState = stateContext.getTarget().getId();
        log.info("State {} -> {}", sourceState, targetState);
    }
}
