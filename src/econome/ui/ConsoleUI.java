package econome.ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import econome.model.Needs;
import econome.model.Wants;
import econome.model.Profile;
import econome.logic.BudgetManager;

/**
 * Console-based user interface for the EconoMe application.
 * <p>
 * The {@code ConsoleUI} provides a text-based interface that allows users to
 * interact with their financial profile directly in the terminal. It supports
 * adding, viewing, and marking completion of Needs and Wants, as well as
 * managing allocation settings for income distribution.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Handle all text-based user input and output.</li>
 *   <li>Provide menu-driven navigation for features.</li>
 *   <li>Delegate calculations to {@link BudgetManager}.</li>
 *   <li>Allow creation and management of {@link Needs} and {@link Wants}.</li>
 * </ul>
 */
public class ConsoleUI {

    // --- Dependencies --------------------------------------------------------

    private final Scanner scanner = new Scanner(System.in);
    private final BudgetManager budgetManager = new BudgetManager();


    // --- Entry Point ---------------------------------------------------------

    /**
     * Starts the console interface for the EconoMe application.
     * <p>
     * Handles profile creation, displays the main menu, and
     * continuously processes user commands until the user exits.
     * </p>
     */
    public void start() {
        System.out.println("Welcome to EconoMe!");

        // --- Profile setup (name and income only) ---
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        double income = promptDouble("Enter your monthly income: ");
        Profile profile = new Profile(name, income, 0.0);

        System.out.println("\nProfile created for " + profile.getName());
        System.out.println("Monthly income: " + profile.getIncome());

        // --- Main menu loop ---
        boolean running = true;
        while (running) {
            System.out.println("\n==== Main Menu ====");
            System.out.println("1) Add Need");
            System.out.println("2) View Needs");
            System.out.println("3) Mark Need Complete");
            System.out.println("4) Add Want");
            System.out.println("5) View Wants");
            System.out.println("6) Mark Want Complete");
            System.out.println("7) Change Funds Allocation");
            System.out.println("8) Show Current Allocation");
            System.out.println("9) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addNeed(profile); break;
                case "2": viewNeeds(profile); break;
                case "3": markNeedComplete(profile); break;
                case "4": addWant(profile); break;
                case "5": viewWants(profile); break;
                case "6": markWantComplete(profile); break;
                case "7": changeFundsAllocation(profile); break;
                case "8": showAllocations(profile); break;
                case "9": running = false; break;
                default: System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }

        System.out.println("Goodbye!");
    } // End of method start


    // --- Needs Management ----------------------------------------------------

    /** Prompts the user to create and add a new {@link Needs} item. */
    private void addNeed(Profile profile) {
        System.out.print("Enter need description: ");
        String desc = scanner.nextLine().trim();

        double cost = promptDouble("Enter cost (e.g., 59.99): ");
        LocalDate dueDate = promptDate("Enter due date (YYYY-MM-DD): ");

        Needs need = new Needs(desc, cost, dueDate);
        profile.addNeed(need);

        System.out.println("Added need: " + desc + " | $" + cost + " | Due " + dueDate);
    } // End of method addNeed


    /** Displays all {@link Needs} currently stored in the profile. */
    private void viewNeeds(Profile profile) {
        List<Needs> needs = profile.getNeedsList();
        if (needs.isEmpty()) {
            System.out.println("(No needs yet)");
            return;
        }

        System.out.println("\n# | Description               | Cost      | Due Date   | Complete");
        System.out.println("---+---------------------------+-----------+------------+---------");
        for (int i = 0; i < needs.size(); i++) {
            Needs need = needs.get(i);
            System.out.printf("%2d | %-25s | $%8.2f | %10s | %s%n",
                    i + 1, need.getDescription(), need.getCost(),
                    need.getDueDate(), need.isComplete());
        }
    } // End of method viewNeeds


    /** Marks a {@link Needs} item as complete, based on user selection. */
    private void markNeedComplete(Profile profile) {
        List<Needs> needs = profile.getNeedsList();
        if (needs.isEmpty()) {
            System.out.println("(No needs to mark complete)");
            return;
        }

        viewNeeds(profile); // Show list for reference
        int index = promptInt("Enter # to mark complete: ", 1, needs.size());
        Needs selectedNeed = needs.get(index - 1);
        selectedNeed.markComplete();

        System.out.println("Marked complete: " + selectedNeed.getDescription());
    } // End of method markNeedComplete


    // --- Wants Management ----------------------------------------------------

    /** Prompts the user to create and add a new {@link Wants} item. */
    private void addWant(Profile profile) {
        System.out.print("Enter want description: ");
        String desc = scanner.nextLine().trim();

        double cost = promptDouble("Enter cost (e.g., 500.00): ");
        LocalDate dueDate = promptDate("Enter target date (YYYY-MM-DD): ");

        Wants want = new Wants(desc, cost, dueDate);
        profile.addWant(want);

        System.out.println("Added want: " + desc + " | $" + cost + " | Target " + dueDate);
    } // End of method addWant


    /** Displays all {@link Wants} currently stored in the profile. */
    private void viewWants(Profile profile) {
        List<Wants> wants = profile.getWantsList();
        if (wants.isEmpty()) {
            System.out.println("(No wants yet)");
            return;
        }

        System.out.println("\n# | Description               | Cost      | Target Date | Complete");
        System.out.println("---+---------------------------+-----------+-------------+---------");
        for (int i = 0; i < wants.size(); i++) {
            Wants want = wants.get(i);
            System.out.printf("%2d | %-25s | $%8.2f | %11s | %s%n",
                    i + 1, want.getDescription(), want.getCost(),
                    want.getDueDate(), want.isComplete());
        }
    } // End of method viewWants


    /** Marks a {@link Wants} item as complete, based on user selection. */
    private void markWantComplete(Profile profile) {
        List<Wants> wants = profile.getWantsList();
        if (wants.isEmpty()) {
            System.out.println("(No wants to mark complete)");
            return;
        }

        viewWants(profile); // Show list for reference
        int index = promptInt("Enter # to mark complete: ", 1, wants.size());
        Wants selectedWant = wants.get(index - 1);
        selectedWant.markComplete();

        System.out.println("Marked complete: " + selectedWant.getDescription());
    } // End of method markWantComplete


    // --- Allocation Management ----------------------------------------------

    /**
     * Allows the user to modify income allocation preferences.
     * <p>
     * Users may choose between percentage-based or fixed-amount allocations.
     * </p>
     */
    private void changeFundsAllocation(Profile profile) {
        System.out.println("\nChange Funds Allocation");
        System.out.println("1) Use Percentages (e.g., 50/30/20)");
        System.out.println("2) Use Fixed Amounts (e.g., $1000, $500, $200)");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        boolean byPercentage = choice.equals("1");

        double needs = promptDouble("Enter allocation for Needs (" + (byPercentage ? "%" : "$") + "): ");
        double wants = promptDouble("Enter allocation for Wants (" + (byPercentage ? "%" : "$") + "): ");
        double savings = promptDouble("Enter allocation for Savings (" + (byPercentage ? "%" : "$") + "): ");

        profile.setAllocations(needs, wants, savings, byPercentage);
        System.out.println("Allocations updated successfully!");
    } // End of method changeFundsAllocation


    /**
     * Displays the user's current allocation setup.
     * <p>
     * Uses preview mode to calculate allocations without modifying the savings balance.
     * </p>
     */
    private void showAllocations(Profile profile) {
        System.out.println("\nCurrent Allocations:");
        if (profile.getNeedsAllocation() == 0 &&
            profile.getWantsAllocation() == 0 &&
            profile.getSavingsAllocation() == 0) {
            System.out.println("(No allocations set yet)");
            return;
        }

        if (profile.isAllocationByPercentage()) {
            System.out.printf("Needs: %.2f%% (%.2f)%n", profile.getNeedsAllocation(),
                    budgetManager.calculateNeedsAmount(profile));
            System.out.printf("Wants: %.2f%% (%.2f)%n", profile.getWantsAllocation(),
                    budgetManager.calculateWantsAmount(profile));
            System.out.printf("Savings: %.2f%% (%.2f)%n", profile.getSavingsAllocation(),
                    budgetManager.calculateProjectedSavings(profile));
        } else {
            System.out.printf("Needs: $%.2f%n", profile.getNeedsAllocation());
            System.out.printf("Wants: $%.2f%n", profile.getWantsAllocation());
            System.out.printf("Savings: $%.2f (Balance: %.2f)%n",
                    profile.getSavingsAllocation(), profile.getSavingsBalance());
        }
    } // End of method showAllocations


    // --- Input Utilities -----------------------------------------------------

    /** Prompts the user for a double value, re-prompting until valid. */
    private double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (e.g., 1200.00).");
            }
        }
    } // End of method promptDouble


    /**
     * Prompts the user for an integer within a specified range.
     *
     * @param prompt message to display to the user
     * @param min    minimum allowed value
     * @param max    maximum allowed value
     * @return a valid integer entered by the user
     */
    private int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("Enter a number between %d and %d.%n", min, max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    } // End of method promptInt


    /**
     * Prompts the user to input a date in {@code YYYY-MM-DD} format.
     * Re-prompts until a valid date is entered.
     *
     * @param prompt message to display to the user
     * @return a valid {@link LocalDate} object
     */
    private LocalDate promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use format YYYY-MM-DD (e.g., 2025-10-03).");
            }
        }
    } // End of method promptDate

} // End of class ConsoleUI