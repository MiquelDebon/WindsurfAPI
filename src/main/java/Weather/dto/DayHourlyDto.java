package Weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayHourlyDto {

    @JsonProperty("Day of the week")
    private String dayOfTheWeek;
    @JsonIgnore
    private String timeHourly;

    @JsonProperty("Time")
    private String hour;
    @JsonIgnore
    private int weathercode;
    @JsonProperty("Conditions:")
    private String conditions;
    @JsonProperty("Wind speed (10m)")
    private double windspeed_10m;
    @JsonProperty("Wave height")
    private double wave_height;

    public DayHourlyDto(String timeHourly, int weathercode, double windspeed_10m, double wave_height) {
        this.dayOfTheWeek = dayOfTheWeek(timeHourly);
        this.timeHourly = timeHourly;
        this.hour = timeHourly.substring(11,16);
        this.weathercode = weathercode;
        this.conditions = weathercodeToConditions(weathercode);
        this.windspeed_10m = windspeed_10m;
        this.wave_height = wave_height;
    }

    @Override
    public String toString() {
        return
                "\n     🕣Time: " + hour +
                "\n     🌡️Conditions: " + weathercodeToConditionsIcon(weathercode) +
                "\n     💨Wind speed (10m): " + windspeed_10m + " km/h"+
                "\n     🌊Wave height: " + wave_height + " m\n";
    }


    public String dayOfTheWeek(String timeHourly){
        // Parse the date string into a LocalDate object
        LocalDate date = LocalDate.parse(timeHourly.substring(0,10), DateTimeFormatter.ISO_LOCAL_DATE);
        // Get the day of the week from the LocalDate object
        return date.getDayOfWeek().toString();

    }

    public String weathercodeToConditions(int weathercode) {
        switch (weathercode) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
                return "Partly cloudy";
            case 3:
                return "Overcast";
            case 45:
                return "Fog";
            case 48:
                return "Depositing rime fog";
            case 51:
                return "Drizzle";
            case 53:
                return "Drizzle";
            case 55:
                return "Drizzle";
            case 56,57:
                return "Freezing Drizzle";
            case 61,63,65:
                return "Rain";
            case 66,67:
                return "Freezing Rain";
            case 71,73,75:
                return "Snow";
            case 77:
                return "Snow grains";
            case 80,81,82:
                return "Rain showers";
            case 85,86:
                return "Snow showers";
            case 95:
                return "Thunderstorm";
            case 96,99:
                return "Thunderstorm  with slight and heavy hail";

        }
        return null;
    }

    public String weathercodeToConditionsIcon(int weathercode) {
        switch (weathercode) {
            case 0:
                return "☀️Clear sky";
            case 1:
                return "🌥️Mainly clear";
            case 2:
                return "⛅️Partly cloudy";
            case 3:
                return "☁️Overcast";
        }
        return null;
    }


}
