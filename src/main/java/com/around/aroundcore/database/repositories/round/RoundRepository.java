package com.around.aroundcore.database.repositories.round;


import com.around.aroundcore.database.models.round.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
    Optional<Round> findFirstByActiveIsTrue();
    @Query(value = "select r from Round r where r.id in (select urtc.round.id from UserRoundTeamCity urtc where urtc.user.id = :userId)")
    List<Round> findByUser(@Param("userId") Integer userId);
    boolean existsById(Integer id);
}