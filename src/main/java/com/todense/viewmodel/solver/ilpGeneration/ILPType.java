package com.todense.viewmodel.solver.ilpGeneration;

/**
 * specifies the type of ILP-Problem
 */
public enum ILPType {
    /**
     * MINCOLORS just minimizes the total amount of colors used in the total coloring ILP-Problem
     */
    MINCOLORS,
    /**
     * SIMILARCOLORS trys to use as many predefined colors as possible in the total coloring ILP-Problem
     */
    SIMILARCOLORS,
    /**
     * WITHSETCOLORS will definitely use the predefined colors in the total coloring ILP-Problem
     */
    WITHSETCOLORS,
    /**
     * WITHLOWCOLORS will try to maximize the use of low number colors in the total coloring ILP-Problem
     */
    WITHLOWCOLORS,



}
