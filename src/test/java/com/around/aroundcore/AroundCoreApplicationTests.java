package com.around.aroundcore;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.gson.GsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AroundCoreApplicationTests {
	@Value("${local.server.port}")
	private int port;

	private static WebClient client;

	BlockingQueue<String> blockingQueue;
	@Autowired
	ObjectMapper mapper;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	GsonParser gsonParser;
	private String tok = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhY2Nlc3MiLCJqdGkiOiIxMzhkOGFkNy00NTZlLTQzMjgtOGI5Mi0wNmU5NjFjMWNkNTQiLCJpYXQiOjE3MTIxNDg0NTQsImV4cCI6MTcxMjI0ODQ1NH0.a_5IKUTwemBuhG1FfgXQjOo-ExY9VvutvsyOBdafQcIwZ644LJ7oUDlqUyHcUhiO";
	//password@gmail.com(team 2) = eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhY2Nlc3MiLCJqdGkiOiIxMzhkOGFkNy00NTZlLTQzMjgtOGI5Mi0wNmU5NjFjMWNkNTQiLCJpYXQiOjE3MTIxNDg0NTQsImV4cCI6MTcxMjI0ODQ1NH0.a_5IKUTwemBuhG1FfgXQjOo-ExY9VvutvsyOBdafQcIwZ644LJ7oUDlqUyHcUhiO
	//password1@gmail.com(team 1) = eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhY2Nlc3MiLCJqdGkiOiIzY2RlNTUwMy02MTJlLTRlMTgtYWRjNy03YzVkNDFlYTEyZTUiLCJpYXQiOjE3MTIxNDc3NDYsImV4cCI6MTcxMjI0Nzc0Nn0.HuGXw62GEUZL94txaH1nOTgfTWCq3ZKr-WMxFE05qTaNdIaP17_Q6q-g2ld1t1_6
	@BeforeAll
	public void setup() throws Exception {

		blockingQueue = new LinkedBlockingDeque<String>();

		RunStopFrameHandler runStopFrameHandler = new RunStopFrameHandler(new CompletableFuture<>());

		String wsUrl = "ws://127.0.0.1:" + port + WebSocketConfig.REGISTRY;

		//WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

		stompClient.setMessageConverter(new StringMessageConverter());

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.set("Authorization", "Bearer "+tok);
		log.info(" HEADERS: "+headers.toString());

		StompSession stompSession = stompClient
				.connect(wsUrl, headers, new StompSessionHandlerAdapter() {})
				.get(1, SECONDS);
		log.info(stompSession.toString());
		client = WebClient.builder()
				.stompClient(stompClient)
				.stompSession(stompSession)
				.handler(runStopFrameHandler)
				.build();
		log.info(client.toString());
	}
	@AfterAll
	public void tearDown() {

		if (client.getStompSession().isConnected()) {
			client.getStompSession().disconnect();
			client.getStompClient().stop();
		}
	}

	@SneakyThrows
	@Test
	public void should_PassSuccessfully_When_CreateChat() {

		StompSession stompSession = client.getStompSession();

		RunStopFrameHandler handler = client.getHandler();

		ChunkDTO chunkDTO = ChunkDTO.builder()
				.id("1")
				.build();

		String json = gsonParser.toJson(chunkDTO);


		StompHeaders headers1 = new StompHeaders();
		headers1.setDestination(ChunkWsController.FETCH_CHUNK_CHANGES_EVENT);
		stompSession.subscribe(headers1, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				blockingQueue.offer((String) payload);
			}
		});

		StompHeaders headers2 = new StompHeaders();
		headers2.setDestination(ChunkWsController.CHUNK_CHANGES_FROM_USER);

		stompSession.send(
				headers2,
				json
		);

		chunkDTO.setTeam_id(2);
		List<ChunkDTO> exceptedList = new ArrayList<>();
		exceptedList.add(chunkDTO);

		String exceptedJson = gsonParser.toJson(exceptedList);

		Assertions.assertEquals(exceptedJson, blockingQueue.poll(5, SECONDS));
	}

	private List<Transport> createTransportClient() {

		List<Transport> transports = new ArrayList<>(1);

		transports.add(new WebSocketTransport(new StandardWebSocketClient()));

		return transports;
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

