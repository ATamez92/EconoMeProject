package econome.model;

import java.io.Serializable;

/**
 * Represents a financial projection result within the EconoMe application.
 * <p>
 * A {@code Projection} models how a user’s savings goal might progress over time
 * based on their planned monthly contribution. It stores both the recurring
 * contribution amount and the estimated number of months required to reach
 * the target goal.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Store the user’s monthly contribution amount.</li>
 *   <li>Store the projected duration (in months) needed to achieve a goal.</li>
 * </ul>
 *
 * <h3>Example:</h3>
 * <pre>
 * Projection projection = new Projection(200.0, 10);
 * // Means contributing $200 per month for 10 months
 * // to reach the desired savings goal.
 * </pre>
 */
public class Projection implements Serializable {

    // --- Serialization -------------------------------------------------------

    /** Required for consistent serialization across versions. */
    private static final long serialVersionUID = 1L;


    // --- Fields --------------------------------------------------------------

    private double monthlyContribution;
    private int monthsNeeded;


    // --- Constructor ---------------------------------------------------------

    /**
     * Constructs a new {@code Projection} with the specified contribution
     * and estimated duration.
     *
     * @param monthlyContribution the amount the user contributes each month
     * @param monthsNeeded        the estimated number of months required
     */
    public Projection(double monthlyContribution, int monthsNeeded) {
        this.monthlyContribution = monthlyContribution;
        this.monthsNeeded = monthsNeeded;
    } // End of constructor Projection


    // --- Accessors & Mutators ------------------------------------------------

    /** @return the planned monthly contribution amount */
    public double getMonthlyContribution() {
        return monthlyContribution;
    } // End of method getMonthlyContribution

    /** Updates the monthly contribution amount. */
    public void setMonthlyContribution(double monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    } // End of method setMonthlyContribution

    /** @return the estimated number of months required to reach the goal */
    public int getMonthsNeeded() {
        return monthsNeeded;
    } // End of method getMonthsNeeded

    /** Updates the estimated number of months required to reach the goal. */
    public void setMonthsNeeded(int monthsNeeded) {
        this.monthsNeeded = monthsNeeded;
    } // End of method setMonthsNeeded

} // End of class Projection