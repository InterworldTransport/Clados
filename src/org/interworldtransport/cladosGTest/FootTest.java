package org.interworldtransport.cladosGTest;


import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosG.Foot;

public class FootTest
{
	public String		fName	= "Test:TangentPoint";
	public Cardinal	fType;
	public RealD		rNumber;
	public Foot			tFoot;
	public Foot			tFoot2;

	@Before
	public void setUp()
	{
		fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealD(fType,0.0);
		tFoot = new Foot(fName, fType);
		// A foot can be created with a raw number type of no magnitude
		tFoot2 = new Foot(fName, rNumber);
		// It can also be created using an actual number without having
		// to drill down to get to the type of division field being used
		// because a Foot doesn't care about the magnitude of the number.
	}


	
	@Test
	public void testFootCompare()
	{
		assertFalse(tFoot == tFoot2); 
		// Two different feet
		assertTrue(tFoot.getCardinal(0) == tFoot2.getCardinal(0));
		// using the same number type.
		// The algebras relying on these feet would fail reference checks
		// but they are allowed to re-use each other's numbering system.
	}
}
