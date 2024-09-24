package Project1Iteration1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;

public class DateUtil {
	//private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM dd, YYYY");
    
    public LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use MM dd, YYYY format.");
        }
    }

    public static LocalDate[] getWeekDates(LocalDate startOfWeek) {
        LocalDate[] weekDates = new LocalDate[7];
        for (int i = 0; i < 7; i++) {
            weekDates[i] = startOfWeek.plusDays(i);
        }
        return weekDates;
    }
}