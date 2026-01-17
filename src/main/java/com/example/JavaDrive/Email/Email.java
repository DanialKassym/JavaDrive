package com.example.JavaDrive.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class Email {
    final String From = "CloudStorage";
    final String Subject = "Email Confirmation";
    final String Text = "This is a message sent by JavaDrive to Confirm your email" +
            "follow the link to confirm the email ";

    private final JavaMailSender javaMailSender;
    @Autowired
    public Email(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmail(String to,String jwtUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(From);
        message.setSubject(Subject);
        message.setText(Text + jwtUrl);
        javaMailSender.send(message);
    }

}
