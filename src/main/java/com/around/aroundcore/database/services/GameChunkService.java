package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.repositories.GameChunkRepository;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameChunkService {
    private GameChunkRepository gameChunkRepository;

    @Transactional
    public void create(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }
    @Transactional
    public void update(GameChunk gameChunk){
        gameChunkRepository.save(gameChunk);
    }
    @Transactional
    public GameChunk findById(String id) throws GameChunkNullException{
        return gameChunkRepository.findById(id).orElseThrow(GameChunkNullException::new);
    }
    @Transactional
    public List<GameChunk> findAll(){
        return gameChunkRepository.findAll();
    }
    @Transactional
    public List<GameChunk> findAllByOwner(GameUser gameUser){
        return gameChunkRepository.findAllByOwner(gameUser);
    }
    @Transactional
    public List<GameChunk> findAllByOwnerTeam(Team team){
        return gameChunkRepository.findAllByOwnerTeam(team);
    }
}
