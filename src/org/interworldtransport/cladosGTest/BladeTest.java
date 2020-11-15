package org.interworldtransport.cladosGTest;

import static org.junit.Assert.*;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.Before;
import org.junit.Test;

public class BladeTest {

	Blade tB0;
	Blade tB4;
	Blade tB42;
	Blade tB43;
	Blade tB8;
	Blade tB10;
	Blade tB14;

	@Before
	public void setUp() throws CladosMonadException, GeneratorRangeException {
		short[] j = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 10 };
		short[] k = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		short[] m = { 1, 2, 3, 4, 5, 6, 7, 8 };
		tB0 = new Blade((short) 0);
		tB4 = new Blade((short) 4);
		tB4.add(j[3]).add(j[0]).add(j[1]);
		tB42 = new Blade(tB4);
		tB43 = new Blade((short) 4);
		tB43.add(j[3]).add(j[1]);

		tB8 = new Blade((short) 8);
		tB8.add(m); // Should be pscalar now
		tB10 = new Blade((short) 10);
		tB10.add(k); // Should be pscalar now
		tB14 = new Blade((short) 14);
		tB14.add(j); // Should be pscalar now
	}
	
	@Test
	public void testBladeSize() throws GeneratorRangeException {
		assertTrue(tB0.key() == 0L && tB0.get().size() == 0);
		assertTrue(tB4.key() == 39L && tB4.get().size() == 3);
		assertTrue(tB42.key() == tB4.key());
		assertFalse(tB42.key() == tB43.key());

		assertTrue(tB8.key() == 6053444L && tB8.get().size() == 8);
		assertTrue(tB10.get().size() == 10);
		assertTrue(tB14.key() == 2234152501943159L && tB14.get().size() == 14);
		
		tB14.remove((short) 12);
		assertTrue(tB14.get().size() == 13);
		
		tB14.add((short) 10);	// generator already there, so silently ignore the add.
		assertTrue(tB14.get().size() == 13);
		
		tB14.add(Short.valueOf((short) 12));
		assertTrue(tB14.key() == 2234152501943159L && tB14.get().size() == 14);
		
	}
	
	@Test
	public void testLimitsIgnored() throws GeneratorRangeException {
		assertTrue(tB0.remove((short) 1).equals(tB0)); //tB is a scalar. Nothing to remove. Silent acceptance expected.
		assertTrue(tB10.add((short) 8).equals(tB10));
	}
	
	@Test(expected = GeneratorRangeException.class)
	public void testLimits() throws GeneratorRangeException {
		assertTrue(tB0.remove((short) 0) instanceof Blade);
		assertTrue(tB14.add((short) 16) instanceof Blade);
	}
	
	@Test
	public void testIndependence() {
		assertTrue(tB4.equals(tB42)); // Same inner meaning should pass equals test
		assertFalse(tB4 == tB42); // but they are not the same objects
	}

}
