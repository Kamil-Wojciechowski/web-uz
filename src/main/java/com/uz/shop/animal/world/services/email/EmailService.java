package com.uz.shop.animal.world.services.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


//Serwis odpowiadający za wysyłanie maili
@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;


    //logujemy że email jest wysyłany
    // Tworzymy wiadomość ustawiamy odpowiednie parametry
    // Wysyłamy
    @Override
    @Async
    public void send(String email, String token, Boolean activation) {
        try {
            LOGGER.info("Sending");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            if(activation) {
                helper.setText(ActivationEmailClass.build("http://localhost:8081/register/" + token), true);
                helper.setSubject("Potwierdź email");
            } else {
                helper.setText(RecoveryEmailClass.build("http://localhost:8081/recovery/token/" + token), true);
                helper.setSubject("Zresetuj hasło");
            }

            helper.setTo(email);
            helper.setFrom("some@some.com");
            mailSender.send(message);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }



}
