/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.DivField<br>
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
 * ---com.interworldtransport.cladosF.DivField<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

/**
 * This class implements the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All CladosObjects use DivFields as a result.
 * <p>
 * FivFields are not named. They do not have any geometric properties. Treat
 * them like you would any other number you could plug into a simple calculator.
 * <p>
 * The number to be plugged in, though, doesn't appear until later in a child
 * of this class. This matters because the number of reals involved varies.
 * Besides... this class is abstract.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public abstract class DivField
{
	/**
	 * Check to see if the incoming argument is of the same field type as the
	 * current object.
	 * 
	 * @param pE
	 *            DivField
	 * @param pF
	 *            DivField
	 * @return boolean
	 */
	public static boolean isTypeMatch(DivField pE, DivField pF)
	{
		return pE.getFieldType().equals(pF.getFieldType());
	}

	/**
	 * Object for the field type. A string used to be used here, but an object
	 * lets us reuse the object through a reference allowing all coefficients in
	 * the monads of a nyad to point to the same place.
	 */
	protected DivFieldType	FieldType;

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
	 * Return a string representation of the real value
	 * @return String
	 */
	public abstract String toString();

	/**
	 * Return a string representation of the field element. 
	 * @return String
	 */
	public abstract String toXMLString();

}
