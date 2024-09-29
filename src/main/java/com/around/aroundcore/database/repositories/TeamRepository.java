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

    @Query(value = "select t from team t where t.id in (select urt.team_id from user_round_team urt where user_id = :gameuser and round_id in (select r.id from Round r where active = true))", nativeQuery = true)
    Optional<Team> findTeamByCurrentRoundForUser(@Param("gameuser") Integer user_id );
}
;