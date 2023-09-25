package Weather.controller;

import Weather.dto.DayHourlyDto;
import Weather.services.EmailService;
import Weather.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;
    private EmailService emailService;

    @Autowired
    public WeatherController(WeatherService weatherService, EmailService emailService) {
        this.weatherService = weatherService;
        this.emailService = emailService;
    }


    @GetMapping("/current_week")
    public ResponseEntity<?> apiGetWeatherCurrentWeek(){
        return ResponseEntity.ok().body(weatherService.bestDaysCurrentWeek());
    }
    @GetMapping("/next_week")
    public ResponseEntity<?> apiGetWeatherNextWeek(){
        return ResponseEntity.ok().body(weatherService.bestDaysNextWeek());
    }

    @GetMapping("/current_week/{email}")
    public ResponseEntity<?> sendEmailCurrentWeek(@PathVariable String email){
        List<DayHourlyDto> currentWeek = weatherService.bestDaysCurrentWeek();
        emailService.sendEmail(
                email,
                "Current week Windsurf Best Days",
                weatherService.bestDaysCurrentWeek());
        return ResponseEntity.ok().body(currentWeek);
    }
    @GetMapping("/next_week/{email}")
    public ResponseEntity<?> sendEmailNextWeek(@PathVariable String email){
        List<DayHourlyDto> nextWeek = weatherService.bestDaysNextWeek();
        emailService.sendEmail(
                email,
                "Current week Windsurf Best Days",
                weatherService.bestDaysCurrentWeek());
        return ResponseEntity.ok().body(nextWeek);
    }



}
