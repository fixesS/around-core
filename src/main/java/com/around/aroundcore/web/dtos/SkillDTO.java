package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for skill info")
public class SkillDTO {

    private Integer id;
    @Schema(description = "Skill name")
    private String name;
    @Schema(description = "Max level of skill")
    private Integer max_level;
    @Nullable
    @Schema(description = "Current level of skill for user. Shows if you get info about user's skills")
    private Integer current_level;
    @Schema(description = "Rule of skill by level(index in array)")
    private List<Integer> rule;
    @Schema(description = "Cost of skill by level(index in array)")
    private List<Integer> cost;
    @Schema(description = "Skill description")
    private String description;
    @Schema(description = "Image url / image name")
    private String image;
    @Schema(description = "Icon url / icon name")
    private String icon;
}
