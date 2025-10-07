package econome.ui;

import econome.logic.ProfileManager;
import econome.model.Profile;
import econome.ui.UITheme;

import javax.swing.*;
import java.awt.*;

/**
 * Entry screen for EconoMe.
 * Allows users to select, create, or load a saved profile.
 */
public class SplashScreenUI extends JFrame {
	private static final long serialVersionUID = 1L; // added to prevent serialization warning
	
    private final ProfileManager profileManager;
    private JComboBox<Profile> profileDropdown;

    public SplashScreenUI() {
        super("Welcome to EconoMe");
        profileManager = new ProfileManager();

        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        // --- Header ---
        JLabel title = new JLabel("💸 Welcome to EconoMe", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        JLabel subtitle = new JLabel("Manage your needs, wants, and savings", SwingConstants.CENTER);
        subtitle.setFont(UITheme.SUBTITLE_FONT);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- Center Panel ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        if (profileManager.getProfiles().isEmpty()) {
            // No profiles yet — show "Create Profile" button
            JButton createButton = createStyledButton("Create Profile", new Color(67, 160, 71)); // soft green
            createButton.addActionListener(e -> createProfile());
            centerPanel.add(Box.createVerticalGlue());
            centerPanel.add(createButton);
            centerPanel.add(Box.createVerticalGlue());
        } else {
            // Profiles exist — show dropdown and actions
            JLabel chooseLabel = new JLabel("Select a profile:");
            chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            chooseLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            profileDropdown = new JComboBox<>(profileManager.getProfiles().toArray(new Profile[0]));
            profileDropdown.setMaximumSize(new Dimension(300, 35));

            JButton loadButton = createStyledButton("Load Profile", new Color(67, 160, 71));
            loadButton.addActionListener(e -> loadSelectedProfile());

            JButton newButton = createStyledButton("Create New Profile", new Color(120, 200, 120));
            newButton.addActionListener(e -> createProfile());

            centerPanel.add(chooseLabel);
            centerPanel.add(profileDropdown);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            centerPanel.add(loadButton);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            centerPanel.add(newButton);
        }

        add(centerPanel, BorderLayout.CENTER);

        // --- Footer ---
        JLabel footer = new JLabel("© 2025 EconoMe", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

     // --- Window Setup (mobile style) ---
        setSize(360, 640);          // typical phone portrait size
        setResizable(false);        // lock window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Handles profile creation flow.
     */
    private void createProfile() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name == null || name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Profile name cannot be empty.");
            return;
        }

        String incomeStr = JOptionPane.showInputDialog(this, "Enter your monthly income:");
        try {
            double income = Double.parseDouble(incomeStr);
            Profile newProfile = new Profile(name, income, 0.0);
            profileManager.addProfile(newProfile);

            JOptionPane.showMessageDialog(this, "Profile created for " + name + ".");
            openDashboard(newProfile);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for income.");
        }
    }

    /**
     * Loads the selected profile and opens the main dashboard.
     */
    private void loadSelectedProfile() {
        Profile selected = (Profile) profileDropdown.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile first.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Loading profile: " + selected.getName());
        openDashboard(selected);
    }

    /**
     * Launches the main dashboard window.
     */
    private void openDashboard(Profile profile) {
        dispose(); // close splash
        SwingUI mainUI = new SwingUI(profile); // Pass profile here
        mainUI.setVisible(true);
    }

    /**
     * Creates a uniformly styled JButton.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreenUI::new);
    }
}
