package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameUserRepository extends JpaRepository<GameUser, Integer> {
    Optional<GameUser> findOneById(Integer id);
    Optional<GameUser> findByEmail(String email);
    Optional<GameUser> findByUsername(String username);
    List<GameUser> findByUsernameContaining(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query(nativeQuery = true,value = "SELECT u FROM public.users u left join public.users_rounds_team_city urtc on u.id = urtc.user_id  where ((:roundids) is null or urtc.round_id in (:roundids)) and u.id in (:userids is null or u.id in (:userids)) group by u.id order by sum(urtc.captured_chunks) desc limit :limit;")
    List<GameUser> getUsersStatTopForRoundsForChunksAll(@Param("roundIds") List<Integer> roundIds,@Param("userIds") List<Integer> userIds,@Param("limit") Integer limit);
    @Query(nativeQuery = true,value = "select u from public.users u join public.users_rounds_team_city urtc on u.id = urtc.user_id join public.chunks c on c.round_id = urtc.round_id and c.city_id = urtc.city_id and c.owner = u.id where ((:roundids) is null or urtc.round_id in (:roundids))  and u.id in ((:userids) is null or u.id in (:userids)) group by u.id, u.username order by count(c.id) desc limit :limit;")
    List<GameUser> getUsersStatTopForRoundsForChunksNow(@Param("roundIds") List<Integer> roundIds,@Param("userIds") List<Integer> userIds,@Param("limit") Integer limit);
    @Modifying
    @Query(nativeQuery = true, value = "update public.users_rounds_team_city set team_id = :teamId where user_id = :userId and round_id = :roundId")
    void setTeamForRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId, @Param("userId") Integer userId);
    @Modifying
    @Query(nativeQuery = true, value = "insert into public.users_rounds_team_city (user_id, round_id, team_id, city_id) VALUES (:userId, :roundId, :teamId, :cityId) on conflict(user_id,round_id) do  update set team_id = :teamId, city_id = :cityId")
    void createTeamAndCityForRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId,@Param("cityId") Integer cityId, @Param("userId") Integer userId);
    @Modifying
    @Query(nativeQuery = true, value = "update public.users_rounds_team_city set city_id = :cityId where user_id = :userId and round_id = :roundId")
    void setCityForRound(@Param("roundId") Integer roundId, @Param("cityId") Integer cityId, @Param("userId") Integer userId);

    @Query(nativeQuery = true, value = "select u from users u where u.id = (select uoauth2.user_id from public.users_oauth uoauth2 where uoauth2.oauth_id = :oauth_id and uoauth2.oauth_provider = CAST(:provider as public.oauth_providers_enum))")
    Optional<GameUser> findByOAuthIdAndProvider(@Param("oauth_id") String oauthId, @Param("provider") String provider);
    @Query(nativeQuery = true, value = " select exists (select uoauth2 from public.users_oauth uoauth2 where uoauth2.user_id = :userId and uoauth2.oauth_provider = CAST(:provider as public.oauth_providers_enum) )")
    boolean existsByUserIdAndProvider(@Param("userId") Integer userId, @Param("provider") String provider);
    @Query(value = "SELECT nextval('users_id_seq') from public.users_id_seq;", nativeQuery = true)
    BigDecimal getNextValMySequence();
}
