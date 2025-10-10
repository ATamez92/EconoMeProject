package econome.ui;

import javax.swing.*;
import java.awt.*;
import econome.model.Profile;

/**
 * Represents the Home screen of the EconoMe application.
 * <p>
 * Displays key user financial information such as total balance
 * and recent activity, and provides quick access to the Needs and Wants pages.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Display the user’s total balance.</li>
 *   <li>List recent activity (currently placeholder text).</li>
 *   <li>Provide quick navigation to Needs and Wants screens.</li>
 *   <li>Include bottom navigation for all app sections.</li>
 * </ul>
 */
public class HomeUI {

    // --- Instance Variables ---
    private final Profile userProfile;  // Active user profile
    private final SwingUI parentUI;     // Reference to parent Swing container

    /**
     * Constructs the Home screen for a given profile.
     *
     * @param profile the current user’s financial profile
     * @param parent  reference to the parent UI for navigation
     */
    public HomeUI(Profile profile, SwingUI parent) {
        this.userProfile = profile;
        this.parentUI = parent;
        buildScreen();
    } // End of constructor HomeUI

    /**
     * Builds and displays the Home screen layout.
     * Includes total balance, recent activity, and quick access buttons.
     */
    private void buildScreen() {
        // --- Base window styled like a mobile screen ---
        JDialog dialog = SharedUI.createBaseScreen("Home", parentUI);
        dialog.setLayout(new BorderLayout());

        // --- Scrollable Content Area ---
        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- Balance Card ---
        JPanel balanceCard = SharedUI.createRoundedPanel(UITheme.PRIMARY, 25);
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel balanceLabel = new JLabel("Total Balance", SwingConstants.CENTER);
        balanceLabel.setFont(UITheme.SUBTITLE_FONT);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceAmount = new JLabel(
                "$" + String.format("%.2f", userProfile.getSavingsBalance()), SwingConstants.CENTER);
        balanceAmount.setFont(UITheme.TITLE_FONT.deriveFont(Font.BOLD, 26f));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceCard.add(balanceLabel);
        balanceCard.add(Box.createRigidArea(new Dimension(0, 5)));
        balanceCard.add(balanceAmount);

        scrollContent.add(balanceCard);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Recent Activity Panel ---
        JPanel recentPanel = new JPanel();
        recentPanel.setOpaque(false);
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel recentLabel = new JLabel("Recent Activity", SwingConstants.CENTER);
        recentLabel.setFont(UITheme.SUBTITLE_FONT);
        recentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noActivity = new JLabel("(No activity yet)", SwingConstants.CENTER);
        noActivity.setFont(UITheme.BODY_FONT);
        noActivity.setAlignmentX(Component.CENTER_ALIGNMENT);

        recentPanel.add(recentLabel);
        recentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        recentPanel.add(noActivity);

        scrollContent.add(recentPanel);

        // --- Scroll Pane Wrapper ---
        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // --- Quick Access Button Bar (Needs / Wants) ---
        JPanel quickAccessOuter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        quickAccessOuter.setOpaque(false);
        quickAccessOuter.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20));

        Color lightGreen = new Color(102, 187, 106);
        Dimension bigButtonSize = new Dimension(150, 48);

        // Needs button
        JButton needsButton = SharedUI.createRoundedButton("🛒 Needs", lightGreen, Color.WHITE);
        needsButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        needsButton.setPreferredSize(bigButtonSize);
        needsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        needsButton.addActionListener(e -> {
            dialog.dispose();
            new NeedsUI(userProfile, parentUI);
        });

        // Wants button
        JButton wantsButton = SharedUI.createRoundedButton("🎯 Wants", lightGreen, Color.WHITE);
        wantsButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        wantsButton.setPreferredSize(bigButtonSize);
        wantsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wantsButton.addActionListener(e -> {
            dialog.dispose();
            new WantsUI(userProfile, parentUI);
        });

        // Keep buttons side by side
        JPanel quickAccessGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        quickAccessGrid.setOpaque(false);
        quickAccessGrid.add(needsButton);
        quickAccessGrid.add(wantsButton);

        quickAccessOuter.add(quickAccessGrid);

        // --- Lower Wrapper (Quick Access + Navigation Bar) ---
        JPanel lowerWrap = new JPanel(new BorderLayout());
        lowerWrap.setOpaque(false);
        lowerWrap.add(quickAccessOuter, BorderLayout.NORTH);

        // --- Bottom Navigation Bar ---
        JPanel navBar = SharedUI.createBottomSection(
                dialog,
                null,
                new Runnable[]{
                        () -> new HomeUI(userProfile, parentUI),
                        () -> new TasksUI(userProfile, parentUI),
                        () -> new PlanUI(userProfile, parentUI),
                        () -> parentUI.showSettingsMenu()
                }
        );
        lowerWrap.add(navBar, BorderLayout.SOUTH);

        dialog.add(lowerWrap, BorderLayout.SOUTH);
        dialog.setVisible(true);
    } // End of method buildScreen
} // End of class HomeUI