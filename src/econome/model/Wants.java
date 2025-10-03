package econome.model;

import java.time.LocalDate;

public class Wants {
    private String description;
    private double cost;
    private LocalDate dueDate;
    private boolean isComplete;

    public Wants(String description, double cost, LocalDate dueDate) {
        this.description = description;
        this.cost = cost;
        this.dueDate = dueDate;
        this.isComplete = false; // default to incomplete
    }

    // --- Getters ---
    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isComplete() { return isComplete; }

    // --- Setters (if needed) ---
    public void setDescription(String description) { this.description = description; }
    public void setCost(double cost) { this.cost = cost; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // --- Actions ---
    public void markComplete() {
        this.isComplete = true;
    }
}