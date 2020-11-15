package org.interworldtransport.cladosGTest;

import org.interworldtransport.cladosG.Basis;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class BasisTest {
	Basis tBasis0;
	Basis tBasis4;
	Basis tBasis42;
	Basis tBasis43;
	Basis tBasis8;
	Basis tBasis10;
	Basis tBasis14;

	@Before
	public void setUp() throws CladosMonadException, GeneratorRangeException {
		tBasis0 = new Basis((short) 0);
		tBasis4 = new Basis((short) 4);
		tBasis42 = new Basis(tBasis4);
		tBasis43 = new Basis((short) 4);
		tBasis8 = new Basis((short) 8);
		tBasis10 = new Basis((short) 10);
		tBasis14 = new Basis((short) 14);
	}

	@Test
	public void testGradeCount() {
		assertTrue(tBasis0.getGradeCount() == 1);
		assertTrue(tBasis4.getGradeCount() == 5);
		assertTrue(tBasis8.getGradeCount() == 9);
		assertTrue(tBasis10.getGradeCount() == 11);
		assertTrue(tBasis14.getGradeCount() == 15);
	}

	@Test
	public void testBladeCount() {
		assertTrue(tBasis0.getBladeCount() == Math.pow(2, 0));
		assertTrue(tBasis4.getBladeCount() == Math.pow(2, 4));
		assertTrue(tBasis8.getBladeCount() == Math.pow(2, 8));
		assertTrue(tBasis10.getBladeCount() == Math.pow(2, 10));
		assertTrue(tBasis14.getBladeCount() == Math.pow(2, 14));
	}

	@Test
	public void testGradeRange() {
		ArrayList<Short> tSpot;

		tSpot = tBasis0.getGrades();
		assertTrue(tSpot.get(0) == 0);

		tSpot = tBasis4.getGrades();
		for (int k = 1; k < 0.5 * (tBasis4.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(4 - k + 1) - tSpot.get(4 - k));

		tSpot = tBasis8.getGrades();
		for (int k = 1; k < 0.5 * (tBasis8.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(8 - k + 1) - tSpot.get(8 - k));

		tSpot = tBasis10.getGrades();
		for (int k = 1; k < 0.5 * (tBasis10.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(10 - k + 1) - tSpot.get(10 - k));

		tSpot = tBasis14.getGrades();
		for (int k = 1; k < 0.5 * (tBasis14.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(14 - k + 1) - tSpot.get(14 - k));

	}

	@Test
	public void testIndependence() {
		assertTrue(tBasis4.getBasis().equals(tBasis42.getBasis()));
		// Using the copy constructor results in both Basis objects sharing vBasis
		assertFalse(tBasis4.getBasis().equals(tBasis43.getBasis()));
		// Using the raw constructor results in both Basis object not sharing a vBasis.
	}
}
