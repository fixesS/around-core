package com.around.aroundcore.web.events;

import com.around.aroundcore.database.models.GameUser;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class OnEmailVerificationEvent {
    private GameUser user;
}
