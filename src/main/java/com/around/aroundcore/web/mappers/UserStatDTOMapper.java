package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameUser;

import com.around.aroundcore.web.dtos.RoundStatDTO;
import com.around.aroundcore.web.dtos.UserStatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatDTOMapper implements Function<GameUser, UserStatDTO> {
    private final GameChunkStatDTOMapper chunkDTOMapper;
    @Override
    public UserStatDTO apply(GameUser user) {
        return UserStatDTO.builder()
                .id(user.getId())
                .level(user.getLevel())
                .roundStat(getRoundStatDTO(user))
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }
    public List<RoundStatDTO> getRoundStatDTO(GameUser user){
        List<RoundStatDTO> roundStatDTOs = new ArrayList<>();
        user.getUserRoundTeams().forEach(urt -> {
            var chunks = urt.getUser().getCapturedChunks(urt.getRound().getId());
            roundStatDTOs.add(RoundStatDTO.builder()
                .round_id(urt.getRound().getId())
                .team_id(urt.getTeam().getId())
                .chunks(chunks.stream().map(chunkDTOMapper).toList())
                .number_of_chunks(chunks.size()).build());
        });
        return roundStatDTOs;
    }
}
