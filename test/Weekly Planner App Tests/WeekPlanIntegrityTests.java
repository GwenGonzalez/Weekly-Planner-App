package Project1Iteration1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

public class WeekPlanIntegrityTests {

    @Test
    void testEmptyWeekPlan() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 5)).isEmpty());
    }

    @Test
    void testWeekPlanStateAfterFailedAddition() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event validEvent = new Event("Valid Event", LocalTime.of(9, 0), LocalTime.of(10, 0), "Should be added");
        weekPlan.addEvent(validEvent, LocalDate.of(2024, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> {
            Event invalidEvent = new Event("Invalid Event", LocalTime.of(10, 0), LocalTime.of(9, 0), "Should not be added");
            weekPlan.addEvent(invalidEvent, LocalDate.of(2024, 3, 3));
        });
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).contains(validEvent));
        assertEquals(1, weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).size());
    }
}