package com.around.aroundcore.web.mappers.chunk;

import com.around.aroundcore.database.models.chunk.GameChunk;
import com.around.aroundcore.web.dtos.stat.ChunkStatDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GameChunkStatDTOMapper  implements Function<GameChunk, ChunkStatDTO> {
    @Override
    public ChunkStatDTO apply(GameChunk gameChunk) {
        return ChunkStatDTO.builder()
                .id(gameChunk.getId())
                .build();
    }
}
