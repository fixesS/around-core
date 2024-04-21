package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.SkillDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class SkillDTOMapper implements Function<Skill, SkillDTO> {
    @Override
    public SkillDTO apply(Skill skill) {
        return SkillDTO.builder()
                .name(Optional.ofNullable(skill.getName()).orElse(""))
                .description(Optional.ofNullable(skill.getDescription()).orElse(""))
                .image(Optional.ofNullable(skill.getImageName()).orElse(""))
                .max_level(Optional.ofNullable(skill.getMaxLevel()).orElse(-1000))
                .cost(skill.getCost().getValue())
                .rule(skill.getRule().getValue())
                .build();
    }
}
