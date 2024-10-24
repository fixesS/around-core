package com.around.aroundcore.web.dtos.stat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for stat info of rounds for city")
public class CityStatDTO {
    private Integer city_id;
    private String name;
    private List<RoundStatDTO> rounds;
}
