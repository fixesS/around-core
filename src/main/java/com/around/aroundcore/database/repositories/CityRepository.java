package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
