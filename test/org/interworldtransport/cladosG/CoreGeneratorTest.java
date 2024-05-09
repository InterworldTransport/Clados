package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class CoreGeneratorTest {
    public Generator tG1 = Generator.E1;
    public Generator tG4 = Generator.E4;
    public Generator tGC = Generator.EC;

    @Test
    public void testGeneratorExists() {
        Stream<Generator> tStream = Generator.stream();
        tStream.forEach(g -> assertTrue(g.ord <= CladosConstant.GENERATOR_MAX.ord));

        tStream = Generator.stream(tGC.ord);
        tStream.forEach(g -> assertTrue(g.ord <= 12));

        Generator testThis = Generator.get((byte) 12);
        assertTrue(testThis == tGC);

        testThis = Generator.get((byte) -12);
        assertTrue(testThis == null);

        testThis = Generator.get((byte) 120);
        assertTrue(testThis == null);

        testThis = Generator.get(Byte.valueOf((byte) 4));
        assertTrue(testThis == tG4);

        testThis = Generator.get(Byte.valueOf((byte) -4));
        assertTrue(testThis == null);

        testThis = Generator.get(Byte.valueOf((byte) 40));
        assertTrue(testThis == null);
    }

    @Test
    public void testStringExport() {
        assertTrue(tGC.toString() == "EC");
    }   
}