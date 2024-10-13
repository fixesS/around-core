package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return user_id == friend.user_id && friend_id == friend.friend_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, friend_id);
    }
}
