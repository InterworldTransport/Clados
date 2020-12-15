package org.interworldtransport.cladosFTest;

import static org.interworldtransport.cladosF.DivField.isTypeMatch;
import static org.interworldtransport.cladosF.RealD.add;
import static org.interworldtransport.cladosF.RealD.conjugate;
import static org.interworldtransport.cladosF.RealD.divide;
import static org.interworldtransport.cladosF.RealD.isEqual;
import static org.interworldtransport.cladosF.RealD.isInfinite;
import static org.interworldtransport.cladosF.RealD.isNaN;
import static org.interworldtransport.cladosF.RealD.isZero;
import static org.interworldtransport.cladosF.RealD.multiply;
import static org.interworldtransport.cladosF.RealD.subtract;
import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreRealDTest {
	public RealD tReal0;
	public RealD tReal1;
	public RealD tReal1n;
	public RealD tReal2;
	public RealD tReal3;
	public RealD tReal4;
	public RealD tReal5;
	public RealD tReal6;
	public RealD tReal7;
	public RealD tReal8;
	public RealD tReal9;
	public RealD[] tReals;

	@BeforeEach
	public void setUp() throws Exception {
		tReal0 = RealD.newZERO("Test:RealD");
		tReal1 = RealD.newONE("Test:RealD");
		tReal1n = new RealD(tReal1.getCardinal(), -1f);
		tReal2 = new RealD(tReal1.getCardinal(), Double.MAX_VALUE);
		tReal3 = new RealD(tReal1.getCardinal(), Double.MAX_EXPONENT);
		tReal4 = new RealD(tReal1.getCardinal(), Double.NaN);
		tReal5 = new RealD(tReal1.getCardinal(), Double.POSITIVE_INFINITY);
		tReal6 = new RealD(tReal1.getCardinal(), Double.NEGATIVE_INFINITY);
		tReal7 = new RealD(tReal1.getCardinal(), Double.MIN_NORMAL);
		tReal8 = new RealD(tReal1.getCardinal(), Double.MIN_VALUE);
		tReal9 = new RealD(tReal1.getCardinal(), Double.MIN_EXPONENT);
		tReals = new RealD[10];
		tReals[0] = tReal0;
		tReals[1] = tReal1;
		tReals[2] = tReal2;
		tReals[3] = tReal3;
		tReals[4] = tReal4;
		tReals[5] = tReal5;
		tReals[6] = tReal6;
		tReals[7] = tReal7;
		tReals[8] = tReal8;
		tReals[9] = tReal9;
	}

	@Test
	public void testGetCardinal() {
		assertTrue(tReal0.getCardinal().equals(tReal1.getCardinal()));
		assertFalse(tReal0.getCardinal() == tReal1.getCardinal());
		assertTrue(tReal1.getCardinal().equals(tReal1n.getCardinal()));
	}

	@Test
	public void testGetCardinalType() {
		assertTrue(tReal0.getCardinalString().equals(tReal1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch() {
		assertFalse(isTypeMatch(tReal0, tReal1));
	}

	@Test
	public void testIsEqual() {
		assertFalse(isEqual(tReal0, tReal1));
	}

	@Test
	public void testIsZero() {
		assertTrue(isZero(tReal0));
	}

	@Test
	public void testIsInfinite() {
		assertTrue(isInfinite(tReal5));
		assertTrue(isInfinite(tReal6));
	}

	@Test
	public void testIsNaN() {
		assertTrue(isNaN(tReal4));
	}

	@Test
	public void testGetReal() {
		double t1 = tReal1.getReal();
		double t1n = tReal1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus() {
		assertTrue(tReal0.getModulus() == 0f);
		assertTrue(tReal1.getModulus() > 0f);
		assertTrue(Double.isNaN(tReal4.getModulus()));
		assertTrue(Double.isInfinite(tReal5.getModulus()));
		assertTrue(Double.isInfinite(tReal6.getModulus()));
		assertTrue(Double.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testGetSQModulus() {
		assertTrue(tReal0.getModulus() == 0f);
		assertTrue(tReal1.getModulus() > 0f);
		assertTrue(Double.isNaN(tReal4.getModulus()));
		assertTrue(Double.isInfinite(tReal5.getModulus()));
		assertTrue(Double.isInfinite(tReal6.getModulus()));
		assertTrue(Double.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testConjugate() {
		assertTrue(isEqual(tReal0.conjugate(), RealD.copyZERO(tReal0)));
		assertFalse(isEqual(tReal1.conjugate(), tReal1n));
		RealD tR = conjugate(tReal0);
		assertTrue(isEqual(tR, tReal0));
	}

	@Test
	public void testScale() {
		assertTrue(isEqual(tReal1n.scale(Double.valueOf(-1d)), tReal1));
		tReal1n.scale(Double.valueOf(-1d));
	}

	@Test
	public void testMultiplyInvert() throws FieldException {
		tReal1n.scale(Double.valueOf(0.0d));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tReal1n.invert(), tReal1));
		Assertions.assertThrows(FieldException.class, () -> isEqual(tReal0.invert(), tReal5));
	}

	@Test
	public void testAdd() throws FieldException {
		assertTrue(isZero(add(tReal1, tReal1n)));
		assertTrue(isZero(tReal1.add(tReal1n)));
		assertFalse(isEqual(tReal1.add(tReal1n).scale(Double.valueOf(-1d)), tReal1n));
	}

	@Test
	public void testSubtract() throws FieldException {
		assertTrue(isZero(subtract(tReal1, tReal1)));
		assertTrue(isZero(tReal1.subtract(tReal1)));
		assertFalse(isEqual(tReal1.subtract(tReal1n), tReal1n));
	}

	@Test
	public void testMultiply() throws FieldException {
		assertTrue(isEqual(multiply(tReal1, tReal1n), tReal1n));
		assertTrue(isEqual(tReal1.multiply(tReal1n), tReal1n));
		assertFalse(isEqual(tReal1.multiply(tReal1n), tReal1n));
	}

	@Test
	public void testDivide() throws FieldException {
		assertTrue(isEqual(divide(tReal1, tReal1n), tReal1n));
		assertTrue(isEqual(tReal1.divide(tReal1n), tReal1n));
		assertFalse(isEqual(tReal1.divide(tReal1n), tReal1n));
	}

	@Test
	public void testCopyFromSQModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.isNaN(RealD.copyFromSQModuliSum(tReals)));
	}

	@Test
	public void testCopyFromModuliSum() throws FieldBinaryException {
		Assertions.assertThrows(FieldBinaryException.class, () -> RealD.isNaN(RealD.copyFromModuliSum(tReals)));
	}

}
