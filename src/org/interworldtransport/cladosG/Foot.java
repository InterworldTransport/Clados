/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Foot<br>
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
 * ---org.interworldtransport.cladosG.Foot<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.UnitAbstract;

/**
 * Objects within the cladosG package have a number of attributes in common that
 * relate to a point that describes where the geometry of the algebra is tangent
 * to the manifold underneath it. In a flat space it is not really necessary to
 * use a foot point, but a simple one is still used in the cladosG package to
 * help avoid the trap of assuming transport of objects along the manifold
 * doesn't cause them to re-orient and change into other objects.
 * <p>
 * At a minimum, a footPoint is the name of an event or a point and the type of
 * numbers used for scale along the coordinate paths. At this location it is
 * assumed the geometry of the algebra is aligned with the local geometry one
 * would assign to the coordinate space that is the manifold at that point. This
 * makes a footPoint the root reference frame.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Foot {
	/**
	 * This factory build method produces a new Foot with an empty cardinal list. It
	 * is intend to name an otherwise opaque constructor that creates a new Foot but
	 * re-uses a Cardinal.
	 * <p>
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
	 * <p>
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
	 * This String is the name the footPoint. Think of it as naming the tangent
	 * point between a flat algebra and the curvy manifold.
	 */
	private String footName;

	/**
	 * Build the Foot from scratch.
	 * <p>
	 * @param pName String This string will be the name of the foot point.
	 */
	public Foot(String pName) {
		setFootName(pName);
		cardinalList = new ArrayList<Cardinal>(1);
		cardinalList.add(CladosFBuilder.createCardinal(pName));
	}

	/**
	 * Build the Foot from scratch then put the Cardinal in the internal list.
	 * <p>
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
	 * Build the Foot from scratch then put the number's Cardinal in the internal list.
	 * <p>
	 * @param pName String This string will be the name of the foot point.
	 * @param pF    UnitAbstract This object holds the cardinal that defines the kind of
	 *              numbers that are meaningful for this foot point
	 */
	public Foot(String pName, UnitAbstract pF) {
		setFootName(pName);
		cardinalList = new ArrayList<Cardinal>(1);
		cardinalList.add(pF.getCardinal());
	}

	/**
	 * This method appends a Cardinal to the list of known cardinals for this foot.
	 * It will silently terminate IF the cardinal is already in the list.
	 * <p>
	 * The uniqueness test is done BY OBJECT and not by the cardinal's string name.
	 * That means visual inspection of the cardinal list could reveal entries that
	 * appear to be the same. They aren't, though. The cardinalList references
	 * objects and NOT their string names. To avoid this use CladosFCache.
	 * <p>
	 * @param fN Cardinal reference to append to this Foot
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
	 * <p>
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

	/**
	 * Simple gettor for one of the cardinals associated with this Foot.
	 * <p>
	 * Note that an error condition (like indexOutOfBounds) will return null.
	 * <p>
	 * @param pIn Integer index of Cardinal in this Foot to retrieve.
	 * @return Cardinal found at the integer index... or null.
	 */
	public Cardinal getCardinal(int pIn) {
		if (pIn >= 0 & pIn < cardinalList.size())
			return cardinalList.get(pIn);
		return null;
	}

	/**
	 * Simple gettor for the entire list of cardinals associated with this Foot.
	 * <p>
	 * @return ArrayList of Cardinal instances associated with this Foot.
	 */
	public ArrayList<Cardinal> getCardinals() {
		return cardinalList;
	}

	/**
	 * Simple gettor of the Foot's name element
	 * <p>
	 * @return String name of the Foot
	 */
	public String getFootName() {
		return footName;
	}

	/**
	 * This method removes a Cardinal from the list of known cardinals for this
	 * foot. It will silently return IF the cardinal wasn't already on the list.
	 * <p>
	 * The uniqueness test is done BY OBJECT and not by the cardinal's string name.
	 * That means visual inspection of the cardinal list could reveal entries that
	 * appear to be the same. They aren't, though. The cardinalList references
	 * objects and NOT their string names. To avoid this use CladosFCache.
	 * <p>
	 * @param pCard Cardinal reference to remove to this Foot
	 */
	public void removeCardinal(Cardinal pCard) {
		cardinalList.remove(pCard);
	}

	/**
	 * Simple setter of the Foot's name element.
	 * <p>
	 * @param footName String name of the Foot to set here
	 */
	public void setFootName(String footName) {
		this.footName = footName;
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <p>
	 * @param indent String indentation to assist with human readability of output
	 *               XML data
	 * @return String formatted as XML containing information about the Foot
	 */
	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Foot>\n");
		rB.append(indent).append("\t<Name>").append(getFootName()).append("</Name>\n");
		// -----------------------------------------------------------------------
		rB.append(indent).append("\t<Cardinals number=\"").append(cardinalList.size()).append("\" >\n");
		for (Cardinal point : cardinalList)
			rB.append(indent).append(point.toXMLString("\t\t"));
		rB.append(indent).append("\t</Cardinals>\n");
		// -----------------------------------------------------------------------
		rB.append(indent).append("</Foot>\n");
		return rB.toString();
	}
}
