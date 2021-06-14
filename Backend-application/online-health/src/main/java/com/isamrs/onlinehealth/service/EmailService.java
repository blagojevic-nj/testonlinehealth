package com.isamrs.onlinehealth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    @Async
    public void sendEmailAsync(String from, String to, String subject, String body) throws InterruptedException, MailException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        if(from.equals("")) {
            mailMessage.setFrom(environment.getProperty("spring.mail.username"));
        }
        else{
            mailMessage.setFrom(from);
        }
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
    }
}
