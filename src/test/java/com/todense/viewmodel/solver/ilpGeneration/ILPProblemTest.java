package com.todense.viewmodel.solver.ilpGeneration;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
                "\"minimize\":true}", jsonString);

    }

    @Test
    void getReverseColorMappingTest() {
        HashMap<Color, Integer> ciMap = new HashMap<>();
        ciMap.put(Color.web("0xff0000"), 0);
        ciMap.put(Color.web("0x00ff00"), 1);
        ciMap.put(Color.web("0x0000ff"), 2);
        ILPProblem ilpp = new ILPProblem();

        //even with no Color Mapping set the reverse Color Map should not be null;
        assertNotNull(ilpp.getReverseColorMapping());
        assertEquals(ilpp.getReverseColorMapping().isEmpty(), true);

        ilpp.setColorMapping(ciMap);
        HashMap<Integer, Color> reverseMap = ilpp.getReverseColorMapping();
        assertEquals(reverseMap.get(0).toString(), "0xff0000ff");
        assertEquals(reverseMap.get(1).toString(), "0x00ff00ff");
        assertEquals(reverseMap.get(2).toString(), "0x0000ffff");
    }
}