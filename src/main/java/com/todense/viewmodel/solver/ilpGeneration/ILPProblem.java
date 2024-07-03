package com.todense.viewmodel.solver.ilpGeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        String[] variables = new String[this.variables.size()];
        String[] constraints = new String[this.constraints.size()];
        String optimizationFunction = optfunction.getAsString();
        boolean minimize = optfunction.isMinimize();
        for (int i = 0; i < variables.length; i++) {
            variables[i] = this.variables.get(i).getAsString();
        }
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = this.constraints.get(i).getAsString();
        }
        LinkedHashMap<String,Object> jsonMap = new LinkedHashMap<String,Object>();
        jsonMap.put("variables", variables);
        jsonMap.put("constraints", constraints);
        jsonMap.put("optimizationFunction", optimizationFunction);
        jsonMap.put("minimize", minimize);
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonMap); // "=" is just saved as \u003d and "<" as \u003c here

        jsonString = jsonString.replace("\\u003d", "="); // replacing \u003d with =
        jsonString = jsonString.replace("\\u003c", "<"); // replacing \u003c with <
        return jsonString;
    }

}
