package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    @Query(value = "select s from Session s where s.sessionUuid = :uuid")
    Optional<Session> findOneBySessionUuid(UUID uuid);
    void deleteAllByUser(GameUser gameUser);
}
