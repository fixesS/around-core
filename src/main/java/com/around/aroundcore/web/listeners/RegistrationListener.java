package com.around.aroundcore.web.listeners;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.VerificationToken;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.VerificationTokenService;
import com.around.aroundcore.web.events.OnRegistrationCompleteEvent;
import com.around.aroundcore.web.services.EmailQueueService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private EmailQueueService emailQueueService;
    private GameUserService gameUserService;
    private VerificationTokenService tokenService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);

    }
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        GameUser user = event.getUser();
        VerificationToken token = tokenService.createTokenWithUser(user);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = AroundConfig.API_V1_REGISTRATION + "/confirm?token=" + token.getToken();
        String message = "Click the link to confirm registration: ";
        String text = message + "\r\n" + "http://localhost:8080/" + confirmationUrl;

        emailQueueService.addToQueue(recipientAddress,subject,text);
    }
}
