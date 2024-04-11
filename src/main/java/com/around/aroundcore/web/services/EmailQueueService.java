package com.around.aroundcore.web.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EmailQueueService {
    private LinkedBlockingQueue<SimpleMailMessage> mailMessages = new LinkedBlockingQueue<>();
    public SimpleMailMessage getFromQueue() {
        return mailMessages.poll();
    }
    public void addToQueue(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailMessages.add(message);
    }
    public List<SimpleMailMessage> getAllFromQueue() {
        List<SimpleMailMessage> messages = new ArrayList<>();
        while (!isEmpty()) {
            messages.add(getFromQueue());
        }
        return messages;
    }
    public boolean isEmpty(){
        return mailMessages.isEmpty();
    }
}
