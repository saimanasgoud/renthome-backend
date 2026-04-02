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

        helper.setTo(toEmail);
        helper.setSubject("Login to RentHome");

       helper.setText(
    "<p>Click below to login:</p>" +
    "<a href=\"" + link + "\">Login to RentHome</a>" +
    "<p>This link expires in 5 minutes</p>",
    true
);

System.out.println("FINAL LINK SENT: " + link);
        mailSender.send(message);

        System.out.println("EMAIL SENT SUCCESS");

    } catch (Exception e) {
        System.out.println("EMAIL ERROR: " + e.getMessage());
        e.printStackTrace();
    }
}

// public void sendMagicLink(String toEmail, String link, String name) {

//     try {
//         MimeMessage message = mailSender.createMimeMessage();
//         MimeMessageHelper helper = new MimeMessageHelper(message, true);

//         helper.setTo(toEmail);
//         helper.setSubject("🔐 Login to RentHome");

//         String htmlContent =
//                 "<div style='font-family:Arial;padding:20px'>" +
//                 "<h2 style='color:#2563eb;'>🏠 RentHome</h2>" +

//                 "<p>Hi <b>" + name + "</b>,</p>" +

//                 "<p>You requested to login to your account.</p>" +

//                 "<a href='" + link + "' " +
//                 "style='display:inline-block;padding:12px 20px;background:#16a34a;color:white;text-decoration:none;border-radius:8px;margin-top:10px'>" +
//                 "Login Now</a>" +

//                 "<p style='margin-top:20px;'>⏰ This link will expire in 5 minutes.</p>" +

//                 "<p>If you didn’t request this, you can ignore this email.</p>" +

//                 "<hr/>" +
//                 "<p style='font-size:12px;color:gray;'>RentHome Team</p>" +
//                 "</div>";

//         helper.setText(htmlContent, true);

//         mailSender.send(message);

//     } catch (Exception e) {
//         e.printStackTrace();
//     }
// }
}