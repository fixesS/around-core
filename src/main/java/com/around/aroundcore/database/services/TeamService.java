package com.around.aroundcore.database.services;

import com.around.aroundcore.core.exceptions.NoVictoryContenders;
import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.repositories.TeamRepository;
import com.around.aroundcore.core.exceptions.api.entity.TeamNullException;
import com.around.aroundcore.database.services.user.GameUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class TeamService {
    private TeamRepository teamRepository;
    private GameUserService gameUserService;
    public Team findById(Integer id) throws TeamNullException {
        return teamRepository.findById(id).orElseThrow(TeamNullException::new);
    }
    public List<Team> findAll(){
        return teamRepository.findAll();
    }
    private boolean existById(Integer id){
        return teamRepository.existsById(id);
    }
    public void checkById(Integer id){
        if(!existById(id)){
            throw new TeamNullException();
        }
    }
    public Team getRandomTeam(){
        return teamRepository.getRandomTeam();
    }
    @Transactional(dontRollbackOn = {NoVictoryContenders.class})
    public List<Team> getVictoryContendersForRoundAndCity(Round round, City city) throws NoVictoryContenders {
        List<Team> contenders = teamRepository.getWinnerForRound(round.getId(), city.getId());
        if(contenders.isEmpty()){
            throw new NoVictoryContenders("No victory contenders");
        }
        log.debug("{} victory contenders (teams) in round {} city {}", contenders.size(), round.getId(), city.getId());
        return contenders;
    }
    public Integer[] calculateCoinsAndExpRewards(List<Team> victoryContenderTeams, Round round){
        Integer coinsReward = round.getGameSettings().getTeamWinRewardCoins();
        Integer expReward = round.getGameSettings().getTeamWinRewardExp();

        if(victoryContenderTeams.size()>1){
            coinsReward = (int) Math.floor(coinsReward * round.getGameSettings().getTeamWinRewardCoinsDividingRatio());
            expReward = (int) Math.floor(expReward * round.getGameSettings().getTeamWinRewardExpDividingRatio());
        }
        coinsReward = coinsReward/victoryContenderTeams.size();
        expReward = expReward/victoryContenderTeams.size();
        return new Integer[]{coinsReward, expReward};

    }
    @Transactional
    public void rewardTeamsMembers(List<Team> teams, Round round, City city, final Integer coinsReward, final Integer expReward){
        teams.forEach(team -> {
            List<GameUser> members = gameUserService.getTeamMembersForRoundAndCity(team, List.of(round), List.of(city));
            members.forEach(member -> {
                member.addExperience(expReward, round.getGameSettings().getUserLevelCost());
                member.addCoins(coinsReward);
            });
            gameUserService.updateAll(members);
        });
    }
}
