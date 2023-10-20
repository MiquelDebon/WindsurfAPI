package Weather.model.services;

import Weather.model.dto.DayHourlyDto;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, List<DayHourlyDto> dayHourlyList) {
        SimpleMailMessage message = new SimpleMailMessage();
        StringBuilder body = new StringBuilder();

        body.append(WeatherService.messageListBestDays(dayHourlyList));
        body.append("\n\nWe hope you the best day!\nBest regards, \nMiquel Debón Villagrasa");

        message.setFrom("mdebonbcn@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body.toString());

        mailSender.send(message);
        System.out.println("Email sent");
    }

    public void sendNextFridayEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("mdebonbcn@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(WeatherService.nextFriday());

        mailSender.send(message);
        System.out.println("Email sent");
    }

    private String cargarContenidoCorreo() throws IOException {
        File file = new ClassPathResource("templates/email.html").getFile();
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }


    public void sendMiquelEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo("mdebonbcn@gmail.com");
            helper.setSubject("mdebonbcn@gmail.com");

            String contenidoHtml = cargarContenidoCorreo();

            helper.setText(contenidoHtml, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }


}
