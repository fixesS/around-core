package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.exceptions.entity.GameUserTeamNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;


@Slf4j
@AllArgsConstructor
public class UpgradeHttpToWebSocketHandshakeHandler extends DefaultHandshakeHandler {
    private SessionService sessionService;
    private JwtService jwtService;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String authHeader = jwtService.resolveAuthHeader(request);
        String accessToken = jwtService.resolveToken(authHeader);
        Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        GameUser user = session.getUser();
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