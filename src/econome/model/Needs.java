package econome.model;

import java.time.LocalDate;

/**
 * Represents an essential financial obligation ("Need") for the user.
 * Examples: rent, utilities, groceries, or other mandatory expenses.
 *
 * Each Need has:
 * - A description (what the expense is for).
 * - A cost (expected payment amount).
 * - A due date (when it must be paid).
 * - A completion status (whether it has been paid/fulfilled).
 */
public class Needs {
    private String description;
    private double cost;
    private LocalDate dueDate;
    private boolean isComplete;

    /**
     * Creates a new Need item.
     *
     * @param description description of the expense
     * @param cost expected cost of the expense
     * @param dueDate due date for the expense
     */
    public Needs(String description, double cost, LocalDate dueDate) {
        this.description = description;
        this.cost = cost;
        this.dueDate = dueDate;
        this.isComplete = false; // default to incomplete
    }

    /** @return the description of this need */
    public String getDescription() { return description; }

    /** @return the cost of this need */
    public double getCost() { return cost; }

    /** @return the due date for this need */
    public LocalDate getDueDate() { return dueDate; }

    /** @return true if this need has been marked complete */
    public boolean isComplete() { return isComplete; }

    /**
     * Marks this need as completed (e.g., bill has been paid).
     */
    public void markComplete() { this.isComplete = true; }
}