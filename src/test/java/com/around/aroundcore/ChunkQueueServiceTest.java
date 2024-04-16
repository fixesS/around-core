package com.around.aroundcore;

import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.services.ChunkQueueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChunkQueueServiceTest {
    @Autowired
    ChunkQueueService chunkQueueService;

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
    void testAddingToQueueOldChunkWithNewTeamId_1(){
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
    void testAddingToQueueOldChunkWithNewTeamId_2(){
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
    void testAddingToQueueOldChunkWithNewTeamId_and_gettingJson(){
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

        List<ChunkDTO> exceptedChunks = new ArrayList<>();
        exceptedChunks.add(new ChunkDTO("1",4));
        exceptedChunks.add(new ChunkDTO("2",1));
        exceptedChunks.add(new ChunkDTO("3",2));
        exceptedChunks.add(new ChunkDTO("4",2));
        exceptedChunks.add(new ChunkDTO("5",3));
        exceptedChunks.add(new ChunkDTO("6",7));
        exceptedChunks.add(new ChunkDTO("7",5));
        exceptedChunks.add(new ChunkDTO("8",5));
        exceptedChunks.add(new ChunkDTO("9",6));
        exceptedChunks.add(new ChunkDTO("10",4));
        exceptedChunks.add(new ChunkDTO("11",11));

        Assertions.assertEquals(exceptedChunks.toString(),chunks.toString());
    }
}
