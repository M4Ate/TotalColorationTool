package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public class NodeColorConstraint extends Constraint{

    public NodeColorConstraint(String constraint) {
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

}
