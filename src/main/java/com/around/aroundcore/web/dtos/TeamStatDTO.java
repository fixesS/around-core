package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for team stat info")
public class TeamStatDTO {
    @Schema(description = "team id")
    private Integer id;
    @Schema(description = "number of captured chunks")
    private Integer number;
}
