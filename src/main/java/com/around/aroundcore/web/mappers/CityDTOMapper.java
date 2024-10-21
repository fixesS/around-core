package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.CityDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CityDTOMapper implements Function<City, CityDTO> {
    @Override
    public CityDTO apply(City city) {
        List<ChunkDTO> chunks = city.getChunks().stream().map(gameChunk -> ChunkDTO.builder().id(gameChunk.getId()).build()).toList();

        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .locale(city.getLocale())
                .chunks(chunks)
                .build();
    }
}
