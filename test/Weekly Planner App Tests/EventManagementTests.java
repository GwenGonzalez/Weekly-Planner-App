package Project1Iteration1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventManagementTests {

    @Test
    void testAddEventSuccess() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event event = new Event("Meeting", LocalTime.parse("09:00"), LocalTime.parse("10:00"), "Discuss project");
        weekPlan.addEvent(event, LocalDate.of(2024, 3, 3));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).contains(event));
    }

    @Test
    void testRemoveEvent() {
        LocalDate date = LocalDate.of(2024, 3, 4);
        WeekPlan weekPlan = new WeekPlan(date);
        Event event = new Event("Team Meeting", LocalTime.of(14, 0), LocalTime.of(15, 0), "Project discussion");
        weekPlan.addEvent(event, date);
        assertTrue(weekPlan.removeEvent("Team Meeting", date));
        assertFalse(weekPlan.getEventsForDay(date).contains(event));
    }

    @Test
    void testAddMultipleEventsDifferentDays() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event event1 = new Event("Meeting 1", LocalTime.of(9, 0), LocalTime.of(10, 0), "Discuss project 1");
        Event event2 = new Event("Meeting 2", LocalTime.of(10, 0), LocalTime.of(11, 0), "Discuss project 2");
        weekPlan.addEvent(event1, LocalDate.of(2024, 3, 3));
        weekPlan.addEvent(event2, LocalDate.of(2024, 3, 4));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).contains(event1));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 4)).contains(event2));
    }

    @Test
    void testEventsAtStartAndEndOfWeek() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event startEvent = new Event("Start Event", LocalTime.of(8, 0), LocalTime.of(9, 0), "Start of week");
        Event endEvent = new Event("End Event", LocalTime.of(20, 0), LocalTime.of(21, 0), "End of week");
        weekPlan.addEvent(startEvent, LocalDate.of(2024, 3, 3));
        weekPlan.addEvent(endEvent, LocalDate.of(2024, 3, 9));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3)).contains(startEvent));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 9)).contains(endEvent));
    }

    @Test
    void testEventListIntegrityAfterRemoval() {
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        Event event1 = new Event("Meeting", LocalTime.of(9, 0), LocalTime.of(10, 0), "Discuss project");
        Event event2 = new Event("Review", LocalTime.of(11, 0), LocalTime.of(12, 0), "Code review");
        weekPlan.addEvent(event1, LocalDate.of(2024, 3, 4));
        weekPlan.addEvent(event2, LocalDate.of(2024, 3, 4));
        weekPlan.removeEvent("Meeting", LocalDate.of(2024, 3, 4));
        assertFalse(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 4)).contains(event1));
        assertTrue(weekPlan.getEventsForDay(LocalDate.of(2024, 3, 4)).contains(event2));
    }
}
