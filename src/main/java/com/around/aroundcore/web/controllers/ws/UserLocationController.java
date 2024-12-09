package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.database.services.round.RoundService;
import com.around.aroundcore.database.services.user.GameUserService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.user.GameUserDTO;
import com.around.aroundcore.web.dtos.user.GameUserLocationDTO;
import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;
import com.around.aroundcore.core.exceptions.api.entity.GameChunkNullException;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.core.services.H3ChunkService;
import com.around.aroundcore.core.services.queues.GameUserLocationQueueService;
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
public class UserLocationController {
    public static final String LOCATION_FROM_USER = "/location";
    private final SessionService sessionService;
    private final RoundService roundService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final GameUserLocationQueueService locationQueueService;
    private final H3ChunkService h3ChunkService;
    private final CityService cityService;
    private final GameUserService userService;
    private final TeamService teamService;

    @MessageMapping(LOCATION_FROM_USER)
    @Transactional
    public void handleLocation(@Payload GameUserLocationDTO gameUserLocationDTO, Principal principal){
        GameUser user = null;
        Round round;
        Session session;
        List<City> cities;
        ChunkDTO chunkDTO;

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
        try {
            round = roundService.getCurrentRound();//check if game is active
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            cities = UserRoundTeamCity.findCitiesForUser(user);
            chunkDTO =  h3ChunkService.getChunkByLatLon(gameUserLocationDTO.getLatitude(),gameUserLocationDTO.getLongitude(),0).stream()
                    .findFirst().orElseThrow(GameChunkNullException::new);
        }catch (ApiException e){
            log.error(e.getMessage());
            ApiError apiError = ApiResponse.getApiError(e.getResponse());
            if(user != null){
                messagingTemplate.convertAndSendToUser(user.getId().toString(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            }
            return;
        }

        Optional<City> cityOptional = cityService.findAll().stream()
                .filter(city -> city.containsChunkDTO(h3ChunkService.getParentId(chunkDTO.getId(), city.getChunksResolution())))
                .findFirst();
        if(cityOptional.isEmpty()){
            ApiError apiError = ApiResponse.getApiError(ApiResponse.CHUNK_DOES_NOT_CORRELATE_WITH_ANY_CITY);
            messagingTemplate.convertAndSendToUser(user.getId().toString(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            return;
        }

        Optional<City> cityOptionalUser = cities.stream()
                .filter(city -> city.containsChunkDTO(h3ChunkService.getParentId(chunkDTO.getId(), city.getChunksResolution())))
                .findFirst();
        if (cityOptionalUser.isEmpty()) {// todo make algorithm for balancing teams, instead of full random team
            City newCityForUser = cityOptional.get();
            userService.setTeamForRoundAndCity(user,round,newCityForUser, teamService.getRandomTeam());
        }

        GameUserDTO gameUserDTO = gameUserDTOMapper.apply(user);
        user.getFriends().forEach(friend ->
            locationQueueService.addToQueue(GameUserLocationDTO.builder()
                .friendId(friend.getId())
                .longitude(gameUserLocationDTO.getLongitude())
                .latitude(gameUserLocationDTO.getLatitude())
                .gameUserDTO(gameUserDTO)
                .build())
        );
    }
}
