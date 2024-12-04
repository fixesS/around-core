package com.around.aroundcore.web.mappers.chunk;

import com.around.aroundcore.web.dtos.ChunkDTO;

import java.util.function.Function;

public class StringGameChunkDTOMapper implements Function<String, ChunkDTO> {
    @Override
    public ChunkDTO apply(String id) {
        return ChunkDTO.builder()
                .id(id)
                .build();
    }
}
