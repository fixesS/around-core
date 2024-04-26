package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.EventProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProviderRepository extends JpaRepository<EventProvider, Integer> {
}
