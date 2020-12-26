package org.interworldtransport.cladosFTest;

import static org.interworldtransport.cladosF.DivField.isTypeMatch;
import static org.interworldtransport.cladosF.ComplexD.add;
import static org.interworldtransport.cladosF.ComplexD.conjugate;
import static org.interworldtransport.cladosF.ComplexD.divide;
import static org.interworldtransport.cladosF.ComplexD.isEqual;
import static org.interworldtransport.cladosF.ComplexD.isInfinite;
import static org.interworldtransport.cladosF.ComplexD.isNaN;
import static org.interworldtransport.cladosF.ComplexD.isZero;
import static org.interworldtransport.cladosF.ComplexD.multiply;
import static org.interworldtransport.cladosF.ComplexD.subtract;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreComplexDTest {
	public ComplexD tComplex0;
	public ComplexD tComplex1;
	public ComplexD tComplex1n;
	public ComplexD tComplex2;
	public ComplexD tComplex3;
	public ComplexD tComplex4;
	public ComplexD tComplex5;
	public ComplexD tComplex6;
	public ComplexD tComplex7;
	public ComplexD tComplex8;
	public ComplexD tComplex9;
	public ComplexD[] tComplexs;

	@BeforeEach
	public void setUp() throws Exception {
		tComplex0 = ComplexD.newZERO("Test:ComplexD");
		tComplex1 = ComplexD.newONE("Test:ComplexD");
		tComplex1n = new ComplexD(tComplex1.getCardinal(), -1f);
		tComplex2 = new ComplexD(tComplex1.getCardinal(), Double.MAX_VALUE);
		tComplex3 = new ComplexD(tComplex1.getCardinal(), Double.MAX_EXPONENT);
		tComplex4 = new ComplexD(tComplex1.getCardinal(), Double.NaN);
		tComplex5 = new ComplexD(tComplex1.getCardinal(), Double.POSITIVE_INFINITY);
		tComplex6 = new ComplexD(tComplex1.getCardinal(), Double.NEGATIVE_INFINITY);
		tComplex7 = new ComplexD(tComplex1.getCardinal(), Double.MIN_NORMAL);
		tComplex8 = new ComplexD(tComplex1.getCardinal(), Double.MIN_VALUE);
		tComplex9 = new ComplexD(tComplex1.getCardinal(), Double.MIN_EXPONENT);
		tComplexs = new ComplexD[10];
		tComplexs[0] = tComplex0;
		tComplexs[1] = tComplex1;
		tComplexs[2] = tComplex2;
		tComplexs[3] = tComplex3;
		tComplexs[4] = tComplex4;
		tComplexs[5] = tComplex5;
		tComplexs[6] = tComplex6;
		tComplexs[7] = tComplex7;
		tComplexs[8] = tComplex8;
		tComplexs[9] = tComplex9;
	}

	@Test
	public void testGetCardinal() {
		assertTrue(tComplex0.getCardinal().equals(tComplex1.getCardinal()));
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());
		assertTrue(tComplex1.getCardinal().equals(tComplex1n.getCardinal()));
	}

	@Test
	public void testGetCardinalType() {
		assertTrue(tComplex0.getCardinalString().equals(tComplex1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() {
		assertTrue(isTypeMatch(tComplex0, tComplex1));
		assertFalse(tComplex0.getCardinal() == tComplex1.getCardinal());
	}

	@Test
	public void testIsEqual() {
		assertFalse(isEqual(tComplex0, tComplex1));
	}

	@Test
	public void testIsZero() {
		assertTrue(isZero(tComplex0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(isInfinite(tComplex5));
		assertTrue(isInfinite(tComplex6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(isNaN(tComplex4));
	}

	@Test
	public void testGetReal() {
		double t1 = tComplex1.getReal();
		double t1n = tComplex1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tComplex0.getModulus() == 0f);
		assertTrue(tComplex1.getModulus() > 0f);
		assertTrue(Double.isNaN(tComplex4.getModulus()));
		assertTrue(Double.isInfinite(tComplex5.getModulus()));
		assertTrue(Double.isInfinite(tComplex6.getModulus()));
		assertTrue(Double.isInfinite(tComplex2.getModulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tComplex0.getModulus() == 0f);
		assertTrue(tComplex1.getModulus() > 0f);
		assertTrue(Double.isNaN(tComplex4.getModulus()));
		assertTrue(Double.isInfinite(tComplex5.getModulus()));
		assertTrue(Double.isInfinite(tComplex6.getModulus()));
		assertTrue(Double.isInfinite(tComplex2.getModulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(isEqual(tComplex0.conjugate(), ComplexD.copyZERO(tComplex0)));
		assertFalse(isEqual(tComplex1.conjugate(), tComplex1n));
		ComplexD tR = conjugate(tComplex0);
		assertTrue(isEqual(tR, tComplex0));
	}

	@Test
	public void testScale() {
		assertTrue(isEqual(tComplex1n.scale(Double.valueOf(-1d)), tComplex1));
		tComplex1n.scale(Double.valueOf(-1d));
	}

	@Test
	public void testMultiplyInvert() throws FieldException {
		tComplex1n.scale(Double.valueOf(0.0d));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tComplex1n.invert(), tComplex1));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tComplex0.invert(), tComplex5));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(isZero(add(tComplex1, tComplex1n)));
		assertTrue(isZero(tComplex1.add(tComplex1n)));
		assertFalse(isEqual(tComplex1.add(tComplex1n).scale(Double.valueOf(-1d)), tComplex1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(isZero(subtract(tComplex1, tComplex1)));
		assertTrue(isZero(tComplex1.subtract(tComplex1)));
		assertFalse(isEqual(tComplex1.subtract(tComplex1n), tComplex1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(isEqual(multiply(tComplex1, tComplex1n), tComplex1n));
		assertTrue(isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
		assertFalse(isEqual(tComplex1.multiply(tComplex1n), tComplex1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(isEqual(divide(tComplex1, tComplex1n), tComplex1n));
		assertTrue(isEqual(tComplex1.divide(tComplex1n), tComplex1n));
		assertFalse(isEqual(tComplex1.divide(tComplex1n), tComplex1n));
	}
/*
	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.isNaN(ComplexD.copyFromSQModuliSum(tComplexs)));
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexD.isNaN(ComplexD.copyFromModuliSum(tComplexs)));
	}
*/
}
