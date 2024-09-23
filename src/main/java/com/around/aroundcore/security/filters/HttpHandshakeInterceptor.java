package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.entity.GameUserTeamNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    SessionService sessionService;
    JwtService jwtService;
    ObjectMapper objectMapper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authHeader = jwtService.resolveAuthHeader(request);
        var accessToken = jwtService.resolveToken(authHeader);
        var session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        var user = session.getUser();
        try{
            log.info("GETTING TEAM FROM USER| INTERCEPTOR");
            UserRoundTeam.findTeamForCurrentRoundAndUser(user);
            return true;
        }catch (GameUserTeamNullForRound | NoActiveRoundException e){
            log.info("NO TEAM| INTERCEPTOR");
            ApiResponse apiResponse = ApiResponse.USER_HAS_NO_TEAM_IN_ROUND;
            response.getBody().write(objectMapper.writeValueAsBytes(apiResponse));
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // no logic for now
    }
}
