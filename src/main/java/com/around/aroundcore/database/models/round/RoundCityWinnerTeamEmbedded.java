package com.around.aroundcore.database.models.round;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoundCityWinnerTeamEmbedded implements Serializable {
    private Round round;
    private City city;
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundCityWinnerTeamEmbedded that = (RoundCityWinnerTeamEmbedded) o;
        return Objects.equals(round, that.round) && Objects.equals(city, that.city) && Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, city, team);
    }
}
