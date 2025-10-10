package econome.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a required financial obligation (“Need”) for the user.
 * <p>
 * Examples include rent, utilities, groceries, or other essential expenses.
 * Each {@code Needs} object tracks its description, cost, due date,
 * and whether it has been completed or paid.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Store descriptive and financial details of a required expense.</li>
 *   <li>Track completion status for payments or obligations.</li>
 * </ul>
 *
 * <h3>Attributes:</h3>
 * <ul>
 *   <li><b>description</b> — purpose of the expense</li>
 *   <li><b>cost</b> — expected payment amount</li>
 *   <li><b>dueDate</b> — payment due date</li>
 *   <li><b>isComplete</b> — whether the expense has been paid</li>
 * </ul>
 */
public class Needs implements Serializable {

    // --- Serialization -------------------------------------------------------

    /** Required for consistent serialization across versions. */
    private static final long serialVersionUID = 1L;


    // --- Fields --------------------------------------------------------------

    private String description;
    private double cost;
    private LocalDate dueDate;
    private boolean isComplete;


    // --- Constructors --------------------------------------------------------

    /**
     * Constructs a new {@code Needs} object representing a required expense.
     *
     * @param description a short description of the expense (e.g., "Rent", "Groceries")
     * @param cost the expected cost of the expense
     * @param dueDate the date the expense is due
     */
    public Needs(String description, double cost, LocalDate dueDate) {
        this.description = description;
        this.cost = cost;
        this.dueDate = dueDate;
        this.isComplete = false; // Default state: incomplete
    } // End of constructor Needs


    // --- Accessors (Getters) -------------------------------------------------

    /** @return the description of this Need */
    public String getDescription() {
        return description;
    } // End of method getDescription

    /** @return the expected cost of this Need */
    public double getCost() {
        return cost;
    } // End of method getCost

    /** @return the due date of this Need */
    public LocalDate getDueDate() {
        return dueDate;
    } // End of method getDueDate

    /** @return {@code true} if this Need has been marked complete */
    public boolean isComplete() {
        return isComplete;
    } // End of method isComplete


    // --- Mutators (Actions) --------------------------------------------------

    /**
     * Marks this Need as completed (for example, when the bill has been paid).
     */
    public void markComplete() {
        this.isComplete = true;
    } // End of method markComplete


} // End of class Needs