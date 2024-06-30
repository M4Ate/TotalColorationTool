package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public class EdgeColorConstraint extends Constraint{

    public EdgeColorConstraint(List<Variable> varList, String constraint) {
        super.varList = varList;
        super.constraint = constraint;
    }

    /**
     * gets the Constraint as a String
     *
     * @return constraint String
     */
    @Override
    public String getAsString() {
        return constraint;
    }

    /**
     * gets the variable list of used variables in this constraint
     *
     * @return variable list
     */
    @Override
    public List<Variable> getVariableList() {
        return varList;
    }
}
