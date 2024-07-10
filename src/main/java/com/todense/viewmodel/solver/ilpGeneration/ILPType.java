package com.todense.viewmodel.solver.ilpGeneration;

import javafx.scene.paint.Color;

/**
 * specifies the type of ILP-Problem
 */
public enum ILPType {
    /**
     * MINCOLORS just minimizes the total amount of colors used in the total coloring ILP-Problem
     */
    MINCOLORS,
    /**
     * SIMILARCOLORS trys to use as many predefined colors as possible in the total coloring ILP-Problem
     */
    SIMILARCOLORS,
    /**
     * WITHSETCOLORS will definitely use the predefined colors in the total coloring ILP-Problem
     */
    WITHSETCOLORS,
    /**
     * WITHLOWCOLORS will try to maximize the use of low number colors in the total coloring ILP-Problem
     */
    WITHLOWCOLORS,
    /**
     * WITHLOWSETCOLORS will try to maximize the use of low number colors, while allowing
     * set colors, in the total coloring ILP-Problem (has to be set with maximizeColor
     */
    WITHLOWSETCOLORS;

    private Color maximizeColor;

    ILPType() {
        maximizeColor = null;
    }

    /**
     * Sets maximizeColor (only necessary for WITHLOWCOLORS
     * @param maximizeColor
     */
    public void setMaximizeColor(Color maximizeColor) {
        this.maximizeColor = maximizeColor;
    }

    /**
     * gives the maximizeColor
     * @return maximizeColor
     */
    public Color getMaximizeColor() {
        return maximizeColor;
    }

}
