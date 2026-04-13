package com.renthome.renthome_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);
    }

    public void sendMagicLink(String toEmail, String link, String name) {
        try {
            System.out.println("SENDING EMAIL TO: " + toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("saimanasgoud170@gmail.com"); // ✅ MUST ADD
            helper.setTo(toEmail);
            helper.setSubject("Login to RentHome");

            helper.setText(
                    "<p>Click below to login:</p>" +
                            "<a href=\"" + link + "\">Login to RentHome</a>" +
                            "<p>This link expires in 5 minutes</p>",
                    true);

            mailSender.send(message);
        System.out.println("EMAIL SENT SUCCESS");

            System.out.println("EMAIL SENT SUCCESS");

        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}