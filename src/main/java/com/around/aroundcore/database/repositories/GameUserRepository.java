package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameUserRepository extends JpaRepository<GameUser, Integer> {
    Optional<GameUser> findByUsername(String username);
}