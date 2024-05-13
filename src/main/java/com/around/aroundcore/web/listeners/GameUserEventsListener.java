package com.around.aroundcore.web.listeners;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.RecoveryToken;
import com.around.aroundcore.database.models.VerificationToken;
import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.database.services.VerificationTokenService;
import com.around.aroundcore.web.events.OnEmailVerificationEvent;
import com.around.aroundcore.web.events.OnPasswordRecoveryEvent;
import com.around.aroundcore.web.services.EmailQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@AllArgsConstructor
public class GameUserEventsListener {
    private EmailQueueService emailQueueService;
    private VerificationTokenService verificationTokenServiceService;
    private RecoveryTokenService recoveryTokenService;

    private final String HOME = "http://localhost:8080/";


    @EventListener
    public void handleEmailVerification(OnEmailVerificationEvent event) {
        GameUser user = event.getUser();
        VerificationToken token = verificationTokenServiceService.createTokenWithUser(user);

        String recipientAddress = user.getEmail();
        String subject = "Confirm your email address.";
        String link  = HOME+AroundConfig.API_V1_REGISTRATION + "/confirm?token=" + token.getToken();

        log.info(link);
        Context context = new Context();
        context.setVariable("link",link);

        try {
            emailQueueService.addMimeMessageToQueue(recipientAddress,subject,"email-verification", context);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
    @EventListener
    public void handlePasswordRecovery(OnPasswordRecoveryEvent event) {
        GameUser user = event.getUser();
        RecoveryToken token = recoveryTokenService.createTokenWithUser(user);

        String recipientAddress = user.getEmail();
        String subject = "Password recovery.";
        String link  = HOME+AroundConfig.API_V1_RECOVERY + "/resetPassword?token=" + token.getToken();

        Context context = new Context();
        context.setVariable("link",link);

        try {
            emailQueueService.addMimeMessageToQueue(recipientAddress,subject,"password-reset", context);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
