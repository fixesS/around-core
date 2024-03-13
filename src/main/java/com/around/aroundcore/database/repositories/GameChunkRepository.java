package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameChunkRepository extends JpaRepository<GameChunk, Integer> {
}
