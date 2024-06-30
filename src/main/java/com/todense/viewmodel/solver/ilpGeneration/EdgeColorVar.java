package com.todense.viewmodel.solver.ilpGeneration;

public class EdgeColorVar extends Variable{

    private String edgeId;

    /**
     * creates a edge color variable with the given name and edge id of the corresponding edge
     *
     * @param name name of the variable
     * @param edgeId edge id
     */
    public EdgeColorVar(String name, String edgeId){
        this.edgeId = edgeId;
        super.name = name;
    }

    /**
     * gets the edge id of the corresponding edge
     *
     * @return edge id
     */
    public String getEdgeId() {
        return edgeId;
    }

    /**
     * gets the variable name as a String
     *
     * @return name of variable
     */
    @Override
    public String getAsString() {
        return name;
    }
}
