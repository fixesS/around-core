package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.ChunkStatDTO;
import com.around.aroundcore.web.dtos.RoundStatDTO;
import com.around.aroundcore.web.dtos.TeamStatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamStatDTOMapper implements Function<Team, TeamStatDTO> {
    private final GameChunkStatDTOMapper chunkDTOMapper;
    @Override
    public TeamStatDTO apply(Team team) {
        return TeamStatDTO.builder()
                .color(team.getColor())
                .id(team.getId())
                .roundStat(getRoundStatDTO(team))
                .build();
    }
    public List<RoundStatDTO> getRoundStatDTO(Team team){
        List<RoundStatDTO> roundStatDTOs = new ArrayList<>();
        List<UserRoundTeam> URTs = UserRoundTeam.getURTsDistinctByRound(team.getUserRoundTeam());
        URTs.forEach(urt -> {
            var chunks = team.getMembersChunksForRound(urt.getRound().getId());
            roundStatDTOs.add(RoundStatDTO.builder()
                    .round_id(urt.getRound().getId())
                    .number_of_chunks(chunks.size())
                    .chunks(chunks.stream().map(chunkDTOMapper).toList())
                    .build());
        });
        return roundStatDTOs;
    }
}
