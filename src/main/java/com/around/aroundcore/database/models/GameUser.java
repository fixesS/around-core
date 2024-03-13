package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "game_user")
@Data
public class GameUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "level")
    private int level;

    @Column(name = "coins")
    private int coins;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "city")
    private String city;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @OneToMany(mappedBy = "owner")
    private List<GameChunk> capturedChunks;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private List<GameUser> friends;
}
