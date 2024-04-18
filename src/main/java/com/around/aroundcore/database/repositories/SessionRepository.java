package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findBySessionUuid(UUID uuid);
    void deleteAllByUser(GameUser gameUser);
}
