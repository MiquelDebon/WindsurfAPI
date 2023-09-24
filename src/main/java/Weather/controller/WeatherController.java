package Weather.controller;

import Weather.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(){
        return ResponseEntity.ok().body(weatherService.getWeather());
    }

    @GetMapping("/weather2")
    public ResponseEntity<?> getWeather2(){
        return ResponseEntity.ok().body(weatherService.getWeather2());
    }

    @GetMapping("/weather3")
    public ResponseEntity<?> getWeather3(){
        return ResponseEntity.ok().body(weatherService.getWeather3());
    }
@GetMapping("/weather4")
    public ResponseEntity<?> getWeather4(){
        return ResponseEntity.ok().body(weatherService.getWeather4());
    }


}
