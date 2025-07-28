/**
 * The {@code FontManager} class handles loading and managing custom fonts used throughout the GUI.
 * Fonts are loaded from the `/fonts/` resource directory and registered with the system's
 * {@code GraphicsEnvironment}. Font styles are also derived to default sizes.
 * 
 * @author Theodore Garcia
 * @author Ronin Zerna
 * @version 2.0
 */
package view;

import java.awt.*;
import java.io.InputStream;

/**
 * Static class for loading and accessing custom fonts used across the application.
 */
public class FontManager {
    public static Font KETCHUM;
    public static Font NEXA_EL;
    public static Font NEXA_H;

    // Static initializer block to load fonts at class load time
    static {
        try {
            InputStream isKetchum = FontManager.class.getResourceAsStream("/fonts/upheavtt.ttf");
            InputStream isNexaEL = FontManager.class.getResourceAsStream("/fonts/Nexa_ExtraLight.ttf");
            InputStream isNexaH = FontManager.class.getResourceAsStream("/fonts/small_pixel.ttf");

            if (isKetchum != null) {
                KETCHUM = Font.createFont(Font.TRUETYPE_FONT, isKetchum).deriveFont(28f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(KETCHUM);
            }

            if (isNexaEL != null) {
                NEXA_EL = Font.createFont(Font.TRUETYPE_FONT, isNexaEL).deriveFont(14f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(NEXA_EL);
            }

            if (isNexaH != null) {
                NEXA_H = Font.createFont(Font.TRUETYPE_FONT, isNexaH).deriveFont(14f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(NEXA_H);
            }

        } catch (Exception e) {
            System.err.println("Font loading failed: " + e.getMessage());
        }
    }
}
