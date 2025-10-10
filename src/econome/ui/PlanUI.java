package econome.ui;

import javax.swing.*;
import java.awt.*;
import econome.model.Profile;

/**
 * Represents the "Plan" screen in the EconoMe application.
 * <p>
 * Displays the user's allocation plan as a donut chart and progress indicators
 * for Needs, Wants, and Savings. Provides a button for managing allocations
 * and integrates into the app’s shared navigation structure.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Visualize allocations via a donut chart and progress bars.</li>
 *   <li>Display allocation percentages for Needs, Wants, and Savings.</li>
 *   <li>Provide a navigation button for adjusting allocations.</li>
 * </ul>
 */
public class PlanUI {

    // --- Instance Variables ---
    private final Profile userProfile; // Active user profile
    private final SwingUI parentUI;    // Reference to parent application window

    /**
     * Constructs the Plan screen for the given user profile.
     *
     * @param profile the current user's profile
     * @param parent  reference to the main application window
     */
    public PlanUI(Profile profile, SwingUI parent) {
        this.userProfile = profile;
        this.parentUI = parent;
        buildScreen();
    } // End of constructor PlanUI

    /**
     * Builds and displays the full Plan screen layout.
     * Includes the allocation donut chart, percentage breakdown, and navigation bar.
     */
    private void buildScreen() {
        // --- Base Window Setup ---
        JDialog dialog = SharedUI.createBaseScreen("Plan", parentUI);

        // --- Scrollable Content Wrapper ---
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Donut Chart Section ---
        double[] allocations = {
                userProfile.getNeedsAllocation() / 100.0,
                userProfile.getWantsAllocation() / 100.0,
                userProfile.getSavingsAllocation() / 100.0
        };
        double[] progress = {0.68, 0.40, 0.90}; // Placeholder progress (visual only)

        Color[] colors = {
                new Color(56, 142, 60),   // Needs
                new Color(102, 187, 106), // Wants
                new Color(165, 214, 167)  // Savings
        };
        String[] labels = {"Needs", "Wants", "Savings"};

        DonutChartPanel donutChart = new DonutChartPanel(allocations, progress, colors, labels);
        donutChart.setBackground(UITheme.BACKGROUND);
        donutChart.setPreferredSize(new Dimension(280, 280));

        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setOpaque(false);
        chartWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        chartWrapper.add(donutChart, BorderLayout.CENTER);
        contentPanel.add(chartWrapper);

        // --- Legend Section ---
        JPanel legendPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        for (int i = 0; i < labels.length; i++) {
            JPanel legendItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
            legendItem.setOpaque(false);

            JLabel colorBox = new JLabel("■");
            colorBox.setForeground(colors[i]);
            colorBox.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JLabel label = new JLabel(labels[i] + ": " + (int) (allocations[i] * 100) + "% allocated");
            label.setFont(UITheme.BODY_FONT);

            legendItem.add(colorBox);
            legendItem.add(label);
            legendPanel.add(legendItem);
        }
        contentPanel.add(legendPanel);

        // --- Progress Bars Section ---
        contentPanel.add(createProgressRow("Needs", (int) Math.round(userProfile.getNeedsAllocation())));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(createProgressRow("Wants", (int) Math.round(userProfile.getWantsAllocation())));

        // --- Manage Allocations Button ---
        JButton manageButton = SharedUI.createRoundedButton(
                "⚙️ Manage Allocations",
                new Color(102, 187, 106),
                Color.WHITE
        );
        manageButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        manageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageButton.setPreferredSize(new Dimension(200, 45));
        manageButton.addActionListener(e -> parentUI.showSettingsMenu());

        JPanel manageWrapper = new JPanel(new BorderLayout());
        manageWrapper.setOpaque(false);
        manageWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        manageWrapper.add(manageButton, BorderLayout.CENTER);
        contentPanel.add(manageWrapper);

        // --- Scroll Pane Setup ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Navigation Bar ---
        JPanel bottomBar = SharedUI.createBottomSection(
                dialog,
                null,
                new Runnable[]{
                        () -> new HomeUI(userProfile, parentUI),
                        () -> new TasksUI(userProfile, parentUI),
                        () -> new PlanUI(userProfile, parentUI),
                        () -> parentUI.showSettingsMenu()
                }
        );
        dialog.add(bottomBar, BorderLayout.SOUTH);

        dialog.setVisible(true);
    } // End of method buildScreen

    /**
     * Creates a labeled row with a progress bar representing allocation percentage.
     *
     * @param label   the label for the row (e.g., "Needs", "Wants")
     * @param percent the percentage value (0–100)
     * @return a {@link JPanel} representing the labeled progress row
     */
    private JPanel createProgressRow(String label, int percent) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(UITheme.BODY_FONT);

        // Choose progress bar color based on label
        Color fillColor;
        switch (label.toLowerCase()) {
            case "needs" -> fillColor = new Color(56, 142, 60);
            case "wants" -> fillColor = new Color(102, 187, 106);
            default -> fillColor = new Color(165, 214, 167);
        }

        // Progress bar setup
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(Math.max(0, Math.min(100, percent)));
        progressBar.setStringPainted(true);
        progressBar.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));
        progressBar.setForeground(fillColor);
        progressBar.setBackground(new Color(220, 225, 220));
        progressBar.setPreferredSize(new Dimension(250, 22));
        progressBar.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        // Custom text color inside/outside the bar
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected Color getSelectionForeground() {
                return new Color(40, 40, 40);
            }

            @Override
            protected Color getSelectionBackground() {
                return new Color(40, 40, 40);
            }
        });

        row.add(nameLabel, BorderLayout.WEST);
        row.add(progressBar, BorderLayout.CENTER);
        return row;
    } // End of method createProgressRow
} // End of class PlanUI