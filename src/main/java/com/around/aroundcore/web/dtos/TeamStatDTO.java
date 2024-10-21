package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@Builder
@Schema(description = "DTO for team stat info")
public class TeamStatDTO {
    @Schema(description = "team id")
    private Integer id;
    private String color;
    private List<CityStatDTO> cities;

}
