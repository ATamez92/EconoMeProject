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
import econome.ui.SharedUI;

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

    public SwingUI(Profile profile) {
        this.profile = profile;
        this.budgetManager = new BudgetManager();

        // Immediately open the new Home page (now your main screen)
        new HomeUI(profile, this);
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
    
    public Profile getProfile() {
        return profile;
    }

    /**
     * Handles main button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == needsButton) {
            new NeedsUI(profile, this);
        } else if (src == wantsButton) {
            JOptionPane.showMessageDialog(this, "Wants section coming soon!");
        } else if (src == savingsButton) {
            JOptionPane.showMessageDialog(this, "Savings section coming soon!");
        } else if (src == settingsButton) {
            JOptionPane.showMessageDialog(this, "Settings coming soon!");
        }
    }

    public void showAddNeedDialog(Runnable refreshAction) {
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
    
    public void showAddWantDialog(Runnable refreshAction) {
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

    public void showDashMenu() {
        new HomeUI(profile, this);
    }

 // --- Placeholder methods for upcoming features ---

    public void showSavingsMenu() {
        JOptionPane.showMessageDialog(this, "Savings screen coming soon!");
    }

    public void showSettingsMenu() {
        JOptionPane.showMessageDialog(this, "Settings screen coming soon!");
    }

}