package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoreComplexFTest {
	public ComplexF tComplex0 = ComplexF.newZERO("Test:ComplexF");
	public ComplexF tComplex1 = ComplexF.newONE("Test:ComplexF");
	public ComplexF tComplex1n = new ComplexF(tComplex1.getCardinal(), -1f);

	public ComplexF tComplex2 = new ComplexF(tComplex1.getCardinal(), Float.MAX_VALUE);
	public ComplexF tComplex8 = new ComplexF(tComplex1.getCardinal(), Float.MIN_VALUE);
	public ComplexF tComplex4 = new ComplexF(tComplex1.getCardinal(), Float.NaN);
	public ComplexF tComplex5 = new ComplexF(tComplex1.getCardinal(), Float.POSITIVE_INFINITY);
	public ComplexF tComplex6 = new ComplexF(tComplex1.getCardinal(), Float.NEGATIVE_INFINITY);

	public ComplexF[] tComplexs;



	@Test
	public void testCardinals() { 												//Really testing UnitAbstract class
		assertTrue(tComplex0.getCardinal().equals(tComplex1.getCardinal()));	//Same String
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());		//Different Objects
		assertTrue(tComplex1.getCardinal().equals(tComplex1n.getCardinal()));	//Same Strings again.
		assertTrue(tComplex0.getCardinalString().equals(tComplex1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() {												//Really testing UnitAbstract class
		assertTrue(ComplexF.isTypeMatch(tComplex0, tComplex1));					//Tests the strings inside Cardinals
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());		//Proof the cardinals don't have to be re-used.
	}

	@Test
	public void testIsEqual() {
		assertFalse(ComplexF.isEqual(tComplex0, tComplex1));
	}

	@Test
	public void testIsZero() {
		assertTrue(ComplexF.isZero(tComplex0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(ComplexF.isInfinite(tComplex5));
		assertTrue(ComplexF.isInfinite(tComplex6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(ComplexF.isNaN(tComplex4));
	}

	@Test
	public void testIsReal() {
		assertTrue(ComplexF.isReal(tComplex0));
		assertFalse(ComplexF.isReal(ComplexF.create(1.0F, 1.0F)));
	}

	@Test
	public void testGetComplex() {
		float t1 = tComplex1.getReal();
		float t1i = tComplex1.getImg();
		float t1n = tComplex1n.getReal();
		float t1ni = tComplex1n.getImg();
		assertTrue(t1 == t1n * -1f);
		assertTrue(t1i == t1ni);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tComplex0.modulus() == 0f);
		assertTrue(tComplex1.modulus() > 0f);
		assertTrue(Float.isNaN(tComplex4.modulus()));
		assertTrue(Float.isInfinite(tComplex5.modulus()));
		assertTrue(Float.isInfinite(tComplex6.modulus()));
		assertTrue(Float.isInfinite(tComplex2.modulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tComplex0.modulus() == 0f);
		assertTrue(tComplex1.modulus() > 0f);
		assertTrue(Float.isNaN(tComplex4.modulus()));
		assertTrue(Float.isInfinite(tComplex5.modulus()));
		assertTrue(Float.isInfinite(tComplex6.modulus()));
		assertTrue(Float.isInfinite(tComplex2.modulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(ComplexF.isEqual(tComplex0.conjugate(), ComplexF.copyZERO(tComplex0)));
		assertFalse(ComplexF.isEqual(tComplex1.conjugate(), tComplex1n));
		ComplexF tR = ComplexF.conjugate(tComplex0);
		assertTrue(ComplexF.isEqual(tR, tComplex0));
	}

	@Test
	public void testScale() {
		assertTrue(ComplexF.isEqual(tComplex1n.scale(Float.valueOf(-1f)), tComplex1));
		tComplex1n.scale(Float.valueOf(-1f));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(ComplexF.isZero(ComplexF.add(tComplex1, tComplex1n)));
		assertTrue(ComplexF.isZero(tComplex1.add(tComplex1n)));
		assertFalse(ComplexF.isEqual(tComplex1.add(tComplex1n).scale(Float.valueOf(-1f)), tComplex1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(ComplexF.isZero(ComplexF.subtract(tComplex1, tComplex1)));
		assertTrue(ComplexF.isZero(tComplex1.subtract(tComplex1)));
		assertFalse(ComplexF.isEqual(tComplex1.subtract(tComplex1n), tComplex1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(ComplexF.isEqual(ComplexF.multiply(tComplex1, tComplex1n), tComplex1n));
		assertTrue(ComplexF.isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
		assertFalse(ComplexF.isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(ComplexF.isEqual(ComplexF.divide(tComplex1, tComplex1n), tComplex1n));
		assertTrue(ComplexF.isEqual(tComplex1.divide(tComplex1n), tComplex1n));
		assertFalse(ComplexF.isEqual(tComplex1.divide(tComplex1n), tComplex1n));
	}

	@Test
	public void testMultiplyInvertPasses() throws FieldException {
		ComplexF testThis = ComplexF.newONE(Cardinal.generate("not important"));
		Assertions.assertDoesNotThrow(() -> ComplexF.isEqual(testThis.invert(), tComplex1));
		assertFalse(ComplexF.isTypeMatch(testThis, tComplex1));				//Type Mismatch expected
		assertTrue(testThis.invert().getReal() == tComplex1.getReal());		//even with real number alignment
	}

	@Test
	public void testMultiplyInvertFails() throws FieldException {
		ComplexF testThis = ComplexF.newZERO(Cardinal.generate("not important"));
		Assertions.assertThrows(FieldException.class, () -> testThis.invert());
	}

	@Test
	public void testDivideByZero() throws FieldException {
		ComplexF testThis = ComplexF.copyZERO(tComplex1);
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.divide(tComplex1, testThis));
		Assertions.assertThrows(FieldBinaryException.class, () -> tComplex1.divide(testThis));
	}

	@Test
	public void testAddsThatShouldNotWork() {
		ComplexF testThis1 = ComplexF.copyZERO(tComplex1);
		ComplexF testThis2 = ComplexF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.add(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.add(testThis1, tComplex4));		//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.add(testThis1, tComplex5));		//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.add(testThis1, tComplex6));		//adding infinity
		Assertions.assertDoesNotThrow(() -> ComplexF.add(testThis1, tComplex2));	//Nothing stops addition to Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexF.add(testThis1, tComplex8));	//Nothing stops addition to Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex4));				//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex5));				//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex6));				//adding infinity
		Assertions.assertDoesNotThrow(() -> testThis1.add(tComplex2));	//Nothing stops addition to Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.add(tComplex8));	//Nothing stops addition to Float.MIN_VALUE right now.
	}

	@Test
	public void testSubtractionsThatShouldNotWork() {
		ComplexF testThis1 = ComplexF.copyZERO(tComplex1);
		ComplexF testThis2 = ComplexF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.subtract(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.subtract(testThis1, tComplex4));		//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.subtract(testThis1, tComplex5));		//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.subtract(testThis1, tComplex6));		//subtract infinity
		Assertions.assertDoesNotThrow(() -> ComplexF.subtract(testThis1, tComplex2));	//Nothing stops subtract of Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexF.subtract(testThis1, tComplex8));	//Nothing stops subtract of Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex4));				//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex5));				//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex6));				//subtract infinity
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tComplex2));	//Nothing stops subtract of Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tComplex8));	//Nothing stops subtract of Float.MIN_VALUE right now.
	}

	@Test
	public void testMultipliesThatShouldNotWork() {
		ComplexF testThis1 = ComplexF.copyZERO(tComplex1);
		ComplexF testThis2 = ComplexF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.multiply(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.multiply(testThis1, tComplex4));		//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.multiply(testThis1, tComplex5));		//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.multiply(testThis1, tComplex6));		//multiply infinity
		Assertions.assertDoesNotThrow(() -> ComplexF.multiply(testThis1, tComplex2));	//Nothing stops multiply with Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexF.multiply(testThis1, tComplex8));	//Nothing stops multiply with Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex4));				//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex5));				//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex6));				//multiply infinity
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tComplex2));	//Nothing stops multiply with Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tComplex8));	//Nothing stops multiply with Float.MIN_VALUE right now.
	}

	@Test
	public void testDividesThatShouldNotWork() { //Divide BY zero already tested elsewhere
		ComplexF testThis1 = ComplexF.copyZERO(tComplex1);
		ComplexF testThis2 = ComplexF.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.divide(testThis1, testThis2)); 		//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.divide(testThis1, tComplex4));		//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.divide(testThis1, tComplex5));		//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.divide(testThis1, tComplex6));		//divide by infinity
		Assertions.assertDoesNotThrow(() -> ComplexF.divide(testThis1, tComplex2));	//Nothing stops divide by Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexF.divide(testThis1, tComplex8));	//Nothing stops divide by Float.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex4));				//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex5));				//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex6));				//divide by infinity
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tComplex2));	//Nothing stops divide by Float.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tComplex8));	//Nothing stops divide by Float.MIN_VALUE right now.
	}

	@Test
	public void testCopy() {
		ComplexF testThis = ComplexF.copyOf(tComplex1);
		assertTrue(testThis.getCardinal() == tComplex1.getCardinal());
		assertTrue(ComplexF.isEqual(testThis, tComplex1));
		assertFalse(testThis == tComplex1);
	}

	@Test
	public void testCreateDirect() {
		ComplexF testThis = ComplexF.create(2.7172F, 3.1415F);
		assertTrue(testThis.getCardinalString() == "COMPLEXF");

		testThis = new ComplexF();
		assertTrue(ComplexF.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "COMPLEXF");

		testThis = new ComplexF(Cardinal.generate("Howz About This"));
		assertTrue(ComplexF.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "Howz About This");

		UnitAbstract partNumber = new UnitAbstract(Cardinal.generate("partWay"));
		testThis = new ComplexF(partNumber, 0.0F, 0.0F);
		assertTrue(ComplexF.isZero(testThis));
		assertTrue(testThis.getCardinal() == partNumber.getCardinal());
	}

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		tComplexs = new ComplexF[7];
		tComplexs[0] = tComplex0;
		tComplexs[1] = tComplex1;
		tComplexs[2] = tComplex2;
		tComplexs[3] = tComplex4;
		tComplexs[4] = tComplex5;
		tComplexs[5] = tComplex6;
		tComplexs[6] = tComplex8;
	
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.copySumSQModulus(tComplexs));
	
		ComplexF[] whatsThis = (ComplexF[]) CladosFListBuilder.COMPLEXF.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> ComplexF.copySumSQModulus(whatsThis));
		assertTrue(ComplexF.isZero(ComplexF.copySumSQModulus(whatsThis)));

		ComplexF[] whatsThis2 = (ComplexF[]) CladosFListBuilder.COMPLEXF.createONE(16);	//Default Cardinal
		ComplexF testThis = ComplexF.copySumSQModulus(whatsThis2);
		assertFalse(ComplexF.isZero(testThis));
		assertTrue(testThis.getReal() == 16.0F);
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		tComplexs = new ComplexF[7];
		tComplexs[0] = tComplex0;
		tComplexs[1] = tComplex1;
		tComplexs[2] = tComplex2;
		tComplexs[3] = tComplex4;
		tComplexs[4] = tComplex5;
		tComplexs[5] = tComplex6;
		tComplexs[6] = tComplex8;

		Assertions.assertThrows(FieldBinaryException.class, () ->ComplexF.copySumModulus(tComplexs));

		ComplexF[] whatsThis = (ComplexF[]) CladosFListBuilder.COMPLEXF.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> ComplexF.copySumModulus(whatsThis));
		assertTrue(ComplexF.isZero(ComplexF.copySumModulus(whatsThis)));

		ComplexF[] whatsThis2 = (ComplexF[]) CladosFListBuilder.COMPLEXF.createONE(16);	//Default Cardinal
		ComplexF testThis = ComplexF.copySumModulus(whatsThis2);
		assertFalse(ComplexF.isZero(testThis));
		assertTrue(testThis.getReal() == 4.0F);
	}

	@Test
	public void testStrings() {
		//System.out.println("tComplex4 is "+tComplex4.toString());
		//System.out.println("tComplex1 is "+tComplex1.toXMLString());
		assertNotNull(tComplex4.toString());
		assertNotNull(tComplex1.toXMLString());
	}

}
