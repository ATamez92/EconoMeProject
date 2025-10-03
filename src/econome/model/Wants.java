package econome.model;

import java.time.LocalDate;

/**
 * Represents a discretionary financial goal ("Want") for the user.
 * Examples: vacation, new gadget, or entertainment purchase.
 *
 * Each Want has:
 * - A description (name of the goal/purchase).
 * - A cost (expected amount required).
 * - A target due date.
 * - A completion status (whether it has been achieved or marked complete).
 */
public class Wants {
    private String description;
    private double cost;
    private LocalDate dueDate;
    private boolean isComplete;

    /**
     * Creates a new Want item.
     *
     * @param description description of the want
     * @param cost expected cost
     * @param dueDate target date for achieving this want
     */
    public Wants(String description, double cost, LocalDate dueDate) {
        this.description = description;
        this.cost = cost;
        this.dueDate = dueDate;
        this.isComplete = false; // default to incomplete
    }

    // --- Getters ---

    /** @return the description of this want */
    public String getDescription() { return description; }

    /** @return the cost of this want */
    public double getCost() { return cost; }

    /** @return the due date for this want */
    public LocalDate getDueDate() { return dueDate; }

    /** @return true if the want has been marked complete */
    public boolean isComplete() { return isComplete; }

    // --- Setters (optional) ---

    /** Updates the description of this want */
    public void setDescription(String description) { this.description = description; }

    /** Updates the cost of this want */
    public void setCost(double cost) { this.cost = cost; }

    /** Updates the due date of this want */
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // --- Actions ---

    /**
     * Marks this want as completed.
     * Can be used when the user has achieved or purchased the item.
     */
    public void markComplete() {
        this.isComplete = true;
    }
}