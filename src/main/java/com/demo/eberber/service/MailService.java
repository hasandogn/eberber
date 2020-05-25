package com.demo.eberber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.logging.Level;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    public SimpleMailMessage templateForSimpleMessage() {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Sayın müsterimiz, ");
        simpleMailMessage.setText("Şifre değişikliği için lütfen aşağıdaki linke tıklayınız. \nhttp://localhost:8080/MemberRestAPIProject/activateMemberWebServiceEndpoint/activateMember?activationToken=%s/");

        return simpleMailMessage;
    }

    public String sendMail(String emailAddress) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setText("Şifre değişikliği için lütfen aşağıdaki linke tıklayınız. \nhttp://localhost:8080/MemberRestAPIProject/activateMemberWebServiceEndpoint/activateMember?activationToken=%s/");
            mimeMessageHelper.setTo(emailAddress);
            mimeMessage.setSubject("Şifre Değişikliği");

        } catch (MessagingException messagingException) {
            messagingException.getMessage();
            return "Error!";
        }
        mailSender.send(mimeMessage);
        return "Mail Gönderildi!";
    }

}
