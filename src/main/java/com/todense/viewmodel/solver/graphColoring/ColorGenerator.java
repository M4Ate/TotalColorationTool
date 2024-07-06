package com.todense.viewmodel.solver.graphColoring;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * ColorGenerator can be used to get calculate visually different Colors from a set of already used Colors
 */
public class ColorGenerator {

    private ArrayList<Color> usedColors;

    /**
     * crates a new ColorGenerator instance
     */
    public ColorGenerator() {
        usedColors = new ArrayList<Color>();
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


        return null;
    }

    /**
     * clears all the used Colors
     */
    public void clear(){
        usedColors.clear();
    }

}
