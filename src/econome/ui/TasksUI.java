package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import econome.model.Profile;
import econome.model.Needs;
import econome.model.Wants;

/**
 * Displays a combined "Tasks" page that shows all active Needs and Wants
 * in a single, scrollable list.
 * <p>
 * Each item card includes:
 * <ul>
 *   <li>Description, cost, and due/target date</li>
 *   <li>A color-coded tag indicating type (Need or Want)</li>
 *   <li>A button to mark completion</li>
 * </ul>
 * </p>
 */
public class TasksUI {

    // --- References ---
    private final Profile profile;
    private final SwingUI parent;

    /**
     * Constructs a new TasksUI screen for the given profile.
     *
     * @param profile the user's profile containing Needs and Wants
     * @param parent  the main SwingUI controller
     */
    public TasksUI(Profile profile, SwingUI parent) {
        this.profile = profile;
        this.parent = parent;
        buildScreen();
    } // End of constructor TasksUI


    // -------------------------------------------------------------------------
    // SCREEN BUILDING
    // -------------------------------------------------------------------------

    /**
     * Builds and displays the main Tasks screen.
     * Includes both Needs and Wants in one scrollable list.
     */
    private void buildScreen() {
        // 🪟 Create base window
        JDialog dialog = SharedUI.createBaseScreen("Tasks", parent);
        dialog.setLayout(new BorderLayout());

        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titleLabel = new JLabel("Your Tasks", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel subtitleLabel = new JLabel("All Needs and Wants in one place", SwingConstants.CENTER);
        subtitleLabel.setFont(UITheme.SUBTITLE_FONT);
        subtitleLabel.setForeground(Color.DARK_GRAY);

        JPanel titleWrap = new JPanel(new GridLayout(2, 1));
        titleWrap.setOpaque(false);
        titleWrap.add(titleLabel);
        titleWrap.add(subtitleLabel);
        headerPanel.add(titleWrap, BorderLayout.CENTER);

        dialog.add(headerPanel, BorderLayout.NORTH);

        // --- Scrollable List Section ---
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Add Needs and Wants
        addTasksToPanel(listPanel, profile.getNeedsList(), "Need", new Color(102, 187, 106));
        addTasksToPanel(listPanel, profile.getWantsList(), "Want", new Color(66, 165, 245));

        // Empty State
        if (profile.getNeedsList().isEmpty() && profile.getWantsList().isEmpty()) {
            JLabel emptyLabel = new JLabel("(No current tasks)", SwingConstants.CENTER);
            emptyLabel.setFont(UITheme.BODY_FONT);
            emptyLabel.setForeground(Color.GRAY);
            listPanel.add(Box.createVerticalGlue());
            listPanel.add(emptyLabel);
            listPanel.add(Box.createVerticalGlue());
        }

        // Scroll Pane Configuration
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
    } // End of method buildScreen


    // -------------------------------------------------------------------------
    // TASK CARD CREATION
    // -------------------------------------------------------------------------

    /**
     * Adds Need or Want task cards to the specified panel.
     *
     * @param parentPanel the container panel to add cards into
     * @param items       the list of tasks (Needs or Wants)
     * @param typeLabel   text label for the tag (e.g., "Need", "Want")
     * @param tagColor    background color for the type tag
     */
    private void addTasksToPanel(JPanel parentPanel, List<?> items, String typeLabel, Color tagColor) {
        if (items.isEmpty()) return;

        for (Object obj : items) {

            // --- Card setup ---
            JPanel card = SharedUI.createCardPanel(90);
            card.setLayout(new BorderLayout(10, 0));
            card.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            card.setPreferredSize(new Dimension(300, 90));
            card.setMaximumSize(new Dimension(300, 90));
            card.setAlignmentX(Component.CENTER_ALIGNMENT);

            // --- Extract info ---
            String desc;
            String costText;
            String dateText;
            boolean complete;

            if (obj instanceof Needs need) {
                desc = need.getDescription();
                costText = "$" + String.format("%.2f", need.getCost());
                dateText = "Due: " + need.getDueDate();
                complete = need.isComplete();
            } else if (obj instanceof Wants want) {
                desc = want.getDescription();
                costText = "$" + String.format("%.2f", want.getCost());
                dateText = "Target: " + want.getDueDate();
                complete = want.isComplete();
            } else continue;

            // --- LEFT: Info Panel (description, cost, date) ---
            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel descLabel = new JLabel(desc);
            descLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));

            JLabel costLabel = new JLabel(costText);
            costLabel.setFont(UITheme.BODY_FONT);

            JLabel dateLabel = new JLabel(dateText);
            dateLabel.setFont(UITheme.BODY_FONT.deriveFont(12f));
            dateLabel.setForeground(Color.DARK_GRAY);

            textPanel.add(descLabel);
            textPanel.add(costLabel);
            textPanel.add(dateLabel);

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

            // --- RIGHT: Tag and Complete Button ---
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);

            // Type Tag
            JLabel tag = new JLabel(typeLabel, SwingConstants.CENTER);
            tag.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 11f));
            tag.setForeground(Color.WHITE);
            tag.setOpaque(true);
            tag.setBackground(tagColor);
            tag.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

            JPanel tagWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            tagWrap.setOpaque(false);
            tagWrap.add(tag);
            rightPanel.add(tagWrap, BorderLayout.NORTH);

            // Complete Button
            JButton completeBtn = SharedUI.createRoundedButton(
                    complete ? "✓ Done" : "Complete",
                    complete ? new Color(180, 180, 180) : UITheme.PRIMARY_LIGHT,
                    Color.WHITE
            );
            completeBtn.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
            completeBtn.setFocusPainted(false);
            completeBtn.setEnabled(!complete);
            completeBtn.setPreferredSize(new Dimension(120, 32));
            completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Button Action
            completeBtn.addActionListener(e -> {
                if (obj instanceof Needs need) need.markComplete();
                else if (obj instanceof Wants want) want.markComplete();

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

            // --- Assemble card ---
            card.add(infoPanel, BorderLayout.CENTER);
            card.add(rightPanel, BorderLayout.EAST);

            parentPanel.add(card);
            parentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
    } // End of method addTasksToPanel
} // End of class TasksUI