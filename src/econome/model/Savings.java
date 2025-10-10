package econome.model;

import java.io.Serializable;

/**
 * Represents the user's savings account within the EconoMe application.
 * <p>
 * A {@code Savings} object maintains the user’s current balance and provides
 * simple methods to retrieve or update that balance. It functions as part of
 * the user’s {@link Profile}, allowing the application to track accumulated
 * savings over time.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Store the current savings balance.</li>
 *   <li>Allow updating and retrieving the balance value.</li>
 * </ul>
 */
public class Savings implements Serializable {

    // --- Serialization -------------------------------------------------------

    /** Ensures consistent serialization across application versions. */
    private static final long serialVersionUID = 1L;


    // --- Fields --------------------------------------------------------------

    /** The user’s current savings balance. */
    private double balance;


    // --- Constructor ---------------------------------------------------------

    /**
     * Constructs a new {@code Savings} instance with the specified balance.
     *
     * @param balance the starting savings balance
     */
    public Savings(double balance) {
        this.balance = balance;
    } // End of constructor Savings


    // --- Accessors & Mutators ------------------------------------------------

    /** @return the user’s current savings balance */
    public double getBalance() {
        return balance;
    } // End of method getBalance

    /**
     * Updates the savings balance to a new amount.
     *
     * @param balance the new balance value to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    } // End of method setBalance

} // End of class Savings