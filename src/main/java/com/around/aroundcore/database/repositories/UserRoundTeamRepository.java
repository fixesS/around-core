package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.models.UserRoundTeamCityEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoundTeamRepository extends JpaRepository<UserRoundTeamCity, UserRoundTeamCityEmbedded> {
    @Query(nativeQuery = true, value = "select urtc from public.users_rounds_team_city urtc where urtc.team_id = :teamId and urtc.round_id = :roundId ;")
    List<UserRoundTeamCity> findByRoundIdAndTeamId(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId);
    @Query(nativeQuery = true, value = "select distinct on (urtc.round_id,urtc.city_id) urtc from public.users_rounds_team_city urtc where urtc.user_id = :userId;")
    List<UserRoundTeamCity> findByUserDistinctOnRoundAndCity(@Param("userId") Integer userId);
    @Modifying
    @Query(nativeQuery = true, value = "update public.users_rounds_team_city  set captured_chunks = captured_chunks + :amount where user_id = :userId and round_id = :roundId and city_id = :city_id;")
    void increaseCapturedChunks(@Param("userId") Integer userId,@Param("roundId") Integer roundId, @Param("cityId") Integer cityId, @Param("amount") Integer amount);
}
