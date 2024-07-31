package com.todense.viewmodel.solver.ilpGeneration;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptfunctionTest {

    @Test
    void optfuntionTest() {
        VertexColorVar vcv = new VertexColorVar("x_v0_c0", 0);
        EdgeColorVar ecv = new EdgeColorVar("y_e0_c0", "0-1");
        ColorVar cv = new ColorVar("z_c0");
        String optString = vcv.getAsString() + " + " + ecv.getAsString() + " + " + cv.getAsString();
        List<Variable> actualVarList = new ArrayList<Variable>();
        actualVarList.add(vcv);
        actualVarList.add(ecv);
        actualVarList.add(cv);
        Optfunction optf = new Optfunction(actualVarList, optString, true);
        assertEquals(optf.getAsString(), optString);
        List<Variable> varList = optf.getVarlist();
        assertEquals(varList.size(), actualVarList.size());
        for (int i = 0; i < varList.size(); i++) {
            assertEquals(varList.get(i).getAsString(), actualVarList.get(i).getAsString());
        }
    }

}