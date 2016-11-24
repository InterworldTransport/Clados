/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.DivFieldF<br>
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
 * ---com.interworldtransport.cladosF.DivFieldF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All CladosObjects use FieldElements as a result.
 * <p>
 * FieldElements are not named. They do not have any geometric properties. Treat
 * them like you would any other number you could plug into a simple calculator.
 * <p>
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 * 
 */
public abstract class DivFieldF extends DivField
{
	protected float[]	vals;

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * 
	 * @return float
	 */
	public abstract float getModulus();

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense
	 * to calculate this before the modulus itself.
	 * 
	 * @return float
	 */
	public abstract float getSQModulus();

	/**
	 * This is the self-altering add method. The incoming variable is added to
	 * this object and this object changes.
	 * 
	 * @param pF
	 *            DivFieldD
	 * @throws FieldBinaryException
	 */
	public abstract DivFieldF add(DivFieldF pF) throws FieldBinaryException;

	/**
	 * This is the self-altering subtract method. The incoming variable is
	 * subtracted from this object and this object changes.
	 * 
	 * @param pF
	 *            DivFieldD
	 * @throws FieldBinaryException
	 */
	public abstract DivFieldF subtract(DivFieldF pF)
					throws FieldBinaryException;

	/**
	 * This is the self-altering multiply method. The incoming variable is
	 * multiplied against this object and this object changes.
	 * 
	 * @param pF
	 *            DivFieldD
	 * @throws FieldBinaryException
	 */
	public abstract DivFieldF multiply(DivFieldF pF)
					throws FieldBinaryException;

	/**
	 * This is the self-altering divide method. The incoming variable is divided
	 * into this object (making the parameter the denominator) and this object
	 * changes.
	 * 
	 * @param pF
	 *            DivFieldD
	 * @throws FieldBinaryException
	 */
	public abstract DivFieldF divide(DivFieldF pF) throws FieldBinaryException;

	/**
	 * Return a string representation of the field element. * @return String
	 */
	public abstract String toXMLString();

	/**
	 * Return a string representation of the real value
	 */
	public abstract String toString();

}
