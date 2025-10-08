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
            new Runnable[] {
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

                // --- Info section (left side) ---
                JLabel desc = new JLabel("• " + n.getDescription());
                desc.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));

                JLabel cost = new JLabel("$" + String.format("%.2f", n.getCost()));
                cost.setFont(UITheme.BODY_FONT);

                JLabel date = new JLabel("Due: " + n.getDueDate());
                date.setFont(UITheme.BODY_FONT.deriveFont(11f));
                date.setForeground(Color.DARK_GRAY);

                JPanel infoPanel = new JPanel();
                infoPanel.setOpaque(false);
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.add(desc);
                infoPanel.add(cost);
                infoPanel.add(date);

                // --- Right side: Mark Complete button ---
                JPanel rightPanel = new JPanel(new BorderLayout());
                rightPanel.setOpaque(false);
                rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // right aligned

                JButton completeBtn = SharedUI.createRoundedButton(
                    n.isComplete() ? "✓ Done" : "Mark Complete",
                    UITheme.PRIMARY_LIGHT,
                    Color.WHITE
                );
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                completeBtn.setEnabled(!n.isComplete());
                completeBtn.setFocusPainted(false);
                completeBtn.setPreferredSize(new Dimension(110, 32));
                completeBtn.addActionListener(e -> {
                    n.markComplete();
                    refreshNeedsContent(dialog, contentPanel);
                });

                JPanel btnWrapper = new JPanel(new GridBagLayout());
                btnWrapper.setOpaque(false);
                btnWrapper.setPreferredSize(new Dimension(120, 40)); // 🔹 adjust here to control button height
                btnWrapper.add(completeBtn);

                rightPanel.add(btnWrapper, BorderLayout.EAST);


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
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // ✅ removes horizontal bar

            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
