/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosF.RealF<br>
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
 * ---com.interworldtransport.cladosF.RealF<br>
 * ----------------------------------
 * ----------------------------------------------
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosFExceptions.FieldException;

/**
 * This class implements the concept of a Real Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and RealF is one
 * possibility.
 * <p>
 * There is no doubt that the overhead related to this class is a waste of
 * resources. However, it allows one to plug fields into the algebra classes
 * without having to maintain many different types of monads and nyads. If Java
 * came with primitive types for complex and quaternion fields, and other
 * primitives implemented a 'Field' interface, I wouldn't bother writing this
 * object or any of the other decendents of DivFieldF.
 * <p>
 * Applications requiring speed should use the monads and nyads that impliment
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Float and implement an interface called
 * DivFieldF. That can't be done, though, because Float is final.
 * <p>
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class RealF extends DivFieldF
{
	/**
	 * The number(s)!
	 */
	// private float[] vals = new float[1];

	/**
	 * Static add method that creates a new RealD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 */
	public static RealF add(RealF pF1, RealF pF2) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) | !RealF.isNaN(pF1) | !RealF.isNaN(pF2)
						| !RealF.isInfinite(pF1) | !RealF.isInfinite(pF2))
		{
			return new RealF(pF1.getFieldType(), pF1.getReal() + pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Addition error found",
							pF2));
	}

	/**
	 * Static method that creates a new RealD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            RealF
	 */
	public static RealF conjugate(RealF pF)
	{
		return new RealF(pF);
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy reuses the field type reference to ensure it will pass a type match
	 * test.
	 * 
	 * @param pF
	 *            RealF
	 */
	public static RealF copy(RealF pF)
	{
		return new RealF(pF);
	}

	/**
	 * Static method that creates a new RealD with a copy of the field type but
	 * not the value. Since this copy reuses the field type reference it will
	 * pass a type match test with pF but not the isEqual test.
	 * 
	 * @param pF
	 *            RealF
	 */
	public static RealF copyzero(RealF pF)
	{
		return new RealF(pF.getFieldType());
	}

	

	/**
	 * Static divide method that creates a new RealD with the product pF1 / pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 */
	public static RealF divide(RealF pF1, RealF pF2)
					throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) | !RealF.isZero(pF2)
						| !RealF.isNaN(pF1) | !RealF.isNaN(pF2)
						| !RealF.isInfinite(pF1) | !RealF.isInfinite(pF2))
		{
			return new RealF(pF1.getFieldType(), pF1.getReal() / pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Division error found",
							pF2));
	}

	/**
	 * Check for the equality of this object with that of the argument. This
	 * checks for exact equality using no tolerances. The FieldObject types must
	 * match first.
	 * 
	 * @param pE
	 *            RealF
	 * @param pF
	 *            RealF
	 * @return boolean <tt>true</tt> if both components are the same;
	 *         <tt>false</tt>, otherwise.
	 */
	public static boolean isEqual(RealF pE, RealF pF)
	{
		return RealF.isTypeMatch(pE, pF) & pE.getReal() == pF.getReal();
	}

	/**
	 * This method checks to see if the value is infinite.
	 */
	public static boolean isInfinite(RealF pF)
	{
		return Float.isInfinite(pF.getReal());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 */
	public static boolean isNaN(RealF pF)
	{
		return Float.isNaN(pF.getReal());
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 */
	public static boolean isZero(RealF pF)
	{
		return (pF.getReal() == 0.0F);
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list. Because these are real numbers,
	 * though, we get away with simply summing the moduli instead. It does not
	 * perform a field type safety check and will throw the exception if that
	 * test fails.
	 * 
	 * @return RealF
	 */
	public static RealF ModulusList(RealF[] pL) throws FieldBinaryException
	{
		RealF tR = new RealF(pL[0].getFieldType(), pL[0].getModulus());

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new RealF(pL[j].getFieldType(), pL[j].getModulus()));
		}
		return tR;
	}

	/**
	 * Static multiply method that creates a new RealD with the product pF1 *
	 * pF2. product.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 */
	public static RealF multiply(RealF pF1, RealF pF2)
					throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) | !RealF.isNaN(pF1) | !RealF.isNaN(pF2)
						| !RealF.isInfinite(pF1) | !RealF.isInfinite(pF2))
		{
			return new RealF(pF1.getFieldType(), pF1.getReal() * pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Multiplication error found", pF2));
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static RealF ONE(RealF pR)
	{
		return new RealF(pR.getFieldType(), 1.0f);
	}

	/**
	 * Static one construction method
	 */
	public static RealF ONE(String pS)
	{
		return new RealF(new DivFieldType(pS), 1.0f);
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @return RealF
	 * @throws FieldBinaryException
	 */
	public static RealF SQModulusList(RealF[] pL) throws FieldBinaryException
	{
		RealF tR = new RealF(pL[0].getFieldType(), pL[0].getSQModulus());

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new RealF(pL[j].getFieldType(), pL[j].getSQModulus()));
		}
		return tR;
	}

	/**
	 * Static subtract method that creates a new RealD with the difference pF1 -
	 * pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 */
	public static RealF subtract(RealF pF1, RealF pF2)
					throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) | !RealF.isNaN(pF1) | !RealF.isNaN(pF2)
						| !RealF.isInfinite(pF1) | !RealF.isInfinite(pF2))
		{
			return new RealF(pF1.getFieldType(), pF1.getReal() - pF2.getReal());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Subtraction error found", pF2));
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static RealF ZERO(RealF pR)
	{
		return new RealF(pR.getFieldType(), 0.0f);
	}

	/**
	 * Static zero construction method
	 */
	public static RealF ZERO(String pS)
	{
		return new RealF(new DivFieldType(pS), 0.0f);
	}

	/**
	 * Basic Constructor with no values to initialize.
	 */
	public RealF()
	{
		vals = new float[1];
		setFieldType(new DivFieldType("Real"));
		setReal(0.0F);
	}

	/**
	 * Basic Constructor with only the field type to initialize.
	 */
	public RealF(DivFieldType pT)
	{
		vals = new float[1];
		setFieldType(pT);
		setReal(0.0F);
	}

	/**
	 * Basic Constructor with everything to initialize.
	 */
	public RealF(DivFieldType pT, float pR)
	{
		vals = new float[1];
		setFieldType(pT);
		setReal(pR);
	}

	/**
	 * Basic Constructor with only the number to initialize.
	 */
	public RealF(float pR)
	{
		vals = new float[1];
		setFieldType(new DivFieldType("Real"));
		setReal(pR);
	}

	/**
	 * Copy Constructor that reuses the field type reference.
	 */
	public RealF(RealF pR)
	{
		vals = new float[1];
		setFieldType(pR.getFieldType());
		setReal(pR.getReal());
	}

	/**
	 * Copy Constructor that reuses the field type reference while allowing the
	 * value to be set.
	 */
	public RealF(RealF pR, float pF)
	{
		vals = new float[1];
		setFieldType(pR.getFieldType());
		setReal(pF);
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @throws FieldBinaryException
	 * @see com.interworldtransport.cladosF.DivFieldF#add(com.interworldtransport.cladosF.DivFieldF)
	 */
	@Override
	public RealF add(DivFieldF pF) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(this, pF))
		{
			setReal(getReal() + ((RealF) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Addition failed type match test", pF));
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 */
	public RealF conjugate()
	{
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#divide(com.interworldtransport.cladosF.DivFieldF)
	 */
	@Override
	public RealF divide(DivFieldF pF) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(this, pF))
		{
			if (!RealF.isZero((RealF) pF))
				setReal(getReal() / ((RealF) pF).getReal());
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
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * 
	 * @return float
	 */
	@Override
	public float getModulus()
	{
		return (float) Math.sqrt(getSQModulus());
	}

	/**
	 * Get the real numeric value from the value array
	 * 
	 * @return float
	 */
	public float getReal()
	{
		return vals[0];
	}

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense
	 * to calculate this before the modulus itself.
	 * 
	 * @return float
	 */
	@Override
	public float getSQModulus()
	{
		float tR = 0f;
		for (int k = 0; k < vals.length; k++)
			tR += vals[k] * vals[k];
		return tR;
	}

	/**
	 * This method inverts real numbers.
	 */
	public RealF invert() throws FieldException
	{
		if (!RealF.isZero(this))
			setReal(1.0F / getReal());
		else
			throw new FieldException(this, "Can't invert a zero RealF");
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#multiply(com.interworldtransport.cladosF.DivFieldF)
	 */
	@Override
	public RealF multiply(DivFieldF pF) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(this, pF))
		{
			setReal(getReal() * ((RealF) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Multiply failed type match test", pF));
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 */
	public RealF scale(float pS)
	{
		setReal(pS * getReal());
		return this;
	}

	/**
	 * Set the real numeric value
	 * 
	 * @param preal
	 *            float
	 */
	public void setReal(float preal)
	{
		vals[0] = preal;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the
	 * result.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#subtract(com.interworldtransport.cladosF.DivFieldF)
	 */
	@Override
	public RealF subtract(DivFieldF pF) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(this, pF))
		{
			setReal(getReal() - ((RealF) pF).getReal());
		}
		else
			throw (new FieldBinaryException(this,
							"Subtraction failed type match test", pF));
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#toString()
	 */
	@Override
	public String toString()
	{
		return (getReal() + "FR");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#toXMLString()
	 */
	@Override
	public String toXMLString()
	{
		return ("<RealF type=\"" + getFieldTypeString() + "\" value=\""
						+ getReal() + "\" />");
	}

}
