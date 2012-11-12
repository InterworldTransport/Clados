/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosF.DivField<br>
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
 * ---com.interworldtransport.cladosF.DivField<br>
 * ------------------------------
 * --------------------------------------------------
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
public abstract class DivField
{
	/**
	 * Object for the field type. A string used to be used here, but an object
	 * lets us reuse the object through a reference allowing all coefficients in
	 * the monads of a nyad to point to the same place.
	 */
	protected DivFieldType	FieldType;

	/**
	 * Set method for FieldType
	 * 
	 * @param pType
	 *            DivFieldType
	 */
	public void setFieldType(DivFieldType pType)
	{
		FieldType = pType;
	}

	/**
	 * Get method for FieldType
	 * 
	 * @return DivFieldType (A division field type)
	 */
	public DivFieldType getFieldType()
	{
		return FieldType;
	}

	/**
	 * Get method for FieldType
	 * 
	 * @return DivFieldType (A division field type)
	 */
	public String getFieldTypeString()
	{
		return FieldType.getType();
	}

	/**
	 * Check to see if the incoming argument is of the same field type as the
	 * current object.
	 * 
	 * @param pE
	 *            DivFieldD
	 * @param pF
	 *            DivFieldD
	 * @throws FieldBinaryException
	 */
	public static boolean isTypeMatch(DivField pE, DivField pF)
	{
		return pE.getFieldType().equals(pF.getFieldType());
	}

	/**
	 * Return a string representation of the field element. * @return String
	 */
	public abstract String toXMLString();

	/**
	 * Return a string representation of the real value
	 */
	public abstract String toString();

}
