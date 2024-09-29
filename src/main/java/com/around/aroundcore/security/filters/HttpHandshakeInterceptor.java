package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.security.services.WebSocketHeaderService;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNotStartsWithPrefixException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserTeamNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private final SessionService sessionService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final WebSocketHeaderService webSocketHeaderService;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();

        try{
            log.info("GETTING TEAM FROM USER| INTERCEPTOR");
            UserRoundTeam.findTeamForCurrentRoundAndUser(user);
            return true;
        }catch (GameUserTeamNullForRound | NoActiveRoundException e){
            log.info("NO TEAM| INTERCEPTOR");
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
