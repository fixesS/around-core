package com.around.aroundcore.database.services;

import com.around.aroundcore.core.exceptions.NoVictoryContenders;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.repositories.TeamRepository;
import com.around.aroundcore.core.exceptions.api.entity.TeamNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public List<Team> getVictoryContendersForRound(Round round) throws NoVictoryContenders {
        List<Team> contenders = teamRepository.getWinnerForRound(round.getId());
        if(contenders.isEmpty()){
            throw new NoVictoryContenders("No victory contenders");
        }
        return contenders;
    }
    @Async
    public void rewardTeamsForRound(List<Team> teams, Round round){
        Integer coinsReward = round.getGameSettings().getTeamWinRewardCoins();
        Integer expReward = round.getGameSettings().getTeamWinRewardExp();
        if(teams.size()>1){
            coinsReward = (int) Math.floor(
                    (coinsReward * round.getGameSettings().getTeamWinRewardCoinsDividingRatio())
                            / teams.size());
            expReward = (int) Math.floor(
                    (expReward * round.getGameSettings().getTeamWinRewardExpDividingRatio())
                            / teams.size());

        }
        doRewards(teams,round,coinsReward,expReward);

    }
    @Transactional
    public void doRewards(List<Team> teams,Round round,final Integer coinsReward,final Integer expReward){
        teams.forEach(team -> {
            List<GameUser> members = gameUserService.getTeamMembersForRound(team, round);
            members.forEach(member -> {
                member.addExperience(expReward, round.getGameSettings().getUserLevelCost());
                member.addCoins(coinsReward);
            });
            gameUserService.updateAll(members);
        });
    }
}
