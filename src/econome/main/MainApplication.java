package econome.main;

import javax.swing.SwingUtilities;
import econome.ui.SplashScreenUI;

/**
 * Main entry point for the EconoMe application.
 * 
 * Launches the splash screen (profile selector / creator),
 * which then loads the main dashboard when a profile is chosen.
 */
public class MainApplication {

    public static void main(String[] args) {
        // Launch the splash screen on the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> new SplashScreenUI());
    }
}