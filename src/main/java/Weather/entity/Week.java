package Weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Week {
    private ArrayList<Day> days = new ArrayList<>();



    public void addDay(Day day){
        days.add(day);
    }

}
