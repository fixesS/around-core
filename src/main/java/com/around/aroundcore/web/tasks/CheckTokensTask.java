package com.around.aroundcore.web.tasks;

import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.database.services.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CheckTokensTask {
    private final RecoveryTokenService recoveryTokenService;
    private final VerificationTokenService verificationTokenService;

    @Scheduled(fixedRate = 480000)
    public void removeExpiredTokens() {
        recoveryTokenService.removeExpired();
        verificationTokenService.removeExpired();
        log.debug("Expired token have been removed");
    }
}
