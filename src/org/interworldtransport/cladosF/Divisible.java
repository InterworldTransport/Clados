/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Divisible<br>
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
 * ---org.interworldtransport.cladosF.Divisible<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import org.interworldtransport.cladosFExceptions.*;

/**
 * This interface implements the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All CladosObjects use DivField's as a result.
 * <p>
 * Divisible's are not named. They do not have any geometric properties. Treat
 * them as the behaviors you would expect of a simple calculator.
 * <p>
 * The number array to be plugged in appears elsewhere.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public interface Divisible 
{
	/**
	 * This is the self-altering add method. The incoming variable is added to
	 * this object and this object changes.
	 * 
	 * @param pF
	 *            Divisible
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return Divisible
	 */
	public abstract Divisible add(Divisible pF) throws FieldBinaryException;
	
	/**
	 * This is the self-altering conjugate method. This object changes when all
	 * of its imaginary members are set to their additive inverses.
	 * 
	 * @return Divisible
	 */
	public abstract Divisible conjugate();

	/**
	 * This is the self-altering divide method. The incoming variable is divided
	 * into this object (making the parameter the denominator) and this object
	 * changes.
	 * 
	 * @param pF
	 *            Divisible
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return Divisible
	 */
	public abstract Divisible divide(Divisible pF) throws FieldBinaryException;
	
	/**
	 * This method returns multiplicative inverses.
	 * 
	 * @throws FieldException
	 * 	This exception is thrown if someone tries to invert a ZERO.
	 * 
	 * @return Divisible
	 */
	public abstract Divisible invert() throws FieldException;

	/**
	 * This is the self-altering multiply method. The incoming variable is
	 * multiplied against this object and this object changes.
	 * 
	 * @param pF
	 *            Divisible
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return Divisible
	 */
	public abstract Divisible multiply(Divisible pF) throws FieldBinaryException;

	/**
	 * This is the self-altering subtract method. The incoming variable is
	 * subtracted from this object and this object changes.
	 * 
	 * @param pF
	 *            Divisible
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the fields fail a match test.
	 * @return Divisible
	 */
	public abstract Divisible subtract(Divisible pF) throws FieldBinaryException;

	/**
	 * Return a string representation of the real value
	 */
	public abstract String toString();

	/**
	 * Return a string representation of the field element. 
	 * @return String
	 * Flat string representation of this Divisible float(s) field
	 */
	public abstract String toXMLString();
}