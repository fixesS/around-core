package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.database.models.UserRoundTeamCityEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoundTeamRepository extends JpaRepository<UserRoundTeamCity, UserRoundTeamCityEmbedded> {
    @Query(nativeQuery = true, value = "select urtc from public.users_rounds_team_city urtc where urtc.team_id = :teamId and urtc.round_id = :roundId ;")
    List<UserRoundTeamCity> findByRoundIdAndTeamId(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId);
}
