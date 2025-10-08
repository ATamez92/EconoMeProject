package econome.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;

/**
 * SharedUI provides reusable UI helper methods for creating consistent
 * components across the EconoMe application.
 * 
 * This includes navigation bars, styled buttons, and rounded panels.
 */
public class SharedUI {

    /**
     * Creates a rounded panel with a given background color and corner radius.
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
    }
    
    /**
     * Creates a rounded button with smooth edges and custom colors.
     * Used for action buttons (e.g., "Add Need", "Mark Complete", "Save", etc.).
     * Navigation bar buttons remain square for consistency.
     */
    public static JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 15f)); // slightly larger text
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        // ✅ make the button a mobile-friendly size
        btn.setPreferredSize(new Dimension(140, 48)); // wider & taller
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18)); // generous padding

        // Custom rounded button paint
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = c.getWidth();
                int h = c.getHeight();

                // Background
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, w, h, 30, 30); // large curve radius

                // Subtle shadow/border
                g2.setColor(new Color(0, 0, 0, 25));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 30, 30);

                // Center text
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(btn.getText());
                int textHeight = fm.getAscent();
                int x = (w - textWidth) / 2;
                int y = (h + textHeight) / 2 - 3;
                g2.setColor(fg);
                g2.drawString(btn.getText(), x, y);
                g2.dispose();
            }
        });

        return btn;
    }



    /**
     * Creates a modern rounded card panel with a soft drop shadow.
     * The fixedHeightPx parameter defines the card height in pixels.
     */
    public static JPanel createCardPanel(int fixedHeightPx) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Soft shadow
                g2.setColor(new Color(0, 0, 0, 35));
                g2.fillRoundRect(3, 3, w - 6, h - 3, 20, 20);

                // Card background
                g2.setColor(UITheme.CARD_BACKGROUND);
                g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300, fixedHeightPx));
        panel.setMaximumSize(new Dimension(300, fixedHeightPx));
        panel.setMinimumSize(new Dimension(300, fixedHeightPx));
        panel.setLayout(new BorderLayout());
        return panel;
    }



    /**
     * Creates a standardized navigation button used in the bottom navigation bar.
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
    }

    /**
     * Creates a standard navigation bar with four buttons: Dash, Wants, Needs, Savings.
     */
    public static JPanel createNavigationBar(Window parent, Runnable[] actions) {
        JPanel navBar = new JPanel(new GridLayout(1, 4, 8, 0)); // small gap between buttons
        navBar.setBackground(UITheme.PRIMARY);
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // ✅ New button labels and icons
        String[] labels = {"Home", "Tasks", "Plan", "Settings"};
        String[] emojis = {"🏠", "📝", "📊", "⚙️"};

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(emojis[i] + " " + labels[i]);
            btn.setFocusPainted(false);
            btn.setBackground(UITheme.PRIMARY_LIGHT);
            btn.setForeground(Color.WHITE);
            btn.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f)); // larger font
            btn.setBorder(BorderFactory.createEmptyBorder(12, 5, 12, 5)); // extra padding
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 🚀 Ensures the text fits by giving each button a preferred width
            btn.setPreferredSize(new Dimension(85, 45)); // Wider buttons

            int index = i;
            btn.addActionListener(e -> {
                // Only close the current dialog, not the main app
                if (parent instanceof JDialog dialog) {
                    dialog.dispose();
                }

                Window topParent = SwingUtilities.getWindowAncestor(parent);
                SwingUI root = null;

                if (topParent instanceof SwingUI sw) {
                    root = sw;
                } else if (parent instanceof SwingUI sw) {
                    root = sw;
                }

                if (root != null) {
                    switch (index) {
                        case 0 -> new HomeUI(root.getProfile(), root);  // Home
                        case 1 -> new TasksUI(root.getProfile(), root); // Tasks
                        case 2 -> new PlanUI(root.getProfile(), root);  // Plan
                        case 3 -> root.showSettingsMenu();              // Settings
                    }
                } else {
                    System.err.println("⚠ Navigation failed — SwingUI parent not found.");
                }
            });

            navBar.add(btn);
        }

        return navBar;
    }
    
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
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UITheme.PRIMARY_LIGHT);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a bottom section that can include an optional main action button (like “Add Need”)
     * followed by the navigation bar.
     */
    public static JPanel createBottomSection(Window parent, JButton actionButton, Runnable[] navActions) {
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        if (actionButton != null) {
            JPanel addPanel = new JPanel(new BorderLayout());
            addPanel.setOpaque(false);
            addPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 8, 15));
            addPanel.add(actionButton, BorderLayout.CENTER);
            bottomWrapper.add(addPanel, BorderLayout.NORTH);
        }

        bottomWrapper.add(createNavigationBar(parent, navActions), BorderLayout.SOUTH);
        return bottomWrapper;
    }


}

