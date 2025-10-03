package econome.model;

public class Projection {
	private double monthlyContribution;
    private int monthsNeeded;

    public Projection(double monthlyContribution, int monthsNeeded) {
        this.monthlyContribution = monthlyContribution;
        this.monthsNeeded = monthsNeeded;
    }

    public double getMonthlyContribution() { return monthlyContribution; }
    public void setMonthlyContribution(double monthlyContribution) { this.monthlyContribution = monthlyContribution; }

    public int getMonthsNeeded() { return monthsNeeded; }
    public void setMonthsNeeded(int monthsNeeded) { this.monthsNeeded = monthsNeeded; }
}