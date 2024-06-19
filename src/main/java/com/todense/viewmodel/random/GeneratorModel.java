package com.todense.viewmodel.random;

public enum GeneratorModel {

    MAX_DEG("Maximum degree generator"),
    MIN_DIST("Minimum distance generator"),
    GEOMETRIC("Geometric"),
    GEOMETRIC_RANDOMIZED("Geometric Randomized"),
    ERDOS_RENYI("Erdős–Rényi model"),
    BARABASI_ALBERT("Barabási–Albert model"),
    ;

    private final String name;

    GeneratorModel(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
