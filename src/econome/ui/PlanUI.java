package econome.ui;

import javax.swing.*;
import java.awt.*;
import econome.model.Profile;

/**
 * Plan screen: Displays allocation donut chart, percentages, and progress bars
 * for Needs and Wants, with an option to manage allocations.
 */
public class PlanUI {

    private final Profile profile;
    private final SwingUI parent;

    public PlanUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    }

    private void buildScreen() {
        JDialog dialog = SharedUI.createBaseScreen("Plan", parent);

        // --- Scrollable content wrapper ---
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Donut Chart Section ---
        double[] allocations = {
            profile.getNeedsAllocation() / 100.0,
            profile.getWantsAllocation() / 100.0,
            profile.getSavingsAllocation() / 100.0
        };
        double[] progress = {0.68, 0.40, 0.90};
        Color[] colors = {
            new Color(56, 142, 60),   // Needs
            new Color(102, 187, 106), // Wants
            new Color(165, 214, 167)  // Savings
        };
        String[] labels = {"Needs", "Wants", "Savings"};

        DonutChartPanel chart = new DonutChartPanel(allocations, progress, colors, labels);
        chart.setBackground(UITheme.BACKGROUND);
        chart.setPreferredSize(new Dimension(280, 280));

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        chartPanel.add(chart, BorderLayout.CENTER);
        content.add(chartPanel);

        // --- Legend Section ---
        JPanel legendPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        for (int i = 0; i < labels.length; i++) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT));
            item.setOpaque(false);

            JLabel colorBox = new JLabel("■");
            colorBox.setForeground(colors[i]);
            colorBox.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JLabel label = new JLabel(labels[i] + ": " + (int) (allocations[i] * 100) + "% allocated");
            label.setFont(UITheme.BODY_FONT);

            item.add(colorBox);
            item.add(label);
            legendPanel.add(item);
        }
        content.add(legendPanel);

        // --- Progress Bars ---
        content.add(createRow("Needs", (int) Math.round(profile.getNeedsAllocation())));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(createRow("Wants", (int) Math.round(profile.getWantsAllocation())));

        // --- Manage Allocations Button ---
        JButton manageBtn = SharedUI.createRoundedButton("⚙️ Manage Allocations", new Color(102, 187, 106), Color.WHITE);
        manageBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        manageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageBtn.setPreferredSize(new Dimension(200, 45));
        manageBtn.addActionListener(e -> parent.showSettingsMenu());

        JPanel manageWrap = new JPanel(new BorderLayout());
        manageWrap.setOpaque(false);
        manageWrap.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        manageWrap.add(manageBtn, BorderLayout.CENTER);
        content.add(manageWrap);

        // ✅ Scroll Pane Wrapper
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        dialog.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Navigation Bar ---
        JPanel bottom = SharedUI.createBottomSection(
            dialog,
            null,
            new Runnable[]{
                () -> new HomeUI(profile, parent),
                () -> new TasksUI(profile, parent),
                () -> new PlanUI(profile, parent),
                () -> parent.showSettingsMenu()
            }
        );
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    private JPanel createRow(String label, int percent) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel name = new JLabel(label);
        name.setFont(UITheme.BODY_FONT);

        // Choose bar color based on label
        Color fillColor;
        switch (label.toLowerCase()) {
            case "needs" -> fillColor = new Color(56, 142, 60);
            case "wants" -> fillColor = new Color(102, 187, 106);
            default -> fillColor = new Color(165, 214, 167);
        }

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(Math.max(0, Math.min(100, percent)));
        bar.setStringPainted(true);
        bar.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));
        bar.setForeground(fillColor); // filled part
        bar.setBackground(new Color(220, 225, 220)); // empty track
        bar.setPreferredSize(new Dimension(250, 22));
        bar.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        // Change the percentage text color
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected Color getSelectionForeground() {
                return new Color(40, 40, 40); // % text color when inside fill
            }

            @Override
            protected Color getSelectionBackground() {
                return new Color(40, 40, 40); // % text color when outside fill
            }
        });

        row.add(name, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

}
