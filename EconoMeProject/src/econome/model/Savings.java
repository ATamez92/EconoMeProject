package econome.model;

public class Savings {
	private double amount;

	public Savings(double initialAmount) {
		this.amount = initialAmount;
	}

	public double getAmount() {
		return amount;
	}

	public void deposit(double amount) {
		if (amount > 0) {
			this.amount += amount;
		}
	}

	public boolean withdraw(double amount) {
		if (amount > 0 && amount <= this.amount) {
			this.amount -= amount;
			return true;
		}
		return false;
	}
	
}
