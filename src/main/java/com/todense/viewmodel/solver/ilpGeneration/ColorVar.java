package com.todense.viewmodel.solver.ilpGeneration;

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
