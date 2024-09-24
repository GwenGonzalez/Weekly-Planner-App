package Project1Iteration1;

import java.util.Scanner;

public class Main {
	static TimeUtil timeUtil = new TimeUtil();
	static DateUtil dateUtil = new DateUtil();
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to Weekly Planner");
		Scanner scnr = new Scanner(System.in);	
		String userOption = "0";
		
		System.out.print("Please type out the start date of the week in the format YYYY-MM-DD: ");
		WeekPlan weekPlan = new WeekPlan(dateUtil.parseDate(scnr.nextLine()));
		
		//Output the options for the user repeatedly.	
		while(!(userOption.equals("6"))) {
			System.out.println("1. Add Event");
			System.out.println("2. Remove Event");
			System.out.println("3. View Week");
			System.out.println("4. Save Week Plan");
			System.out.println("5. Load Week Plan");
			System.out.println("6. Quit");
			System.out.print("Enter your choice: ");
			
			//Scans for the user's option 1-6.
			userOption = scnr.nextLine();
			
			switch(userOption) {
				//Prints the all countries.
				case "1":
					System.out.print("Name of event: ");
					String eventName = scnr.nextLine();
					System.out.print("Date of the event in the format YYYY-MM-DD: ");
					String eventDate = scnr.nextLine();
					System.out.print("Start time of event in HH:MM format: ");
					String startTime = scnr.nextLine();
					System.out.print("End time of event in HH:MM format: ");
					String endTime = scnr.nextLine();
					System.out.print("Description of event (optional): ");
					String description = scnr.nextLine();
					weekPlan.addEvent(new Event(eventName, timeUtil.parseTime(startTime), timeUtil.parseTime(endTime), description), dateUtil.parseDate(eventDate));
					break;
				//Sorts all the countries by name.
				case "2":
					System.out.print("Name of event you want to delete: ");
					String delEventName = scnr.nextLine();
					System.out.print("Date of the event in the format YYYY-MM-DD: ");
					String delEventDate = scnr.nextLine();
					weekPlan.removeEvent(delEventName, dateUtil.parseDate(delEventDate));
					break;

				case "3":
					weekPlan.printWeekPlan();
					break;

				case "4":
					System.out.print("Enter filename to save: ");
					String saveFilename = scnr.nextLine();
					weekPlan.saveWeekPlanToFile(saveFilename);
					System.out.println("Week plan saved successfully.");
					break;

				case "5":
					System.out.print("Enter filename to load: ");
					String loadFilename = scnr.nextLine();
					weekPlan.loadWeekPlanFromFile(loadFilename);
					System.out.println("Week plan loaded successfully.");
					break;

				case "6":
					System.out.println("Have a good day!");
					break;

				default:
					System.out.println("Invalid choice, try again.");
					break;
			}
		}
		scnr.close();
	}
}