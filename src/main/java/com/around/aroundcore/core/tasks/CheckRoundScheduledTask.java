package com.around.aroundcore.core.tasks;

import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.core.statemachine.GameStates;
import com.around.aroundcore.core.statemachine.RoundEvents;
import com.around.aroundcore.core.exceptions.api.entity.NoActiveRoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckRoundScheduledTask {
    @Value("${around.time.locale}")
    private String timeLocale;
    private final RoundService roundService;
    private final StateMachine<GameStates, RoundEvents> stateMachine;

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void checkCurrentRound(){
        if(!stateMachine.getState().getId().equals(GameStates.ACTIVE)){
            return;
        }
        try{
            Round round = roundService.getCurrentRound();
            LocalDateTime now = new Date().toInstant().atZone(ZoneId.of(timeLocale)).toLocalDateTime();
            if(now.isAfter(round.getEnds())){
                log.debug("Round ("+round.getId()+") ended!");
                Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.END).setHeader("round", round).build();
                stateMachine.sendEvent(Mono.just(message)).subscribe();
            }
        }catch (NoActiveRoundException ignored){
            log.error("No active round");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
