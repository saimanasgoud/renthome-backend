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

    public boolean sendMagicLink(String toEmail, String link, String name) {
        try {
            System.out.println("SENDING EMAIL TO: " + toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("RentHome <saimanasgoud170@gmail.com>"); // ✅ MUST ADD

            helper.setTo(toEmail);
            helper.setSubject("Login to RentHome");

            helper.setText(
                    "<div style='font-family: Arial; padding:20px; background:#f4f6f8;'>"

                            + "<div style='max-width:500px; margin:auto; background:white; padding:20px; border-radius:10px;'>"

                            + "<h2 style='color:#2563eb; text-align:center;'>🏠 RentHome</h2>"

                            + "<p>Hi <b>" + name + "</b>,</p>"

                            + "<p>Click below to login securely:</p>"

                            + "<div style='text-align:center; margin:20px;'>"
                            + "<a href='" + link + "' "
                            + "style='background:#2563eb; color:white; padding:12px 20px; "
                            + "text-decoration:none; border-radius:8px; font-weight:bold;'>"
                            + "Login to RentHome"
                            + "</a>"
                            + "</div>"

                            + "<p style='font-size:13px; color:#666;'>Link valid for 5 minutes</p>"

                            + "<hr/>"

                            + "<p style='font-size:12px; color:#999;'>"
                            + "If you didn’t request this, ignore this email."
                            + "</p>"

                            + "</div>"
                            + "</div>",
                    true);
                    
            mailSender.send(message);

            System.out.println("EMAIL SENT TO: " + toEmail);

            System.out.println("EMAIL SENT SUCCESS");
            return true;
        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}