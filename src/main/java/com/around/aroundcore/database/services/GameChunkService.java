package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.repositories.GameChunkRepository;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.api.entity.CityNullException;
import com.around.aroundcore.web.exceptions.api.entity.GameChunkNullException;
import com.around.aroundcore.web.exceptions.api.entity.RoundNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GameChunkService {
    private GameChunkRepository gameChunkRepository;
    private RoundService roundService;
    private CityService cityService;

    public void create(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }

    public void update(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }

    public void saveListOfChunks(List<GameChunk> gameChunks){
        gameChunkRepository.saveAll(gameChunks);
    }
    /**
     * @deprecated (udoli potom)
     */
    @Deprecated(forRemoval = true)
    public GameChunk findById(String id) throws GameChunkNullException{
        return gameChunkRepository.findById(id).orElseThrow(GameChunkNullException::new);
    }
    public GameChunk findByIdAndRoundId(String id, Integer roundId) throws GameChunkNullException, RoundNullException {
        roundService.checkById(roundId);
        return gameChunkRepository.findByIdAndRoundId(id, roundId).orElseThrow(GameChunkNullException::new);
    }
    public List<GameChunk> findAllByRoundAndCity(Integer roundId, Integer cityId) throws RoundNullException, CityNullException {
        roundService.checkById(roundId);
        cityService.checkById(cityId);
        return gameChunkRepository.findAllByRoundIdAndCityId(roundId,cityId);
    }
    public List<GameChunk> findAllByOwnerAndRoundAndCity(GameUser gameUser, Round round, City city) {
        return gameChunkRepository.findAllByOwnerIdAndRoundIdAndCityId(gameUser.getId(), round.getId(), city.getId());
    }
    public List<GameChunk> findAllByRoundAndTeamAndCity(Round round, Team team, City city) {
        return gameChunkRepository.findAllByTeamIdAndRoundIdAndCityId(round.getId(), team.getId(), city.getId());
    }
    public void saveListOfChunkDTOs(List<ChunkDTO> chunkDTOList, GameUser user, Round round, City city){
        List<GameChunk> gameChunkList = chunkDTOList.stream().map(chunk ->
                GameChunk.builder().round(round).owner(user).city(city).id(chunk.getId()).build()
        ).toList();
        gameChunkRepository.saveAll(gameChunkList);
    }
}
