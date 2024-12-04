package com.around.aroundcore.web.dtos.stat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
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
    @Nullable
    private Integer team_id;
    @Nullable
    private Long chunks_all;
    private Integer chunks_now;
    @Nullable
    private List<ChunkStatDTO> chunks;
}
