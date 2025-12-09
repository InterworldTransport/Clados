/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ProtoN<br>
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
 * ---org.interworldtransport.cladosF.ProtoN<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * ProtoN is short for 'proto number'. 
 * This is the parent class supporting the notion of a division field
 * from mathematics. Division fields are used as 'numbers' to scale blades 
 * in an algebra, but they are better known as scalars in the sense of 
 * linear combinations in vector spaces.
 * <br><br>
 * ProtoN handles unit references common to all its children. When you imagine
 * the difference between five pigs and five meters you have the role played 
 * by ProtoN's cardinal element. It says what a number is without saying
 * how much of it there is.
 * <br><br>
 * ProtoN also implements comparisons that can be overridden if deeper
 * comparisons are necessary. For example, equality tests at this level only 
 * test high level references to object and cardinal equality. This is just
 * enough for unit type matching, but not quantity matching.
 * <br><br>
 * The number(s) that make up quantity don't appear except in a child of
 * this class. The number and precision of reals for a division field varies. 
 * Complex numbers require two. Quaternions require four. 
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class ProtoN {	
	/**
	 * Check to see if the two argument are of the same cardinal.
	 * <br>
	 * @param pE ProtoN
	 * @param pF ProtoN
	 * @return boolean
	 */
	public static final boolean isTypeMatch(ProtoN pE, ProtoN pF) {
		if (pE == null | pF == null)	return false;
		if (pE.card != null)			return pE.equals(pF);
		if (pF.card != null)			return pF.equals(pE);
		return true;
	}

	/**
	 * Object for the cardinal. A string used to be used here, but a Cardinal 
	 * object enables reuse through reference allowing all coefficients in monads
	 * to point to the same place.
	 * <br>
	 * This Cardinal is what gives a proto number a sense of what the number means.
	 */
	protected Cardinal card;

	/**
	 * Construct a simple ProtoN using the Cardinal offered.
	 * <br>
	 * @param pCard Cardinal to re-use.
	 */
	public ProtoN(Cardinal pCard) {
		setCardinal(pCard);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)								return true;
		if (obj == null)								return false;
		if (getClass() != obj.getClass())				return false;
		if 		(getCardinal() == null) {
			if 	(((ProtoN) obj).getCardinal() == null)	return true;
			else 										return false;
		}
		else if (((ProtoN) obj).getCardinal() == null)	return false;
		
		return getCardinal().equals(((ProtoN) obj).getCardinal());		
	}

	/**
	 * Get method for _card
	 * <br>
	 * @return Cardinal (A cardinal name for a ProtoN)
	 */
	public Cardinal getCardinal() {
		return card;
	}

	/**
	 * Get method for _card
	 * <br>
	 * @return Cardinal (A cardinal name for a ProtoN)
	 */
	public String getCardinalString() {
		return card.getUnit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCardinal() == null) ? 0 : getCardinal().hashCode());
		return result;
	}

	/**
	 * Return a string representation of the field element.
	 * <br>
	 * @param pA ProtoN to be exported as XML... which just means the Cardinal right now.
	 * @return String Flat string representation of this Field float(s) field
	 */
	public static String toXMLString(ProtoN pA) {
		return ("<ProtoN cardinal=\"" + pA.getCardinalString() + "\" />");
	}
	
	/**
	 * Set method for the cardinal. Mess with this at your peril. Seriously. Altering
	 * it after construction leads to a redefining of what the number means and in 
	 * multivectors it HAS to be done consistently or you'll get reference match failures.
	 * <br><br>
	 * @param pType Cardinal
	 */
	public void setCardinal(Cardinal pType) {
		card = pType;
	}
}