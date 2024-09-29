package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "round")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Round implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "user_round_team",
            joinColumns = @JoinColumn(name = "round_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeam> userRoundTeam;

    @Column
    private String name;

    @Column
    private LocalDateTime starts;

    @Column
    private LocalDateTime ends;

    @Column
    private Boolean active;
}
