package com.around.aroundcore.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for map-event info")
public class MapEventDTO {
    private Integer id;
    private String name;
    private String provider;
    private LocalDateTime starts;
    private LocalDateTime ends;
    private String url;
    @Schema(description = "chunks where map-event is located")
    private List<ChunkDTO> chunks;
}
