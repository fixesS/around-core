package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.repositories.RoundRepository;
import com.around.aroundcore.database.repositories.UserRoundTeamRepository;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.entity.RoundNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RoundService {
    private final RoundRepository roundRepository;
    private final UserRoundTeamRepository userRoundTeamRepository;

    public Round getCurrentRound() throws RoundNullException{
        //todo auto delete expired rounds
        return roundRepository.findFirstByActiveIsTrue().orElseThrow(RoundNullException::new);
    }
    public UserRoundTeam getUserRoundTeamByTeamInCurrentRound(Integer teamId) throws NoActiveRoundException {
        return userRoundTeamRepository.findByRoundIdAndTeamId(getCurrentRound().getId(), teamId).stream().findFirst().orElseThrow(NoActiveRoundException::new);
    }
    public UserRoundTeam getUserRoundTeamByTeamInRound(Integer teamId, Integer roundId) throws RoundNullException {
        return userRoundTeamRepository.findByRoundIdAndTeamId(roundId, teamId).stream().findFirst().orElseThrow(RoundNullException::new);
    }
    public void checkIfExistById(Integer roundId){
        if(!roundRepository.existsById(roundId)){
            throw new RoundNullException();
        }
    }

}
