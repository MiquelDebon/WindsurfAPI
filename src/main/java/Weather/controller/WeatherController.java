package Weather.controller;

import Weather.model.dto.DayHourlyDto;
import Weather.model.dto.EmailDto;
import Weather.model.entity.Subscriber;
import Weather.model.services.EmailService;
import Weather.model.services.SubscriberService;
import Weather.model.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;
    private EmailService emailService;
    private SubscriberService subscriberService;

    @Autowired
    public WeatherController(WeatherService weatherService, EmailService emailService, SubscriberService subscriberService){
        this.weatherService = weatherService;
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    //localhost:8080/weather/current_week

    @GetMapping("/current_week")
    public ResponseEntity<?> apiGetWeatherCurrentWeek(){
        return ResponseEntity.ok().body(weatherService.bestDaysCurrentWeek());
    }
    @GetMapping("/next_days")
    public ResponseEntity<?> apiGetWeatherNext7Days(){
        return ResponseEntity.ok().body(weatherService.bestDaysNextDays());
    }

    //Email
    @GetMapping("/current_week/{email}")
    public ResponseEntity<?> sendEmailCurrentWeek(@PathVariable String email){
        List<DayHourlyDto> currentWeek = weatherService.bestDaysCurrentWeek();
        emailService.sendEmail(
                email,
                "Current week Windsurf Best Days",
                currentWeek);
        return ResponseEntity.ok().body(currentWeek);
    }
    @GetMapping("/next_days/{email}")
    public ResponseEntity<?> sendEmailNext7Days(@PathVariable String email){
        List<DayHourlyDto> next7days = weatherService.bestDaysNextDays();
        emailService.sendEmail(
                email,
                "Next 7 days Windsurf - Best Days",
                next7days);
        return ResponseEntity.ok().body(next7days);
    }
    @PostMapping("/sendEmail")
    public String sendEmailFromWebSite(@ModelAttribute("email")  EmailDto email, BindingResult result, Model model){
        List<DayHourlyDto> next7days = weatherService.bestDaysNextDays();

        emailService.sendEmail(
                email.getEmail(),
                "Next 7 days Windsurf - Best Days",
                next7days
        );

        model.addAttribute("body", "Best following days");
        model.addAttribute("days", weatherService.bestDaysNextDays());
        return "redirect:/weather/next_days/home?sent=true";
    }

    @GetMapping("/friday")
    public String sendSubscribeEmail( Model model){
        List<DayHourlyDto> next7days = weatherService.bestDaysNextDays();
        int subscribersCount = subscriberService.subscribersCount();
        List<Subscriber> subscribers = subscriberService.getSubscribers();

        for(int i = 0; i < subscribersCount; i++){
            emailService.sendEmail(
                    subscribers.get(i).getEmail(),
                    "Next 7 days Windsurf - Best Days",
                    next7days
            );
        }

        model.addAttribute("body", "Best following days");
        model.addAttribute("days", weatherService.bestDaysNextDays());
        return "redirect:/weather/next_days/home";
    }


    //HTML rendered page
    @GetMapping("/next_days/home")
    public String next7DaysHomePage(Model model) {
        model.addAttribute("body", "Best following days");
        model.addAttribute("days", weatherService.bestDaysNextDays());
        model.addAttribute("paramValue", "/next_days/home");

        return "home";
    }
    @GetMapping("/current_week/home")
    public String currentWeekHomePage(Model model) {
        model.addAttribute("body", "Best current week days");
        model.addAttribute("paramValue", "/current_week/home");
        model.addAttribute("days", weatherService.bestDaysCurrentWeek());
        return "home";
    }
    @GetMapping("/all_next_days/home")
    public String allnextDaysHomePage(Model model) {
        model.addAttribute("body", "All following days");
        model.addAttribute("days", weatherService.allNextDays());
        model.addAttribute("paramValue", "/all_next_days/home");

        return "home";
    }


    @PostMapping("/subscribe")
    public String subscribe(@ModelAttribute("subscriber") Subscriber subscriber, BindingResult result, Model model){
        boolean exist = subscriberService.saveDeleteSubscriber(subscriber.getEmail());

        model.addAttribute("body", "Best following days");
        model.addAttribute("days", weatherService.bestDaysNextDays());

        if(exist){
            return "redirect:/weather/next_days/home?unsubscribe=true";
        }else{
            return "redirect:/weather/next_days/home?subscribe=true";
        }
    }

    //Email with HTML
    @GetMapping("/html")
    public ResponseEntity<?> html(){
        emailService.sendMiquelEmail();
        return ResponseEntity.ok().body("hola");
    }



}
