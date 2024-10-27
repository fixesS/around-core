package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.web.dtos.SkillDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class SkillDTOMapper implements Function<Skill, SkillDTO> {
    @Value("${around.home}")
    private String aroundHome;
    @Override
    public SkillDTO apply(Skill skill) {
        return SkillDTO.builder()
                .id(Optional.ofNullable(skill.getId()).orElse(-1000))
                .name(Optional.ofNullable(skill.getName()).orElse(""))
                .description(Optional.ofNullable(skill.getDescription()).orElse(""))
                .image(skill.getImage().getUrl())
                .icon(skill.getIcon().getUrl())
                .max_level(Optional.ofNullable(skill.getMaxLevel()).orElse(-1000))
                .cost(skill.getCost().getValue())
                .rule(skill.getRule().getValue())
                .build();
    }
}
