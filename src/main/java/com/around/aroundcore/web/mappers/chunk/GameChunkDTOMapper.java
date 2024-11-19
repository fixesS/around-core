package com.around.aroundcore.web.mappers.chunk;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.web.dtos.ChunkDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GameChunkDTOMapper implements Function<GameChunk, ChunkDTO> {
    private final GameChunkService gameChunkService;
    @Override
    public ChunkDTO apply(GameChunk gameChunk) {
        return ChunkDTO.builder()
                .id(Optional.ofNullable(gameChunk.getId()).orElse(""))
                .round_id(gameChunk.getRound().getId())
                .team_id(getTeamId(gameChunk))
                .build();
    }
    private Integer getTeamId(final GameChunk gameChunk) {
        Team team = gameChunkService.getTeamOfChunkOwnerInCurrentRound(gameChunk);
        if(team == null) {
            return -1000;
        }
        return team.getId();
    }

}
