package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.repositories.RoundRepository;
import com.around.aroundcore.database.repositories.UserRoundTeamRepository;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.entity.RoundNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import com.around.aroundcore.web.exceptions.entity.URTNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RoundService {
    private final RoundRepository roundRepository;
    private final TeamService teamService;
    private final UserRoundTeamRepository userRoundTeamRepository;

    @Cacheable("currentRound")
    public Round getCurrentRound() throws NoActiveRoundException{
        return roundRepository.findFirstByActiveIsTrue().orElseThrow(NoActiveRoundException::new);
    }
    public UserRoundTeam getUserRoundTeamByTeamInCurrentRound(Integer teamId) throws NoActiveRoundException, TeamNullException, URTNullException {
        teamService.checkById(teamId);
        return userRoundTeamRepository.findByRoundIdAndTeamId(getCurrentRound().getId(), teamId).stream().findFirst().orElseThrow(URTNullException::new);
    }
    public UserRoundTeam getUserRoundTeamByTeamInRound(Integer teamId, Integer roundId) throws RoundNullException, TeamNullException, URTNullException{
        checkById(roundId);
        teamService.checkById(teamId);
        return userRoundTeamRepository.findByRoundIdAndTeamId(roundId, teamId).stream().findFirst().orElseThrow(URTNullException::new);
    }
    @Cacheable(value = "checkRound", key = "#roundId")
    public void checkById(Integer roundId){
        if(!roundRepository.existsById(roundId)){
            throw new RoundNullException();
        }
    }

}