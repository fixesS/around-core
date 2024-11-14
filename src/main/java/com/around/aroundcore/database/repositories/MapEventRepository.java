package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.MapEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapEventRepository extends JpaRepository<MapEvent, Integer> {

    @Query(nativeQuery = true, value = "select * from map_events where map_events.verified = :verified and  map_events.active = :active and map_events.id in (select map_events_chunks.event_id from map_events_chunks where map_events_chunks.city_id = :cityId)")
    List<MapEvent>  findAllByVerifiedAndActiveAndCityId(@Param("verified") Boolean verified, @Param("active") Boolean active,@Param("cityId") Integer cityId );
    @Query(nativeQuery = true, value = "SELECT me from map_events me where me.verified = true and me.active = true and me.id in (select mec.event_id from map_events_chunks mec where mec.chunk_id in (:chunks)) and me.id not in (select meu.event_id from map_events_users meu where meu.user_id = :userId)")
    List<MapEvent> findVerifiedActiveByChunksAndNotVisitedByUser(@Param(":chunks") List<String> chunkIds, @Param("userId") Integer userId);
    boolean existsByUrl(String url);
}
