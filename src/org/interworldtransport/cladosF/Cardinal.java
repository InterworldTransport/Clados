/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Cardinal<br>
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
 * ---org.interworldtransport.cladosF.Cardinal<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * This class is a simple string holder that names a division field.
 * Examples include Real, Complex, and Quaternion.
 * <p>
 * Cardinals are conveniences only. They are meant to provide all DivFields with 
 * a single object that names them in order to speed up field comparisons in 
 * TypeMatch methods. A class using fields for calculations would declare only 
 * one of these and then share the reference among all the objects.
 * <p>
 * This may seem like a waste of time, but it is useful when a class must be
 * prepared to use different kinds of fields without knowing in advance which
 * one will be created. It is most important when an object exists that uses
 * DivFields with unknown pedigrees.
 * <p>
 * One consequence of this approach is that two division fields might use
 * different objects to type them. The TypeMismatch method will state that they
 * are different, so this allows an application writer to keep two distinct
 * number system apart in their application even though the fields are internally
 * identical. This is useful when objects in one algebra might be scaled
 * different than objects in another.
 * <p>
 * Yes. This soft typing to an OOP developer. The point is that it allows for
 * differences between objects that can't assume 'scale' means the same thing
 * to both of them.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */

public final class Cardinal
{
	public static final Cardinal generate(String pT)
	{
		return new Cardinal(pT);
	}
	
	private String	type;

	private Cardinal(String pT)
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
	public String toXMLString()
	{
		return ("<Cardinal unit=\"" + type + "\" />\n");
	}
}
