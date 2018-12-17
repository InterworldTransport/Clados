/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.ComplexD<br>
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
 * ---com.interworldtransport.cladosF.ComplexD<br>
 * ------------------------------------------------------------------------ <br>
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
public class ComplexD extends DivField implements DivisableD
{
	/**
	 * Static add method that creates a new ComplexD with the sum pF1 + pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return ComplexD
	 */
	public static ComplexD add(ComplexD pF1, ComplexD pF2) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2)
						&& !ComplexD.isInfinite(pF1) && !ComplexD.isInfinite(pF2))
			return new ComplexD(pF1.getFieldType(), pF1.getReal() + pF2.getReal());
		
		throw (new FieldBinaryException(pF1, "Static Addition error found",	pF2));
	}
	
	/**
	 * Static method that creates a new ComplexD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * 
	 * @param pF
	 *            ComplexD
	 * @return ComplexD
	 */
	public static ComplexD conjugate(ComplexD pF)
	{
		return new ComplexD(pF.getFieldType(), pF.getReal(), -1.0D * pF.getImg());
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the field type
	 * but not the value. Since this copy reuses the field type reference it
	 * will pass a type match test with pF but not the isEqual test.
	 * 
	 * @param pF
	 *            ComplexD
	 * @return ComplexD
	 */
	//public static ComplexD copyAsZero(ComplexD pF)
	//{
	//	return new ComplexD(pF.getFieldType());
	//}

	/**
	 * This static method takes a list of ComplexD objects and returns one
	 * ComplexD that has a real value that is equal to the square root of the
	 * sum of the SQModulus of each entry on the list. <p>
	 * It does not perform a field type safety check and will throw the exception 
	 * if that test fails.
	 * 
	 * @param pL
	 * 			ComplexD[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception happens when there is a field mismatch. It shouldn't happen
	 * 	but it is technically possible because of the dependence on sqMagnitude.	 * 
	 * @return ComplexD
	 */
	public static ComplexD copyFromModuliSum(ComplexD[] pL) throws FieldBinaryException
	{
		ComplexD tR = ComplexD.copyFromSQModuliSum(pL);
		// now figure out how to do the SQRT of this complex object.
		double tM = Math.sqrt(tR.getModulus());
		double tA = tR.getArgument() / 2;
		tR.setReal(tM * Math.cos(tA));
		tR.setImg(tM * Math.sin(tA));
		return tR;
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD
	 * that has a value that is equal to the sum of the SQModulus of each entry
	 * on the list. It does not perform a field type safety check and will throw
	 * the exception if that test fails.
	 * 
	 * @param pL
	 * 			ComplexD[]
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch. It should never happen
	 * 	but the implementation uses multiplication, thus it is technically possible.
	 * 
	 * @return ComplexD
	 */
	public static ComplexD copyFromSQModuliSum(ComplexD[] pL) throws FieldBinaryException
	{
		ComplexD tR = ComplexD.copyONE(pL[0]).scale(pL[0].getSQModulus());

		for (int j = 1; j < pL.length; j++)
			if (isTypeMatch(pL[j], tR))
				tR.add(ComplexD.copyONE(pL[j]).scale(pL[j].getSQModulus()));
			else
				throw new FieldBinaryException(pL[j], "Field Type mistach during addition", tR);
		
		return tR;
	}
	
	/**
	 * Static method that creates a new ComplexD with a copy of the parameter.
	 * This copy reuses the field type reference to ensure it will pass a type
	 * match test.
	 * 
	 * @param pF
	 *            ComplexD
	 * @return ComplexD
	 */
	public static ComplexD copyOf(ComplexD pF)
	{
		return new ComplexD(pF);
	}

	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 			ComplexD
	 * 
	 * @return ComplexD
	 */
	public static ComplexD copyONE(ComplexD pR)
	{
		return new ComplexD(pR.getFieldType(), 1.0d, 0.0d);
	}

	/**
	 * Static zero construction method with copied field type
	 * 
	 * @param pR
	 * 			ComplexD
	 * 
	 * @return ComplexD
	 */
	public static ComplexD copyZERO(ComplexD pR)
	{
		return new ComplexD(pR.getFieldType(), 0.0d, 0.0d);
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
	 * @return ComplexD
	 */
	public static ComplexD create(double pR, double pI)
	{
		return new ComplexD(pR, pI);
	}

	/**
	 * Static divide method that creates a new ComplexD with the product pF1 / pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when there is a field mismatch
	 * @return ComplexD
	 */
	public static ComplexD divide(ComplexD pF1, ComplexD pF2)
					throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) && !ComplexD.isZero(pF2)
				&& !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2)
				&& !ComplexD.isInfinite(pF1) && !ComplexD.isInfinite(pF2))
		{
			ComplexD tZ = new ComplexD(pF1);
			pF2.conjugate();
			tZ.multiply(pF2);
			pF2.conjugate();
			tZ.scale(1.0D / pF2.getSQModulus());
			return tZ;
		}
		throw (new FieldBinaryException(pF1, "Static Division error found",	pF2));
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
	 * @return boolean <i>true</i> if both components are the same;
	 *         <i>false</i>, otherwise.
	 */
	public static boolean isEqual(ComplexD pE, ComplexD pF)
	{
		return 	   DivField.isTypeMatch(pE, pF) 
				&& (pE.getReal() == pF.getReal())
				&& (pE.getImg() == pF.getImg());
	}

	/**
	 * Returns true if the real part is zero and imaginary part is not zero.
	 * @param pF 
	 * 			ComplexD
	 * @return boolean
	 */
	public static boolean isImaginary(ComplexD pF)
	{
		return (pF.getReal() == 0.0d && pF.getImg() != 0.0d);
	}
	
	/**
	 * This method checks to see if either value is infinite.
	 * 
	 * @param pF 
	 * 			ComplexDF
	 * @return boolean
	 */
	public static boolean isInfinite(ComplexD pF)
	{
		return Double.isInfinite(pF.getReal()) || Double.isInfinite(pF.getImg());
	}
	
	/**
	 * This method checks to see if either value is not a number at all. NAN
	 * 
	 * @param pF 
	 * 			ComplexD
	 * @return boolean
	 */
	public static boolean isNaN(ComplexD pF)
	{
		return Double.isNaN(pF.getReal()) || Double.isNaN(pF.getImg());
	}

	/**
	 * Returns true if the imaginary part is zero
	 * 
	 * @param pF 
	 * 			ComplexD
	 * @return boolean
	 */
	public static boolean isReal(ComplexD pF)
	{
		return (pF.getImg() == 0.0d);
	}
	
	/**
	 * This method checks to see if the number is exactly zero.
	 * 
	 * @param pF 
	 * 			ComplexD
	 * @return boolean
	 */
	public static boolean isZero(ComplexD pF)
	{
		return (pF.getReal() == 0.0D && pF.getImg() == 0.0D);
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
	 * 	This exception happens when there is a field mismatch.
	 * @return complexD
	 */
	public static ComplexD multiply(ComplexD pF1, ComplexD pF2) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2) 
				&& !ComplexD.isInfinite(pF1) && !ComplexD.isInfinite(pF2))
		{
			double tempR = pF1.getReal() * pF2.getReal() - pF1.getImg() * pF2.getImg();
			double tempI = pF1.getReal() * pF2.getImg() + pF1.getImg() * pF2.getReal();
			return new ComplexD(pF1.getFieldType(), tempR, tempI);
		}
		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}
	
	/**
	 * Static one construction method
	 * 
	 * @param pS
	 * 			String
	 * 
	 * @return ComplexD
	 */
	public static ComplexD newONE(String pS)
	{
		return new ComplexD(new DivFieldType(pS), 1.0d, 0.0d);
	}

	/**
	 * Static zero construction method
	 * 
	 * @param pS
	 * 			String
	 * 
	 * @return ComplexD
	 */
	public static ComplexD newZERO(String pS)
	{
		return new ComplexD(new DivFieldType(pS), 0.0D, 0.0D);
	}
	
	/**
	 * Static subtract method that creates a new ComplexD with the difference pF1-pF2.
	 * 
	 * @param pF1
	 *            ComplexD
	 * @param pF2
	 *            ComplexD
	 * @throws FieldBinaryException
	 * 	This exception occurs when there is a field mismatch.
	 * @return ComplexD
	 */
	public static ComplexD subtract(ComplexD pF1, ComplexD pF2) throws FieldBinaryException
	{
		if (ComplexD.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2) 
						&& !ComplexD.isInfinite(pF1) && !ComplexD.isInfinite(pF2))
			return new ComplexD(pF1.getFieldType(), pF1.getReal() - pF2.getReal(), pF1.getImg() - pF2.getImg());
		
		throw (new FieldBinaryException(pF1, "Static Subtraction error found", pF2));
	}
	
	protected double[]	vals;
	
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
	 * Copy Constructor that reuses the field type reference.
	 * 
	 * @param pC
	 * 		ComplexD
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
	 * @param pC
	 * 		ComplexD
	 * @param pR
	 * 		double
	 * @param pI
	 * 		double
	 */
	public ComplexD(ComplexD pC, double pR, double pI)
	{
		vals = new double[2];
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
	public ComplexD(DivFieldType pT)
	{
		vals = new double[2];
		setFieldType(pT);
		setReal(0.0D);
		setImg(0.0D);
	}
	
	/**
	 * Basic Constructor with everything to initialize but the imaginary.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		double
	 */
	public ComplexD(DivFieldType pT, double pR)
	{
		vals = new double[2];
		setFieldType(pT);
		setReal(pR);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize it.
	 * 
	 * @param pT
	 * 		DivFieldType
	 * @param pR
	 * 		double
	 * @param pI
	 * 		double
	 */
	public ComplexD(DivFieldType pT, double pR, double pI)
	{
		vals = new double[2];
		setFieldType(pT);
		setReal(pR);
		setImg(pI);
	}
	
	/**
	 * Basic Constructor with only the number to initialize.
	 * 
	 * @param pR
	 * 		double
	 * @param pI
	 * 		double
	 */
	public ComplexD(double pR, double pI)
	{
		vals = new double[2];
		setFieldType(new DivFieldType("Complex"));
		setReal(pR);
		setImg(pI);
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @param pF
	 * 		DivFieldD
	 * 
	 * @throws FieldBinaryException
	 * 	This exception occurs when a field mismatch happens
	 * @see com.interworldtransport.cladosF.DivisableD#add(com.interworldtransport.cladosF.DivisableD)
	 * 
	 * @return ComplexF
	 */
	@Override
	public ComplexD add(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !ComplexD.isNaN(this) && !ComplexD.isNaN((ComplexD) pF)
				&& !ComplexD.isInfinite(this) && !ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Addition failed type match test", (DivField) pF));
		setReal(getReal() + ((ComplexD) pF).getReal());
		setImg(getImg() + ((ComplexD) pF).getImg());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD conjugate()
	{
		setImg(-1.0D * getImg());
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
	 * @return ComplexD
	 */
	@Override
	public ComplexD divide(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !ComplexD.isNaN(this) && !ComplexD.isNaN((ComplexD) pF)
				&& !ComplexD.isInfinite(this) && !ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Divide failed type match test", (DivField) pF));
		if (ComplexD.isZero((ComplexD) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (DivField) pF));
		
		ComplexD tempZ = (ComplexD) pF;
		tempZ.conjugate();
		multiply(tempZ);
		tempZ.conjugate();
		scale(1.0D / tempZ.getSQModulus());
		return this;
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
		
		return (getImg() >= 0) ? Math.PI / 2.0D : 3.0D * Math.PI / 2.0D;
	}

	/**
	 * Get the imaginary numeric value from the value array
	 * 
	 * @return double
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
	 * 	This exception is thrown when someone tries to invert ZERO.
	 * 
	 * @return ComplexD 
	 */
	@Override
	public ComplexD invert() throws FieldException
	{
		if (ComplexD.isZero(this))
			throw new FieldException(this, "Can't invert a zero ComplexD");
		
		double tM = 1.0 / getModulus();
		double tA = -1.0 * getArgument();
		setReal(tM * Math.cos(tA));
		setImg(tM * Math.sin(tA));
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
	 * @return ComplexD
	 */
	@Override
	public ComplexD multiply(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !ComplexD.isNaN(this) && !ComplexD.isNaN((ComplexD) pF)
				&& !ComplexD.isInfinite(this) && !ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match test", (DivField) pF));
		ComplexD tempZ = (ComplexD) pF;
		setReal(getReal() * tempZ.getReal() - getImg() * tempZ.getImg());
		setImg(getReal() * tempZ.getImg() + getImg() * tempZ.getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		double
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD scale(double pS)
	{
		double tempS = Math.sqrt(Math.abs(pS));
		setReal(Math.signum(pS) * tempS * getReal());
		setImg(Math.signum(pS) * tempS * getImg());
		return this;
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
	 * @return ComplexD
	 */
	@Override
	public ComplexD subtract(DivisableD pF) throws FieldBinaryException
	{
		if (!DivField.isTypeMatch(this, (DivField) pF) && !ComplexD.isNaN(this) && !ComplexD.isNaN((ComplexD) pF)
				&& !ComplexD.isInfinite(this) && !ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match test", (DivField) pF));
		ComplexD tempZ = (ComplexD) pF;
		setReal(getReal() - tempZ.getReal());
		setImg(getImg() - tempZ.getImg());
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
		return (getReal() + "DR, " + getImg() + "DI");
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
		return ("<ComplexD type=\"" + getFieldTypeString() + "\" realvalue=\""
						+ getReal() + "\" imgvalue=\"" + getImg() + "\"/>");
	}
}