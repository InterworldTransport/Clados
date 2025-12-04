/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GProduct<br>
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
 * ---org.interworldtransport.cladosG.GProduct<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Optional;

import org.interworldtransport.cladosGExceptions.BadSignatureException;

/**
 * This class defines a geometric product on an associated basis within a
 * Clifford Algebra. The flat space on which multiplication is defined is
 * assumed to be locally tangent to a manifold, but the difference in product
 * results form one tangent space to another are NOT tracked here. Only the
 * structure of the geometric product on a canonical basis is. Other
 * distinctions are kept in the algebra objects that reference a GProduct.
 * <br><br>
 * A GProduct object actually assumes it is OK to perform a requested operation
 * and will throw an exception if it discovers later that it isn't. This is true
 * most everywhere except in the constructor where input is examined first.
 * <br><br>
 * Most errors can be avoided by using GBuilder to construct this object.
 * However, it shouldn't be necessary to construct a GProduct directly. Best
 * practice is to create an algebra and let it construct its product.
 * <br><br>
 * The implemented interface is currently all the methods available in this
 * class. That will change in the future as helper methods are built here that
 * need not be exposed elsewhere.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class GProduct implements CliffordProduct, Comparable<GProduct> {

	/**
	 * This basis holds a representation of all the elements that can be built from
	 * the generators to span the algebra's vector space. It is the object that Ken
	 * Greider called the Eddington Basis.
	 */
	private final Basis canonBasis;

	/**
	 * This array holds the geometric multiplication table for a Clifford algebra
	 * using the associated basis. The array contains numbers that represent the
	 * blade # one would produce with a product of blades (row) and (column) of
	 * result.
	 * <br>
	 * Negative results in the array imply the resulting blade is scaled by -1.
	 */
	private final int[][] result;

	/**
	 * This string holds the signature information describing the squares of all
	 * geometry generators present on the multiplication table.
	 * <br>
	 * The term 'signature' is currently overloaded with meanings. The one being 
	 * used here is a long form aggregate of '+', '-', and '0' bytes encoding the
	 * squares of an algebra's generators. The short form that adds up the number
	 * for each byte and presents a list of three integers is NOT in use here. 
	 * That means this signature string has the details one expects in a quadratic
	 * form after generators have been assigned roles in a basis.
	 */
	private final String signature;

	/**
	 * This array is an integer representation of the signature string. Generators
	 * with positive squares are one (1), negative squares are minus one (-1), 
	 * and degenerate ones as (0). The order of the array ALWAYS matches the order
	 * of the generators in the basis.
	 * <br>
	 * This array is kept to increase the speed of product calculations.
	 */
	private final byte[] nSignature;

	/**
	 * Main constructor of GProduct with signature information passed in. It
	 * figures out the rest of what it needs.
	 * <br>
	 * @param pSig String form of the signature. Looks like "-+++0".
	 * @throws BadSignatureException   Thrown when an invalid signature is found
	 */
	public GProduct(String pSig) throws BadSignatureException {
		this(Optional.ofNullable(null), pSig);
	}

	/**
	 * A re-use constructor of GProduct with signature and Basis passed in. It
	 * figures out the rest of what it needs.
	 * <br>
	 * The size of the signature string used to be checked using a static method
	 * on Basis, but that was duplicating the effort performed by CliffordProduct
	 * when it checks the validity of the string. Size and characters ARE checked.
	 * <br>
	 * @param pSig String form of the signature. Looks like "-+++0".
	 * @param pB   Optional Basis to re-use in constructing this product.
	 * @throws BadSignatureException   Thrown when an invalid signature is found
	 */
	public GProduct(Optional<Basis> pB, String pSig) throws BadSignatureException {
		if (!CliffordProduct.validateSignature(pSig))
			throw new BadSignatureException(this, "Valid signature required.");
		// ------Init signature
		nSignature = (pSig.length() == 0) ? new byte[1] : new byte[pSig.length()];
		int m = 0;
		for (char b : pSig.toCharArray()) {
			switch (b) {
			case '+' -> nSignature[m] = 1;
			case '0' -> nSignature[m] = 0;
			case '-' -> nSignature[m] = -1;
			}
			m++;
		}
		signature = pSig;
		// ------Get Basis
		canonBasis = (pB.isPresent()) ? pB.get() : GBuilder.createBasis((byte) pSig.length());
		// ------Build Product Table
		result = new int[getBladeCount()][getBladeCount()];
		canonBasis.bladeStream().parallel().forEach(bladeLeft -> {
			int row = canonBasis.find(bladeLeft) - 1;
			canonBasis.bladeStream().forEach(bladeRight -> {
				int col = canonBasis.find(bladeRight) - 1;
				Blade bMult = BladeDuet.simplify(bladeLeft, bladeRight, nSignature);
				result[row][col] = ((int) bMult.sign() != 0) ? 
							(int) bMult.sign() * (int) canonBasis.find(bMult)
							: 
							0;		//This case happens when the two blades share a generator that is degenerate.
			});
		});
	}

	/**
	 * Return a measure of whether blades pj and pk anticommute. Return a 1 if they
	 * anticommute. Return a 0 otherwise.
	 * <br>
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
	 * <br>
	 * @return Basis
	 */
	@Override
	public final Basis getBasis() {
		return canonBasis;
	}

	/**
	 * Get the linear dimension of the vector space that uses the associated Basis.
	 * <br>
	 * @return int
	 */
	@Override
	public final int getBladeCount() {
		return canonBasis.getBladeCount();
	}

	/**
	 * Return a measure of whether blades pj and pk commute. Return a 1 if they
	 * commute. Return a 0 otherwise.
	 * <br>
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
	 * <br>
	 * @return byte
	 */
	@Override
	public final byte getGradeCount() {
		return canonBasis.getGradeCount();
	}

	/**
	 * Get start and end index from the GradeRange array for grade pGrade.
	 * <br>
	 * There is currently no protection on this method. If someone asks for a grade
	 * that isn't in range, they WILL get -1 in the cells.
	 * <br>
	 * @param pGrade byte primitive = grade for which the range is needed
	 * @return int[] start and end indexes returned as a int[] array
	 */
	@Override
	public final int[] getGradeRange(byte pGrade) {
		int[] tR = new int[2];
		tR[0] = (int) canonBasis.getGradeStart(pGrade);
		tR[1] = (int) ((pGrade == canonBasis.getGradeCount() - 1) // is this MaxGrade? If so, top=bottom
				? tR[0]
				: (canonBasis.getGradeStart((byte) (pGrade + 1)) - 1));
		return tR;
	}

	@Override
	public final int[] getPScalarRange() {
		int[] tR = new int[2];
		tR[0] = canonBasis.getPScalarStart();
		tR[1] = tR[0];
		return tR;
	}

	/**
	 * Return row of result array. Meant for alternate multiplication methods.
	 * <br>
	 * @param pRow int
	 * @return int[][]
	 */
	public final int[] getResult(int pRow) {
		return result[pRow];
	}

	/**
	 * Return an element in the geometric multiplication result table.
	 * <br>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getResult(int pRow, int pCol) {
		return result[pRow][pCol];
	}

	/**
	 * This method takes two blades, finds their index values in the basis, and then retrieves
	 * the index value of their product from the Cayley table. With the result index, it return the 
	 * basis blade at that location.
	 * <br><br>
	 * One could just multiply the two blades using BladeDuet.simplify() and get the blade returned.
	 * That's how the Cayley table was constructed in the first place.
	 * <br><br>
	 * @param pRow 	Blade acting as the row entry for the Cayley table result
	 * @param pCol	Blade acting as the column entry for the Cayley table result
	 * @return Blade representing the result found in the Cayley table. If none found, null is returned.
	 */
	public final Blade getResult(Blade pRow, Blade pCol) {
		if (canonBasis.hasBlade(pRow) & canonBasis.hasBlade(pCol)) {
			int p = canonBasis.find(pRow) - 1;
			int q = canonBasis.find(pCol) - 1;	
			return canonBasis.getSingleBlade(	(getResult(p, q)==0) ? 0 : Math.abs(getResult(p, q)) - 1	);	
		}		
		return null;
	}

	/**
	 * Return the sign of an element in the geometric multiplication result table.
	 * <br>
	 * @param pRow int
	 * @param pCol int
	 * @return int
	 */
	@Override
	public final int getSign(int pRow, int pCol) {
		return (result[pRow][pCol] < 0) ? -1 : (result[pRow][pCol] > 0) ? 1 : 0;
	}

	/**
	 * This method takes two blades, finds their index values in the basis, and then retrieves
	 * the sign variation at that location in the Cayley table. 
	 * <br><br>
	 * @param pRow 	Blade acting as the row entry for the Cayley table result
	 * @param pCol	Blade acting as the column entry for the Cayley table result
	 * @return int representing the sign of the result found in the Cayley table. If none found, 0 is returned.
	 */
	public final int getSign(Blade pRow, Blade pCol) {
		if (canonBasis.hasBlade(pRow) & canonBasis.hasBlade(pCol)) {
			int p = canonBasis.find(pRow) - 1;
			int q = canonBasis.find(pCol) - 1;	
			return getSign(p, q);
			//return canonBasis.getSingleBlade(	(getResult(p, q)==0) ? 0 : Math.abs(getResult(p, q)) - 1	);	
		}		
		return 0;
	}


	/**
	 * Return the signature of the generating geometry. This lists the squares of the
	 * generators in their numeric order.
	 * <br>
	 * @return String
	 */
	@Override
	public final String signature() {
		return signature;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form.
	 * <br>
	 * @param pG A geometric product to be exported to XML
	 * @param indent A string to use for XML element intentation. Not required.
	 * @return String This is the XML string export of an object.
	 */
	public final static String toXMLString(GProduct pG, String indent) {
		if (indent == null)			indent = "\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<GProduct signature=\""+pG.signature()+"\">\n");
		rB	.append(Basis.toXMLString(pG.getBasis(), indent + "\t"));
		rB	.append(indent)
			.append("\t<CayleyTable rows=\"")
			.append(pG.getBladeCount())
			.append("\">\n");
		for (int k = 0; k < pG.getBladeCount(); k++) {		// Appending rows of the Cayley table
			rB	.append(indent)
				.append("\t\t<row id=\"")
				.append(k)
				.append("\" cells=\"");
			for (int m = 0; m < pG.getBladeCount(); m++)
				rB	.append(pG.getResult(k, m))
					.append(",");
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append(indent + "\t</CayleyTable>\n");
		rB.append(indent + "</GProduct>\n");
		return rB.toString();
	}

	/**
	 * I may need to come up with a better idea here. String comparisons might not be how 
	 * we should compare GP's. They should probably be size first and then string comparisons
	 * that are restricted to p, q, r measures.
	 * @param pIn GProduct the other GP to compare to this one
	 * @return int comparison of the two GProducts
	 */
	@Override
	public int compareTo(GProduct pIn) {
		return signature().compareTo(pIn.signature());
	}
}