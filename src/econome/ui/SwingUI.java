package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import econome.model.Wants;
import java.time.LocalDate;
import econome.model.Needs;
import econome.logic.BudgetManager;
import econome.model.Profile;
import econome.ui.UITheme;

/**
 * Main dashboard for the EconoMe application.
 * Displays the user's overview and key navigation buttons.
 */
public class SwingUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private final BudgetManager budgetManager;
    private final Profile profile;

    // --- Buttons ---
    private final JButton needsButton = new JButton("Needs");
    private final JButton wantsButton = new JButton("Wants");
    private final JButton savingsButton = new JButton("Savings");
    private final JButton settingsButton = new JButton("Settings");

    /**
     * Constructs the EconoMe dashboard for a specific profile.
     *
     * @param profile The Profile selected or created in SplashScreenUI.
     */
    public SwingUI(Profile profile) {
        super("EconoMe Dashboard");
        this.profile = profile;
        this.budgetManager = new BudgetManager();

        // --- Window Setup (mobile style) ---
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        // --- Header ---
        JLabel greetingLabel = new JLabel("Welcome back, " + profile.getName() + "!", SwingConstants.CENTER);
        greetingLabel.setFont(UITheme.TITLE_FONT);
        greetingLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(greetingLabel, BorderLayout.NORTH);

        // --- Main Content Panel ---
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- Balance Card ---
        JPanel balanceCard = createRoundedPanel(UITheme.PRIMARY, 25);
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel balanceLabel = new JLabel("Total Balance", SwingConstants.CENTER);
        balanceLabel.setFont(UITheme.SUBTITLE_FONT);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceAmount = new JLabel("$" + String.format("%.2f", profile.getSavingsBalance()), SwingConstants.CENTER);
        balanceAmount.setFont(UITheme.TITLE_FONT.deriveFont(Font.BOLD, 26f));
        balanceAmount.setForeground(Color.WHITE);
        balanceAmount.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceCard.add(balanceLabel);
        balanceCard.add(Box.createRigidArea(new Dimension(0, 5)));
        balanceCard.add(balanceAmount);

        mainPanel.add(balanceCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Recent Activity Section ---
        JPanel recentPanel = new JPanel();
        recentPanel.setOpaque(false);
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JLabel recentLabel = new JLabel("Recent Activity", SwingConstants.CENTER);
        recentLabel.setFont(UITheme.SUBTITLE_FONT);
        recentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noActivity = new JLabel("(No activity yet)", SwingConstants.CENTER);
        noActivity.setFont(UITheme.BODY_FONT);
        noActivity.setAlignmentX(Component.CENTER_ALIGNMENT);

        recentPanel.add(recentLabel);
        recentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        recentPanel.add(noActivity);

        mainPanel.add(recentPanel);
        add(mainPanel, BorderLayout.CENTER);

        // --- 📱 Bottom Navigation Bar ---
        JPanel navBar = new JPanel(new GridLayout(1, 4, 10, 0));
        navBar.setBackground(UITheme.PRIMARY);
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JButton needsNav = createNavButton("Needs", "🛒");
        JButton wantsNav = createNavButton("Wants", "🎯");
        JButton savingsNav = createNavButton("Savings", "💰");
        JButton settingsNav = createNavButton("Settings", "⚙️");

        navBar.add(needsNav);
        navBar.add(wantsNav);
        navBar.add(savingsNav);
        navBar.add(settingsNav);

        add(navBar, BorderLayout.SOUTH);

        // --- Navigation Actions ---
        needsNav.addActionListener(e -> showNeedsMenu());
        wantsNav.addActionListener(e -> showWantsMenu());
        savingsNav.addActionListener(e -> showSavingsMenu());
        settingsNav.addActionListener(e -> showSettingsMenu());

        setVisible(true);
    }



    /**
     * Helper: creates a rounded corner panel (used for balance card).
     */
    private JPanel createRoundedPanel(Color bgColor, int cornerRadius) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            }
        };
    }

    /**
     * Helper: applies consistent styling to main dashboard buttons.
     */
    private void styleMainButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Helper: creates a navigation button with emoji and text.
     * @param text
     * @param emoji
     * @return
     */
    private JButton createNavButton(String text, String emoji) {
        JButton button = new JButton(emoji + " " + text);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));
        button.setBackground(UITheme.PRIMARY_LIGHT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Creates a shared bottom navigation bar with 4 buttons:
     * Needs, Wants, Savings, and Settings.
     * This ensures consistent appearance and navigation behavior across screens.
     *
     * @param dialog the current dialog window (will be disposed when navigating)
     * @return JPanel containing the full navigation bar
     */
    private JPanel createNavigationBar(JDialog dialog) {
        JPanel navBar = new JPanel(new GridLayout(1, 4, 10, 0));
        navBar.setBackground(UITheme.PRIMARY);
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JButton needsNav = createNavButton("Needs", "🛒");
        JButton wantsNav = createNavButton("Wants", "🎯");
        JButton savingsNav = createNavButton("Savings", "💰");
        JButton settingsNav = createNavButton("Settings", "⚙️");

        navBar.add(needsNav);
        navBar.add(wantsNav);
        navBar.add(savingsNav);
        navBar.add(settingsNav);

        // --- Navigation actions ---
        needsNav.addActionListener(e -> { dialog.dispose(); showNeedsMenu(); });
        wantsNav.addActionListener(e -> { dialog.dispose(); showWantsMenu(); });
        savingsNav.addActionListener(e -> { dialog.dispose(); showSavingsMenu(); });
        settingsNav.addActionListener(e -> { dialog.dispose(); showSettingsMenu(); });

        return navBar;
    }

    /**
     * Handles main button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == needsButton) {
            showNeedsMenu();
        } else if (src == wantsButton) {
            JOptionPane.showMessageDialog(this, "Wants section coming soon!");
        } else if (src == savingsButton) {
            JOptionPane.showMessageDialog(this, "Savings section coming soon!");
        } else if (src == settingsButton) {
            JOptionPane.showMessageDialog(this, "Settings coming soon!");
        }
    }

    private void showAddNeedDialog(Runnable refreshAction) {
        // Create modal dialog
        JDialog dialog = new JDialog(this, "Add Need", true);
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);
        dialog.setLayout(new BorderLayout(10, 10));

        // 📱 Mobile-style size and layout
        dialog.setSize(360, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        // --- Header ---
        JLabel header = new JLabel("Add a New Need", SwingConstants.CENTER);
        header.setFont(UITheme.TITLE_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(header, BorderLayout.NORTH);

        // --- Input Fields Panel ---
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Description Field
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(UITheme.BODY_FONT);
        JTextField descField = new JTextField();
        descField.setFont(UITheme.BODY_FONT);
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Cost Field
        JLabel costLabel = new JLabel("Cost ($):");
        costLabel.setFont(UITheme.BODY_FONT);
        JTextField costField = new JTextField();
        costField.setFont(UITheme.BODY_FONT);
        costField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Due Date Field
        JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD):");
        dateLabel.setFont(UITheme.BODY_FONT);
        JTextField dateField = new JTextField();
        dateField.setFont(UITheme.BODY_FONT);
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Add to panel
        inputPanel.add(descLabel);
        inputPanel.add(descField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(costLabel);
        inputPanel.add(costField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        dialog.add(inputPanel, BorderLayout.CENTER);

        // --- Bottom Buttons ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UITheme.BODY_FONT);
        cancelButton.setBackground(UITheme.ACCENT);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        JButton saveButton = new JButton("Save Need");
        saveButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 15f));
        saveButton.setBackground(UITheme.PRIMARY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // --- Actions ---
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String desc = descField.getText().trim();
            String costText = costField.getText().trim();
            String dateText = dateField.getText().trim();

            if (desc.isEmpty() || costText.isEmpty() || dateText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                double cost = Double.parseDouble(costText);
                java.time.LocalDate dueDate = java.time.LocalDate.parse(dateText);

                Needs need = new Needs(desc, cost, dueDate);
                profile.addNeed(need);

                JOptionPane.showMessageDialog(dialog, "Need added successfully!");
                refreshAction.run();
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for cost.");
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.");
            }
        });

        dialog.setVisible(true);
    }
    
    private void showAddWantDialog(Runnable refreshAction) {
        JDialog dialog = new JDialog(this, "Add Want", true);
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(360, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        JLabel header = new JLabel("Add a New Want", SwingConstants.CENTER);
        header.setFont(UITheme.TITLE_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(UITheme.BODY_FONT);
        JTextField descField = new JTextField();
        descField.setFont(UITheme.BODY_FONT);
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel costLabel = new JLabel("Cost ($):");
        costLabel.setFont(UITheme.BODY_FONT);
        JTextField costField = new JTextField();
        costField.setFont(UITheme.BODY_FONT);
        costField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel dateLabel = new JLabel("Target Date (YYYY-MM-DD):");
        dateLabel.setFont(UITheme.BODY_FONT);
        JTextField dateField = new JTextField();
        dateField.setFont(UITheme.BODY_FONT);
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        inputPanel.add(descLabel);
        inputPanel.add(descField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(costLabel);
        inputPanel.add(costField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        dialog.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UITheme.BODY_FONT);
        cancelButton.setBackground(UITheme.ACCENT);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        JButton saveButton = new JButton("Save Want");
        saveButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 15f));
        saveButton.setBackground(UITheme.PRIMARY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            String desc = descField.getText().trim();
            String costText = costField.getText().trim();
            String dateText = dateField.getText().trim();

            if (desc.isEmpty() || costText.isEmpty() || dateText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                double cost = Double.parseDouble(costText);
                java.time.LocalDate dueDate = java.time.LocalDate.parse(dateText);

                Wants want = new Wants(desc, cost, dueDate);
                profile.addWant(want);

                JOptionPane.showMessageDialog(dialog, "Want added successfully!");
                refreshAction.run();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for cost.");
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.");
            }
        });

        dialog.setVisible(true);
    }


    private void showNeedsMenu() {
        // Create a dialog styled like a mobile sub-screen
        JDialog dialog = new JDialog(this, "Your Needs", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);

        // 📱 Mobile window style
        dialog.setSize(360, 640);       // same portrait size as main app
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        // --- Title Label ---
        JLabel title = new JLabel("Your Needs", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(title, BorderLayout.NORTH);

        // --- Content Panel (list of needs) ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        // --- Add Need Button ---
        JButton addButton = new JButton("➕ Add Need");
        addButton.setBackground(UITheme.PRIMARY_LIGHT); // lighter green for contrast
        addButton.setForeground(Color.WHITE);
        addButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Bottom Wrapper (Add Button + Navigation Bar) ---
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false); // let background show through

        // Small padding above Add button
        JPanel addPanel = new JPanel(new BorderLayout());
        addPanel.setOpaque(false);
        addPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 8, 15));
        addPanel.add(addButton, BorderLayout.CENTER);

        // --- Navigation Bar (identical to dashboard) ---
        JPanel navBar = new JPanel(new GridLayout(1, 4, 10, 0));
        navBar.setBackground(UITheme.PRIMARY); // same dark green as dashboard
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JButton needsNav = createNavButton("Needs", "🛒");
        JButton wantsNav = createNavButton("Wants", "🎯");
        JButton savingsNav = createNavButton("Savings", "💰");
        JButton settingsNav = createNavButton("Settings", "⚙️");

        navBar.add(needsNav);
        navBar.add(wantsNav);
        navBar.add(savingsNav);
        navBar.add(settingsNav);

        // Combine addPanel and navBar
        bottomWrapper.add(addPanel, BorderLayout.NORTH);
        bottomWrapper.add(createNavigationBar(dialog), BorderLayout.SOUTH);
        dialog.add(bottomWrapper, BorderLayout.SOUTH);

        // --- Actions ---
        addButton.addActionListener(e -> showAddNeedDialog(() -> refreshNeedsContent(dialog, contentPanel)));

        // Navigation buttons (dispose current window before opening)
        needsNav.addActionListener(e -> { dialog.dispose(); showNeedsMenu(); });
        wantsNav.addActionListener(e -> { dialog.dispose(); showWantsMenu(); });
        savingsNav.addActionListener(e -> { dialog.dispose(); showSavingsMenu(); });
        settingsNav.addActionListener(e -> { dialog.dispose(); showSettingsMenu(); });

        // --- Build content ---
        Runnable refresh = () -> refreshNeedsContent(dialog, contentPanel);
        refresh.run();

        dialog.setVisible(true);
    }

    private void refreshNeedsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Needs> needs = profile.getNeedsList();

        if (needs.isEmpty()) {
            JLabel noNeedsLabel = new JLabel("(No needs yet)", SwingConstants.CENTER);
            noNeedsLabel.setFont(UITheme.BODY_FONT);
            contentPanel.add(noNeedsLabel, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setOpaque(false);
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Needs n : needs) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setOpaque(false);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

                // Left: description + meta
                JLabel label = new JLabel(
                    "• " + n.getDescription() +
                    " — $" + String.format("%.2f", n.getCost()) +
                    " (Due: " + n.getDueDate() + ")"
                );
                label.setFont(UITheme.BODY_FONT);

                // Right: action
                JButton completeBtn = new JButton(n.isComplete() ? "Completed" : "Mark Complete");
                completeBtn.setEnabled(!n.isComplete());
                completeBtn.setBackground(UITheme.PRIMARY_LIGHT);
                completeBtn.setForeground(Color.WHITE);
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
                completeBtn.setFocusPainted(false);
                completeBtn.addActionListener(e -> {
                    int choice = JOptionPane.showConfirmDialog(
                            dialog,
                            "Mark this task complete?",
                            "Confirm Completion",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        n.markComplete();
                        refreshNeedsContent(dialog, contentPanel);
                    }
                });

                itemPanel.add(label, BorderLayout.CENTER);
                itemPanel.add(completeBtn, BorderLayout.EAST);
                listPanel.add(itemPanel);
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

 // --- Placeholder methods for upcoming features ---

    private void showWantsMenu() {
        // Create a dialog styled like a mobile sub-screen
        JDialog dialog = new JDialog(this, "Your Wants", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);

        // 📱 Mobile window style
        dialog.setSize(360, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        // --- Title Label ---
        JLabel title = new JLabel("Your Wants", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(title, BorderLayout.NORTH);

        // --- Content Panel (list of wants) ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        // --- Add Want Button ---
        JButton addButton = new JButton("➕ Add Want");
        addButton.setBackground(UITheme.PRIMARY_LIGHT);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Bottom Wrapper (Add Button + Navigation Bar) ---
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        JPanel addPanel = new JPanel(new BorderLayout());
        addPanel.setOpaque(false);
        addPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 8, 15));
        addPanel.add(addButton, BorderLayout.CENTER);

        bottomWrapper.add(addPanel, BorderLayout.NORTH);
        bottomWrapper.add(createNavigationBar(dialog), BorderLayout.SOUTH);

        dialog.add(bottomWrapper, BorderLayout.SOUTH);

        // --- Actions ---
        addButton.addActionListener(e -> showAddWantDialog(() -> refreshWantsContent(dialog, contentPanel)));

        // Runnable that rebuilds the content
        Runnable refresh = () -> refreshWantsContent(dialog, contentPanel);
        refresh.run();

        dialog.setVisible(true);
    }


    private void showSavingsMenu() {
        JOptionPane.showMessageDialog(this, "Savings screen coming soon!");
    }

    private void showSettingsMenu() {
        JOptionPane.showMessageDialog(this, "Settings screen coming soon!");
    }
    
    /**
     * Rebuilds the content of the "View Needs" dialog.
     * Avoids self-referential lambdas by centralizing the refresh logic here.
     * Includes a "Mark Complete" button per item.
     */
    private void refreshWantsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Wants> wants = profile.getWantsList();

        if (wants.isEmpty()) {
            JLabel noWantsLabel = new JLabel("(No wants yet)", SwingConstants.CENTER);
            noWantsLabel.setFont(UITheme.BODY_FONT);
            contentPanel.add(noWantsLabel, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setOpaque(false);
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Wants w : wants) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setOpaque(false);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

                JLabel label = new JLabel("• " + w.getDescription() + " — $" + w.getCost() + " (Due: " + w.getDueDate() + ")");
                label.setFont(UITheme.BODY_FONT);

                JButton completeBtn = new JButton("Mark Complete");
                completeBtn.setBackground(UITheme.PRIMARY_LIGHT);
                completeBtn.setForeground(Color.WHITE);
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
                completeBtn.setFocusPainted(false);
                completeBtn.addActionListener(e -> {
                    w.markComplete();
                    refreshWantsContent(dialog, contentPanel);
                });

                itemPanel.add(label, BorderLayout.CENTER);
                itemPanel.add(completeBtn, BorderLayout.EAST);
                listPanel.add(itemPanel);
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}