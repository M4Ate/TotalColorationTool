package com.todense.viewmodel.solver.ilpGeneration;

/**
 * VertexColorVar represents a variable in an ILP-Problem and is used to indicate the coloring of a vertex/node
 */
public class VertexColorVar extends Variable{

    private int vertexId;

    /**
     * creates a vertex color variable with the given name and vertex id of the corresponding vertex
     *
     * @param name name of the variable
     * @param vertexId vertex id
     */
    public VertexColorVar(String name, int vertexId){
        this.vertexId = vertexId;
        super.name = name;
    }

    /**
     * gets the vertex id of the corresponding vertex
     *
     * @return vertex id
     */
    public int getVertexId() {
        return vertexId;
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
