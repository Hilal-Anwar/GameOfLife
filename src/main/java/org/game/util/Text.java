package org.game.util;

import org.game.util.Colors;

public class Text {
    public static String getColorText(String text, Colors colors){
        return colors.getColor()+text+"\33[0m";
    }
}
