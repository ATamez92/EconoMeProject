package econome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user profile in the EconoMe application.
 * Stores personal financial information such as income, savings balance,
 * allocations for Needs/Wants/Savings, and lists of financial tasks.
 *
 * Responsibilities:
 * - Maintain user data (name, income, savings balance).
 * - Manage task lists for Needs and Wants.
 * - Store allocation preferences (percentage vs fixed amounts).
 */
public class Profile {
    private String name;
    private double income;
    private double savingsBalance;
    
    // Allocation amounts (interpreted as % or fixed amounts depending on mode)
    private double needsAllocation;    // can be % or fixed amount
    private double wantsAllocation;
    private double savingsAllocation;

    // true = allocation is percentage-based, false = fixed amount
    private boolean allocationByPercentage;

    // Lists of tasks (expenses/goals)
    private List<Needs> needsList; 
    private List<Wants> wantsList;

    /**
     * Constructs a Profile with basic financial details.
     * By default, allocations are not set (all = 0).
     *
     * @param name the user's name
     * @param income the user's monthly income
     * @param savingsBalance the current savings balance
     */
    public Profile(String name, double income, double savingsBalance) {
        this.name = name;
        this.income = income;
        this.savingsBalance = savingsBalance;
        this.needsList = new ArrayList<>();
        this.wantsList = new ArrayList<>();
        
        // Default: no allocations until user specifies
        this.allocationByPercentage = true; // default mode
        this.needsAllocation = 0.0;
        this.wantsAllocation = 0.0;
        this.savingsAllocation = 0.0;
    }

    // --- Getters and Setters ---
    
    /** @return the user's name */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getIncome() {
        return income;
    }
    public void setIncome(double income) {
        this.income = income;
    }

    public double getSavingsBalance() {
        return savingsBalance;
    }
    public void setSavingsBalance(double savingsBalance) {
        this.savingsBalance = savingsBalance;
    }

    // --- Needs methods ---
    
    /**
     * Adds a new Need item to the user's list.
     * @param need the Need task to add
     */
    public void addNeed(Needs need) {
        needsList.add(need);
    }
    
    /**
     * Removes a Need item from the user's list.
     * @param need the Need task to remove
     */
    public void removeNeed(Needs need) {
        needsList.remove(need);
    }
    
    /**
     * @return the list of Needs
     */
    public List<Needs> getNeedsList() {
        return needsList;
    }

    // --- Wants methods ---
    
    /**
     * Adds a new Want item to the user's list.
     * @param want the Want task to add
     */
    public void addWant(Wants want) {
        wantsList.add(want);
    }
    
    /**
     * Removes a Want item from the user's list.
     * @param want the Want task to remove
     */
    public void removeWant(Wants want) {
        wantsList.remove(want);
    }
    
    /**
     * @return the list of Wants
     */
    public List<Wants> getWantsList() {
        return wantsList;
    }
    
    // --- Allocation methods ---
    
    /**
     * Updates allocation settings for Needs, Wants, and Savings.
     *
     * @param needs the allocation value for Needs (percent or fixed)
     * @param wants the allocation value for Wants (percent or fixed)
     * @param savings the allocation value for Savings (percent or fixed)
     * @param byPercentage true if values are percentages, false if fixed amounts
     */
    public void setAllocations(double needs, double wants, double savings, boolean byPercentage) {
        this.needsAllocation = needs;
        this.wantsAllocation = wants;
        this.savingsAllocation = savings;
        this.allocationByPercentage = byPercentage;
    }

    public double getNeedsAllocation() { 
    	return needsAllocation;
    	}
    public double getWantsAllocation() { 
    	return wantsAllocation;
    	}
    public double getSavingsAllocation() { 
    	return savingsAllocation;
    	}
    public boolean isAllocationByPercentage() { 
    	return allocationByPercentage;
    	}
}