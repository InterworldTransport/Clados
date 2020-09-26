package com.interworldtransport.cladosGTest;

import org.junit.*;
import static org.junit.Assert.*;

import com.interworldtransport.cladosF.DivFieldType;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosG.MonadRealF;

import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

public class MonadRealFOverheadTest 
{
	String		fType="TestMonadRealFs";
	String		mName="Test MonadRealF ";
	String		ftName="Foot Default";
	String		aName="Motion Algebra";
	RealF[]		cRF;
	MonadRealF	tM0;
	
	
	@Test
	public void testGen00() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=0;
			int bladeCount= (int) Math.pow(2, pGen);
			
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
			
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
			
			tM0=new MonadRealF(	mName+"0", 
								aName,
								ftName+" Frame", 
								ftName, 
								"", 
								cRF);
			assertFalse(tM0 == null);
		}
		
	}
	@Test
	public void testGen01() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=1;
			int bladeCount= (int) Math.pow(2, pGen);
			
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
			
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
			
			tM0=new MonadRealF(	mName+"0", 
								aName,
								ftName+" Frame", 
								ftName, 
								"+", 
								cRF);
			assertFalse(tM0 == null);
		}
		
	}
	@Test
	public void testGen02() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=2;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen03() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=3;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen04() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=4;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen05() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<1000; m++)
		{
			int pGen=5;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen06() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<1000; m++)
		{
			int pGen=6;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen07() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<100; m++)
		{
			int pGen=7;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen08() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<100; m++)
		{
			int pGen=8;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	
	@Test
	public void testGen09() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10; m++)
		{
			int pGen=9;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen10() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10; m++)
		{
			int pGen=10;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new RealF[bladeCount];
			DivFieldType tSpot = new DivFieldType(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new RealF(tSpot, (float) k);
		
			tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen11() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10; m++)
		{
		int pGen=11;
		int bladeCount= (int) Math.pow(2, pGen);
		
		cRF = new RealF[bladeCount];
		DivFieldType tSpot = new DivFieldType(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new RealF(tSpot, (float) k);
		
		tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+++-+++-+++", 
							cRF);
		assertFalse(tM0 == null);
		}
	}
	/*
	@Test
	public void testGen12() throws BadSignatureException, CladosMonadException
	{
		for (short m=0; m<10; m++)
		{
		int pGen=12;
		int bladeCount= (int) Math.pow(2, pGen);
		
		cRF = new RealF[bladeCount];
		DivFieldType tSpot = new DivFieldType(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new RealF(tSpot, (float) k);
		
		tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"-+++-+++-+++", 
							cRF);
		assertFalse(tM0 == null);
		}
	}

	@Test
	public void testGen13() throws BadSignatureException, CladosMonadException
	{
		int pGen=13;
		int bladeCount= (int) Math.pow(2, pGen);
		
		cRF = new RealF[bladeCount];
		DivFieldType tSpot = new DivFieldType(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new RealF(tSpot, (float) k);
		
		tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+-+++-+++-+++", 
							cRF);
		assertFalse(tM0 == null);
	}
	
	@Test
	public void testGen14() throws BadSignatureException, CladosMonadException
	{
		int pGen=14;
		int bladeCount= (int) Math.pow(2, pGen);
		
		cRF = new RealF[bladeCount];
		DivFieldType tSpot = new DivFieldType(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new RealF(tSpot, (float) k);
		
		tM0=new MonadRealF(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++-+++-+++", 
							cRF);
		assertFalse(tM0 == null);
	}
	*/
}
