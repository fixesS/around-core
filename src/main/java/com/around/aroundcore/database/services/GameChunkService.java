package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.repositories.GameChunkRepository;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GameChunkService {
    private GameChunkRepository gameChunkRepository;

    public void create(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }

    public void update(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }

    public void saveListOfChunks(List<GameChunk> gameChunks){
        gameChunkRepository.saveAll(gameChunks);
    }
    public GameChunk findById(String id) throws GameChunkNullException{
        return gameChunkRepository.findById(id).orElseThrow(GameChunkNullException::new);
    }
    public List<GameChunk> findAll(){
        return gameChunkRepository.findAll();
    }
    public List<GameChunk> findAllByOwner(GameUser gameUser){
        return gameChunkRepository.findAllByOwner(gameUser);
    }
    public List<GameChunk> findAllByOwnerTeam(Team team){
        return gameChunkRepository.findAllByOwnerTeam(team);
    }
    public void saveListOfChunkDTOs(List<ChunkDTO> chunkDTOList, GameUser user){
        List<GameChunk> gameChunkList = chunkDTOList.stream().map(chunk ->
                GameChunk.builder().owner(user).id(chunk.getId()).build()
        ).toList();
        gameChunkRepository.saveAll(gameChunkList);
    }
}
