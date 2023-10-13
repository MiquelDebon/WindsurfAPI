package Weather.controller;

import Weather.model.dto.DayHourlyDto;
import Weather.model.services.EmailService;
import Weather.model.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;
    private EmailService emailService;

    @Autowired
    public WeatherController(WeatherService weatherService, EmailService emailService) {
        this.weatherService = weatherService;
        this.emailService = emailService;
    }

    //localhost:8080/weather/current_week

    @GetMapping("/current_week")
    public ResponseEntity<?> apiGetWeatherCurrentWeek(){
        return ResponseEntity.ok().body(weatherService.bestDaysCurrentWeek());
    }
    @GetMapping("/next_7days")
    public ResponseEntity<?> apiGetWeatherNext7Days(){
        return ResponseEntity.ok().body(weatherService.bestDaysNext7Days());
    }

    //Email
    @GetMapping("/current_week/{email}")
    public ResponseEntity<?> sendEmailCurrentWeek(@PathVariable String email){
        List<DayHourlyDto> currentWeek = weatherService.bestDaysCurrentWeek();
        emailService.sendEmail(
                email,
                "Current week Windsurf Best Days",
                weatherService.bestDaysCurrentWeek());
        return ResponseEntity.ok().body(currentWeek);
    }
    @GetMapping("/next_7days/{email}")
    public ResponseEntity<?> sendEmailNext7Days(@PathVariable String email){
        List<DayHourlyDto> next7days = weatherService.bestDaysNext7Days();
        emailService.sendEmail(
                email,
                "Next 7 days Windsurf - Best Days",
                next7days);
        return ResponseEntity.ok().body(next7days);
    }

    //HTML rendered page
    @GetMapping("/next_7days/home")
    public String next7DaysHomePage(Model model) {
        model.addAttribute("body", "Best 7 next days to do Windsurf");
        model.addAttribute("days", weatherService.bestDaysNext7Days());
        return "home";
    }
    @GetMapping("/current_week/home")
    public String currentWeekHomePage(Model model) {
        model.addAttribute("body", "Best current-week-days to do Windsurf");
        model.addAttribute("days", weatherService.bestDaysCurrentWeek());
        return "home";
    }


    //Email with HTML
    @GetMapping("/html")
    public ResponseEntity<?> html(){
        emailService.sendMiquelEmail();
        return ResponseEntity.ok().body("hola");
    }



}
