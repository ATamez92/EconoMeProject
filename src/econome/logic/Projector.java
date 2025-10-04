package econome.logic;

import econome.model.Wants;
import econome.model.Profile;
import econome.model.Profile;

/**
 * Provides projection-related calculations for the EconoMe application.
 * 
 * Responsibilities:
 * - Estimate timelines for financial goals (Wants).
 * - Use profile data and user-defined contributions to project completion.
 */
public class Projector {

    /**
     * Estimates the number of months needed to reach a financial goal.
     * <p>
     * Note: This method is currently a placeholder and should be implemented
     * with actual projection logic (e.g., dividing goal cost by contributions).
     *
     * @param want the financial goal (Want) to achieve
     * @param profile the user's financial profile (income, savings, etc.)
     * @param monthlyContribution how much the user plans to contribute each month
     * @return estimated months required to reach the goal (currently always 0)
     */
  public int estimateGoalTimeline(Wants want, Profile profile, double monthlyContribution) {
        double goalCost = want.getCost();
        double currentSavings = profile.getSavingsBalance();
        double remainingAmount = goalCost - currentSavings;

        // Edge cases
        if (goalCost <= 0) {
        	// No cost, no time needed
            return 0; 
        }
     // Already have enough savings
        if (remainingAmount <= 0) {
            return 0; 
        }
     // Cannot reach goal with zero/negative contribution
        if (monthlyContribution <= 0) {
            return -1; 
        }

        // Calculate months needed (round up to nearest whole month)
        int months = (int) Math.ceil(remainingAmount / monthlyContribution);
        return months;
    }
}
