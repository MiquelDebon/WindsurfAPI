package Weather;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TestMain {

    @Test
    public void getDayOfTheWeek() {
        Calendar calendar = new GregorianCalendar(2023, 9, 24);

        int day = calendar.get(calendar.DAY_OF_WEEK)-1;
        switch (day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:

        }
    }

}
