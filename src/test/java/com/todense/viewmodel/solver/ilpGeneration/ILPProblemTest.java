package com.todense.viewmodel.solver.ilpGeneration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ILPProblemTest {

    @Test
    void getILPAsJsonStringTest() {
        //the following is not intended as a useful ILP-Problem
        ILPProblem ilpp = new ILPProblem();
        VertexColorVar v0 = new VertexColorVar("x_v0_c0", 0);
        VertexColorVar v1 = new VertexColorVar("x_v1_c0", 1);
        ColorVar c0 = new ColorVar("z_c0");
        ColorVar c1 = new ColorVar("z_c1");
        ilpp.addVariable(v0);
        ilpp.addVariable(v1);
        ilpp.addVariable(c0);
        ilpp.addVariable(c1);

        List<Variable> vars = ilpp.getVarList();
        Optfunction opt = new Optfunction(vars, "z_c0 + z_c1", true);
        ilpp.setOptfunction(opt);

        NodeColorConstraint ncc = new NodeColorConstraint("x_v0_c0 + x_v1_c0 = 1");
        SetColorConstraint scc = new SetColorConstraint("xv0_c0 = 1");
        ilpp.addConstraint(ncc);
        ilpp.addConstraint(scc);

        String jsonString = ilpp.getILPAsJsonString();

        assertEquals("{\"variables\":[\"x_v0_c0\",\"x_v1_c0\",\"z_c0\",\"z_c1\"]," +
                "\"constraints\":[\"x_v0_c0 + x_v1_c0 = 1\",\"xv0_c0 = 1\"]," +
                "\"optimizationFunction\":\"z_c0 + z_c1\"," +
                "\"minimize\":false}", jsonString);

    }
}