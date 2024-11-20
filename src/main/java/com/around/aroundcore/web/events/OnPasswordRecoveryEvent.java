package com.around.aroundcore.web.events;

import com.around.aroundcore.database.models.user.GameUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnPasswordRecoveryEvent{
    private GameUser user;
}
