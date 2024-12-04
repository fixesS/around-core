package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameSettings;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.GameSettingService;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.core.statemachine.GameStates;
import com.around.aroundcore.core.statemachine.RoundEvents;
import com.around.aroundcore.web.dtos.round.CreateRoundDTO;
import com.around.aroundcore.web.dtos.round.RoundDTO;
import com.around.aroundcore.web.mappers.RoundDTOMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_ADMIN)
public class AdminController {
    private final RoundService roundService;
    private final StateMachine<GameStates, RoundEvents> roundStateMachine;
    private final GameSettingService gameSettingService;
    private final RoundDTOMapper roundDTOMapper;

    @PostMapping("/game/activate")
    @Transactional
    public ResponseEntity<String> activateGameByRound(@RequestParam("round_id") Integer roundId) {
        Round round = roundService.getRoundById(roundId);
        Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.RESUME).setHeader("round", round).build();
        roundStateMachine.sendEvent(Mono.just(message)).subscribe();
        return ResponseEntity.ok("");
    }
    @PostMapping("/game/pause")
    @Transactional
    public ResponseEntity<String> pauseGame() {
        Round round = roundService.getCurrentRound();
        Message<RoundEvents> message = MessageBuilder.withPayload(RoundEvents.PAUSE).setHeader("round", round).build();
        roundStateMachine.sendEvent(Mono.just(message)).subscribe();
        return ResponseEntity.ok("");
    }
    @PutMapping("/round")
    @Transactional
    public ResponseEntity<RoundDTO> addNewRound(@Validated @RequestBody CreateRoundDTO createRoundDTO) {
        Round previousRound = roundService.getRoundById(createRoundDTO.getPrevious_round_id());
        GameSettings gameSettings;
        if(createRoundDTO.getSettings_id()!=null) {
            gameSettings = gameSettingService.findById(createRoundDTO.getSettings_id());
        }else{
            gameSettings = gameSettingService.findById(1);
        }
        Round newRound = Round.builder()
                .name(createRoundDTO.getName())
                .starts(createRoundDTO.getStarts())
                .ends(createRoundDTO.getEnds())
                .previousRound(previousRound)
                .gameSettings(gameSettings)
                .build();
        roundService.save(newRound);
        previousRound.setNextRound(newRound);
        roundService.save(previousRound);
        return ResponseEntity.ok(roundDTOMapper.apply(newRound));
    }
}
