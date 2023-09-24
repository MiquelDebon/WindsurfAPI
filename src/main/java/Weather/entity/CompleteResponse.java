package Weather.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteResponse {
    private String latitude;
    private String longitude;
    private Weather current_weather;

    public String toString(){
        return  "Latitude : " + latitude +
                "\nLongitude : " + longitude +
                "\nTemperature : " + current_weather.getTemperature() +
                "\nWindspeed : " + current_weather.getWindspeed()+
                "\nTime : " + current_weather.getTime();

    }

    @Data
    private static class Weather {
        private float temperature;
        private float windspeed;
        private String time;
    }


}
