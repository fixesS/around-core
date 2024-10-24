package com.around.aroundcore.web.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class GameUserLocationDTO {
    @JsonIgnore
    private String name;
    private GameUserDTO gameUserDTO;
    private double latitude;
    private double longitude;

    @Override
    public boolean equals(Object object) {
        boolean same = false;

        if (object instanceof GameUserLocationDTO gameUserLocationDTO) {
            same = Objects.equals(this.name, gameUserLocationDTO.getName()) && Objects.equals(this.gameUserDTO.getId(), gameUserLocationDTO.getGameUserDTO().getId());
        }

        return same;
    }

    @Override
    public String toString() {
        return "GameUserLocationDTO{" +
                "name='" + name + '\'' +
                ", gameUserDTO=" + gameUserDTO +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
