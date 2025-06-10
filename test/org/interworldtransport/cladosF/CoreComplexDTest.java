package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoreComplexDTest {
	public ComplexD tComplex0 = ComplexD.newZERO("Test:ComplexD");
	public ComplexD tComplex1 = ComplexD.newONE("Test:ComplexD");
	public ComplexD tComplex1n = new ComplexD(tComplex1.getCardinal(), -1f);

	public ComplexD tComplex2 = new ComplexD(tComplex1.getCardinal(), Double.MAX_VALUE);
	public ComplexD tComplex8 = new ComplexD(tComplex1.getCardinal(), Double.MIN_VALUE);
	public ComplexD tComplex4 = new ComplexD(tComplex1.getCardinal(), Double.NaN);
	public ComplexD tComplex5 = new ComplexD(tComplex1.getCardinal(), Double.POSITIVE_INFINITY);
	public ComplexD tComplex6 = new ComplexD(tComplex1.getCardinal(), Double.NEGATIVE_INFINITY);

	public ComplexD[] tComplexs;



	@Test
	public void testCardinals() { 												//Really testing UnitAbstract class
		assertTrue(tComplex0.getCardinal().equals(tComplex1.getCardinal()));	//Same String
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());		//Different Objects
		assertTrue(tComplex1.getCardinal().equals(tComplex1n.getCardinal()));	//Same Strings again.
		assertTrue(tComplex0.getCardinalString().equals(tComplex1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() {												//Really testing UnitAbstract class
		assertTrue(ComplexD.isTypeMatch(tComplex0, tComplex1));					//Tests the strings inside Cardinals
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());		//Proof the cardinals don't have to be re-used.
	}

	@Test
	public void testIsEqual() {
		assertFalse(ComplexD.isEqual(tComplex0, tComplex1));
	}

	@Test
	public void testIsZero() {
		assertTrue(ComplexD.isZero(tComplex0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(ComplexD.isInfinite(tComplex5));
		assertTrue(ComplexD.isInfinite(tComplex6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(ComplexD.isNaN(tComplex4));
	}

	@Test
	public void testIsReal() {
		assertTrue(ComplexD.isReal(tComplex0));
		assertFalse(ComplexD.isReal(ComplexD.create(1.0F, 1.0F)));
	}

	@Test
	public void testGetComplex() {
		double t1 = tComplex1.getReal();
		double t1i = tComplex1.getImg();
		double t1n = tComplex1n.getReal();
		double t1ni = tComplex1n.getImg();
		assertTrue(t1 == t1n * -1f);
		assertTrue(t1i == t1ni);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tComplex0.modulus() == 0f);
		assertTrue(tComplex1.modulus() > 0f);
		assertTrue(Double.isNaN(tComplex4.modulus()));
		assertTrue(Double.isInfinite(tComplex5.modulus()));
		assertTrue(Double.isInfinite(tComplex6.modulus()));
		assertTrue(Double.isInfinite(tComplex2.modulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tComplex0.modulus() == 0f);
		assertTrue(tComplex1.modulus() > 0f);
		assertTrue(Double.isNaN(tComplex4.modulus()));
		assertTrue(Double.isInfinite(tComplex5.modulus()));
		assertTrue(Double.isInfinite(tComplex6.modulus()));
		assertTrue(Double.isInfinite(tComplex2.modulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(ComplexD.isEqual(tComplex0.conjugate(), ComplexD.copyZERO(tComplex0)));
		assertFalse(ComplexD.isEqual(tComplex1.conjugate(), tComplex1n));
		ComplexD tR = ComplexD.conjugate(tComplex0);
		assertTrue(ComplexD.isEqual(tR, tComplex0));
	}

	@Test
	public void testScale() {
		assertTrue(ComplexD.isEqual(tComplex1n.scale(Double.valueOf(-1f)), tComplex1));
		tComplex1n.scale(Double.valueOf(-1f));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(ComplexD.isZero(ComplexD.add(tComplex1, tComplex1n)));
		assertTrue(ComplexD.isZero(tComplex1.add(tComplex1n)));
		assertFalse(ComplexD.isEqual(tComplex1.add(tComplex1n).scale(Double.valueOf(-1f)), tComplex1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(ComplexD.isZero(ComplexD.subtract(tComplex1, tComplex1)));
		assertTrue(ComplexD.isZero(tComplex1.subtract(tComplex1)));
		assertFalse(ComplexD.isEqual(tComplex1.subtract(tComplex1n), tComplex1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(ComplexD.isEqual(ComplexD.multiply(tComplex1, tComplex1n), tComplex1n));
		assertTrue(ComplexD.isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
		assertFalse(ComplexD.isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(ComplexD.isEqual(ComplexD.divide(tComplex1, tComplex1n), tComplex1n));
		assertTrue(ComplexD.isEqual(tComplex1.divide(tComplex1n), tComplex1n));
		assertFalse(ComplexD.isEqual(tComplex1.divide(tComplex1n), tComplex1n));
	}

	@Test
	public void testMultiplyInvertPasses() throws FieldException {
		ComplexD testThis = ComplexD.newONE(Cardinal.generate("not important"));
		Assertions.assertDoesNotThrow(() -> ComplexD.isEqual(testThis.invert(), tComplex1));
		assertFalse(ComplexD.isTypeMatch(testThis, tComplex1));				//Type Mismatch expected
		assertTrue(testThis.invert().getReal() == tComplex1.getReal());		//even with real number alignment
	}

	@Test
	public void testMultiplyInvertFails() throws FieldException {
		ComplexD testThis = ComplexD.newZERO(Cardinal.generate("not important"));
		Assertions.assertThrows(FieldException.class, () -> testThis.invert());
	}

	@Test
	public void testDivideByZero() throws FieldException {
		ComplexD testThis = ComplexD.copyZERO(tComplex1);
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.divide(tComplex1, testThis));
		Assertions.assertThrows(FieldBinaryException.class, () -> tComplex1.divide(testThis));
	}

	@Test
	public void testAddsThatShouldNotWork() {
		ComplexD testThis1 = ComplexD.copyZERO(tComplex1);
		ComplexD testThis2 = ComplexD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.add(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.add(testThis1, tComplex4));		//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.add(testThis1, tComplex5));		//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.add(testThis1, tComplex6));		//adding infinity
		Assertions.assertDoesNotThrow(() -> ComplexD.add(testThis1, tComplex2));	//Nothing stops addition to Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexD.add(testThis1, tComplex8));	//Nothing stops addition to Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex4));				//adding NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex5));				//adding infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.add(tComplex6));				//adding infinity
		Assertions.assertDoesNotThrow(() -> testThis1.add(tComplex2));	//Nothing stops addition to Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.add(tComplex8));	//Nothing stops addition to Double.MIN_VALUE right now.
	}

	@Test
	public void testSubtractionsThatShouldNotWork() {
		ComplexD testThis1 = ComplexD.copyZERO(tComplex1);
		ComplexD testThis2 = ComplexD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.subtract(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.subtract(testThis1, tComplex4));		//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.subtract(testThis1, tComplex5));		//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.subtract(testThis1, tComplex6));		//subtract infinity
		Assertions.assertDoesNotThrow(() -> ComplexD.subtract(testThis1, tComplex2));	//Nothing stops subtract of Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexD.subtract(testThis1, tComplex8));	//Nothing stops subtract of Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex4));				//subtract NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex5));				//subtract infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.subtract(tComplex6));				//subtract infinity
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tComplex2));	//Nothing stops subtract of Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.subtract(tComplex8));	//Nothing stops subtract of Double.MIN_VALUE right now.
	}

	@Test
	public void testMultipliesThatShouldNotWork() {
		ComplexD testThis1 = ComplexD.copyZERO(tComplex1);
		ComplexD testThis2 = ComplexD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.multiply(testThis1, testThis2)); 	//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.multiply(testThis1, tComplex4));		//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.multiply(testThis1, tComplex5));		//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.multiply(testThis1, tComplex6));		//multiply infinity
		Assertions.assertDoesNotThrow(() -> ComplexD.multiply(testThis1, tComplex2));	//Nothing stops multiply with Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexD.multiply(testThis1, tComplex8));	//Nothing stops multiply with Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex4));				//multiply NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex5));				//multiply infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.multiply(tComplex6));				//multiply infinity
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tComplex2));	//Nothing stops multiply with Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.multiply(tComplex8));	//Nothing stops multiply with Double.MIN_VALUE right now.
	}

	@Test
	public void testDividesThatShouldNotWork() { //Divide BY zero already tested elsewhere
		ComplexD testThis1 = ComplexD.copyZERO(tComplex1);
		ComplexD testThis2 = ComplexD.newONE(Cardinal.generate("PurposelyDifferent"));
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.divide(testThis1, testThis2)); 		//Cardinal mismatch
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(testThis2));				//Cardinal mismatch

		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.divide(testThis1, tComplex4));		//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.divide(testThis1, tComplex5));		//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.divide(testThis1, tComplex6));		//divide by infinity
		Assertions.assertDoesNotThrow(() -> ComplexD.divide(testThis1, tComplex2));	//Nothing stops divide by Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> ComplexD.divide(testThis1, tComplex8));	//Nothing stops divide by Double.MIN_VALUE right now.

		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex4));				//divide by NaN
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex5));				//divide by infinity
		Assertions.assertThrows(FieldBinaryException.class, () -> testThis1.divide(tComplex6));				//divide by infinity
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tComplex2));	//Nothing stops divide by Double.MAX_VALUE right now.
		Assertions.assertDoesNotThrow(() -> testThis1.divide(tComplex8));	//Nothing stops divide by Double.MIN_VALUE right now.
	}

	@Test
	public void testCopy() {
		ComplexD testThis = ComplexD.copyOf(tComplex1);
		assertTrue(testThis.getCardinal() == tComplex1.getCardinal());
		assertTrue(ComplexD.isEqual(testThis, tComplex1));
		assertFalse(testThis == tComplex1);
	}

	@Test
	public void testCreateDirect() {
		ComplexD testThis = ComplexD.create(2.7172D, 3.1415D);
		assertTrue(testThis.getCardinalString() == "COMPLEXD");

		testThis = new ComplexD();
		assertTrue(ComplexD.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "COMPLEXD");

		testThis = new ComplexD(Cardinal.generate("Howz About This"));
		assertTrue(ComplexD.isZero(testThis));
		assertTrue(testThis.getCardinalString() == "Howz About This");

		UnitAbstract partNumber = new UnitAbstract(Cardinal.generate("partWay"));
		testThis = new ComplexD(partNumber, 0.0D, 0.0D);
		assertTrue(ComplexD.isZero(testThis));
		assertTrue(testThis.getCardinal() == partNumber.getCardinal());
	}

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		tComplexs = new ComplexD[7];
		tComplexs[0] = tComplex0;
		tComplexs[1] = tComplex1;
		tComplexs[2] = tComplex2;
		tComplexs[3] = tComplex4;
		tComplexs[4] = tComplex5;
		tComplexs[5] = tComplex6;
		tComplexs[6] = tComplex8;
	
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.copySumSQModulus(tComplexs));
	
		ComplexD[] whatsThis = (ComplexD[]) FListBuilder.COMPLEXD.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> ComplexD.copySumSQModulus(whatsThis));
		assertTrue(ComplexD.isZero(ComplexD.copySumSQModulus(whatsThis)));

		ComplexD[] whatsThis2 = (ComplexD[]) FListBuilder.COMPLEXD.createONE(16);	//Default Cardinal
		ComplexD testThis = ComplexD.copySumSQModulus(whatsThis2);
		assertFalse(ComplexD.isZero(testThis));
		assertTrue(testThis.getReal() == 16.0F);
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		tComplexs = new ComplexD[7];
		tComplexs[0] = tComplex0;
		tComplexs[1] = tComplex1;
		tComplexs[2] = tComplex2;
		tComplexs[3] = tComplex4;
		tComplexs[4] = tComplex5;
		tComplexs[5] = tComplex6;
		tComplexs[6] = tComplex8;

		Assertions.assertThrows(FieldBinaryException.class, () ->ComplexD.copySumModulus(tComplexs));

		ComplexD[] whatsThis = (ComplexD[]) FListBuilder.COMPLEXD.create(16);		//Default Cardinal
		Assertions.assertDoesNotThrow(() -> ComplexD.copySumModulus(whatsThis));
		assertTrue(ComplexD.isZero(ComplexD.copySumModulus(whatsThis)));

		ComplexD[] whatsThis2 = (ComplexD[]) FListBuilder.COMPLEXD.createONE(16);	//Default Cardinal
		ComplexD testThis = ComplexD.copySumModulus(whatsThis2);
		assertFalse(ComplexD.isZero(testThis));
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
