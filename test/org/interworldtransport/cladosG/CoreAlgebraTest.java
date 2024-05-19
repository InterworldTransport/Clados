package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreAlgebraTest {
	protected String fName = "Tst:TangentPoint";
	protected String aName = "Tst Algebra";
	protected String pSig31 = "-+++";
	protected String pSig13 = "+---";
	protected Cardinal fType;
	protected RealF rNumber;
	protected Foot tFoot;
	protected Foot tFoot2;
	protected Algebra alg1;
	protected Algebra alg2;
	protected Algebra alg3;

	@BeforeEach
	public void setUp() throws BadSignatureException, GeneratorRangeException {
		fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealF(fType, 0.0f);
		tFoot = new Foot(fName, fType);
		tFoot2 = new Foot(fName, rNumber);

		alg1 = new Algebra(aName, tFoot, pSig31, rNumber);
		alg2 = new Algebra(aName, tFoot, pSig13, rNumber);
		alg3 = new Algebra(aName, tFoot, pSig13, fType);
	}

	@Test
	public void testProtoNumber() {
		assertFalse(alg1.getProtoNumber() == null);
		assertTrue(alg1.getProtoNumber() instanceof RealF);
		assertFalse(alg1.getProtoNumber() instanceof RealD);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testComparisons() {
		int hash3 = alg3.hashCode();
		assertTrue(alg2.compareTo(alg3) == 0); //Same Name
		assertTrue(alg1.compareTo(alg2) == 0); //Same Name even though signatures are different.
		alg3.setAlgebraName("Something Else");
		assertTrue(alg3.hashCode() == hash3);	//Stays the same because the uuid didn't change.
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		assertFalse(alg3.compareTo(alg2) == 0); //Different Names
		
		alg3.setAlgebraName("");
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		alg3.setAlgebraName(null);
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		assertFalse(alg3.compareTo(alg2) == 0); //Different Names
		alg2.setAlgebraName(null);
		assertTrue(alg2.compareTo(alg3) == 0); //Same Null Name

		assertTrue(alg1.equals(alg1));
		assertFalse(alg1.equals(null));
		assertFalse(alg1.equals(tFoot)); //alg1 contains a reference to tFoot, but isn't tFoot.
	}

	@Test
	public void testSignatureLinks() throws GeneratorRangeException {
		assertSame(alg1.getGBasis(), alg2.getGBasis(), "Two algebras only have different signatures.");
		assertNotSame(alg1.getGProduct(), alg2.getGProduct(), "Two sigs ARE different forces different GProducts");
		assertNotEquals(alg1.getGProduct().signature(), alg2.getGProduct().signature(),
				"signature strings used in construction were different.");
	}

	@Test
	public void testAppendReferenceFrame() {
		assertNotNull(alg1, "Algebra setUp properly");
		assertEquals(alg1.getReferenceFrames().size(), 1, "Default frame (only) present after alg construction.");
		alg1.appendFrame(fName + "-Spherical");
		assertEquals(alg1.getReferenceFrames().size(), 2, "Appended frame makes for two present.");
		assertTrue(alg1.getFrames().size() == 2);
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
	public void testFootShared() throws BadSignatureException, GeneratorRangeException {
		tFoot.appendCardinal(rNumber.getCardinal());
		Algebra alg3 = new Algebra(aName, tFoot, pSig31, rNumber);
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
	public void testCompareCores() throws CladosMonadException, BadSignatureException, GeneratorRangeException {

		Algebra alg4 = new Algebra("light weight frame", alg1);
		assertFalse(alg4 == alg1);
		assertTrue(alg4.getFoot().equals(alg1.getFoot()));
		assertTrue(alg4.getGProduct() == (alg1.getGProduct()));
		// Foot re-used, GProduct re-used, but different names ensures algebra mis-match

		Algebra alg5 = new Algebra("medium weight frame", alg1);
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

		Algebra alg6 = new Algebra(aName, fName, pSig31, rNumber);
		assertFalse(alg6.equals(alg1));
		assertFalse(alg6.getFoot() == alg1.getFoot());
		assertTrue(alg6.getGProduct() == (alg1.getGProduct()));
		assertFalse(alg6.getFoot().getCardinal(0) == alg1.getFoot().getCardinal(0));
		assertTrue(alg6.getCardinal().getUnit().equals(alg1.getCardinal().getUnit()));
		// Cardinal string re-use is NOT Cardinal re-uses
	}

	@Test
	public void testCompareCounts() {
		assertTrue(alg1.getGradeCount() == alg2.getGradeCount());
		assertTrue(alg1.getBladeCount() == alg2.getBladeCount());
		// Different signatures does not lead to different grade and blade counts.
		int[] where = alg1.getGradeRange((byte) 2);
		assertTrue(where[0] == 5);
		assertTrue(where[1] == 10);
	}

	@Test
	public void testModality() {
		assertTrue(alg1.getMode() == CladosField.REALF);
		assertTrue(alg3.getMode() == null);

		RealF oldProto = (RealF) alg1.getProtoNumber();
		RealD tryThis = (RealD) FBuilder.REALD.createONE("Howz About This One");
		alg1.setMode(tryThis);
		assertTrue(alg1.getMode() == CladosField.REALD);
		assertFalse(alg1.getProtoNumber() == oldProto);
		assertFalse(alg1.getProtoNumber() == tryThis); //A Copy is made and linked to preserve integrity of parameter
		assertTrue(alg1.getProtoNumber().getCardinal() == tryThis.getCardinal()); //but Cardinal is re-used.
	}

	@Test
	public void testXMLOutput() {
		String test = Algebra.toXMLString(alg1, "");
		assertTrue(test != null);
	}

}
