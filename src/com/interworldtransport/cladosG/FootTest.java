package com.interworldtransport.cladosG;

import org.junit.*;
import com.interworldtransport.cladosF.*;
import static org.junit.Assert.*;

public class FootTest
{
	public String		fName	= "Test:TangentPoint";
	public DivFieldType	fType;
	public Foot			tFoot;

	@Before
	public void setUp()
	{
		fType = new DivFieldType(fName);
		tFoot = new Foot(fName, fType);
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
	}
}
