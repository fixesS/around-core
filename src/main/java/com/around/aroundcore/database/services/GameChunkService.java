package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.repositories.GameChunkRepository;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import com.around.aroundcore.web.exceptions.entity.RoundNullException;
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
     * @deprecated (udoli potom) ok
     */
    @Deprecated(forRemoval = true)
    public GameChunk findById(String id) throws GameChunkNullException{
        return gameChunkRepository.findById(id).orElseThrow(GameChunkNullException::new);
    }
    public GameChunk findByIdAndRoundId(String id, Integer roundId) throws GameChunkNullException, RoundNullException {
        roundService.checkById(roundId);
        return gameChunkRepository.findByIdAndRoundId(id, roundId).orElseThrow(GameChunkNullException::new);
    }
    public List<GameChunk> findAllByRound(Integer roundId) throws RoundNullException{
        roundService.checkById(roundId);
        return gameChunkRepository.findAllByRound(roundId);
    }
    public List<GameChunk> findAllByOwner(GameUser gameUser){
        return gameChunkRepository.findAllByOwner(gameUser);
    }
    public List<GameChunk> findAllByUserRoundTeam(UserRoundTeam urt){
        return gameChunkRepository.findAllByTeamAndRound(urt.getRound().getId(), urt.getTeam().getId());
    }
    public void saveListOfChunkDTOs(List<ChunkDTO> chunkDTOList, GameUser user, Round round){
        List<GameChunk> gameChunkList = chunkDTOList.stream().map(chunk ->
                GameChunk.builder().round(round).owner(user).id(chunk.getId()).build()
        ).toList();
        gameChunkRepository.saveAll(gameChunkList);
    }
}
