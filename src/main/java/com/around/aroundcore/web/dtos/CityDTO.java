package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for city info")
public class CityDTO {
    private int id;
    private String name;
    private String locale;
    @Schema(description = "JSON of list of chunkDTO (must be only id)")
    private List<ChunkDTO> chunks;
}
