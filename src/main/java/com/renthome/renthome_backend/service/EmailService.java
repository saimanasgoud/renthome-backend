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
                    "<div style='font-family: Arial; padding:20px; background:#f9f9f9;'>"
                            + "<h2 style='color:#16a34a;'>🏠 RentHome</h2>"

                            + "<p>Hi " + name + ",</p>"

                            + "<p>We received a request to login to your RentHome account.</p>"

                            + "<div style='margin:20px 0;'>"
                            + "<a href='" + link + "' "
                            + "style='background:#2563eb; color:white; padding:12px 20px; "
                            + "text-decoration:none; border-radius:6px;'>"
                            + "Login Securely"
                            + "</a>"
                            + "</div>"

                            + "<p style='color:#555;'>This link is valid for 5 minutes.</p>"

                            + "<p style='color:#16a34a; font-weight:bold;'>"
                            + "✔ 100% Safe & Secure | RentHome Verified Login"
                            + "</p>"

                            + "<hr/>"

                            + "<p style='font-size:12px; color:#777;'>"
                            + "If you did not request this login, please ignore this email."
                            + "</p>"

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