package com.around.aroundcore.database.models;

import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Entity
@Table(name = "teams", schema = "public")
@Getter
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "color")
    private String color;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "users_rounds_team_city",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false),
                    @JoinColumn(name = "city_id", referencedColumnName = "city_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeamCity> userRoundTeamCity;

    @Override
    public String toString(){
        return String.format("team_id: %d, color: %s",this.id,this.color);
    }
}
