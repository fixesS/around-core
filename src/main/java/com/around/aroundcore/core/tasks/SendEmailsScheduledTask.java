package com.around.aroundcore.core.tasks;

import com.around.aroundcore.core.services.queues.EmailQueueService;
import com.around.aroundcore.core.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SendEmailsScheduledTask {
    private EmailService emailService;
    private EmailQueueService emailQueueService;

    @Scheduled(fixedRate = 1000)
    public void sendEmailMessages() {
        if(!emailQueueService.isSimpleMessageQueueEmpty()){
            emailService.send(emailQueueService.getAllSimpleMessagesFromQueue());
            log.debug("Simple email messages have been sent");
        }
        if(!emailQueueService.isMimeMessageQueueEmpty()){
            emailService.sendHTML(emailQueueService.getAllMimeMessagesFromQueue());
            log.debug("Mime email messages have been sent");
        }
    }
}
