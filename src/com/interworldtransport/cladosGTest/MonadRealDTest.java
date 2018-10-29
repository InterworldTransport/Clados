package com.interworldtransport.cladosGTest;

import org.junit.*;

import com.interworldtransport.cladosF.DivFieldType;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosFExceptions.FieldException;
import com.interworldtransport.cladosG.MonadRealD;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

import static com.interworldtransport.cladosF.RealD.*;
import static org.junit.Assert.*;
import static com.interworldtransport.cladosG.MonadRealD.*;

public class MonadRealDTest
{
	RealD[]		cRF;
	MonadRealD	tM0;
	MonadRealD	tM1;
	MonadRealD	tM2;
	MonadRealD	tM3;
	MonadRealD	tM4;
	MonadRealD	tM5;
	MonadRealD	tM6;
	MonadRealD	tM7;
	MonadRealD	tM8;

	@Before
	public void setUp() throws BadSignatureException, CladosMonadException
	{
		cRF = new RealD[16];
		DivFieldType tSpot = new DivFieldType("TestRealFs");
		for (int k = 0; k < 16; k++)
			cRF[k] = new RealD(tSpot, (float) k);

		tM0 = new MonadRealD("Test MonadRealF 0", "Motion Algebra",
						"Foot Default Frame", "Test Foot 0", "-+++", new RealD(
										new DivFieldType("Test Float 1"), 0f));
		tM1 = new MonadRealD("Test MonadRealF 1", "Property Algebra",
						"Foot Default Frame", "Test Foot 1", "-+++", new RealD(
										new DivFieldType("Test Float 1"), 0f));
		tM2 = new MonadRealD("Test MonadRealF 2", tM1);
		tM3 = new MonadRealD("Test MonadRealF 3", tM1);
		tM4 = new MonadRealD(tM0);
		tM5 = new MonadRealD("Test MonadRealF 5", "Motion Algebra",
						"Foot Default Frame", "Test Foot 5", "-+++", new RealD(
										new DivFieldType("Test Float 5"), 0f),
						"Unit PScalar");
		tM6 = new MonadRealD("Test MonadRealF 6", "Property Algebra",
						"Foot Default Frame", "Test Foot 6", "-+++", cRF);
		tM7 = new MonadRealD(tM6);
		tM8 = new MonadRealD(tM6);
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
		assertTrue(isIdempotentMultiple(tM4));
		assertTrue(isNilpotent(tM2));
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
		assertTrue(isGZero(tM5.scale(RealD.ZERO(tM5.getCoeff(0)))));
		assertTrue(tM6.invert().invert().isGEqual(tM7));
		assertTrue(tM6.reverse().reverse().isGEqual(tM7));
		assertTrue(isEqual(tM6.normalize().magnitude(),
						RealD.ONE(tM7.getCoeff(0))));
		assertTrue(hasGrade(tM6, 2));
		assertFalse(hasGrade(tM7, 0));
	}

	@Test
	public void testBiMathOps1() throws FieldBinaryException,
					CladosMonadBinaryException, CladosMonadException
	{
		tM6.add(tM7);
		tM7.scale(new RealD(tM6.getCoeff(0), 2.0f));
		assertTrue(tM6.isGEqual(tM7));
		tM6.subtract(tM7)
						.subtract(tM7)
						.scale(new RealD(tM7.getCoeff(0).getFieldType(), -1.0f));
		assertTrue(tM6.isGEqual(tM7));
		
	}
	@Test
	public void testBiMathOps2() throws FieldBinaryException,
					CladosMonadBinaryException, CladosMonadException
	{
		tM8.gradePart((short) 4).normalize();
		
		tM6.multiplyLeft(tM8).dualLeft();
		tM6.scale(new RealD(tM6.getCoeff(0), -1f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM6.multiplyRight(tM8).dualRight();
		tM6.scale(new RealD(tM6.getCoeff(0), -1f));
		assertTrue(tM6.isGEqual(tM7));
		
		tM5.setCoeff(tM6.getCoeff());
		assertFalse(tM5.isGEqual(tM6));
		
		tM6.multiplySymm(tM8);
		tM6.scale(new RealD(tM6.getCoeff(0), -1f));
		System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));
		
		tM6.setCoeff(tM7.getCoeff());
		tM6.multiplyAntisymm(tM8);
		tM6.scale(new RealD(tM6.getCoeff(0), -1f));
		//System.out.println(toXMLString(tM6));
		assertFalse(tM6.isGEqual(tM7));
	}
}
