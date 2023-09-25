package Weather.services;

import Weather.dto.DayHourlyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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

}
