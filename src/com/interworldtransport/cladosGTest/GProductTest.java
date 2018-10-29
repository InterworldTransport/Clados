package com.interworldtransport.cladosGTest;

import org.junit.*;

import com.interworldtransport.cladosG.GProduct;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

import static org.junit.Assert.*;

public class GProductTest
{
	GProduct	tGP4;
	GProduct	tGP8;
	//GProduct	tGP10;
	GProduct	tGP12;

	@Before
	public void setUp() throws CladosMonadException, BadSignatureException
	{
		tGP4 = new GProduct("-+++");
		tGP8 = new GProduct("-+++-+++");
		//tGP10 = new GProduct("-++++-++++");
		tGP12 = new GProduct("-+++-+++-+++");
	}

	@Test
	public void testGradeCount()
	{
		assertTrue(tGP4.getGradeCount() == 5);
		assertTrue(tGP8.getGradeCount() == 9);
		//assertTrue(tGP10.getGradeCount() == 11);
		assertTrue(tGP12.getGradeCount() == 13);
	}

	@Test
	public void testBladeCount()
	{
		assertTrue(tGP4.getBladeCount() == Math.pow(2, 4));
		assertTrue(tGP8.getBladeCount() == Math.pow(2, 8));
		//assertTrue(tGP10.getBladeCount() == Math.pow(2, 10));
		assertTrue(tGP12.getBladeCount() == Math.pow(2, 12));
	}

	@Test
	public void testProductResult()
	{
		int tS=(int)Math.pow(2,12);
		int tSum=tS*(tS+1)/2;
		for (short k=0; k<tGP12.getBladeCount(); k++)
		{
			short[] tSpot=tGP12.getResult(k);
			int tSumP=0;
			for (int j=0; j<tSpot.length; j++)
			{
				tSumP += Math.abs(tSpot[j]);
			}
			assertTrue(tSum==tSumP);
		}
		
		tS=(int)Math.pow(2,8);
		tSum=tS*(tS+1)/2;
		for (short k=0; k<tGP8.getBladeCount(); k++)
		{
			short[] tSpot=tGP8.getResult(k);
			int tSumP=0;
			for (int j=0; j<tSpot.length; j++)
			{
				tSumP += Math.abs(tSpot[j]);
			}
			assertTrue(tSum==tSumP);
		}
		
		tS=(int)Math.pow(2,4);
		tSum=tS*(tS+1)/2;
		for (short k=0; k<tGP4.getBladeCount(); k++)
		{
			short[] tSpot=tGP4.getResult(k);
			int tSumP=0;
			for (int j=0; j<tSpot.length; j++)
			{
				tSumP += Math.abs(tSpot[j]);
			}
			assertTrue(tSum==tSumP);
		}
		
	}
}
