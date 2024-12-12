package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.chunk.GameChunk;
import com.around.aroundcore.database.models.chunk.GameChunkEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameChunkRepository extends JpaRepository<GameChunk, GameChunkEmbedded> {
    @Query(value = "select chunk from GameChunk chunk where chunk.round.id = :roundId and chunk.city.id = :cityId and ((chunk.owner is null and :nullable is true) or (chunk.owner is not null and :nullable is false ))")
    List<GameChunk> findAllByRoundIdAndCityId(@Param("roundId") Integer roundId,@Param("cityId") Integer cityId, @Param("nullable") Boolean nullable);
    @Query(value = "select chunk from GameChunk chunk where chunk.round.id= :roundId and chunk.owner.id = :ownerId and chunk.city.id = :cityId")
    List<GameChunk> findAllByOwnerIdAndRoundIdAndCityId(@Param("ownerId") Integer ownerId, @Param("roundId") Integer roundId, @Param("cityId") Integer cityId);
    @Query(nativeQuery = true, value = "select chunk.* from public.chunks chunk where chunk.round_id = :roundId and chunk.city_id = :cityId and chunk.owner in (select urt.user_id from public.users_rounds_team_city urt where urt.team_id = :teamId and urt.round_id = :roundId and urt.city_id = :cityId);")
    List<GameChunk> findAllByTeamIdAndRoundIdAndCityId(@Param("roundId") Integer roundId, @Param("teamId") Integer teamId, @Param("cityId") Integer cityId);
    @Modifying
    @Query( value = "delete from GameChunk chunk where chunk.round.id = :roundId AND chunk.city.id = :cityId and chunk.owner.id = :userId")
    void deleteChunks(@Param("userId") Integer userId, @Param("roundId") Integer roundId, @Param("cityId") Integer cityId);
    Boolean existsByIdAndRoundId(String id, Integer roundId);
    Optional<GameChunk> findByIdAndRoundId(String id, Integer roundId);
}
