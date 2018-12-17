/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.RealF<br>
 * -------------------------------------------------------------------- <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<p>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <p> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<p> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.RealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

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
 * object or any of the other descendants of DivFieldF.
 * <p>
 * Applications requiring speed should use the monads and nyads that implement
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Float and implement an interface called
 * DivFieldF. That can't be done, though, because Float is final.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class RealF extends DivField implements DivisableF
{
	/**
	 * Static add method that creates a new RealD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return RealF
	 */
	public static RealF add(RealF pF1, RealF pF2) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2)
						&& !RealF.isInfinite(pF1) && !RealF.isInfinite(pF2))
			return new RealF(pF1.getFieldType(), pF1.getReal() + pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Addition error found",	pF2));
	}

	/**
	 * Static method that creates a new RealD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            RealF
	 * @return RealF
	 */
	public static RealF conjugate(RealF pF)
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
	 * @return RealF
	 */
	//public static RealF copyAsZero(RealF pF)
	//{
	//	return new RealF(pF.getFieldType());
	//}

	/**
	 * This static method takes a list of RealF objects and returns one RealF
	 * that has a value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list. Because these are real numbers,
	 * though, we get away with simply summing the moduli instead. It does not
	 * perform a field type safety check and will throw the exception if that
	 * test fails.
	 * 
	 * @param pL
	 * 		RealF[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception is thrown when sqMagnitude fails with the RealF array
	 * @return RealF
	 */
	public static RealF copyFromModuliSum(RealF[] pL) throws FieldBinaryException
	{
		RealF tR = RealF.copyONE(pL[0]).scale(pL[0].getModulus());
		for (int j = 1; j < pL.length; j++)
			if (isTypeMatch(pL[j], tR))
				tR.add(RealF.copyONE(pL[j].scale(pL[j].getModulus())));
			else
				throw new FieldBinaryException(pL[j], "Field Type mistach during addition", tR);
		return tR;
	}

	/**
	 * This static method takes a list of RealF objects and returns one RealF
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @param pL
	 * 		RealF[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch. It should never happen
	 * 	but the implementation uses multiplication, thus it is technically possible.
	 * 
	 * @return RealF
	 */
	public static RealF copyFromSQModuliSum(RealF[] pL) throws FieldBinaryException
	{
		RealF tR = RealF.copyONE(pL[0]).scale(pL[0].getSQModulus());
		
		for (int j = 1; j < pL.length; j++)
			if (isTypeMatch(pL[j], tR))
				tR.add(RealF.copyONE(pL[j].scale(pL[j].getSQModulus())));
			else
				throw new FieldBinaryException(pL[j], "Field Type mistach during addition", tR);
		
		return tR;
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy reuses the field type reference to ensure it will pass a type match
	 * test.
	 * 
	 * @param pF
	 *            RealF
	 * @return RealF
	 */
	public static RealF copyOf(RealF pF)
	{
		return new RealF(pF);
	}

	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 		RealF
	 * 
	 * @return RealF
	 */
	public static RealF copyONE(RealF pR)
	{
		return new RealF(pR.getFieldType(), 1.0f);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 		RealF
	 * 
	 * @return RealF
	 */
	public static RealF copyZERO(RealF pR)
	{
		return new RealF(pR.getFieldType(), 0.0f);
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy does not reuse a field type reference so it is likely to fail type
	 * mismatch tests.
	 * 
	 * @param pR
	 *            float
	 * 
	 * 
	 * @return RealF
	 */
	public static RealF create(float pR)
	{
		return new RealF(pR);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Static divide method that creates a new RealD with the product pF1 / pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealF
	 */
	public static RealF divide(RealF pF1, RealF pF2)
					throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isZero(pF2)
						&& !RealF.isNaN(pF1) && !RealF.isNaN(pF2)
						&& !RealF.isInfinite(pF1) && !RealF.isInfinite(pF2))
			return new RealF(pF1.getFieldType(), pF1.getReal() / pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Division error found",	pF2));
		
		
		
		
		
		
		
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
	 * @return boolean <i>true</i> if both components are the same;
	 *         <i>false</i>, otherwise.
	 */
	public static boolean isEqual(RealF pE, RealF pF)
	{
		return 	   DivField.isTypeMatch(pE, pF) 
				&& pE.getReal() == pF.getReal();
		
	}

	/**
	 * This method checks to see if the value is infinite.
	 * @param pF
	 * 		RealF
	 * @return boolean
	 */
	public static boolean isInfinite(RealF pF)
	{
		return Float.isInfinite(pF.getReal());
	}

	
	
	
	/**
	 * This method checks to see if the value is not a number at all. NAN
	 * @param pF
	 * 		RealF
	 * @return boolean
	 */
	public static boolean isNaN(RealF pF)
	{
		return Float.isNaN(pF.getReal());
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * 
	 * @param pF
	 * 		RealF
	 * @return boolean
	 */
	public static boolean isZero(RealF pF)
	{
		return (pF.getReal() == 0.0F);
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
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealF
	 */
	public static RealF multiply(RealF pF1, RealF pF2) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2)
						&& !RealF.isInfinite(pF1) && !RealF.isInfinite(pF2))
			return new RealF(pF1.getFieldType(), pF1.getReal() * pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}

	/**
	 * Static one construction method
	 * 
	 * @param pS
	 * 		String
	 * 
	 * @return RealF
	 */
	public static RealF newONE(String pS)
	{
		return new RealF(new DivFieldType(pS), 1.0f);
	}
	
	/**
	 * Static zero construction method
	 * 
	 * @param pS
	 * 		String
	 * 
	 * @return RealF
	 */
	public static RealF newZERO(String pS)
	{
		return new RealF(new DivFieldType(pS), 0.0f);
	}

	/**
	 * Static subtract method that creates a new RealD with the difference pF1-pF2.
	 * 
	 * @param pF1
	 *            RealF
	 * @param pF2
	 *            RealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealF
	 */
	public static RealF subtract(RealF pF1, RealF pF2) throws FieldBinaryException
	{
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2)
						&& !RealF.isInfinite(pF1) && !RealF.isInfinite(pF2))
			return new RealF(pF1.getFieldType(), pF1.getReal() - pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Subtraction error found", pF2));
	}

	protected float[]	vals;
	
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
	 * 
	 * @param pT
	 * 		DivFieldType
	 */
	public RealF(DivFieldType pT)
	{
		vals = new float[1];
		setFieldType(pT);
		setReal(0.0F);
		
	}

	/**
	 * Basic Constructor with everything to initialize.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		float
	 */
	public RealF(DivFieldType pT, float pR)
	{
		vals = new float[1];
		setFieldType(pT);
		setReal(pR);
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Basic Constructor with only the number to initialize.
	 * 
	 * @param pR
	 * 		float
	 * 
	 * 
	 */
	public RealF(float pR)
	{
		vals = new float[1];
		setFieldType(new DivFieldType("Real"));
		setReal(pR);
		
	}

	/**
	 * Copy Constructor that reuses the field type reference.
	 * @param pR
	 * 			RealF
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
	 * @param pR
	 * 		RealF
	 * 
	 * 
	 * @param pF
	 * 		float
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
	 * @param pF
	 * 		DivFieldF
	 * 
	 * @throws FieldBinaryException
	 * 	This exception is thrown if there is a field mismatch
	 * @see com.interworldtransport.cladosF.DivisableF#add(com.interworldtransport.cladosF.DivisableF)
	 * 
	 * @return RealF
	 */
	@Override
	public RealF add(DivisableF pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealF.isNaN(this) && !RealF.isNaN((RealF) pF) 
				&& !RealF.isInfinite(this) && !RealF.isInfinite((RealF) pF))		
			throw (new FieldBinaryException(this, "Addition failed type match test", (DivField) pF));
		
		setReal(getReal() + ((RealF) pF).getReal());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return RealF
	 */
	@Override
	public RealF conjugate()
	{
		
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldF
	 * 
	 * @see com.interworldtransport.cladosF.DivisableF#divide(com.interworldtransport.cladosF.DivisableF)
	 * 
	 * @return RealF
	 */
	@Override
	public RealF divide(DivisableF pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealF.isNaN(this) && !RealF.isNaN((RealF) pF) 
				&& !RealF.isInfinite(this) && !RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Divide failed type match test", (DivField) pF));
		
		if (RealF.isZero((RealF) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (DivField) pF));
		
		
		
		
		setReal(getReal() / ((RealF) pF).getReal());
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
	 * 
	 * @throws FieldException
	 * 	This exception is thrown if someone tries to invert a ZERO.
	 * 
	 * @return RealF
	 */
	@Override
	public RealF invert() throws FieldException
	{
		if (RealF.isZero(this))
			throw new FieldException(this, "Can't invert a zero RealF");
		
		
		
		
		setReal(1.0F / getReal());
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldF
	 * 
	 * @see com.interworldtransport.cladosF.DivisableF#multiply(com.interworldtransport.cladosF.DivisableF)
	 * 
	 * @return RealF
	 */
	@Override
	public RealF multiply(DivisableF pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealF.isNaN(this) && !RealF.isNaN((RealF) pF) 
				&& !RealF.isInfinite(this) && !RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match test", (DivField) pF));
	
		
		setReal(getReal() * ((RealF) pF).getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		float
	 * 
	 * @return RealF
	 */
	@Override
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
	 * @param pF
	 *            DivFieldF 
	 *
	 * @see com.interworldtransport.cladosF.DivisableF#subtract(com.interworldtransport.cladosF.DivisableF)
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch.
	 * 
	 * @return RealF
	 */
	@Override
	public RealF subtract(DivisableF pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealF.isNaN(this) && !RealF.isNaN((RealF) pF) 
				&& !RealF.isInfinite(this) && !RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match test", (DivField) pF));
		
		
		setReal(getReal() - ((RealF) pF).getReal());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivisableF#toString()
	 * 
	 * @return String
	 */
	@Override
	public String toString()
	{
		return (getReal() + "FR");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivisableF#toXMLString()
	 * 
	 * @return String
	 */
	@Override
	public String toXMLString()
	{
		return ("<RealF type=\"" + getFieldTypeString() + "\" value=\""
						+ getReal() + "\" />");
	}
}