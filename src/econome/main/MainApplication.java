package econome.main;

import javax.swing.SwingUtilities;
import econome.ui.SplashScreenUI;

/**
 * Entry point for the EconoMe application.
 * <p>
 * The {@code MainApplication} class initializes and launches the user interface.
 * It displays the {@link econome.ui.SplashScreenUI} first, allowing users to
 * create, select, or load their financial profiles before navigating to the
 * main dashboard.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Initialize the Swing UI system on the Event Dispatch Thread (EDT).</li>
 *   <li>Display the splash screen as the first window of the application.</li>
 * </ul>
 */
public class MainApplication {

    // --- Application Entry Point ---------------------------------------------

    /**
     * The main method of the EconoMe application.
     * <p>
     * Ensures all Swing components are initialized on the Event Dispatch Thread
     * (EDT) to maintain thread safety and prevent UI-related race conditions.
     * </p>
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Launch the splash screen asynchronously on the EDT
        SwingUtilities.invokeLater(SplashScreenUI::new);
    } // End of method main

} // End of class MainApplication