package com.around.aroundcore.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class ChunkDTO {
    private String id;
    @Nullable
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
}
