package econome.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String name;
    private double income;
    private double savingsBalance;

    private List<Needs> needsList; 
    private List<Wants> wantsList;

    // Constructor
    public Profile(String name, double income, double savingsBalance) {
        this.name = name;
        this.income = income;
        this.savingsBalance = savingsBalance;
        this.needsList = new ArrayList<>();
        this.wantsList = new ArrayList<>();
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
}