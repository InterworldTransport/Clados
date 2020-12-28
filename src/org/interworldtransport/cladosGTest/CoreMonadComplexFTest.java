package org.interworldtransport.cladosGTest;

import static org.interworldtransport.cladosG.Monad.isGrade;
import static org.interworldtransport.cladosG.Monad.isMultiGrade;
import static org.interworldtransport.cladosG.Monad.isUniGrade;
import static org.interworldtransport.cladosG.Monad.isGZero;
import static org.interworldtransport.cladosG.Monad.isIdempotent;
import static org.interworldtransport.cladosG.Monad.isNilpotent;
import static org.interworldtransport.cladosG.Monad.isReferenceMatch;
import static org.interworldtransport.cladosG.Monad.isScaledIdempotent;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFListBuilder;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosG.Monad;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreMonadComplexFTest {
	String fType = "TestMonadComplexFs";
	String mName = "Test MonadComplexF ";
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
	ComplexF[] cRF;
	Monad tM0;
	Monad tM1;
	Monad tM2;
	Monad tM3;
	Monad tM4;
	Monad tM5;
	Monad tM6;
	Monad tM7;
	Monad tM8;
	Monad tM9;

	@BeforeEach
	public void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		cRF = new ComplexF[16];
		Cardinal tSpot = Cardinal.generate("TestComplexFs");
		cRF = (ComplexF[]) CladosFListBuilder.COMPLEXF.createONE(tSpot, cRF.length);

		tM0 = new Monad("Test MonadComplexF 0", "Motion Algebra", "Foot Default Frame", "Test Foot 0", "-+++",
				new ComplexF(Cardinal.generate("Test Float 1"), 0f, 0f));
		tM1 = new Monad("Test MonadComplexF 1", "Property Algebra", "Foot Default Frame", "Test Foot 1", "-+++",
				new ComplexF(Cardinal.generate("Test Float 1"), 0f, 0f));
		tM2 = new Monad("Test MonadComplexF 2", tM1);
		tM3 = new Monad("Test MonadComplexF 3", tM1);
		tM4 = new Monad(tM0);
		tM5 = new Monad("Test MonadComplexF 5", "Motion Algebra", "Foot Default Frame", "Test Foot 5", "-+++",
				new ComplexF(Cardinal.generate("Test Float 5"), 0f), "Unit PScalar");
		tM6 = new Monad("Test MonadComplexF 6", "Property Algebra", "Foot Default Frame", "Test Foot 6", "-+++", cRF);
		tM7 = new Monad(mName + "7", tM6);
		tM8 = new Monad(mName + "8", tM6);
		tM9 = new Monad(mName + "9", tM2);
		
		ComplexF tAdj = new ComplexF(tM9.getAlgebra().getCardinal(), 0.0f);
		ComplexF[] tFix = (ComplexF[]) CladosFListBuilder.COMPLEXF.create(tAdj.getCardinal(), 16);
		tFix[1] = new ComplexF(tM9.getAlgebra().getFoot().getCardinal(0), 1.0f, 0.0f);
		tFix[4] = ComplexF.copyOf(tFix[1]);
		tM9.setCoeff(tFix); 
	}
	
	@Test
	public void testMultiplication() {
		assert(tM1.getAlgebra() == tM2.getAlgebra());
		assert(tM2.getAlgebra() == tM9.getAlgebra());
		
		for (short m = 0; m < 100; m++) {
			//System.out.println("ComplexF | " + m);
			Monad check1 = new Monad(tM9);
			assertTrue(tM9.isGEqual(check1));
			check1.multiplyLeft(tM9);
			assert (Monad.isGZero(check1));
		}
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
		assertTrue(isGrade(tM6.gradePart((byte) 0), 0));
		assertTrue(isGrade(tM5.gradePart((byte) 4), tM5.getAlgebra().getGradeCount() - 1));
		assertTrue(isNilpotent(tM2, 2));
		assertFalse(isGZero(tM9));	
		//System.out.println(Monad.toXMLString(tM9, ""));
		assertTrue(isNilpotent(tM9, 2));
		assertFalse(isNilpotent(tM9, 1));
		assertFalse(isIdempotent(tM9));
	}

	@Test
	public void testUniMathOps() throws FieldException, CladosMonadException {
		assertTrue(tM4.isGEqual(tM0.dualLeft()));
		assertTrue(tM4.isGEqual(tM0.dualRight()));
		assertTrue(isGZero(tM5.scale(ComplexF.copyZERO((ComplexF) tM5.getCoeff(0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));

		tM6.normalize();
		if (ComplexF.isEqual((ComplexF) tM6.magnitude(), ComplexF.copyONE((ComplexF) tM7.getCoeff(0)))) {
			assertTrue(ComplexF.isEqual((ComplexF) tM6.magnitude(), ComplexF.copyONE((ComplexF) tM7.getCoeff(0))));
		} else {
			ComplexF tSpot = (ComplexF) tM6.magnitude();
			assertTrue(tSpot.getImg() == 0.0f);
			assertTrue(Math.abs(tSpot.getReal() - 1.0f) <= 0.000001f);
		}

		assertTrue(Monad.hasGrade(tM6, 2));
		assertTrue(Monad.hasGrade(tM7, 0));
	}

	@Test
	public void testBiOpsTests() {
		assertTrue(tM1.isGEqual(tM3));
		assertTrue(tM1.isGEqual(tM2));
	}
	
	@Test
	public void testBiMathOps1() throws CladosMonadException {
		tM6.add(tM7);
		tM7.scale(new ComplexF(tM6.getCoeff((short) 0), 2.0f, 0.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7).subtract(tM7).scale(new ComplexF(tM7.getCoeff((short) 0).getCardinal(), -1.0f));
		assertTrue(tM6.isGEqual(tM7));

	}
	
	
	@Test
	public void testBiMathOps2() throws FieldException, CladosMonadException {
		tM8.gradePart((byte) 4).normalize();
		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM5.setCoeff((ComplexF[]) tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));
		tM6.multiplySymm(tM8);
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		assertFalse(tM6.isGEqual(tM7));

		tM6.setCoeff((ComplexF[]) tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new ComplexF(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));
	}

}
