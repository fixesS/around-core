package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
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
    @Nullable
    private Integer team_id;
    private Integer number_of_chunks;
    @Nullable
    private List<ChunkStatDTO> chunks;
}
