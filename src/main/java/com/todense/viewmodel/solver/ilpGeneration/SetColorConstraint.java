package com.todense.viewmodel.solver.ilpGeneration;

/**
 * SetColorConstraint represents a constraint in an ILP-Problem and is used to set the color variables if the
 * corresponding color is used anywhere in the coloration
 */
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
