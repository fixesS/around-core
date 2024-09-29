package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;
@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for chunk stat info")
public class ChunkStatDTO {
    @Schema(description = "Chunk id")
    private String id;

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
        return "ChunkDTO(id = "+this.id+")";
    }
}
