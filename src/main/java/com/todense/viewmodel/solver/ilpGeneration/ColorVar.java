package com.todense.viewmodel.solver.ilpGeneration;

/**
 * ColorVar represents a variable in an ILP-Problem and is supposed to be set to 1 if that color is used and 0 if the
 * color isn't used in a coloration
 */
public class ColorVar extends Variable{

    /**
     * creates a color variable with the given name
     *
     * @param name name of the variable
     */
    public ColorVar(String name) {
        super.name = name;
    }

    /**
     * gets the variable name as a String
     *
     * @return name of variable
     */
    @Override
    public String getAsString() {
        return name;
    }
}
