package econome.logic;

import econome.model.Profile;

/**
 * Handles core financial calculations for the EconoMe application.
 * <p>
 * The {@code BudgetManager} class determines how a user's income is
 * divided across their Needs, Wants, and Savings categories based on
 * either percentage-based or fixed-value allocations.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Calculate the monetary amount allocated for Needs, Wants, and Savings.</li>
 *   <li>Preview or apply savings to the user's total balance.</li>
 * </ul>
 */
public class BudgetManager {

    // --- Calculation Methods ---

    /**
     * Calculates the monetary amount allocated to the user's Needs category.
     *
     * @param userProfile the user's financial profile
     * @return the calculated Needs amount based on allocation type
     */
    public double calculateNeedsAmount(Profile userProfile) {
        return userProfile.isAllocationByPercentage()
                ? userProfile.getIncome() * (userProfile.getNeedsAllocation() / 100.0)
                : userProfile.getNeedsAllocation();
    } // End of method calculateNeedsAmount


    /**
     * Calculates the monetary amount allocated to the user's Wants category.
     *
     * @param userProfile the user's financial profile
     * @return the calculated Wants amount based on allocation type
     */
    public double calculateWantsAmount(Profile userProfile) {
        return userProfile.isAllocationByPercentage()
                ? userProfile.getIncome() * (userProfile.getWantsAllocation() / 100.0)
                : userProfile.getWantsAllocation();
    } // End of method calculateWantsAmount


    /**
     * Calculates the projected amount to be saved this cycle.
     * <p>
     * This method does <b>not</b> modify the user's profile balance — 
     * it only returns the computed amount for preview purposes.
     * </p>
     *
     * @param userProfile the user's financial profile
     * @return the projected savings amount based on allocation type
     */
    public double calculateProjectedSavings(Profile userProfile) {
        return userProfile.isAllocationByPercentage()
                ? userProfile.getIncome() * (userProfile.getSavingsAllocation() / 100.0)
                : userProfile.getSavingsAllocation();
    } // End of method calculateProjectedSavings


    // --- Update Methods ---

    /**
     * Applies the calculated savings amount to the user's total savings balance.
     * <p>
     * This method retrieves the projected savings amount and adds it to the
     * user's existing savings balance.
     * </p>
     *
     * @param userProfile the user's financial profile
     * @return the amount added to the savings balance
     */
    public double applySavingsToProfile(Profile userProfile) {
        double savings = calculateProjectedSavings(userProfile);
        userProfile.setSavingsBalance(userProfile.getSavingsBalance() + savings);
        return savings;
    } // End of method applySavingsToProfile

} // End of class BudgetManager