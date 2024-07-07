package com.todense.viewmodel.solver.graphColoring;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * ColorGenerator can be used to get calculate visually different Colors from a set of already used Colors
 */
public class ColorGenerator {

    private static final String[] DEFAULT_COLOR_PALETTE = {"0x00FF00","0x0000FF","0xFF0000","0x01FFFE",
            "0xFFA6FE","0xFFDB66","0x006401","0x010067","0x95003A","0x007DB5","0xFF00F6","0xFFEEE8","0x774D00",
            "0x90FB92","0x0076FF","0xD5FF00","0xFF937E","0x6A826C","0xFF029D","0xFE8900","0x7A4782","0x7E2DD2",
            "0x85A900","0xFF0056","0xA42400","0x00AE7E","0x683D3B","0xBDC6FF","0x263400","0xBDD393","0x00B917",
            "0x9E008E","0x001544","0xC28C9F","0xFF74A3","0x01D0FF","0x004754","0xE56FFE","0x788231","0x0E4CA1",
            "0x91D0CB","0xBE9970","0x968AE8","0xBB8800","0x43002C","0xDEFF74","0x00FFC6","0xFFE502","0x620E00",
            "0x008F9C","0x98FF52","0x7544B1","0xB500FF","0x00FF78","0xFF6E41","0x005F39","0x6B6882","0x5FAD4E",
            "0xA75740","0xA5FFD2","0xFFB167","0xE85EBE","0x009BFF","0x000000"};

    private ArrayList<Color> usedColors;
    private ArrayList<Color> colorPalette;

    /**
     * crates a new ColorGenerator instance with a default Color Palette containing 64 Colors
     */
    public ColorGenerator() {
        usedColors = new ArrayList<Color>();
        colorPalette = new ArrayList<Color>();
        for (String c : DEFAULT_COLOR_PALETTE) {
            colorPalette.add(Color.web(c));
        }
    }

    /**
     * crates a new ColorGenerator instance with a specified Color Palette
     */
    public ColorGenerator(List<Color> palette) {
        usedColors = new ArrayList<Color>();
        colorPalette = new ArrayList<Color>();
        colorPalette.addAll(palette);
    }

    /**
     * adds a Color that is already in use, so that getUniqueColor() will try to not pick a Color that is too close to it
     *
     * @param color color that is already in use
     */
    public void addUsedColor(Color color) {
        usedColors.add(color);
    }

    /**
     * Adds a color that is as visually different as possible from the already used colors
     *
     * @return different Color;
     */
    public Color getUniqueColor(){
        if (usedColors.size() == 0) {
            return colorPalette.get(0);
        }
        double maxDistance = 0.0;
        Color maxDistanceColor = null;
        for (int c = 0; c < colorPalette.size(); c++) {
            double distance = Double.MAX_VALUE;
            for (int u = 0; u < usedColors.size(); u++) {
                double checkDist = getColorDistanceSquared(colorPalette.get(c), usedColors.get(u));
                if(checkDist <= distance){
                    distance = checkDist;
                }
            }
            if (distance >= maxDistance) {
                maxDistance = distance;
                maxDistanceColor = colorPalette.get(c);
            }
        }
        addUsedColor(maxDistanceColor);
        return maxDistanceColor;
    }

    /**
     * clears all the used Colors
     */
    public void clear(){
        usedColors.clear();
    }

    private double getColorDistanceSquared(Color c1, Color c2) {
        double distanceRed = (c2.getRed() - c1.getRed()) * (c2.getRed() - c1.getRed());
        double distanceGreen = (c2.getGreen() - c1.getGreen()) * (c2.getGreen() - c1.getGreen());
        double distanceBlue = (c2.getBlue() - c1.getBlue()) * (c2.getBlue() - c1.getBlue());
        return distanceRed + distanceGreen + distanceBlue;
    }

}
