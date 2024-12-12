package com.around.aroundcore.database.repositories.event;

import com.around.aroundcore.database.models.event.MapEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapEventRepository extends JpaRepository<MapEvent, Integer> {

    @Query(nativeQuery = true, value = "select events.* from map_events.events events where events.verified = :verified and  events.active = :active and events.id in (select events_chunks.event_id from map_events.events_chunks events_chunks where events_chunks.city_id = :cityId)")
    List<MapEvent>  findAllByVerifiedAndActiveAndCityId(@Param("verified") Boolean verified, @Param("active") Boolean active,@Param("cityId") Integer cityId );
    @Query(nativeQuery = true, value = "SELECT e.* from map_events.events e where e.verified = true and e.active = true and e.id in (select ec.event_id from map_events.events_chunks ec where ec.chunk_id in (:chunks)) and e.id not in (select eu.event_id from map_events.events_users eu where eu.user_id = :userId)")
    List<MapEvent> findVerifiedActiveByChunksAndNotVisitedByUser(@Param("chunks") List<String> chunkIds, @Param("userId") Integer userId);
    boolean existsByUrl(String url);
}
