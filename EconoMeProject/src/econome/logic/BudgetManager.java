package econome.logic;

import econome.model.Profile;

public class BudgetManager {
	
	private double needs;
    private double wants;
    private double savings;

    public void calculateBudget(Profile profile) {
        double income = profile.getIncome();
        this.needs = income * 0.50;   // 50% Needs
        this.wants = income * 0.30;   // 30% Wants
        this.savings = income * 0.20; // 20% Savings
    }

    public void printBudget() {
        System.out.println("Needs: $" + needs);
        System.out.println("Wants: $" + wants);
        System.out.println("Savings: $" + savings);
    }
}