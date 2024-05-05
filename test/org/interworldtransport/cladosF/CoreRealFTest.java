package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoreRealFTest {
	public RealF tReal0 = RealF.newZERO("Test:RealF");
	public RealF tReal1 = RealF.newONE("Test:RealF");
	public RealF tReal1n = new RealF(tReal1.getCardinal(), -1f);

	public RealF tReal2 = new RealF(tReal1.getCardinal(), Float.MAX_VALUE);
	public RealF tReal8 = new RealF(tReal1.getCardinal(), Float.MIN_VALUE);
	public RealF tReal4 = new RealF(tReal1.getCardinal(), Float.NaN);
	public RealF tReal5 = new RealF(tReal1.getCardinal(), Float.POSITIVE_INFINITY);
	public RealF tReal6 = new RealF(tReal1.getCardinal(), Float.NEGATIVE_INFINITY);

	public RealF[] tReals;

	

	@Test
	public void testCardinals() { 										//Really testing UnitAbstract class
		assertTrue(tReal0.getCardinal().equals(tReal1.getCardinal()));	//Same String
		assertFalse(tReal0.getCardinal() == tReal1.getCardinal());		//Different Objects
		assertTrue(tReal1.getCardinal().equals(tReal1n.getCardinal()));	//Same Strings again.
		assertTrue(tReal0.getCardinalString().equals(tReal1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() { 									//Really testing UnitAbstract class
		assertTrue(RealF.isTypeMatch(tReal0, tReal1));					//Tests the strings inside Cardinals
		assertFalse(tReal0.getCardinal() == tReal1.getCardinal());		//Proof the cardinals don't have to be re-used.
	}

	@Test
	public void testIsEqual() {
		assertFalse(RealF.isEqual(tReal0, tReal1));
	}

	@Test
	public void testIsZero() {
		assertTrue(RealF.isZero(tReal0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(RealF.isInfinite(tReal5));
		assertTrue(RealF.isInfinite(tReal6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(RealF.isNaN(tReal4));
	}

	@Test
	public void testGetReal() {
		float t1 = tReal1.getReal();
		float t1n = tReal1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tReal0.modulus() == 0f);
		assertTrue(tReal1.modulus() > 0f);
		assertTrue(Float.isNaN(tReal4.modulus()));
		assertTrue(Float.isInfinite(tReal5.modulus()));
		assertTrue(Float.isInfinite(tReal6.modulus()));
		assertTrue(Float.isInfinite(tReal2.modulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tReal0.sqModulus() == 0f);
		assertTrue(tReal1.sqModulus() > 0f);
		assertTrue(Float.isNaN(tReal4.sqModulus()));
		assertTrue(Float.isInfinite(tReal5.sqModulus()));
		assertTrue(Float.isInfinite(tReal6.sqModulus()));
		assertTrue(Float.isInfinite(tReal2.sqModulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(RealF.isEqual(tReal0.conjugate(), RealF.copyZERO(tReal0)));
		assertFalse(RealF.isEqual(tReal1.conjugate(), tReal1n));
		RealF tR = RealF.conjugate(tReal0);
		assertTrue(RealF.isEqual(tR, tReal0));
	}

	@Test
	public void testScale() {
		assertTrue(RealF.isEqual(tReal1n.scale(Float.valueOf(-1f)), tReal1));
		tReal1n.scale(Float.valueOf(-1f));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(RealF.isZero(RealF.add(tReal1, tReal1n)));
		assertTrue(RealF.isZero(tReal1.add(tReal1n)));
		assertFalse(RealF.isEqual(tReal1.add(tReal1n).scale(Float.valueOf(-1f)), tReal1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(RealF.isZero(RealF.subtract(tReal1, tReal1)));
		assertTrue(RealF.isZero(tReal1.subtract(tReal1)));
		assertFalse(RealF.isEqual(tReal1.subtract(tReal1n), tReal1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(RealF.isEqual(RealF.multiply(tReal1, tReal1n), tReal1n));
		assertTrue(RealF.isEqual(tReal1.multiply(tReal1n), tReal1n));
		assertFalse(RealF.isEqual(tReal1.multiply(tReal1n), tReal1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(RealF.isEqual(RealF.divide(tReal1, tReal1n), tReal1n));
		assertTrue(RealF.isEqual(tReal1.divide(tReal1n), tReal1n));
		assertFalse(RealF.isEqual(tReal1.divide(tReal1n), tReal1n));
	}

	@Test
	public void testMultiplyInvertPasses() throws FieldException {
		RealF testThis = RealF.newONE(Cardinal.generate("not important"));
		Assertions.assertDoesNotThrow(() -> RealF.isEqual(testThis.invert(), tReal1));
		assertFalse(RealF.isTypeMatch(testThis, tReal1));				//Type Mismatch expected
		assertTrue(testThis.invert().getReal() == tReal1.getReal());	//even with real number alignment
	}

	@Test
	public void testMultiplyInvertFails() throws FieldException {
		RealF testThis = RealF.newZERO(Cardinal.generate("not important"));
		Assertions.assertThrows(FieldException.class, () -> testThis.invert());
	}

	@Test
	public void testDivideByZero() throws FieldException {
		RealF testThis = RealF.copyZERO(tReal1);
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.divide(tReal1, testThis));
		Assertions.assertThrows(FieldBinaryException.class, () -> tReal1.divide(testThis));
	}

	@Test
	public void testAddsThatShouldNotWork() {
		RealF testThis1 = RealF.copyZERO(tReal1);
		RealF testThis2 = RealF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.add(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.add(testThis1, tReal4));		//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.add(testThis1, tReal5));		//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.add(testThis1, tReal6));		//adding infinity
		Assertions.assertDoesNotThrow(() -> RealF.add(testThis1, tReal2));	//Nothing stops addition to Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealF.add(testThis1, tReal8));	//Nothing stops addition to Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal4));				//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal5));				//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal6));				//adding infinity
		Assertions.assertDoesNotThrow(() -> testThis1.add(tReal2));	//Nothing stops addition to Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.add(tReal8));	//Nothing stops addition to Float.MIN_VALUE right now.
	}

	@Test
	public void testSubtractionsThatShouldNotWork() {
		RealF testThis1 = RealF.copyZERO(tReal1);
		RealF testThis2 = RealF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.subtract(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.subtract(testThis1, tReal4));		//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.subtract(testThis1, tReal5));		//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.subtract(testThis1, tReal6));		//subtract infinity
		Assertions.assertDoesNotThrow(() -> RealF.subtract(testThis1, tReal2));	//Nothing stops subtract of Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealF.subtract(testThis1, tReal8));	//Nothing stops subtract of Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal4));				//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal5));				//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal6));				//subtract infinity
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tReal2));	//Nothing stops subtract of Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tReal8));	//Nothing stops subtract of Float.MIN_VALUE right now.
	}

	@Test
	public void testMultipliesThatShouldNotWork() {
		RealF testThis1 = RealF.copyZERO(tReal1);
		RealF testThis2 = RealF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.multiply(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.multiply(testThis1, tReal4));		//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.multiply(testThis1, tReal5));		//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.multiply(testThis1, tReal6));		//multiply infinity
		Assertions.assertDoesNotThrow(() -> RealF.multiply(testThis1, tReal2));	//Nothing stops multiply with Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealF.multiply(testThis1, tReal8));	//Nothing stops multiply with Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal4));				//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal5));				//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal6));				//multiply infinity
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tReal2));	//Nothing stops multiply with Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tReal8));	//Nothing stops multiply with Float.MIN_VALUE right now.
	}

	@Test
	public void testDividesThatShouldNotWork() { //Divide BY zero already tested elsewhere
		RealF testThis1 = RealF.copyZERO(tReal1);
		RealF testThis2 = RealF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.divide(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.divide(testThis1, tReal4));		//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.divide(testThis1, tReal5));		//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.divide(testThis1, tReal6));		//divide by infinity
		Assertions.assertDoesNotThrow(() -> RealF.divide(testThis1, tReal2));	//Nothing stops divide by Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealF.divide(testThis1, tReal8));	//Nothing stops divide by Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal4));				//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal5));				//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal6));				//divide by infinity
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tReal2));	//Nothing stops divide by Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tReal8));	//Nothing stops divide by Float.MIN_VALUE right now.
	}

	@Test
	public void testCopy() {
		RealF testThis = RealF.copyOf(tReal1);
		assertTrue(testThis.getCardinal() == tReal1.getCardinal());
		assertTrue(RealF.isEqual(testThis, tReal1));
		assertFalse(testThis == tReal1);
	}

	@Test
	public void testCreateDirect() {
		RealF testThis = RealF.create(2.7172F);
		assertTrue(testThis.getCardinalString() == "REALF");

		testThis = new RealF();
		assertTrue(RealF.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "REALF");

		testThis = new RealF(Cardinal.generate("Howz About This"));
		assertTrue(RealF.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "Howz About This");

		UnitAbstract partNumber = new UnitAbstract(Cardinal.generate("partWay"));
		testThis = new RealF(partNumber, 0.0F);
		assertTrue(RealF.isZero(testThis));
		assertTrue(testThis.getCardinal() == partNumber.getCardinal());
	}

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		tReals = new RealF[7];
		tReals[0] = tReal0;
		tReals[1] = tReal1;
		tReals[2] = tReal2;
		tReals[3] = tReal4;
		tReals[4] = tReal5;
		tReals[5] = tReal6;
		tReals[6] = tReal8;
	
		Assertions.assertThrows(FieldBinaryException.class, () -> RealF.copySumSQModulus(tReals));
	
		RealF[] whatsThis = (RealF[]) CladosFListBuilder.REALF.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> RealF.copySumSQModulus(whatsThis));
		assertTrue(RealF.isZero(RealF.copySumSQModulus(whatsThis)));

		RealF[] whatsThis2 = (RealF[]) CladosFListBuilder.REALF.createONE(16);	//Default Cardinal
		RealF testThis = RealF.copySumSQModulus(whatsThis2);
		assertFalse(RealF.isZero(testThis));
		assertTrue(testThis.getReal() == 16.0F);
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		tReals = new RealF[7];
		tReals[0] = tReal0;
		tReals[1] = tReal1;
		tReals[2] = tReal2;
		tReals[3] = tReal4;
		tReals[4] = tReal5;
		tReals[5] = tReal6;
		tReals[6] = tReal8;

		Assertions.assertThrows(FieldBinaryException.class, () ->RealF.copySumModulus(tReals));

		RealF[] whatsThis = (RealF[]) CladosFListBuilder.REALF.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> RealF.copySumModulus(whatsThis));
		assertTrue(RealF.isZero(RealF.copySumModulus(whatsThis)));

		RealF[] whatsThis2 = (RealF[]) CladosFListBuilder.REALF.createONE(16);	//Default Cardinal
		RealF testThis = RealF.copySumModulus(whatsThis2);
		assertFalse(RealF.isZero(testThis));
		assertTrue(testThis.getReal() == 4.0F);
	}

	@Test
	public void testStrings() {
		//System.out.println("tReal4 is "+tReal4.toString());
		//System.out.println("tReal1 is "+tReal1.toXMLString());
		assertNotNull(tReal4.toString());
		assertNotNull(tReal1.toXMLString());
	}
}
