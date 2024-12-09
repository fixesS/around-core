package com.around.aroundcore.database.models.round;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "rounds_cities_winner_team", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RoundCityWinnerTeamEmbedded.class)
@Getter
public class RoundCityWinnerTeam implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "round_id")
    private Round round;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_id")
    private City city;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "winner_team_id")
    private Team team;

}
