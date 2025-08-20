package com.chronoai.backend.service;

import com.chronoai.backend.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNotification(Task.NotificationType type, String target, String message) {
        log.info("Preparing to send notification. Type: {}, Target: {}", type, target);

        switch (type) {
            case EMAIL:
                try {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(target);
                    mailMessage.setSubject("ChronoAI Task Execution");
                    mailMessage.setText(message);
                    javaMailSender.send(mailMessage);
                    log.info("--- EMAIL SENT successfully to {} ---", target);
                } catch (Exception e) {
                    log.error("Failed to send email", e);
                }
                break;
            case WEBHOOK:
                log.info("--- SIMULATING CALLING WEBHOOK ---");
                log.info("URL: {}", target);
                log.info("Payload: {}", message);
                log.info("--- WEBHOOK CALLED ---");
                break;
            default:
                log.warn("Unknown notification type: {}", type);
        }
    }
}
