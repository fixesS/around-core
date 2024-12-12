package com.around.aroundcore.database.repositories.event;

import com.around.aroundcore.database.models.event.EventProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProviderRepository extends JpaRepository<EventProvider, Integer> {
}
