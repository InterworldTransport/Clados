package com.interworldtransport.cladosGTest;

import org.junit.*;
import static org.junit.Assert.*;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosG.*;

import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

public class AlgebraComplexDTest 
{

	protected String			fName	= "Test:TangentPoint";
	protected String			aName	= "Test Algebra";
	protected String			pSig31	= "-+++";
	protected String			pSig13	= "+---";
	protected DivFieldType		fType;
	protected ComplexD			rNumber;
	protected Foot				tFoot;
	protected Foot				tFoot2;
	protected GProduct			gProduct;
	protected AlgebraComplexD	alg1;
	protected AlgebraComplexD	alg2;
	protected AlgebraComplexD	alg3;
	
	@Before
	public void setUp() throws CladosMonadException, BadSignatureException
	{
		fType = new DivFieldType("Test:NumberType");
		rNumber = new ComplexD(fType, 0.0d, 1.0d);
		tFoot = new Foot(fName, fType);
		tFoot2 = new Foot(fName, rNumber);
		
		alg1= new AlgebraComplexD(aName, tFoot, pSig31);
		alg2= new AlgebraComplexD(aName, tFoot, pSig13);
	}

	@Test
	public void testCompareCores() throws CladosMonadException, BadSignatureException
	{
		assertTrue(alg1.getFoot() == alg2.getFoot());
		assertFalse(alg1.getGBasis() == alg2.getGBasis());
		assertFalse(alg1.getGProduct() == alg2.getGProduct());
		//Two algebras share the foot, but use different signatures
		//to overlay metrics on their coordinate systems.
		
		tFoot.setNumberType(rNumber.getFieldType());
		alg3= new AlgebraComplexD(aName, tFoot, pSig31);
		assertTrue(alg1.getFoot() == alg3.getFoot());
		assertTrue(alg1.getFoot() == alg2.getFoot());
		//because the Foot is shared between algebras, changing the number
		//type to use to build alg3 changes it for the others as well.
		
		alg3.setFoot(tFoot2);
		assertFalse(alg1.getFoot() == alg3.getFoot());
		//Both feet are essentially the same inside, but represented as two distinct objects.
		//That should cause this test to be false.
	}

	@Test
	public void testCompareSignatures()
	{
		assertFalse(alg1.getGProduct().getSignature().equals(alg2.getGProduct().getSignature()));
		//exposing the different signatures another way
	}
	
	@Test
	public void testCompareCounts()
	{
		assertTrue(alg1.getGProduct().getGradeCount() == alg2.getGProduct().getGradeCount());
		assertTrue(alg1.getGProduct().getBladeCount() == alg2.getGProduct().getBladeCount());
		//Different signatures does not lead to different grade and blade counts.
	}
	
	@Test
	public void testStaticOp()
	{
		ComplexD result=AlgebraComplexD.generateNumber(alg1, 5.0d, 10.0d);
		assertTrue(result != null);
		assertTrue(result.getFieldType() == alg1.getFoot().getNumberType());
		//this shows that an algebra can be used to generate numbers of the same type
		//by using the static method built into the class. This method is picky, but 
		//when used properly it will safely generate matches that will pass reference
		//tests.
	}

}
