package com.around.aroundcore.web.services;

import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.chunk.WrongChunkResolution;
import com.around.aroundcore.web.mappers.StringGameChunkDTOMapper;
import com.uber.h3core.H3Core;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class H3ChunkService {
    private final H3Core h3Core;
    private final StringGameChunkDTOMapper stringGameChunkDTOMapper;

    /**
     * for radius = 1
     * 0 - self
     * 1 - left-bottom
     * 2 - bottom
     * 3 - right-bottom
     * 4 - right-top
     * 5 - top
     * 6 - left-top
    */
    public List<String> getNeighboursForSkillWithLevel(String chunkId, int level){
        if(h3Core.getResolution(chunkId) != 11){
            throw new WrongChunkResolution();
        }
        level++;
        int radius = 2;
        if(level > 18){
            radius+=1;
        }
        List<String> neighbours = h3Core.gridDisk(chunkId, radius);
        while (level < neighbours.size()) {
            neighbours.remove(level);
        }
        return neighbours;
    }
    public List<ChunkDTO> getChunksForWidthSkill(String chunkId, GameUserSkill userSkill){
        List<String> neighbours = getNeighboursForSkillWithLevel(chunkId, userSkill.getCurrentLevel());
        List<ChunkDTO> chunkDTOList = neighbours.stream().map(stringGameChunkDTOMapper).toList();
        chunkDTOList.forEach(chunkDTO -> chunkDTO.setTeam_id(userSkill.getGameUserSkillEmbedded().getGameUser().getTeam().getId()));
        return chunkDTOList;
    }
}