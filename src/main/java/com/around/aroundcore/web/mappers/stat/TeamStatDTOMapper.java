package com.around.aroundcore.web.mappers.stat;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.services.CityService;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.web.dtos.stat.CityStatDTO;
import com.around.aroundcore.web.dtos.stat.RoundStatDTO;
import com.around.aroundcore.web.dtos.stat.TeamStatDTO;
import com.around.aroundcore.web.mappers.chunk.GameChunkStatDTOMapper;
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
    private final GameChunkService gameChunkService;
    private final CityService cityService;

    @Override
    public TeamStatDTO apply(Team team) {
        return TeamStatDTO.builder()
                .color(team.getColor())
                .id(team.getId())
                .cities(getRoundStatDTOS(team))
                .build();
    }
    public List<RoundStatDTO> getRoundStatDTOS(Team team){
        List<RoundStatDTO> rounds = new ArrayList<>();
        List<UserRoundTeamCity> teamURTCsByRound = UserRoundTeamCity.getURTsDistinctByRound(team.getUserRoundTeamCity());
        List<City> allCities = cityService.findAll();
        teamURTCsByRound.forEach(urt -> {
            List<CityStatDTO> cities = new ArrayList<>();
            allCities.forEach(city -> {
                var chunks = gameChunkService.findAllByRoundAndTeamAndCity(urt.getRound(), team, city);
                cities.add(CityStatDTO.builder()
                        .team_id(team.getId())
                        .name(city.getName())
                        .chunks_now(chunks.size())
                        .chunks(chunks.stream().map(chunkDTOMapper).toList())
                        .build());
            });
            rounds.add(RoundStatDTO.builder().round_id(urt.getRound().getId()).cities(cities).build());
        });
        return rounds;
    }
}