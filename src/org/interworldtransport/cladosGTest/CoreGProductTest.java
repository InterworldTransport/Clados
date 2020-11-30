package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.GProduct;
import org.interworldtransport.cladosG.GProductMap;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreGProductTest {
	String pSig0 = "";
	String pSig2 = "-+";
	String pSig3 = "+++";
	String pSig4 = "-+++";
	String pSig8 = "-+++-+++";
	String pSig10 = "+++-++++++";
	String pSig12 = "-+++-+++-+++";
	String pSig14 = "++-+++-+++-+++";

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void test00s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig0);
		assertTrue(tGP.getSignature().equals(""));
		assertTrue(tGP.getGradeCount() == 1);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 0));

		int tS = (int) Math.pow(2, 0);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}

		GProduct tGPClone = new GProduct(tGP);
		assertTrue(tGPClone.getBasis().equals(tGP.getBasis()));
		assertFalse(tGPClone.getResult().equals(tGP.getResult()));
	}

	@Test
	public void test02s() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		GProduct tGP = new GProduct(pSig2);
		//System.out.println(tGP.toXMLString());
		assertTrue(tGP.getSignature().equals("-+"));
		assertTrue(tGP.getGradeCount() == 3);
		assertTrue(tGP.getBladeCount() == (1 << 2));

		int tS = (1 << 2);
		int tSum = (tS * (tS + 1) / 2);
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}
	
	@Test
	public void test03s() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		GProduct tGP = new GProduct(pSig3);
		System.out.println(tGP.toXMLString());
		assertTrue(tGP.getSignature().equals("+++"));
		assertTrue(tGP.getGradeCount() == 4);
		assertTrue(tGP.getBladeCount() == (1<<3));

		int tS = 1<<3;
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test04s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig4);
		//System.out.println(tGP.toXMLString());
		assertTrue(tGP.getSignature().equals("-+++"));
		assertTrue(tGP.getGradeCount() == 5);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 4));

		int tS = (int) Math.pow(2, 4);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}

		GProduct tGPClone = new GProduct(tGP);
		assertTrue(tGPClone.getBasis().equals(tGP.getBasis()));
		assertFalse(tGPClone.getResult().equals(tGP.getResult()));

	}
/*
	@Test
	public void test08s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig8);
		assertTrue(tGP.getSignature().equals("-+++-+++"));
		assertTrue(tGP.getGradeCount() == 9);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 8));

		int tS = (int) Math.pow(2, 8);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test10s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig10);
		assertTrue(tGP.getSignature().equals("+++-++++++"));
		assertTrue(tGP.getGradeCount() == 11);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 10));

		int tS = (int) Math.pow(2, 10);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test12s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig12);
		assertTrue(tGP.getSignature().equals("-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 13);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 12));

		int tS = (int) Math.pow(2, 12);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}

	@Test
	public void test14s() throws CladosMonadException, BadSignatureException, GeneratorRangeException {
		GProduct tGP = new GProduct(pSig14);
		assertTrue(tGP.getSignature().equals("++-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 15);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 14));

		int tS = (int) Math.pow(2, 14);
		int tSum = tS * (tS + 1) / 2;
		for (short k = 0; k < tGP.getBladeCount(); k++) {
			short[] tSpot = tGP.getResult(k);
			int tSumP = 0;
			for (int j = 0; j < tSpot.length; j++)
				tSumP += Math.abs(tSpot[j]);

			assertTrue(tSum == tSumP);
		}
	}
*/
}
