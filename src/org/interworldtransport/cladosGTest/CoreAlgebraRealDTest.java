package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosG.AlgebraRealD;
import org.interworldtransport.cladosG.Foot;
//import org.interworldtransport.cladosG.GProductMap;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreAlgebraRealDTest {
	protected String fName = "Test:TangentPoint";
	protected String aName = "Test Algebra";
	protected String pSig31 = "-+++";
	protected String pSig13 = "+---";
	protected Cardinal fType;
	protected RealD rNumber;
	protected Foot tFoot;
	protected Foot tFoot2;
	//protected GProductMap gProduct;
	protected AlgebraRealD alg1;
	protected AlgebraRealD alg2;

	@BeforeEach
	void setUp() throws Exception {
		fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealD(fType, 0.0d);
		tFoot = new Foot(fName, fType);
		tFoot2 = new Foot(fName, rNumber);

		alg1 = new AlgebraRealD(aName, tFoot, pSig31, rNumber);
		alg2 = new AlgebraRealD(aName, tFoot, pSig13, rNumber);
	}

	@Test
	public void testSignatureLinks() throws GeneratorRangeException {
		assertTrue(alg1.getGBasis() == alg2.getGBasis());
		// Two algebras share a Basis?
		//assertTrue(CladosGBuilder.INSTANCE.getBasisListSize() == 1);
		assertFalse(alg1.getGProduct() == alg2.getGProduct()); // but not the products
		assertFalse(alg1.getGProduct().signature().equals(alg2.getGProduct().signature()));
		// because the signatures are different.
	}
	
	@Test
	public void testAppendReferenceFrame() {
		assertFalse(alg1.equals(null));
		assertTrue(alg1.getReferenceFrames().size() == 1);
		alg1.appendFrame(fName + "-Spherical");
		assertTrue(alg1.getReferenceFrames().size() == 2);
	}

	@Test
	public void testRemoveReferenceFrame() {
		assertTrue(alg2.getReferenceFrames().size() == 1);
		alg2.appendFrame(fName + "-Spherical2");
		assertTrue(alg2.getReferenceFrames().size() == 2);
		alg2.removeFrame(fName + "-Spherical2");
		assertTrue(alg2.getReferenceFrames().size() == 1);
		alg2.removeFrame("Un-named frame that shouldn't be found.");
		assertTrue(alg2.getReferenceFrames().size() == 1);
		// Attempting to remove a frame that isn't there silently moves on.
		// If one needs to ensure the frame was there and confirm it's
		// removal, one should find it first.
		assertTrue(alg2.getReferenceFrames().indexOf("Un-named frame that shouldn't be found.") == -1);
	}
	
	@Test
	public void testFootLinks() {
		assertTrue(alg1.getFoot() == alg2.getFoot()); // Two algebras share the foot
	}

	@Test
	public void testFootShared() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		tFoot.appendCardinal(rNumber.getCardinal());
		AlgebraRealD alg3 = new AlgebraRealD(aName, tFoot, pSig31, rNumber);
		assertTrue(alg1.getFoot() == alg3.getFoot());
		assertTrue(alg1.getFoot() == alg2.getFoot());
		// because the Foot is shared between algebras, changing the number
		// type to use to build alg3 changes it for the others as well.
		alg3.setFoot(tFoot2);
		assertFalse(alg1.getFoot() == alg3.getFoot());
		// Both feet are essentially the same inside, but represented as two distinct
		// objects. That should cause this test to be false.
		alg3.setFoot(tFoot2);
		assertFalse(alg1.getFoot() == alg3.getFoot());
		// Both feet are essentially the same inside, but represented as two distinct
		// objects. That should cause this test to be false.
	}

	@Test
	public void testCompareCores() throws CladosMonadException, BadSignatureException, GeneratorRangeException, BladeCombinationException {

		AlgebraRealD alg4 = new AlgebraRealD("light weight frame", alg1);
		assertFalse(alg4 == alg1);
		assertTrue(alg4.getFoot().equals(alg1.getFoot()));
		assertTrue(alg4.getGProduct() == (alg1.getGProduct()));
		// Foot re-used, GProduct re-used, but different names ensures algebra mis-match
	
		AlgebraRealD alg5 = new AlgebraRealD("medium weight frame", alg1);
		assertFalse(alg5 == alg1);
		assertTrue(alg5.getFoot().equals(alg1.getFoot()));
		assertTrue(alg5.getGProduct() == (alg1.getGProduct()));
		// Foot re-used, signature re-used... ensures different GProduct thus algebra
		// mis-match

		assertFalse(alg5.getAlgebraName() == alg4.getAlgebraName());
		alg5.setAlgebraName(alg4.getAlgebraName());
		assertTrue(alg5.getAlgebraName() == alg4.getAlgebraName());
		assertFalse(alg5.equals(alg4));
		// Setting names equal isn't anywhere near enough to make algebras pass
		// reference match

		AlgebraRealD alg6 = new AlgebraRealD(aName, fName, pSig31, rNumber);
		assertFalse(alg6.equals(alg1));
		assertFalse(alg6.getFoot() == alg1.getFoot());
		assertTrue(alg6.getGProduct() == (alg1.getGProduct()));
		assertFalse(alg6.getFoot().getCardinal(0) == alg1.getFoot().getCardinal(0));
		assertTrue(alg6.shareCardinal().getUnit().equals(alg1.shareCardinal().getUnit()));
		// Cardinal string re-use is NOT Cardinal re-uses
	}

	@Test
	public void testCompareCounts() {
		assertTrue(alg1.getGradeCount() == alg2.getGradeCount());
		assertTrue(alg1.getBladeCount() == alg2.getBladeCount());
		// Different signatures does not lead to different grade and blade counts.
	}

	@Test
	public void testStaticOp() {
		RealD result = alg1.generateNumber(10.0d);
		assertTrue(result != null);
		assertTrue(result.getCardinal() == alg1.getFoot().getCardinal(0));
		// this shows that an algebra can be used to generate numbers of the same type
		// by using the static method built into the class. This method is picky, but
		// when used properly it will safely generate matches that will pass reference
		// tests.
	}

	@Test
	public void testXMLOutput() {
		String test = AlgebraRealD.toXMLString(alg1);
		assertTrue(test != null);
	}

}
