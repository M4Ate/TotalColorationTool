package com.todense.viewmodel.solver.ilpGeneration;

/**
 * AdjacentEdgeConstraint represents a constraint in an ILP-Problem and is used to ensure that any node/vertex and all
 * of its adjacent edges have different colors
 */
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
