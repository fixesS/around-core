package com.around.aroundcore.web.mappers.chunk;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.web.dtos.ChunkDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class GameChunkDTOMapper implements Function<GameChunk, ChunkDTO> {
    @Override
    public ChunkDTO apply(GameChunk gameChunk) {
        return ChunkDTO.builder()
                .id(Optional.ofNullable(gameChunk.getId()).orElse(""))
                .round_id(gameChunk.getRound().getId())
                .team_id(gameChunk.getTeam().getId())
                .build();
    }

}
