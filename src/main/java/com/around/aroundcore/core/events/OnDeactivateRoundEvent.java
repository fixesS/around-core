package com.around.aroundcore.core.events;

import com.around.aroundcore.database.models.round.Round;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnDeactivateRoundEvent {
    private Round round;
}
