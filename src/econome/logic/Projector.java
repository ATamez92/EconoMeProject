package econome.logic;

import econome.model.Wants;
import econome.model.Profile;

/**
 * Handles projection and forecasting logic for the EconoMe application.
 * <p>
 * The {@code Projector} class provides estimates on how long it will take
 * users to achieve their financial goals (Wants), based on income, savings,
 * and monthly contributions.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Estimate completion time for each financial goal (Want).</li>
 *   <li>Use user profile data (savings balance, income) for calculations.</li>
 *   <li>Provide edge-case handling for invalid or completed goals.</li>
 * </ul>
 */
public class Projector {

    // --- Public Projection Methods -------------------------------------------

    /**
     * Estimates the number of months required for a user to reach a specific
     * financial goal based on their current savings and monthly contribution.
     * <p>
     * The calculation subtracts current savings from the total goal cost,
     * divides by the monthly contribution amount, and rounds up to the nearest
     * full month. The result represents an estimated time to completion.
     * </p>
     *
     * @param targetGoal the financial goal (Want) being evaluated
     * @param userProfile the user's {@link Profile} containing savings data
     * @param monthlyContribution the user's planned monthly contribution
     * @return the estimated number of months required to reach the goal:
     *         <ul>
     *             <li>{@code 0} → if the goal is already met or has no cost</li>
     *             <li>{@code -1} → if the contribution is zero or negative (unreachable goal)</li>
     *             <li>{@code >0} → estimated months to reach goal</li>
     *         </ul>
     */
    public int estimateGoalCompletionMonths(Wants targetGoal, Profile userProfile, double monthlyContribution) {
        double goalCost = targetGoal.getCost();
        double currentSavings = userProfile.getSavingsBalance();
        double remainingAmount = goalCost - currentSavings;

        // --- Edge Case 1: Goal has no cost or is already fulfilled
        if (goalCost <= 0 || remainingAmount <= 0) {
            return 0;
        }

        // --- Edge Case 2: No or invalid monthly contribution (unreachable goal)
        if (monthlyContribution <= 0) {
            return -1;
        }

        // --- Standard Case: Calculate months required (rounded up)
        int monthsRequired = (int) Math.ceil(remainingAmount / monthlyContribution);
        return monthsRequired;
    } // End of method estimateGoalCompletionMonths

} // End of class Projector