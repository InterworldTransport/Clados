package org.interworldtransport.cladosFTest;

import org.junit.*;

import static org.interworldtransport.cladosF.ComplexF.*;
import static org.junit.Assert.*;

import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;

public class ComplexFTest // extends TestCase
{

	public ComplexF		tReal0;
	public ComplexF		tReal1;
	public ComplexF		tReal1n;
	public ComplexF		tReal1i;
	public ComplexF		tReal2;
	public ComplexF		tReal3;
	public ComplexF		tReal4;
	public ComplexF		tReal5;
	public ComplexF		tReal6;
	public ComplexF		tReal7;
	public ComplexF		tReal8;
	public ComplexF		tReal9;
	public ComplexF[]	tReals;

	@Before
	public void setUp()
	{
		tReal0 = ComplexF.newZERO("Test:ComplexF");
		tReal1 = ComplexF.newONE("Test:ComplexF");
		tReal1n = new ComplexF(tReal1.getCardinal(), -1.0f);
		tReal1i = new ComplexF(tReal1.getCardinal(), 0.0f, 1.0f);
		tReal2 = new ComplexF(tReal1.getCardinal(), Float.MAX_VALUE);
		tReal3 = new ComplexF(tReal1.getCardinal(), Float.MAX_EXPONENT);
		tReal4 = new ComplexF(tReal1.getCardinal(), Float.NaN);
		tReal5 = new ComplexF(tReal1.getCardinal(), Float.POSITIVE_INFINITY);
		tReal6 = new ComplexF(tReal1.getCardinal(), Float.NEGATIVE_INFINITY);
		tReal7 = new ComplexF(tReal1.getCardinal(), Float.MIN_NORMAL);
		tReal8 = new ComplexF(tReal1.getCardinal(), Float.MIN_VALUE);
		tReal9 = new ComplexF(tReal1.getCardinal(), Float.MIN_EXPONENT);
		tReals = new ComplexF[10];
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
	public void testGetCardinal()
	{
		assertFalse(tReal0.getCardinal().equals(tReal1.getCardinal()));
		assertTrue(tReal1.getCardinal().equals(tReal1n.getCardinal()));
	}

	@Test
	public void testGetCardinalType()
	{
		assertTrue(tReal0.getCardinalString().equals(
						tReal1.getCardinalString()));
	}

	@Test
	public void testIsTypeMatch()
	{
		assertFalse(isTypeMatch(tReal0, tReal1));
	}

	@Test
	public void testIsEqual()
	{
		assertFalse(isEqual(tReal0, tReal1));
	}

	@Test
	public void testIsZero()
	{
		assertTrue(isZero(tReal0));
	}

	@Test
	public void testIsInfinite()
	{
		assertTrue(isInfinite(tReal5));
		assertTrue(isInfinite(tReal6));
	}

	@Test
	public void testIsNaN()
	{
		assertTrue(isNaN(tReal4));
	}
	
	@Test
	public void testIsImaginary()
	{
		assertTrue(isImaginary(tReal1i));
	}
	
	@Test
	public void testIsReal()
	{
		assertTrue(isReal(tReal1n));
	}

	@Test
	public void testGetReal()
	{
		float t1 = tReal1.getReal();
		float t1n = tReal1n.getReal();
		assertTrue(t1 == t1n * -1f);
	}

	@Test
	public void testGetModulus()
	{
		assertTrue(tReal0.getModulus() == 0f);
		assertTrue(tReal1.getModulus() > 0f);
		assertTrue(Float.isNaN(tReal4.getModulus()));
		assertTrue(Float.isInfinite(tReal5.getModulus()));
		assertTrue(Float.isInfinite(tReal6.getModulus()));
		assertTrue(Float.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testGetSQModulus()
	{
		assertTrue(tReal0.getModulus() == 0f);
		assertTrue(tReal1.getModulus() > 0f);
		assertTrue(Float.isNaN(tReal4.getModulus()));
		assertTrue(Float.isInfinite(tReal5.getModulus()));
		assertTrue(Float.isInfinite(tReal6.getModulus()));
		assertTrue(Float.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testConjugate()
	{
		assertTrue(isEqual(tReal0.conjugate(), ComplexF.copyZERO(tReal0)));
		assertFalse(isEqual(tReal1.conjugate(), tReal1n));
		ComplexF tR = conjugate(tReal0);
		assertTrue(isEqual(tR, tReal0));
	}

	@Test
	public void testScale()
	{
		assertTrue(isEqual(tReal1n.scale(-1f), tReal1));
		tReal1n.scale(-1f);
	}

	@Test(expected = FieldException.class)
	public void testInvert() throws FieldException
	{
		tReal1n.scale(-1f);
		assertTrue(isEqual(tReal1n.invert(), tReal1));
		tReal1n.scale(-1f);
		assertFalse(isEqual(tReal0.invert(), tReal5));
	}

	@Test
	public void testAdd() throws FieldException
	{
		assertTrue(isZero(add(tReal1, tReal1n)));
		assertTrue(isZero(tReal1.add(tReal1n)));
		assertFalse(isEqual(tReal1.add(tReal1n).scale(-1f), tReal1n));
	}

	@Test
	public void testSubtract() throws FieldException
	{
		assertTrue(isZero(subtract(tReal1, tReal1)));
		assertTrue(isZero(tReal1.subtract(tReal1)));
		assertFalse(isEqual(tReal1.subtract(tReal1n), tReal1n));
	}

	@Test
	public void testMultiply() throws FieldException
	{
		assertTrue(isEqual(multiply(tReal1, tReal1n), tReal1n));
		assertTrue(isEqual(tReal1.multiply(tReal1n), tReal1n));
		assertFalse(isEqual(tReal1.multiply(tReal1n), tReal1n));
	}

	@Test
	public void testDivide() throws FieldException
	{
		assertTrue(isEqual(divide(tReal1, tReal1n), tReal1n));
		assertTrue(isEqual(tReal1.divide(tReal1n), tReal1n));
		assertFalse(isEqual(tReal1.divide(tReal1n), tReal1n));
	}

	@Test(expected = FieldBinaryException.class)
	public void testCopyFromSQModuliSum() throws FieldBinaryException
	{
		assertTrue(ComplexF.isNaN(ComplexF.copyFromSQModuliSum(tReals)));
	}

	@Test(expected = FieldBinaryException.class)
	public void testCopyFromModuliSum() throws FieldBinaryException
	{
		assertTrue(ComplexF.isNaN(ComplexF.copyFromModuliSum(tReals)));
	}
}