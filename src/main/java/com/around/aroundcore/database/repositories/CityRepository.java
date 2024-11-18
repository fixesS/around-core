package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    @Query(nativeQuery = true,value = "select c from public.cities c where c.id in (select urtc.city_id from public.users_rounds_team_city urtc where urtc.user_id = :userId and round_id = :roundId);")
    List<City> findCitiesByUserAndRound(@Param("userId") Integer userId, @Param("roundId") Integer roundId);
}
