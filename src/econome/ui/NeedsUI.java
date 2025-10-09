package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Needs;

/**
 * Screen for displaying and managing the user's list of Needs.
 * Uses SharedUI for consistent layout and styling.
 */
public class NeedsUI {

    private final Profile profile;
    private final SwingUI parent; // Reference to main dashboard

    public NeedsUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    }

    private void buildScreen() {
        // 🪟 Create base mobile-sized dialog
        JDialog dialog = SharedUI.createBaseScreen("Your Needs", parent);

        // --- Content area for listing needs ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        // --- Add Need button ---
        JButton addButton = SharedUI.createRoundedButton("➕ Add Need",
                new Color(102, 187, 106), Color.WHITE); // Light green to match Home Needs
        addButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e ->
                parent.showAddNeedDialog(() -> refreshNeedsContent(dialog, contentPanel))
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

        // --- Load Needs ---
        refreshNeedsContent(dialog, contentPanel);
        dialog.setVisible(true);
    }

    private void refreshNeedsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Needs> needs = profile.getNeedsList();

        if (needs.isEmpty()) {
            JLabel emptyLabel = new JLabel("(No needs yet)", SwingConstants.CENTER);
            emptyLabel.setFont(UITheme.BODY_FONT);
            contentPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel(new GridBagLayout());
            listPanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 0, 5, 0);

            int cardHeight = 85;

            for (Needs n : needs) {
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

                JLabel desc = new JLabel("• " + n.getDescription());
                desc.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));

                JLabel cost = new JLabel("$" + String.format("%.2f", n.getCost()));
                cost.setFont(UITheme.BODY_FONT);

                JLabel date = new JLabel("Due: " + n.getDueDate());
                date.setFont(UITheme.BODY_FONT.deriveFont(11f));
                date.setForeground(Color.DARK_GRAY);

                textPanel.add(desc);
                textPanel.add(cost);
                textPanel.add(date);

                JPanel infoPanel = new JPanel(new GridBagLayout());
                infoPanel.setOpaque(false);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // small left padding

                GridBagConstraints gbcInfo = new GridBagConstraints();
                gbcInfo.gridx = 0;
                gbcInfo.gridy = 0;
                gbcInfo.weightx = 1.0;
                gbcInfo.weighty = 1.0;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.anchor = GridBagConstraints.WEST; // left side, vertically centered
                infoPanel.add(textPanel, gbcInfo);

                // --- Right side: Buttons ---
                JPanel rightPanel = new JPanel(new GridBagLayout());
                rightPanel.setOpaque(false);
                rightPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 10));

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setOpaque(false);
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

                Dimension buttonSize = new Dimension(110, 29);

                JButton completeBtn = SharedUI.createRoundedButton(
                        n.isComplete() ? "✓ Done" : "Mark Complete",
                        n.isComplete() ? new Color(180, 180, 180) : UITheme.PRIMARY_LIGHT,
                        Color.WHITE
                );
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                completeBtn.setEnabled(!n.isComplete());
                completeBtn.setFocusPainted(false);
                completeBtn.setPreferredSize(buttonSize);
                completeBtn.setMaximumSize(buttonSize);
                completeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                completeBtn.addActionListener(e -> {
                    n.markComplete();
                    refreshNeedsContent(dialog, contentPanel);
                });

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
                            "Delete \"" + n.getDescription() + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        profile.removeNeed(n);
                        refreshNeedsContent(dialog, contentPanel);
                    }
                });

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
            }

            gbc.weighty = 1;
            listPanel.add(Box.createVerticalGlue(), gbc);

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}