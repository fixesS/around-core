package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.user.GameUser;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select u from GameUser u left join UserRoundTeamCity urtc on u.id = urtc.user.id where (coalesce(null,:roundIds) is null or urtc.round.id in (:roundIds)) and (coalesce(null,:userIds) is null or u.id in (:userIds)) group by u.id order by sum(urtc.capturedChunks) desc")
    List<GameUser> getUsersStatTopForRoundsForChunksAll(@Param("roundIds") List<Integer> roundIds,@Param("userIds") List<Integer> userIds,Pageable pageable);
    @Query(value = "select u from GameUser u join UserRoundTeamCity urtc on u.id = urtc.user.id join GameChunk c on c.round.id = urtc.round.id and c.city.id = urtc.city.id and c.owner.id = u.id where (coalesce(null,:roundIds) is null or urtc.round.id in (:roundIds)) and (coalesce(null,:userIds) is null or u.id in (:userIds)) group by u.id order by count(c.id) desc")
    List<GameUser> getUsersStatTopForRoundsForChunksNow(@Param("roundIds") List<Integer> roundIds, @Param("userIds") List<Integer> userIds, Pageable pageable);
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into public.users_rounds_team_city (user_id, round_id, city_id, team_id ) VALUES (:userId, :roundId, :cityId,:teamId) on conflict(user_id,round_id,city_id) do  update set team_id = :teamId")
    void setTeamForRoundAndCity(@Param("userId") Integer userId, @Param("roundId") Integer roundId, @Param("cityId") Integer cityId, @Param("teamId") Integer teamId);
    @Query(nativeQuery = true, value = "select * from users u where u.id = (select uoauth2.user_id from public.users_oauth uoauth2 where uoauth2.oauth_id = :oauth_id and uoauth2.oauth_provider = CAST(:provider as public.oauth_providers_enum))")
    Optional<GameUser> findByOAuthIdAndProvider(@Param("oauth_id") String oauthId, @Param("provider") String provider);
    @Query(nativeQuery = true, value = " select exists (select uoauth2 from public.users_oauth uoauth2 where uoauth2.user_id = :userId and uoauth2.oauth_provider = CAST(:provider as public.oauth_providers_enum) )")
    boolean existsByUserIdAndProvider(@Param("userId") Integer userId, @Param("provider") String provider);
    @Query(nativeQuery = true, value = "SELECT nextval('users_id_seq') from public.users_id_seq;")
    BigDecimal getNextValMySequence();
}
