package econome.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Provides shared UI components and helper methods for consistent
 * styling across the EconoMe application.
 * <p>
 * This includes reusable widgets such as rounded panels, styled buttons,
 * and navigation bars used across all screens.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Create consistently styled buttons and panels.</li>
 *   <li>Provide reusable navigation components.</li>
 *   <li>Maintain cohesive visual design across the app.</li>
 * </ul>
 */
public class SharedUI {

    // -------------------------------------------------------------------------
    // ROUNDED PANELS
    // -------------------------------------------------------------------------

    /**
     * Creates a rounded panel with a given background color and corner radius.
     *
     * @param color  the background color of the panel
     * @param radius the radius of the rounded corners
     * @return a custom rounded {@link JPanel}
     */
    public static JPanel createRoundedPanel(Color color, int radius) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            }
        };
        panel.setOpaque(false);
        return panel;
    } // End of method createRoundedPanel


    // -------------------------------------------------------------------------
    // ROUNDED BUTTONS
    // -------------------------------------------------------------------------

    /**
     * Creates a rounded button with smooth edges and custom colors.
     * <p>
     * Commonly used for main UI actions such as "Add Need" or "Mark Complete".
     * </p>
     *
     * @param text the button label
     * @param bg   background color
     * @param fg   text color
     * @return a custom rounded {@link JButton}
     */
    public static JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 15f));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(140, 48));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        // Custom rounded paint
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();

                // Background fill
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, width, height, 30, 30);

                // Soft shadow outline
                g2.setColor(new Color(0, 0, 0, 25));
                g2.drawRoundRect(0, 0, width - 1, height - 1, 30, 30);

                // Centered text
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(button.getText());
                int textHeight = fm.getAscent();
                int x = (width - textWidth) / 2;
                int y = (height + textHeight) / 2 - 3;

                g2.setColor(fg);
                g2.drawString(button.getText(), x, y);
                g2.dispose();
            }
        });

        return button;
    } // End of method createRoundedButton


    // -------------------------------------------------------------------------
    // CARD PANELS
    // -------------------------------------------------------------------------

    /**
     * Creates a modern rounded card panel with a soft drop shadow.
     *
     * @param fixedHeightPx the desired fixed card height in pixels
     * @return a styled card {@link JPanel}
     */
    public static JPanel createCardPanel(int fixedHeightPx) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 35));
                g2.fillRoundRect(3, 3, w - 6, h - 3, 20, 20);

                // Card body
                g2.setColor(UITheme.CARD_BACKGROUND);
                g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);

                g2.dispose();
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(300, fixedHeightPx));
        cardPanel.setMaximumSize(new Dimension(300, fixedHeightPx));
        cardPanel.setMinimumSize(new Dimension(300, fixedHeightPx));
        cardPanel.setLayout(new BorderLayout());
        return cardPanel;
    } // End of method createCardPanel


    // -------------------------------------------------------------------------
    // NAVIGATION COMPONENTS
    // -------------------------------------------------------------------------

    /**
     * Creates a standardized navigation button used within the bottom nav bar.
     *
     * @param label button label text
     * @param emoji icon or emoji prefix
     * @return a styled navigation {@link JButton}
     */
    public static JButton createNavButton(String label, String emoji) {
        JButton button = new JButton(emoji + " " + label);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 13f));
        button.setBackground(UITheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    } // End of method createNavButton


    /**
     * Creates a standard navigation bar with four buttons (Home, Tasks, Plan, Settings).
     *
     * @param parent  reference to the parent window
     * @param actions an array of navigation actions
     * @return a fully styled navigation {@link JPanel}
     */
    public static JPanel createNavigationBar(Window parent, Runnable[] actions) {
        JPanel navBar = new JPanel(new GridLayout(1, 4, 8, 0));
        navBar.setBackground(UITheme.PRIMARY);
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        String[] labels = {"Home", "Tasks", "Plan", "Settings"};
        String[] emojis = {"🏠", "📝", "📊", "⚙️"};

        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(emojis[i] + " " + labels[i]);
            button.setFocusPainted(false);
            button.setBackground(UITheme.PRIMARY_LIGHT);
            button.setForeground(Color.WHITE);
            button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
            button.setBorder(BorderFactory.createEmptyBorder(12, 5, 12, 5));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(85, 45));

            int index = i;
            button.addActionListener(e -> {
                if (parent instanceof JDialog dialog) {
                    dialog.dispose();
                }

                Window ancestor = SwingUtilities.getWindowAncestor(parent);
                SwingUI root = (ancestor instanceof SwingUI sw) ? sw :
                               (parent instanceof SwingUI sw) ? sw : null;

                if (root != null) {
                    switch (index) {
                        case 0 -> new HomeUI(root.getProfile(), root);
                        case 1 -> new TasksUI(root.getProfile(), root);
                        case 2 -> new PlanUI(root.getProfile(), root);
                        case 3 -> root.showSettingsMenu();
                    }
                } else {
                    System.err.println("⚠ Navigation failed — SwingUI parent not found.");
                }
            });

            navBar.add(button);
        }

        return navBar;
    } // End of method createNavigationBar


    // -------------------------------------------------------------------------
    // BASE WINDOW AND BOTTOM SECTION HELPERS
    // -------------------------------------------------------------------------

    /**
     * Creates a standard base dialog window styled for EconoMe’s mobile layout.
     *
     * @param title  the title of the screen
     * @param parent the parent window
     * @return a styled {@link JDialog} ready for content
     */
    public static JDialog createBaseScreen(String title, Window parent) {
        JDialog dialog = new JDialog(parent instanceof Frame ? (Frame) parent : null, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(360, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(UITheme.BACKGROUND);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        return dialog;
    } // End of method createBaseScreen


    /**
     * Creates a primary styled button used for key actions.
     *
     * @param text the button label
     * @return a styled {@link JButton}
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UITheme.PRIMARY_LIGHT);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    } // End of method createPrimaryButton


    /**
     * Creates a reusable bottom section combining an optional main action button
     * and the persistent navigation bar.
     *
     * @param parent      the parent window
     * @param actionButton an optional main action button (e.g., “Add Need”)
     * @param navActions   an array of navigation actions for each nav button
     * @return a combined bottom section {@link JPanel}
     */
    public static JPanel createBottomSection(Window parent, JButton actionButton, Runnable[] navActions) {
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        // Optional top action button
        if (actionButton != null) {
            JPanel addPanel = new JPanel(new BorderLayout());
            addPanel.setOpaque(false);
            addPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 8, 15));
            addPanel.add(actionButton, BorderLayout.CENTER);
            bottomWrapper.add(addPanel, BorderLayout.NORTH);
        }

        // Persistent bottom navigation bar
        bottomWrapper.add(createNavigationBar(parent, navActions), BorderLayout.SOUTH);
        return bottomWrapper;
    } // End of method createBottomSection
} // End of class SharedUI