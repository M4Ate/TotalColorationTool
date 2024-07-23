package com.todense.viewmodel.random;

public enum GeneratorModel {

    MAX_DEG("Maximum degree "),
    SIMILAR_GRAPH("Similar graph"),
    BARABASI_ALBERT("Barabási–Albert model"),
    ERDOS_RENYI("Erdős–Rényi model"),
    GEOMETRIC("Geometric"),
    GEOMETRIC_RANDOMIZED("Geometric Randomized"),
    ;

    private final String name;

    GeneratorModel(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
