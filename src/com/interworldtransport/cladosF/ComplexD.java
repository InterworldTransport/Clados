/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosF.ComplexD<br>
 * --------
 * ------------------------------------------------------------------------ <p>
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
 * ---com.interworldtransport.cladosF.ComplexD<br>
 * ------------------------------
 * --------------------------------------------------
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Complex Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and ComplexD is one
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
public class ComplexD extends DivFieldD
{
	/**
	 * The number(s)!
	 */
	//private double[]	vals	= new double[2];

	/**
	 * Basic Constructor with no values to initialize.
	 */
	public ComplexD()
	{
		vals	= new double[2];
		setFieldType(new DivFieldType("Complex"));
		setReal(0.0D);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with only the field type to initialize.
	 */
	public ComplexD(DivFieldType pT)
	{
		vals	= new double[2];
		setFieldType(pT);
		setReal(0.0D);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize but the imaginary.
	 */
	public ComplexD(DivFieldType pT, double pR)
	{
		vals	= new double[2];
		setFieldType(pT);
		setReal(pR);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize it.
	 */
	public ComplexD(DivFieldType pT, double pR, double pI)
	{
		vals	= new double[2];
		setFieldType(pT);
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the number to initialize.
	 */
	public ComplexD(double pR, double pI)
	{
		vals	= new double[2];
		setFieldType(new DivFieldType("Complex"));
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Copy Constructor that reuses the field type reference.
	 */
	public ComplexD(ComplexD pC)
	{
		vals	= new double[2];
		setFieldType(pC.getFieldType());
		setReal(pC.getReal());
		setImg(pC.getImg());
	}
	
	/**
	 * Copy Constructor that reuses the field type reference while allowing the
	 * values to be set.
	 */
	public ComplexD(ComplexD pC, double pR, double pI)
	{
		vals	= new double[2];
		setFieldType(pC.getFieldType());
		setReal(pR);
		setImg(pI);
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
	 * Set the imaginary numeric value
	 * 
	 * @param pimg
	 *            double
	 */
	public void setImg(double pimg)
	{
		vals[1] = pimg;
	}

	/**
	 * Set the imaginary numeric value
	 */
	public double getImg()
	{
		return vals[1];
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
	 * Get method for the argument of the complex number. This function uses the
	 * arctangent function, so its range and domain are the same.
	 * 
	 * @return double
	 */
	public double getArgument()
	{
		if (!ComplexD.isImaginary(this))
			return Math.atan(getImg() / getReal());
		else
			return (getImg() >= 0) ? Math.PI / 2.0D : 3.0D * Math.PI / 2.0D;
	}

	/**
	 * Check for the equality of this object with that of the argument. This
	 * checks for exact equality using no tolerances. The FieldObject types must
	 * match first.
	 * 
	 * @param pE
	 *            ComplexD
	 * @param pF
	 *            ComplexD
	 * @return boolean <tt>true</tt> if both components are the same;
	 *         <tt>false</tt>, otherwise.
	 */
	public static boolean isEqual(ComplexD pE, ComplexD pF)
	{
		return ComplexD.isTypeMatch(pE, pF) & pE.getReal() == pF.getReal()
						& pE.getImg() == pF.getImg();
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 */
	public static boolean isZero(ComplexD pF)
	{
		return (pF.getReal() == 0.0D & pF.getImg() == 0.0D);
	}

	/**
	 * This method checks to see if the value is infinite.
	 */
	public static boolean isInfinite(ComplexD pF)
	{
		return Double.isInfinite(pF.getReal()) | Double.isInfinite(pF.getImg());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 */
	public static boolean isNaN(ComplexD pF)
	{
		return Double.isNaN(pF.getReal()) | Double.isNaN(pF.getImg());
	}

	/**
	 * Returns true if the imaginary part is zero
	 */
	public static boolean isReal(ComplexD pF)
	{
		return (pF.getImg() == 0.0d);
	}

	/**
	 * Returns true if the imaginary part is zero
	 */
	public static boolean isImaginary(ComplexD pF)
	{
		return (pF.getReal() == 0.0d);
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 */
	public ComplexD conjugate()
	{
		setImg(-1.0D * getImg());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 */
	public ComplexD scale(double pS)
	{
		double tempS = Math.sqrt(Math.abs(pS));
		setReal(tempS * getReal());
		setImg(tempS * getImg());
		if (pS < 0)
		{
			setReal(-1 * getReal());
			setImg(-1 * getImg());
		}
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
	public ComplexD add(DivFieldD pF) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(this, pF))
		{
			setReal(getReal() + ((ComplexD) pF).getReal());
			setImg(getImg() + ((ComplexD) pF).getImg());
		}
		else
			throw (new FieldBinaryException(this,
							"Addition failed type match test", pF));
		return this;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the
	 * result.
	 */
	@Override
	public ComplexD subtract(DivFieldD pF) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(this, pF))
		{
			ComplexD tempZ = (ComplexD) pF;
			setReal(getReal() - tempZ.getReal());
			setImg(getImg() - tempZ.getImg());
		}
		else
			throw (new FieldBinaryException(this,
							"Subtraction failed type match test", pF));
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 */
	@Override
	public ComplexD multiply(DivFieldD pF) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(this, pF))
		{
			ComplexD tempZ = (ComplexD) pF;
			double tempR = getReal() * tempZ.getReal() - getImg()
							* tempZ.getImg();
			double tempI = getReal() * tempZ.getImg() + getImg()
							* tempZ.getReal();
			setReal(tempR);
			setImg(tempI);
		}
		else
			throw (new FieldBinaryException(this,
							"Multiply failed type match test", pF));
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 */
	@Override
	public ComplexD divide(DivFieldD pF) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(this, pF))
		{
			ComplexD tempZ = (ComplexD) pF;
			tempZ.conjugate();
			multiply(tempZ);
			tempZ.conjugate();
			scale(1.0D / tempZ.getSQModulus());
		}
		else
			throw (new FieldBinaryException(this,
							"Divide failed type match test", pF));
		return this;
	}

	/**
	 * This method inverts real numbers.
	 */
	public ComplexD invert() throws FieldException
	{
		if (!ComplexD.isZero(this))
		{
			double tM = 1.0 / getModulus();
			double tA = -1.0 * getArgument();
			setReal(tM * Math.cos(tA));
			setImg(tM * Math.sin(tA));
		}
		else
			throw new FieldException(this, "Can't invert a zero ComplexD");
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
		return ("<ComplexD type=\"" + getFieldTypeString() + "\" realvalue=\""
						+ getReal() + "\" imgvalue=\"" + getImg() + "\"/>");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldD#toString()
	 */
	@Override
	public String toString()
	{
		return (getReal() + "R, " + getImg() + "I");
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the parameter.
	 * This copy does not reuse a field type reference so it is likely to fail
	 * type mismatch tests.
	 * 
	 * @param pR
	 *            double
	 * @param pI
	 *            double
	 */
	public static ComplexD create(double pR, double pI)
	{
		return new ComplexD(pR, pI);
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the parameter.
	 * This copy reuses the field type reference to ensure it will pass a type
	 * match test.
	 * 
	 * @param pF
	 *            ComplexD
	 */
	public static ComplexD copy(ComplexD pF)
	{
		return new ComplexD(pF);
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the field type
	 * but not the value. Since this copy reuses the field type reference it
	 * will pass a type match test with pF but not the isEqual test.
	 * 
	 * @param pF
	 *            ComplexD
	 */
	public static ComplexD copyzero(ComplexD pF)
	{
		return new ComplexD(pF.getFieldType());
	}

	/**
	 * Static method that creates a new ComplexD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            ComplexD
	 */
	public static ComplexD conjugate(ComplexD pF)
	{
		return new ComplexD(pF.getFieldType(), pF.getReal(), -1.0D
						* pF.getImg());
	}

	/**
	 * Static add method that creates a new ComplexD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public static ComplexD add(ComplexD pF1, ComplexD pF2)
					throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) | !ComplexD.isNaN(pF1)
						| !ComplexD.isNaN(pF2) | !ComplexD.isInfinite(pF1)
						| !ComplexD.isInfinite(pF2))
		{
			return new ComplexD(pF1.getFieldType(), pF1.getReal()
							+ pF2.getReal(), pF1.getImg() + pF2.getImg());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Addition error found",
							pF2));
	}

	/**
	 * Static subtract method that creates a new ComplexD with the difference
	 * pF1 - pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public static ComplexD subtract(ComplexD pF1, ComplexD pF2)
					throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) | !ComplexD.isNaN(pF1)
						| !ComplexD.isNaN(pF2) | !ComplexD.isInfinite(pF1)
						| !ComplexD.isInfinite(pF2))
		{
			return new ComplexD(pF1.getFieldType(), pF1.getReal()
							- pF2.getReal(), pF1.getImg() - pF2.getImg());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Subtraction error found", pF2));
	}

	/**
	 * Static multiply method that creates a new ComplexD with the product pF1 *
	 * pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public static ComplexD multiply(ComplexD pF1, ComplexD pF2)
					throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) | !ComplexD.isNaN(pF1)
						| !ComplexD.isNaN(pF2) | !ComplexD.isInfinite(pF1)
						| !ComplexD.isInfinite(pF2))
		{
			double tempR = pF1.getReal() * pF2.getReal() - pF1.getImg()
							* pF2.getImg();
			double tempI = pF1.getReal() * pF2.getImg() + pF1.getImg()
							* pF2.getReal();
			return new ComplexD(pF1.getFieldType(), tempR, tempI);
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Multiplication error found", pF2));
	}

	/**
	 * Static divide method that creates a new ComplexD with the product pF1 /
	 * pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public static ComplexD divide(ComplexD pF1, ComplexD pF2)
					throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) | !ComplexD.isZero(pF2)
						| !ComplexD.isNaN(pF1) | !ComplexD.isNaN(pF2)
						| !ComplexD.isInfinite(pF1) | !ComplexD.isInfinite(pF2))
		{
			ComplexD tZ = new ComplexD(pF1);
			pF2.conjugate();
			tZ.multiply(pF2);
			pF2.conjugate();
			tZ.scale(1.0D / pF2.getSQModulus());
			return tZ;
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
	 * @return ComplexD
	 * @throws FieldBinaryException
	 */
	public static ComplexD SQModulusList(ComplexD[] pL)
					throws FieldBinaryException
	{
		ComplexD tR = new ComplexD(pL[0].getFieldType(), 0, 0);

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new ComplexD(pL[j].getFieldType(), pL[j].getSQModulus(), 0));
		}
		return tR;
	}

	/**
	 * This static method takes a list of ComplexD objects and returns one
	 * ComplexD that has a real value that is equal to the square root of the
	 * sum of the SQModulus of each entry on the list. It does not perform a
	 * field type safety check and will throw the exception if that test fails.
	 * 
	 * @return ComplexD
	 */
	public static ComplexD ModulusList(ComplexD[] pL)
					throws FieldBinaryException
	{
		ComplexD tR = ComplexD.SQModulusList(pL);
		// now figure out how to do the SQRT of this complex object.
		double tM = Math.sqrt(tR.getModulus());
		double tA = tR.getArgument() / 2;
		tR.setReal(tM * Math.cos(tA));
		tR.setImg(tM * Math.sin(tA));
		return tR;
	}

	/**
	 * Static zero construction method
	 */
	public static ComplexD ZERO(String pS)
	{
		return new ComplexD(new DivFieldType(pS), 0.0, 0.0);
	}

	/**
	 * Static one construction method
	 */
	public static ComplexD ONE(String pS)
	{
		return new ComplexD(new DivFieldType(pS), 1.0, 0.0);
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static ComplexD ZERO(ComplexD pR)
	{
		return new ComplexD(pR.getFieldType(), 0.0, 0.0);
	}

	/**
	 * Static zero construction method with copied field type
	 */
	public static ComplexD ONE(ComplexD pR)
	{
		return new ComplexD(pR.getFieldType(), 1.0, 0.0);
	}
}
