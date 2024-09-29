package com.around.aroundcore;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.dtos.ApiError;
import com.around.aroundcore.web.dtos.AuthDTO;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.TokenData;
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
class ChunkWebSocketTest {

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

	private static WebClient client;

	LinkedBlockingQueue<ArrayList<ChunkDTO>> blockingQueue;
	LinkedBlockingQueue<ApiError> blockingQueueError;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private TestRestTemplate restTemplate;
	private TokenData token;

	@BeforeAll
	public void setup() throws Exception {

		//token = getTokenData(email1,pass1);

		String token_s = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhY2Nlc3MiLCJqdGkiOiIyYmFkYTNmZS0yNGIxLTQ0NWYtYWExNy02NTI2NDRhNzljOWYiLCJpYXQiOjE3Mjc2MDUzMDQsImV4cCI6MTcyNzcwNTMwNH0.w2SohNB81ucF0-_dE3gvNK6aLN2tciDFETsvwu_-4EKip9-Ueazrfw9JV0qHy2xc";
		blockingQueue = new LinkedBlockingQueue<>();
		blockingQueueError = new LinkedBlockingQueue<>();

		RunStopFrameHandler runStopFrameHandler = new RunStopFrameHandler(new CompletableFuture<>());

		String wsUrl = ws+ home+ WebSocketConfig.REGISTRY;
		log.info(wsUrl);

		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.set("Authorization", "Bearer "+token_s);
		log.info(headers.get("Authorization").toString());

		StompSession stompSession = stompClient
				.connectAsync(wsUrl, headers, new StompSessionHandlerAdapter() {})
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
	void testAddingChunk() {

		StompSession stompSession = client.getStompSession();
		log.info(client.sessionToken);

		RunStopFrameHandler handler = client.getHandler();

		ChunkDTO chunkDTO = ChunkDTO.builder()
				.id("8b10dc9268adfff")
				.build();

		StompHeaders headers1 = new StompHeaders();
		headers1.setDestination(ChunkWsController.CHUNK_CHANGES_EVENT);
		stompSession.subscribe(headers1, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return new TypeReference<List<ChunkDTO>>(){}.getType();
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				blockingQueue.offer((ArrayList<ChunkDTO>) payload);
			}
		});

		stompSession.subscribe("/user"+ChunkWsController.QUEUE_ERROR_FOR_SESSION, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return new TypeReference<ApiError>(){}.getType();
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				blockingQueueError.offer((ApiError) payload);
			}
		});

		stompSession.send(
				ChunkWsController.CHUNK_CHANGES_FROM_USER,
				chunkDTO
		);

		chunkDTO.setTeam_id(1);
		List<ChunkDTO> exceptedList = new ArrayList<>();
		exceptedList.add(chunkDTO);
		//exceptedList.add(ChunkDTO.builder().id("8b10dc934223fff").team_id(1).build())
		List<ChunkDTO> receivedList = objectMapper.convertValue(blockingQueue.poll(5, SECONDS),new TypeReference<ArrayList<ChunkDTO>>(){});
		log.info("{}",receivedList.size());
		ApiError apiError = blockingQueueError.poll(5, SECONDS);
		log.info("{}",apiError.getMessage());

		Assertions.assertEquals(exceptedList, receivedList);

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
	private TokenData getTokenData(String email, String password){
		AuthDTO authDTO = new AuthDTO(email,password);
		log.info(authDTO.toString());
		String url = Http+host+port+"/"+AroundConfig.API_V1_LOGIN;
		log.info(url);
		TokenData tokenData = restTemplate.postForEntity(url,authDTO,TokenData.class).getBody();
		log.info(tokenData.toString());
		return tokenData;
	}
}

