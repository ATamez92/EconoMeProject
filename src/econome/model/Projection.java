package econome.model;

/**
 * Represents the result of a financial projection in the EconoMe application.
 * 
 * Responsibilities:
 * - Store how much a user plans to contribute monthly.
 * - Store the estimated number of months required to achieve a goal.
 *
 * Example usage:
 *   A projection might show that contributing $200 per month
 *   will allow a user to reach a $2,000 goal in 10 months.
 */
public class Projection {
    private double monthlyContribution;
    private int monthsNeeded;

    /**
     * Creates a Projection object.
     *
     * @param monthlyContribution the planned contribution per month
     * @param monthsNeeded the estimated number of months required
     */
    public Projection(double monthlyContribution, int monthsNeeded) {
        this.monthlyContribution = monthlyContribution;
        this.monthsNeeded = monthsNeeded;
    }

    /** @return the monthly contribution amount */
    public double getMonthlyContribution() { return monthlyContribution; }

    /** Updates the monthly contribution amount */
    public void setMonthlyContribution(double monthlyContribution) { this.monthlyContribution = monthlyContribution; }

    /** @return the estimated months required for the projection */
    public int getMonthsNeeded() { return monthsNeeded; }

    /** Updates the estimated months required */
    public void setMonthsNeeded(int monthsNeeded) { this.monthsNeeded = monthsNeeded; }
}