package econome.ui;

import javax.swing.*;
import java.awt.*;
import econome.model.Profile;

/**
 * Home screen for EconoMe.
 * Displays total balance, recent activity, and quick access to Needs/Wants.
 */
public class HomeUI {

    private final Profile profile;
    private final SwingUI parent;

    public HomeUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    }

    private void buildScreen() {
        // 🪟 Base window styled like mobile screen
        JDialog dialog = SharedUI.createBaseScreen("Home", parent);
        dialog.setLayout(new BorderLayout());

        // --- Scrollable content (Balance + Recent Activity) ---
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
                "$" + String.format("%.2f", profile.getSavingsBalance()), SwingConstants.CENTER);
        balanceAmount.setFont(UITheme.TITLE_FONT.deriveFont(Font.BOLD, 26f));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceCard.add(balanceLabel);
        balanceCard.add(Box.createRigidArea(new Dimension(0, 5)));
        balanceCard.add(balanceAmount);

        scrollContent.add(balanceCard);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Recent Activity ---
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

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        dialog.add(scrollPane, BorderLayout.CENTER);

     // --- Fixed Needs/Wants Button Bar ---
        JPanel quickAccessOuter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        quickAccessOuter.setOpaque(false);
        quickAccessOuter.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20));

        Color lightGreen = new Color(102, 187, 106);
        Dimension bigBtn = new Dimension(150, 48);

        JButton needsBtn = SharedUI.createRoundedButton("🛒 Needs", lightGreen, Color.WHITE);
        needsBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        needsBtn.setPreferredSize(bigBtn);
        needsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        needsBtn.addActionListener(e -> {
            dialog.dispose();
            new NeedsUI(profile, parent);
        });

        JButton wantsBtn = SharedUI.createRoundedButton("🎯 Wants", lightGreen, Color.WHITE);
        wantsBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        wantsBtn.setPreferredSize(bigBtn);
        wantsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wantsBtn.addActionListener(e -> {
            dialog.dispose();
            new WantsUI(profile, parent);
        });

        // ✅ Inner grid keeps them side-by-side (never stacks)
        JPanel quickAccessGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        quickAccessGrid.setOpaque(false);
        quickAccessGrid.add(needsBtn);
        quickAccessGrid.add(wantsBtn);

        quickAccessOuter.add(quickAccessGrid);

        // Wrap quick access above nav bar
        JPanel lowerWrap = new JPanel(new BorderLayout());
        lowerWrap.setOpaque(false);
        lowerWrap.add(quickAccessOuter, BorderLayout.NORTH);

        // --- Bottom Navigation Bar ---
        JPanel navBar = SharedUI.createBottomSection(
                dialog,
                null,
                new Runnable[]{
                        () -> new HomeUI(profile, parent),
                        () -> new TasksUI(profile, parent),
                        () -> new PlanUI(profile, parent),
                        () -> parent.showSettingsMenu()
                }
        );
        lowerWrap.add(navBar, BorderLayout.SOUTH);

        dialog.add(lowerWrap, BorderLayout.SOUTH);


        dialog.setVisible(true);
    }
}
