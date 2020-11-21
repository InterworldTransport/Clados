package org.interworldtransport.cladosGTest;

import static org.interworldtransport.cladosG.MonadAbstract.hasGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isMultiGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isUniGrade;
import static org.interworldtransport.cladosG.MonadRealD.isGZero;
import static org.interworldtransport.cladosG.MonadRealD.isIdempotent;
import static org.interworldtransport.cladosG.MonadRealD.isNilpotent;
import static org.interworldtransport.cladosG.MonadRealD.isReferenceMatch;
import static org.interworldtransport.cladosG.MonadRealD.isScaledIdempotent;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosG.MonadRealD;
import org.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreMonadRealDTest {
	String fType = "TestMonadRealDs";
	String mName = "Test MonadRealD ";
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
	RealD[] cRD;
	MonadRealD tM0;
	MonadRealD tM1;
	MonadRealD tM2;
	MonadRealD tM3;
	MonadRealD tM4;
	MonadRealD tM5;
	MonadRealD tM6;
	MonadRealD tM7;
	MonadRealD tM8;
	MonadRealD tM9;

	@BeforeEach
	void setUp() throws Exception {
		cRD = new RealD[16];

		Cardinal tSpot = Cardinal.generate(fType);

		for (int k = 0; k < 16; k++) {
			cRD[k] = new RealD(tSpot, (float) k);
			assertTrue(cRD[k] != null);
			assertTrue(cRD[k].getCardinal().equals(tSpot));
		}

		tM0 = new MonadRealD(mName + "0", aName, "Foot Default Frame", "Test Foot 0", "-+++",
				new RealD(Cardinal.generate("Test Double 1"), 0d));

		tM1 = new MonadRealD(mName + "1", aName2, "Foot Default Frame", "Test Foot 1", "-+++",
				new RealD(Cardinal.generate("Test Double 1"), 0d));

		tM2 = new MonadRealD(mName + "2", tM1);
		tM3 = new MonadRealD(mName + "3", tM1);
		tM4 = new MonadRealD(tM0);

		tM5 = new MonadRealD(mName + "5", aName, "Foot Default Frame", "Test Foot 5", "-+++",
				new RealD(Cardinal.generate("Test Double 5"), 0d), "Unit PScalar");

		tM6 = new MonadRealD("Test MonadRealF 6", "Property Algebra", "Foot Default Frame", "Test Foot 6", "-+++", cRD);

		tM7 = new MonadRealD(tM6);
		tM8 = new MonadRealD(tM6);

		tM9 = new MonadRealD(mName + "9", tM2);
		RealD tAdj = new RealD(tM9.getAlgebra().getFoot().getCardinal(0), 0.0f);
		RealD[] tFix = new RealD[16];
		for (int k = 0; k < 16; k++)
			tFix[k] = RealD.copyOf(tAdj);

		tFix[1] = new RealD(tM9.getAlgebra().getFoot().getCardinal(0), 1.0f);
		tFix[4] = RealD.copyOf(tFix[1]);
		tM9.setCoeff(tFix); // Should be e0 + e3 now
	}

	@Test
	public void testConstructionVariants() {
		assertFalse(isReferenceMatch(tM0, tM1));
		assertTrue(isReferenceMatch(tM1, tM2));
		assertTrue(isReferenceMatch(tM1, tM3));
		assertTrue(isReferenceMatch(tM0, tM4));
	}

	@Test
	public void testUniOpsTests() throws CladosMonadException, FieldException {
		assertFalse(isGZero(tM5));
		assertTrue(isUniGrade(tM5));
		assertTrue(isMultiGrade(tM6));
		assertFalse(isIdempotent(tM5));
		assertTrue(isIdempotent(tM4));
		assertTrue(isScaledIdempotent(tM4));
		assertTrue(isNilpotent(tM2, 2));

		assertTrue(isNilpotent(tM9, 2));
		assertFalse(isNilpotent(tM9, 1));

		assertTrue(isGrade(tM6.SP(), 0));
		assertTrue(isGrade(tM5.PSP(), tM5.getAlgebra().getGradeCount() - 1));
	}

	@Test
	public void testBiOpsTests() {
		assertTrue(tM1.isGEqual(tM3));
		assertTrue(tM1.isGEqual(tM2));
	}

	@Test
	public void testUniMathOps() throws FieldBinaryException, CladosMonadException {
		assertTrue(tM4.isGEqual(tM0.dualLeft()));
		assertTrue(tM4.isGEqual(tM0.dualRight()));
		assertTrue(isGZero(tM5.scale(RealD.copyZERO(tM5.getCoeff((short) 0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));
//		assertTrue(isEqual(tM6.normalize().magnitude(), RealD.copyONE(tM7.getCoeff((short) 0))));
		assertTrue(hasGrade(tM6, 2));
		assertFalse(hasGrade(tM7, 0));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM6.add(tM7);
		tM7.scale(new RealD(tM6.getCoeff((short) 0), 2.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7).subtract(tM7).scale(new RealD(tM7.getCoeff((short) 0).getCardinal(), -1.0f));
		assertTrue(tM6.isGEqual(tM7));

	}

	@Test
	public void testBiMathOps2() throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM8.gradePart((short) 4).normalize();

		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new RealD(tM6.getCoeff((short) 0), -1f));
		assertTrue(tM6.isGEqual(tM7));

		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new RealD(tM6.getCoeff((short) 0), -1f));
		assertTrue(tM6.isGEqual(tM7));

		tM5.setCoeff(tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));

		tM6.multiplySymm(tM8);
		tM6.scale(new RealD(tM6.getCoeff((short) 0), -1f));
		assertFalse(tM6.isGEqual(tM7));

		tM6.setCoeff(tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new RealD(tM6.getCoeff((short) 0), -1f));
		assertFalse(tM6.isGEqual(tM7));
	}

}
