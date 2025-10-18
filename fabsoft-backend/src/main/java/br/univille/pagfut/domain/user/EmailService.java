package br.univille.pagfut.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.backend.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String pagFutEmail;

    public void sendConfirmationEmail(String toEmail, String token){
        SimpleMailMessage message = new SimpleMailMessage();

        String confirmationUrl = baseUrl + "users/confirm?token=" + token;

        message.setFrom(pagFutEmail);
        message.setTo(toEmail);
        message.setSubject("Confimação de cadastro - PagFut");
        message.setText("Obrigado por se registrar! Por favor, clique no link abaixo para ativar sua conta:\n\n" + confirmationUrl);

        mailSender.send(message);
    }
}
