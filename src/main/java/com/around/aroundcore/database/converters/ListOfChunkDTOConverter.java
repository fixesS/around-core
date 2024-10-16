package com.around.aroundcore.database.converters;

import com.around.aroundcore.database.dtos.ChunkDTOForCity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class ListOfChunkDTOConverter implements AttributeConverter<List<ChunkDTOForCity>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<ChunkDTOForCity> chunkDTOS) {
        try {
            return objectMapper.writeValueAsString(chunkDTOS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ChunkDTOForCity> convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, new TypeReference<List<ChunkDTOForCity>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
