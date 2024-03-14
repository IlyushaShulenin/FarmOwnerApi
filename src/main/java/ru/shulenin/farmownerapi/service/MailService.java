package ru.shulenin.farmownerapi.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отпрвки сообщеений на почту
 */
@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String username;

    public final JavaMailSender emailSender;


    /**
     * Отправка сообщения
     *
     * @param emailTo почта получателя
     * @param subject тема собщения
     * @param message сообщение
     * @throws MessagingException
     */
    public void send(String emailTo, String subject, String message) throws MessagingException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        emailSender.send(mailMessage);
    }
}
