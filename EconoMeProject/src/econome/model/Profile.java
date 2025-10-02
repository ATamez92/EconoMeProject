package econome.model;

public class Profile {
    private String name;
    private double income;

    public Profile(String name, double income) {
        this.name = name;
        this.income = income;
    }

    public String getName() { return name; }
    public double getIncome() { return income; }
}