package com.around.aroundcore;

import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.services.H3ChunkService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.diff.Chunk;
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
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,0));
        exceptedNeighbours.add(chunkId);
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,1));
        exceptedNeighbours.add("8b10dc934223fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,2));
        exceptedNeighbours.add("8b10dc934205fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,3));
        exceptedNeighbours.add("8b10dc93422afff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,4));
        exceptedNeighbours.add("8b10dc934228fff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,5));
        exceptedNeighbours.add("8b10dc93422cfff");
        Assertions.assertEquals(exceptedNeighbours,h3ChunkService.getNeighboursForSkillRuleValue(chunkId,6));
    }

    @Test
    void getCellByLATLon(){
        Double lat = 56.8308147;
        Double lon = 60.6216886;
        List<ChunkDTO> chunkDTO = h3ChunkService.getChunkByLatLon(lat,lon,2);
        log.info(chunkDTO.toString());
        Assertions.assertNotNull(chunkDTO);
    }

    @Test
    void checkIsParent(){
        String chunkId = "8b10dc93422efff";

        List<String> parentChunks = new ArrayList<>();
        parentChunks.add("8610c221fffffff");
        parentChunks.add("8610c22f7ffffff");
        parentChunks.add("8610c22afffffff");
        parentChunks.add("8610c22d7ffffff");
        parentChunks.add("8610c228fffffff");
        parentChunks.add("8610c2287ffffff");
        parentChunks.add("8610c22b7ffffff");
        parentChunks.add("8610dc92fffffff");
        parentChunks.add("8610dc927ffffff");
        parentChunks.add("8610c229fffffff");
        parentChunks.add("8610c2297ffffff");
        parentChunks.add("8610dc907ffffff");
        parentChunks.add("8610dc937ffffff");
        parentChunks.add("8610c266fffffff");
        parentChunks.add("8610dc917ffffff");
        parentChunks.add("8610c264fffffff");
        Assertions.assertTrue(parentChunks.contains(h3ChunkService.getParentId(chunkId,6)));
    }
}
