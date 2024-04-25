package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.MapEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapEventRepository extends JpaRepository<MapEvent, Integer> {
    List<MapEvent> findAllByVerified(Boolean verified);
    boolean existsByUrl(String url);
}
