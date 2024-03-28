package com.around.aroundcore.web.controllers.ws;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.jwt.JwtService;
import com.around.aroundcore.web.gson.GsonParser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
@Controller
public class ChunkWsController {

    SimpMessagingTemplate messagingTemplate;

    SessionService sessionService;
    JwtService jwtService;
    GsonParser gsonParser;
    public static final String CHUNK_CHANGES = "/topic/chunk.changes";
    public static final String CHUNK_CHANGES_FROM_USER = "/topic/chunk.changes";
    public static final String FETCH_CHUNK_CHANGES_EVENT = "/topic/chunk.event";

    @SubscribeMapping(FETCH_CHUNK_CHANGES_EVENT)
    public void fetchChunkChangesEvent(){
        log.info("Got new subscription");
    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    public void handleChunkChanges(@Payload String json, StompHeaderAccessor stompHeaderAccessor) throws InterruptedException {
        String authorizationHeader = stompHeaderAccessor.getFirstNativeHeader("Authorization");
        if (isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }
        String accessToken = authorizationHeader.substring("Bearer".length() + 1);
        Session session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        GameUser user = session.getUser();
        log.info(user.getEmail());


        Thread.sleep(500);

        messagingTemplate.convertAndSend(
                FETCH_CHUNK_CHANGES_EVENT,
                json
        );
    }



}
