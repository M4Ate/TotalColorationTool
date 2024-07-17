package com.todense.viewmodel.solver.ilpGeneration;

/**
 * NodeColorConstraint represents a constraint in an ILP-Problem and is used to ensure that any given node has exactly
 * one color
 */
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
