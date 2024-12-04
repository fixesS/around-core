package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSettingsRepository extends JpaRepository<GameSettings, Integer> {
}
