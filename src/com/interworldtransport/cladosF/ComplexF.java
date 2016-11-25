/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.ComplexF<br>
 * -------------------------------------------------------------------- <p>
 * You ("Licensee") are granted a license to this software under the terms of 
 * the GNU General Public License. A full copy of the license can be found 
 * bundled with this package or code file. If the license file has become 
 * separated from the package, code file, or binary executable, the Licensee is
 * still expected to read about the license at the following URL before 
 * accepting this material. 
 * <code>http://www.opensource.org/gpl-license.html</code><p> 
 * Use of this code or executable objects derived from it by the Licensee states
 * their willingness to accept the terms of the license. <p> 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.ComplexF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Complex Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and ComplexF is one
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
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class ComplexF extends DivFieldF
{
	/**
	 * Static add method that creates a new ComplexF with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            ComplexF
	 * @param pF2
	 *            ComplexF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return ComplexF
	 */
	public static ComplexF add(ComplexF pF1, ComplexF pF2)
					throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(pF1, pF2) | !ComplexF.isNaN(pF1)
						| !ComplexF.isNaN(pF2) | !ComplexF.isInfinite(pF1)
						| !ComplexF.isInfinite(pF2))
		{
			return new ComplexF(pF1.getFieldType(), pF1.getReal()
							+ pF2.getReal(), pF1.getImg() + pF2.getImg());
		}
		else
			throw (new FieldBinaryException(pF1, "Static Addition error found",
							pF2));
	}
	
	/**
	 * Static method that creates a new ComplexF with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            ComplexF
	 * @return ComplexF
	 */
	public static ComplexF conjugate(ComplexF pF)
	{
		return new ComplexF(pF.getFieldType(), pF.getReal(), -1.0f
						* pF.getImg());
	}

	/**
	 * Static method that creates a new ComplexF with a copy of the parameter.
	 * This copy reuses the field type reference to ensure it will pass a type
	 * match test.
	 * 
	 * @param pF
	 *            ComplexF
	 * @return ComplexF
	 */
	public static ComplexF copy(ComplexF pF)
	{
		return new ComplexF(pF);
	}

	/**
	 * Static method that creates a new ComplexF with a copy of the field type
	 * but not the value. Since this copy reuses the field type reference it
	 * will pass a type match test with pF but not the isEqual test.
	 * 
	 * @param pF
	 *            ComplexF
	 * @return ComplexF
	 */
	public static ComplexF copyzero(ComplexF pF)
	{
		return new ComplexF(pF.getFieldType());
	}

	/**
	 * Static method that creates a new ComplexF with a copy of the parameter.
	 * This copy does not reuse a field type reference so it is likely to fail
	 * type mismatch tests.
	 * 
	 * @param pR
	 *            float
	 * @param pI
	 *            float
	 * @return ComplexF
	 */
	public static ComplexF create(float pR, float pI)
	{
		return new ComplexF(pR, pI);
	}

	/**
	 * Static divide method that creates a new ComplexF with the product pF1 /
	 * pF2.
	 * 
	 * @param pF1
	 *            ComplexF
	 * @param pF2
	 *            ComplexF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return ComplexF
	 */
	public static ComplexF divide(ComplexF pF1, ComplexF pF2)
					throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(pF1, pF2) | !ComplexF.isZero(pF2)
						| !ComplexF.isNaN(pF1) | !ComplexF.isNaN(pF2)
						| !ComplexF.isInfinite(pF1) | !ComplexF.isInfinite(pF2))
		{
			ComplexF tZ = new ComplexF(pF1);
			pF2.conjugate();
			tZ.multiply(pF2);
			pF2.conjugate();
			tZ.scale(1.0f / pF2.getSQModulus());
			return tZ;
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
	 *            ComplexF
	 * @param pF
	 *            ComplexF
	 * @return boolean <tt>true</tt> if both components are the same;
	 *         <tt>false</tt>, otherwise.
	 */
	public static boolean isEqual(ComplexF pE, ComplexF pF)
	{
		return ComplexF.isTypeMatch(pE, pF) & pE.getReal() == pF.getReal()
						& pE.getImg() == pF.getImg();
	}

	/**
	 * Returns true if the imaginary part is zero
	 * 
	 * @param pF 
	 * 			ComplexF
	 * @return boolean
	 */
	public static boolean isImaginary(ComplexF pF)
	{
		return (pF.getReal() == 0.0f);
	}

	/**
	 * This method checks to see if the value is infinite.
	 * 
	 * @param pF 
	 * 			ComplexF
	 * @return boolean
	 */
	public static boolean isInfinite(ComplexF pF)
	{
		return Float.isInfinite(pF.getReal()) | Float.isInfinite(pF.getImg());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 * 
	 * @param pF 
	 * 			ComplexF
	 * @return boolean
	 */
	public static boolean isNaN(ComplexF pF)
	{
		return Float.isNaN(pF.getReal()) | Float.isNaN(pF.getImg());
	}

	/**
	 * Returns true if the imaginary part is zero
	 * 
	 * @param pF 
	 * 			ComplexF
	 * @return boolean
	 */
	public static boolean isReal(ComplexF pF)
	{
		return (pF.getImg() == 0.0f);
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * 
	 * @param pF 
	 * 			ComplexF
	 * @return boolean
	 */
	public static boolean isZero(ComplexF pF)
	{
		return (pF.getReal() == 0.0f & pF.getImg() == 0.0f);
	}
	
	/**
	 * This static method takes a list of ComplexF objects and returns one
	 * ComplexF that has a real value that is equal to the square root of the
	 * sum of the SQModulus of each entry on the list. It does not perform a
	 * field type safety check and will throw the exception if that test fails.
	 * 
	 * @param pL
	 * 			ComplexF[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception happens when there is a field mismatch. It shouldn't happen
	 * 	but it is technically possible because of the dependence on sqMagnitude.
	 * @return complexF
	 */
	public static ComplexF ModulusList(ComplexF[] pL)
					throws FieldBinaryException
	{
		ComplexF tR = ComplexF.SQModulusList(pL);
		// now figure out how to do the SQRT of this complex object.
		float tM = (float) Math.sqrt(tR.getModulus());
		float tA = tR.getArgument() / 2;
		tR.setReal((float) (tM * Math.cos(tA)));
		tR.setImg((float) (tM * Math.sin(tA)));
		return tR;
	}
	
	/**
	 * Static multiply method that creates a new ComplexF with the product pF1 *
	 * pF2.
	 * 
	 * @param pF1
	 *            ComplexF
	 * @param pF2
	 *            ComplexF
	 * @throws FieldBinaryException
	 * 	This exception happens when there is a field mismatch.
	 * @return complexF
	 */
	public static ComplexF multiply(ComplexF pF1, ComplexF pF2)
					throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(pF1, pF2) | !ComplexF.isNaN(pF1)
						| !ComplexF.isNaN(pF2) | !ComplexF.isInfinite(pF1)
						| !ComplexF.isInfinite(pF2))
		{
			float tempR = pF1.getReal() * pF2.getReal() - pF1.getImg()
							* pF2.getImg();
			float tempI = pF1.getReal() * pF2.getImg() + pF1.getImg()
							* pF2.getReal();
			return new ComplexF(pF1.getFieldType(), tempR, tempI);
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Multiplication error found", pF2));
	}

	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 			ComplexF
	 * 
	 * @return ComplexF
	 */
	public static ComplexF ONE(ComplexF pR)
	{
		return new ComplexF(pR.getFieldType(), 1.0f, 0.0f);
	}
	
	/**
	 * Static one construction method
	 * 
	 * @param pS
	 * 			String
	 * 
	 * @return ComplexF
	 */
	public static ComplexF ONE(String pS)
	{
		return new ComplexF(new DivFieldType(pS), 1.0f, 0.0f);
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @param pL
	 * 			ComplexF[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch. It should never happen
	 * 	but the implementation uses multiplication, thus it is technically possible.
	 * @return ComplexF
	 */
	public static ComplexF SQModulusList(ComplexF[] pL)
					throws FieldBinaryException
	{
		ComplexF tR = new ComplexF(pL[0].getFieldType(), 0, 0);

		for (int j = 1; j < pL.length; j++)
		{
			tR.add(new ComplexF(pL[j].getFieldType(), pL[j].getSQModulus(), 0));
		}
		return tR;
	}

	/**
	 * Static subtract method that creates a new ComplexF with the difference
	 * pF1 - pF2.
	 * 
	 * @param pF1
	 *            ComplexF
	 * @param pF2
	 *            ComplexF
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch.
	 * @return ComplexF
	 */
	public static ComplexF subtract(ComplexF pF1, ComplexF pF2)
					throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(pF1, pF2) | !ComplexF.isNaN(pF1)
						| !ComplexF.isNaN(pF2) | !ComplexF.isInfinite(pF1)
						| !ComplexF.isInfinite(pF2))
		{
			return new ComplexF(pF1.getFieldType(), pF1.getReal()
							- pF2.getReal(), pF1.getImg() - pF2.getImg());
		}
		else
			throw (new FieldBinaryException(pF1,
							"Static Subtraction error found", pF2));
	}

	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 			ComplexF
	 * 
	 * @return ComplexF
	 */
	public static ComplexF ZERO(ComplexF pR)
	{
		return new ComplexF(pR.getFieldType(), 0.0f, 0.0f);
	}

	/**
	 * Static zero construction method
	 * 
	 * @param pS
	 * 			String
	 * 
	 * @return ComplexF
	 */
	public static ComplexF ZERO(String pS)
	{
		return new ComplexF(new DivFieldType(pS), 0.0f, 0.0f);
	}
	
	/**
	 * Basic Constructor with no values to initialize.
	 */
	public ComplexF()
	{
		vals	= new float[2];
		setFieldType(new DivFieldType("Complex"));
		setReal(0.0f);
		setImg(0.0f);
	}

	/**
	 * Copy Constructor that reuses the field type reference.
	 * 
	 * @param pC
	 * 		ComplexF
	 */
	public ComplexF(ComplexF pC)
	{
		vals	= new float[2];
		setFieldType(pC.getFieldType());
		setReal(pC.getReal());
		setImg(pC.getImg());
	}

	/**
	 * Copy Constructor that reuses the field type reference while allowing the
	 * values to be set.
	 * 
	 * @param pC
	 * 		ComplexF
	 * @param pR
	 * 		float
	 * @param pI
	 * 		float
	 */
	public ComplexF(ComplexF pC, float pR, float pI)
	{
		vals	= new float[2];
		setFieldType(pC.getFieldType());
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the field type to initialize.
	 * 
	 * @param pT
	 * 		DivFieldType
	 */
	public ComplexF(DivFieldType pT)
	{
		vals	= new float[2];
		setFieldType(pT);
		setReal(0.0f);
		setImg(0.0f);
	}

	/**
	 * Basic Constructor with everything to initialize but the imaginary.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		float
	 */
	public ComplexF(DivFieldType pT, float pR)
	{
		vals	= new float[2];
		setFieldType(pT);
		setReal(pR);
		setImg(0.0f);
	}

	/**
	 * Basic Constructor with everything to initialize it.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		float
	 * @param pI
	 * 		float
	 */
	public ComplexF(DivFieldType pT, float pR, float pI)
	{
		vals	= new float[2];
		setFieldType(pT);
		setReal(pR);
		setImg(pI);
	}
	
	/**
	 * Basic Constructor with only the number to initialize.
	 * 
	 * @param pR
	 * 		float
	 * @param pI
	 * 		float
	 */
	public ComplexF(float pR, float pI)
	{
		vals	= new float[2];
		setFieldType(new DivFieldType("Complex"));
		setReal(pR);
		setImg(pI);
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when a field mismatch happens
	 * @see com.interworldtransport.cladosF.DivFieldF#add(com.interworldtransport.cladosF.DivFieldF)
	 */
	@Override
	public ComplexF add(DivFieldF pF) throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(this, pF))
		{
			setReal(getReal() + ((ComplexF) pF).getReal());
			setImg(getImg() + ((ComplexF) pF).getImg());
		}
		else
			throw (new FieldBinaryException(this,
							"Addition failed type match test", pF));
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return ComplexF
	 */
	public ComplexF conjugate()
	{
		setImg(-1.0f * getImg());
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 */
	@Override
	public ComplexF divide(DivFieldF pF) throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(this, pF))
		{
			ComplexF tempZ = (ComplexF) pF;
			tempZ.conjugate();
			multiply(tempZ);
			tempZ.conjugate();
			scale(1.0f / tempZ.getSQModulus());
		}
		else
			throw (new FieldBinaryException(this,
							"Divide failed type match test", pF));
		return this;
	}

	/**
	 * Get method for the argument of the complex number. This function uses the
	 * arctangent function, so its range and domain are the same.
	 * 
	 * @return float
	 */
	public float getArgument()
	{
		if (!ComplexF.isImaginary(this))
			return (float) Math.atan(getImg() / getReal());
		else
			return (float) ((getImg() >= 0f) ? Math.PI / 2.0f
							: 3.0D * Math.PI / 2.0f);
	}

	/**
	 * Get the imaginary numeric value from the value array
	 * 
	 * @return float
	 */
	public float getImg()
	{
		return vals[1];
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
	 * 
	 * @throws FieldException
	 * 	This exception is thrown when someone tries to invert ZERO.
	 * 
	 * @return ComplexF
	 */
	public ComplexF invert() throws FieldException
	{
		if (!ComplexF.isZero(this))
		{
			float tM = 1.0f / getModulus();
			float tA = -1.0f * getArgument();
			setReal((float) (tM * Math.cos(tA)));
			setImg((float) (tM * Math.sin(tA)));
		}
		else
			throw new FieldException(this, "Can't invert a zero ComplexF");
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldF
	 * @return ComplexF
	 */
	@Override
	public ComplexF multiply(DivFieldF pF) throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(this, pF))
		{
			ComplexF tempZ = (ComplexF) pF;
			float tempR = getReal() * tempZ.getReal() - getImg()
							* tempZ.getImg();
			float tempI = getReal() * tempZ.getImg() + getImg()
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
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		float
	 * @return ComplexF
	 */
	public ComplexF scale(float pS)
	{
		float tempS = (float) Math.sqrt(Math.abs(pS));
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
	 * Set the imaginary numeric value
	 * 
	 * @param pimg
	 *            float
	 */
	public void setImg(float pimg)
	{
		vals[1] = pimg;
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
	 * @param pF
	 *            DivFieldF
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch.
	 * @return ComplexF
	 */
	@Override
	public ComplexF subtract(DivFieldF pF) throws FieldBinaryException
	{
		if (ComplexF.isTypeMatch(this, pF))
		{
			ComplexF tempZ = (ComplexF) pF;
			setReal(getReal() - tempZ.getReal());
			setImg(getImg() - tempZ.getImg());
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
		return (getReal() + "R, " + getImg() + "I");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivFieldF#toXMLString()
	 */
	@Override
	public String toXMLString()
	{
		return ("<ComplexF type=\"" + getFieldTypeString() + "\" realvalue=\""
						+ getReal() + "\" imgvalue=\"" + getImg() + "\"/>");
	}

}
