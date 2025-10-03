package econome.main;

import econome.model.Profile;
import econome.logic.BudgetManager;


import java.util.Scanner;

public class MainApplication {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to EconoMe!");
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your monthly income: ");
        double income = sc.nextDouble();

        Profile profile = new Profile(name, income);
        BudgetManager manager = new BudgetManager();

        manager.calculateBudget(profile);
        manager.printBudget();

        sc.close();
    }
}