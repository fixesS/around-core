package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.services.RoundService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.user.GameUserDTO;
import com.around.aroundcore.web.dtos.user.GameUserLocationDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.services.GameUserLocationQueueService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class UserLocationController {
    private final SessionService sessionService;
    private final RoundService roundService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final GameUserLocationQueueService locationQueueService;

    public static final String LOCATION_FROM_USER = "/location";

    @MessageMapping(LOCATION_FROM_USER)
    @Transactional
    public void handleLocation(@Payload GameUserLocationDTO gameUserLocationDTO, Principal principal){
        GameUser user = null;
        Session session;

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        var sessionUuid = (UUID) jwtAuthenticationToken.getPrincipal();
        try {
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            roundService.getCurrentRound();
        }catch (ApiException e){
            log.error(e.getMessage());
            ApiError apiError = ApiResponse.getApiError(e.getResponse());
            if(user != null){
                messagingTemplate.convertAndSendToUser(user.getUsername(), WebSocketConfig.QUEUE_ERROR_FOR_USER, apiError);
            }
            return;
        }
        GameUserDTO gameUserDTO = gameUserDTOMapper.apply(user);
        user.getFriends().forEach(friend ->
            locationQueueService.addToQueue(GameUserLocationDTO.builder()
                .name(friend.getUsername())
                .longitude(gameUserLocationDTO.getLongitude())
                .latitude(gameUserLocationDTO.getLatitude())
                .gameUserDTO(gameUserDTO)
                .build())
        );
    }
}
