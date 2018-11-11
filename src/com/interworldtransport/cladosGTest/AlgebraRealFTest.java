package com.interworldtransport.cladosGTest;

import org.junit.*;
import static org.junit.Assert.*;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosG.*;
//import static com.interworldtransport.cladosG.AlgebraRealF.*;

import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

public class AlgebraRealFTest 
{

	protected String		fName	= "Test:TangentPoint";
	protected String		aName	= "Test Algebra";
	protected String		pSig31	= "-+++";
	protected String		pSig13	= "+---";
	protected DivFieldType	fType;
	protected RealF			rNumber;
	protected Foot			tFoot;
	protected Foot			tFoot2;
	//protected GProduct		gProduct;
	protected AlgebraRealF	alg1;
	protected AlgebraRealF	alg2;
	
	@Before
	public void setUp() throws CladosMonadException, BadSignatureException
	{
		fType = new DivFieldType("Test:NumberType");
		rNumber = new RealF(fType,0.0f);
		tFoot = new Foot(fName, fType);
		tFoot2 = new Foot(fName, rNumber);
		
		alg1= new AlgebraRealF(aName, tFoot, pSig31);
		alg2= new AlgebraRealF(aName, tFoot, pSig13);
	}

	@Test
	public void testCompareCores() throws CladosMonadException, BadSignatureException
	{
		assertTrue(alg1.getFootPoint() == alg2.getFootPoint());
		assertFalse(alg1.getGBasis() == alg2.getGBasis());
		assertFalse(alg1.getGProduct() == alg2.getGProduct());
		//Two algebras share the foot, but use different signatures
		//to overlay metrics on their coordinate systems.
		
		tFoot.setNumberType(rNumber.getFieldType());
		AlgebraRealF alg3= new AlgebraRealF(aName, tFoot, pSig31);
		assertTrue(alg1.getFootPoint() == alg3.getFootPoint());
		assertTrue(alg1.getFootPoint() == alg2.getFootPoint());
		//because the Foot is shared between algebras, changing the number
		//type to use to build alg3 changes it for the others as well.
		
		alg3.setFootPoint(tFoot2);
		assertFalse(alg1.getFootPoint() == alg3.getFootPoint());
		//Both feet are essentially the same inside, but represented as two distinct objects.
		//That should cause this test to be false.
		
		AlgebraRealF alg4 = new AlgebraRealF("light weight frame", alg1.getFootPoint(), alg1.getGProduct());
		assertFalse(alg4.equals(alg1));
		assertTrue(alg4.getFootPoint().equals(alg1.getFootPoint()));
		assertTrue(alg4.getGProduct().equals(alg1.getGProduct()));
		//Foot re-used, GProduct re-used, but different names ensures algebra mis-match
		
		AlgebraRealF alg5 = new AlgebraRealF("medium weight frame", alg1.getFootPoint(), alg1.getGProduct().getSignature());
		assertFalse(alg5.equals(alg1));
		assertTrue(alg5.getFootPoint().equals(alg1.getFootPoint()));
		assertFalse(alg5.getGProduct().equals(alg1.getGProduct()));
		//Foot re-used, signature re-used... ensures different GProduct thus algebra mis-match
		assertFalse(alg5.getAlgebraName() == alg4.getAlgebraName());
		alg5.setAlgebraName(alg4.getAlgebraName());
		assertTrue(alg5.getAlgebraName() == alg4.getAlgebraName());
		assertFalse(alg5.equals(alg4));
		//Setting names equal isn't anywhere near enough to make algebras pass reference match
		
		AlgebraRealF alg6 = new AlgebraRealF(aName, fName, pSig31, rNumber);
		assertFalse(alg6.equals(alg1));
		assertFalse(alg6.getFootPoint().equals(alg1.getFootPoint()));
		assertFalse(alg6.getGProduct().equals(alg1.getGProduct()));
		assertTrue(alg6.getFootPoint().getNumberType().equals(alg1.getFootPoint().getNumberType()));
		//Number type re-used but nothing else ensures algebra mis-match
		
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
		RealF result=AlgebraRealF.generateNumber(alg1, 10.0f);
		assertTrue(result != null);
		assertTrue(result.getFieldType() == alg1.getFootPoint().getNumberType());
		//this shows that an algebra can be used to generate numbers of the same type
		//by using the static method built into the class. This method is picky, but 
		//when used properly it will safely generate matches that will pass reference
		//tests.
	}
	
	@Test
	public void testXMLOutput()
	{
		String test = AlgebraRealF.toXMLString(alg1);
		assertTrue(test != null);
	}

}
