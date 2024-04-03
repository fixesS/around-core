package com.around.aroundcore;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.services.ChunkQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChunkQueueServiceTest {
    @Autowired
    ChunkQueueService chunkQueueService;

    @Autowired
    GsonParser gsonParser;


    private void fillQueue(){
        chunkQueueService.addToQueue(ChunkDTO.builder().id("1").team_id(1).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("2").team_id(1).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("3").team_id(2).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("4").team_id(2).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("5").team_id(3).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("6").team_id(4).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("7").team_id(5).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("8").team_id(5).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("9").team_id(6).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("10").team_id(6).build());

    }

    @Test
    public void testAddingToQueueOldChunkWithNewTeamId_1(){
        fillQueue();
        chunkQueueService.addToQueue(ChunkDTO.builder().id("10").team_id(7).build());

        List<ChunkDTO> chunks = chunkQueueService.getAllFromQueue();

        for(ChunkDTO chunkDTO : chunks){
            if(chunkDTO.getId().equals("10")){
                Assertions.assertEquals(7,chunkDTO.getTeam_id());
            }
        }
        Assertions.assertTrue(chunkQueueService.isEmpty());

    }
    @Test
    public void testAddingToQueueOldChunkWithNewTeamId_2(){
        fillQueue();
        chunkQueueService.addToQueue(ChunkDTO.builder().id("10").team_id(4).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("1").team_id(4).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("6").team_id(7).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("11").team_id(11).build());

        List<ChunkDTO> chunks = chunkQueueService.getAllFromQueue();

        for(ChunkDTO chunkDTO : chunks){
            switch (chunkDTO.getId()) {
                case "10", "1" -> Assertions.assertEquals(4, chunkDTO.getTeam_id());
                case "6" -> Assertions.assertEquals(7, chunkDTO.getTeam_id());
                case "11" -> Assertions.assertEquals(11, chunkDTO.getTeam_id());
            }
        }
        Assertions.assertTrue(chunkQueueService.isEmpty());
    }
    @Test
    public void testAddingToQueueOldChunkWithNewTeamId_and_gettingJson(){
        fillQueue();
        chunkQueueService.addToQueue(ChunkDTO.builder().id("10").team_id(4).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("1").team_id(4).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("6").team_id(7).build());
        chunkQueueService.addToQueue(ChunkDTO.builder().id("11").team_id(11).build());

        List<ChunkDTO> chunks = chunkQueueService.getAllFromQueue();

        for(ChunkDTO chunkDTO : chunks){
            switch (chunkDTO.getId()) {
                case "10", "1" -> Assertions.assertEquals(4, chunkDTO.getTeam_id());
                case "6" -> Assertions.assertEquals(7, chunkDTO.getTeam_id());
                case "11" -> Assertions.assertEquals(11, chunkDTO.getTeam_id());
            }
        }
        Assertions.assertTrue(chunkQueueService.isEmpty());
        String json = gsonParser.toJson(chunks);
        String exceptedJson = "[" +
                "  {\n" +
                "    \"id\": \"1\",\n" +
                "    \"team_id\": 4\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"2\",\n" +
                "    \"team_id\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"3\",\n" +
                "    \"team_id\": 2\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"4\",\n" +
                "    \"team_id\": 2\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5\",\n" +
                "    \"team_id\": 3\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"6\",\n" +
                "    \"team_id\": 7\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"7\",\n" +
                "    \"team_id\": 5\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"8\",\n" +
                "    \"team_id\": 5\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"9\",\n" +
                "    \"team_id\": 6\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"10\",\n" +
                "    \"team_id\": 4\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"11\",\n" +
                "    \"team_id\": 11\n" +
                "  }\n" +
                "]\n";
        json = json.replaceAll("\n","").replaceAll(" ","");
        exceptedJson = exceptedJson.replaceAll("\n","").replaceAll(" ","");
        Assertions.assertEquals(exceptedJson,json);
    }

}
