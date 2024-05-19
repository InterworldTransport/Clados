package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoreRealDTest {
	public RealD tReal0 = RealD.newZERO("Test:RealD");
	public RealD tReal1 = RealD.newONE("Test:RealD");
	public RealD tReal1n = new RealD(tReal1.getCardinal(), -1f);

	public RealD tReal2 = new RealD(tReal1.getCardinal(), Double.MAX_VALUE);
	public RealD tReal8 = new RealD(tReal1.getCardinal(), Double.MIN_VALUE);
	public RealD tReal4 = new RealD(tReal1.getCardinal(), Double.NaN);
	public RealD tReal5 = new RealD(tReal1.getCardinal(), Double.POSITIVE_INFINITY);
	public RealD tReal6 = new RealD(tReal1.getCardinal(), Double.NEGATIVE_INFINITY);

	public RealD[] tReals;



	@Test
	public void testCardinals() { 										//Really testing UnitAbstract class
		assertTrue(tReal0.getCardinal().equals(tReal1.getCardinal()));	//Same String
		assertFalse(tReal0.getCardinal() == tReal1.getCardinal());		//Different Objects
		assertTrue(tReal1.getCardinal().equals(tReal1n.getCardinal()));	//Same Strings again.
		assertTrue(tReal0.getCardinalString().equals(tReal1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() { 									//Really testing UnitAbstract class
		assertTrue(RealD.isTypeMatch(tReal0, tReal1));					//Tests the strings inside Cardinals
		assertFalse(tReal0.getCardinal() == tReal1.getCardinal());		//Proof the cardinals don't have to be re-used.
	}

	@Test
	public void testIsEqual() {
		assertFalse(RealD.isEqual(tReal0, tReal1));
	}

	@Test
	public void testIsZero() {
		assertTrue(RealD.isZero(tReal0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(RealD.isInfinite(tReal5));
		assertTrue(RealD.isInfinite(tReal6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(RealD.isNaN(tReal4));
	}

	@Test
	public void testGetReal() {
		double t1 = tReal1.getReal();
		double t1n = tReal1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tReal0.modulus() == 0f);
		assertTrue(tReal1.modulus() > 0f);
		assertTrue(Double.isNaN(tReal4.modulus()));
		assertTrue(Double.isInfinite(tReal5.modulus()));
		assertTrue(Double.isInfinite(tReal6.modulus()));
		assertTrue(Double.isInfinite(tReal2.modulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tReal0.modulus() == 0f);
		assertTrue(tReal1.modulus() > 0f);
		assertTrue(Double.isNaN(tReal4.modulus()));
		assertTrue(Double.isInfinite(tReal5.modulus()));
		assertTrue(Double.isInfinite(tReal6.modulus()));
		assertTrue(Double.isInfinite(tReal2.modulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(RealD.isEqual(tReal0.conjugate(), RealD.copyZERO(tReal0)));
		assertFalse(RealD.isEqual(tReal1.conjugate(), tReal1n));
		RealD tR = RealD.conjugate(tReal0);
		assertTrue(RealD.isEqual(tR, tReal0));
	}

	@Test
	public void testScale() {
		assertTrue(RealD.isEqual(tReal1n.scale(Double.valueOf(-1d)), tReal1));
		tReal1n.scale(Double.valueOf(-1d));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(RealD.isZero(RealD.add(tReal1, tReal1n)));
		assertTrue(RealD.isZero(tReal1.add(tReal1n)));
		assertFalse(RealD.isEqual(tReal1.add(tReal1n).scale(Double.valueOf(-1f)), tReal1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(RealD.isZero(RealD.subtract(tReal1, tReal1)));
		assertTrue(RealD.isZero(tReal1.subtract(tReal1)));
		assertFalse(RealD.isEqual(tReal1.subtract(tReal1n), tReal1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(RealD.isEqual(RealD.multiply(tReal1, tReal1n), tReal1n));
		assertTrue(RealD.isEqual(tReal1.multiply(tReal1n), tReal1n));
		assertFalse(RealD.isEqual(tReal1.multiply(tReal1n), tReal1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(RealD.isEqual(RealD.divide(tReal1, tReal1n), tReal1n));
		assertTrue(RealD.isEqual(tReal1.divide(tReal1n), tReal1n));
		assertFalse(RealD.isEqual(tReal1.divide(tReal1n), tReal1n));
	}

	@Test
	public void testMultiplyInvertPasses() throws FieldException {
		RealD testThis = RealD.newONE(Cardinal.generate("not important"));
		Assertions.assertDoesNotThrow(() -> RealD.isEqual(testThis.invert(), tReal1));
		assertFalse(RealD.isTypeMatch(testThis, tReal1));				//Type Mismatch expected
		assertTrue(testThis.invert().getReal() == tReal1.getReal());	//even with real number alignment
	}

	@Test
	public void testMultiplyInvertFails() throws FieldException {
		RealD testThis = RealD.newZERO(Cardinal.generate("not important"));
		Assertions.assertThrows(FieldException.class, () -> testThis.invert());
	}

	@Test
	public void testDivideByZero() throws FieldException {
		RealD testThis = RealD.copyZERO(tReal1);
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.divide(tReal1, testThis));
		Assertions.assertThrows(FieldBinaryException.class, () -> tReal1.divide(testThis));
	}

	@Test
	public void testAddsThatShouldNotWork() {
		RealD testThis1 = RealD.copyZERO(tReal1);
		RealD testThis2 = RealD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.add(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.add(testThis1, tReal4));		//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.add(testThis1, tReal5));		//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.add(testThis1, tReal6));		//adding infinity
		Assertions.assertDoesNotThrow(() -> RealD.add(testThis1, tReal2));	//Nothing stops addition to Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealD.add(testThis1, tReal8));	//Nothing stops addition to Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal4));				//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal5));				//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tReal6));				//adding infinity
		Assertions.assertDoesNotThrow(() -> testThis1.add(tReal2));	//Nothing stops addition to Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.add(tReal8));	//Nothing stops addition to Double.MIN_VALUE right now.
	}

	@Test
	public void testSubtractionsThatShouldNotWork() {
		RealD testThis1 = RealD.copyZERO(tReal1);
		RealD testThis2 = RealD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.subtract(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.subtract(testThis1, tReal4));		//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.subtract(testThis1, tReal5));		//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.subtract(testThis1, tReal6));		//subtract infinity
		Assertions.assertDoesNotThrow(() -> RealD.subtract(testThis1, tReal2));	//Nothing stops subtract of Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealD.subtract(testThis1, tReal8));	//Nothing stops subtract of Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal4));				//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal5));				//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tReal6));				//subtract infinity
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tReal2));	//Nothing stops subtract of Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tReal8));	//Nothing stops subtract of Double.MIN_VALUE right now.
	}

	@Test
	public void testMultipliesThatShouldNotWork() {
		RealD testThis1 = RealD.copyZERO(tReal1);
		RealD testThis2 = RealD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.multiply(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.multiply(testThis1, tReal4));		//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.multiply(testThis1, tReal5));		//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.multiply(testThis1, tReal6));		//multiply infinity
		Assertions.assertDoesNotThrow(() -> RealD.multiply(testThis1, tReal2));	//Nothing stops multiply with Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealD.multiply(testThis1, tReal8));	//Nothing stops multiply with Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal4));				//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal5));				//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tReal6));				//multiply infinity
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tReal2));	//Nothing stops multiply with Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tReal8));	//Nothing stops multiply with Double.MIN_VALUE right now.
	}

	@Test
	public void testDividesThatShouldNotWork() { //Divide BY zero already tested elsewhere
		RealD testThis1 = RealD.copyZERO(tReal1);
		RealD testThis2 = RealD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.divide(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(testThis2));			//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.divide(testThis1, tReal4));		//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.divide(testThis1, tReal5));		//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.divide(testThis1, tReal6));		//divide by infinity
		Assertions.assertDoesNotThrow(() -> RealD.divide(testThis1, tReal2));	//Nothing stops divide by Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> RealD.divide(testThis1, tReal8));	//Nothing stops divide by Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal4));				//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal5));				//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tReal6));				//divide by infinity
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tReal2));	//Nothing stops divide by Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tReal8));	//Nothing stops divide by Double.MIN_VALUE right now.
	}

	@Test
	public void testCopy() {
		RealD testThis = RealD.copyOf(tReal1);
		assertTrue(testThis.getCardinal() == tReal1.getCardinal());
		assertTrue(RealD.isEqual(testThis, tReal1));
		assertFalse(testThis == tReal1);
	}

	@Test
	public void testCreateDirect() {
		RealD testThis = RealD.create(2.7172F);
		assertTrue(testThis.getCardinalString() == "REALD");

		testThis = new RealD();
		assertTrue(RealD.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "REALD");

		testThis = new RealD(Cardinal.generate("Howz About This"));
		assertTrue(RealD.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "Howz About This");

		UnitAbstract partNumber = new UnitAbstract(Cardinal.generate("partWay"));
		testThis = new RealD(partNumber, 0.0F);
		assertTrue(RealD.isZero(testThis));
		assertTrue(testThis.getCardinal() == partNumber.getCardinal());
	}

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		tReals = new RealD[7];
		tReals[0] = tReal0;
		tReals[1] = tReal1;
		tReals[2] = tReal2;
		tReals[3] = tReal4;
		tReals[4] = tReal5;
		tReals[5] = tReal6;
		tReals[6] = tReal8;
	
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.copySumSQModulus(tReals));
	
		RealD[] whatsThis = (RealD[]) FListBuilder.REALD.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> RealD.copySumSQModulus(whatsThis));
		assertTrue(RealD.isZero(RealD.copySumSQModulus(whatsThis)));

		RealD[] whatsThis2 = (RealD[]) FListBuilder.REALD.createONE(16);	//Default Cardinal
		RealD testThis = RealD.copySumSQModulus(whatsThis2);
		assertFalse(RealD.isZero(testThis));
		assertTrue(testThis.getReal() == 16.0F);
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		tReals = new RealD[7];
		tReals[0] = tReal0;
		tReals[1] = tReal1;
		tReals[2] = tReal2;
		tReals[3] = tReal4;
		tReals[4] = tReal5;
		tReals[5] = tReal6;
		tReals[6] = tReal8;

		Assertions.assertThrows(FieldBinaryException.class, () ->RealD.copySumModulus(tReals));

		RealD[] whatsThis = (RealD[]) FListBuilder.REALD.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> RealD.copySumModulus(whatsThis));
		assertTrue(RealD.isZero(RealD.copySumModulus(whatsThis)));

		RealD[] whatsThis2 = (RealD[]) FListBuilder.REALD.createONE(16);	//Default Cardinal
		RealD testThis = RealD.copySumModulus(whatsThis2);
		assertFalse(RealD.isZero(testThis));
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
