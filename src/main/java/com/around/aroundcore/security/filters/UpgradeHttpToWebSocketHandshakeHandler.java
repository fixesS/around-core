package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.security.services.WebSocketAuthService;
import com.around.aroundcore.security.services.WebSocketHeaderService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.exceptions.entity.GameUserTeamNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;


@Slf4j
@AllArgsConstructor
public class UpgradeHttpToWebSocketHandshakeHandler extends DefaultHandshakeHandler {
    private SessionService sessionService;
    private JwtService jwtService;
    private WebSocketHeaderService webSocketHeaderService;
    private AuthenticationManager authenticationManager;
    private WebSocketAuthService webSocketAuthService;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler, Map<String, Object> attributes) {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session);
        authentication.setAuthenticated(true);
        try{
            UserRoundTeam.findTeamForCurrentRoundAndUser(user);
            authentication.setAuthenticated(true);
        }catch (NoActiveRoundException | GameUserTeamNullForRound e){
            authentication.setAuthenticated(false);
        }
        return authentication;
    }
}