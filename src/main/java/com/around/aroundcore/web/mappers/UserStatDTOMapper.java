package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.web.dtos.stat.RoundStatDTO;
import com.around.aroundcore.web.dtos.stat.GameUserStatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatDTOMapper implements Function<GameUser, GameUserStatDTO> {
    private final GameChunkStatDTOMapper chunkDTOMapper;
    private final GameChunkService gameChunkService;
    @Override
    public GameUserStatDTO apply(GameUser user) {
        return GameUserStatDTO.builder()
                .id(user.getId())
                .level(user.getLevel())
                .roundStat(getRoundStatDTO(user))
                .username(user.getUsername())
                .avatar(user.getAvatar().getUrl())
                .build();
    }
    public List<RoundStatDTO> getRoundStatDTO(GameUser user){
        List<RoundStatDTO> roundStatDTOs = new ArrayList<>();
        user.getUserRoundTeamCities().forEach(urt -> {
            var chunks = gameChunkService.findAllByOwnerAndRoundAndCity(user,urt.getRound(),urt.getCity());
            roundStatDTOs.add(RoundStatDTO.builder()
                .round_id(urt.getRound().getId())
                .team_id(urt.getTeam().getId())
                .chunks(chunks.stream().map(chunkDTOMapper).toList())
                .number_of_chunks(chunks.size()).build());
        });
        return roundStatDTOs;
    }
}
