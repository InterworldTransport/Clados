/*
f * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosF.RealD<br>
 * ------------
 * -------------------------------------------------------------------- <p>
 * Interworld Transport grants you ("Licensee") a license to this software under
 * the terms of the GNU General Public License.<br> A full copy of the license
 * can be found bundled with this package or code file. <p> If the license file
 * has become separated from the package, code file, or binary executable, the
 * Licensee is still expected to read about the license at the following URL
 * before accepting this material.
 * <blockquote><code>http://www.opensource.org/gpl
 * -license.html</code></blockquote> <p> Use of this code or executable objects
 * derived from it by the Licensee states their willingness to accept the terms
 * of the license. <p> A prospective Licensee unable to find a copy of the
 * license terms should contact Interworld Transport for a free copy. <p>
 * --------
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.RealD<br>
 * ----------------------------------
 * ----------------------------------------------
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Real Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and RealD is one
 * possibility.
 * <p>
 * There is no doubt that the overhead related to this class is a waste of
 * resources. However, it allows one to plug fields into the algebra classes
 * without having to maintain many different types of monads and nyads. If Java
 * came with primitive types for complex and quaternion fields, and other
 * primitives implemented a 'Field' interface, I wouldn't bother writing this
 * object or any of the other decendents of DivFieldD.
 * <p>
 * Applications requiring speed should use the monads and nyads that impliment
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Double and implement an interface called
 * DivFieldD. That can't be done, though, because Double is final.
 * <p>
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class RealD extends DivFieldD
{
	/**
	 * The number(s)!
	 */
	//private double[]	vals	= new double[1];

	/**
	 * Basic Constructor with no values to initialize.
	 */
	public RealD()
	{
		vals	= new double[1];
		setFieldType(new DivFieldType("Real"));
		setReal(0.0D);
	}

	/**
	 * Basic Constructor with only the field type to initialize.
	 */
	public RealD(DivFieldType pT)
	{
		vals	= new double[1];
		setFieldType(pT);
		setReal(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize.
	 */
	public RealD(DivFieldType pT, double pR)
	{
		vals	= new double[1];
		setFieldType(pT);
		setReal(pR);
	}

	/**
	 * Basic Constructor with only the number to initialize.
	 */
	public RealD(double pR)
	{
		vals	= new double[1];
		setFieldType(new DivFieldType("Real"));
		setReal(pR);
	}

	/**
	 * Copy Constructor that reuses the field type reference.
	 */
	public RealD(RealD pR)
	{
		vals	= new double[1];
		setFieldType(pR.getFieldType());
		setReal(pR.getReal());
	}
	
	/**
	 * Copy Constructor that reuses the field type reference while allowing the
	 * value to be set.
	 */
	public RealD(RealD pR, double pD)
	{
		vals	= new double[1];
		setFieldType(pR.getFieldType());
		setReal(pD);
	}

	/**
	 * Set the real numeric value
	 * 
	 * @param preal
	 *            double
	 */
	public void setReal(double preal)
	{
		vals[0] = preal;
	}

	/**
	 * Get the real numeric value from the value array
	 * 
	 * @return double
	 */
	public double getReal()
	{
		return vals[0];
	}

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * 
	 * @return double
	 */
	@Override
	public double getModulus()
	{
		return Math.sqrt(getSQModulus());
	}

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense
	 * to calculate this before the modulus itself.
	 * 
	 * @return double
	 */
	@Override
	public double getSQModulus()
	{
		double tR = 0d;
		for (int k = 0; k < vals.length; k++)
			tR += vals[k] * vals[k];
		return tR;
	}

	/**
	 * Check for the equality of this object with that of the argument. This
	 * checks for exact equality using no tolerances. The FieldObject types must
	 * match first.
	 * 
	 * @param pE
	 *            RealD
	 * @param pF
	 *            RealD
	 * @return boolean <tt>true</tt> if both components are the same;
	 *         <tt>false</tt>, otherwise.
	 */
	public static boolean isEqual(RealD pE, RealD pF)
	{
		return RealD.isTypeMatch(pE, pF) & pE.getReal() == pF.getReal();
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 */
	public static boolean isZero(RealD pF)
	{
		return (pF.getReal() == 0.0D);
	}

	/**
	 * This method checks to see if the value is infinite.
	 */
	public static boolean isInfinite(RealD pF)
	{
		return Double.isInfinite(pF.getReal());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 */
	public static boolean isNaN(RealD pF)
	{
		return Double.isNaN(pF.getReal());
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 */
	public RealD conjugate()
	{
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 */
	public RealD scale(double pS)
	{
		setReal(pS * getReal());
		return this;
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @throws FieldBinaryException
	 * @see com.interworldtransport.cladosF.DivFieldD#add(com.interworldtransport.cladosF.DivFieldD)
	 */
	@Override
	public RealD add(DivFieldD pF) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(this, pF))
		{
			setReal(getReal() + ((RealD) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Addition failed type match test", pF));
		return this;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#subtract(com.interworldtransport.cladosF.DivFieldD)
	 */
	@Override
	public RealD subtract(DivFieldD pF) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(this, pF))
		{
			setReal(getReal() - ((RealD) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Subtraction failed type match test", pF));
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#multiply(com.interworldtransport.cladosF.DivFieldD)
	 */
	@Override
	public RealD multiply(DivFieldD pF) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(this, pF))
		{
			setReal(getReal() * ((RealD) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Multiply failed type match test", pF));
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#divide(com.interworldtransport.cladosF.DivFieldD)
	 */
	@Override
	public RealD divide(DivFieldD pF) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(this, pF))
		{
			if (!RealD.isZero((RealD) pF))
				setReal(getReal() / ((RealD) pF).getReal());
			else
				throw (new FieldBinaryException(this,
								"Divide by Zero detected", pF));
		}
		else
			throw (new FieldBinaryException(this,
							"Divide failed type match test", pF));
		return this;
	}

	/**
	 * This method inverts real numbers.
	 */
	public RealD invert() throws FieldException
	{
		if (!RealD.isZero(this))
			setReal(1.0 / getReal());
		else
			throw new FieldException(this, "Can't invert a zero RealD");
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#toXMLString()
	 */
	@Override
	public String toXMLString()
	{
		return ("<RealD type=\"" + getFieldTypeString() + "\" value=\""
						+ getReal() + "\" />");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#toString()
	 */
	@Override
	public String toString()
	{
		return (getReal() + "DR");
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy does not reuse a field type reference so it is likely to fail type
	 * mismatch tests.
	 * 
	 * @param pR
	 *            double
	 */
	public static RealD create(double pR)
	{
		return new RealD(pR);
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy reuses the field type reference to ensure it will pass a type match
	 * test.
	 * 
	 * @param pF
	 *            RealD
	 */
	public static RealD copy(RealD pF)
	{
		return new RealD(pF);
	}

	/**
	 * Static method that creates a new RealD with a copy of the field type but
	 * not the value. Since this copy reuses the field type reference it will
	 * pass a type match test with pF but not the isEqual test.
	 * 
	 * @param pF
	 *            RealD
	 */
	public static RealD copyzero(RealD pF)
	{
		return new RealD(pF.getFieldType());
	}

	/**
	 * Static method that creates a new RealD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            RealD
	 */
	public static RealD conjugate(RealD pF)
	{
		return new RealD(pF);
	}

	/**
	 * Static add method that creates a new RealD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 */
	public static RealD add(RealD pF1, RealD pF2) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) | !RealD.isNaN(pF1) | !RealD.isNaN(pF2)
						| !RealD.isInfinite(pF1) | !RealD.isInfinite(pF2))
		{
			return new RealD(pF1.getFieldType(), pF1.getReal() + pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Addition error found",
							pF2));
	}

	/**
	 * Static subtract method that creates a new RealD with the difference pF1 -
	 * pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 */
	public static RealD subtract(RealD pF1, RealD pF2)
					throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) | !RealD.isNaN(pF1) | !RealD.isNaN(pF2)
						| !RealD.isInfinite(pF1) | !RealD.isInfinite(pF2))
		{
			return new RealD(pF1.getFieldType(), pF1.getReal() - pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Subtraction error found", pF2));
	}

	/**
	 * Static multiply method that creates a new RealD with the product pF1 *
	 * pF2. product.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 */
	public static RealD multiply(RealD pF1, RealD pF2)
					throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) | !RealD.isNaN(pF1) | !RealD.isNaN(pF2)
						| !RealD.isInfinite(pF1) | !RealD.isInfinite(pF2))
		{
			return new RealD(pF1.getFieldType(), pF1.getReal() * pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Multiplication error found", pF2));
	}

	/**
	 * Static divide method that creates a new RealD with the product pF1 / pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 */
	public static RealD divide(RealD pF1, RealD pF2)
					throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) | !RealD.isZero(pF2)
						| !RealD.isNaN(pF1) | !RealD.isNaN(pF2)
						| !RealD.isInfinite(pF1) | !RealD.isInfinite(pF2))
		{
			return new RealD(pF1.getFieldType(), pF1.getReal() / pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Division error found",
							pF2));
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @return RealD
	 * @throws FieldBinaryException
	 */
	public static RealD SQModulusList(RealD[] pL) throws FieldBinaryException
	{
		RealD tR = new RealD(pL[0].getFieldType(), pL[0].getSQModulus());

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new RealD(pL[j].getFieldType(), pL[j].getSQModulus()));
		}
		return tR;
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list. Because these are real numbers,
	 * though, we get away with simply summing the moduli instead. It does not
	 * perform a field type safety check and will throw the exception if that
	 * test fails.
	 * 
	 * @return RealD
	 * @throws FieldBinaryException
	 */
	public static RealD ModulusList(RealD[] pL) throws FieldBinaryException
	{
		RealD tR = new RealD(pL[0].getFieldType(), pL[0].getModulus());

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new RealD(pL[j].getFieldType(), pL[j].getModulus()));
		}
		return tR;
	}

	/**
	 * Static zero construction method
	 */
	public static RealD ZERO(String pS)
	{
		return new RealD(new DivFieldType(pS), 0.0D);
	}

	/**
	 * Static one construction method
	 */
	public static RealD ONE(String pS)
	{
		return new RealD(new DivFieldType(pS), 1.0D);
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static RealD ZERO(RealD pR)
	{
		return new RealD(pR.getFieldType(), 0.0D);
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static RealD ONE(RealD pR)
	{
		return new RealD(pR.getFieldType(), 1.0D);
	}
}
