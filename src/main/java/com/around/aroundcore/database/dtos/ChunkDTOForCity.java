package com.around.aroundcore.database.dtos;

import com.around.aroundcore.database.converters.ChunkDTOForCityDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@JsonDeserialize(using = ChunkDTOForCityDeserializer.class)
@NoArgsConstructor
public class ChunkDTOForCity implements Serializable{
    @JsonSerialize(using = StringSerializer.class, as = String.class)
    private String id;

    @JsonCreator
    public ChunkDTOForCity(@JsonProperty("id") String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkDTOForCity that = (ChunkDTOForCity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
