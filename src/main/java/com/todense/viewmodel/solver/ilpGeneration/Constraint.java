package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public abstract class Constraint {

    protected String constraint;

    /**
     * gets the Constraint as a String
     *
     * @return constraint String
     */
    public abstract String getAsString();


}
