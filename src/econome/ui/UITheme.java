package econome.ui;

import java.awt.*;

/**
 * {@code UITheme} defines all shared colors and fonts used across
 * the EconoMe application.
 * <p>
 * This centralizes styling so that visual changes (colors, fonts, tone)
 * can be updated consistently across all UI classes without modifying
 * individual components.
 * </p>
 *
 * <h3>Usage Guidelines:</h3>
 * <ul>
 *   <li>Never hard-code colors or fonts directly in UI classes.</li>
 *   <li>Reference these constants for a cohesive, consistent style.</li>
 *   <li>Colors follow a soft green palette to evoke calm and clarity.</li>
 * </ul>
 */
public class UITheme {

    // ---------------------------------------------------------------------
    // COLOR PALETTE
    // ---------------------------------------------------------------------

    /** Window background — soft off-white tone for minimal contrast. */
    public static final Color BACKGROUND = new Color(247, 249, 247);

    /** Primary accent — deep green used for headers, nav bars, and emphasis. */
    public static final Color PRIMARY = new Color(67, 160, 71);

    /** Lighter shade of primary green — used for buttons and highlights. */
    public static final Color PRIMARY_LIGHT = new Color(102, 187, 106);

    /** Secondary accent — light mint green used for borders and passive states. */
    public static final Color ACCENT = new Color(165, 214, 167);

    /** Background color for content cards and panels. */
    public static final Color CARD_BACKGROUND = new Color(250, 250, 250);

    /** Border color for cards and container outlines. */
    public static final Color CARD_BORDER = new Color(200, 200, 200);

    /** Action-oriented green (e.g., Add or Save buttons). */
    public static final Color ACTION_GREEN = new Color(102, 187, 106);


    // ---------------------------------------------------------------------
    // TYPOGRAPHY
    // ---------------------------------------------------------------------

    /** Large bold font for titles and screen headers. */
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);

    /** Medium text for subtitles or section headers. */
    public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /** Standard text for body labels and general UI elements. */
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
} // End of class UITheme