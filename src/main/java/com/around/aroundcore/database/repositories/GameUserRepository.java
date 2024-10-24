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

    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.password, u.role, u.email, u.verified, u.captured_chunks\n" +
            "FROM users u\n" +
            "ORDER BY u.captured_chunks DESC\n" +
            "LIMIT 5;")
    List<GameUser> getStatTop5();
    @Query(nativeQuery = true, value = "SELECT u.id, u.level, u.coins, u.username, u.avatar, u.password, u.role, u.email, u.verified, u.captured_chunks\n" +
            "FROM users u\n" +
            "ORDER BY u.captured_chunks DESC\n" +
            "LIMIT 50;"
            )
    List<GameUser> getStatTopAll();
    @Modifying
    @Query(nativeQuery = true, value = "update public.users_rounds_team_city set team_id = :teamId where user_id = :userId and round_id = :roundId")
    void setTeamForRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId, @Param("userId") Integer userId);
    @Modifying
    @Query(nativeQuery = true, value = "insert into users_rounds_team_city (user_id, round_id, team_id, city_id) VALUES (:userId, :roundId, :teamId, :cityId) on conflict(user_id,round_id) do  update set team_id = :teamId, city_id = :cityId")
    void createTeamAndCityForRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId,@Param("cityId") Integer cityId, @Param("userId") Integer userId);
    @Modifying
    @Query(nativeQuery = true, value = "update public.users_rounds_team_city set city_id = :cityId where user_id = :userId and round_id = :roundId")
    void setCityForRound(@Param("roundId") Integer roundId, @Param("cityId") Integer cityId, @Param("userId") Integer userId);

    @Query(nativeQuery = true, value = "select * from users where users.id = (select users_oauth.user_id from users_oauth where oauth_id = :oauth_id and oauth_provider = CAST(:provider as public.oauth_providers_enum))")
    Optional<GameUser> findByOAuthIdAndProvider(@Param("oauth_id") Long oauthId, @Param("provider") String provider);
    @Query(nativeQuery = true, value = " select exists (select * from users_oauth where user_id = :userId and oauth_provider = (:provider) )")
    boolean existsByUserIdAndProvider(@Param("userId") Integer userId, @Param("provider") String provider);
}
