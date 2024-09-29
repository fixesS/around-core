package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.city, u.password, u.role, u.email, u.verified, count(c.id) AS captured_cells\n" +
            "FROM game_user u\n" +
            "         JOIN game_chunk c ON u.id = c.owner\n" +
            "GROUP BY u.id\n" +
            "ORDER BY captured_cells DESC\n" +
            "LIMIT 5;")
    List<GameUser> getStatTop5();
    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.city, u.password, u.role, u.email, u.verified, count(c.id) AS captured_cells\n" +
            "FROM game_user u\n" +
            "         JOIN game_chunk c ON u.id = c.owner\n" +
            "GROUP BY u.id\n" +
            "ORDER BY captured_cells DESC\n"+
            "LIMIT 50;"
            )
    List<GameUser> getStatTopAll();
    @Modifying
    @Query(nativeQuery = true, value = "insert into user_round_team (user_id, round_id, team_id) VALUES (:userId, :roundId, :teamId) on conflict(user_id,round_id) do  update set team_id = :teamId")
    void setTeamForRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId, @Param("userId") Integer userId);
}
