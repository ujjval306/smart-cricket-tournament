package com.example.smart_cricket_tournament.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("ujjaval.i@agileinfoways.com");
        message.setSubject("Verify your registration - Smart Cricket Tournament");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }
}
