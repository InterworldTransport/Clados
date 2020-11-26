/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GProductMap<br>
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
 * ---org.interworldtransport.cladosG.GProductMap<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.interworldtransport.cladosGExceptions.GradeOutOfRangeException;

/**
 * This class defines a geometric product for an associated basis.
 * <p>
 * GProduct encapsulates Clifford algebra geometric multiplication. GProduct
 * objects are stored as a separate class to support flat space multivectors
 * that can afford to share their product definition objects.
 * <p>
 * Operations in the GProduct should return logicals for tests the objects can
 * answer. They should alter inbound objects for complex requests. At no point
 * should a GProduct object have to make a copy of itself or inbound objects
 * except for private, temporary use.
 * <p>
 * A GProduct object will actually assume it is OK to perform the requested
 * operations and throw an exception if it discovers later that it isn't. Error
 * discovery isn't likely to be complete, so it should not be expected to work
 * well. Only checks against primitive elements can be done. Physical sense
 * checks need to be performed at the level of the object referencing the
 * GProduct or higher.
 * <p>
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class GProductMap {

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @return String This is the XML string export of an object.
	 */
	public String toXMLString(GProductMap gP, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct>\n");
		rB.append(indent).append("\t<Signature>").append(gP.signature()).append("</Signature>\n");
		rB.append(canonicalBasis.toXMLString());
		rB.append(indent).append("\t<ProductTable rows=\"").append(gP.getBladeCount()).append("\">\n");
		for (short k = 0; k < gP.getBladeCount(); k++) // Appending rows
		{
			rB.append(indent + "\t\t<row number=\"" + k + "\" cells=\"");
			for (short m = 0; m < gP.getBladeCount(); m++)
				rB.append(getResult(k, m) + ",");
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append(indent + "\t</ProductTable>\n");
		rB.append(indent + "</GProduct>\n");
		return rB.toString();
	}

	/**
	 * Return a measure of the validity of the Signature string. A string with +'s
	 * and -'s will pass. No other one does.
	 * 
	 * This method also establishes the internal integer representation of the
	 * signature.
	 * 
	 * @param pSg String
	 * @return boolean This boolean states whether the GProduct signature is valid.
	 */
	public static final boolean validateSignature(String pSg) {
		if (pSg == null)
			return false; // Nothing to test
		else if (!BasisList.validateSize((byte) pSg.length()))
			return false;
		else if (pSg.length() == 0)
			return true; // Empty list IS allowed
		else
			for (char j : pSg.toCharArray())
				switch (j) {
				case '+' | '-':
					continue;
				default:
					return false;
				}
		return true; // nothing bad detected
	}

	/**
	 * This integer array is an internal translation of the product signature.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as minus one (-1). This array is kept to increase the
	 * speed of product calculations.
	 */
	private final byte[] nSignature;

	/**
	 * This string holds the signature information describing the squares of all
	 * geometry generators present on the multiplication table.
	 */
	private final String signature;

	/**
	 * This is a method that only gets called during construction. It fills the
	 * numeric signature array. '+' becomes 1 and '-' becomes -1.
	 * 
	 * This numeric signature is used later as a shorthand for whether to do a sign
	 * flip when generator pairs are removed from a product. 1 implies no need for
	 * sign flip. -1 implies a need for sign flip.
	 * 
	 * @param pSig
	 */
	private void fillNumericSignature(String pSig) {
		int m = 0;
		for (char b : pSig.toCharArray()) {
			switch (b) {
			case '+' -> nSignature[m] = 1;
			case '-' -> nSignature[m] = -1;
			}
			m++;
		}
	}

}
