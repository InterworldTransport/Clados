package org.interworldtransport.cladosGTest;

import org.junit.*;
import static org.junit.Assert.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosG.MonadComplexD;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public class MonadComplexDOverheadTest 
{
	String		fType="TestMonadComplexDs";
	String		mName="Test MonadComplexD ";
	String		ftName="Foot Default";
	String		aName="Motion Algebra";
	ComplexD[]		cRF;
	MonadComplexD	tM0;
	
	@Test
	public void testGen01() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=1;
			int bladeCount= (int) Math.pow(2, pGen);
			
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
			
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
			
			tM0=new MonadComplexD(	mName+"0", 
								aName,
								ftName+" Frame", 
								ftName, 
								"+", 
								cRF);
			assertFalse(tM0 == null);
		}
		
	}
	@Test
	public void testGen02() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=2;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen03() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=3;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen04() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10000; m++)
		{
			int pGen=4;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot =Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen05() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<1000; m++)
		{
			int pGen=5;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen06() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<1000; m++)
		{
			int pGen=6;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen07() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<100; m++)
		{
			int pGen=7;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen08() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<100; m++)
		{
			int pGen=8;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	
	@Test
	public void testGen09() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10; m++)
		{
			int pGen=9;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"+-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen10() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10; m++)
		{
			int pGen=10;
			int bladeCount= (int) Math.pow(2, pGen);
		
			cRF = new ComplexD[bladeCount];
			Cardinal tSpot = Cardinal.generate(fType);
		
			for (short k = 0; k < bladeCount; k++)
				cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
			tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++-+++", 
							cRF);
			assertFalse(tM0 == null);
		}
	}
	@Test
	public void testGen11() throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{
		for (short m=0; m<10; m++)
		{
		int pGen=11;
		int bladeCount= (int) Math.pow(2, pGen);
		
		cRF = new ComplexD[bladeCount];
		Cardinal tSpot = Cardinal.generate(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
		tM0=new MonadComplexD(	mName+"0", 
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
		
		cRF = new ComplexD[bladeCount];
		Cardinal tSpot = new Cardinal(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
		tM0=new MonadComplexD(	mName+"0", 
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
		
		cRF = new ComplexD[bladeCount];
		Cardinal tSpot = new Cardinal(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
		tM0=new MonadComplexD(	mName+"0", 
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
		
		cRF = new ComplexD[bladeCount];
		Cardinal tSpot = new Cardinal(fType);
		
		for (short k = 0; k < bladeCount; k++)
			cRF[k] = new ComplexD(tSpot, (double) k, (double) k);
		
		tM0=new MonadComplexD(	mName+"0", 
							aName,
							ftName+" Frame", 
							ftName, 
							"++-+++-+++-+++", 
							cRF);
		assertFalse(tM0 == null);
	}
	*/
}