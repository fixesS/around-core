package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameChunkRepository extends JpaRepository<GameChunk, String> {

    List<GameChunk> findAllByOwner(GameUser gameUser);
    List<GameChunk> findAllByOwnerTeam(Team team);
}
