package com.interworldtransport.cladosG;

import org.junit.*;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

import static org.junit.Assert.*;

public class BasisTest
{
	Basis	tBasis4;
	Basis	tBasis8;
	Basis	tBasis10;
	Basis	tBasis14;

	@Before
	public void setUp() throws CladosMonadException
	{
		tBasis4 = new Basis((short) 4);
		tBasis8 = new Basis((short) 8);
		tBasis10 = new Basis((short) 10);
		tBasis14 = new Basis((short) 14);
	}

	@Test
	public void testGradeCount()
	{
		assertTrue(tBasis4.getGradeCount() == 5);
		assertTrue(tBasis8.getGradeCount() == 9);
		assertTrue(tBasis10.getGradeCount() == 11);
		assertTrue(tBasis14.getGradeCount() == 15);
	}
	
	@Test
	public void testBladeCount()
	{
		assertTrue(tBasis4.getBladeCount() == Math.pow(2,4));
		assertTrue(tBasis8.getBladeCount() == Math.pow(2,8));
		assertTrue(tBasis10.getBladeCount() == Math.pow(2,10));
		assertTrue(tBasis14.getBladeCount() == Math.pow(2,14));
	}

	@Test
	public void testGradeRange()
	{
		short[] tSpot = tBasis14.getGradeRange();	
		for (int k=1; k<0.5*(tBasis14.getGradeCount()-1); k++)
		{
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[14-k+1] - tSpot[14-k]);
		}
		tSpot = tBasis10.getGradeRange();	
		for (int k=1; k<0.5*(tBasis10.getGradeCount()-1); k++)
		{
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[10-k+1] - tSpot[10-k]);
		}
		tSpot = tBasis8.getGradeRange();	
		for (int k=1; k<0.5*(tBasis8.getGradeCount()-1); k++)
		{
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[8-k+1] - tSpot[8-k]);
		}
		tSpot = tBasis4.getGradeRange();	
		for (int k=1; k<0.5*(tBasis4.getGradeCount()-1); k++)
		{
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[4-k+1] - tSpot[4-k]);
		}

	}
}
