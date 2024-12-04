package com.around.aroundcore.web.mappers.event;

import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.events.MapEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MapEventDTOMapper implements Function<MapEvent, MapEventDTO> {
    @Value("${around.time.locale}")
    private String timeLocale;
    private final CategoryDTOMapper categoryDTOMapper;

    @Override
    public MapEventDTO apply(MapEvent event) {
        return MapEventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .categories(event.getCategories().stream().map(categoryDTOMapper).toList())
                .url(event.getUrl())
                .provider(event.getProvider().getName())
                .is_ad(event.isAd())
                .image(event.getImage().getUrl())
                .description(event.getDescription())
                .starts(timestampToEpoch(event.getStarts()))
                .ends(timestampToEpoch(event.getEnds()))
                .chunks(event.getChunks().stream().map(gameChunk -> ChunkDTO.builder().id(gameChunk.getId()).build()).distinct().toList())
                .build();
    }
    private Long timestampToEpoch(LocalDateTime timestamp) {
        ZoneId zone = ZoneId.of(timeLocale);
        return timestamp.atZone(zone).toEpochSecond();
    }
}
