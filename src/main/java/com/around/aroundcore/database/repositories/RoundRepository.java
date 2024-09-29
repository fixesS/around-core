package com.around.aroundcore.database.repositories;


import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.UserRoundTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
    Optional<Round> findFirstByActiveIsTrue();
    boolean existsById(Integer id);
}