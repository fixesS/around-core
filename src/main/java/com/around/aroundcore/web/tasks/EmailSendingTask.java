package com.around.aroundcore.web.tasks;

import com.around.aroundcore.web.services.EmailQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class EmailSendingTask implements Runnable{
    private JavaMailSender mailSender;
    private EmailQueueService emailQueueService;

    @Override
    public void run() {
        if(!emailQueueService.isEmpty()){
            List<SimpleMailMessage> messageList = emailQueueService.getAllFromQueue();
            SimpleMailMessage[] array = new SimpleMailMessage[messageList.size()];
            messageList.toArray(array);
            mailSender.send(array);
        }
    }
}
