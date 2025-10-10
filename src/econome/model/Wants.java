package econome.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a discretionary financial goal ("Want") for the user.
 * <p>
 * Examples include vacations, electronics, entertainment, or other
 * non-essential purchases. Each {@code Want} tracks its description,
 * cost, target date, and completion status.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Store descriptive and financial details of a user's goal.</li>
 *   <li>Track completion status (achieved or not).</li>
 * </ul>
 */
public class Wants implements Serializable {

    // --- Serialization -------------------------------------------------------

    /** Ensures consistent serialization across versions. */
    private static final long serialVersionUID = 1L;


    // --- Fields --------------------------------------------------------------

    /** Short description of the financial goal (e.g., "Vacation to Japan"). */
    private String description;

    /** Total expected cost required to achieve the goal. */
    private double cost;

    /** Target date by which the user aims to achieve the goal. */
    private LocalDate dueDate;

    /** Whether this goal has been marked as complete or achieved. */
    private boolean isComplete;


    // --- Constructor ---------------------------------------------------------

    /**
     * Constructs a new {@code Want} with the given details.
     *
     * @param description  a short description of the goal
     * @param cost         the expected cost of achieving the goal
     * @param dueDate      the target completion date
     */
    public Wants(String description, double cost, LocalDate dueDate) {
        this.description = description;
        this.cost = cost;
        this.dueDate = dueDate;
        this.isComplete = false; // Default to incomplete
    } // End of constructor Wants


    // --- Accessors -----------------------------------------------------------

    /** @return the description of this Want */
    public String getDescription() {
        return description;
    } // End of method getDescription

    /** @return the total cost associated with this Want */
    public double getCost() {
        return cost;
    } // End of method getCost

    /** @return the target date by which this Want should be completed */
    public LocalDate getDueDate() {
        return dueDate;
    } // End of method getDueDate

    /** @return {@code true} if this Want has been marked as complete */
    public boolean isComplete() {
        return isComplete;
    } // End of method isComplete


    // --- Mutators ------------------------------------------------------------

    /** Updates the description for this Want. */
    public void setDescription(String description) {
        this.description = description;
    } // End of method setDescription

    /** Updates the total cost for this Want. */
    public void setCost(double cost) {
        this.cost = cost;
    } // End of method setCost

    /** Updates the target completion date for this Want. */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    } // End of method setDueDate


    // --- Actions -------------------------------------------------------------

    /**
     * Marks this Want as completed.
     * <p>
     * Typically used when the user has achieved or purchased the desired item.
     * </p>
     */
    public void markComplete() {
        this.isComplete = true;
    } // End of method markComplete

} // End of class Wants