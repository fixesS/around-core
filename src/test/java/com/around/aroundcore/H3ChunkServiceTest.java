package com.around.aroundcore;

import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.services.H3ChunkService;
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
class H3ChunkServiceTest {
    @Autowired
    H3ChunkService h3ChunkService;

    @Test
    void getChunksWithSkillLevels(){
        String chunkId = "8b10dc93422efff";
        List<String> exceptedNeighbours = new ArrayList<>();
        exceptedNeighbours.add(chunkId);
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,0));
        exceptedNeighbours.add("8b10dc934223fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,1));
        exceptedNeighbours.add("8b10dc934205fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,2));
        exceptedNeighbours.add("8b10dc93422afff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,3));
        exceptedNeighbours.add("8b10dc934228fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,4));
        exceptedNeighbours.add("8b10dc93422cfff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,5));
        exceptedNeighbours.add("8b10dc934221fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillWithLevel(chunkId,6));
    }

    @Test
    void getCellByLATLon(){
        Double lat = 56.8308147;
        Double lon = 60.6216886;
        List<ChunkDTO> chunkDTO = h3ChunkService.getChunkByLatLon(lat,lon,2);
        log.info(chunkDTO.toString());
        Assertions.assertNotNull(chunkDTO);
    }
}
