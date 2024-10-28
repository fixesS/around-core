package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.MapEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

@Service
public class MapEventDTOMapper implements Function<MapEvent, MapEventDTO> {
    @Value("${around.time.locale}")
    private String timeLocale;

    @Override
    public MapEventDTO apply(MapEvent event) {
        return MapEventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .url(event.getUrl())
                .provider(event.getProvider().getName())
                .is_ad(event.isAd())
                .image(event.getImage().getUrl())
                .starts(timestampToEpoch(event.getStarts()))
                .ends(timestampToEpoch(event.getEnds()))
                .chunks(event.getChunks().stream().map(gameChunk -> ChunkDTO.builder().id(gameChunk.getId()).build()).toList())
                .build();
    }
    private Long timestampToEpoch(LocalDateTime timestamp) {
        ZoneId zone = ZoneId.of(timeLocale);
        return timestamp.atZone(zone).toEpochSecond();
    }
}
