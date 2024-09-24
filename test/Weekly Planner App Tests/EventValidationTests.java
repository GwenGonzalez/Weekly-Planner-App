package Project1Iteration1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventValidationTests {

    @Test
    void testAddEventNotInWeek() {
        LocalDate weekStart = LocalDate.of(2024, 3, 3);
        WeekPlan weekPlan = new WeekPlan(weekStart);
        LocalDate eventDate = weekStart.plusWeeks(1);
        Event event = new Event("Test2", LocalTime.parse("10:00"), LocalTime.parse("12:00"), "Outside Week Event");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> weekPlan.addEvent(event, eventDate));
        assertTrue(exception.getMessage().contains("Event date must be within the selected week"));
    }

    @Test
    void testAddEventStartTimeAfterEndTime() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Event event = new Event("Test3", LocalTime.parse("12:00"), LocalTime.parse("11:00"), "");
            weekPlan.addEvent(event, LocalDate.of(2024, 3, 3));
        });
        assertTrue(thrown.getMessage().contains("Event times are not valid"));
    }

    @Test
    void testAddOverlappingEvents() {
        LocalDate eventDate = LocalDate.of(2024, 3, 3);
        WeekPlan weekPlan = new WeekPlan(eventDate);
        Event event1 = new Event("Test4", LocalTime.parse("10:00"), LocalTime.parse("12:00"), "");
        weekPlan.addEvent(event1, eventDate);
        Event event2 = new Event("Test5", LocalTime.parse("11:30"), LocalTime.parse("13:00"), "");
        assertThrows(IllegalArgumentException.class, () -> weekPlan.addEvent(event2, eventDate));
    }

    @Test
    void testEventOverlappingByMinutes() {
        LocalDate date = LocalDate.of(2024, 3, 3);
        WeekPlan weekPlan = new WeekPlan(date);
        Event event1 = new Event("Overlap Test 1", LocalTime.of(9, 0), LocalTime.of(10, 0), "First Event");
        Event event2 = new Event("Overlap Test 2", LocalTime.of(9, 59), LocalTime.of(11, 0), "Second Event");
        weekPlan.addEvent(event1, date);
        assertThrows(IllegalArgumentException.class, () -> weekPlan.addEvent(event2, date));
    }

    @Test
    void testAddingEventsWithSameNameDifferentTimes() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event event1 = new Event("Overlap", LocalTime.of(9, 0), LocalTime.of(10, 0), "First instance");
        weekPlan.addEvent(event1, LocalDate.of(2024, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> {
            Event event2 = new Event("Overlap", LocalTime.of(9, 30), LocalTime.of(11, 30), "Second instance on same day");
            weekPlan.addEvent(event2, LocalDate.of(2024, 3, 3));
        });
    }

    @Test
    void testFullDayEvent() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event fullDayEvent = new Event("Full Day Workshop", LocalTime.of(0, 0), LocalTime.of(23, 59), "Covers the entire day");
        weekPlan.addEvent(fullDayEvent, LocalDate.of(2024, 3, 3));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).contains(fullDayEvent));
    }
}
