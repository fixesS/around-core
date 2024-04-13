package com.around.aroundcore.web.tasks;

import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.database.services.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CheckTokensTask implements Runnable{
    private RecoveryTokenService recoveryTokenService;
    private VerificationTokenService verificationTokenService;
    @Override
    public void run() {
        recoveryTokenService.removeExpired();
        verificationTokenService.removeExpired();
        log.debug("Expired token have been removed");
    }
}
