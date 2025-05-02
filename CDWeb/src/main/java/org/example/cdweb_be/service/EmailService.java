package org.example.cdweb_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.cdweb_be.entity.OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    protected String FROM;
    public boolean sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(FROM);

            mailSender.send(message);
            return true; // Email gửi thành công
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
            return false; // Email không gửi thành công
        }
    }
    public boolean sendEmailResetPassword(OTP otp) {

        String title = "OTP authentication reset password";
        String content = "OTP authentication reset password of account <strong>" + otp.getUsername() + "</strong> is: <strong>" + otp.getOtp() +
                "</strong>. OTP is valid for 5 minutes, expires at: <strong>"+otp.getExpireAt().toString()+"</strong>";

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true cho phép gửi file đính kèm
            helper.setTo(otp.getEmail());
            helper.setSubject(title);
            helper.setFrom(FROM);
            helper.setText(content, true); // true để gửi nội dung HTML

            mailSender.send(message);
            System.out.println("Send mail success");
            return true;
        } catch (MessagingException e) {
            System.out.println("Send mail error");
            e.printStackTrace();
            return false;
        }
    }
}
