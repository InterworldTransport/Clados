/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.DivFieldType<br>
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
 * ---com.interworldtransport.cladosF.DivFieldType<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

/**
 * This class is a simple string holder that names a division field type.
 * Examples include Real, Complex, and Quaternion.
 * <p>
 * Field types are conveniences only. They are meant to provide all
 * DivFieldElements with a single object that names the field type in order to
 * speed up field type comparisons in TypeMatch methods. A class using fields
 * for calculations would declare only one of these and then share the reference
 * among all the objects.
 * <p>
 * This may seem like a waste of time, but it is useful when a class must be
 * prepared to use different kinds of fields without knowing in advance which
 * one will be created. It is most important when an object exists that uses
 * field elements with an unknown pedigree.
 * <p>
 * One consequence of this approach is that two division fields might use
 * different objects to type them. The TypeMismatch method will state that they
 * are different, so this allows an application writer to keep two distinct
 * number system apart in their application even those the fields are internally
 * identical. This is useful when objects in one algebra might be scaled
 * different than objects in another.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */

public final class DivFieldType
{
	public static final DivFieldType generate(String pT)
	{
		return new DivFieldType(pT);
	}
	
	private String	type;

	public DivFieldType(String pT)
	{
		type = pT;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String pFT)
	{
		type = pFT;
	}

}
