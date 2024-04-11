package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
//    @Query(value = "SELECT * FROM verification_tokens WHERE token=:token",nativeQuery = true)
    Optional<VerificationToken> findByToken(String token);
}
