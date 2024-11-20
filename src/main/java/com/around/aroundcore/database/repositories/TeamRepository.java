package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsById(Integer id);

    @Query(nativeQuery = true,value = "select t.* from public.teams t where t.id in (select urt.team_id from public.users_rounds_team_city urt where user_id = :userId and round_id in (select r.id from public.rounds r where active = true))")
    Optional<Team> findTeamByCurrentRoundForUser(@Param("userId") Integer userId );

    @Query(nativeQuery = true, value = "select t.* from public.teams t order by random() limit 1;")
    Team getRandomTeam();
}
