package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.user.GameUserSkill;
import com.around.aroundcore.web.dtos.SkillDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class SkillDTOWithCurrentLevelMapper implements Function<GameUserSkill, SkillDTO> {
    private final SkillDTOMapper skillDTOMapper;

    @Override
    public SkillDTO apply(GameUserSkill gameUserSkill) {
        SkillDTO skillDTO = skillDTOMapper.apply(gameUserSkill.getGameUserSkillEmbedded().getSkill());
        skillDTO.setCurrent_level(gameUserSkill.getCurrentLevel());
        return skillDTO;
    }
}
