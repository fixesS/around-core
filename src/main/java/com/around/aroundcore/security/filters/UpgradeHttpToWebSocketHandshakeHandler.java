package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.core.exceptions.api.entity.GameUserTeamNullForRoundAndAneCity;
import com.around.aroundcore.core.exceptions.api.entity.NoActiveRoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
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

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler, Map<String, Object> attributes) {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        //var user = session.getUser();
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session);
        authentication.setAuthenticated(true);
        try{
            //UserRoundTeamCity.findTeamByAnyCityForUser(user); todo disabled because the user must be able to send the locations via STOMP (see HttpHandshakeInterceptor too)
            authentication.setAuthenticated(true);
        }catch (NoActiveRoundException | GameUserTeamNullForRoundAndAneCity e){
            authentication.setAuthenticated(false);
        }
        return authentication;
    }
}