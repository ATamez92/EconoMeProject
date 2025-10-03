package econome.logic;

import econome.model.Profile;

public class BudgetManager {
	
	 // Splits income into Needs/Wants/Savings (logic to be added later)
    public double calculateNeeds(Profile profile) {
        return profile.isAllocationByPercentage()
                ? profile.getIncome() * (profile.getNeedsAllocation() / 100.0)
                : profile.getNeedsAllocation();
    }
    
    public double calculateWants(Profile profile) {
        return profile.isAllocationByPercentage()
                ? profile.getIncome() * (profile.getWantsAllocation() / 100.0)
                : profile.getWantsAllocation();
    }

    // Preview savings without updating balance
    public double previewSavings(Profile profile) {
        return profile.isAllocationByPercentage()
                ? profile.getIncome() * (profile.getSavingsAllocation() / 100.0)
                : profile.getSavingsAllocation();
    }

    // Apply savings and update balance
    public double applySavings(Profile profile) {
        double savings = previewSavings(profile);
        double newBalance = profile.getSavingsBalance() + savings;
        profile.setSavingsBalance(newBalance);
        return savings;
    }
}