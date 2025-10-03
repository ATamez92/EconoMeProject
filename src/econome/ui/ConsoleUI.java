package econome.ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import econome.model.Needs;
import econome.model.Profile;
import econome.logic.BudgetManager;

public class ConsoleUI {
	 private final Scanner scanner = new Scanner(System.in);

	    public void start() {
	        System.out.println("Welcome to EconoMe!");

	        // Profile setup (no age)
	        System.out.print("Enter your name: ");
	        String name = scanner.nextLine();

	        double income = promptDouble("Enter your monthly income: ");

	        Profile profile = new Profile(name, income, 0.0);

	        System.out.println("\nProfile created for " + profile.getName());
	        System.out.println("Monthly income: " + profile.getIncome());

	        // Main loop — Needs list demo (add/view/mark complete/exit)
	        boolean running = true;
	        while (running) {
	            System.out.println("\n==== Needs Menu ====");
	            System.out.println("1) Add Need");
	            System.out.println("2) View Needs");
	            System.out.println("3) Mark Need Complete");
	            System.out.println("4) Exit");
	            System.out.print("Choose an option: ");

	            String choice = scanner.nextLine().trim();
	            switch (choice) {
	                case "1":
	                    addNeed(profile);
	                    break;
	                case "2":
	                    viewNeeds(profile);
	                    break;
	                case "3":
	                    markNeedComplete(profile);
	                    break;
	                case "4":
	                    running = false;
	                    break;
	                default:
	                    System.out.println("Invalid option. Try 1-4.");
	            }
	        }

	        System.out.println("Goodbye!");
	    }

	    // ---- Helpers ----

	    private void addNeed(Profile profile) {
	        System.out.print("Enter need description: ");
	        String desc = scanner.nextLine().trim();

	        double cost = promptDouble("Enter cost (e.g., 59.99): ");

	        LocalDate dueDate = promptDate("Enter due date (YYYY-MM-DD): ");

	        Needs need = new Needs(desc, cost, dueDate);
	        profile.addNeed(need);
	        System.out.println("Added need: " + desc + " | $" + cost + " | Due " + dueDate);
	    }

	    private void viewNeeds(Profile profile) {
	        List<Needs> needs = profile.getNeedsList();
	        if (needs.isEmpty()) {
	            System.out.println("(No needs yet)");
	            return;
	        }
	        System.out.println("\n# | Description               | Cost      | Due Date   | Complete");
	        System.out.println("---+---------------------------+-----------+------------+---------");
	        for (int i = 0; i < needs.size(); i++) {
	            Needs n = needs.get(i);
	            System.out.printf("%2d | %-25s | $%8.2f | %10s | %s%n",
	                    i + 1, n.getDescription(), n.getCost(), n.getDueDate(), n.isComplete());
	        }
	    }

	    private void markNeedComplete(Profile profile) {
	        List<Needs> needs = profile.getNeedsList();
	        if (needs.isEmpty()) {
	            System.out.println("(No needs to mark complete)");
	            return;
	        }

	        viewNeeds(profile);
	        int idx = promptInt("Enter # to mark complete: ", 1, needs.size());
	        Needs n = needs.get(idx - 1);
	        n.markComplete();
	        System.out.println("Marked complete: " + n.getDescription());
	    }

	    private double promptDouble(String prompt) {
	        while (true) {
	            System.out.print(prompt);
	            String in = scanner.nextLine().trim();
	            try {
	                return Double.parseDouble(in);
	            } catch (NumberFormatException e) {
	                System.out.println("Please enter a valid number (e.g., 1200.00).");
	            }
	        }
	    }

	    private int promptInt(String prompt, int min, int max) {
	        while (true) {
	            System.out.print(prompt);
	            String in = scanner.nextLine().trim();
	            try {
	                int val = Integer.parseInt(in);
	                if (val < min || val > max) {
	                    System.out.printf("Enter a number between %d and %d.%n", min, max);
	                } else {
	                    return val;
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Please enter a whole number.");
	            }
	        }
	    }

	    private LocalDate promptDate(String prompt) {
	        while (true) {
	            System.out.print(prompt);
	            String in = scanner.nextLine().trim();
	            try {
	                return LocalDate.parse(in);
	            } catch (DateTimeParseException e) {
	                System.out.println("Invalid date. Use format YYYY-MM-DD (e.g., 2025-10-03).");
	            }
	        }
	    }
	}