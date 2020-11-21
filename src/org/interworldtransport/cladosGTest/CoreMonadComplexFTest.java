package org.interworldtransport.cladosGTest;

import static org.interworldtransport.cladosG.MonadAbstract.hasGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isMultiGrade;
import static org.interworldtransport.cladosG.MonadAbstract.isUniGrade;
import static org.interworldtransport.cladosG.MonadComplexF.isGZero;
import static org.interworldtransport.cladosG.MonadComplexF.isIdempotent;
import static org.interworldtransport.cladosG.MonadComplexF.isNilpotent;
import static org.interworldtransport.cladosG.MonadComplexF.isReferenceMatch;
import static org.interworldtransport.cladosG.MonadComplexF.isScaledIdempotent;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosG.MonadComplexF;
import org.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreMonadComplexFTest {
	String fType = "TestMonadComplexFs";
	String mName = "Test MonadComplexF ";
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
	ComplexF[] cRF;
	MonadComplexF tM0;
	MonadComplexF tM1;
	MonadComplexF tM2;
	MonadComplexF tM3;
	MonadComplexF tM4;
	MonadComplexF tM5;
	MonadComplexF tM6;
	MonadComplexF tM7;
	MonadComplexF tM8;
	MonadComplexF tM9;

	@BeforeEach
	void setUp() throws Exception {
		cRF = new ComplexF[16];
		Cardinal tSpot = Cardinal.generate("TestComplexFs");
		for (int k = 0; k < 16; k++)
			cRF[k] = new ComplexF(tSpot, (float) k, (float) 15 - k);

		tM0 = new MonadComplexF("Test MonadComplexF 0", "Motion Algebra", "Foot Default Frame", "Test Foot 0", "-+++",
				new ComplexF(Cardinal.generate("Test Float 1"), 0f));
		tM1 = new MonadComplexF("Test MonadComplexF 1", "Property Algebra", "Foot Default Frame", "Test Foot 1", "-+++",
				new ComplexF(Cardinal.generate("Test Float 1"), 0f));
		tM2 = new MonadComplexF("Test MonadComplexF 2", tM1);
		tM3 = new MonadComplexF("Test MonadComplexF 3", tM1);
		tM4 = new MonadComplexF(tM0);
		tM5 = new MonadComplexF("Test MonadComplexF 5", "Motion Algebra", "Foot Default Frame", "Test Foot 5", "-+++",
				new ComplexF(Cardinal.generate("Test Float 5"), 0f), "Unit PScalar");
		tM6 = new MonadComplexF("Test MonadComplexF 6", "Property Algebra", "Foot Default Frame", "Test Foot 6", "-+++",
				cRF);
		tM7 = new MonadComplexF(tM6);
		tM8 = new MonadComplexF(tM6);
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
		assertTrue(isGrade(tM6.SP(), 0));
		assertTrue(isGrade(tM5.PSP(), tM5.getAlgebra().getGradeCount() - 1));
	}

	@Test
	public void testBiOpsTests() {
		assertTrue(tM1.isGEqual(tM3));
		assertTrue(tM1.isGEqual(tM2));
	}

	@Test
	public void testUniMathOps() throws FieldBinaryException, FieldException, CladosMonadException {
		assertTrue(tM4.isGEqual(tM0.dualLeft()));
		assertTrue(tM4.isGEqual(tM0.dualRight()));
		assertTrue(isGZero(tM5.scale(ComplexF.copyZERO(tM5.getCoeff((short) 0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));

		tM6.normalize();
		if (ComplexF.isEqual(tM6.magnitude(), ComplexF.copyONE(tM7.getCoeff((short) 0)))) {
			assertTrue(ComplexF.isEqual(tM6.magnitude(), ComplexF.copyONE(tM7.getCoeff((short) 0))));
		} else {
			ComplexF tSpot = tM6.magnitude();
			assertTrue(tSpot.getImg() == 0.0f);
			assertTrue(Math.abs(tSpot.getReal() - 1.0f) <= 0.000001f);
		}

		assertTrue(hasGrade(tM6, 2));
		assertTrue(hasGrade(tM7, 0));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM6.add(tM7);
		tM7.scale(new ComplexF(tM6.getCoeff((short) 0), 2.0f, 0.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7).subtract(tM7).scale(new ComplexF(tM7.getCoeff((short) 0).getCardinal(), -1.0f));
		assertTrue(tM6.isGEqual(tM7));

	}

	@Test
	public void testBiMathOps2() throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM8.gradePart((short) 4).normalize();
		// System.out.println(toXMLString(tM8));
		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		// System.out.println(toXMLString(tM7));
		assertTrue(tM6.isGEqual(tM7));

		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		assertTrue(tM6.isGEqual(tM7));

		tM5.setCoeff(tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));

		tM6.multiplySymm(tM8);
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));

		tM6.setCoeff(tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));
	}

}
