package com.around.aroundcore.web.dtos.events;

import com.around.aroundcore.web.dtos.ChunkDTO;
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
@Schema(description = "DTO for map-event info")
public class MapEventDTO {
    private Integer id;
    private String name;
    private String description;
    private List<CategoryDTO> categories;
    private String provider;
    private Long starts;
    private Long ends;
    private String url;
    private Boolean is_ad;
    private String image;
    @Schema(description = "chunks where map-event is located")
    private List<ChunkDTO> chunks;
}
