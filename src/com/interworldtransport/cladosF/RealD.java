/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.RealD<br>
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
 * ---com.interworldtransport.cladosF.RealD<br>
 * ------------------------------------------------------------------------ <br>
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
 * object or any of the other descendants of DivFieldD.
 * <p>
 * Applications requiring speed should use the monads and nyads that implement
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Double and implement an interface called
 * DivFieldD. That can't be done, though, because Double is final.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class RealD extends DivField implements DivisableD
{
	/**
	 * Static add method that creates a new RealD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return RealD
	 */
	public static RealD add(RealD pF1, RealD pF2) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2)
						&& !RealD.isInfinite(pF1) && !RealD.isInfinite(pF2))
			return new RealD(pF1.getFieldType(), pF1.getReal() + pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Addition error found",	pF2));
	}
	
	/**
	 * Static method that creates a new RealD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            RealD
	 * @return RealD
	 */
	public static RealD conjugate(RealD pF)
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
	 * @return RealD
	 */
	//public static RealD copyAsZero(RealD pF)
	//{
	//	return new RealD(pF.getFieldType());
	//}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list. Because these are real numbers,
	 * though, we get away with simply summing the moduli instead. It does not
	 * perform a field type safety check and will throw the exception if that
	 * test fails.
	 * 
	 * @param pL
	 * 		RealD[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception is thrown when sqMagnitude fails with the RealF array
	 * @return RealD
	 */
	public static RealD copyFromModuliSum(RealD[] pL) throws FieldBinaryException
	{
		RealD tR = RealD.copyONE(pL[0]).scale(pL[0].getModulus());
		for (int j = 1; j < pL.length; j++)
			if (isTypeMatch(pL[j], tR))
				tR.add(RealD.copyONE(pL[j].scale(pL[j].getModulus())));
			else
				throw new FieldBinaryException(pL[j], "Field Type mistach during addition", tR);
		return tR;
	}
	
	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @param pL
	 * 		RealD[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch. It should never happen
	 * 	but the implementation uses multiplication, thus it is technically possible.
	 * 
	 * @return RealD
	 */
	public static RealD copyFromSQModuliSum(RealD[] pL) throws FieldBinaryException
	{
		RealD tR = RealD.copyONE(pL[0]).scale(pL[0].getSQModulus());
		
		for (int j = 1; j < pL.length; j++)
			if (isTypeMatch(pL[j], tR))
				tR.add(RealD.copyONE(pL[j].scale(pL[j].getSQModulus())));
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
	 *            RealD
	 * @return RealD
	 */
	public static RealD copyOf(RealD pF)
	{
		return new RealD(pF);
	}
	
	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 		RealD
	 * 
	 * @return RealD
	 */
	public static RealD copyONE(RealD pR)
	{
		return new RealD(pR.getFieldType(), 1.0D);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 		RealD
	 * 
	 * @return RealD
	 */
	public static RealD copyZERO(RealD pR)
	{
		return new RealD(pR.getFieldType(), 0.0D);
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy does not reuse a field type reference so it is likely to fail type
	 * mismatch tests.
	 * 
	 * @param pR
	 *            double
	 * 
	 * 
	 * @return RealD
	 */
	public static RealD create(double pR)
	{
		return new RealD(pR);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Static divide method that creates a new RealD with the product pF1 / pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealD
	 */
	public static RealD divide(RealD pF1, RealD pF2)
					throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isZero(pF2)
						&& !RealD.isNaN(pF1) && !RealD.isNaN(pF2)
						&& !RealD.isInfinite(pF1) && !RealD.isInfinite(pF2))
			return new RealD(pF1.getFieldType(), pF1.getReal() / pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Division error found",	pF2));
		
		
		
		
		
		
		
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
	 * @return boolean <i>true</i> if both components are the same;
	 *         <i>false</i>, otherwise.
	 */
	public static boolean isEqual(RealD pE, RealD pF)
	{
		return 	   DivField.isTypeMatch(pE, pF) 
				&& (pE.getReal() == pF.getReal());
		
	}

	/**
	 * This method checks to see if the value is infinite.
	 * @param pF
	 * 		RealD
	 * @return boolean
	 */
	public static boolean isInfinite(RealD pF)
	{
		return Double.isInfinite(pF.getReal());
	}
	
	
	
	
	/**
	 * This method checks to see if the value is not a number at all. NAN
	 * @param pF
	 * 		RealD
	 * @return boolean
	 */
	public static boolean isNaN(RealD pF)
	{
		return Double.isNaN(pF.getReal());
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * 
	 * @param pF
	 * 		RealD
	 * @return boolean
	 */
	public static boolean isZero(RealD pF)
	{
		return (pF.getReal() == 0.0D);
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
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealD
	 */
	public static RealD multiply(RealD pF1, RealD pF2) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2)
						&& !RealD.isInfinite(pF1) && !RealD.isInfinite(pF2))
			return new RealD(pF1.getFieldType(), pF1.getReal() * pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}
	
	/**
	 * Static one construction method
	 * 
	 * @param pS
	 * 		String
	 * 
	 * @return RealD
	 */
	public static RealD newONE(String pS)
	{
		return new RealD(new DivFieldType(pS), 1.0D);
	}
	
	/**
	 * Static zero construction method
	 * 
	 * @param pS
	 * 		String
	 * 
	 * @return RealD
	 */
	public static RealD newZERO(String pS)
	{
		return new RealD(new DivFieldType(pS), 0.0D);
	}
	
	/**
	 * Static subtract method that creates a new RealD with the difference pF1-pF2.
	 * 
	 * @param pF1
	 *            RealD
	 * @param pF2
	 *            RealD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch.
	 * @return RealD
	 */
	public static RealD subtract(RealD pF1, RealD pF2) throws FieldBinaryException
	{
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2)
						&& !RealD.isInfinite(pF1) && !RealD.isInfinite(pF2))
			return new RealD(pF1.getFieldType(), pF1.getReal() - pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Subtraction error found", pF2));
	}
	
	protected double[]	vals;
	
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
	 * 
	 * @param pT
	 * 		DivFieldType
	 */
	public RealD(DivFieldType pT)
	{
		vals = new double[1];
		setFieldType(pT);
		setReal(0.0D);
		
	}

	/**
	 * Basic Constructor with everything to initialize.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		double
	 */
	public RealD(DivFieldType pT, double pR)
	{
		vals = new double[1];
		setFieldType(pT);
		setReal(pR);
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Basic Constructor with only the number to initialize.
	 * 
	 * @param pR
	 * 		double
	 * 
	 * 
	 */
	public RealD(double pR)
	{
		vals = new double[1];
		setFieldType(new DivFieldType("Real"));
		setReal(pR);
		
	}
	
	/**
	 * Copy Constructor that reuses the field type reference.
	 * @param pR
	 * 			RealD
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
	 * @param pR
	 * 		RealD
	 * 
	 * 
	 * @param pD
	 * 		float
	 */
	public RealD(RealD pR, double pD)
	{
		vals = new double[1];
		setFieldType(pR.getFieldType());
		setReal(pD);
		
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldD
	 * 
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch.
	 * @see com.interworldtransport.cladosF.DivisableD#add(com.interworldtransport.cladosF.DivisableD)
	 * 
	 * @return RealD
	 */
	@Override
	public RealD add(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF) 
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Addition failed type match test", (DivField) pF));
		
		setReal(getReal() + ((RealD) pF).getReal());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return RealD
	 */
	@Override
	public RealD conjugate()
	{
		
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldD
	 * 
	 * @see com.interworldtransport.cladosF.DivisableD#divide(com.interworldtransport.cladosF.DivisableD)
	 * 
	 * @return RealD
	 */
	@Override
	public RealD divide(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF) 
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Divide failed type match test", (DivField) pF));
		
		if (RealD.isZero((RealD) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (DivField) pF));
		
		
		
		
		setReal(getReal() / ((RealD) pF).getReal());
		return this;
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
	 * Get the real numeric value from the value array
	 * 
	 * @return double
	 */
	public double getReal()
	{
		return vals[0];
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
	 * This method inverts real numbers.
	 * 
	 * @throws FieldException
	 * 	This exception is thrown if someone tries to invert a ZERO.
	 * 
	 * @return RealD
	 */
	@Override
	public RealD invert() throws FieldException
	{
		if (RealD.isZero(this))
			throw new FieldException(this, "Can't invert a zero RealD");
		
		
		
		
		setReal(1.0D / getReal());	
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldD
	 * 
	 * @see com.interworldtransport.cladosF.DivisableD#multiply(com.interworldtransport.cladosF.DivisableD)
	 * 
	 * @return RealD
	 */
	@Override
	public RealD multiply(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF) 
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match test", (DivField) pF));
		
		
		setReal(getReal() * ((RealD) pF).getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		double
	 * 
	 * @return RealD
	 */
	@Override
	public RealD scale(double pS)
	{
		setReal(pS * getReal());
		
		
		
		
		
		
		
		
		
		
		return this;
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
	 * This method subtracts real numbers and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 *            DivFieldD
	 * 
	 * @see com.interworldtransport.cladosF.DivisableD#subtract(com.interworldtransport.cladosF.DivisableD)
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch.
	 * 
	 * @return RealD
	 */
	@Override
	public RealD subtract(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF) 
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match test", (DivField) pF));
		
		
		setReal(getReal() - ((RealD) pF).getReal());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivisableD#toString()
	 * 
	 * @return String
	 */
	@Override
	public String toString()
	{
		return (getReal() + "DR");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @see com.interworldtransport.cladosF.DivisableD#toXMLString()
	 * 
	 * @return String
	 */
	@Override
	public String toXMLString()
	{
		return ("<RealD type=\"" + getFieldTypeString() + "\" value=\""
						+ getReal() + "\" />");
	}
}