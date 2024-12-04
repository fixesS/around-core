package com.around.aroundcore.core.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public void sendHTML(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(mimeMessage);
    }
    public void send(List<SimpleMailMessage> messageList) {
        SimpleMailMessage[] messages = new SimpleMailMessage[messageList.size()];
        messageList.toArray(messages);
        mailSender.send(messages);
    }
    public void sendHTML(List<MimeMessage> messageList) {
        MimeMessage[] messages = new MimeMessage[messageList.size()];
        messageList.toArray(messages);
        mailSender.send(messages);
    }
}
