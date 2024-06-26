package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsById(Integer id);
}
