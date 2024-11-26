package com.around.aroundcore.web.dtos.round;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for round info")
public class RoundDTO {
    private Integer id;
    private String name;
    @Schema(description = "Time when round is starts in unix")
    private Long starts;
    @Schema(description = "Time when round is ends in unix")
    private Long ends;
    private Boolean active;
}
