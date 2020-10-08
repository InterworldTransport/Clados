package com.interworldtransport.cladosGTest;


import com.interworldtransport.cladosG.Foot;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosF.Cardinal;

import org.junit.*;

import static org.junit.Assert.*;

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
		fType = new Cardinal("Test:NumberType");
		rNumber = new RealD(fType,0.0);
		tFoot = new Foot(fName, fType);
		// A foot can be created with a raw number type of no magnitude
		tFoot2 = new Foot(fName, rNumber);
		// It can also be created using an actual number without having
		// to drill down to get to the type of division field being used
		// because a Foot doesn't care about the magnitude of the number.
	}

	@Test
	public void testAppendReferenceFrame()
	{
		assertFalse(tFoot.equals(null));
		assertTrue(tFoot.getReferenceFrames().size() == 1);
		tFoot.appendIfUniqueRFrame(fName + "-Spherical");
		assertTrue(tFoot.getReferenceFrames().size() == 2);
	}

	@Test
	public void testRemoveReferenceFrame()
	{
		assertTrue(tFoot.getReferenceFrames().size() == 1);
		tFoot.appendIfUniqueRFrame(fName + "-Spherical2");
		assertTrue(tFoot.getReferenceFrames().size() == 2);
		tFoot.removeRFrames(fName + "-Spherical2");
		assertTrue(tFoot.getReferenceFrames().size() == 1);
		tFoot.removeRFrames("Un-named frame that shouldn't be found.");
		assertTrue(tFoot.getReferenceFrames().size() == 1);
		//Attempting to remove a frame that isn't there silently moves on.
		//If one needs to ensure the frame was there and confirm it's 
		//removal, one should find it first.
		assertTrue(tFoot.getReferenceFrames()
				.indexOf("Un-named frame that shouldn't be found.") == -1);
	}
	
	@Test
	public void testFootCompare()
	{
		assertFalse(tFoot == tFoot2); 
		// Two different feet
		assertTrue(tFoot.getNumberType() == tFoot2.getNumberType());
		// using the same number type.
		// The algebras relying on these feet would fail reference checks
		// but they are allowed to re-use each other's numbering system.
	}
}
