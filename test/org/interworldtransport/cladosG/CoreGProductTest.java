package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
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
	String pSig16 = "-+++-+++-+++-+++";

	String pSig30 = "+++0";

	@Test
	public void testCachedGP() throws BadSignatureException, GeneratorRangeException {
		CliffordProduct tGP1 = CladosGBuilder.createGProduct(pSig3);
		assertTrue(CladosGCache.INSTANCE.getGProductListSize() == 1); 	//The builder cached it
		CliffordProduct tGP2 = CladosGBuilder.createGProduct(pSig3);	//Same sig so a repeat
		assertTrue(tGP1 == tGP2);	//The builder noticed a GP with the same sign and returned it instead

		CliffordProduct tGP3 = CladosGBuilder.createGProduct(pSig4);	//Inverted sig this time. different GP.
		assertTrue(CladosGCache.INSTANCE.getGProductListSize() == 2); 	//The builder cached it
		CladosGCache.INSTANCE.removeGProduct(pSig3);					//Remove the gp named by its signature	
		assertTrue(CladosGCache.INSTANCE.getGProductListSize() == 1); 	//Found the first GP and removed it.
		CladosGCache.INSTANCE.removeGProduct(pSig3);					//Remove the gp named by its signature	
		assertTrue(CladosGCache.INSTANCE.getGProductListSize() == 1); 	//Not found and silently handled.
		CladosGCache.INSTANCE.removeGProduct(tGP3);						//Remove the gp named by reference.
		assertTrue(CladosGCache.INSTANCE.getGProductListSize() == 0); 	//Found the second GP and removed it.
	}

	@Test
	public void testValidations() {
		assertTrue(CliffordProduct.validateSignature(pSig4));
		assertFalse(CliffordProduct.validateSignature(pSig16)); //Beyond supported size right now.
		assertFalse(CliffordProduct.validateSignature(pSig30)); //Can't support PGA yet.

		assertTrue(CliffordProduct.validateSignature("")); //Allowed. No generators.
		assertFalse(CliffordProduct.validateSignature(null)); //Disallowed. No Info != No generators.
	}
	
	@Test
	public void testSigns() {
		try {
			GProduct tGP = new GProduct(pSig4);
			//System.out.println(tGP.toXMLString(""));
			assertTrue(tGP.getACommuteSign(1, 2) == 1); //They anticommute
			assertFalse(tGP.getACommuteSign(1, 12) == 1); //They commute
			assertTrue(tGP.getCommuteSign(1, 11) == 1); //They commute
			assertFalse(tGP.getCommuteSign(1, 3) == 1); //They anticommute

			assertTrue(tGP.getSign(1, 2) == 1); //Should be positive on row 1
			assertTrue(tGP.getSign(2, 1) == -1); //Should be neg to get anticommute
			assertTrue(tGP.getResult(15, 15) == -1); //PScalar squares to -1.
			assertTrue(tGP.signature().length() == 4);
		} catch (BadSignatureException esig) {
			;
		} catch (GeneratorRangeException egen) {
			;
		}
	}

	@Test
	public void testThrows() {
		assertThrows(BadSignatureException.class, () -> new GProduct(pSig30));
	}

	@Test
	public void testRanges() throws BadSignatureException, GeneratorRangeException {
		try {
			GProduct tGP = (GProduct) CladosGBuilder.createGProduct(pSig8);
			int[] pRange = tGP.getPScalarRange();
			assertTrue(pRange[0] == pRange[1]);
			assertTrue(pRange[0] == 255);
			pRange = tGP.getGradeRange((byte) 2);
			assertTrue(pRange[0] == 9);
			assertTrue(pRange[1] == 36);
			pRange = tGP.getGradeRange((byte) 8);
			assertTrue(pRange[0] == pRange[1]);
			assertTrue(pRange[0] == 255);

		} catch (BadSignatureException esig) {
			;
		} catch (GeneratorRangeException egen) {
			;
		}
	}

	@Test
	public void testXMLOutput() {
		try {
			GProduct tGP = (GProduct) CladosGBuilder.createGProduct(pSig10);
			System.out.println(tGP.toXMLString(""));
		} catch (BadSignatureException esig) {
			;
		} catch (GeneratorRangeException egen) {
			;
		}
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