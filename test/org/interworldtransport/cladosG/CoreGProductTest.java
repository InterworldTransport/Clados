package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

//import org.interworldtransport.cladosG.CladosGBuilder;
//import org.interworldtransport.cladosG.CladosGCache;
//import org.interworldtransport.cladosG.CliffordProduct;
//import org.interworldtransport.cladosG.GProduct;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreGProductTest {
	String pSig0 = "";
	String pSig1 = "+";
	String pSig2 = "-+";
	String pSig3 = "+++";
	String pSig4 = "-+++";
	String pSig8 = "-+++-+++";
	String pSig10 = "+++-++++++";
	String pSig12 = "-+++-+++-+++";
	String pSig14 = "++-+++-+++-+++";
	String pSig15 = "+++-+++-+++-+++";
	//String pSig16 = "-+++-+++-+++-+++";

	@BeforeEach
	public void setUp() {
	}

	@Test
	public void testCachedGP() throws BadSignatureException, GeneratorRangeException {
		CliffordProduct tGP1 = CladosGBuilder.createGProduct(pSig3);
		assertTrue(CladosGCache.INSTANCE.getGProductListSize()>0);
		CliffordProduct tGP2 = CladosGBuilder.createGProduct(pSig3);
		assertTrue(tGP1 == tGP2);
	}

	@Test
	public void testValidations() {
		assertTrue(CanonicalBasis.validateSize(0));
		assertFalse(CanonicalBasis.validateSize(-1));
		assertFalse(CanonicalBasis.validateSize(17));
		assertFalse(CanonicalBasis.validateSize(16));
	}
	
	
	@Test
	public void test00s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig0);
		assertTrue(tGP.signature().equals(""));
		assertTrue(tGP.getGradeCount() == 1);
		assertTrue(tGP.getBladeCount() == (1 << 0));
		//System.out.println(tGP.toXMLString(""));
		int tS = (1 << 0);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}
	
	@Test
	public void test01s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig1);
		assertTrue(tGP.signature().equals("+"));
		assertTrue(tGP.getGradeCount() == 2);
		assertTrue(tGP.getBladeCount() == (1 << 1));
		//System.out.println(tGP.toXMLString(""));
		int tS = (1 << 1);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test02s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig2);
		//System.out.println(tGP.toXMLString(""));
		assertTrue(tGP.signature().equals("-+"));
		assertTrue(tGP.getGradeCount() == 3);
		assertTrue(tGP.getBladeCount() == (1 << 2));
		int tS = (1 << 2);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	
	@Test
	public void test03s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig3);
		//System.out.println(tGP.toXMLString(""));
		assertTrue(tGP.signature().equals("+++"));
		assertTrue(tGP.getGradeCount() == 4);
		assertTrue(tGP.getBladeCount() == (1<<3));

		int tS = 1<<3;
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test04s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig4);
		//System.out.println(tGP.toXMLString(""));
		assertTrue(tGP.signature().equals("-+++"));
		assertTrue(tGP.getGradeCount() == 5);
		assertTrue(tGP.getBladeCount() == (1<<4));

		int tS = (int) Math.pow(2, 4);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test08s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig8);
		assertTrue(tGP.signature().equals("-+++-+++"));
		assertTrue(tGP.getGradeCount() == 9);
		assertTrue(tGP.getBladeCount() == (1<<8));

		int tS = (int) Math.pow(2, 8);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test10s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig10);
		assertTrue(tGP.signature().equals("+++-++++++"));
		assertTrue(tGP.getGradeCount() == 11);
		assertTrue(tGP.getBladeCount() == (1<<10));

		int tS = (int) Math.pow(2, 10);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test12s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig12);
		assertTrue(tGP.signature().equals("-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 13);
		assertTrue(tGP.getBladeCount() == (1<<12));

		int tS = (int) Math.pow(2, 12);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test14s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig14);
		assertTrue(tGP.signature().equals("++-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 15);
		assertTrue(tGP.getBladeCount() == (1<<14));

		int tS = (int) Math.pow(2, 14);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}
	
	@Test
	public void test15s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig15);
		assertTrue(tGP.signature().equals("+++-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 16);
		assertTrue(tGP.getBladeCount() == (1<<15));

		int tS = (int) Math.pow(2, 15);
		int tSum = tS * (tS + 1) / 2;
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			int[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}
/* 
	@Test
	public void test16s() throws BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig16);
		assertTrue(tGP.signature().equals("-+++-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 17);
		assertTrue(tGP.getBladeCount() == (1<<16));

		//long tS = 65536;
		long tSum = 65536 * 65537 / 2;
		
		System.out.println("Cayley rows should sum to: "+tSum);

		int[] firstRow = tGP.getResult(0);
		long tSumP = 0;
		for (int j = 0; j < firstRow.length; j++)
			tSumP += (long) Math.abs(firstRow[j]);
		System.out.println("Cayley-FirstRow---"+tSumP);

		int[] lastRow = tGP.getResult(tGP.getBladeCount()-1);
		tSumP = 0;
		for (int j = 0; j < lastRow.length; j++)
			tSumP += (long) Math.abs(lastRow[j]);
		System.out.println("Cayley-LastRow---"+tSumP);
*/

		/*
		for (int k = 0; k < tGP.getBladeCount(); k++) {
			//int[] tSpot = tGP.getResult(k);
			long tSumP = 0;
			for (int j = 0; j < tGP.getResult(k).length; j++)
				tSumP += (long) Math.abs(tGP.getResult(k, j));

			assertTrue(tSum == tSumP);
		}
		
	}
*/
}