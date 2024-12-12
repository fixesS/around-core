package com.around.aroundcore.database.repositories.round;

import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.models.round.UserRoundTeamCityEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoundTeamRepository extends JpaRepository<UserRoundTeamCity, UserRoundTeamCityEmbedded> {
    @Query(value = "select urtc from UserRoundTeamCity urtc where urtc.user.id = :userId and urtc.round.id = :roundId and urtc.city.id = :cityId")
    Optional<UserRoundTeamCity> findByUserIdAndRoundIdAndCityId(@Param("userId") Integer userId,@Param("roundId") Integer roundId, @Param("cityId") Integer cityId);
    @Query(nativeQuery = true, value = "select sum(urtc.captured_chunks) FROM public.users_rounds_team_city urtc where (coalesce(null,:roundIds) is null or urtc.round_id in (:roundIds)) and (coalesce(null,:cityIds) is null or urtc.city_id in (:cityIds)) and  (coalesce(null,:teamIds) is null or urtc.team_id in (:teamIds))")
    Long getSumOfChunksAllByRoundIdsAndCityIdsAndTeamIds(@Param("roundIds") List<Integer> roundIds,@Param("cityIds") List<Integer> cityIds, @Param("teamIds") List<Integer> teamIds);
    @Query(nativeQuery = true, value = "select distinct on (urtc.round_id,urtc.city_id) urtc.* from public.users_rounds_team_city urtc where urtc.user_id = :userId;")
    List<UserRoundTeamCity> findByUserDistinctOnRoundAndCity(@Param("userId") Integer userId);
    @Modifying
    @Query(value = "update UserRoundTeamCity urtc set urtc.capturedChunks = urtc.capturedChunks + :amount where urtc.user.id = :userId and urtc.round.id = :roundId and urtc.city.id = :cityId")
    void increaseCapturedChunks(@Param("userId") Integer userId,@Param("roundId") Integer roundId, @Param("cityId") Integer cityId, @Param("amount") Integer amount);
}
