package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for chunk info")
public class ChunkDTO {
    @Schema(description = "Chunk id")
    private String id;
    @Nullable
    @Schema(description = "Chunk owner team id")
    private Integer team_id;

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object instanceof ChunkDTO chunkDTO)
        {
            sameSame = Objects.equals(this.id, chunkDTO.getId());
        }

        return sameSame;
    }
    @Override
    public String toString(){
        return "ChunkDTO(id = "+this.id+",team_id = "+this.team_id+")";
    }
}
