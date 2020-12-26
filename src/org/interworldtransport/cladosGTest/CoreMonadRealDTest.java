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
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosG.CladosConstant;
import org.interworldtransport.cladosG.Monad;
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
	public void setUp() throws Exception {
		cRD = new RealD[16];
		Cardinal tSpot = Cardinal.generate(fType);
		cRD = (RealD[]) CladosFListBuilder.REALD.createONE(tSpot, cRD.length);

		tM0 = new Monad(mName + "0", aName, "Foot Default Frame", "Test Foot 0", "-+++",
				new RealD(Cardinal.generate("Test Double 1"), 0d));
		tM1 = new Monad(mName + "1", aName2, "Foot Default Frame", "Test Foot 1", "-+++",
				new RealD(Cardinal.generate("Test Double 1"), 0d));
		tM2 = new Monad(mName + "2", tM1);
		tM3 = new Monad(mName + "3", tM1);
		tM4 = new Monad(tM0);
		tM5 = new Monad(mName + "5", aName, "Foot Default Frame", "Test Foot 5", "-+++",
				new RealD(Cardinal.generate("Test Double 5"), 0d), "Unit PScalar");
		tM6 = new Monad("Test MonadRealD 6", "Property Algebra", "Foot Default Frame", "Test Foot 6", "-+++", cRD);
		tM7 = new Monad(mName + "7", tM6);
		tM8 = new Monad(mName + "8", tM6);
		tM9 = new Monad(mName + "9", tM2);
		
		RealD tAdj = new RealD(tM9.getAlgebra().shareCardinal(), 0.0f);
		RealD[] tFix = (RealD[]) CladosFListBuilder.REALD.create(tAdj.getCardinal(), 16);
		tFix[1] = new RealD(tM9.getAlgebra().getFoot().getCardinal(0), 1.0f);
		tFix[4] = RealD.copyOf(tFix[1]);
		tM9.setCoeff(tFix); 
	}
	
	@Test
	public void testMultiplication() throws FieldBinaryException, CladosMonadBinaryException {
		assert(tM1.getAlgebra() == tM2.getAlgebra());
		assert(tM2.getAlgebra() == tM9.getAlgebra());
		
		for (short m = 0; m < 100; m++) {
			//System.out.println("RealD | " + m);
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
		assertTrue(isNilpotent(tM9, 2));
		assertFalse(isNilpotent(tM9, 1));
		assertFalse(isIdempotent(tM9));
	}

	@Test
	public void testUniMathOps() throws FieldException {
		tM0.dualLeft();
		assertTrue(tM4.isGEqual(tM0));
		assertTrue(tM4.isGEqual(tM0.dualRight()));
		assertTrue(isGZero(tM5.scale(RealD.copyZERO((RealD) tM5.getCoeff(0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));
		
		assertTrue(RealD.isEqual((RealD) tM6.normalize().magnitude(), RealD.copyONE((RealD) tM7.getCoeff(0))));
		
		assertTrue(Monad.hasGrade(tM6, 2));
		assertTrue(Monad.hasGrade(tM7, 0));
	}

	@Test
	public void testBiOpsTests() {
		assertTrue(tM1.isGEqual(tM3));
		assertTrue(tM1.isGEqual(tM2));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM6.add(tM7);
		tM7.scale(new RealD(tM6.getCoeff(0), 2.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7).subtract(tM7);
		tM6.scale(new RealD(tM7.getAlgebra().shareCardinal(), CladosConstant.MINUS_ONE_F));
		assertTrue(tM6.isGEqual(tM7));
	}

	@Test
	public void testBiMathOps2() 
			throws FieldBinaryException, CladosMonadBinaryException, CladosMonadException {
		tM8.gradePart((byte) 4);
		tM6.multiplyLeft(tM8);
		tM6.dualLeft();
		tM6.scale(new RealD(tM6.getAlgebra().shareProtoNumber(), CladosConstant.MINUS_ONE_F));
		assertTrue(tM6.isGEqual(tM7));

		tM6.multiplyRight(tM8);
		tM6.dualRight();
		tM6.scale(new RealD(tM6.getAlgebra().shareProtoNumber(), CladosConstant.MINUS_ONE_F));
		assertTrue(tM6.isGEqual(tM7));

		tM5.setCoeff((RealD[]) tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));

		tM6.multiplySymm(tM8);
		tM6.scale(new RealD(tM6.getAlgebra().shareProtoNumber(), CladosConstant.MINUS_ONE_F));
		assertFalse(tM6.isGEqual(tM7));

		tM6.setCoeff((RealD[]) tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new RealD(tM6.getAlgebra().shareProtoNumber(), CladosConstant.MINUS_ONE_F));
		assertFalse(tM6.isGEqual(tM7));
	}
	
	@Test
	public void testNewConstructor() throws CladosMonadException {
		for (short m = 0; m < tM0.getCoeff().length; m++)
			assertFalse(tM0.getCoeff(m).equals(null));

		Monad newOne = new Monad("newName", tM0.getAlgebra(), "unimportantFrameName", (RealD[]) tM0.getCoeff());
		assertFalse(newOne.equals(null));
		assertFalse(isReferenceMatch(tM0, newOne));
	}
	
	/*
	@Test
	public void testXMLOutput() {
		System.out.println(MonadRealD.toXMLString(tM6, ""));
	}
*/
}
