package econome.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import econome.logic.ProfileManager;

/**
 * Represents a user's financial profile in the EconoMe application.
 * <p>
 * Each {@code Profile} stores personal financial data such as income, savings,
 * and lists of Needs and Wants. Profiles also record how the user allocates
 * their funds—either by percentage or by fixed amounts.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Store and manage the user’s Needs and Wants lists.</li>
 *   <li>Track income, savings, and allocation preferences.</li>
 *   <li>Persist changes automatically to local storage via serialization.</li>
 * </ul>
 */
public class Profile implements Serializable {

    // --- Serialization -------------------------------------------------------

    /** Ensures consistent serialization across versions. */
    private static final long serialVersionUID = 1L;


    // --- Profile Information -------------------------------------------------

    private String name;
    private double income;
    private double savingsBalance;


    // --- Allocation Settings -------------------------------------------------

    /** Allocations may represent percentages (if {@code allocationByPercentage = true}) or fixed values. */
    private double needsAllocation;
    private double wantsAllocation;
    private double savingsAllocation;
    private boolean allocationByPercentage; // true = %, false = fixed amount


    // --- Task Lists ----------------------------------------------------------

    private List<Needs> needsList;
    private List<Wants> wantsList;


    // --- Constructor ---------------------------------------------------------

    /**
     * Creates a new {@code Profile} with an initial income and savings balance.
     *
     * @param name           the user's name
     * @param income         the user's monthly income
     * @param savingsBalance the starting savings balance
     */
    public Profile(String name, double income, double savingsBalance) {
        this.name = name;
        this.income = income;
        this.savingsBalance = savingsBalance;

        this.needsList = new ArrayList<>();
        this.wantsList = new ArrayList<>();

        // Default: allocations set to 0%, percentage-based mode
        this.allocationByPercentage = true;
        this.needsAllocation = 0.0;
        this.wantsAllocation = 0.0;
        this.savingsAllocation = 0.0;
    } // End of constructor Profile


    // --- Accessors & Mutators (Basic Info) -----------------------------------

    /** @return the user's name */
    public String getName() { return name; }

    /** Updates the user's name. */
    public void setName(String name) { this.name = name; }

    /** @return the user's monthly income */
    public double getIncome() { return income; }

    /** Updates the user's income. */
    public void setIncome(double income) { this.income = income; }

    /** @return the user's current savings balance */
    public double getSavingsBalance() { return savingsBalance; }

    /** Updates the user's savings balance. */
    public void setSavingsBalance(double savingsBalance) { this.savingsBalance = savingsBalance; }


    // --- Needs Management ----------------------------------------------------

    /** Adds a Need item to the user's list and persists changes. */
    public void addNeed(Needs need) {
        needsList.add(need);
        saveProfile();
    } // End of method addNeed

    /** Removes a Need item and persists changes. */
    public void removeNeed(Needs need) {
        needsList.remove(need);
        saveProfile();
    } // End of method removeNeed

    /** @return the full list of the user's Needs */
    public List<Needs> getNeedsList() {
        return needsList;
    } // End of method getNeedsList


    // --- Wants Management ----------------------------------------------------

    /** Adds a Want item to the user's list and persists changes. */
    public void addWant(Wants want) {
        wantsList.add(want);
        saveProfile();
    } // End of method addWant

    /** Removes a Want item and persists changes. */
    public void removeWant(Wants want) {
        wantsList.remove(want);
        saveProfile();
    } // End of method removeWant

    /** @return the full list of the user's Wants */
    public List<Wants> getWantsList() {
        return wantsList;
    } // End of method getWantsList


    // --- Allocation Management ----------------------------------------------

    /**
     * Sets how the user's income is distributed among Needs, Wants, and Savings.
     *
     * @param needs         allocation amount or percentage for Needs
     * @param wants         allocation amount or percentage for Wants
     * @param savings       allocation amount or percentage for Savings
     * @param byPercentage  {@code true} to use percentages, {@code false} for fixed values
     */
    public void setAllocations(double needs, double wants, double savings, boolean byPercentage) {
        this.needsAllocation = needs;
        this.wantsAllocation = wants;
        this.savingsAllocation = savings;
        this.allocationByPercentage = byPercentage;
        saveProfile();
    } // End of method setAllocations

    /** @return the user's allocation for Needs */
    public double getNeedsAllocation() { return needsAllocation; }

    /** @return the user's allocation for Wants */
    public double getWantsAllocation() { return wantsAllocation; }

    /** @return the user's allocation for Savings */
    public double getSavingsAllocation() { return savingsAllocation; }

    /** @return whether allocations are set by percentage ({@code true}) or fixed amount ({@code false}) */
    public boolean isAllocationByPercentage() { return allocationByPercentage; }


    // --- Persistence (Save / Update) -----------------------------------------

    /**
     * Saves the current profile state to persistent storage.
     * <p>
     * This method automatically updates an existing profile in {@code profiles.dat}
     * or adds a new one if it doesn’t already exist.
     * </p>
     */
    public void saveProfile() {
        try {
            ProfileManager profileManager = new ProfileManager();
            List<Profile> profiles = profileManager.getProfiles();

            // 🔄 Update existing profile if one matches by name
            boolean updated = false;
            for (int i = 0; i < profiles.size(); i++) {
                if (profiles.get(i).getName().equalsIgnoreCase(this.name)) {
                    profiles.set(i, this);
                    updated = true;
                    break;
                }
            }

            // ➕ If profile does not exist, add it
            if (!updated) {
                profiles.add(this);
            }

            // 💾 Serialize updated list
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("profiles.dat"))) {
                oos.writeObject(profiles);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to save profile: " + e.getMessage());
            e.printStackTrace();
        }
    } // End of method saveProfile


    // --- Object Overrides ----------------------------------------------------

    /**
     * Returns a human-readable representation of the profile,
     * useful for display in dropdowns or debugging.
     *
     * @return a formatted string including name and income
     */
    @Override
    public String toString() {
        return name + " (Income: $" + String.format("%.2f", income) + ")";
    } // End of method toString

} // End of class Profile