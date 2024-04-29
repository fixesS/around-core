package com.around.aroundcore.web.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class EmailQueueService {

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    private LinkedBlockingQueue<SimpleMailMessage> simpleMailMessages = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<MimeMessage> mimeMessages = new LinkedBlockingQueue<>();

    @Autowired
    public EmailQueueService(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    public SimpleMailMessage getSimpleMessageFromQueue() {
        return simpleMailMessages.poll();
    }
    public MimeMessage getMimeMessageFromQueue() {
        return mimeMessages.poll();
    }
    public void addSimpleMessageToQueue(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        simpleMailMessages.add(message);
    }
    public void addMimeMessageToQueue(String to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String htmlContent = templateEngine.process(templateName, context);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mimeMessages.add(message);
    }
    public List<SimpleMailMessage> getAllSimpleMessagesFromQueue() {
        List<SimpleMailMessage> messages = new ArrayList<>();
        while (!isSimpleMessageQueueEmpty()) {
            messages.add(getSimpleMessageFromQueue());
        }
        return messages;
    }
    public List<MimeMessage> getAllMimeMessagesFromQueue() {
        List<MimeMessage> messages = new ArrayList<>();
        while (!isMimeMessageQueueEmpty()) {
            messages.add(getMimeMessageFromQueue());
        }
        return messages;
    }
    public boolean isSimpleMessageQueueEmpty(){
        return simpleMailMessages.isEmpty();
    }
    public boolean isMimeMessageQueueEmpty() {
        return mimeMessages.isEmpty();
    }
}
