/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.DivisableD<br>
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
 * ---com.interworldtransport.cladosF.DivisableD<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

/**
 * This interface implements the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All CladosObjects use DivField's as a result.
 * <p>
 * DivisableD's are not named. They do not have any geometric properties. Treat
 * them as the behaviors you would expect of a simple calculator.
 * <p>
 * The number array to be plugged in appears elsewhere.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public interface DivisableD
{
	/**
	 * This is the self-altering add method. The incoming variable is added to
	 * this object and this object changes.
	 * 
	 * @param pF
	 *            DivisableD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return DivisableD
	 */
	public abstract DivisableD add(DivisableD pF) throws FieldBinaryException;
	
	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return DivisableD
	 */
	public DivisableD conjugate();

	/**
	 * This is the self-altering divide method. The incoming variable is divided
	 * into this object (making the parameter the denominator) and this object
	 * changes.
	 * 
	 * @param pF
	 *            DivisableD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return DivisableD
	 */
	public abstract DivisableD divide(DivisableD pF) throws FieldBinaryException;

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * 
	 * @return double
	 */
	public abstract double getModulus();

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense
	 * to calculate this before the modulus itself.
	 * 
	 * @return double
	 */
	public abstract double getSQModulus();
	
	/**
	 * This method returns multiplicative inverses.
	 * 
	 * @throws FieldException
	 * 	This exception is thrown if someone tries to invert a ZERO.
	 * 
	 * @return DivisableD
	 */
	public DivisableD invert() throws FieldException;

	/**
	 * This is the self-altering multiply method. The incoming variable is
	 * multiplied against this object and this object changes.
	 * 
	 * @param pF
	 *            DivisableD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return DivisableD
	 */
	public abstract DivisableD multiply(DivisableD pF)
					throws FieldBinaryException;

	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		double
	 * 
	 * @return DivisableD
	 */
	public DivisableD scale(double pS);
	
	/**
	 * This is the self-altering subtract method. The incoming variable is
	 * subtracted from this object and this object changes.
	 * 
	 * @param pF
	 *            DivisableD
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return DivisableD
	 */
	public abstract DivisableD subtract(DivisableD pF)
					throws FieldBinaryException;

	/**
	 * Return a string representation of the real value
	 */
	public abstract String toString();

	/**
	 * Return a string representation of the field element. * @return String
	 */
	public abstract String toXMLString();
}