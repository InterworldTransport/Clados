/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Algebra<br>
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
 * ---org.interworldtransport.cladosG.Algebra<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.UUID;

import org.interworldtransport.cladosF.Field;			//Algebras are defined over fields
import org.interworldtransport.cladosF.Normalizable;	//Limit on usable fields
import org.interworldtransport.cladosF.ProtoN;			//Parent class of usable fields
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x ProtoN} except for the
 * actual field. That makes this a partial abstraction of an algebra. Once an
 * actual division field is in the mix we are there, but that structure is
 * reserved for the Monad class.
 * <br><br>
 * The primary data structures in a Algebra are a Basis and a GProduct.
 * Between them they define the structure of operations an Algebra can support.
 * The basis provides for most behaviors people know from vector spaces. The
 * product provides the other behaviors people know from differential forms.
 * Together, though, they enable linear combinations of multi-ranked sums, thus
 * they step beyond familiar ground from forms and outer products AND the
 * familiar ground of scalar-only multiplication in vector spaces. All elements
 * of an algebra an participate in addition and multiplication and
 * multiplicative commutativity is NOT expected.
 * <br><br>
 * This isn't the place to explain what Clifford Algebras are and what they do.
 * This IS the place to point that that Clados extends the idea slightly in
 * order to support future uses.
 * <br><br>
 * 1. An Algebra references a 'Foot' object to imitate a location where the
 * algebra's geometry is expected to be a tangent space to some underlying
 * curved sub-manifold. No attempt at curvature is made here, but the Foot
 * object IS used in reference match tests. This is intentionally done to
 * prevent different tangent spaces being compared. In a model that assumes
 * curvature on the manifold, one must first transport their frame before making
 * comparisons. No 'transport' capability is written for Clados, but it might be
 * some day.
 * <br><br>
 * Anyone wanting to get around this feature need only declare one 'Foot' and
 * then re-use it everywhere. The computational penalty is miniscule.
 * <br><br>
 * 2. There is a UUID string kept internally for use an XML variant of
 * serialization. It has no geometric meaning. Think of it as a digital name.
 * <br><br>
 * 3. There is also a 'name' string for the human readable name of an algebra.
 * It has no geometric meaning and is not used for anything important.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Algebra implements Comparable<Algebra> {
	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <br>
	 * @param pA     Algebra to be exported as XML data
	 * @param indent String of tab characters to assist with human readability of
	 *               output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public final static String toXMLString(Algebra pA, String indent) {
		if (indent == null)
			indent = "\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent).append("<Algebra UUID=\"").append(pA.uuid).append("\" >\n");
		rB.append(indent).append("\t<Name>").append(pA.getAlgebraName()).append("</Name>\n");
		// -----------------------------------------------------------------------
		rB.append(Foot.toXMLString(pA.getFoot(), indent + "\t"));
		rB.append(GProduct.toXMLString(pA.getGProduct(), indent + "\t"));
		rB.append(indent).append("</Algebra>\n");
		return rB.toString();
	}

	/**
	 * All algebra types share some elements that are not dependent on number types.
	 * The first among them is the 'tangent point' of the sub-manifold represented
	 * by the algebra. This is the Foot.
	 */
	protected Foot foot;

	/**
	 * The second among the common elements is the Eddington basis formed from all
	 * blades that can be produced through exterior products of generating
	 * 'coordinate' vectors. For N generators, there are 2^N blades.
	 */
	protected Basis gBasis;
	/**
	 * The third among the common elements is the geometric product table formed by
	 * every product possible using members of the Eddington basis. This class has a
	 * few helper methods for dealing with symmetric and antisymmetric products and
	 * detection of other useful conditions.
	 */
	protected GProduct gProduct;
	/**
	 * Finally, the algebra has a name because this helps distinguish different
	 * reference frames associated with the same Foot.
	 */
	protected String name;
	/**
	 * Unique string (hopefully) that provides a machine readable name more likely
	 * to be unique. Used by apps that need more than the human readable name to
	 * avoid duplicating objects unnecessarily.
	 * <br>
	 * 
	 */
	protected String uuid;

	/**
	 * This is the constructor that assumes a full Algebra has already been
	 * constructed. This new one re-uses the objects in the one offered. No
	 * independent objects are made in this constructor except the algebra itself
	 * <br>
	 * THIS CONSTRUCTOR is one that enables algebras to function as light weight frames.
	 * <br>
	 * @param pNewName 	This is the Algebra's name
	 * @param pA 		This is the other Algebra to copy.
	 */
	public Algebra(String pNewName, Algebra pA) {
		setAlgebraName(pNewName);
		setFoot(pA.getFoot());							//RE-USE of Foot
		setGProduct(pA.getGProduct());					//RE-USE of GP
		gBasis = pA.getGProduct().getBasis();			//RE-USE of Basis
		uuid = UUID.randomUUID().toString();
		GCache.INSTANCE.appendBasis(gBasis);
		GCache.INSTANCE.appendGProduct(gProduct);
	}

	/**
	 * This is the constructor that assumes a Foot and GProduct have been
	 * instantiated and will simply point at them. It takes in one string for 
	 * the algebra name as well and then produces the algebra. Nothing can be 
	 * wrong with the signature since the GProduct is already constructed.
	 * <br>
	 * THIS CONSTRUCTOR is the one that most enables algebras to function as light
	 * weight frames. Two algebras can have different names but share everything
	 * else and cause reference matches to fail. The effect is that the canonical
	 * basis in both algebras is the same, but the name differences ensure the
	 * mismatch needed to prevent unphysical operations.
	 * <br>
	 * @param pNewName	This is the Algebra's name
	 * @param pF    	This is the foot being offered for reference
	 * @param pGP   	This is the geometric product being offered for reference
	 */
	public Algebra(String pNewName, Foot pF, GProduct pGP) {
		setAlgebraName(pNewName);
		setFoot(pF);
		setGProduct(pGP);							//RE-USE of GP
		gBasis = pGP.getBasis();					//RE-USE of Basis
		uuid = UUID.randomUUID().toString();
		GCache.INSTANCE.appendBasis(gBasis);
		GCache.INSTANCE.appendGProduct(gProduct);
	}
	
	/**
	 * This is the constructor that assumes a Foot has been instantiated. 
	 * It takes in two strings (one name and a product signature) and the Foot 
	 * and produces an Algebra. If anything is wrong with the signature it throws 
	 * an exception. Any other error throws a general monad exception.
	 * <br>
	 * THIS CONSTRUCTOR is the one that enables algebras to function as medium
	 * weight frames. Two algebras can have different names and GProducts but share
	 * a Foot and cause reference matches to fail. This is the behavior necessary to
	 * prevent unintended operations between monads expressed using different
	 * signatures in their geometric products.
	 * <br>
	 * @param pNewName	This is the Algebra's name
	 * @param pF    	This is the foot being offered for reference
	 * @param pSig  	This is the signature of the GProduct
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public Algebra(String pNewName, Foot pF, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		this(	pNewName, 
				pF, 
				GBuilder.createGProduct(	GCache.INSTANCE.findBasis((byte) pSig.length()),
											pSig));
		GCache.INSTANCE.appendBasis(gBasis);
		GCache.INSTANCE.appendGProduct(gProduct);
	}

	/**
	 * This is the raw constructor that assumes only the number type has been
	 * instantiated. It takes in three strings (two names and a product signature)
	 * and the example ProtoN and produces an Algebra. If anything is wrong with
	 * the signature it throws one of two exceptions.
	 * <br>
	 * This is the constructor that ensures algebra reference match failures even
	 * when exactly the same string names are used to construct all its parts.
	 * Because the Foot object is constructed within, the algebra will be distinct
	 * by definition.
	 * <br>
	 * @param <D>  		This is the type of "Number" being offered
	 * @param pNewName	This is the Algebra's name
	 * @param pFootName This is the Foot's name
	 * @param pSig      This is the signature of the GProduct
	 * @param pF        This is the number type to use expressed as a ProtoN
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public <D extends ProtoN & Field & Normalizable>  Algebra(	String pNewName, 
																String pFootName, 
																String pSig, 
																D pF)
			throws BadSignatureException, GeneratorRangeException {
		
		this(	pNewName, 
				GBuilder.createFoot(		pFootName, 
											pF.getCardinalString()),
				GBuilder.createGProduct(	GCache.INSTANCE.findBasis((byte) pSig.length()),
											pSig));
		GCache.INSTANCE.appendBasis(gBasis);
		GCache.INSTANCE.appendGProduct(gProduct);
	}

	/**
	 * This method is present to enable sorting of lists of algebras. It isn't
	 * critical in the geometric sense, but it might be useful in certain physical
	 * models.
	 * <br>
	 * @param pAnother Algebra This is the algebra to be name compared
	 * @return int -1 if the name of 'this' algebra is 'less' than that of pAnother.
	 *         0 if the two names are the same +1 if the name of this algebra is
	 *         'greater' than that of pAnother.
	 */
	@Override
	public int compareTo(Algebra pAnother) {
		if (this.name == null)						return (pAnother.name == null) ? 0 : 1; 			//Null name is larger than a non-null name
		else if (pAnother.name == null)				return -1;	//Null name is larger than a non-null name
		else if (this.name.equals(pAnother.name))	return 0;	
		else {													//Neither name is null. Nor are they equal
			char[] first = this.name.toCharArray();
			char[] second = pAnother.name.toCharArray();
			int loopLimit = (first.length <= second.length) ? first.length : second.length;
			for (int j = 0; j < loopLimit; j++) {
				if (first[j] < second[j])			return -1;	//Character in first name is smaller. Decision done.
				if (first[j] > second[j])			return +1;	//Character in second name is smaller. Decision done.
			}													//No decision. Loop to the next character

			return (first.length < second.length) ? -1 : +1;	//We know the two names are the same up to the characters 
																//in the shorter one. We also know they can't be equal. 
																//So the longer one wins.
		}
	}
	/**
	 * Overridden Equals method from Object.
	 * This ensures reference equality is the standard. They must literally be the same object to be equal.
	 * @return boolean check for reference equality
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj) ? true : false;
	}

	/**
	 * This method returns the Algebra's name.
	 * <br>
	 * @return String name of the algebra
	 */
	public String getAlgebraName() {
		return name;
	}

	/**
	 * This is a short-hand method providing the blade count on the canonical basis.
	 * A Frame's blade count will be limited at the upper end by this blade count.
	 * <br>
	 * @return short This is the size of a monad's coefficient array, but more
	 *         importantly it is the number of dimensions in the vector space
	 *         represented by the canonical basis.
	 */
	public int getBladeCount() {
		return gBasis.getBladeCount();
	}

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * <br>
	 * @return Foot
	 */
	public Foot getFoot() {
		return foot;
	}

	/**
	 * Return the entire basis definition object.
	 * <br>
	 * @return gBasis
	 */
	public Basis getGBasis() {
		return gBasis;
	}

	/**
	 * Return the entire product definition object.
	 * <br>
	 * @return gProduct
	 */
	public GProduct getGProduct() {
		return gProduct;
	}

	/**
	 * This is a short-hand method providing the grade count on the canonical basis.
	 * A Frame's grade count will be limited at the upper end by this grade count.
	 * <br>
	 * @return byte This is the length of a monad's grade key. In an algebra with N
	 *         generators it will always be N+1.
	 */
	public byte getGradeCount() {
		return (byte) gBasis.getGradeCount();
	}

	/**
	 * This is a short-hand method providing where a particular grade starts and
	 * ends on the canonical basis. The GProduct is asked instead of the Basis
	 * because the basis tracks where they start. GProduct already knows where.
	 * <br>
	 * @param pInd short integer describing the grade to be selected from the basis.
	 * @return int[] This is an integer index between 0 and bladeCount inclusive.
	 */
	public int[] getGradeRange(byte pInd) {
		return gProduct.getGradeRange(pInd);
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	/**
	 * Resetting the algebra name is mildly useful when its purpose in a model
	 * shifts. Otherwise, it will probably not be used. Once it is set by the
	 * constructor, it will probably remain.
	 * <br>
	 * @param pName String for the Algebra's name.
	 */
	public void setAlgebraName(String pName) {
		name = pName;
	}

	/**
	 * This method is a little dangerous and should use used only with great
	 * caution. Ideally, it would never be used because an algebra is defined
	 * relative to a tangent point on a sub-manifold. Sometimes, however, one might
	 * find that two seemingly distinct feet are actually the same. In this limited
	 * case it should be possible for a model writer to adjust an algebra to point
	 * at a different foot after construction.
	 * <br>
	 * @param footPoint Foot for the Algebra to use as its 'tangent' contact point.
	 */
	public void setFoot(Foot footPoint) {
		foot = footPoint;
	}

	/**
	 * This method is a little dangerous and should use used only with great
	 * caution. Ideally, it would never be used because an algebra is defined
	 * relative to a tangent point and the coordinates there form both a geometric
	 * basis on which a geometric product is defined. Sometimes, however, one might
	 * find that two seemingly distinct feet are actually the same. In this limited
	 * case it should be possible to adjust an algebra to at a different gProduct
	 * after construction.
	 * <br>
	 * @param pGP GProduct GProduct object for the Algebra to use as its geometric
	 *            product operation on the canonical basis.
	 */
	protected void setGProduct(GProduct pGP) {
		gProduct = pGP;
		gBasis = pGP.getBasis();
	}
}
