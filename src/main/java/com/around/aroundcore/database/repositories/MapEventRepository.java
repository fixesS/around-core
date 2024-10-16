package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.MapEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapEventRepository extends JpaRepository<MapEvent, Integer> {

    @Query(nativeQuery = true, value = "select * from map_events where map_events.verified = :verified and map_events.id in (select map_events_chunks.event_id from map_events_chunks where map_events_chunks.city_id = :cityId)")
    List<MapEvent> findAllByVerifiedAndCityId(@Param("verified") Boolean verified,@Param("cityId") Integer cityId );
    boolean existsByUrl(String url);
}
