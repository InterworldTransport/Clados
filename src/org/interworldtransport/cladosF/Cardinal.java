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
 * This class is a simple string holder that names a division field in a manner
 * similar to the concept of 'units' in a physical model. Examples... "metres",
 * "litres", "amperes". There is no requirement one use a particular standard
 * for unit systems, though. They are free strings.
 * <p>
 * Cardinals are meant to provide all DivFields instances with a single object
 * they may share that names them in order to speed field comparisons in
 * TypeMatch methods. A class using fields for calculations would declare only
 * one of these and then share the reference among all the objects that may be
 * operated upon by the two primary opertaions of the field. ( +, * )
 * <p>
 * This may seem like a waste of time, but it is useful when a class must be
 * prepared to use different kinds of fields without knowing in advance which
 * one will be created. It is most important when an object exists that uses
 * DivFields with unknown pedigrees. The Cardinal can be checked to discover
 * intended uses and avoid mixing apples and oranges.
 * <p>
 * One consequence of this approach is that two division fields might use
 * different Cardinal objects of the same name. The TypeMatch method in a
 * DivField will state that they are different because object equality is tested
 * instead of string content equality. This allows an application writer to keep
 * two distinct systems of numbers apart in their application even though the
 * fields are internally identical. This is useful when objects in one algebra
 * might be scaled different than objects in another. If one never intends to
 * use this feature, though, it is easy to avoid. Simply re-use a Cardinal.
 * <p>
 * Yes. This is soft typing to an OOP developer. The point is that it allows for
 * differences between objects that can't assume 'scale' means the same thing to
 * both of them. Since 'scale' and 'multiply' are NOT the same concepts, this
 * distinction is needed in some scenarios.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class Cardinal {
	/**
	 * When one does not wish to use Cardinals much, the best bypass is to use this
	 * factory method to create a minimal cardinal. The unit value will be set to
	 * the name of the CladosField offered
	 * 
	 * @param pT CladosField Name the element of the enumeration will be re-used as
	 *           the unit value within the Cardinal.
	 * @return Cardinal
	 */
	public static final Cardinal generate(CladosField pT) {
		return new Cardinal(pT);
	}

	/**
	 * When one does wish to use Cardinals, the best approach is to use this factory
	 * method to create a typical cardinal. The unit value is set to the string.
	 * 
	 * @param pT String The Cardinal's unit value will be set to this string.
	 * @return Cardinal
	 */
	public static final Cardinal generate(String pT) {
		return new Cardinal(pT);
	}

	private String unit;

	private Cardinal(CladosField pT) {
		unit = pT.name();
	}

	private Cardinal(String pT) {
		unit = pT;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cardinal other = (Cardinal) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}

	/**
	 * Simple gettor method for the name of the 'unit' represented by this Cardinal.
	 * 
	 * @return String This string names the 'unit type' represented by the Cardinal
	 */
	public String getUnit() {
		return unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	/**
	 * Simple settor method for the name of the 'unit' represented by this Cardinal.
	 * 
	 * @param pUnit String This string names the 'unit type' represented by the Cardinal
	 */
	public void setUnit(String pUnit) {
		unit = pUnit;
	}

	/**
	 * Similar to a toString() mehtod, but it focuses upon an XML style output.
	 * 
	 * @return String XML compatible sub-unit for code relying on exportable Cardinals.
	 */
	public String toXMLString() {
		return ("<Cardinal unit=\"" + unit + "\" />\n");
	}
}
