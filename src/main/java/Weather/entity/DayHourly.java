package Weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayHourly {
    private String timeHourly;
    private double windspeed_10m;
    private double wave_height;


}
