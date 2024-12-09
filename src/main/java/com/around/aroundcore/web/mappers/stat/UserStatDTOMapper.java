package com.around.aroundcore.web.mappers.stat;

import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.round.RoundService;
import com.around.aroundcore.web.dtos.stat.CityStatDTO;
import com.around.aroundcore.web.dtos.stat.RoundStatDTO;
import com.around.aroundcore.web.dtos.stat.GameUserStatDTO;
import com.around.aroundcore.web.mappers.chunk.GameChunkStatDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserStatDTOMapper implements Function<GameUser, GameUserStatDTO> {
    private final RoundService roundService;
    private final GameChunkStatDTOMapper gameChunkStatDTOMapper;
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
    //todo could be done with hashmaps(chatgpt suggection), but i not sure about performnce (a lot of forEach vs if-statement)
    //todo check performance in future
    public List<RoundStatDTO> getRoundStatDTO(GameUser user){
        List<RoundStatDTO> rounds = new ArrayList<>();
        List<UserRoundTeamCity> userURTCs = roundService.getURTCByUserDistinctOnRoundAndCity(user.getId());
        List<Round> userRounds = userURTCs.stream().map(UserRoundTeamCity::getRound).distinct().toList();
        userRounds.forEach(r -> {
            List<CityStatDTO> cities = new ArrayList<>();
            userURTCs.forEach(urtc->{
                if(r.equals(urtc.getRound())){
                    var chunks = gameChunkService.findAllByOwnerAndRoundAndCity(user,urtc.getRound(),urtc.getCity());
                    cities.add(CityStatDTO.builder()
                            .team_id(urtc.getTeam().getId())
                            .city_id(urtc.getCity().getId())
                            .name(urtc.getCity().getName())
                            .chunks(chunks.stream().map(gameChunkStatDTOMapper).toList())
                            .chunks_now(chunks.size())
                            .chunks_all(urtc.getCapturedChunks())
                            .build());
                }
            });
            rounds.add(RoundStatDTO.builder().round_id(r.getId()).cities(cities).build());
        });
        return rounds;
    }
}