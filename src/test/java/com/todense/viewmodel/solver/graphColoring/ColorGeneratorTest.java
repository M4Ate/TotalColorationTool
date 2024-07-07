package com.todense.viewmodel.solver.graphColoring;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorGeneratorTest {

    @Test
    void getUniqueColorTest() {
        ColorGenerator cg = new ColorGenerator();
        cg.addUsedColor(Color.web("0x00FF00"));
        Color c1 = cg.getUniqueColor();
        assertEquals(c1.toString(), "0xff00f6ff");
        Color c2 = cg.getUniqueColor();
        assertEquals(c2.toString(), "0x0076ffff");
    }
}