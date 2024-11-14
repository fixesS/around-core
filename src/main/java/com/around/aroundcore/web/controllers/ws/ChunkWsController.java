package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.enums.Skills;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.services.queues.ChunkQueueService;
import com.around.aroundcore.web.services.H3ChunkService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class ChunkWsController {
    private final ChunkQueueService chunkQueueService;
    private final GameChunkService gameChunkService;
    private final SessionService sessionService;
    private final MapEventService mapEventService;
    private final GameUserService userService;
    private final H3ChunkService h3ChunkService;
    private final SimpMessagingTemplate messagingTemplate;
    public static final String CHUNK_CHANGES_FROM_USER = "/chunk.changes";
    public static final String CHUNK_CHANGES_EVENT = "/topic/chunk.event";

    @SubscribeMapping(CHUNK_CHANGES_EVENT)
    public void fetchChunkChangesEvent(Principal principal){
        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            log.debug("Got subscription from: {}",session.getUser().getEmail());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    @Transactional
    public void handleChunkChanges(@Payload ChunkDTO chunkDTO, Principal principal){
        GameUser user = null;
        Session session;
        GameUserSkill userWidthSkill;
        Round round;
        City city;

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
        try {
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            userWidthSkill = user.getUserSkillBySkillId(Skills.WIDTH.getId());
            round = UserRoundTeamCity.findCurrentRoundFromURTs(user.getUserRoundTeamCities());
            city = UserRoundTeamCity.findCityForCurrentRoundAndUser(user);
        }catch (ApiException e){
            log.error(e.getMessage());
            if(user != null){
                ApiError apiError = ApiResponse.getApiError(e.getResponse());
                messagingTemplate.convertAndSendToUser(user.getUsername(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            }
            return;
        }

        if(!city.containsChunkDTO(h3ChunkService.getParentId(chunkDTO.getId(),city.getChunksResolution()))){// if chunk is not in user city
            ApiError apiError = ApiResponse.getApiError(ApiResponse.CHUNK_DOES_NOT_CORRELATES_WITH_USER_CITY);
            messagingTemplate.convertAndSendToUser(user.getUsername(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            return;
        }

        List<ChunkDTO> chunksDTOList = h3ChunkService.getChunksForWidthSkill(chunkDTO.getId(),userWidthSkill); // getting neighbours for width userskill level
        chunksDTOList = chunksDTOList.stream().filter(chunk -> city.containsChunkDTO(h3ChunkService.getParentId(chunk.getId(),city.getChunksResolution()))).toList(); // filtering for chunks by city
        gameChunkService.saveListOfChunkDTOs(chunksDTOList, user, round, city);// adding chunks
        user.addCapturedChunks(chunksDTOList.size()); // increase captured chunks value
        chunksDTOList.forEach(chunkDTO1 -> chunkDTO1.setRound_id(round.getId()));

        //getting visited events (NOW) for user (in DB they are not visited yet)
        List<MapEvent> visitedEvents = mapEventService.findVerifiedActiveByChunksAndNotVisitedByUser(List.of(chunkDTO.getId()),user.getId());
        user.addVisitedEvents(visitedEvents);
        user.addCoins(visitedEvents.stream().mapToInt(MapEvent::getReward).sum());//sum of rewards
        userService.update(user);

        chunkQueueService.addToQueue(chunksDTOList);
    }
}