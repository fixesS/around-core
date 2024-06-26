package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.web.dtos.ChunkDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class GameChunkDTOMapper  implements Function<GameChunk, ChunkDTO> {
    @Override
    public ChunkDTO apply(GameChunk gameChunk) {
        return ChunkDTO.builder()
                .id(Optional.ofNullable(gameChunk.getId()).orElse(""))
                .team_id(Optional.ofNullable(gameChunk.getOwner()).map(user -> user.getTeam().getId()).orElse(-1000))
                .build();
    }
}
