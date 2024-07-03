package com.todense.viewmodel.random;

public enum GeneratorModel {

    BARABASI_ALBERT("Barabási–Albert model"),
    ERDOS_RENYI("Erdős–Rényi model"),
    GEOMETRIC("Geometric"),
    GEOMETRIC_RANDOMIZED("Geometric Randomized"),
    MAX_DEG("Maximum degree "),
    MIN_DIST("Minimum distance"),
    SIMILAR_GRAPH("Similar graph"),
    ;

    private final String name;

    GeneratorModel(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
