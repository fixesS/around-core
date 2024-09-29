package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameChunkRepository extends JpaRepository<GameChunk, String> {
    @Query(nativeQuery = true, value = "select * from game_chunk where game_chunk.round_id = :roundId")
    List<GameChunk> findAllByRound(@Param("roundId") Integer roundId);

    Optional<GameChunk> findByIdAndRoundId(String id, Integer roundId);

    List<GameChunk> findAllByOwner(GameUser gameUser);

    @Query(nativeQuery = true, value = "select * from game_chunk where game_chunk.round_id = :roundId and game_chunk.owner in (select urt.user_id from user_round_team urt where urt.team_id = :teamId)")
    List<GameChunk> findAllByTeamAndRound(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId);
}
