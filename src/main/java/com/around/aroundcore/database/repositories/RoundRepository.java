package com.around.aroundcore.database.repositories;


import com.around.aroundcore.database.models.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
    Optional<Round> findFirstByActiveIsTrue();
    @Query(nativeQuery = true,value = "select r from public.rounds r where r.id in (select urtc.round_id from public.users_rounds_team_city urtc where urtc.user_id = :userId);")
    List<Round> findByUser(@Param("UserId") Integer userId);
    boolean existsById(Integer id);
}