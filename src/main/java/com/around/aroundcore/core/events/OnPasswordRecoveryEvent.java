package com.around.aroundcore.core.events;

import com.around.aroundcore.database.models.user.GameUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnPasswordRecoveryEvent{
    private GameUser user;
}
