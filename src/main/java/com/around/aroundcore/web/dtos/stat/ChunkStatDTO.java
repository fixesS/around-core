package com.around.aroundcore.web.dtos.stat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;
@Data
@Builder
@AllArgsConstructor
@Schema(description = "DTO for chunk stat info")
public class ChunkStatDTO {
    @Schema(description = "Chunk id")
    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkStatDTO that = (ChunkStatDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString(){
        return "ChunkDTO(id = "+this.id+")";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
