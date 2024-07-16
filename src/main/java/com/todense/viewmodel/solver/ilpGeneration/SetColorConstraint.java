package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public class SetColorConstraint extends Constraint{

    public SetColorConstraint(String constraint) {
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
