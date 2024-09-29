package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_friends")
public class Friend {
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Id
    @Column(name = "friend_id")
    private int friend_id;
}
