package Weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Day {
    private String time;
    private int weathercode;
    private double windspeed_10m_max;
    private double wave_height;


}
