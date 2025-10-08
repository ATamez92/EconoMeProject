package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Wants;

/**
 * Screen for displaying and managing the user's list of Wants.
 * Uses SharedUI for consistent layout and styling.
 */
public class WantsUI {

    private final Profile profile;
    private final SwingUI parent; // Reference to main dashboard

    public WantsUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    }

    private void buildScreen() {
        // 🪟 Create base mobile-sized dialog
        JDialog dialog = SharedUI.createBaseScreen("Your Wants", parent);

        // --- Content area for listing wants ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dialog.add(contentPanel, BorderLayout.CENTER);

        JButton addButton = SharedUI.createRoundedButton("➕ Add Want",
                new Color(102, 187, 106), Color.WHITE); // lighter green to match Home Wants
        addButton.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e ->
            parent.showAddWantDialog(() -> refreshWantsContent(dialog, contentPanel))
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

        // --- Load Wants ---
        refreshWantsContent(dialog, contentPanel);
        dialog.setVisible(true);
    }

    private void refreshWantsContent(JDialog dialog, JPanel contentPanel) {
        contentPanel.removeAll();

        List<Wants> wants = profile.getWantsList();

        if (wants.isEmpty()) {
            JLabel emptyLabel = new JLabel("(No wants yet)", SwingConstants.CENTER);
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

            for (Wants w : wants) {
                // --- Card wrapper ---
                JPanel card = SharedUI.createCardPanel(cardHeight);
                card.setLayout(new BorderLayout());
                card.setPreferredSize(new Dimension(320, cardHeight));
                card.setMaximumSize(new Dimension(320, cardHeight));
                card.setMinimumSize(new Dimension(320, cardHeight));

                // --- Info section (left side) ---
                JLabel desc = new JLabel("• " + w.getDescription());
                desc.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));

                JLabel cost = new JLabel("$" + String.format("%.2f", w.getCost()));
                cost.setFont(UITheme.BODY_FONT);

                JLabel date = new JLabel("Target: " + w.getDueDate());
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
                    w.isComplete() ? "✓ Done" : "Mark Complete",
                    UITheme.PRIMARY_LIGHT,
                    Color.WHITE
                );
                completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
                completeBtn.setEnabled(!w.isComplete());
                completeBtn.setFocusPainted(false);
                completeBtn.setPreferredSize(new Dimension(110, 32));
                completeBtn.addActionListener(e -> {
                    w.markComplete();
                    refreshWantsContent(dialog, contentPanel);
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
