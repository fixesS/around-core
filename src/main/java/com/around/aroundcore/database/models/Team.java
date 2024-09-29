package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Table(name = "team")
@Getter
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "color")
    private String color;

    @OneToMany
    @JoinTable(
            name = "user_round_team",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeam> userRoundTeam;

    public List<GameUser> getMembersForRound(Integer roundId){
        return userRoundTeam.stream().filter(urt->urt.getRound().getId().equals(roundId)).map(UserRoundTeam::getUser).toList();
    }
    public List<GameChunk> getMembersChunksForRound(Integer roundId){
        var users = getMembersForRound(roundId);
        return users.stream().map(user -> user.getCapturedChunks(roundId)).flatMap(List::stream).toList();

    }

    @Override
    public String toString(){
        return String.format("team_id: %d, color: %s",this.id,this.color);
    }
}
