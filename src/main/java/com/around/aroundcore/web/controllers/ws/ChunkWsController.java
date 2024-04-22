package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.database.models.Skills;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.SkillService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.entity.SkillNullException;
import com.around.aroundcore.web.services.ChunkQueueService;
import com.around.aroundcore.web.services.H3ChunkService;
import com.around.aroundcore.web.tasks.ChunkEventTask;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Duration;
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
    private final SkillService skillService;
    private final ChunkEventTask chunkEventTask;
    private final H3ChunkService h3ChunkService;
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
        GameUserSkill userWidthSkill;

        try {// getting user
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
        }catch (Exception e){
            log.error(e.getMessage());
            return;
        }

        try{// getting width userskill
            userWidthSkill = user.getUserSkills().stream().filter(gameUserSkill -> gameUserSkill.getSkillId().equals(Skills.WIDTH.getId()))
                    .findAny().orElseThrow(SkillNullException::new);
        }catch (SkillNullException e){
            log.error(e.getMessage());
            return;
        }
        // getting neighbours for width userskill level
        List<ChunkDTO> chunksDTOList = h3ChunkService.getChunksForWidthSkill(chunkDTO.getId(),userWidthSkill);

        gameChunkService.saveListOfChunkDTOs(chunksDTOList, user);

        chunkQueueService.addToQueue(chunksDTOList);
    }

}
