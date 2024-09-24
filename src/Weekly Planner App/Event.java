package Project1Iteration1;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

public class Event {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    // DateTimeFormatter for displaying times in a user-friendly format
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    // Constructor
    public Event(String name, LocalTime startTime, LocalTime endTime, String description) {
        if (startTime.isAfter(endTime) || ChronoUnit.HOURS.between(startTime, endTime) > 24) {
            throw new IllegalArgumentException("Event times are not valid. The start time must be before the end time, and the event duration cannot exceed 24 hours.");
        }
        setName(name);
        setStartTime(startTime);
        setEndTime(endTime);
        setDescription(description); // Use empty string if description is null
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
       // validateEventDuration(); // Ensure event duration remains valid
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        //validateEventDuration(); // Ensure event duration remains valid
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
    @Override
    public String toString() {
        return String.format("Event: %s\nStart Time: %s\nEnd Time: %s\nDescription: %s",
                name, startTime.format(TIME_FORMATTER), endTime.format(TIME_FORMATTER), description);
    }
}
