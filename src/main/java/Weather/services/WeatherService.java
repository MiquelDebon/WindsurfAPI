package Weather.services;

import Weather.entity.*;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    //Summart
    // https://api.open-meteo.com/v1/forecast?latitude=41.38&longitude=2.16&current_weather=true

    //Weathercode + temperature + sunrise + sunset
    //https://api.open-meteo.com/v1/forecast?latitude=41.3888&longitude=2.159&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,windspeed_10m_max&current_weather=true&start_date=2023-09-24&end_date=2023-09-24

    //Wave height
    //https://marine-api.open-meteo.com/v1/marine?latitude=41.3888&longitude=2.159&hourly=wave_height&start_date=2023-09-23&end_date=2023-09-28

    private final String URL_WEATHER_CURRENT_WEAK = "https://api.open-meteo.com/v1/forecast?latitude=41.3888&longitude=2.159&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,windspeed_10m_max&current_weather=true&" +
            "start_date=" + firstDayCurrentWeek() + "&" +
            "end_date=" + lastDayCurrentWeek();

    private final String URL_WAVE_CURRENT_WEEK = "https://marine-api.open-meteo.com/v1/marine?latitude=41.3888&longitude=2.159&hourly=wave_height&" +
            "start_date=" + firstDayCurrentWeek() + "&" +
            "end_date=" + lastDayCurrentWeek();

    private final String URL_WIND_CURRENT_WEEK = "https://api.open-meteo.com/v1/forecast?latitude=41.3888&longitude=2.159&hourly=windspeed_10m&current_weather=true&" +
            "start_date="  + firstDayCurrentWeek() + "&" +
            "end_date="  + lastDayCurrentWeek();

    private final String URL = "https://api.open-meteo.com/v1/forecast?latitude=41.38&longitude=2.16&current_weather=true";

    private RestTemplate restTemplate;
    private WebClient webClient;

    public WeatherService(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient = WebClient.builder().build();
    }

    public CompleteResponse getWeather() {
        try{
            URI uri = new URI(URL_WEATHER_CURRENT_WEAK);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            Gson gson = new Gson();
            CompleteResponse completeResponse = gson.fromJson(response.body(), CompleteResponse.class);
            System.out.println(URL_WEATHER_CURRENT_WEAK);
            System.out.println(URL_WAVE_CURRENT_WEEK);

            return completeResponse;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Day> getWeather2() {
        ResponseEntity<WeatherDaily> weather = restTemplate.getForEntity(URL_WEATHER_CURRENT_WEAK, WeatherDaily.class);
        ResponseEntity<Wave> waves = restTemplate.getForEntity(URL_WAVE_CURRENT_WEEK, Wave.class);

        WeatherDaily weatherResp = weather.getBody();
        Wave waveResp = waves.getBody();

        Day day ;
        Week week = new Week();
        for(int i = 0; i < weatherResp.getDaily().getTime().length; i++){
            day= new Day(
                    weatherResp.getDaily().getTime()[i],
                    weatherResp.getDaily().getWeathercode()[i],
                    weatherResp.getDaily().getWindspeed_10m_max()[i],
                    splitArrayUsingStreams(waveResp.getHourly().getWave_height(),i)) ;

            week.addDay(day);
        }

        System.out.println(weatherResp);
        System.out.println(URL_WEATHER_CURRENT_WEAK);

        List<Day> daysList = week.getDays().stream()
                .filter(days -> days.getWave_height() < 0.6)
                .filter(wind -> wind.getWindspeed_10m_max() > 10 && wind.getWindspeed_10m_max() < 17)
                .sorted(Comparator.comparing(Day::getWave_height))
                .collect(Collectors.toList());
        return daysList;
    }
    public List<DayHourly> getWeather4() {
        ResponseEntity<Wave> waves = restTemplate.getForEntity(URL_WAVE_CURRENT_WEEK, Wave.class);
        ResponseEntity<Wind> wind = restTemplate.getForEntity(URL_WIND_CURRENT_WEEK, Wind.class);

        Wave waveResp = waves.getBody();
        Wind windResp = wind.getBody();

        DayHourly day ;
        List<DayHourly> week = new ArrayList<DayHourly>();
        for(int i = 0; i < waveResp.getHourly().getTime().length; i++){
            day= new DayHourly(
                    waveResp.getHourly().getTime()[i],
                    windResp.getHourly().getWindspeed_10m()[i],
                    waveResp.getHourly().getWave_height()[i]);
            week.add(day);
        }


        List<DayHourly> daysList = week.stream()
                .filter(days -> days.getWave_height() < 0.6)
                .filter(winds -> winds.getWindspeed_10m() > 10 && winds.getWindspeed_10m() < 17)
                .filter(time ->
                        time.getTimeHourly().endsWith("12:00") ||
                        time.getTimeHourly().endsWith("15:00") ||
                        time.getTimeHourly().endsWith("17:00"))
                .sorted(Comparator.comparing(DayHourly::getWave_height))
                .collect(Collectors.toList());
        return daysList;
    }

    public CompleteResponse getWeather3() {
        return webClient.get()
                .uri(URL)
                .retrieve()
                .bodyToMono(CompleteResponse.class)
                .block();
    }



    public String firstDayCurrentWeek(){
        LocalDate currentDate = LocalDate.now();
        // Calculate the first day of the week (Monday)
        return currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString();
    }
    public String lastDayCurrentWeek(){
        LocalDate currentDate = LocalDate.now();
        // Calculate the first day of the week (Monday)
        return currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString();
    }

    private static double splitArrayUsingStreams(double[] originalArray, int grupIndex) {
        double sum = 0;
        double average = 0;
        for (int i = 0; i < 24; i++) {
            int startIndex = (grupIndex * 24);
            int endIndex = startIndex + 24;
            sum += originalArray[startIndex + i];
        }
        average = sum / 24;
        return Math.round(average * 100.0) / 100.0;
    }

}
