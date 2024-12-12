package com.around.aroundcore.database.services.round;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.repositories.round.RoundRepository;
import com.around.aroundcore.database.repositories.round.UserRoundTeamRepository;
import com.around.aroundcore.core.exceptions.api.entity.NoActiveRoundException;
import com.around.aroundcore.core.exceptions.api.entity.RoundNullException;
import com.around.aroundcore.database.services.TeamService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RoundService {
    private final RoundRepository roundRepository;
    private final TeamService teamService;
    private final UserRoundTeamRepository userRoundTeamRepository;

    @Cacheable("currentRound")
    @Transactional(dontRollbackOn = {NoActiveRoundException.class})
    public Round getCurrentRound() throws NoActiveRoundException{
        return roundRepository.findFirstByActiveIsTrue().orElseThrow(NoActiveRoundException::new);
    }
    @Cacheable(value = "getRoundById", key = "#id")
    public Round getRoundById(Integer id) throws RoundNullException{
        return roundRepository.findById(id).orElseThrow(RoundNullException::new);
    }
    public List<UserRoundTeamCity> getURTCByUserDistinctOnRoundAndCity(Integer userId){
        return userRoundTeamRepository.findByUserDistinctOnRoundAndCity(userId);
    }
    public Long getSumOfChunksAllByRoundsAndCitiesAndTeams(List<Round> rounds, List<City> cities, List<Team> teams){
        List<Integer> roundIds = rounds.stream().map(Round::getId).collect(
                Collectors.collectingAndThen(Collectors.toList(), list ->list.isEmpty()? null : list));
        List<Integer> cityIds = cities.stream().map(City::getId).collect(
                Collectors.collectingAndThen(Collectors.toList(),list ->list.isEmpty()? null : list));
        List<Integer> teamIds = teams.stream().map(Team::getId).collect(
                Collectors.collectingAndThen(Collectors.toList(),list ->list.isEmpty()? null : list));
        return userRoundTeamRepository.getSumOfChunksAllByRoundIdsAndCityIdsAndTeamIds(roundIds,cityIds,teamIds);
    }
    @Cacheable(value = "checkRound", key = "#roundId")
    public void checkById(Integer roundId){
        if(!roundRepository.existsById(roundId)){
            throw new RoundNullException();
        }
    }
    @CacheEvict(value = {"checkRound","currentRound","getRoundById"}, allEntries = true)
    public void save(Round round){
        roundRepository.saveAndFlush(round);
    }
}
