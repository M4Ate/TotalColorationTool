package com.todense.viewmodel.solver.ilpGeneration;

/**
 * Constraint represents a constraint in an ILP-Problem
 */
public abstract class Constraint {

    protected String constraint;

    /**
     * gets the Constraint as a String
     *
     * @return constraint String
     */
    public abstract String getAsString();


}
