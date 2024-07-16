package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public class AdjacentEdgeConstraint extends Constraint{

    public AdjacentEdgeConstraint(String constraint) {
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
