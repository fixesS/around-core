package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.repositories.RoundRepository;
import com.around.aroundcore.database.repositories.UserRoundTeamRepository;
import com.around.aroundcore.web.exceptions.api.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.api.entity.RoundNullException;
import com.around.aroundcore.web.exceptions.api.entity.TeamNullException;
import com.around.aroundcore.web.exceptions.api.entity.URTNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Cacheable(value = "getRoundById", key = "#id")
    public Round getRoundById(Integer id) throws RoundNullException{
        return roundRepository.findById(id).orElseThrow(RoundNullException::new);
    }
    public UserRoundTeamCity getUserRoundTeamByTeamInCurrentRound(Integer teamId) throws NoActiveRoundException, TeamNullException, URTNullException {
        teamService.checkById(teamId);
        Round round = roundRepository.findFirstByActiveIsTrue().orElseThrow(NoActiveRoundException::new);
        return userRoundTeamRepository.findByRoundIdAndTeamId(round.getId(), teamId).stream().findFirst().orElseThrow(URTNullException::new);
    }
    public List<UserRoundTeamCity> getURTCByUserDistinctOnRoundAndCity(Integer userId){
        return userRoundTeamRepository.findByUserDistinctOnRoundAndCity(userId);
    }
    public List<Round> getUserRounds(Integer userId){
        return roundRepository.findByUser(userId);
    }
    public UserRoundTeamCity getUserRoundTeamByTeamInRound(Integer teamId, Integer roundId) throws RoundNullException, TeamNullException, URTNullException{
        if(!roundRepository.existsById(roundId)){
            throw new RoundNullException();
        }
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
