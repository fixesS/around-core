package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.user.GameUserSkill;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.enums.Skills;
import com.around.aroundcore.core.exceptions.api.ApiException;
import com.around.aroundcore.core.services.queues.ChunkQueueService;
import com.around.aroundcore.core.services.H3ChunkService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
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
    private final RoundService roundService;

//    deleted because spring did not call this due /topic (only if /topic sat as application prefix in config)
//    todo decide what to do with this method
//    @SubscribeMapping(CHUNK_CHANGES_EVENT)
//    public void fetchChunkChangesEvent(Principal principal){
//        GameUser user = null;
//        try {
//            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
//            var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
//            var session = sessionService.findByUuid(sessionUuid);
//            user = session.getUser();
//            log.info("Fetching chunk changes");
//            log.error(String.valueOf(user));
//            user.checkTeamForCurrentRoundAndAnyCity();
//        }catch (ApiException e){
//            log.error(e.getClass().getName());
//            if(user != null){
//                ApiError apiError = ApiResponse.getApiError(e.getResponse());
//                messagingTemplate.convertAndSendToUser(user.getId().toString(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
//            }
//        }
//    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    @Transactional
    public void handleChunkChanges(@Payload ChunkDTO chunkDTO, Principal principal){
        GameUser user = null;
        Session session;
        GameUserSkill userWidthSkill;
        Round round;
        List<City> cities;

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
        try {
            roundService.getCurrentRound();//check if game is active
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            userWidthSkill = user.getUserSkillBySkillId(Skills.WIDTH.getId());
            user.checkTeamForCurrentRoundAndAnyCity();
            round = user.getUserRoundTeamCities().get(0).getRound();
            cities = UserRoundTeamCity.findCitiesForUser(user);
        }catch (ApiException e){
            log.error(e.getClass().getName());
            if(user != null){
                ApiError apiError = ApiResponse.getApiError(e.getResponse());
                messagingTemplate.convertAndSendToUser(user.getId().toString(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            }
            return;
        }

        //find city by chunk
        Optional<City> cityOptional = cities.stream()
                .filter(city -> city.containsChunkDTO(h3ChunkService.getParentId(chunkDTO.getId(), city.getChunksResolution())))
                .findFirst();
        if (cityOptional.isEmpty()) {
            ApiError apiError = ApiResponse.getApiError(ApiResponse.CHUNK_DOES_NOT_CORRELATE_WITH_USER_CITY);
            messagingTemplate.convertAndSendToUser(user.getId().toString(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            return;
        }
        City city = cityOptional.get();


        // getting neighbours for width userskill level
        List<ChunkDTO> chunksDTOList = h3ChunkService.getChunksForWidthSkill(chunkDTO.getId(),userWidthSkill);
        // filtering for chunks by city (remove chunks that out of the city)

        chunksDTOList = chunksDTOList.stream()
                .filter(chunk -> city.containsChunkDTO(h3ChunkService.getParentId(chunk.getId(),city.getChunksResolution())))
                .toList();
        gameChunkService.saveListOfChunkDTOs(chunksDTOList, user, round, city);// adding chunks
        userService.increaseCapturedChunksToUserInRoundInCity(user,round,city,chunksDTOList.size());
        user.addCoins(chunksDTOList.size()*round.getGameSettings().getChunkReward());//add reward for each chunk to coins
        chunksDTOList.forEach(chunkDTO1 -> chunkDTO1.setRound_id(round.getId()));// setting round for correct output from chunk queue

        //getting visited events (NOW) for user (in DB they are not visited yet)
        List<MapEvent> visitedEvents = mapEventService.findVerifiedActiveByChunksAndNotVisitedByUser(List.of(chunkDTO.getId()),user.getId());
        user.addVisitedEvents(visitedEvents);
        user.addCoins(visitedEvents.stream().mapToInt(MapEvent::getReward).sum());//add sum of rewards to coins
        userService.update(user);

        chunkQueueService.addToQueue(chunksDTOList);
    }
}