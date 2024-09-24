package Project1Iteration1;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.IntStream;

class WeeklyPlannerGUI extends JFrame {
    private final JButton addButton;
    private final JButton removeButton;
    private final JButton viewButton;
    private final JButton saveButton;
    private final JButton loadButton;
    private final JTextArea textArea;
    private final WeekPlan weekPlan;

    public WeeklyPlannerGUI() {
        setTitle("Weekly Planner");
        setSize(600, 500); // Adjusted for better layout visibility
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        addButton = new JButton("Add Event");
        removeButton = new JButton("Remove Event");
        viewButton = new JButton("View Week");
        saveButton = new JButton("Save Plan");
        loadButton = new JButton("Load Plan");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        weekPlan = new WeekPlan(LocalDate.now()); // Initialize WeekPlan with today's date

        setupEventHandlers();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> {
            JTextField eventNameField = new JTextField(20);
            JTextArea eventDescriptionArea = new JTextArea(5, 20);

            JComboBox<Integer> yearBox = new JComboBox<>(IntStream.rangeClosed(LocalDate.now().getYear(), 2032).boxed().toArray(Integer[]::new));
            JComboBox<Integer> monthBox = new JComboBox<>(IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));
            JComboBox<Integer> dayBox = new JComboBox<>();
            updateDayComboBox(yearBox, monthBox, dayBox);
            yearBox.addActionListener(event -> updateDayComboBox(yearBox, monthBox, dayBox));
            monthBox.addActionListener(event -> updateDayComboBox(yearBox, monthBox, dayBox));

            String[] hours = IntStream.rangeClosed(1, 12).mapToObj(i -> String.format("%02d", i)).toArray(String[]::new);
            String[] minutes = {"00", "15", "30", "45"};
            String[] amPm = {"AM", "PM"};

            JComboBox<String> startHourBox = new JComboBox<>(hours);
            JComboBox<String> startMinuteBox = new JComboBox<>(minutes);
            JComboBox<String> startAmPmBox = new JComboBox<>(amPm);
            JComboBox<String> endHourBox = new JComboBox<>(hours);
            JComboBox<String> endMinuteBox = new JComboBox<>(minutes);
            JComboBox<String> endAmPmBox = new JComboBox<>(amPm);

            JPanel datePanel = new JPanel();
            datePanel.add(new JLabel("Date:"));
            datePanel.add(yearBox);
            datePanel.add(monthBox);
            datePanel.add(dayBox);

            JPanel startTimePanel = new JPanel();
            startTimePanel.add(new JLabel("Start:"));
            startTimePanel.add(startHourBox);
            startTimePanel.add(startMinuteBox);
            startTimePanel.add(startAmPmBox);

            JPanel endTimePanel = new JPanel();
            endTimePanel.add(new JLabel("End:"));
            endTimePanel.add(endHourBox);
            endTimePanel.add(endMinuteBox);
            endTimePanel.add(endAmPmBox);

            JPanel eventDetailsPanel = new JPanel();
            eventDetailsPanel.setLayout(new BoxLayout(eventDetailsPanel, BoxLayout.Y_AXIS));
            eventDetailsPanel.add(new JLabel("Name of event:"));
            eventDetailsPanel.add(eventNameField);
            eventDetailsPanel.add(datePanel);
            eventDetailsPanel.add(startTimePanel);
            eventDetailsPanel.add(endTimePanel);
            eventDetailsPanel.add(new JLabel("Description of event (optional):"));
            eventDetailsPanel.add(eventDescriptionArea);

            int result = JOptionPane.showConfirmDialog(null, eventDetailsPanel, "Please Enter Event Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Debug information: Print out the selected date and time
                    System.out.println("Selected Year: " + yearBox.getSelectedItem());
                    System.out.println("Selected Month: " + monthBox.getSelectedItem());
                    System.out.println("Selected Day: " + dayBox.getSelectedItem());
                    System.out.println("Selected Start Hour: " + startHourBox.getSelectedItem());
                    // ... Print statements for each time component ...

                    LocalDate eventDate = LocalDate.of((Integer) yearBox.getSelectedItem(), (Integer) monthBox.getSelectedItem(), (Integer) dayBox.getSelectedItem());
                    String startTimeStr = startHourBox.getSelectedItem() + ":" + startMinuteBox.getSelectedItem() + " " + startAmPmBox.getSelectedItem();
                    String endTimeStr = endHourBox.getSelectedItem() + ":" + endMinuteBox.getSelectedItem() + " " + endAmPmBox.getSelectedItem();

                    // Debug information: Print out the constructed date-time strings
                    System.out.println("Start Time String: " + startTimeStr);
                    System.out.println("End Time String: " + endTimeStr);

                    LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("hh:mm a"));
                    LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("hh:mm a"));

                    Event newEvent = new Event(eventNameField.getText(), startTime, endTime, eventDescriptionArea.getText());
                    weekPlan.addEvent(newEvent, eventDate);
                    textArea.append("Event added successfully.\n");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                    ex.printStackTrace(); // This will print the stack trace of the exception to the console
                }
            }
        });

        removeButton.addActionListener(e -> {
            JComboBox<String> eventCombo = new JComboBox<>();
            List<String> eventsInfo = weekPlan.getAllEventsNamesAndDates();
            for (String eventInfo : eventsInfo) {
                eventCombo.addItem(eventInfo);
            }

            int result = JOptionPane.showConfirmDialog(null, eventCombo, "Select an event to remove", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String selectedEventInfo = (String) eventCombo.getSelectedItem();
                String[] parts = selectedEventInfo.split(" - ");
                String eventName = parts[0];
                LocalDate eventDate = LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);

                if (weekPlan.removeEvent(eventName, eventDate)) {
                    JOptionPane.showMessageDialog(null, "Event removed successfully.");
                    // Refresh the view to show updated events list, if needed
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove the event. It may not exist.");
                }
            }
        });

        viewButton.addActionListener(e -> {
            LocalDate startOfWeek = LocalDate.now(); //.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            StringBuilder weekEvents = new StringBuilder("Events for the next 7 days:\n");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

            for (int i = 0; i < 7; i++) {
                LocalDate date = startOfWeek.plusDays(i);
                List<Event> eventsForDay = weekPlan.getEventsForDay(date); // Correctly typed as List<Event>
                weekEvents.append(date.format(dateFormatter)).append(":\n");
                if (eventsForDay.isEmpty()) {
                    weekEvents.append("    No events\n\n");
                } else {
                    for (Event event : eventsForDay) { // Now it's correctly typed and should not throw an error
                        weekEvents.append("    Event: ").append(event.getName()).append("\n")
                                .append("    Start Time: ").append(event.getStartTime().format(timeFormatter)).append("\n")
                                .append("    End Time: ").append(event.getEndTime().format(timeFormatter)).append("\n")
                                .append("    Description: ").append(event.getDescription()).append("\n\n");
                    }
                }
            }

            textArea.setText(weekEvents.toString());
        });

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                weekPlan.saveWeekPlanToFile(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Plan saved successfully.");
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a file to load");
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                weekPlan.loadWeekPlanFromFile(fileToLoad.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Plan loaded successfully.");
            }
        });
    }

    private void updateDayComboBox(JComboBox<Integer> yearBox, JComboBox<Integer> monthBox, JComboBox<Integer> dayBox) {
        YearMonth yearMonth = YearMonth.of((Integer) yearBox.getSelectedItem(), (Integer) monthBox.getSelectedItem());
        int daysInMonth = yearMonth.lengthOfMonth();
        dayBox.removeAllItems();
        IntStream.rangeClosed(1, daysInMonth).forEach(dayBox::addItem);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeeklyPlannerGUI::new);
    }
}