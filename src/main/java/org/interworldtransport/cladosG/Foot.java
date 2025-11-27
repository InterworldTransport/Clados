/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Foot<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Foot<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

//import java.util.ArrayList;
//import java.util.Optional;

//import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.ProtoN;

/**
 * Objects within the cladosG package have a number of attributes in common that
 * relate to a point that describes where the geometry of the algebra is tangent
 * to the manifold underneath it. In a flat space it is not really necessary to
 * use a foot point, but a simple one is still used in the cladosG package to
 * help avoid the trap of assuming transport of objects along the manifold
 * doesn't cause them to re-orient and change into other objects.
 * <br><br>
 * At a minimum, a Foot is the name of an event and the type of numbers used 
 * as wieghts for the tangent space. At this location it is
 * assumed the geometry of the algebra is aligned with the tangent geometry of 
 * the manifold at that point. This makes a Foot the root reference.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Foot {
	/**
	 * This factory build method produces a new Foot with an empty cardinal list. It
	 * is intend to name an otherwise opaque constructor that creates a new Foot but
	 * re-uses a Cardinal.
	 * <br>
	 * @param pFootName String name for the new Foot
	 * @return Foot Factory method builds a new Foot re-using a Cardinal object.
	 */
	public static final Foot buildAsType(String pFootName) {
		return new Foot(pFootName);
	}

	/**
	 * This String is the name the footPoint. Think of it as naming the tangent
	 * point between a flat algebra and the curvy manifold.
	 */
	private String footName;

	/**
	 * Build the Foot from scratch.
	 * <br>
	 * @param pName String This string will be the name of the foot point.
	 */
	public Foot(String pName) {
		setFootName(pName);
		FBuilder.createCardinal(pName);
	}

	/**
	 * Build the Foot from scratch then put the number's Cardinal in the internal list.
	 * <br>
	 * @param pName String This string will be the name of the foot point.
	 * @param pF    ProtoN This object holds the cardinal that defines the kind of
	 *              numbers that are meaningful for this foot point
	 */
	public Foot(String pName, ProtoN pF) {
		setFootName(pName);
		FCache.INSTANCE.appendCardinal(pF.getCardinal());
	}

	/**
	 * Simple gettor of the Foot's name element
	 * <br>
	 * @return String name of the Foot
	 */
	public String getFootName() {
		return footName;
	}

	/**
	 * Simple setter of the Foot's name element.
	 * <br>
	 * @param footName String name of the Foot to set here
	 */
	public void setFootName(String footName) {
		this.footName = footName;
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <br>
	 * @param pF Foot the be exported as XML.
	 * @param indent String indentation to assist with human readability of output
	 *               XML data
	 * @return String formatted as XML containing information about the Foot
	 */
	public final static String toXMLString(Foot pF, String indent) {
		if (indent == null)		indent = "\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Foot name=\"");
		rB	.append(pF.getFootName())
			.append("\" />\n");
		return rB.toString();
	}
}