package Project1Iteration1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.time.*;
import java.util.*;
import java.io.*;

public class WeekPlan {
    private final LocalDate weekStartDate;
    private final Map<LocalDate, List<Event>> eventsByDay;

    public WeekPlan(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
        this.eventsByDay = new HashMap<>();
        // Initialize the map for each day of the week
        for (int i = 0; i < 7; i++) {
            eventsByDay.put(weekStartDate.plusDays(i), new ArrayList<>());
        }
    }

    // Method to add an event
    public void addEvent(Event event, LocalDate eventDate) {
        if (eventDate.isBefore(weekStartDate) || eventDate.isAfter(weekStartDate.plusDays(6))) {
            throw new IllegalArgumentException("Event date must be within the selected week.");
        }

        List<Event> eventsForDay = eventsByDay.get(eventDate);
        if (eventsForDay == null) {
            eventsForDay = new ArrayList<>();
            eventsByDay.put(eventDate, eventsForDay);
        } else {
            for (Event existingEvent : eventsForDay) {
                if (event.getName().equals(existingEvent.getName())) {
                    throw new IllegalArgumentException("An event with the same name already exists on this date.");
                }
                if ((event.getStartTime().isBefore(existingEvent.getEndTime()) && event.getEndTime().isAfter(existingEvent.getStartTime())) ||
                        event.getStartTime().equals(existingEvent.getStartTime())) {
                    throw new IllegalArgumentException("The new event overlaps with an existing event on " + eventDate);
                }
            }
        }
        eventsForDay.add(event);
    }


    // Method to remove an event
    public boolean removeEvent(String eventName, LocalDate eventDate) {
        List<Event> eventsForDay = eventsByDay.get(eventDate);
        return eventsForDay.removeIf(event -> event.getName().equalsIgnoreCase(eventName));
    }

    // Method to get the events for a specific day
    public List<Event> getEventsForDay(LocalDate date) {
        return eventsByDay.getOrDefault(date, Collections.emptyList());
    }

    // Method to print the week's plan, for debugging purposes
    public String printWeekPlan() {
        for (LocalDate date : eventsByDay.keySet()) {
            List<Event> events = eventsByDay.get(date);
            if (!events.isEmpty()) {
                System.out.println("\nEvents on " + date + ":");
                for (Event event : events) {
                    System.out.println(event);
                }
                System.out.println();
            }
        }
        return null;
    }

    public void saveWeekPlanToFile(String filename) {
        System.out.println("Saving week plan to file: " + filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<LocalDate, List<Event>> entry : eventsByDay.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    //System.out.println("No events to save for " + entry.getKey());
                    continue;
                }
                for (Event event : entry.getValue()) {
                    String line = String.format("%s|%s|%s|%s|%s",
                            entry.getKey(),
                            event.getName(),
                            event.getStartTime(),
                            event.getEndTime(),
                            event.getDescription());
                    line += System.lineSeparator(); // Append the system-dependent newline character
                    //System.out.println("Writing: " + line);
                    writer.write(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWeekPlanFromFile(String filename) {
        eventsByDay.clear(); // Clear existing events before loading new ones
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime startTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("H:mm"));
                LocalTime endTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("H:mm"));
                String description = parts.length > 4 ? parts[4] : ""; // Use an empty string if description is missing
                Event event = new Event(parts[1], startTime, endTime, description);
                addEvent(event, date); // Make sure the WeekPlan class has a method to add events correctly
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllEventsNamesAndDates() {
        List<String> eventsList = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Event>> entry : eventsByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<Event> events = entry.getValue();

            for (Event event : events) {
                String eventInfo = event.getName() + " - " + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                eventsList.add(eventInfo);
            }
        }
        return eventsList;
    }

}