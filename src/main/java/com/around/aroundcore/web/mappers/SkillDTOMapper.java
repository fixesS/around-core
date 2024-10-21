package com.around.aroundcore.web.mappers;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.web.dtos.SkillDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class SkillDTOMapper implements Function<Skill, SkillDTO> {
    @Override
    public SkillDTO apply(Skill skill) {
        return SkillDTO.builder()
                .id(Optional.ofNullable(skill.getId()).orElse(-1000))
                .name(Optional.ofNullable(skill.getName()).orElse(""))
                .description(Optional.ofNullable(skill.getDescription()).orElse(""))
                .image(AroundConfig.URL_ICON+skill.getImageName())
                .icon(AroundConfig.URL_ICON+skill.getIcon())
                .max_level(Optional.ofNullable(skill.getMaxLevel()).orElse(-1000))
                .cost(skill.getCost().getValue())
                .rule(skill.getRule().getValue())
                .build();
    }
}
