package econome.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Simple donut chart with segment allocations. If total allocation is 0,
 * renders a neutral base ring so the chart is never invisible.
 */
public class DonutChartPanel extends JPanel {
    private final double[] allocations; // fractions 0..1
    private final double[] progress;    // optional per-segment 0..1 (unused for slice size; can be used for labels)
    private final Color[] colors;
    private final String[] labels;

    private static final int THICKNESS = 24; // ring thickness

    public DonutChartPanel(double[] allocations, double[] progress, Color[] colors, String[] labels) {
        this.allocations = allocations != null ? allocations.clone() : new double[]{};
        this.progress    = progress    != null ? progress.clone()    : new double[]{};
        this.colors      = colors      != null ? colors.clone()      : new Color[]{Color.GRAY};
        this.labels      = labels      != null ? labels.clone()      : new String[]{};
        setOpaque(false);
        setPreferredSize(new Dimension(260, 260)); // ensure we get some space
        setMinimumSize(new Dimension(220, 220));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (allocations.length == 0 || colors.length == 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int outer = (int) (size * 0.80);           // overall diameter
        int x = (getWidth() - outer) / 2;
        int y = (getHeight() - outer) / 2;

        // Base ring background (always visible)
        g2.setStroke(new BasicStroke(THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(220, 225, 220));     // neutral light gray-green
        g2.drawOval(x, y, outer, outer);

        // Total allocation
        double total = 0.0;
        for (double v : allocations) total += Math.max(0, v);

        if (total > 0) {
            double start = 90.0; // start at top
            for (int i = 0; i < allocations.length; i++) {
                double frac = Math.max(0, allocations[i]) / total;
                if (frac <= 0) continue;
                double sweep = frac * 360.0;

                g2.setColor(colors[i % colors.length]);
                g2.drawArc(x, y, outer, outer, (int) -start, (int) -sweep);

                start += sweep;
            }
        }

        // Center label: show total allocated percent (sum of allocations * 100)
        // (If you prefer something else, replace this with any text)
        int percent = (int) Math.round(total * 100);
        String center = percent + "%";
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(center)) / 2;
        int ty = (getHeight() + fm.getAscent()) / 2 - 4;
        g2.setColor(new Color(40, 40, 40));
        g2.drawString(center, tx, ty);

        g2.dispose();
    }
}
