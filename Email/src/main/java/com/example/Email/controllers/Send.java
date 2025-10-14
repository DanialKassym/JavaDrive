package com.example.Email.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Send {
    @Autowired
    private MailSender mailSender;
    @RequestMapping("/sendMail")
    public String sendConfirmationEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("aaa@localhost");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        msg.setFrom("user");
        mailSender.send(msg);
        return "OK";
    }
}
