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

import java.util.Optional;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.interworldtransport.cladosGExceptions.GradeOutOfRangeException;

/**
 * This class defines a geometric product on an associated basis within a
 * Clifford Algebra. The flat space on which multiplication is defined is
 * assumed to be locally tangent to a manifold, but the difference in product
 * results form one tangent space to another are NOT tracked here. Only the
 * structure of the geometric product on a canonical basis is. Other
 * distinctions are kept in the algebra objects that reference a GProduct.
 * 
 * A GProduct object actually assumes it is OK to perform a requested operation
 * and will throw an exception if it discovers later that it isn't. This is true
 * most everywhere except in the constructor where input is examined first.
 * 
 * Most errors can be avoided by using CladosGBuilder to construct this object.
 * However, it shouldn't be necessary to construct a GProduct directly. Best
 * practice is to create an algebra instead.
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class GProductMap {

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
				case '+':
					continue;
				case '-':
					continue;
				default:
					return false;
				}
		return true; // nothing bad detected
	}

	/**
	 * This basis holds a representation of all the elements that can be built from
	 * the generators to span the algebra's vector space. It is the object that Ken
	 * Greider called the Eddington Basis.
	 */
	private final BasisList canonicalBasis;

	/**
	 * This integer array is an internal translation of the product signature.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as minus one (-1). This array is kept to increase the
	 * speed of product calculations.
	 */
	private final byte[] nSignature;
	/**
	 * This array holds the geometric multiplication table for a Clifford algebra
	 * using the associated basis. The array contains numbers that represent the
	 * blade # one would produce with a product of blades (row+1) and (column+1) of
	 * result.
	 * 
	 * Negative results in the array imply the resulting blade is scaled by -1.
	 * 
	 * The +1 offsets are present because java arrays start with an index of 0,
	 * while the lowest rank blade is #1.
	 */
	private final int[][] result;

	/**
	 * This string holds the signature information describing the squares of all
	 * geometry generators present on the multiplication table.
	 */
	private final String signature;

	/**
	 * Main constructor of ProductTable with signature information passed in. It
	 * figures out the rest of what it needs.
	 * 
	 * @param pSig String form of the signature. Looks like "-+++".
	 * @throws GeneratorRangeException   Thrown when a Basis fails to form because
	 *                                   some internal call demands a generator not
	 *                                   in the supported list.
	 * @throws BadSignatureException     Thrown when an invalid signature is found
	 * @throws BladeCombinationException Thrown when to blades multiplied do not
	 *                                   result in another known blade.
	 */
	public GProductMap(String pSig) throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(this, "Valid signature required.");
		else if (!BasisList.validateSize(pSig.length()))
			throw new GeneratorRangeException("Signature length unsupported");

		nSignature = (pSig.length() == 0) ? new byte[1] : new byte[pSig.length()];
		fillNumericSignature(pSig);
		signature = pSig;

		// canonicalBasis = CladosGBuilder.INSTANCE.createBasis((short) pSig.length());
		canonicalBasis = new BasisList((byte) pSig.length()); // Replace with CladosGBuilder call soon

		result = new int[getBladeCount()][getBladeCount()];
		fillResult();
		// fillResult() can throw two exceptions detected for above, so it likely won't.
		// The third one involves malformed blade combinations. Again... that shouldn't
		// happen if blades are well formed.
	}

	/**
	 * Return a measure of whether blades pj and pk anticommute. Return a 1 if they
	 * anticommute. Return a 0 otherwise.
	 * 
	 * @param pj int
	 * @param pk int
	 * @return int
	 */
	public int getACommuteSign(int pj, int pk) {
		return (result[pj][pk] == result[pk][pj] * -1) ? 1 : 0;
	}

	/**
	 * Basic Get method for the Basis generated by the signature of this GProduct.
	 * 
	 * @return Basis
	 */
	public BasisList getBasis() {
		return canonicalBasis;
	}

	/**
	 * Get the linear dimension of the vector space that uses the associated Basis.
	 * 
	 * @return int
	 */
	public int getBladeCount() {
		return canonicalBasis.getBladeCount();
	}

	/**
	 * Return a measure of whether blades pj and pk commute. Return a 1 if they
	 * commute. Return a 0 otherwise.
	 * 
	 * @param pj int
	 * @param pk int
	 * @return int
	 */
	public int getCommuteSign(int pj, int pk) {
		return (result[pj][pk] == result[pk][pj]) ? 1 : 0;
	}

	/**
	 * Get the grade count of the algebra that uses this GProduct.
	 * 
	 * @return byte
	 */
	public byte getGradeCount() {
		return canonicalBasis.getGradeCount();
	}

	/**
	 * Return row pj of result array. Meant for alternate multiplication methods.
	 * 
	 * @param pj int
	 * @return int[][]
	 */
	public int[] getResult(int pj) {
		return result[pj];
	}

	/**
	 * Return an element of the array holding the geometric multiplication results.
	 * 
	 * @param pj int
	 * @param pk int
	 * @return int
	 */
	public int getResult(int pj, int pk) {
		return result[pj][pk];
	}

	/**
	 * Return an element of the array holding the geometric multiplication results.
	 * 
	 * @param pj short
	 * @param pk short
	 * @return int
	 */
	public int getSign(int pj, int pk) {
		return (result[pj][pk] < 0) ? -1 : 1;
	}

	/**
	 * Return the signature of the generator geometry. This lists the squares of the
	 * generators in their numeric order.
	 * 
	 * @return String
	 */
	public String signature() {
		return signature;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @return String This is the XML string export of an object.
	 */
	public final static String toXMLString(GProductMap gP, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct>\n");
		rB.append(indent).append("\t<Signature>").append(gP.signature()).append("</Signature>\n");
		rB.append(gP.getBasis().toXMLString(""));
		rB.append(indent).append("\t<ProductTable rows=\"").append(gP.getBladeCount()).append("\">\n");
		for (short k = 0; k < gP.getBladeCount(); k++) // Appending rows
		{
			rB.append(indent).append("\t\t<row number=\"").append(k).append("\" cells=\"");
			for (short m = 0; m < gP.getBladeCount(); m++)
				rB.append(gP.getResult(k, m)).append(",");
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append(indent + "\t</ProductTable>\n");
		rB.append(indent + "</GProduct>\n");
		return rB.toString();
	}

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

	private void fillResult() throws BladeCombinationException, GeneratorRangeException {
		for (int j = 0; j < getBladeCount(); j++) {
			result[0][j] = (j + 1);
			result[j][0] = (j + 1);
		} // Scalar section of result done separately because result blade is known.
		Blade bLeft, tSpot;  //bRight,
		for (int j = 1; j < getBladeCount(); j++) {
			bLeft = canonicalBasis.getSingleBlade(j);
			for (int k = 1; k < getBladeCount(); k++) {
				//bRight = canonicalBasis.getSingleBlade(k);

				// TODO I don't really need the whole blade. I need the blade's key and sign.
				BladeDuet bD = new BladeDuet(bLeft, canonicalBasis.getSingleBlade(k));
				tSpot = bD.reduce(nSignature);
				// BladeDuet does the heavy lifting of blade multiplication
				int tSpotLoc = canonicalBasis.findKey(tSpot.key()) + 1;

				result[j][k] = tSpotLoc * tSpot.sign();
			}
		}
	}

	/**
	 * Get start and end index from the GradeRange array for grade pGrade.
	 * 
	 * @param pGrade short primitive = grade for which the range is needed
	 * @return short[] start and end indexes returned as a short[] array
	 * @throws GradeOutOfRangeException
	 */
	protected int[] getGradeRange(byte pGrade) throws GradeOutOfRangeException {
		int[] tR = new int[2];
		tR[0] = canonicalBasis.getGradeStart(pGrade);
		tR[1] = ((pGrade == canonicalBasis.getGradeCount() - 1) // is this MaxGrade? If so, top=bottom
				? tR[0]
				: (canonicalBasis.getGradeStart((byte) (pGrade + 1)) - 1));
		return tR;
	}

}
