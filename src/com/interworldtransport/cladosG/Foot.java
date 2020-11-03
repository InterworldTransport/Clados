/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.Foot<br>
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
 * ---com.interworldtransport.cladosG.Foot<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;
//import java.util.ListIterator;

import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosF.Cardinal;

/**
 * Objects within the cladosG package have a number of attributes in common that
 * relate to a point that describes where the geometry of the algebra is tangent
 * to the manifold underneath it. In a flat space it is not really necessary to
 * use a foot point, but a simple one is still used in the cladosG package to
 * help avoid the trap of assuming transport of objects along the manifold
 * doesn't cause them to re-orient and change into other objects.
 * 
 * At a minimum, a footPoint is the name of an event or a point and the type of
 * numbers used for scale along the coordinate paths. At this location it is
 * assumed the geometry of the algebra is aligned with the local geometry one
 * would assign to the coordinate space that is the manifold at that point. This
 * makes a footPoint the root reference frame.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class Foot {
	/**
	 * This factory build method produces a new Foot with an empty cardinal list. It
	 * is intend to name an otherwise opaque constructor that creates a new Foot but
	 * re-uses a Cardinal.
	 * 
	 * @param pFootName String name for the new Foot
	 * @return Foot Factory method builds a new Foot re-using a Cardinal object.
	 */
	public static final Foot buildAsType(String pFootName) {
		return new Foot(pFootName);
	}

	/**
	 * This factory build method produces a new Foot using the offered Cardinal. It
	 * is intend to name an otherwise opaque constructor that creates a new Foot but
	 * re-uses a Cardinal.
	 * 
	 * @param pFootName    String name for the new Foot
	 * @param pNewCardinal Cardinal to re-use for this new Foot
	 * @return Foot Factory method builds a new Foot re-using a Cardinal object.
	 */
	public static final Foot buildAsType(String pFootName, Cardinal pNewCardinal) {
		return new Foot(pFootName, pNewCardinal);
	}

	/**
	 * This object defines the type of numbers used by all objects that share this
	 * footPoint.
	 */
	private ArrayList<Cardinal> cardinalList;
	/**
	 * This String is the name the footPoint of the Reference Frame of the Monad
	 */
	private String footName;

	/**
	 * Build the footPoint object from scratch.
	 * 
	 * @param pName String This string will be the name of the foot point.
	 */
	public Foot(String pName) {
		setFootName(pName);
		cardinalList = new ArrayList<Cardinal>(1);
		cardinalList.add(Cardinal.generate(pName));
	}

	/**
	 * Build the footPoint object from scratch.
	 * 
	 * @param pName String This string will be the name of the foot point.
	 * @param pFT   Cardinal This object defines the kind of numbers that are
	 *              meaningful for this foot point
	 */
	public Foot(String pName, Cardinal pFT) {
		setFootName(pName);
		cardinalList = new ArrayList<Cardinal>(1);
		cardinalList.add(pFT);
	}

	/**
	 * Build the footPoint object from scratch.
	 * 
	 * @param pName String This string will be the name of the foot point.
	 * @param pF    DivField This object holds the cardinal that defines the kind of
	 *              numbers that are meaningful for this foot point
	 */
	public Foot(String pName, DivField pF) {
		setFootName(pName);
		cardinalList = new ArrayList<Cardinal>(1);
		cardinalList.add(pF.getCardinal());
	}

	/**
	 * This method appends a Cardinal to the list of known cardinals for this foot.
	 * It will silently terminate IF the cardinal is already in the list.
	 * 
	 * The uniqueness test is done BY OBJECT and not by the cardinal's string name.
	 * That means visual inspection of the cardinal list could reveal entries that
	 * appear to be the same. They aren't, though. The cardinalList references
	 * objects and NOT their string names.
	 * 
	 * @param fN Cardinal This is a reference to the Cardinal to append to this Foot
	 */
	public void appendCardinal(Cardinal fN) {
		if (cardinalList.contains(fN))
			return;
		cardinalList.ensureCapacity(cardinalList.size() + 1);
		cardinalList.add(fN);
	}

	/**
	 * This method looks for the requested cardinal object in the Foot's tracking
	 * list.
	 * 
	 * @param pIn Cardinal This is a reference to the Cardinal to be found in the
	 *            Foot's cardinal list.
	 * @return int This method looks for the requested Cardinal in the Foot's list
	 *         and returns the index if it is found. If not, it returns -1.
	 */
	public int findCardinal(Cardinal pIn) {
		if (cardinalList == null)
			return -1;
		if (cardinalList.contains(pIn))
			return cardinalList.indexOf(pIn);
		return -1;
	}

	public Cardinal getCardinal(int pIn) {
		return cardinalList.get(pIn);
	}

	public ArrayList<Cardinal> getCardinals() {
		return cardinalList;
	}

	public String getFootName() {
		return footName;
	}

	public void removeCardinal(Cardinal pCard) {
		cardinalList.remove(pCard);
	}

	public void setFootName(String footName) {
		this.footName = footName;
	}

	public String toXMLString(String pind) {
		String indent = "\t\t";
		if (pind == null)
			pind = "";
		StringBuilder rB = new StringBuilder(indent + pind + "<Foot>\n");
		rB.append(indent + pind + "\t<Name>\"" + getFootName() + "\"</Name>\n");
		// -----------------------------------------------------------------------
		rB.append(indent + pind + "\t<Cardinals number=\"" + cardinalList.size() + "\" >\n");
		for (Cardinal point : cardinalList)
			rB.append(indent + pind + "\t\t" + point.toXMLString());
		rB.append(indent + pind + "\t</Cardinals>\n");
		// -----------------------------------------------------------------------
		rB.append(indent + pind + "</Foot>\n");
		return rB.toString();
	}
}
