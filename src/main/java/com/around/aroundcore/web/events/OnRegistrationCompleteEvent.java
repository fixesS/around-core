package com.around.aroundcore.web.events;

import com.around.aroundcore.database.models.GameUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    @Getter
    private GameUser user;

    public OnRegistrationCompleteEvent(Object source, GameUser user) {
        super(source);
        this.user = user;
    }

}
