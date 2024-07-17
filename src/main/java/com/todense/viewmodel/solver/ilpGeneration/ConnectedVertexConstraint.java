package com.todense.viewmodel.solver.ilpGeneration;

/**
 * ConnectedVertexConstraint represents a constraint in an ILP-Problem and is used to ensure that connected nodes/
 * vertices have different colors
 */
public class ConnectedVertexConstraint extends Constraint{

    public ConnectedVertexConstraint(String constraint) {
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
