package econome.ui;

import econome.logic.ProfileManager;
import econome.model.Profile;
import javax.swing.*;
import java.awt.*;

/**
 * Represents the splash (entry) screen for the EconoMe application.
 * <p>
 * Allows users to:
 * <ul>
 *   <li>Select and load an existing financial profile.</li>
 *   <li>Create a new profile.</li>
 *   <li>Delete an existing profile.</li>
 * </ul>
 * This is the first screen shown when launching the application.
 * </p>
 */
public class SplashScreenUI extends JFrame {
    private static final long serialVersionUID = 1L;

    // --- Instance Variables ---
    private final ProfileManager profileManager;
    private JComboBox<Profile> profileDropdown;
    private JButton deleteButton;

    /**
     * Constructs and displays the splash screen for EconoMe.
     * Initializes the UI and loads saved profiles from disk.
     */
    public SplashScreenUI() {
        super("Welcome to EconoMe");
        profileManager = new ProfileManager();

        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        buildHeaderSection();
        buildCenterSection();
        buildFooterSection();

        // --- Window Setup ---
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    } // End of constructor SplashScreenUI


    // -------------------------------------------------------------------------
    // UI BUILD SECTIONS
    // -------------------------------------------------------------------------

    /**
     * Builds the top header with title and subtitle.
     */
    private void buildHeaderSection() {
        JLabel titleLabel = new JLabel("💸 Welcome to EconoMe", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        JLabel subtitleLabel = new JLabel("Manage your needs, wants, and savings", SwingConstants.CENTER);
        subtitleLabel.setFont(UITheme.SUBTITLE_FONT);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);
    } // End of method buildHeaderSection


    /**
     * Builds the main content area of the splash screen.
     * Displays either a “Create Profile” prompt or profile selection options.
     */
    private void buildCenterSection() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        if (profileManager.getProfiles().isEmpty()) {
            // No profiles yet — only show Create Profile button
            JButton createButton = SharedUI.createRoundedButton("➕ Create Profile",
                    new Color(102, 187, 106), Color.WHITE);
            createButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
            createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            createButton.addActionListener(e -> createProfile());

            Dimension buttonSize = new Dimension(220, 45);
            createButton.setPreferredSize(buttonSize);
            createButton.setMaximumSize(buttonSize);

            centerPanel.add(Box.createVerticalGlue());
            centerPanel.add(createButton);
            centerPanel.add(Box.createVerticalGlue());

        } else {
            // Existing profiles found — show dropdown and controls
            JLabel chooseLabel = new JLabel("Select a profile:");
            chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 14f));
            chooseLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            profileDropdown = new JComboBox<>(profileManager.getProfiles().toArray(new Profile[0]));
            profileDropdown.setFont(UITheme.BODY_FONT);
            profileDropdown.setForeground(Color.DARK_GRAY);
            profileDropdown.setBackground(Color.WHITE);
            profileDropdown.setFocusable(false);
            profileDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
            profileDropdown.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            // Custom rounded dropdown style
            profileDropdown.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 20, 20);
                    g2.dispose();
                }
            });

            Dimension buttonSize = new Dimension(220, 45);
            profileDropdown.setPreferredSize(buttonSize);
            profileDropdown.setMaximumSize(buttonSize);

            // Action buttons
            JButton loadButton = SharedUI.createRoundedButton("▶ Load Profile",
                    new Color(102, 187, 106), Color.WHITE);
            deleteButton = SharedUI.createRoundedButton("🗑️ Delete Profile",
                    new Color(200, 80, 80), Color.WHITE);
            JButton newButton = SharedUI.createRoundedButton("➕ Create New Profile",
                    new Color(102, 187, 106), Color.WHITE);

            for (JButton button : new JButton[]{loadButton, deleteButton, newButton}) {
                button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setPreferredSize(buttonSize);
                button.setMaximumSize(buttonSize);
            }

            deleteButton.setEnabled(false);
            profileDropdown.addActionListener(e ->
                    deleteButton.setEnabled(profileDropdown.getSelectedItem() != null));

            // Button listeners
            loadButton.addActionListener(e -> loadSelectedProfile());
            deleteButton.addActionListener(e -> deleteSelectedProfile());
            newButton.addActionListener(e -> createProfile());

            // Assemble UI vertically
            centerPanel.add(chooseLabel);
            centerPanel.add(profileDropdown);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            centerPanel.add(loadButton);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            centerPanel.add(deleteButton);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            centerPanel.add(newButton);
        }

        add(centerPanel, BorderLayout.CENTER);
    } // End of method buildCenterSection


    /**
     * Builds the footer with copyright text.
     */
    private void buildFooterSection() {
        JLabel footerLabel = new JLabel("© 2025 EconoMe", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footerLabel, BorderLayout.SOUTH);
    } // End of method buildFooterSection


    // -------------------------------------------------------------------------
    // PROFILE MANAGEMENT ACTIONS
    // -------------------------------------------------------------------------

    /**
     * Prompts the user to create a new financial profile.
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
            refreshSplash();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for income.");
        }
    } // End of method createProfile


    /**
     * Deletes the currently selected profile after user confirmation.
     */
    private void deleteSelectedProfile() {
        Profile selectedProfile = (Profile) profileDropdown.getSelectedItem();
        if (selectedProfile == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the profile \"" + selectedProfile.getName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            profileManager.deleteProfile(selectedProfile);
            JOptionPane.showMessageDialog(this,
                    "Profile \"" + selectedProfile.getName() + "\" has been deleted.");

            if (profileManager.getProfiles().isEmpty()) {
                dispose();
                new SplashScreenUI(); // Show Create-only screen
            } else {
                profileDropdown.removeItem(selectedProfile);
            }
        }
    } // End of method deleteSelectedProfile


    /**
     * Loads the selected profile and opens the main dashboard.
     */
    private void loadSelectedProfile() {
        Profile selectedProfile = (Profile) profileDropdown.getSelectedItem();
        if (selectedProfile == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile first.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Loading profile: " + selectedProfile.getName());
        openDashboard(selectedProfile);
    } // End of method loadSelectedProfile


    /**
     * Opens the main EconoMe dashboard for the specified profile.
     *
     * @param profile the profile to load
     */
    private void openDashboard(Profile profile) {
        dispose();
        SwingUI mainUI = new SwingUI(profile);
        mainUI.setVisible(true);
    } // End of method openDashboard


    /**
     * Refreshes the splash screen after profile creation or deletion.
     */
    private void refreshSplash() {
        dispose();
        new SplashScreenUI();
    } // End of method refreshSplash


    // -------------------------------------------------------------------------
    // APPLICATION ENTRY POINT
    // -------------------------------------------------------------------------

    /**
     * Launches the EconoMe splash screen.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreenUI::new);
    } // End of method main
} // End of class SplashScreenUI