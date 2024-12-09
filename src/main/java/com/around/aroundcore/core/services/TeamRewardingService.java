package com.around.aroundcore.core.services;

import com.around.aroundcore.core.exceptions.NoVictoryContenders;
import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.CityService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.database.services.round.RoundCityWinnerTeamService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeamRewardingService {
    private TeamService teamService;
    private CityService cityService;
    private RoundCityWinnerTeamService roundCityWinnerTeamService;

    @Transactional
    public void rewardTeamsForRound(Round round){
        List<City> cities = cityService.findAll();
        cities.forEach(city -> {
            log.debug("Finding victory contenders for round {} and city {} ...",round.getId(),city.getId());
            List<Team> teams;
            try{
                teams = teamService.getVictoryContendersForRoundAndCity(round, city);

                log.debug("Rewarding victory contenders for round {} and city {} ...",round.getId(),city.getId());
                Integer[] rewards = teamService.calculateCoinsAndExpRewards(teams, round);
                teamService.rewardTeamsMembers(teams,round,city,rewards[0],rewards[1]);
                roundCityWinnerTeamService.save(round,city,teams);
            }catch (NoVictoryContenders ignored){
                log.warn("No victory contenders found for round {} and city {} !",round.getId(),city.getId());
            }catch (Exception e){
                log.error(e.getMessage());
            }
        });
    }
}
