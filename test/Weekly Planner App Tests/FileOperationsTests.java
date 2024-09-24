package Project1Iteration1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FileOperationsTests {

    @Test
    void testSaveWeekPlanToFile() throws IOException {
        LocalDate weekStart = LocalDate.of(2024, 3, 3);
        WeekPlan weekPlan = new WeekPlan(weekStart);
        Event event = new Event("Meeting", LocalTime.parse("09:00"), LocalTime.parse("10:00"), "Discuss project");
        weekPlan.addEvent(event, weekStart);
        Path tempFile = Files.createTempFile("weekPlan", ".txt");
        weekPlan.saveWeekPlanToFile(tempFile.toString());
        String content = Files.readString(tempFile);
        assertTrue(content.contains("Meeting"));
        assertTrue(content.contains("09:00"));
        assertTrue(content.contains("10:00"));
        assertTrue(content.contains("Discuss project"));
        Files.delete(tempFile);
    }

    @Test
    void testLoadWeekPlanFromFile() throws IOException {
        Path tempFile = Files.createTempFile("weekPlan", ".txt");
        String eventData = "2024-03-03|Meeting|09:00|10:00|Discuss project\n";
        Files.writeString(tempFile, eventData);
        WeekPlan weekPlan = new WeekPlan(LocalDate.of(2024, 3, 3));
        weekPlan.loadWeekPlanFromFile(tempFile.toString());
        List<Event> events = weekPlan.getEventsForDay(LocalDate.of(2024, 3, 3));
        assertFalse(events.isEmpty());
        Event loadedEvent = events.get(0);
        assertEquals("Meeting", loadedEvent.getName());
        assertEquals(LocalTime.parse("09:00"), loadedEvent.getStartTime());
        assertEquals(LocalTime.parse("10:00"), loadedEvent.getEndTime());
        assertEquals("Discuss project", loadedEvent.getDescription());
        Files.delete(tempFile);
    }
}