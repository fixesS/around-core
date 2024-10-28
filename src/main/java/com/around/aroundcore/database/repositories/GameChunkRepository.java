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
    @Query(nativeQuery = true, value = "select * from chunks where chunks.round_id = :roundId and chunks.city_id = :cityId")
    List<GameChunk> findAllByRoundIdAndCityId(@Param("roundId") Integer roundId,@Param("cityId") Integer cityId);

    @Query(nativeQuery = true, value = "select * from chunks where chunks.round_id = :roundId and chunks.owner = :ownerId and chunks.city_id = :cityId")
    List<GameChunk> findAllByOwnerIdAndRoundIdAndCityId(@Param("ownerId") Integer ownerId, @Param("roundId") Integer roundId, @Param("cityId") Integer cityId);

    @Query(nativeQuery = true, value = "select * from chunks where chunks.round_id = :roundId and chunks.city_id = :cityId and chunks.owner in (select urt.user_id from users_rounds_team_city urt where urt.team_id = :teamId)")
    List<GameChunk> findAllByTeamIdAndRoundIdAndCityId(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId, @Param("cityId") Integer cityId);

    Boolean existsByIdAndRoundId(String id, Integer roundId);
    Optional<GameChunk> findByIdAndRoundId(String id, Integer roundId);
}
