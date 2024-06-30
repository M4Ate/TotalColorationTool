package com.todense.viewmodel.solver.ilpGeneration;

public abstract class Variable {

    protected String name;

    /**
     * gets the variable name as a String
     *
     * @return name of variable
     */
    public abstract String getAsString();

}
