package com.around.aroundcore.core.tasks;

import com.around.aroundcore.database.services.*;
import com.around.aroundcore.database.services.event.MapEventService;
import com.around.aroundcore.database.services.token.RecoveryTokenService;
import com.around.aroundcore.database.services.token.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RemoveExpiredScheduledTask {
    private final SessionService sessionService;
    private final RecoveryTokenService recoveryTokenService;
    private final VerificationTokenService verificationTokenService;
    private final MapEventService mapEventService;

    @Scheduled(fixedRate = 480000)
    public void removeExpiredTokens() {
        recoveryTokenService.removeExpired();
        verificationTokenService.removeExpired();
        log.debug("Expired tokens have been removed");
    }

    @Scheduled(fixedRate = 480000)
    private void removeExpiredSessions() {
        sessionService.removeExpired();
        log.debug("Expired sessions have been removed");
    }
    @Scheduled(fixedRate = 900000)
    private void disableEndedEvents(){
        mapEventService.disableEndedEvents();
    }
}
