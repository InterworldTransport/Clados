package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.interworldtransport.cladosG.BasisList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBasisListTest {

	BasisList tBasis0;
	BasisList tBasis4;
	BasisList tBasis42;
	BasisList tBasis43;
	BasisList tBasis8;
	BasisList tBasis10;
	BasisList tBasis14;

	@BeforeEach
	void setUp() throws Exception {
		tBasis0 = new BasisList((byte) 0);
		tBasis4 = new BasisList((byte) 4);
		// tBasis42 = new BasisList(tBasis4);
		tBasis43 = new BasisList((byte) 4);
		tBasis8 = new BasisList((byte) 8);
		tBasis10 = new BasisList((byte) 10);
		tBasis14 = new BasisList((byte) 14);
	}

	@Test
	public void testIndependence() {
		assertTrue(tBasis4.equals(tBasis43));
		// Using the raw constructor leaves both similar enough to pass equals test
		assertFalse(tBasis4 == tBasis43);
		// but not SO similar they are the same object. Hence desirability of caching.
	}

	@Test
	public void testBladeCount() {
		assertTrue(tBasis0.getBladeCount() == (1 << 0));
		assertTrue(tBasis4.getBladeCount() == (1 << 4));
		assertTrue(tBasis8.getBladeCount() == (1 << 8));
		assertTrue(tBasis10.getBladeCount() == (1 << 10));
		assertTrue(tBasis14.getBladeCount() == (1 << 14));
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
	public void testGradeRange() {
		ArrayList<Integer> tSpot;

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
	void testXMLOutput() {
		String xml = tBasis14.toXMLString("");
		System.out.println(xml);
		assertTrue(xml != null);
	}

}
