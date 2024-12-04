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
    private Integer friendId;
    private GameUserDTO gameUserDTO;
    private double latitude;
    private double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameUserLocationDTO that = (GameUserLocationDTO) o;
        return Objects.equals(friendId, that.friendId) && Objects.equals(gameUserDTO, that.gameUserDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, gameUserDTO);
    }
}
