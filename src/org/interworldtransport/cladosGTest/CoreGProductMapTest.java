package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.GProductMap;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreGProductMapTest {
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
/*
	@Test
	public void test00s() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		GProductMap tGP = new GProductMap(pSig0);
		assertTrue(tGP.signature().equals(""));
		assertTrue(tGP.getGradeCount() == 1);
		assertTrue(tGP.getBladeCount() == (1 << 0));

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
*/	
	@Test
	public void test02s() throws BadSignatureException, GeneratorRangeException {
		GProductMap tGP = new GProductMap(pSig2);
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
		GProductMap tGP = new GProductMap(pSig3);
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
		GProductMap tGP = new GProductMap(pSig4);
		//System.out.println(tGP.toXMLString(""));
		assertTrue(tGP.signature().equals("-+++"));
		assertTrue(tGP.getGradeCount() == 5);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 4));

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
		GProductMap tGP = new GProductMap(pSig8);
		assertTrue(tGP.signature().equals("-+++-+++"));
		assertTrue(tGP.getGradeCount() == 9);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 8));

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
		GProductMap tGP = new GProductMap(pSig10);
		assertTrue(tGP.signature().equals("+++-++++++"));
		assertTrue(tGP.getGradeCount() == 11);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 10));

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
		GProductMap tGP = new GProductMap(pSig12);
		assertTrue(tGP.signature().equals("-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 13);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 12));

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
		GProductMap tGP = new GProductMap(pSig14);
		assertTrue(tGP.signature().equals("++-+++-+++-+++"));
		assertTrue(tGP.getGradeCount() == 15);
		assertTrue(tGP.getBladeCount() == Math.pow(2, 14));

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

}
