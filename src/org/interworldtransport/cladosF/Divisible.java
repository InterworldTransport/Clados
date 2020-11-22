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
 * This interface establishes the concept of the primary operations of a
 * Division Field from mathematics. Field objects within the clados packages are
 * used as 'numbers' in the definition of an algebra. All Clados objects use
 * DivField's as a result.
 * <p>
 * Divisible's are not named. They do not have any geometric properties. Treat
 * them as the behaviors you would expect in a simple calculator.
 * <p>
 * The "Number" to be computed appears elsewhere.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public interface Divisible {
	/**
	 * This is the self-altering add method. The incoming variable is added to this
	 * object and this object changes.
	 * 
	 * @param pF Divisible
	 * @throws FieldBinaryException This exception is thrown when the fields fail a
	 *                              match test.
	 * @return Divisible
	 */
	public abstract Divisible add(Divisible pF) throws FieldBinaryException;

	/**
	 * This is the self-altering conjugate method. This object changes when all of
	 * its imaginary members are set to their additive inverses.
	 * 
	 * @return Divisible
	 */
	public abstract Divisible conjugate();

	/**
	 * This is the self-altering divide method. The incoming variable is divided
	 * into this object (making the parameter the denominator) and this object
	 * changes.
	 * 
	 * @param pF Divisible
	 * @throws FieldBinaryException This exception is thrown when the fields fail a
	 *                              match test.
	 * @return Divisible
	 */
	public abstract Divisible divide(Divisible pF) throws FieldBinaryException;

	/**
	 * This method returns multiplicative inverses. A field is a division algebra IF
	 * such an operation is viable for all values other than the multiplicative
	 * ZERO, so this method establishes a big part of the contract for a division
	 * algebra's behaviors.
	 * 
	 * @throws FieldException This is thrown if one attempts inversion of ZERO.
	 * 
	 * @return Divisible
	 */
	public abstract Divisible invert() throws FieldException;

	/**
	 * This is the self-altering multiply method. The incoming variable is
	 * multiplied against this object and this object changes.
	 * 
	 * @param pF Divisible
	 * @throws FieldBinaryException This exception is thrown when the fields fail a
	 *                              match test.
	 * @return Divisible
	 */
	public abstract Divisible multiply(Divisible pF) throws FieldBinaryException;

	/**
	 * Scale method scales the modulus of a DivField by a Number. Divisible multiply
	 * operation is avoided.
	 * 
	 * For real numbers this is scaling the distance from a number line origin. For
	 * complex numbers, one scales the modulus of the number which is radius in a
	 * polar representation.
	 * 
	 * This method will appear peculiar to the uninitiated. Why not just use the
	 * multiply operation they might ask. One certainly could, but in doing so one
	 * risks tripping an exception for Cardinal matching. It will often be the case
	 * that the exception makes no sense, but because it can be thrown in some cases
	 * it has to be checked in all cases. For example, scaling by "-1" and
	 * multiplying by "-1" are not the same if one is tracking the units of "-1".
	 * That's what a Cardinal is FOR in DivField subclasses which implement this
	 * interface, thus we should make the distinction between scaling by "-1" and
	 * multiplying by "-1 with a cardinal". We do so by establishing "scale" in the
	 * contract implied by this method. A "scalable" object is one that can be
	 * altered using a Number of some kind WITHOUT a cardinal check. Why not just
	 * ditch the Cardinal instead? Check the documentation for Cardinal to see why.
	 * 
	 * One very important distinction is that cladosF DivField children are scaled
	 * by Numbers from java.lang. Essentially the boxed promitives that look like
	 * numbers will do. Byte, Short, Integer, and Long along with Float & Double.
	 * Geometry in cladosG is scaled by CladosF DivField children and NOT Numbers.
	 * Why? In order to bring Cardinal into a physical model containing geometry.
	 * 
	 * @param pS Number
	 * @return Divisable
	 */
	public abstract Divisible scale(Number pN); // Note | No throwing of FieldBinaryException

	/**
	 * This is the self-altering subtract method. The incoming variable is
	 * subtracted from this object and this object changes.
	 * 
	 * @param pF Divisible
	 * @throws FieldBinaryException This exception is thrown when the fields fail a
	 *                              match test.
	 * @return Divisible
	 */
	public abstract Divisible subtract(Divisible pF) throws FieldBinaryException;

}