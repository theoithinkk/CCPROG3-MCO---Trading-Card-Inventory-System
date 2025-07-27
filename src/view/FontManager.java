package view;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
    public static Font KETCHUM;
    public static Font NEXA_EL;
    public static Font NEXA_H;

    static {
        try {
            InputStream isKetchum = FontManager.class.getResourceAsStream("/fonts/Ketchum.otf");
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
