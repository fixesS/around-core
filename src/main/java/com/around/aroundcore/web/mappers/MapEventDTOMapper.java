package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.MapEventDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MapEventDTOMapper implements Function<MapEvent, MapEventDTO> {
    @Override
    public MapEventDTO apply(MapEvent event) {
        return MapEventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .url(event.getUrl())
                .provider(event.getProvider().getName())
                .is_ad(event.isAd())
                .starts(event.getStarts())
                .ends(event.getEnds())
                .chunks(event.getChunks().stream().map(gameChunk -> ChunkDTO.builder().id(gameChunk.getId()).build()).toList())
                .build();
    }
}
