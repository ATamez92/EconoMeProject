package econome.ui;

import javax.swing.*;
import java.awt.*;

/**
 * A custom Swing component that renders a donut-style chart for visualizing
 * allocation distributions (e.g., Needs, Wants, Savings) in the EconoMe app.
 * <p>
 * Each allocation segment is drawn as a colored ring section proportional
 * to its value. A neutral background ring is always displayed, ensuring
 * visibility even when no allocations exist.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Render a circular donut chart with proportional segments.</li>
 *   <li>Display a central label showing the total allocation percentage.</li>
 *   <li>Provide visual consistency with the EconoMe UI theme.</li>
 * </ul>
 */
public class DonutChartPanel extends JPanel {

    // --- Chart Data ---
    private final double[] allocations; // Fractions between 0 and 1 for each segment
    private final double[] progress;    // Optional: progress indicators (currently unused)
    private final Color[] colors;       // Colors for each segment
    private final String[] labels;      // Labels for each segment (currently unused)

    // --- Visual Constants ---
    private static final int RING_THICKNESS = 24; // Thickness of the donut ring
    private static final Color BASE_RING_COLOR = new Color(220, 225, 220); // Light gray-green

    /**
     * Constructs a new {@code DonutChartPanel}.
     *
     * @param allocations fractional values (0..1) for each segment
     * @param progress    optional progress values per segment (unused)
     * @param colors      color array corresponding to each segment
     * @param labels      label array corresponding to each segment
     */
    public DonutChartPanel(double[] allocations, double[] progress, Color[] colors, String[] labels) {
        this.allocations = allocations != null ? allocations.clone() : new double[]{};
        this.progress = progress != null ? progress.clone() : new double[]{};
        this.colors = colors != null ? colors.clone() : new Color[]{Color.GRAY};
        this.labels = labels != null ? labels.clone() : new String[]{};

        setOpaque(false);
        setPreferredSize(new Dimension(260, 260));
        setMinimumSize(new Dimension(220, 220));
    } // End of constructor DonutChartPanel

    /**
     * Renders the donut chart visualization.
     * Called automatically by the Swing paint system when the panel
     * is displayed or updated.
     *
     * @param g the {@link Graphics} context used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ensure there is valid data to draw
        if (allocations.length == 0 || colors.length == 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int outerDiameter = (int) (size * 0.80);
        int x = (getWidth() - outerDiameter) / 2;
        int y = (getHeight() - outerDiameter) / 2;

        // --- Base Ring (background) ---
        g2.setStroke(new BasicStroke(RING_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(BASE_RING_COLOR);
        g2.drawOval(x, y, outerDiameter, outerDiameter);

        // --- Calculate total allocation ---
        double total = 0.0;
        for (double value : allocations) {
            total += Math.max(0, value);
        }

        // --- Draw segments if there’s data ---
        if (total > 0) {
            double startAngle = 90.0; // Start at top of circle
            for (int i = 0; i < allocations.length; i++) {
                double fraction = Math.max(0, allocations[i]) / total;
                if (fraction <= 0) continue;

                double sweepAngle = fraction * 360.0;
                g2.setColor(colors[i % colors.length]);
                g2.drawArc(x, y, outerDiameter, outerDiameter, (int) -startAngle, (int) -sweepAngle);
                startAngle += sweepAngle;
            }
        }

        // --- Draw Center Label (total percentage) ---
        int percent = (int) Math.round(total * 100);
        String label = percent + "%";
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));

        FontMetrics metrics = g2.getFontMetrics();
        int textX = (getWidth() - metrics.stringWidth(label)) / 2;
        int textY = (getHeight() + metrics.getAscent()) / 2 - 4;

        g2.setColor(new Color(40, 40, 40));
        g2.drawString(label, textX, textY);

        g2.dispose();
    } // End of method paintComponent
} // End of class DonutChartPanel