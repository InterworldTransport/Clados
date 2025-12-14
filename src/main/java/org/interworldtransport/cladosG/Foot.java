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

import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.ProtoN;

/**
 * Objects within the cladosG package have a number of attributes in common that relate to a point that describes 
 * where the geometry of the algebra is tangent to the manifold underneath it. In a flat space it is not necessary 
 * to use a foot, but a simple one is still used by cladosG classes to help avoid the trap of assuming transport of 
 * objects along the manifold doesn't cause them to transform into other objects.
 * <br><br>
 * At a minimum a Foot is the name of a point, but it may also be thought of as a name for an open set of points
 * covered by a 'chart' (topology). This set is the portion of the underlying manifold that has an image in the
 * tangent space where the cladosG classes will represent geometry. Objects with different Feet, therefore, are 
 * covered by different charts. That makes a Foot a root reference label.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Foot {
	/**
	 * This factory build method produces a new Foot. Plain and simple.
	 * <br><br>
	 * @param pFootName String name for the new Foot
	 * @return Foot Factory method builds a new Foot re-using a Cardinal object.
	 */
	public static final Foot buildAsType(String pFootName) {
		return new Foot(pFootName);
	}

	/**
	 * A human readable name for the Foot. The "tangent point" between a flat algebra and the curvy manifold is being labeled.
	 */
	private String footName;

	/**
	 * Build the Foot from scratch.
	 * <br><br>
	 * @param pName String This string will be the name of the foot point.
	 */
	public Foot(String pName) {
		setName(pName);
		FBuilder.createCardinal(pName);
	}

	/**
	 * Build the Foot from scratch then put the number's Cardinal in the internal list.
	 * <br><br>
	 * @param pName String This string will be the name of the foot point.
	 * @param pF    ProtoN This object holds the cardinal that defines the kind of
	 *              numbers that are meaningful for this foot point
	 */
	public Foot(String pName, ProtoN pF) {
		setName(pName);
		FCache.INSTANCE.appendCardinal(pF.getCardinal());
	}

	/**
	 * Simple gettor of the Foot's name element
	 * <br><br>
	 * @return String name of the Foot
	 */
	public String getName() {
		return footName;
	}

	/**
	 * Simple setter of the Foot's name element.
	 * <br><br>
	 * @param footName String name of the Foot to set here
	 */
	public void setName(String footName) {
		this.footName = footName;
	}

	
}