package org.vertexarmy.hermes.core.gui;

import java.awt.Font;

/**
 * User: Alex
 * Date: 12/23/13
 */
public class Toolkit {

    public static Font createFont(String[] familyNames, int style, int size) {
        for (String familyName : familyNames) {
            Font font = new Font(familyName, style, size);
            if (font.getFamily().equals(familyName)) {
                return font;
            }
        }

        return new Font(Font.DIALOG, style, size);
    }
}
