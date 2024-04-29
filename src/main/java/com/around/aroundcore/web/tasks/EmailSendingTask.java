package com.around.aroundcore.web.tasks;

import com.around.aroundcore.web.services.EmailQueueService;
import com.around.aroundcore.web.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class EmailSendingTask implements Runnable{
    private EmailService emailService;
    private EmailQueueService emailQueueService;

    @Override
    public void run() {
        if(!emailQueueService.isSimpleMessageQueueEmpty()){
            emailService.send(emailQueueService.getAllSimpleMessagesFromQueue());
            log.info("Simple email messages have been sent");
        }
        if(!emailQueueService.isMimeMessageQueueEmpty()){
            emailService.sendHTML(emailQueueService.getAllMimeMessagesFromQueue());
            log.info("Mime email messages have been sent");
        }
    }
}
