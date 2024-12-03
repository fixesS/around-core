package com.around.aroundcore.database.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "users_followers", schema = "public")
public class Follower {
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Id
    @Column(name = "follower_id")
    private int follower_id;

    @Override
    public int hashCode() {
        return Objects.hash(user_id, follower_id);
    }
}
