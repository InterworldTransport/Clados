package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBladeTest {
	private short[] j = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 10 };
	private short[] k = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private short[] m = { 1, 2, 3, 4, 5, 6, 7, 8 };
	private Generator[] g = {Generator.E1, Generator.E2, Generator.E3, Generator.E4};
	private Blade tB0;
	private Blade tB4;
	private Blade tB42;
	private Blade tB43;

	@BeforeEach
	void setUp() throws Exception {
		
		tB0 = new Blade((short) 0);
		tB4 = new Blade((short) 4);
		tB4.addSort(g[0]).addSort(g[1]);
		tB42 = new Blade(tB4);
		tB42.addSort(g[3]).addSort(g[0]).addSort(g[1]);
		tB43 = new Blade((short) 4);
		tB43.addSort(g[1]).addSort(g[0]);
	}

	@Test
	public void testIndependence() {
		assertFalse(tB4.equals(tB43)); // Same inner meaning should pass equals test
		assertFalse(tB4 == tB43); // but they are not the same objects
	}

	@Test
	public void testBladeSize() throws GeneratorRangeException {

		assertTrue(tB0.key() == 0L && tB0.get().size() == 0);
		assertTrue(tB4.key() == 7L && tB4.get().size() == 2);
		assertTrue(tB43.key() == tB4.key());
		assertFalse(tB42.key() == tB43.key());
		
		Blade tB8 = new Blade((short) 8);
		tB8.addSort(m); // Should be pscalar now
		Blade tB10 = new Blade((short) 10);
		tB10.addSort(k); // Should be pscalar now
		Blade tB14 = new Blade((short) 14);
		tB14.addSort(j); // Should be pscalar now

		assertTrue(tB8.key() == 6053444L && tB8.get().size() == 8);
		assertTrue(tB10.get().size() == 10);
		assertTrue(tB14.key() == 2234152501943159L && tB14.get().size() == 14);

		tB14.remove((short) 12);
		assertTrue(tB14.get().size() == 13);

		tB14.addSort((short) 10); // generator already there, so silently ignore the add.
		assertTrue(tB14.get().size() == 13);

		tB14.addSort(Short.valueOf((short) 12));
		assertTrue(tB14.key() == 2234152501943159L && tB14.get().size() == 14);

	}

	@Test
	public void testLimitsIgnored() throws GeneratorRangeException {
		Blade newtB0 = new Blade(tB0);
		newtB0.remove(Generator.E1); // Should silently fail since E1 isn't in there. 
		assertTrue(newtB0.equals(tB0)); // tB is a scalar. Nothing to remove. Silent acceptance expected.
		Blade tB10 = new Blade((short) 10);
		tB10.addSort(k); // Should be pscalar now
		Blade newtB10 = new Blade(tB10);
		newtB10.addSort(Generator.E8); // Should be silently ignored since E8 is in there.
		assertTrue(newtB10.equals(tB10));
	}

	@Test
	public void testLowGeneratorLimit() throws GeneratorRangeException {
		Assertions.assertThrows(GeneratorRangeException.class, () -> tB0.remove((short) 0));
	}
	
	@Test
	public void testHighGeneratorLimit() throws GeneratorRangeException {
		Assertions.assertThrows(GeneratorRangeException.class, () -> tB0.remove((short) 16));
	}
	

}
