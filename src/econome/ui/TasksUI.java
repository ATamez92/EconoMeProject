package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Needs;
import econome.model.Wants;

/**
 * Combined "Tasks" page — shows both Needs and Wants in one scrollable list.
 */
public class TasksUI {

    private final Profile profile;
    private final SwingUI parent;

    public TasksUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    }

    private void buildScreen() {
        JDialog dialog = SharedUI.createBaseScreen("Tasks", parent);
        dialog.setLayout(new BorderLayout());

        // --- Scrollable list area ---
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Add tasks for Needs and Wants
        addTasksToPanel(listPanel, profile.getNeedsList(), "Need", new Color(102, 187, 106));
        addTasksToPanel(listPanel, profile.getWantsList(), "Want", new Color(66, 165, 245));

        // --- Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // ✅ disable horizontal scroll
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

    /**
     * Adds Need/Want task cards to the given panel.
     */
    private void addTasksToPanel(JPanel parentPanel, List<?> items, String typeLabel, Color tagColor) {
        if (items.isEmpty()) return;

        for (Object obj : items) {
            // --- Card setup ---
            JPanel card = SharedUI.createCardPanel(90);
            card.setLayout(new BorderLayout(10, 0));
            card.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            // ✅ Match width with Needs/Wants pages
            card.setPreferredSize(new Dimension(300, 90));
            card.setMaximumSize(new Dimension(300, 90));
            card.setMinimumSize(new Dimension(300, 90));
            card.setAlignmentX(Component.CENTER_ALIGNMENT);

            // --- Extract info ---
            String desc, costText, dateText;
            boolean complete;

            if (obj instanceof Needs n) {
                desc = n.getDescription();
                costText = "$" + String.format("%.2f", n.getCost());
                dateText = "Due: " + n.getDueDate();
                complete = n.isComplete();
            } else if (obj instanceof Wants w) {
                desc = w.getDescription();
                costText = "$" + String.format("%.2f", w.getCost());
                dateText = "Target: " + w.getDueDate();
                complete = w.isComplete();
            } else continue;

            // --- Left info panel ---
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false);
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            JLabel descLabel = new JLabel(desc);
            descLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));

            JLabel costLabel = new JLabel(costText);
            costLabel.setFont(UITheme.BODY_FONT);

            JLabel dateLabel = new JLabel(dateText);
            dateLabel.setFont(UITheme.BODY_FONT.deriveFont(12f));
            dateLabel.setForeground(Color.DARK_GRAY);

            infoPanel.add(descLabel);
            infoPanel.add(costLabel);
            infoPanel.add(dateLabel);

            // --- Right panel (tag + button) ---
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);

            // ✅ Rounded Need/Want tag — top-right corner
            JLabel tag = new JLabel(typeLabel, SwingConstants.CENTER);
            tag.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 11f));
            tag.setForeground(Color.WHITE);
            tag.setOpaque(true);
            tag.setBackground(tagColor);
            tag.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            tag.putClientProperty("JComponent.roundRect", true);

            JPanel tagWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            tagWrap.setOpaque(false);
            tagWrap.add(tag);
            rightPanel.add(tagWrap, BorderLayout.NORTH);

            // ✅ Rounded Complete button — same size as Needs/Wants pages
            JButton completeBtn = SharedUI.createRoundedButton(
                    complete ? "✓ Done" : "Complete",
                    UITheme.PRIMARY_LIGHT,
                    Color.WHITE
            );
            completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
            completeBtn.setFocusPainted(false);
            completeBtn.setEnabled(!complete);
            completeBtn.setPreferredSize(new Dimension(120, 40)); // same as Mark Complete
            completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            completeBtn.addActionListener(e -> {
                if (obj instanceof Needs n) n.markComplete();
                else if (obj instanceof Wants w) w.markComplete();
                parentPanel.removeAll();
                addTasksToPanel(parentPanel, profile.getNeedsList(), "Need", new Color(102, 187, 106));
                addTasksToPanel(parentPanel, profile.getWantsList(), "Want", new Color(66, 165, 245));
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            buttonWrap.setOpaque(false);
            buttonWrap.add(completeBtn);
            rightPanel.add(buttonWrap, BorderLayout.SOUTH);

            card.add(infoPanel, BorderLayout.CENTER);
            card.add(rightPanel, BorderLayout.EAST);

            parentPanel.add(card);
            parentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
    }
}
