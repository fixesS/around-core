package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.jwt.JwtAuthenticationToken;
import com.around.aroundcore.security.jwt.JwtService;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.sql.Time;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

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
    public static final String FETCH_CHUNK_CHANGES_EVENT = "/topic/chunk.event";

    @PostConstruct
    public void executeSendingUpdates(){
        Duration duration = Duration.of(100, TimeUnit.MILLISECONDS.toChronoUnit());
        taskScheduler.scheduleWithFixedDelay(chunkEventTask, duration);
    }

    @SubscribeMapping(FETCH_CHUNK_CHANGES_EVENT)
    public void fetchChunkChangesEvent(){
        log.info("Got new subscription");
    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    public void handleChunkChanges(@Payload ChunkDTO chunkDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal){
        GameUser user = null;

        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            UUID sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            Session session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
        }catch (Exception e){
            log.error(e.getMessage());
            return;
        }

        GameChunk gameChunk = null;
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
