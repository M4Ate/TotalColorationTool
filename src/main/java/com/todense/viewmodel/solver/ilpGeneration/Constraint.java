package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public abstract class Constraint {

    protected List<Variable> varList;
    protected String constraint;

    /**
     * gets the Constraint as a String
     *
     * @return constraint String
     */
    public abstract String getAsString();

    /**
     * gets the variable list of used variables in this constraint
     *
     * @return variable list
     */
    public abstract List<Variable> getVariableList();

}
