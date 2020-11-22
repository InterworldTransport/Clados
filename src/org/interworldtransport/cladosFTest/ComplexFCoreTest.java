package org.interworldtransport.cladosFTest;

import static org.interworldtransport.cladosF.DivField.isTypeMatch;
import static org.interworldtransport.cladosF.ComplexF.add;
import static org.interworldtransport.cladosF.ComplexF.conjugate;
import static org.interworldtransport.cladosF.ComplexF.divide;
import static org.interworldtransport.cladosF.ComplexF.isEqual;
import static org.interworldtransport.cladosF.ComplexF.isInfinite;
import static org.interworldtransport.cladosF.ComplexF.isNaN;
import static org.interworldtransport.cladosF.ComplexF.isZero;
import static org.interworldtransport.cladosF.ComplexF.multiply;
import static org.interworldtransport.cladosF.ComplexF.subtract;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComplexFCoreTest {
	public ComplexF tComplex0;
	public ComplexF tComplex1;
	public ComplexF tComplex1n;
	public ComplexF tComplex2;
	public ComplexF tComplex3;
	public ComplexF tComplex4;
	public ComplexF tComplex5;
	public ComplexF tComplex6;
	public ComplexF tComplex7;
	public ComplexF tComplex8;
	public ComplexF tComplex9;
	public ComplexF[] tComplexs;

	@BeforeEach
	void setUp() throws Exception {
		tComplex0 = ComplexF.newZERO("Test:ComplexF");
		tComplex1 = ComplexF.newONE("Test:ComplexF");
		tComplex1n = new ComplexF(tComplex1.getCardinal(), -1f);
		tComplex2 = new ComplexF(tComplex1.getCardinal(), Float.MAX_VALUE);
		tComplex3 = new ComplexF(tComplex1.getCardinal(), Float.MAX_EXPONENT);
		tComplex4 = new ComplexF(tComplex1.getCardinal(), Float.NaN);
		tComplex5 = new ComplexF(tComplex1.getCardinal(), Float.POSITIVE_INFINITY);
		tComplex6 = new ComplexF(tComplex1.getCardinal(), Float.NEGATIVE_INFINITY);
		tComplex7 = new ComplexF(tComplex1.getCardinal(), Float.MIN_NORMAL);
		tComplex8 = new ComplexF(tComplex1.getCardinal(), Float.MIN_VALUE);
		tComplex9 = new ComplexF(tComplex1.getCardinal(), Float.MIN_EXPONENT);
		tComplexs = new ComplexF[10];
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
		assertFalse(isTypeMatch(tComplex0, tComplex1));
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
	public void testGetComplex() {
		float t1 = tComplex1.getReal();
		float t1n = tComplex1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tComplex0.getModulus() == 0f);
		assertTrue(tComplex1.getModulus() > 0f);
		assertTrue(Float.isNaN(tComplex4.getModulus()));
		assertTrue(Float.isInfinite(tComplex5.getModulus()));
		assertTrue(Float.isInfinite(tComplex6.getModulus()));
		assertTrue(Float.isInfinite(tComplex2.getModulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tComplex0.getModulus() == 0f);
		assertTrue(tComplex1.getModulus() > 0f);
		assertTrue(Float.isNaN(tComplex4.getModulus()));
		assertTrue(Float.isInfinite(tComplex5.getModulus()));
		assertTrue(Float.isInfinite(tComplex6.getModulus()));
		assertTrue(Float.isInfinite(tComplex2.getModulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(isEqual(tComplex0.conjugate(), ComplexF.copyZERO(tComplex0)));
		assertFalse(isEqual(tComplex1.conjugate(), tComplex1n));
		ComplexF tR = conjugate(tComplex0);
		assertTrue(isEqual(tR, tComplex0));
	}

	@Test
	public void testScale() {
		assertTrue(isEqual(tComplex1n.scale(Float.valueOf(-1f)), tComplex1));
		tComplex1n.scale(Float.valueOf(-1f));
	}

	@Test
	public void testAdditiveInvert() throws FieldException {
		tComplex1n.scale(Float.valueOf(-1f));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tComplex1n.invert(), tComplex1));
		tComplex1n.scale(Float.valueOf(-1f));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tComplex0.invert(), tComplex5));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(isZero(add(tComplex1, tComplex1n)));
		assertTrue(isZero(tComplex1.add(tComplex1n)));
		assertFalse(isEqual(tComplex1.add(tComplex1n).scale(Float.valueOf(-1f)), tComplex1n));
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

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.isNaN(ComplexF.copyFromSQModuliSum(tComplexs)));
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> ComplexF.isNaN(ComplexF.copyFromModuliSum(tComplexs)));
	}

}
