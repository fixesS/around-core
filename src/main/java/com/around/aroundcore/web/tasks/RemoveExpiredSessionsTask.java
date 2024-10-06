package com.around.aroundcore.web.tasks;

import com.around.aroundcore.database.services.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RemoveExpiredSessionsTask {
    private final SessionService sessionService;

    @Scheduled(fixedRate = 480000)
    private void removeExpiredSessions() {
        sessionService.removeExpired();
        log.debug("Expired sessions have been removed");
    }
}
