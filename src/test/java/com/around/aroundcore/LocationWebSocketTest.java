package com.around.aroundcore;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.controllers.ws.UserLocationController;
import com.around.aroundcore.web.dtos.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocationWebSocketTest {

    private final String Http = "http://";
    private final String ws = "ws://";
    private final String host = "localhost:";
    @Value("${local.server.port}")
    private int port;
    @Value("${around.home}")
    private String home;
    @Value("${testing.team1.email}")
    private String email1;
    @Value("${testing.team1.password}")
    private String pass1;
    @Value("${testing.team2.email}")
    private String email2;
    @Value("${testing.team2.password}")
    private String pass2;

    private static WebClient clientMike;
    private static WebClient clientMary;

    LinkedBlockingQueue<GameUserLocationDTO> blockingQueue;
    LinkedBlockingQueue<ApiError> blockingQueueError;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    private TokenData token;

    @BeforeAll
    public void setup() throws Exception {

        blockingQueue = new LinkedBlockingQueue<>();
        blockingQueueError = new LinkedBlockingQueue<>();

        RunStopFrameHandler runStopFrameHandler = new RunStopFrameHandler(new CompletableFuture<>());

        String wsUrl = ws+ home+ WebSocketConfig.REGISTRY;
        //String wsUrl = "wss://aroundgame.ru/ws";
        log.info(wsUrl);

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders headers1 = new WebSocketHttpHeaders();
        headers1.set("login", email1);
        headers1.set("passcode", pass1);

        WebSocketHttpHeaders headers2 = new WebSocketHttpHeaders();
        headers2.set("login", email2);
        headers2.set("passcode", pass2);

        StompSession stompSessionMike = stompClient
                .connectAsync(wsUrl, headers1, new StompSessionHandlerAdapter() {})
                .get(4, SECONDS);
        clientMike = WebClient.builder()
                .stompClient(stompClient)
                .stompSession(stompSessionMike)
                .handler(runStopFrameHandler)
                .build();

        StompSession stompSessionMary = stompClient
                .connectAsync(wsUrl, headers2, new StompSessionHandlerAdapter() {})
                .get(4, SECONDS);
        clientMary = WebClient.builder()
                .stompClient(stompClient)
                .stompSession(stompSessionMary)
                .handler(runStopFrameHandler)
                .build();
    }
    @AfterAll
    public void tearDown() {

        if (clientMike.getStompSession().isConnected()) {
            clientMike.getStompSession().disconnect();
            clientMike.getStompClient().stop();
        }
        if (clientMary.getStompSession().isConnected()) {
            clientMary.getStompSession().disconnect();
            clientMary.getStompClient().stop();
        }
    }

    @SneakyThrows
    @Test
    void testSendingLocation() {
        StompSession stompSessionMike = clientMike.getStompSession();
        StompSession stompSessionMary = clientMary.getStompSession();

        GameUserLocationDTO gameUserLocationDTOFromMike = GameUserLocationDTO.builder()
                .latitude(1)
                .longitude(2)
                .build();

        stompSessionMary.subscribe("/user"+WebSocketConfig.QUEUE_LOCATIONS_FOR_USER, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new TypeReference<GameUserLocationDTO>(){}.getType();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((GameUserLocationDTO) payload);
            }
        });

        stompSessionMike.send(
                "/app"+ UserLocationController.LOCATION_FROM_USER,
                gameUserLocationDTOFromMike
        );

        GameUserLocationDTO receivedLocationToMary = objectMapper.convertValue(blockingQueue.poll(5, SECONDS),new TypeReference<GameUserLocationDTO>(){});

        Assertions.assertEquals(gameUserLocationDTOFromMike.toString(), receivedLocationToMary.toString());
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class RunStopFrameHandler implements StompFrameHandler {

        CompletableFuture<Object> future;

        @Override
        public @NonNull Type getPayloadType(StompHeaders stompHeaders) {

            log.info(stompHeaders.toString());

            return byte[].class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, Object o) {

            log.info((String) o);

            future.complete(o);

            future = new CompletableFuture<>();
        }
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class WebClient {

        WebSocketStompClient stompClient;

        StompSession stompSession;

        String sessionToken;

        RunStopFrameHandler handler;
    }
}
