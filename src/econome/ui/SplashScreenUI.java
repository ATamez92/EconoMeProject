package econome.ui;

import econome.logic.ProfileManager;
import econome.model.Profile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Entry screen for EconoMe.
 * Allows users to select, create, load, or delete a saved profile.
 */
public class SplashScreenUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ProfileManager profileManager;
    private JComboBox<Profile> profileDropdown;
    private JButton deleteButton;

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
            // No profiles yet — show "Create Profile" button only
            JButton createButton = SharedUI.createRoundedButton("➕ Create Profile",
                    new Color(102, 187, 106), Color.WHITE);
            createButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
            createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            createButton.addActionListener(e -> createProfile());

            Dimension buttonSize = new Dimension(220, 45);
            createButton.setPreferredSize(buttonSize);
            createButton.setMaximumSize(buttonSize);
            createButton.setMinimumSize(buttonSize);

            centerPanel.add(Box.createVerticalGlue());
            centerPanel.add(createButton);
            centerPanel.add(Box.createVerticalGlue());

        } else {
            // Profiles exist — show dropdown and action buttons
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

            // ✅ Modern rounded dropdown style (Option 2)
            profileDropdown.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);
                    g2.setColor(new Color(200, 200, 200)); // soft gray border
                    g2.drawRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 20, 20);
                    g2.dispose();
                }
            });

            // Match dropdown width with buttons
            Dimension buttonSize = new Dimension(220, 45);
            profileDropdown.setMaximumSize(buttonSize);
            profileDropdown.setPreferredSize(buttonSize);
            profileDropdown.setMinimumSize(buttonSize);

            // Create buttons
            JButton loadButton = SharedUI.createRoundedButton("▶ Load Profile",
                    new Color(102, 187, 106), Color.WHITE);
            JButton deleteButton = SharedUI.createRoundedButton("🗑️ Delete Profile",
                    new Color(200, 80, 80), Color.WHITE);
            JButton newButton = SharedUI.createRoundedButton("➕ Create New Profile",
                    new Color(102, 187, 106), Color.WHITE);

            for (JButton btn : new JButton[]{loadButton, deleteButton, newButton}) {
                btn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setPreferredSize(buttonSize);
                btn.setMaximumSize(buttonSize);
                btn.setMinimumSize(buttonSize);
            }

            deleteButton.setEnabled(false); // only active when a profile is selected

            // Enable delete when selection changes
            profileDropdown.addActionListener(e -> deleteButton.setEnabled(profileDropdown.getSelectedItem() != null));

            loadButton.addActionListener(e -> loadSelectedProfile());
            deleteButton.addActionListener(e -> deleteSelectedProfile());
            newButton.addActionListener(e -> createProfile());

            // Assemble vertically
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

        // --- Footer ---
        JLabel footer = new JLabel("© 2025 EconoMe", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        // --- Window Setup ---
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // === Profile Management Actions ===

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
    }

    /**
     * Deletes the currently selected profile after user confirmation.
     */
    private void deleteSelectedProfile() {
        Profile selected = (Profile) profileDropdown.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the profile \"" + selected.getName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            profileManager.deleteProfile(selected);
            JOptionPane.showMessageDialog(this,
                    "Profile \"" + selected.getName() + "\" has been deleted.");

            // Refresh dropdown or recreate UI depending on remaining profiles
            if (profileManager.getProfiles().isEmpty()) {
                dispose();
                new SplashScreenUI(); // rebuild UI to show "Create Profile" only
            } else {
                profileDropdown.removeItem(selected);
            }
        }
    }


    private void loadSelectedProfile() {
        Profile selected = (Profile) profileDropdown.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile first.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Loading profile: " + selected.getName());
        openDashboard(selected);
    }

    private void openDashboard(Profile profile) {
        dispose();
        SwingUI mainUI = new SwingUI(profile);
        mainUI.setVisible(true);
    }

    private void refreshSplash() {
        dispose();
        new SplashScreenUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreenUI::new);
    }
}
