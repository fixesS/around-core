package com.around.aroundcore.database.converters;

import com.around.aroundcore.database.dtos.ChunkDTOForCity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ChunkDTOForCityDeserializer extends JsonDeserializer<ChunkDTOForCity> {

    @Override
    public ChunkDTOForCity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String id = jsonParser.getText();
        return new ChunkDTOForCity(id);
    }
}
