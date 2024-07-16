package com.todense.viewmodel.solver.ilpGeneration;

import java.util.List;

public class Optfunction {

    private List<Variable> varlist;
    private String optfunction;
    private boolean isMinimize;

    /**
     * creates an Opt-function with the specified opt-function String and the used variables
     *
     * @param vars list of used variables
     * @param optfunction optimization function as a String
     * @param isMinimize true if it is a minimization function, false if it is a maximization function
     */
    public Optfunction(List<Variable> vars, String optfunction, boolean isMinimize) {
        this.varlist = vars;
        this.optfunction = optfunction;
        this.isMinimize = isMinimize;
    }

    /**
     * gets the list of used variables
     *
     * @return variable list
     */
    public List<Variable> getVarlist(){
        return varlist;
    }

    /**
     * gets the optimization function as a String
     *
     * @return opt-function
     */
    public String getAsString(){
        return optfunction;
    }

    /**
     * indicates whether it is a min or max function
     *
     * @return true if it is a minimization function, false if it is a maximization function
     */
    public boolean isMinimize() {
        return isMinimize;
    }
}
