package com.around.aroundcore.web.controllers.ws;

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

import java.util.Set;

@Slf4j
@AllArgsConstructor
@Controller
public class ChunkWsController {

    SimpMessagingTemplate messagingTemplate;
    GsonParser gsonParser;
    public static final String CHUNK_CHANGES = "/topic/chunk.changes";
    public static final String CHUNK_CHANGES_FROM_USER = "/topic/chunk.changes";
    public static final String FETCH_CHUNK_CHANGES_EVENT = "/topic/chunk.event";

    @SubscribeMapping(FETCH_CHUNK_CHANGES_EVENT)
    public void fetchChunkChangesEvent(){
        log.error("Got new subscription");
    }

    @MessageMapping(CHUNK_CHANGES_FROM_USER)
    @SendTo(FETCH_CHUNK_CHANGES_EVENT)
    public void handleChunkChanges(@Payload String json, StompHeaderAccessor stompHeaderAccessor) throws InterruptedException {

        Thread.sleep(500);

        messagingTemplate.convertAndSend(
                FETCH_CHUNK_CHANGES_EVENT,
                json
        );
    }



}
