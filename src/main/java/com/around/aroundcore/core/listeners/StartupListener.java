package com.around.aroundcore.core.listeners;

import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.core.statemachine.GameStates;
import com.around.aroundcore.core.statemachine.RoundEvents;
import com.around.aroundcore.core.exceptions.api.entity.NoActiveRoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
public class StartupListener {
    @Value("${around.time.locale}")
    private String timeLocale;
    private final StateMachine<GameStates, RoundEvents> stateMachine;
    private final RoundService roundService;


    @EventListener
    @Transactional
    public void onStartup(ApplicationReadyEvent event) {
        Round round = null;
        try{
            round = roundService.getCurrentRound();
            LocalDateTime now = new Date().toInstant().atZone(ZoneId.of(timeLocale)).toLocalDateTime();
            if(now.isBefore(round.getEnds())){
                log.debug("Current round is active startup.");
                Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.STARTUP_ACTIVATE).build();
                stateMachine.sendEvent(Mono.just(message)).subscribe();
            }else {
                log.debug("Current round is ended on startup.");
                Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.STARTUP_END).setHeader("round", round).build();
                stateMachine.sendEvent(Mono.just(message)).subscribe();
            }
        }catch (NoActiveRoundException e){
            log.error("There is no active on startup.");
            Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.STARTUP_PAUSE).build();
            stateMachine.sendEvent(Mono.just(message)).subscribe();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
