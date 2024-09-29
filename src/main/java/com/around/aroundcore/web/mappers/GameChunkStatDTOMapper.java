package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.ChunkStatDTO;
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
