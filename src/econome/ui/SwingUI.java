package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import econome.model.*;
import econome.logic.BudgetManager;
import java.time.LocalDate;

/**
 * Acts as the main controller for the EconoMe application's Swing interface.
 * <p>
 * Handles navigation between pages (Home, Needs, Wants, etc.)
 * and manages the dialogs for adding new Needs or Wants.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Launch and manage main dashboard screens.</li>
 *   <li>Provide modal dialogs for creating Needs and Wants.</li>
 *   <li>Route user interactions and refresh content dynamically.</li>
 * </ul>
 */
public class SwingUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    // --- Core Application References ---
    private final BudgetManager budgetManager;
    private final Profile profile;

    // --- Buttons (not currently shown on Home, but preserved for future use) ---
    private final JButton needsButton = new JButton("Needs");
    private final JButton wantsButton = new JButton("Wants");
    private final JButton savingsButton = new JButton("Savings");
    private final JButton settingsButton = new JButton("Settings");

    /**
     * Constructs the SwingUI controller for a specific profile.
     * Automatically opens the Home screen upon creation.
     *
     * @param profile the user profile currently in use
     */
    public SwingUI(Profile profile) {
        this.profile = profile;
        this.budgetManager = new BudgetManager();

        // Launch main Home screen immediately
        new HomeUI(profile, this);
    } // End of constructor SwingUI


    // -------------------------------------------------------------------------
    // STYLING HELPERS
    // -------------------------------------------------------------------------

    /**
     * Applies consistent styling to dashboard buttons.
     *
     * @param button  the button to style
     * @param bgColor background color
     */
    private void styleMainButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    } // End of method styleMainButton


    /**
     * Returns the currently active user profile.
     *
     * @return the loaded {@link Profile}
     */
    public Profile getProfile() {
        return profile;
    } // End of method getProfile


    // -------------------------------------------------------------------------
    // ACTION HANDLING
    // -------------------------------------------------------------------------

    /**
     * Handles navigation button actions (if used).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == needsButton) {
            new NeedsUI(profile, this);
        } else if (source == wantsButton) {
            JOptionPane.showMessageDialog(this, "Wants section coming soon!");
        } else if (source == savingsButton) {
            JOptionPane.showMessageDialog(this, "Savings section coming soon!");
        } else if (source == settingsButton) {
            JOptionPane.showMessageDialog(this, "Settings coming soon!");
        }
    } // End of method actionPerformed


    // -------------------------------------------------------------------------
    // NEEDS DIALOG
    // -------------------------------------------------------------------------

    /**
     * Displays a modal dialog allowing the user to add a new "Need".
     *
     * @param refreshAction callback to refresh the UI after saving
     */
    public void showAddNeedDialog(Runnable refreshAction) {
        JDialog dialog = createBaseDialog("Add Need");

        JLabel header = new JLabel("Add a New Need", SwingConstants.CENTER);
        header.setFont(UITheme.TITLE_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(header, BorderLayout.NORTH);

        JPanel inputPanel = buildInputForm(
                "Description:",
                "Cost ($):",
                "Due Date (YYYY-MM-DD):"
        );
        dialog.add(inputPanel, BorderLayout.CENTER);

        JTextField descField = (JTextField) inputPanel.getComponent(1);
        JTextField costField = (JTextField) inputPanel.getComponent(4);
        JTextField dateField = (JTextField) inputPanel.getComponent(7);

        JPanel buttonPanel = buildFormButtons(dialog, () -> {
            String desc = descField.getText().trim();
            String costText = costField.getText().trim();
            String dateText = dateField.getText().trim();

            if (desc.isEmpty() || costText.isEmpty() || dateText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                double cost = Double.parseDouble(costText);
                LocalDate dueDate = LocalDate.parse(dateText);

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

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    } // End of method showAddNeedDialog


    // -------------------------------------------------------------------------
    // WANTS DIALOG
    // -------------------------------------------------------------------------

    /**
     * Displays a modal dialog allowing the user to add a new "Want".
     *
     * @param refreshAction callback to refresh the UI after saving
     */
    public void showAddWantDialog(Runnable refreshAction) {
        JDialog dialog = createBaseDialog("Add Want");

        JLabel header = new JLabel("Add a New Want", SwingConstants.CENTER);
        header.setFont(UITheme.TITLE_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(header, BorderLayout.NORTH);

        JPanel inputPanel = buildInputForm(
                "Description:",
                "Cost ($):",
                "Target Date (YYYY-MM-DD):"
        );
        dialog.add(inputPanel, BorderLayout.CENTER);

        JTextField descField = (JTextField) inputPanel.getComponent(1);
        JTextField costField = (JTextField) inputPanel.getComponent(4);
        JTextField dateField = (JTextField) inputPanel.getComponent(7);

        JPanel buttonPanel = buildFormButtons(dialog, () -> {
            String desc = descField.getText().trim();
            String costText = costField.getText().trim();
            String dateText = dateField.getText().trim();

            if (desc.isEmpty() || costText.isEmpty() || dateText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                double cost = Double.parseDouble(costText);
                LocalDate targetDate = LocalDate.parse(dateText);

                Wants want = new Wants(desc, cost, targetDate);
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

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    } // End of method showAddWantDialog


    // -------------------------------------------------------------------------
    // UI CONSTRUCTION HELPERS
    // -------------------------------------------------------------------------

    /**
     * Builds a mobile-style modal dialog window for add/edit forms.
     *
     * @param title the dialog title
     * @return a configured {@link JDialog}
     */
    private JDialog createBaseDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(360, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        return dialog;
    } // End of method createBaseDialog


    /**
     * Builds a form panel with three labeled text fields.
     *
     * @param label1 label for the first field
     * @param label2 label for the second field
     * @param label3 label for the third field
     * @return a vertical {@link JPanel} containing labels and fields
     */
    private JPanel buildInputForm(String label1, String label2, String label3) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        for (String label : new String[]{label1, label2, label3}) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(UITheme.BODY_FONT);
            JTextField field = new JTextField();
            field.setFont(UITheme.BODY_FONT);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            panel.add(lbl);
            panel.add(field);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panel;
    } // End of method buildInputForm


    /**
     * Builds a button bar for dialogs with Cancel and Save actions.
     *
     * @param dialog       parent dialog reference
     * @param saveRunnable action to perform on save
     * @return a {@link JPanel} containing the buttons
     */
    private JPanel buildFormButtons(JDialog dialog, Runnable saveRunnable) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UITheme.BODY_FONT);
        cancelButton.setBackground(UITheme.ACCENT);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 15f));
        saveButton.setBackground(UITheme.PRIMARY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> saveRunnable.run());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    } // End of method buildFormButtons


    // -------------------------------------------------------------------------
    // NAVIGATION SHORTCUTS
    // -------------------------------------------------------------------------

    /** Opens the dashboard (Home page). */
    public void showDashMenu() {
        new HomeUI(profile, this);
    } // End of method showDashMenu


    /** Opens the Savings section (placeholder). */
    public void showSavingsMenu() {
        JOptionPane.showMessageDialog(this, "Savings screen coming soon!");
    } // End of method showSavingsMenu


    /** Opens the Settings menu (placeholder). */
    public void showSettingsMenu() {
        JOptionPane.showMessageDialog(this, "Settings screen coming soon!");
    } // End of method showSettingsMenu
} // End of class SwingUI