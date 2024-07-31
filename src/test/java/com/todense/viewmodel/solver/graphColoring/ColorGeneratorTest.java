package com.todense.viewmodel.solver.graphColoring;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void getUniqueColorWithSetPalletTest() {
        List<Color> cl = new ArrayList<>();
        cl.add(Color.web("0xff0000"));
        cl.add(Color.web("0x00ff00"));
        cl.add(Color.web("0x0000ff"));
        ColorGenerator cg = new ColorGenerator(cl);
        Color c1 = cg.getUniqueColor();
        Color c2 = cg.getUniqueColor();
        assertEquals(c1.toString(), "0xff0000ff");
        assertEquals(c2.toString(), "0x0000ffff");
        cg.clear();
        Color c3 = cg.getUniqueColor();
        assertEquals(c3.toString(), "0xff0000ff");
    }
}