package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@Schema(description = "DTO for team info")
public class TeamDTO {
    private Integer id;
    private String color;
    @Nullable
    @Schema(description = "list of ids")
    private List<Integer> members;
}
