package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.services.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //var session = sessionService.findByUuid(sessionUuid);
        //var user = session.getUser();

        return true;
//        try{
//            //log.debug("Getting team from user in current round");
//            //UserRoundTeamCity.findTeamByAnyCityForUser(user); todo disabled because the user must be able to send the locations via STOMP (see UpgradeHttpToWebSocketHandshakeHandler too)
//            return true;
//        }catch (GameUserTeamNullForRoundAndAneCity | NoActiveRoundException e){
//            log.debug("User has no team in current round");
//            response.getBody().write(objectMapper.writeValueAsBytes(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND));
//            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
//            return false;
//        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // no logic for now
    }
}
