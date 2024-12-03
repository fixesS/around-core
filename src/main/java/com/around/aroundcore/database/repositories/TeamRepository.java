package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsById(Integer id);

    @Query(nativeQuery = true,value = "select t.* from public.teams t where t.id in (select urt.team_id from public.users_rounds_team_city urt where user_id = :userId and round_id in (select r.id from public.rounds r where active = true))")
    Optional<Team> findTeamByCurrentRoundForUser(@Param("userId") Integer userId );

    @Query(nativeQuery = true, value = "select t.* from public.teams t order by random() limit 1;")
    Team getRandomTeam();

    @Query(nativeQuery = true,value = """
WITH team_chunks_count AS (
    SELECT
        urtc.team_id,
        COUNT(*) AS chunk_count
    FROM
        public.chunks AS c
            LEFT JOIN
        public.users_rounds_team_city AS urtc
        ON c.owner = urtc.user_id
            AND c.round_id = urtc.round_id
            AND c.city_id = urtc.city_id
    WHERE
        c.round_id = :roundId
    GROUP BY
        urtc.team_id
),
     max_chunks AS (
         SELECT
             MAX(chunk_count) AS max_chunk_count
         FROM
             team_chunks_count
     ),
     candidate_teams AS (
         SELECT
             team_id,
             chunk_count
         FROM
             team_chunks_count
         WHERE
             chunk_count = (SELECT max_chunk_count FROM max_chunks)
     ),
     total_teams AS (
         SELECT
             COUNT(*) AS num_candidates
         FROM
             candidate_teams
     )
SELECT
    t.*
FROM
    candidate_teams AS ct
        JOIN
    public.teams AS t
    ON ct.team_id = t.id
WHERE
    (SELECT num_candidates FROM total_teams) >= 1;
""")
    List<Team> getWinnerForRound(@Param("roundId") Integer roundId );
}
