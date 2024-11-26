package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameSettings;
import com.around.aroundcore.database.repositories.GameSettingsRepository;
import com.around.aroundcore.core.exceptions.api.entity.GameSettingNullException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class GameSettingService {
    private final GameSettingsRepository gameSettingsRepository;
    public GameSettings findById(Integer id) throws GameSettingNullException {
        return gameSettingsRepository.findById(id).orElseThrow(GameSettingNullException::new);
    }
}
