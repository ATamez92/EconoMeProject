package econome.model;

/**
 * Represents the user's savings account in the EconoMe application.
 * 
 * Responsibilities:
 * - Store the current savings balance.
 * - Provide methods to retrieve and update the balance.
 */
public class Savings {
    private double balance;

    /**
     * Creates a Savings object with an initial balance.
     *
     * @param balance the starting balance for savings
     */
    public Savings(double balance) {
        this.balance = balance;
    }

    /**
     * @return the current savings balance
     */
    public double getBalance() { return balance; }

    /**
     * Updates the savings balance.
     *
     * @param balance the new balance amount
     */
    public void setBalance(double balance) { this.balance = balance; }
}