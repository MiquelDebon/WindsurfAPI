package Weather.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wave {
    private WaveHourly hourly;

    @Data
    public static class WaveHourly {
        private String[] time;
        private double[] wave_height;
    }


}
