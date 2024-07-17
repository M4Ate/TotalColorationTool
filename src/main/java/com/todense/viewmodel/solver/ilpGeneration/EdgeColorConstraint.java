package com.todense.viewmodel.solver.ilpGeneration;

/**
 * EdgeColorConstraint represents a constraint in an ILP-Problem and is used to ensure that any given edge has exactly
 * one color
 */
public class EdgeColorConstraint extends Constraint{

    public EdgeColorConstraint(String constraint) {
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
