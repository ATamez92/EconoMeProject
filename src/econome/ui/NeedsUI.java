package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Needs;

/**
 * Represents the Needs screen in the EconoMe application.
 * <p>
 * Displays and manages the user’s list of essential expenses ("Needs"),
 * allowing them to add, mark complete, or delete items.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Display all recorded Need items as styled cards.</li>
 *   <li>Allow users to add, mark complete, or delete Needs.</li>
 *   <li>Persist all changes to the active user profile.</li>
 * </ul>
 */
public class NeedsUI {

    // --- Instance Variables ---
    private final Profile userProfile; // Active user profile
    private final SwingUI parentUI;    // Reference to the parent dashboard

    /**
     * Constructs the Needs screen for the given user profile.
     *
     * @param profile the current user’s financial profile
     * @param parent  reference to the main application window
     */
    public NeedsUI(Profile profile, SwingUI parent) {
        this.userProfile = profile;
        this.parentUI = parent;
        buildScreen();
    } // End of constructor NeedsUI

    /**
     * Builds and displays the main Needs management screen.
     */
    private void buildScreen() {
        // --- Base Window ---
        JDialog dialog = SharedUI.createBaseScreen("Your Needs", parentUI);

        // --- Content Area ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        // --- Add Need Button ---
        JButton addNeedButton = SharedUI.createRoundedButton(
                "➕ Add Need",
                new Color(102, 187, 106),
                Color.WHITE
        );
        addNeedButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addNeedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addNeedButton.addActionListener(e ->
                parentUI.showAddNeedDialog(() -> refreshNeedsContent(dialog, contentPanel))
        );

        // --- Bottom Section (Add Button + Navigation) ---
        JPanel bottomPanel = SharedUI.createBottomSection(
                dialog,
                addNeedButton,
                new Runnable[]{
                        () -> new NeedsUI(userProfile, parentUI),
                        () -> new WantsUI(userProfile, parentUI),
                        () -> parentUI.showSavingsMenu(),
                        () -> parentUI.showSettingsMenu()
                }
        );
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // --- Load and Display Needs ---
        refreshNeedsContent(dialog, contentPanel);
        dialog.setVisible(true);
    } // End of method buildScreen

    /**
     * Refreshes and rebuilds the list of Needs displayed on screen.
     * Called whenever a Need is added, updated, or deleted.
     *
     * @param dialog        the parent dialog window
     * @param contentPanel  the main content area container
     */
    private void refreshNeedsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Needs> needsList = userProfile.getNeedsList();

        // --- Empty State ---
        if (needsList.isEmpty()) {
            JLabel emptyLabel = new JLabel("(No needs yet)", SwingConstants.CENTER);
            emptyLabel.setFont(UITheme.BODY_FONT);
            contentPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // --- Needs List ---
            JPanel listPanel = new JPanel(new GridBagLayout());
            listPanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 0, 5, 0);

            int cardHeight = 85;

            for (Needs needItem : needsList) {
                // --- Card Wrapper ---
                JPanel cardPanel = SharedUI.createCardPanel(cardHeight);
                cardPanel.setLayout(new BorderLayout());
                cardPanel.setPreferredSize(new Dimension(320, cardHeight));

                // --- Left Side: Text Info ---
                JPanel textPanel = new JPanel();
                textPanel.setOpaque(false);
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                JLabel descriptionLabel = new JLabel("• " + needItem.getDescription());
                descriptionLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));

                JLabel costLabel = new JLabel("$" + String.format("%.2f", needItem.getCost()));
                costLabel.setFont(UITheme.BODY_FONT);

                JLabel dueDateLabel = new JLabel("Due: " + needItem.getDueDate());
                dueDateLabel.setFont(UITheme.BODY_FONT.deriveFont(11f));
                dueDateLabel.setForeground(Color.DARK_GRAY);

                textPanel.add(descriptionLabel);
                textPanel.add(costLabel);
                textPanel.add(dueDateLabel);

                JPanel infoPanel = new JPanel(new GridBagLayout());
                infoPanel.setOpaque(false);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                GridBagConstraints gbcInfo = new GridBagConstraints();
                gbcInfo.gridx = 0;
                gbcInfo.gridy = 0;
                gbcInfo.weightx = 1.0;
                gbcInfo.weighty = 1.0;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.anchor = GridBagConstraints.WEST;
                infoPanel.add(textPanel, gbcInfo);

                // --- Right Side: Action Buttons ---
                JPanel rightPanel = new JPanel(new GridBagLayout());
                rightPanel.setOpaque(false);
                rightPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 10));

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setOpaque(false);
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

                Dimension buttonSize = new Dimension(110, 29);

                // ✅ Complete Button
                JButton completeButton = SharedUI.createRoundedButton(
                        needItem.isComplete() ? "✓ Done" : "Mark Complete",
                        needItem.isComplete() ? new Color(180, 180, 180) : UITheme.PRIMARY_LIGHT,
                        Color.WHITE
                );
                completeButton.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                completeButton.setEnabled(!needItem.isComplete());
                completeButton.setFocusPainted(false);
                completeButton.setPreferredSize(buttonSize);
                completeButton.setMinimumSize(buttonSize);
                completeButton.setMaximumSize(buttonSize);
                completeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                completeButton.addActionListener(e -> {
                    needItem.markComplete();
                    userProfile.saveProfile(); // Persist completion state
                    refreshNeedsContent(dialog, contentPanel);
                });

                // 🗑 Delete Button
                JButton deleteButton = SharedUI.createRoundedButton(
                        "🗑 Delete",
                        new Color(200, 80, 80),
                        Color.WHITE
                );
                deleteButton.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                deleteButton.setFocusPainted(false);
                deleteButton.setPreferredSize(buttonSize);
                deleteButton.setMaximumSize(buttonSize);
                deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            dialog,
                            "Delete \"" + needItem.getDescription() + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        userProfile.removeNeed(needItem);
                        refreshNeedsContent(dialog, contentPanel);
                    }
                });

                // Add both buttons with spacing
                buttonsPanel.add(completeButton);
                buttonsPanel.add(Box.createRigidArea(new Dimension(0, 6)));
                buttonsPanel.add(deleteButton);

                GridBagConstraints gbcRight = new GridBagConstraints();
                gbcRight.gridx = 0;
                gbcRight.gridy = 0;
                gbcRight.anchor = GridBagConstraints.CENTER;
                rightPanel.add(buttonsPanel, gbcRight);

                // --- Assemble Card ---
                cardPanel.add(infoPanel, BorderLayout.CENTER);
                cardPanel.add(rightPanel, BorderLayout.EAST);

                gbc.gridy = listPanel.getComponentCount();
                listPanel.add(cardPanel, gbc);
            } // End of loop

            gbc.weighty = 1;
            listPanel.add(Box.createVerticalGlue(), gbc);

            // --- Scroll Pane Setup ---
            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(UITheme.BACKGROUND);
            scrollPane.getViewport().setBackground(UITheme.BACKGROUND);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));

            contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 0));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    } // End of method refreshNeedsContent
} // End of class NeedsUI