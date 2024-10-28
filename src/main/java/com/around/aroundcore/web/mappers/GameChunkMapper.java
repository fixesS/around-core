package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.web.dtos.ChunkDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GameChunkMapper implements Function<ChunkDTO, GameChunk> {
    @Override
    public GameChunk apply(ChunkDTO chunkDTO) {
        return GameChunk.builder()
                .id(chunkDTO.getId())
                .build();
    }
}
