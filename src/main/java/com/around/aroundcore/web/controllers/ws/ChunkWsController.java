package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Controller
public class ChunkWsController {
    private ThreadPoolTaskScheduler taskScheduler;
    private ChunkQueueService chunkQueueService;
    private GameChunkService gameChunkService;
    private SessionService sessionService;
    private ChunkEventTask chunkEventTask;
    public static final String CHUNK_CHANGES_FROM_USER = "/topic/chunk.changes";
    public static final String CHUNK_CHANGES_EVENT = "/topic/chunk.event";

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
    public void handleChunkChanges(@Payload ChunkDTO chunkDTO, Principal principal){
        GameUser user;

        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
        }catch (Exception e){
            log.error(e.getMessage());
            return;
        }

        GameChunk gameChunk;
        try {
            gameChunk = gameChunkService.findById(chunkDTO.getId());
            gameChunk.setOwner(user);
            gameChunkService.update(gameChunk);

        }catch (Exception e){
            gameChunk = GameChunk.builder()
                    .owner(user)
                    .id(chunkDTO.getId())
                    .build();

            gameChunkService.create(gameChunk);
        }
        chunkDTO.setTeam_id(user.getTeam().getId());

        /*
         * TODO: user skills on chunks
         *  skills:
         *  bomb
         *  ...
         */

        chunkQueueService.addToQueue(chunkDTO);
    }

}
