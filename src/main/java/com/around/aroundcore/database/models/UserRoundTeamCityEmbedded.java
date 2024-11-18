package com.around.aroundcore.database.models;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoundTeamCityEmbedded implements Serializable {
    private Round round;
    private GameUser user;
    private City city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoundTeamCityEmbedded that = (UserRoundTeamCityEmbedded) o;
        return Objects.equals(round, that.round) && Objects.equals(user, that.user) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, user, city);
    }
}
