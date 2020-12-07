package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBladeTest {

	private Generator[] g = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	private Blade tB0;
	private Blade tB4;
	private Blade tB42;
	private Blade tB43;

	@BeforeEach
	void setUp() throws Exception {
		tB0 = new Blade((byte) 0);
		tB4 = new Blade((byte) 4);
		tB4.add(g[0]).add(g[1]);
		tB42 = new Blade(tB4);
		tB42.add(g[3]).add(g[0]).add(g[1]);
		tB43 = new Blade((byte) 4);
		tB43.add(g[1]).add(g[0]);
	}

	@Test
	public void testIndependence() {
		assertTrue(tB4.equals(tB43)); // Same inner meaning should pass equals test
		assertFalse(tB4 == tB43); // but they are not the same objects
	}

	@Test
	public void testBladeSize() throws GeneratorRangeException {

		assertTrue(tB0.getGenerators().size() == 0);
		assertTrue(tB4.getGenerators().size() == 2);
		assertTrue(tB43.key() == tB4.key());
		assertFalse(tB42.key() == tB43.key());

		Blade tB8 = new Blade((byte) 8);
		Generator.flow((byte) 8).forEach(g-> tB8.add(g));
		Blade tB10 = new Blade((byte) 10);
		Generator.flow((byte) 10).forEach(g-> tB10.add(g));
		Blade tB15 = new Blade((byte) 14);
		Generator.flow((byte) 14).forEach(g-> tB15.add(g));


		assertTrue(tB8.getGenerators().size() == 8);
		assertTrue(tB10.getGenerators().size() == 10);
		assertTrue(tB15.getGenerators().size() == 14);

		tB15.remove((byte) 12);
		assertTrue(tB15.getGenerators().size() == 13);

		tB15.add((byte) 10); // generator already there, so silently ignore the add.
		assertTrue(tB15.getGenerators().size() == 13);

		tB15.add(Byte.valueOf((byte) 12));
		assertTrue(tB15.getGenerators().size() == 14);

	}

	@Test
	public void testLimitsIgnored() throws GeneratorRangeException {
		Blade newtB0 = new Blade(tB0);
		newtB0.remove(Generator.E1); // Should silently fail since E1 isn't in there.
		assertTrue(newtB0.equals(tB0)); // tB is a scalar. Nothing to remove. Silent acceptance expected.
		Blade tB10 = new Blade((byte) 10);
		Generator.flow((byte) 10).forEach(g-> tB10.add(g));
		Blade newtB10 = new Blade(tB10);
		newtB10.add(Generator.E8); // Should be silently ignored since E8 is in there.
		assertTrue(newtB10.equals(tB10));
	}

	@Test
	public void testLowGeneratorLimit() throws GeneratorRangeException {
		Assertions.assertThrows(GeneratorRangeException.class, () -> tB0.remove((byte) 0));
	}

	@Test
	public void testHighGeneratorLimit() throws GeneratorRangeException {
		Assertions.assertThrows(GeneratorRangeException.class, () -> tB0.remove((byte) 16));
	}

	@Test
	public void testXMLOutput() throws GeneratorRangeException {
		Blade tB = new Blade((byte) 14);
		Generator.flow((byte) 14).forEach(g-> tB.add(g));
		System.out.println(Blade.toXMLString(tB,""));
	}

}
