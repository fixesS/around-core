package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@Builder
@ToString
@AllArgsConstructor
@Schema(description = "DTO for chunk info")
public class ChunkDTO {
    @Schema(description = "Chunk id")
    private String id;
    @Schema(description = "Chunk owner team id")
    private Integer team_id;
    @Schema(description = "Round where team owned chunk")
    private Integer round_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkDTO chunkDTO = (ChunkDTO) o;
        return Objects.equals(id, chunkDTO.id) && Objects.equals(round_id, chunkDTO.round_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, round_id);
    }
}
