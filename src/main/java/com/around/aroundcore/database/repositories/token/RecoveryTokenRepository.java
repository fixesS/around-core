package com.around.aroundcore.database.repositories.token;

import com.around.aroundcore.database.models.token.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Integer> {
    Optional<RecoveryToken> findByToken(String token);
}
