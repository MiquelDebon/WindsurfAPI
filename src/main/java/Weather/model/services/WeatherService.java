package Weather.model.services;

import Weather.model.dto.DayHourlyDto;
import Weather.model.entity.Wave;
import Weather.model.entity.Wind;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private RestTemplate restTemplate;
    private WebClient webClient;
    public WeatherService(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient = WebClient.builder().build();
    }


    //Summary weather data
    // https://api.open-meteo.com/v1/forecast?latitude=41.38&longitude=2.16&current_weather=true

    //Current week
    private final String URL_WAVE_CURRENT_WEEK = "https://marine-api.open-meteo.com/v1/marine?latitude=41.3888&longitude=2.159&hourly=wave_height&" +
            "start_date=" + firstDayCurrentWeek() + "&" +
            "end_date=" + lastDayCurrentWeek();
    private final String URL_WIND_CURRENT_WEEK = "https://api.open-meteo.com/v1/forecast?latitude=41.3888&longitude=2.159&hourly=weathercode,windspeed_10m&current_weather=true&" +
            "start_date="  + firstDayCurrentWeek() + "&" +
            "end_date="  + lastDayCurrentWeek();

    //Next 7Days
    private final String URL_WAVE_NEXT_DAYS = "https://marine-api.open-meteo.com/v1/marine?latitude=41.3888&longitude=2.159&hourly=wave_height&" +
            "start_date="   +   LocalDate.now() + "&" +
            "end_date="     +   LocalDate.now().plusDays(10);
    private final String URL_WIND_NEXT_DAYS = "https://api.open-meteo.com/v1/forecast?latitude=41.3888&longitude=2.159&hourly=weathercode,windspeed_10m&current_weather=true&" +
            "start_date="   +   LocalDate.now() + "&" +
            "end_date="     +   LocalDate.now().plusDays(10);




    public List<DayHourlyDto> bestDaysCurrentWeek() {
        ResponseEntity<Wave> waves = restTemplate.getForEntity(URL_WAVE_CURRENT_WEEK, Wave.class);
        ResponseEntity<Wind> wind = restTemplate.getForEntity(URL_WIND_CURRENT_WEEK, Wind.class);
        return daysListFilterLogic(waves, wind);
    }
    public List<DayHourlyDto> bestDaysNextDays() {
        ResponseEntity<Wave> waves = restTemplate.getForEntity(URL_WAVE_NEXT_DAYS, Wave.class);
        ResponseEntity<Wind> wind = restTemplate.getForEntity(URL_WIND_NEXT_DAYS, Wind.class);
        return daysListFilterLogic(waves, wind);
    }


    public List<DayHourlyDto> daysListFilterLogic(ResponseEntity<Wave> waves, ResponseEntity<Wind> wind) {
        Wave waveResp = waves.getBody();
        Wind windResp = wind.getBody();

        DayHourlyDto day ;
        List<DayHourlyDto> week = new ArrayList<DayHourlyDto>();
        for(int i = 0; i < waveResp.getHourly().getTime().length; i++){
            day= new DayHourlyDto(
                    waveResp.getHourly().getTime()[i],
                    windResp.getHourly().getWeathercode()[i],
                    windResp.getHourly().getWindspeed_10m()[i],
                    waveResp.getHourly().getWave_height()[i]);
            week.add(day);
        }

        List<DayHourlyDto> daysList = week.stream()
                .filter(days -> days.getWave_height() < 0.6)
                .filter(winds -> winds.getWindspeed_10m() > 10 && winds.getWindspeed_10m() < 17)
                .filter(conditions -> conditions.getWeathercode() <= 3)
                .filter(time ->
                        time.getTimeHourly().endsWith("12:00") ||
                                time.getTimeHourly().endsWith("15:00") ||
                                time.getTimeHourly().endsWith("17:00"))
                .collect(Collectors.toList());
        return daysList;
    }


    public String firstDayCurrentWeek(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString();
    }
    public String lastDayCurrentWeek(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString();
    }
    public static Date nextFriday(){
        LocalDate currentDate = LocalDate.now();
        return new java.util.Date(currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).toString());
    }

    public static String messageListBestDays(List<DayHourlyDto> dayHourlyList){
        StringBuilder body = new StringBuilder();
        int monday = 0;
        int tuesday = 0;
        int wednesday = 0;
        int thursday = 0;
        int friday = 0;
        int saturday = 0;
        int sunday = 0;

        body.append("The best days for windsurfing are:\n\n");
        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("MON")){
                if(monday == 0){
                    monday++;
                    body.append("MONDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(monday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("TUE")){
                if(tuesday == 0){
                    tuesday++;
                    body.append("TUESDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(tuesday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("WED")){
                if(wednesday == 0){
                    wednesday++;
                    body.append("WEDNESDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(wednesday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("THU")){
                if(thursday == 0){
                    thursday++;
                    body.append("THURSDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(thursday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("FRI")){
                if(friday == 0){
                    friday++;
                    body.append("FRIDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(friday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("SAT")){
                if(saturday == 0){
                    saturday++;
                    body.append("SATURDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        if(saturday != 0) body.append("\n\n");

        for (DayHourlyDto dayHourly : dayHourlyList) {
            if(dayHourly.getDayOfTheWeek().startsWith("SUN")){
                if(sunday == 0){
                    sunday++;
                    body.append("SUNDAY: " + dayHourly.getTimeHourly().substring(0, 10));
                }
                body.append(dayHourly.toString());
            }
        }
        return body.toString();
    }

    /**
     * Other ways to get data
     */


//    public CompleteResponse getWeatherWebClient() {
//        return webClient.get()
//                .uri(URL)
//                .retrieve()
//                .bodyToMono(CompleteResponse.class)
//                .block();
//    }
//
//    public CompleteResponse getWeatherHttpRequestOption() {
//        try{
//            URI uri = new URI(URL_WEATHER_CURRENT_WEAK);
//
//            HttpRequest httpRequest = HttpRequest.newBuilder()
//                    .uri(uri)
//                    .GET()
//                    .build();
//            HttpClient httpClient = HttpClient.newHttpClient();
//            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//            System.out.println(response.body());
//            Gson gson = new Gson();
//            CompleteResponse completeResponse = gson.fromJson(response.body(), CompleteResponse.class);
//            System.out.println(URL_WEATHER_CURRENT_WEAK);
//            System.out.println(URL_WAVE_CURRENT_WEEK);
//
//            return completeResponse;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//

}
