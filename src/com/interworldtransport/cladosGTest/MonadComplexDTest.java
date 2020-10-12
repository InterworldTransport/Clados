package com.interworldtransport.cladosGTest;

import org.junit.*;

import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosF.Cardinal;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosFExceptions.FieldException;
import com.interworldtransport.cladosG.MonadComplexD;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

import static org.junit.Assert.*;
import static com.interworldtransport.cladosG.MonadComplexD.*;

public class MonadComplexDTest
{
	String		fType="TestMonadComplexDs";
	String		mName="Test MonadComplexD ";
	String		aName="Motion Algebra";
	String		aName2="Property Algebra";
	ComplexD[]		cRF;
	MonadComplexD	tM0;
	MonadComplexD	tM1;
	MonadComplexD	tM2;
	MonadComplexD	tM3;
	MonadComplexD	tM4;
	MonadComplexD	tM5;
	MonadComplexD	tM6;
	MonadComplexD	tM7;
	MonadComplexD	tM8;
	MonadComplexD	tM9;

	@Before
	public void setUp() throws BadSignatureException, CladosMonadException
	{
		cRF = new ComplexD[16];
		Cardinal tSpot = new Cardinal("TestComplexDs");
		for (int k = 0; k < 16; k++)
			cRF[k] = new ComplexD(tSpot, (double) k, (double) 15-k);

		tM0 = new MonadComplexD("Test MonadComplexD 0", "Motion Algebra",
						"Foot Default Frame", "Test Foot 0", "-+++",
						new ComplexD(new Cardinal("Test Float 1"), 0d));
		tM1 = new MonadComplexD("Test MonadComplexD 1", "Property Algebra",
						"Foot Default Frame", "Test Foot 1", "-+++",
						new ComplexD(new Cardinal("Test Float 1"), 0d));
		tM2 = new MonadComplexD("Test MonadComplexD 2", tM1);
		tM3 = new MonadComplexD("Test MonadComplexD 3", tM1);
		tM4 = new MonadComplexD(tM0);
		tM5 = new MonadComplexD("Test MonadComplexD 5", "Motion Algebra",
						"Foot Default Frame", "Test Foot 5", "-+++",
						new ComplexD(new Cardinal("Test Float 5"), 0d),
						"Unit PScalar");
		tM6 = new MonadComplexD("Test MonadComplexD 6", "Property Algebra",
						"Foot Default Frame", "Test Foot 6", "-+++", cRF);
		tM7 = new MonadComplexD(tM6);
		tM8 = new MonadComplexD(tM6);
	}

	@Test
	public void testConstructionVariants()
	{
		assertFalse(isReferenceMatch(tM0, tM1));
		assertTrue(isReferenceMatch(tM1, tM2));
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
		assertTrue(isGrade(tM6.SP(), 0));
		assertTrue(isGrade(tM5.PSP(), tM5.getAlgebra().getGProduct().getGradeCount() - 1));
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
		assertTrue(isGZero(tM5.scale(ComplexD.copyZERO(tM5.getCoeff((short) 0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));
		
		tM6.normalize();
		if(ComplexD.isEqual(tM6.magnitude(), ComplexD.copyONE(tM7.getCoeff((short) 0))))
		{
			assertTrue(ComplexD.isEqual(tM6.magnitude(), ComplexD.copyONE(tM7.getCoeff((short) 0))));
		}
		else
		{
			ComplexD tSpot = tM6.magnitude();
			assertTrue(tSpot.getImg() == 0.0f);
			assertTrue(Math.abs(tSpot.getReal() - 1.0f)  <= 0.000001f);
		}
		
		assertTrue(hasGrade(tM6, 2));
		assertTrue(hasGrade(tM7, 0));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException,
					CladosMonadBinaryException, CladosMonadException
	{
		tM6.add(tM7);
		tM7.scale(new ComplexD(tM6.getCoeff((short) 0), 2.0f, 0.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7)
						.subtract(tM7)
						.scale(new ComplexD(tM7.getCoeff((short) 0).getCardinal(),
										-1.0f));
		assertTrue(tM6.isGEqual(tM7));

	}

	@Test
	public void testBiMathOps2() throws FieldBinaryException,
					CladosMonadBinaryException, CladosMonadException
	{
		tM8.gradePart((short) 4).normalize();
		//System.out.println(toXMLString(tM8));
		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new ComplexD(tM6.getCoeff((short) 0), -1f, 0f));
		//System.out.println(toXMLString(tM6));
		//System.out.println(toXMLString(tM7));
		assertTrue(tM6.isGEqual(tM7));

		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new ComplexD(tM6.getCoeff((short) 0), -1f, 0f));
		assertTrue(tM6.isGEqual(tM7));

		tM5.setCoeff(tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));

		tM6.multiplySymm(tM8);
		tM6.scale(new ComplexD(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));

		tM6.setCoeff(tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new ComplexD(tM6.getCoeff((short) 0), -1f, 0f));
		// System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));
	}
}
