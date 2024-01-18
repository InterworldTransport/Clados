/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GProduct<br>
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
 * ---org.interworldtransport.cladosG.GProduct<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * This class defines a geometric product on an associated basis within a
 * Clifford Algebra. The flat space on which multiplication is defined is
 * assumed to be locally tangent to a manifold, but the difference in product
 * results form one tangent space to another are NOT tracked here. Only the
 * structure of the geometric product on a canonical basis is. Other
 * distinctions are kept in the algebra objects that reference a GProduct.
 * <p>
 * A GProduct object actually assumes it is OK to perform a requested operation
 * and will throw an exception if it discovers later that it isn't. This is true
 * most everywhere except in the constructor where input is examined first.
 * <p>
 * Most errors can be avoided by using CladosGBuilder to construct this object.
 * However, it shouldn't be necessary to construct a GProduct directly. Best
 * practice is to create an algebra and let it construct its product.
 * <p>
 * The implemented interface is currently all the methods available in this
 * class. That will change in the future as helper methods are built here that
 * need not be exposed elsewhere.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class GProduct implements CliffordProduct {

	/**
	 * This basis holds a representation of all the elements that can be built from
	 * the generators to span the algebra's vector space. It is the object that Ken
	 * Greider called the Eddington Basis.
	 */
	private final CanonicalBasis canonicalBasis;

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
	 * <p>
	 * Negative results in the array imply the resulting blade is scaled by -1.
	 * <p>
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
	 * Main constructor of GProduct with signature information passed in. It
	 * figures out the rest of what it needs.
	 * <p>
	 * @param pSig String form of the signature. Looks like "-+++".
	 * @throws GeneratorRangeException Thrown when a Basis fails to form because
	 *                                 some internal call demands a generator not in
	 *                                 the supported list.
	 * @throws BadSignatureException   Thrown when an invalid signature is found
	 */
	public GProduct(String pSig) throws BadSignatureException, GeneratorRangeException {
		this(null, pSig);
	}

	/**
	 * A re-use constructor of GProduct with signature and Basis passed in. It
	 * figures out the rest of what it needs.
	 * <p>
	 * @param pSig String form of the signature. Looks like "-+++".
	 * @param pB   Canonical Basis to re-use in constructing this product.
	 * @throws GeneratorRangeException Thrown when a Basis fails to form because
	 *                                 some internal call demands a generator not in
	 *                                 the supported list.
	 * @throws BadSignatureException   Thrown when an invalid signature is found
	 */
	public GProduct(CanonicalBasis pB, String pSig) throws BadSignatureException, GeneratorRangeException {
		if (!CliffordProduct.validateSignature(pSig))
			throw new BadSignatureException(this, "Valid signature required.");
		else if (!CanonicalBasis.validateSize(pSig.length()))
			throw new GeneratorRangeException("Signature length unsupported");
		// ------Init signature
		nSignature = (pSig.length() == 0) ? new byte[1] : new byte[pSig.length()];
		int m = 0;
		for (char b : pSig.toCharArray()) {
			switch (b) {
			case '+' -> nSignature[m] = 1;
			case '-' -> nSignature[m] = -1;
			}
			m++;
		}
		signature = pSig;
		// ------Get CanonicalBasis
		canonicalBasis = (pB != null) ? pB : CladosGBuilder.createBasis((byte) pSig.length());
		// ------Build Product Table
		result = new int[getBladeCount()][getBladeCount()];
		canonicalBasis.bladeStream().parallel().forEach(bladeLeft -> {
			int row = canonicalBasis.getKeyIndexMap().get(bladeLeft.key()) - 1;
			canonicalBasis.bladeStream().forEach(bladeRight -> {
				int col = canonicalBasis.getKeyIndexMap().get(bladeRight.key()) - 1;
				Blade bMult = BladeDuet.simplify(bladeLeft, bladeRight, nSignature);
				result[row][col] = bMult.sign() * canonicalBasis.getKeyIndexMap().get(bMult.key());
			});
		});
	}

	/**
	 * Return a measure of whether blades pj and pk anticommute. Return a 1 if they
	 * anticommute. Return a 0 otherwise.
	 * <p>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getACommuteSign(int pRow, int pCol) {
		return (result[pRow][pCol] == result[pCol][pRow]) ? 0 : 1;
	}

	/**
	 * Basic Get method for the Basis generated by the signature of this GProduct.
	 * <p>
	 * @return Basis
	 */
	@Override
	public final CanonicalBasis getBasis() {
		return canonicalBasis;
	}

	/**
	 * Get the linear dimension of the vector space that uses the associated Basis.
	 * <p>
	 * @return int
	 */
	@Override
	public final int getBladeCount() {
		return canonicalBasis.getBladeCount();
	}

	/**
	 * Return a measure of whether blades pj and pk commute. Return a 1 if they
	 * commute. Return a 0 otherwise.
	 * <p>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getCommuteSign(int pRow, int pCol) {
		return (result[pRow][pCol] == result[pCol][pRow]) ? 1 : 0;
	}

	/**
	 * Get the grade count of the algebra that uses this GProduct.
	 * <p>
	 * @return byte
	 */
	@Override
	public final byte getGradeCount() {
		return canonicalBasis.getGradeCount();
	}

	/**
	 * Get start and end index from the GradeRange array for grade pGrade.
	 * <p>
	 * @param pGrade short primitive = grade for which the range is needed
	 * @return int[] start and end indexes returned as a short[] array
	 */
	@Override
	public final int[] getGradeRange(byte pGrade) {
		int[] tR = new int[2];
		tR[0] = canonicalBasis.getGradeStart(pGrade);
		tR[1] = ((pGrade == canonicalBasis.getGradeCount() - 1) // is this MaxGrade? If so, top=bottom
				? tR[0]
				: (canonicalBasis.getGradeStart((byte) (pGrade + 1)) - 1));
		return tR;
	}

	@Override
	public final int[] getPScalarRange() {
		int[] tR = new int[2];
		tR[0] = canonicalBasis.getPScalarStart();
		tR[1] = tR[0];
		return tR;
	}

	/**
	 * Return row of result array. Meant for alternate multiplication methods.
	 * <p>
	 * @param pRow int
	 * @return int[][]
	 */
	public final int[] getResult(int pRow) {
		return result[pRow];
	}

	/**
	 * Return an element in the geometric multiplication result table.
	 * <p>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getResult(int pRow, int pCol) {
		return result[pRow][pCol];
	}

	/**
	 * Return the sign of an element in the geometric multiplication result table.
	 * <p>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getSign(int pRow, int pCol) {
		return (result[pRow][pCol] < 0) ? -1 : 1;
	}

	/**
	 * Return the signature of the generating geometry. This lists the squares of the
	 * generators in their numeric order.
	 * <p>
	 * @return String
	 */
	@Override
	public final String signature() {
		return signature;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form.
	 * <p>
	 * @return String This is the XML string export of an object.
	 */
	@Override
	public final String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct>\n");
		rB.append(indent).append("\t<Signature>").append(signature()).append("</Signature>\n");
		rB.append(getBasis().toXMLString(indent + "\t"));
		rB.append(indent).append("\t<ProductTable rows=\"").append(getBladeCount()).append("\">\n");
		for (short k = 0; k < getBladeCount(); k++) // Appending rows
		{
			rB.append(indent).append("\t\t<row number=\"").append(k).append("\" cells=\"");
			for (short m = 0; m < getBladeCount(); m++)
				rB.append(getResult(k, m)).append(",");
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append(indent + "\t</ProductTable>\n");
		rB.append(indent + "</GProduct>\n");
		return rB.toString();
	}
}