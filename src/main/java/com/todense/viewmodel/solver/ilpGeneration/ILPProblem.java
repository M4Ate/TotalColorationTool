package com.todense.viewmodel.solver.ilpGeneration;

import java.util.ArrayList;
import java.util.List;

public class ILPProblem {

    private List<Variable> variables;
    private List<Constraint> constraints;
    private Optfunction optfunction;

    /**
     * creates a new empty ILP-Problem
     */
    public ILPProblem() {
        variables = new ArrayList<Variable>();
        constraints = new ArrayList<Constraint>();
        optfunction = null;
    }

    /**
     * adds a Constraint to the ILP-Problem
     *
     * @param constraint the constraint that is added
     */
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    /**
     * adds a Variable to the ILP-Problem
     *
     * @param var the variable that is added
     */
    public void addVariable(Variable var) {
        variables.add(var);
    }

    /**
     * sets the optimization function (there can always be just 1 optimization function for a given ILP-Problem
     *
     * @param opt optimization function
     */
    public void setOptfunction(Optfunction opt) {
        optfunction = opt;
    }

    /**
     * gets the List of current variables
     *
     * @return list of variables
     */
    public List<Variable> getVarList() {
        return variables;
    }

    /**
     * gets the current list of constraints
     *
     * @return list of constraints
     */
    public List<Constraint> getConstraintList() {
        return constraints;
    }

    /**
     * gets the entire ILP-Problem formatted as a JSON in a String
     *
     * @return JSON as a String
     */
    public String getILPAsJsonString() {
        // to be implemented
        return "";
    }

}
