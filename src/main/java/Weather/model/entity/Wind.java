package Weather.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wind {
    private WindHourly hourly;

    @Data
    public static class WindHourly {
        private double[] windspeed_10m;
        private int[] weathercode;
    }
}
