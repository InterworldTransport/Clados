package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Assertions;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBasisTest {

	CanonicalBasis tBasis0;
	CanonicalBasis tBasis1;
	CanonicalBasis tBasis4;
	CanonicalBasis tBasis43;
	CanonicalBasis tBasis8;
	CanonicalBasis tBasis10;
	CanonicalBasis tBasis14;
//	CanonicalBasis tBasis16;

	@BeforeEach
	public void setUp() throws GeneratorRangeException {
		tBasis0 = new Basis((byte) 0);
		tBasis1 = new Basis((byte) 1);
		tBasis4 = new Basis((byte) 4);
		tBasis43 = new Basis((byte) 4);
		tBasis8 = new Basis((byte) 8);
	}
	
	@Test
	public void testCachedBasis() throws GeneratorRangeException {
		CanonicalBasis tB1 = CladosGBuilder.createBasis((byte) 3);
		assertTrue(CladosGCache.INSTANCE.getBasisListSize()>0);
		CanonicalBasis tB2 = CladosGBuilder.createBasis((byte) 3);
		assertTrue(tB1 == tB2);
	}

	@Test
	public void testIndependence() {
		assertTrue(tBasis4.equals(tBasis43));
		// Using the raw constructor leaves both similar enough to pass equals test
		assertFalse(tBasis4 == tBasis43);
		// but not SO similar they are the same object. Hence desirability of caching.
	}

	@Test
	public void testBladeCount() throws GeneratorRangeException {
		tBasis10 = new Basis((byte) 10);
		tBasis14 = new Basis((byte) 14);
//		tBasis16 = new Basis((byte) 16);
		assertTrue(tBasis0.getBladeCount() == (1 << 0));
		assertTrue(tBasis4.getBladeCount() == (1 << 4));
		assertTrue(tBasis8.getBladeCount() == (1 << 8));
		assertTrue(tBasis10.getBladeCount() == (1 << 10));
		assertTrue(tBasis14.getBladeCount() == (1 << 14));
//		assertTrue(tBasis16.getBladeCount() == (1 << 16));
	}

	@Test
	public void testGradeCount() throws GeneratorRangeException {
		tBasis10 = new Basis((byte) 10);
		tBasis14 = new Basis((byte) 14);
//		tBasis16 = new Basis((byte) 16);
		assertTrue(tBasis0.getGradeCount() == 1);
		assertTrue(tBasis4.getGradeCount() == 5);
		assertTrue(tBasis8.getGradeCount() == 9);
		assertTrue(tBasis10.getGradeCount() == 11);
		assertTrue(tBasis14.getGradeCount() == 15);
//		assertTrue(tBasis16.getGradeCount() == 17);
	}

	@Test
	public void testGradeRange() throws GeneratorRangeException  {
		tBasis10 = new Basis((byte) 10);
		tBasis14 = new Basis((byte) 14);
		//Basis tBasis15 = new Basis((byte) 15);
//		tBasis16 = new Basis((byte) 16);
		ArrayList<Integer> tSpot;

		tSpot = tBasis0.getGrades();
		assertTrue(tSpot.get(0) == 0);

		tSpot = tBasis4.getGrades();
		assertTrue(tBasis4.getBladeCount() == 16);
		for (int k = 1; k < 0.5 * (tBasis4.getGradeCount() - 1); k++) {
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(4 - k + 1) - tSpot.get(4 - k));
		}

		tSpot = tBasis8.getGrades();
		assertTrue(tBasis8.getBladeCount() == 256);
		for (int k = 1; k < 0.5 * (tBasis8.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(8 - k + 1) - tSpot.get(8 - k));

		tSpot = tBasis10.getGrades();
		assertTrue(tBasis10.getBladeCount() == 1024);
		for (int k = 1; k < 0.5 * (tBasis10.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(10 - k + 1) - tSpot.get(10 - k));

		tSpot = tBasis14.getGrades();
		assertTrue(tBasis14.getBladeCount() == 16384);
		for (int k = 1; k < 0.5 * (tBasis14.getGradeCount() - 1); k++)
			assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(14 - k + 1) - tSpot.get(14 - k));

		//System.out.println(tBasis15.toXMLString(""));

//		tSpot = tBasis16.getGrades();
		

		//for (int k=50000; k<55540; k++) {
		//	System.out.println("Key Index Map: "+k+" goes with "+tBasis16.getKey(k));
		//}
//		assertTrue(tBasis16.getBladeCount() == 65536);

		//for (int k = 1; k < 0.5 * (tBasis16.getGradeCount() - 1); k++)
		//	System.out.println("Asserting something for "+k+": "+(tSpot.get(k + 1) - tSpot.get(k))+" and "+(tSpot.get(17 - k ) - tSpot.get(16 - k)));
		//		assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(16 - k + 1) - tSpot.get(16 - k));
	}

	@Test
	public void testLooseValidations() {
		assertTrue(CanonicalBasis.validateSize(0));
		assertFalse(CanonicalBasis.validateSize(-1));
		assertFalse(CanonicalBasis.validateSize(17));
		assertFalse(CanonicalBasis.validateSize(16));

		assertFalse(tBasis8.validateBladeIndex(65536)); //Index 65536 is more appropriate for 16 generator basis.
		assertFalse(tBasis8.validateBladeIndex(256)); //Index 256 is one too many.
		assertFalse(tBasis8.validateBladeIndex(-1)); //Index -1 is too low.
		assertTrue(tBasis8.validateBladeIndex(255)); //Index 255 is just right says Goldilocks.

		assertNull(tBasis8.getSingleBlade(256));
		assertTrue(tBasis8.getSingleBlade(255) instanceof Blade);

		assertThrows(GeneratorRangeException.class, () -> Basis.using((byte) 17));
		assertDoesNotThrow(() -> Basis.using(Generator.EF));
		assertThrows(GeneratorRangeException.class, () -> Basis.using(Generator.EG));

		try {
			Basis tryThisNow = Basis.using(Generator.EG);
			assertTrue(tryThisNow.getGradeCount() == 17);
		} catch (GeneratorRangeException egen) {
			;
		}

		Stream<Blade> testThis = tBasis4.bladeStream();
		assertTrue(testThis.count() == 16);	// 16 blades in a 4-gen basis
		testThis = tBasis8.bladeOfGradeStream((byte) 2);
		assertTrue(testThis.count() == 28); // 28 bivectors in an 8-gen basis
		testThis = tBasis8.bladeOfGradeStream((byte) 17);
		assertTrue(testThis.count() == 0); // out of range

		IntStream testInts = tBasis8.gradeStream();
		assertTrue(testInts.count() == 9); // The 9 grades add up that way. Completeness tested.

		LongStream testLongs = tBasis4.keyStream();
		assertTrue(testLongs.count() == 16);
		testLongs = tBasis4.keyStream();
		assertTrue(testLongs.sum() == 464);

		assertTrue(tBasis4.getKeyIndexMap() instanceof TreeMap<Long, Integer>);
		assertTrue(tBasis4.hashCode() == tBasis4.getGradeCount());
}

	@Test
	public void testBladeReturns() {
		Blade testThis = tBasis8.getScalarBlade();
		assertTrue(testThis.maxGenerator()==8);
		assertTrue(testThis.rank()==0);

		testThis = tBasis8.getPScalarBlade();
		assertTrue(testThis.maxGenerator()==8);
		assertTrue(testThis.rank()==8);
		assertTrue(tBasis8.getPScalarStart() == 255);
		assertTrue(tBasis8.getKey(255) > 0);
		assertTrue(tBasis8.getKey(256) < 0);

		EnumSet<Generator> testSet = tBasis8.getBladeSet(255);
		assertTrue(testSet instanceof EnumSet<Generator>);
		assertTrue(testSet.size() == 8);

		testSet = tBasis8.getBladeSet(256);
		assertTrue(testSet instanceof EnumSet<Generator>);
		assertTrue(testSet.size() == 0); 								//Out of range produces empty generator sets.

		assertTrue(tBasis8.getGradeStart((byte) 8) == 255);
		assertTrue(tBasis8.getGradeStart((byte) 9) < 0);

		assertTrue(tBasis8.find(tBasis8.getPScalarBlade()) == 256); 	//Indexed position(!) and NOT array position.
		assertTrue(tBasis8.find(null) == -1); 						//Nothing to find.
		assertTrue(tBasis8.find(Blade.createBlade(Generator.EA)) == -1); //Not in the basis
	}


	@Test
	void testXMLOutput() {
		String xml = tBasis4.toXMLString("");
		//System.out.println(xml);
		assertTrue(xml != null);
	}
}