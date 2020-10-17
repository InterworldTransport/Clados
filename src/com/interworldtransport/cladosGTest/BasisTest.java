package com.interworldtransport.cladosGTest;

import org.junit.*;

import com.interworldtransport.cladosG.Basis;
import com.interworldtransport.cladosGExceptions.CladosMonadException;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;

import static org.junit.Assert.*;

public class BasisTest
{
	Basis	tBasis0;
	Basis	tBasis4;
	Basis	tBasis42;
	Basis	tBasis43;
	Basis	tBasis8;
	Basis	tBasis10;
	Basis	tBasis14;

	@Before
	public void setUp() throws CladosMonadException, GeneratorRangeException
	{
		tBasis0 = new Basis((short) 0);
		tBasis4 = new Basis((short) 4);
		tBasis42 = new Basis(tBasis4);
		tBasis43 = new Basis((short) 4);
		tBasis8 = new Basis((short) 8);
		tBasis10 = new Basis((short) 10);
		tBasis14 = new Basis((short) 14);
	}

	@Test
	public void testGradeCount()
	{
		assertTrue(tBasis0.getGradeCount() == 1);
		assertTrue(tBasis4.getGradeCount() == 5);
		assertTrue(tBasis8.getGradeCount() == 9);
		assertTrue(tBasis10.getGradeCount() == 11);
		assertTrue(tBasis14.getGradeCount() == 15);
	}
	
	@Test
	public void testBladeCount()
	{
		assertTrue(tBasis0.getBladeCount() == Math.pow(2,0));
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
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[14-k+1] - tSpot[14-k]);
		
		tSpot = tBasis10.getGradeRange();	
		for (int k=1; k<0.5*(tBasis10.getGradeCount()-1); k++)
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[10-k+1] - tSpot[10-k]);
		
		tSpot = tBasis8.getGradeRange();	
		for (int k=1; k<0.5*(tBasis8.getGradeCount()-1); k++)
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[8-k+1] - tSpot[8-k]);
		
		tSpot = tBasis4.getGradeRange();	
		for (int k=1; k<0.5*(tBasis4.getGradeCount()-1); k++)
			assertTrue(tSpot[k+1] - tSpot[k] == tSpot[4-k+1] - tSpot[4-k]);
		
		tSpot = tBasis0.getGradeRange();
		assertTrue(tSpot[0]==0);
		
	}
	
	@Test
	public void testIndependence()
	{
		assertTrue(tBasis4.getBasis().equals(tBasis42.getBasis()));
		// Using the copy constructor results in both Basis objects sharing vBasis
		assertFalse(tBasis4.getBasis().equals(tBasis43.getBasis()));
		// Using the raw constructor results in both Basis object not sharing a vBasis.
	}
}
