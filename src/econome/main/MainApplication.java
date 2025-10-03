package econome.main;

import econome.ui.ConsoleUI;

/**
 * Main entry point for the EconoMe application.
 * 
 * Responsibilities:
 * - Launches the console-based user interface.
 * - Serves as the starting class when the program is executed.
 */
public class MainApplication {

    /**
     * Program entry point.
     * Initializes the ConsoleUI and starts the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}