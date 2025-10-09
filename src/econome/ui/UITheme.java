package econome.ui;

import java.awt.*;

/**
 * Centralized style guide for all EconoMe Swing windows.
 * Use these constants instead of hard-coding colors or fonts.
 */
public class UITheme {
	// Card surfaces
	public static final Color CARD_BACKGROUND = new Color(250, 250, 250); // soft neutral
	public static final Color CARD_BORDER     = new Color(200, 200, 200); // subtle outline
	public static final Color ACTION_GREEN = new Color(102, 187, 106); // soft light green

    public static final Color BACKGROUND      = new Color(247, 249, 247);   // off-white
    public static final Color PRIMARY         = new Color(67, 160, 71);     // deep green
    public static final Color PRIMARY_LIGHT   = new Color(102, 187, 106);
    public static final Color ACCENT          = new Color(165, 214, 167);

    public static final Font  TITLE_FONT      = new Font("SansSerif", Font.BOLD, 22);
    public static final Font  SUBTITLE_FONT   = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font  BODY_FONT       = new Font("SansSerif", Font.PLAIN, 14);
}