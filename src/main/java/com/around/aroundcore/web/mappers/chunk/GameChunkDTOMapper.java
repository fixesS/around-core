package com.around.aroundcore.web.mappers.chunk;

import com.around.aroundcore.core.exceptions.api.entity.TeamNullException;
import com.around.aroundcore.database.models.chunk.GameChunk;
import com.around.aroundcore.web.dtos.ChunkDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameChunkDTOMapper implements Function<GameChunk, ChunkDTO> {
    @Override
    public ChunkDTO apply(GameChunk gameChunk) {
        return ChunkDTO.builder()
                .id(Optional.ofNullable(gameChunk.getId()).orElse(""))
                .round_id(gameChunk.getRound().getId())
                .team_id(getTeamId(gameChunk))
                .build();
    }
    private Integer getTeamId(final GameChunk gameChunk) {
        if(gameChunk.getOwner()==null){
            return -1000;
        }
        try {
            return gameChunk.getOwner().getTeam(gameChunk.getCity()).getId();
        }catch (TeamNullException e){
            return -1000;
        }
    }
}
