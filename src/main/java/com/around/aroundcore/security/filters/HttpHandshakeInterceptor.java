package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.entity.GameUserTeamCityNullForRound;
import com.around.aroundcore.web.exceptions.api.entity.NoActiveRoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();

        try{
            log.debug("Getting team from user in current round");
            UserRoundTeamCity.findTeamByAnyCityForUser(user);
            return true;
        }catch (GameUserTeamCityNullForRound | NoActiveRoundException e){
            log.debug("User has no team in current round");
            response.getBody().write(objectMapper.writeValueAsBytes(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND));
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // no logic for now
    }
}
