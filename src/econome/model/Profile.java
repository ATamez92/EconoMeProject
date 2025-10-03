package econome.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String name;
    private double income;
    private double savingsBalance;
    
    private double needsAllocation;    // can be % or fixed amount
    private double wantsAllocation;
    private double savingsAllocation;

    private boolean allocationByPercentage; // true = %, false = fixed amount

    private List<Needs> needsList; 
    private List<Wants> wantsList;

    // Constructor
    public Profile(String name, double income, double savingsBalance) {
        this.name = name;
        this.income = income;
        this.savingsBalance = savingsBalance;
        this.needsList = new ArrayList<>();
        this.wantsList = new ArrayList<>();
        
     // Default: no allocations
        this.allocationByPercentage = true; // default mode
        this.needsAllocation = 0.0;
        this.wantsAllocation = 0.0;
        this.savingsAllocation = 0.0;
    }

    // --- Getters and Setters ---
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
    public void addNeed(Needs need) {
        needsList.add(need);
    }
    public void removeNeed(Needs need) {
        needsList.remove(need);
    }
    public List<Needs> getNeedsList() {
        return needsList;
    }

    // --- Wants methods ---
    public void addWant(Wants want) {
        wantsList.add(want);
    }
    public void removeWant(Wants want) {
        wantsList.remove(want);
    }
    public List<Wants> getWantsList() {
        return wantsList;
    }
    
    // --- Allocation methods ---
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