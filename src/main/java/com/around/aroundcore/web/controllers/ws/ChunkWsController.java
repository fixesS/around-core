package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.enums.Skills;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.services.H3ChunkService;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Controller
public class ChunkWsController {
    private final ThreadPoolTaskScheduler taskScheduler;
    private final ChunkQueueService chunkQueueService;
    private final GameChunkService gameChunkService;
    private final SessionService sessionService;
    private final MapEventService mapEventService;
    private final GameUserService userService;
    private final ChunkEventTask chunkEventTask;
    private final RoundService roundService;
    private final H3ChunkService h3ChunkService;
    private final SimpMessagingTemplate messagingTemplate;
    public static final String CHUNK_CHANGES_FROM_USER = "/topic/chunk.changes";
    public static final String CHUNK_CHANGES_EVENT = "/topic/chunk.event";
    public static final String QUEUE_ERROR_FOR_SESSION = "/exchange/private.message/error";

    @PostConstruct
    public void executeSendingUpdates(){
        Duration duration = Duration.of(100, TimeUnit.MILLISECONDS.toChronoUnit());
        taskScheduler.scheduleWithFixedDelay(chunkEventTask, duration);
    }

    @SubscribeMapping(CHUNK_CHANGES_EVENT)
    public void fetchChunkChangesEvent(Principal principal){
        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            log.info("Got new subscription from: {}",session.getUser().getEmail());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    @Transactional
    public void handleChunkChanges(@Payload ChunkDTO chunkDTO, Principal principal){
        GameUser user;
        Session session;
        GameUserSkill userWidthSkill;
        Round round;

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
        try {// getting user
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            userWidthSkill = user.getUserSkillBySkillId(Skills.WIDTH.getId());
            roundService.getCurrentRound();
            round = UserRoundTeam.findCurrentRoundFromURTs(user.getUserRoundTeams());
        }catch (ApiException e){
            log.error(e.getMessage());
            ApiError apiError = ApiResponse.getApiError(e.getResponse());
            messagingTemplate.convertAndSendToUser(sessionUuid.toString(), QUEUE_ERROR_FOR_SESSION, apiError);
            return;
        }

        // getting neighbours for width userskill level
        List<ChunkDTO> chunksDTOList = h3ChunkService.getChunksForWidthSkill(chunkDTO.getId(),userWidthSkill);
        gameChunkService.saveListOfChunkDTOs(chunksDTOList, user, round);// adding
        chunksDTOList.forEach(chunkDTO1 -> chunkDTO1.setRound_id(round.getId()));

        //getting user visited events from verified events on map
        List<MapEvent> visitedEvents = getVisitedByUserAndChunk(user, gameChunkService.findByIdAndRoundId(chunkDTO.getId(), round.getId()));
        user.addVisitedEvents(visitedEvents);
        userService.update(user);

        chunkQueueService.addToQueue(chunksDTOList);
    }
    private List<MapEvent> getVisitedByUserAndChunk(GameUser user, GameChunk gameChunk){
        List<MapEvent> visitedEvents = new ArrayList<>();
        List<MapEvent> eventsOnMap = mapEventService.findAllVerified();
        eventsOnMap.forEach(event -> {
            if (!user.getVisitedEvents().contains(event) && event.getChunks().contains(gameChunk)){
                visitedEvents.add(event);
            }
        });
        return visitedEvents;
    }
}
