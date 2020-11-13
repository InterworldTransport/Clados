package org.interworldtransport.cladosGTest;

import org.junit.*;

import static org.interworldtransport.cladosG.MonadRealF.*;
import static org.junit.Assert.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosG.MonadRealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.interworldtransport.cladosGExceptions.GradeOutOfRangeException;

public class MonadRealFTest
{
	String		fType="TestMonadRealFs";
	String		mName="Test MonadRealF ";
	String		aName="Motion Algebra";
	String		aName2="Property Algebra";
	RealF[]		cRF;
	MonadRealF	tM0;
	MonadRealF	tM1;
	MonadRealF	tM2;
	MonadRealF	tM3;
	MonadRealF	tM4;
	MonadRealF	tM5;
	MonadRealF	tM6;
	MonadRealF	tM7;
	MonadRealF	tM8;
	MonadRealF	tM9;

	@Before
	public void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException, GradeOutOfRangeException
	{
		cRF = new RealF[16];
		
		Cardinal tSpot =Cardinal.generate(fType);
		
		for (int k = 0; k < 16; k++)
		{
			cRF[k] = new RealF(tSpot, (float) k);
			assertTrue(cRF[k] != null);
			assertTrue(cRF[k].getCardinal().equals(tSpot));
		}

		tM0 = new MonadRealF(	mName+"0", 
								aName,
								"Foot Default Frame", 
								"Test Foot 0", 
								"-+++", 
								new RealF(Cardinal.generate("Test Float 1"), 0f)
							);
		
		tM1 = new MonadRealF(	mName+"1", 
								aName2,
								"Foot Default Frame", 
								"Test Foot 1", 
								"-+++", 
								new RealF(Cardinal.generate("Test Float 1"), 0f)
							);
		
		tM2 = new MonadRealF(mName+"2", tM1);
		tM3 = new MonadRealF(mName+"3", tM1);
		tM4 = new MonadRealF(tM0);

		tM5 = new MonadRealF(	mName+"5", 
								aName,
								"Foot Default Frame", 
								"Test Foot 5", 
								"-+++", 
								new RealF(Cardinal.generate("Test Float 5"), 0f),
								"Unit PScalar"
							);
		
		tM6 = new MonadRealF(	mName+"6", 
								aName2,
								"Foot Default Frame", 
								"Test Foot 6", 
								"-+++", 
								cRF
							);
		
		tM7 = new MonadRealF(mName+"7", tM6);
		tM8 = new MonadRealF(mName+"8",tM6);
		
		tM9 = new MonadRealF(mName+"9",tM2);
		RealF tAdj = new RealF(tM9.getAlgebra().getFoot().getCardinal(0), 0.0f);
		RealF[] tFix = new RealF[16];
		for (int k = 0; k < 16; k++)
			tFix[k] = RealF.copyOf(tAdj);
		
		tFix[1] = new RealF(tM9.getAlgebra().getFoot().getCardinal(0), 1.0f);
		tFix[4] = RealF.copyOf(tFix[1]);
		tM9.setCoeff(tFix); // Should be e0 + e3 now
		//System.out.println(toXMLString(tM9));
	
		
	}

	@Test
	public void testConstructionVariants()
	{
		assertFalse(isReferenceMatch(tM0, tM1));
		assertFalse(isReferenceMatch(tM1, tM4));
		assertTrue(isReferenceMatch(tM1, tM3));
		assertTrue(isReferenceMatch(tM0, tM4));
	}

	@Test
	public void testUniOpsTests() throws CladosMonadException, FieldException
	{
		assertFalse(isGZero(tM5));
		assertTrue(isUniGrade(tM5));
		assertTrue(isMultiGrade(tM6));
		assertFalse(isIdempotent(tM5));
		assertTrue(isIdempotent(tM4));
		assertTrue(isScaledIdempotent(tM4));
		
		assertTrue(isNilpotent(tM2, 2));
		assertTrue(isNilpotent(tM9, 2));
		assertFalse(isNilpotent(tM9, 1));
		assertFalse(isIdempotent(tM9));
		
		assertTrue(isGrade(tM6.SP(), 0));
		assertTrue(isGrade(tM5.PSP(), tM5.getAlgebra().getGradeCount() - 1));
	}

	@Test
	public void testBiOpsTests()
	{
		assertTrue(tM1.isGEqual(tM3));
		assertTrue(tM1.isGEqual(tM2));
	}

	@Test
	public void testUniMathOps() throws FieldBinaryException,
					CladosMonadException
	{
		assertTrue(tM4.isGEqual(tM0.dualLeft()));
		assertTrue(tM4.isGEqual(tM0.dualRight()));
		assertTrue(isGZero(tM5.scale(RealF.copyZERO(tM5.getCoeff((short) 0)))));
				assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));
		
//		tM6.normalize();
		
//		System.out.println(toXMLString(tM6));
//		System.out.println(tM6.magnitude().toXMLString());
//		System.out.println(RealF.copyONE(tM7.getCoeff((short) 0)).toXMLString());
		//assertTrue(isEqual(tM6.normalize().magnitude(), RealF.copyONE(tM7.getCoeff((short) 0))));
		
		assertTrue(hasGrade(tM6, 2));
		assertFalse(hasGrade(tM7, 0));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException,
					CladosMonadBinaryException, CladosMonadException
	{
		tM6.add(tM7);
		tM7.scale(new RealF(tM6.getCoeff((short) 0), 2.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7)
						.subtract(tM7)
						.scale(new RealF(tM7.getCoeff((short) 0).getCardinal(), -1.0f));
		assertTrue(tM6.isGEqual(tM7));
		
	}
	@Test
	public void testBiMathOps2() throws FieldBinaryException, FieldException,
					CladosMonadBinaryException, CladosMonadException
	{
		
		tM8.gradePart((short) 4).normalize();	
		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new RealF(tM6.getCoeff((short) 0), -1f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new RealF(tM6.getCoeff((short) 0), -1f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM5.setCoeff(tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));
		
		tM6.multiplySymm(tM8);
		tM6.scale(new RealF(tM6.getCoeff((short) 0), -1f));
		assertFalse(tM6.isGEqual(tM7));
		
		tM6.setCoeff(tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new RealF(tM6.getCoeff((short) 0), -1f));
		assertFalse(tM6.isGEqual(tM7));
	}
	
	@Test
	public void testNewConstructor() throws CladosMonadException
	{
		for (short m=0; m<tM0.getCoeff().length; m++)
			assertFalse(tM0.getCoeff(m).equals(null));
		
		MonadRealF newOne=new MonadRealF("newName", tM0.getAlgebra(), "unimportantFrameName", tM0.getCoeff());
		assertFalse(newOne.equals(null));
		assertFalse(isReferenceMatch(tM0, newOne));
	}
}


