package com.around.aroundcore.core.listeners;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.token.RecoveryToken;
import com.around.aroundcore.database.models.token.VerificationToken;
import com.around.aroundcore.database.services.RecoveryTokenService;
import com.around.aroundcore.database.services.VerificationTokenService;
import com.around.aroundcore.core.events.OnEmailVerificationEvent;
import com.around.aroundcore.core.events.OnPasswordRecoveryEvent;
import com.around.aroundcore.core.services.queues.EmailQueueService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
public class GameUserEventsListener {
    private final EmailQueueService emailQueueService;
    private final VerificationTokenService verificationTokenServiceService;
    private final RecoveryTokenService recoveryTokenService;

    @Value("${around.home}")
    private String serverHome;


    @EventListener
    public void handleEmailVerification(OnEmailVerificationEvent event) {
        GameUser user = event.getUser();
        VerificationToken token = verificationTokenServiceService.createTokenWithUser(user);

        String recipientAddress = user.getEmail();
        String subject = "Confirm your email address.";
        String link  = serverHome +AroundConfig.API_V1_REGISTRATION + "/confirm?token=" + token.getToken();

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
        String link  = serverHome +AroundConfig.API_V1_RECOVERY + "/resetPassword?token=" + token.getToken();

        Context context = new Context();
        context.setVariable("link",link);

        try {
            emailQueueService.addMimeMessageToQueue(recipientAddress,subject,"password-reset", context);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
