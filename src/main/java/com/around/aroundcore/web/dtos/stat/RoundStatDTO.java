package com.around.aroundcore.web.dtos.stat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for stat info of chunks for round")
public class RoundStatDTO {
    private Integer round_id;
    private List<CityStatDTO> cities;
}
