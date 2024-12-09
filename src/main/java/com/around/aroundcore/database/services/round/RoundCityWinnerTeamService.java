package com.around.aroundcore.database.services.round;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.round.RoundCityWinnerTeam;
import com.around.aroundcore.database.repositories.round.RoundCityWinnerTeamRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RoundCityWinnerTeamService {
    private final RoundCityWinnerTeamRepository roundCityWinnerTeamRepository;

    public void save(Round round, City city, List<Team> teams) {
        List<RoundCityWinnerTeam> roundCityWinnerTeams = new ArrayList<>();
        teams.forEach(team ->
                roundCityWinnerTeams.add(RoundCityWinnerTeam.builder().round(round).city(city).team(team).build()));
        roundCityWinnerTeamRepository.saveAll(roundCityWinnerTeams);
    }
}
