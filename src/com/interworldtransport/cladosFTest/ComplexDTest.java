package com.interworldtransport.cladosFTest;

import org.junit.*;
import static org.junit.Assert.*;
import static com.interworldtransport.cladosF.ComplexD.*;

import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosFExceptions.FieldException;

public class ComplexDTest  //extends TestCase
{

	public ComplexD		tReal0;
	public ComplexD		tReal1;
	public ComplexD		tReal1n;
	public ComplexD		tReal1i;
	public ComplexD		tReal2;
	public ComplexD		tReal3;
	public ComplexD		tReal4;
	public ComplexD		tReal5;
	public ComplexD		tReal6;
	public ComplexD		tReal7;
	public ComplexD		tReal8;
	public ComplexD		tReal9;
	public ComplexD[]	tReals;

	@Before
	public void setUp()
	{
		tReal0 = ComplexD.newZERO("Test:ComplexD");
		tReal1 = ComplexD.newONE("Test:ComplexD");
		tReal1n = new ComplexD(tReal1.getFieldType(), -1d);
		tReal1i = new ComplexD(tReal1.getFieldType(), 0.0d, 1.0d);
		tReal2 = new ComplexD(tReal1.getFieldType(), Double.MAX_VALUE);
		tReal3 = new ComplexD(tReal1.getFieldType(), Double.MAX_EXPONENT);
		tReal4 = new ComplexD(tReal1.getFieldType(), Double.NaN);
		tReal5 = new ComplexD(tReal1.getFieldType(), Double.POSITIVE_INFINITY);
		tReal6 = new ComplexD(tReal1.getFieldType(), Double.NEGATIVE_INFINITY);
		tReal7 = new ComplexD(tReal1.getFieldType(), Double.MIN_NORMAL);
		tReal8 = new ComplexD(tReal1.getFieldType(), Double.MIN_VALUE);
		tReal9 = new ComplexD(tReal1.getFieldType(), Double.MIN_EXPONENT);
		tReals = new ComplexD[10];
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
	public void testGetDivFieldType()
	{
		assertFalse(tReal0.getFieldType().equals(tReal1.getFieldType()));
		assertTrue(tReal1.getFieldType().equals(tReal1n.getFieldType()));
	}

	@Test
	public void testGetDivFieldTypeType()
	{
		assertTrue(tReal0.getFieldTypeString().equals(
						tReal1.getFieldTypeString()));
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
		double t1 = tReal1.getReal();
		double t1n = tReal1n.getReal();
		assertTrue(t1 == t1n * -1d);
	}

	@Test
	public void testGetModulus()
	{
		assertTrue(tReal0.getModulus() == 0d);
		assertTrue(tReal1.getModulus() > 0d);
		assertTrue(Double.isNaN(tReal4.getModulus()));
		assertTrue(Double.isInfinite(tReal5.getModulus()));
		assertTrue(Double.isInfinite(tReal6.getModulus()));
		assertTrue(Double.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testGetSQModulus()
	{
		assertTrue(tReal0.getModulus() == 0d);
		assertTrue(tReal1.getModulus() > 0d);
		assertTrue(Double.isNaN(tReal4.getModulus()));
		assertTrue(Double.isInfinite(tReal5.getModulus()));
		assertTrue(Double.isInfinite(tReal6.getModulus()));
		assertTrue(Double.isInfinite(tReal2.getModulus()));
	}

	@Test
	public void testConjugate()
	{
		assertTrue(isEqual(tReal0.conjugate(), ComplexD.copyZERO(tReal0)));
		assertFalse(isEqual(tReal1.conjugate(), tReal1n));
		ComplexD tR = conjugate(tReal0);
		assertTrue(isEqual(tR, tReal0));
	}

	@Test
	public void testScale()
	{
		assertTrue(isEqual(tReal1n.scale(-1d), tReal1));
		tReal1n.scale(-1d);
	}

	@Test(expected = FieldException.class)
	public void testInvert() throws FieldException
	{
		tReal1n.scale(-1d);
		assertTrue(isEqual(tReal1n.invert(), tReal1));
		tReal1n.scale(-1d);
		assertFalse(isEqual(tReal0.invert(), tReal5));
	}

	@Test
	public void testAdd() throws FieldException
	{
		assertTrue(isZero(add(tReal1, tReal1n)));
		assertTrue(isZero(tReal1.add(tReal1n)));
		assertFalse(isEqual(tReal1.add(tReal1n).scale(-1d), tReal1n));
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
		assertTrue(ComplexD.isNaN(ComplexD.copyFromSQModuliSum(tReals)));
	}

	@Test(expected = FieldBinaryException.class)
	public void testCopyFromModuliSum() throws FieldBinaryException
	{
		assertTrue(ComplexD.isNaN(ComplexD.copyFromModuliSum(tReals)));
	}
}
