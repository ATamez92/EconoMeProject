package econome.model;

import java.time.LocalDate;

public class Needs {
	 private String description;
	    private double cost;
	    private LocalDate dueDate;
	    private boolean isComplete;

	    public Needs(String description, double cost, LocalDate dueDate) {
	        this.description = description;
	        this.cost = cost;
	        this.dueDate = dueDate;
	        this.isComplete = false; // default to incomplete
	    }

	    public String getDescription() { return description; }
	    public double getCost() { return cost; }
	    public LocalDate getDueDate() { return dueDate; }
	    public boolean isComplete() { return isComplete; }

	    public void markComplete() { this.isComplete = true; }
	}