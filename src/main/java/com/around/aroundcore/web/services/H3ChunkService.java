package com.around.aroundcore.web.services;

import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.chunk.WrongChunkResolution;
import com.around.aroundcore.web.exceptions.api.entity.RoundNullException;
import com.around.aroundcore.web.mappers.chunk.StringGameChunkDTOMapper;
import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class H3ChunkService {
    private final H3Core h3Core;
    private final StringGameChunkDTOMapper stringGameChunkDTOMapper;
    private final Integer resolution;

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
    //important: int value 0 returns empty list. It's ok.
    public List<String> getNeighboursForSkillRuleValue(String chunkId, int value){
        if(h3Core.getResolution(chunkId) != resolution){
            throw new WrongChunkResolution();
        }
        int radius = 2;
        if(value > 18){
            radius+=1;
        }
        List<String> neighbours = h3Core.gridDisk(chunkId, radius);
        while (value < neighbours.size()) {
            neighbours.remove(value);
        }
        return neighbours;
    }
    public List<ChunkDTO> getChunksForWidthSkill(String chunkId, GameUserSkill userSkill){
        Skill skill = userSkill.getGameUserSkillEmbedded().getSkill();
        Integer skillRuleValue = skill.getRule().getValue().get(userSkill.getCurrentLevel());
        List<String> neighbours = getNeighboursForSkillRuleValue(chunkId, skillRuleValue);
        List<ChunkDTO> chunkDTOList = neighbours.stream().map(stringGameChunkDTOMapper).toList();
        UserRoundTeamCity urt = userSkill.getGameUserSkillEmbedded().getGameUser().getUserRoundTeamCities().stream().filter(urt1->urt1.getRound().getActive()).findFirst().orElseThrow(RoundNullException::new);
        chunkDTOList.forEach(chunkDTO -> chunkDTO.setTeam_id(urt.getTeam().getId()));
        return chunkDTOList;
    }
    public List<ChunkDTO> getChunkByLatLon(Double lat, Double lon, int radius){
        String id =  h3Core.latLngToCellAddress(lat,lon,resolution);
        List<String> neighbours = h3Core.gridDisk(id, radius);
        return neighbours.stream().map(stringGameChunkDTOMapper).toList();
    }
    public String getParentId(String childChunkId, Integer parentResolution){
        return h3Core.cellToParentAddress(childChunkId, parentResolution);
    }
}