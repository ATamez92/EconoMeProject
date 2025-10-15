package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Wants;

/**
 * Represents the Wants screen in the EconoMe application.
 * <p>
 * Displays and manages the user’s list of discretionary expenses ("Wants"),
 * allowing users to add, mark complete, or delete items.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Display all recorded Want items as styled cards.</li>
 *   <li>Allow users to add, mark complete, or delete Wants.</li>
 *   <li>Persist all changes to the active user profile.</li>
 * </ul>
 */
public class WantsUI {
    // --- Instance Variables ---
    private final Profile profile;
    private final SwingUI parent; // Reference to main dashboard

    /**
     * Constructs the Wants screen for the given user profile.
     *
     * @param profile the current user’s financial profile
     * @param parent  reference to the main application window
     */
    public WantsUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    } // End of constructor
    
    /**
     * Builds and displays the main Wants management screen.
     */
    private void buildScreen() {
        // Create base mobile-sized dialog
        JDialog dialog = SharedUI.createBaseScreen("Your Wants", parent);

        // --- Content area for listing wants ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        // --- Add Want button ---
        JButton addButton = SharedUI.createRoundedButton("➕ Add Want",
                new Color(102, 187, 106), Color.WHITE);
        addButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e ->
                parent.showAddWantDialog(() -> refreshWantsContent(dialog, contentPanel))
        );

        // --- Bottom section (Add button + Navigation bar) ---
        JPanel bottom = SharedUI.createBottomSection(
                dialog,
                addButton,
                new Runnable[]{
                        () -> new NeedsUI(profile, parent),
                        () -> new WantsUI(profile, parent),
                        () -> parent.showSavingsMenu(),
                        () -> parent.showSettingsMenu()
                }
        );
        dialog.add(bottom, BorderLayout.SOUTH);

        // --- Load Wants ---
        refreshWantsContent(dialog, contentPanel);
        dialog.setVisible(true);
    } // End of method

    /**
     * Refreshes and rebuilds the list of Wants displayed on screen.
     * Called whenever a Want is added, updated, or deleted.
     *
     * @param dialog        the parent dialog window
     * @param contentPanel  the main content area container
     */
    private void refreshWantsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Wants> wants = profile.getWantsList();

        // --- Empty State ---
        if (wants.isEmpty()) {
            JLabel emptyLabel = new JLabel("(No wants yet)", SwingConstants.CENTER);
            emptyLabel.setFont(UITheme.BODY_FONT);
            contentPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // --- Wants List ---
            JPanel listPanel = new JPanel(new GridBagLayout());
            listPanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 0, 5, 0);

            int cardHeight = 85;

            for (Wants wantItem : wants) {
                // --- Card wrapper ---
                JPanel card = SharedUI.createCardPanel(cardHeight);
                card.setLayout(new BorderLayout());
                card.setPreferredSize(new Dimension(320, cardHeight));
                card.setMaximumSize(new Dimension(320, cardHeight));
                card.setMinimumSize(new Dimension(320, cardHeight));

                // --- Info section (LEFT: vertically centered, left-aligned) ---
                JPanel textPanel = new JPanel();
                textPanel.setOpaque(false);
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                JLabel desc = new JLabel("• " + wantItem.getDescription());
                desc.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));

                JLabel cost = new JLabel("$" + String.format("%.2f", wantItem.getCost()));
                cost.setFont(UITheme.BODY_FONT);

                JLabel date = new JLabel("Target: " + wantItem.getDueDate());
                date.setFont(UITheme.BODY_FONT.deriveFont(11f));
                date.setForeground(Color.DARK_GRAY);

                textPanel.add(desc);
                textPanel.add(cost);
                textPanel.add(date);

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

                // --- Right side: Buttons ---
                JPanel rightPanel = new JPanel(new GridBagLayout());
                rightPanel.setOpaque(false);
                rightPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 10));

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setOpaque(false);
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

                Dimension buttonSize = new Dimension(110, 29);

                // Complete Button
                JButton completeBtn = SharedUI.createRoundedButton(
                		wantItem.isComplete() ? "✓ Done" : "Mark Complete",
                		wantItem.isComplete() ? new Color(180, 180, 180) : UITheme.PRIMARY_LIGHT,
                        Color.WHITE
                );
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                completeBtn.setEnabled(!wantItem.isComplete());
                completeBtn.setFocusPainted(false);
                completeBtn.setPreferredSize(buttonSize);
                completeBtn.setMaximumSize(buttonSize);
                completeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                completeBtn.addActionListener(e -> {
                	wantItem.markComplete();
                    profile.saveProfile(); // ✅ persist after marking complete
                    refreshWantsContent(dialog, contentPanel);
                });

                // Delete Button
                JButton deleteBtn = SharedUI.createRoundedButton(
                        "🗑 Delete",
                        new Color(200, 80, 80),
                        Color.WHITE
                );
                deleteBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                deleteBtn.setFocusPainted(false);
                deleteBtn.setPreferredSize(buttonSize);
                deleteBtn.setMaximumSize(buttonSize);
                deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            dialog,
                            "Delete \"" + wantItem.getDescription() + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        profile.removeWant(wantItem);
                        refreshWantsContent(dialog, contentPanel);
                    }
                });

                // Add both buttons with spacing
                buttonsPanel.add(completeBtn);
                buttonsPanel.add(Box.createRigidArea(new Dimension(0, 6)));
                buttonsPanel.add(deleteBtn);

                GridBagConstraints gbcRight = new GridBagConstraints();
                gbcRight.gridx = 0;
                gbcRight.gridy = 0;
                gbcRight.anchor = GridBagConstraints.CENTER;
                rightPanel.add(buttonsPanel, gbcRight);

                // --- Assemble card ---
                card.add(infoPanel, BorderLayout.CENTER);
                card.add(rightPanel, BorderLayout.EAST);

                gbc.gridy = listPanel.getComponentCount();
                listPanel.add(card, gbc);
            } // End of for-loop

            gbc.weighty = 1;
            listPanel.add(Box.createVerticalGlue(), gbc);

            // --- Scroll Pane Setup ---
            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(BorderFactory.createEmptyBorder()); 
            scroll.setOpaque(true);
            scroll.getViewport().setOpaque(true);
            scroll.setBackground(UITheme.BACKGROUND);
            scroll.getViewport().setBackground(UITheme.BACKGROUND);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));

            contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 0)); 
            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    } // End of method refreshWantsContent
} // End of class WantsUI