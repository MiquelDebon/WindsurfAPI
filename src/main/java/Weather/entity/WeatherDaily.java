package Weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDaily {
    private WeatherWindsurf daily;

    @Data
    public static class WeatherWindsurf {
        private String[] time;
        private int[] weathercode;
        private double[] windspeed_10m_max;
    }


}

