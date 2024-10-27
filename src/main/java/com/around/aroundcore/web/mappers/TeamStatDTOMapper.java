package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.services.CityService;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.web.dtos.stat.CityStatDTO;
import com.around.aroundcore.web.dtos.stat.RoundStatDTO;
import com.around.aroundcore.web.dtos.stat.TeamStatDTO;
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
                .cities(getCityStatDTO(team))
                .build();
    }
    public List<CityStatDTO> getCityStatDTO(Team team){
        List<CityStatDTO> cityStatDTOs = new ArrayList<>();
        List<UserRoundTeamCity> URTs = UserRoundTeamCity.getURTsDistinctByRound(team.getUserRoundTeamCity());
        List<City> cities = cityService.findAll();
        cities.forEach(city -> {
            List<RoundStatDTO> roundStatDTOs = new ArrayList<>();
            URTs.forEach(urt -> {
                var chunks = gameChunkService.findAllByRoundAndTeamAndCity(urt.getRound(), team, city);
                roundStatDTOs.add(RoundStatDTO.builder()
                        .round_id(urt.getRound().getId())
                        .number_of_chunks(chunks.size())
                        .chunks(chunks.stream().map(chunkDTOMapper).toList())
                        .build());
                });

            cityStatDTOs.add(CityStatDTO.builder()
                    .city_id(city.getId())
                    .name(city.getName())
                    .rounds(roundStatDTOs)
                    .build());
        });
        return cityStatDTOs;
    }
}
