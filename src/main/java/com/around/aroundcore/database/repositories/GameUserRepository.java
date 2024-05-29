package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameUserRepository extends JpaRepository<GameUser, Integer> {
    Optional<GameUser> findByEmail(String email);
    Optional<GameUser> findByUsername(String username);
    List<GameUser> findByUsernameContaining(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.team_id, u.city, u.password, u.role, u.email, u.verified, count(c.id) AS captured_cells\n" +
            "FROM game_user u\n" +
            "         JOIN game_chunk c ON u.id = c.owner\n" +
            "GROUP BY u.id\n" +
            "ORDER BY captured_cells DESC\n" +
            "LIMIT 5;")
    List<GameUser> getStatTop5();
    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.team_id, u.city, u.password, u.role, u.email, u.verified, count(c.id) AS captured_cells\n" +
            "FROM game_user u\n" +
            "         JOIN game_chunk c ON u.id = c.owner\n" +
            "GROUP BY u.id\n" +
            "ORDER BY captured_cells DESC\n"+
            "LIMIT 50;"
            )
    List<GameUser> getStatTopAll();
}
