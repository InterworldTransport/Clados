/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Algebra<br>
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
 * ---org.interworldtransport.cladosG.Algebra<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.UUID;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.UnitAbstract;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x UnitAbstract} except for the
 * actual field. That makes this a partial abstraction of an algebra. Once an
 * actual division field is in the mix we are there, but that structure is
 * reserved for the Monad class.
 * 
 * The primary data structures in a Algebra are a CanonicalBasis and a GProduct.
 * Between them they define the structure of operations an Algebra can support.
 * The basis provides for most behaviors people know from vector spaces. The
 * product provides the other behaviors people know from differential forms.
 * Together, though, they enable linear combinations of multi-ranked sums, thus
 * they step beyond familiar ground from forms and outer products AND the
 * familiar ground of scalar-only multiplication in vector spaces. All elements
 * of an algebra an participate in addition and multiplication and
 * multiplicative commutativity is NOT expected.
 * 
 * This isn't the place to explain what Clifford Algebras are and what they do.
 * This IS the place to point that that Clados extends the idea slightly in
 * order to support future uses.
 * 
 * 1. An Algebra references a 'Foot' object to imitate a location where the
 * algebra's geometry is expected to be a tangent space to some underlying
 * curved sub-manifold. No attempt at curvature is made here, but the Foot
 * object IS used in reference match tests. This is intentionally done to
 * prevent different tangent spaces being compared. In a model that assumes
 * curvature on the manifold, one must first transport their frame before making
 * comparisons. No 'transport' capability is written for Clados, but it might be
 * some day.
 * 
 * Anyone wanting to get around this feature need only declare one 'Foot' and
 * then re-use it everywhere. The computational penalty is miniscule.
 * 
 * 2. An Algebra has a CladosField mode. Whether the field is real or complex
 * matters. For computational reasons, the floating point precision technique in
 * use also matters. For those reasons, an Algebra maintains an internal mode
 * reference.
 * 
 * 3. An Algebra has a UnitAbstract element too in order to contain the Cardinal
 * within it and to use it combined with Mode to generate field numbers. This
 * might change in the future as the builder classes mature. It used to be used
 * as an operand in a copy function frequently in Monad in Clados V1.0, but is
 * largely bypassed in V2.0. If a complete bypass happens, the UnitAbstract element
 * may be reduced to it's contained Cardinal.
 * 
 * 4. There is a residual reference to a list of frame names with related
 * settors and gettors. This is changing in V2.0 as frames are better described
 * by linear combinations of basis elements, which makes them sets of Scale's.
 * Algebra's WILL track them, but by reference in a more complicated manner
 * since they will be used to 'cut out' the meanings of multiplication and
 * addition.
 * 
 * 5. There is a UUID string kept internally for use an XML variant of
 * serialization. It has no geometric meaning. Think of it as a digital name.
 * 
 * 6. There is also a 'name' string for the human readable name of an algebra.
 * It has no geometric meaning and is not used for anything important.
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Algebra implements Unitized, Modal, Comparable<Algebra> {
	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * 
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
		rB.append(indent).append("\t").append(pA.protoNumber.toXMLString()).append("\n");
		// -----------------------------------------------------------------------
		rB.append(indent).append("\t<Frames number=\"").append(pA.rFrames.size()).append("\" >\n");
		for (String tip : pA.rFrames)
			rB.append(indent).append("\t\t<Frame number=\"").append(pA.rFrames.indexOf(tip)).append("\" name=\"")
					.append(tip).append("\" />\n");
		rB.append(indent).append("\t</Frames>\n");
		// -----------------------------------------------------------------------
		rB.append(pA.getFoot().toXMLString(indent + "\t"));
		rB.append(pA.getGProduct().toXMLString(indent + "\t"));
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
	protected CanonicalBasis gBasis;
	/**
	 * The third among the common elements is the geometric product table formed by
	 * every product possible using members of the Eddington basis. This class has a
	 * few helper methods for dealing with symmetric and antisymmetric products and
	 * detection of other useful conditions.
	 */
	protected CliffordProduct gProduct;
	/**
	 * The algebras mode is the particular UnitAbstract child used to represent numbers.
	 */
	protected CladosField mode;
	/**
	 * Finally, the algebra has a name because this helps distinguish different
	 * reference frames associated with the same tangent point Foot.
	 */
	protected String name;
	/**
	 * The algebra's prototypical 'number'. A UnitAbstract suffices most of the time,
	 * but there is no issue with using a child of UnitAbstract.
	 */
	protected UnitAbstract protoNumber;
	/**
	 * This is the list of known frames defined against this Algebra.
	 */
	protected ArrayList<String> rFrames;
	/**
	 * Unique string (hopefully) that provides a machine readable name more likely
	 * to be unique. Used by apps that need more than the human readable name to
	 * avoid duplicating objects unnecessarily.
	 */
	protected String uuid;

	/**
	 * This is the constructor that assumes a full Algebra has already been
	 * constructed. This new one re-uses the objects in the one offered. No
	 * independent objects are made in this constructor except the algebra itself
	 * 
	 * THIS CONSTRUCTOR is one that enables algebras to function as light weight
	 * frames.
	 * 
	 * @param pS This is the Algebra's name
	 * @param pA This is the other Algebra to copy.
	 */
	public Algebra(String pS, Algebra pA) {
		this(pS, pA.getFoot(), pA.getCardinal(), pA.getGProduct());
	}

	/**
	 * This is the constructor that assumes a Foot, Cardinal, and GProduct have been
	 * instantiated. It appends the Cardinal to the Foot and points at the offered
	 * GProduct. It takes in one string for the algebra name as well and then
	 * produces the algebra. Nothing can be wrong with the signature since the
	 * GProduct is already constructed.
	 * 
	 * THIS CONSTRUCTOR is the one that most enables algebras to function as light
	 * weight frames. Two algebras can have different names but share everything
	 * else and cause reference matches to fail. The effect is that the canonical
	 * basis in both algebras is the same, but the name differences ensure the
	 * mismatch needed to prevent unphysical operations.
	 * 
	 * @param pS    This is the Algebra's name
	 * @param pF    This is the foot being offered for reference
	 * @param pCard This is the Cardinal to use as a protoNumber
	 * @param pGP   This is the geometric product being offered for reference
	 */
	public Algebra(String pS, Foot pF, Cardinal pCard, CliffordProduct pGP) {
		setAlgebraName(pS);
		protoNumber = new UnitAbstract(pCard);
		setFoot(pF);
		foot.appendCardinal(protoNumber.getCardinal());
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		rFrames = new ArrayList<String>(1);
		rFrames.add("canonical"); // Canonical basis is ALWAYS a frame
		uuid = UUID.randomUUID().toString();
	}

	/**
	 * This is the constructor that assumes a Foot has been instantiated, so it
	 * takes the ComplexD number type from there. It takes in two strings (one name
	 * and a product signature) and the Foot and produces an AlgebraComplexD. If
	 * anything is wrong with the signature it throws an exception. Any other error
	 * throws a general monad exception.
	 * 
	 * THIS CONSTRUCTOR is the one that enables algebras to function as medium
	 * weight frames. Two algebras can have different names and GProducts but share
	 * a Foot and cause reference matches to fail. This is the behavior necessary to
	 * prevent unintended operations between monads expressed using different
	 * signatures in their geometric products.
	 * 
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'number' in a nyad. Because the Foot is reused, the Cardinal will match
	 * and no field mismatches will happen. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad.
	 * [This may result in eliminating this class.]
	 * 
	 * @param pS    This is the Algebra's name
	 * @param pF    This is the foot being offered for reference
	 * @param pCard This is the Cardinal to use as a protoNumber
	 * @param pSig  This is the signature of the GProduct
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public Algebra(String pS, Foot pF, Cardinal pCard, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, pCard, CladosGBuilder.createGProduct(pSig));
	}

	/**
	 * This is the constructor that assumes a Foot and UnitAbstract have been
	 * instantiated. It takes in two strings (one name and a product signature), the
	 * Foot and Cardinal and produces an Algebra.
	 * 
	 * If anything is wrong with the signature it throws one of two exception,
	 * though both errors can be manifest in the signature string.
	 * 
	 * THIS CONSTRUCTOR is one that enables algebras to function as medium weight
	 * frames. Two algebras can have different names and GProducts but share a Foot
	 * and Cardinal causing reference matches to fail.
	 * 
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'scale' in a nyad. Because a Foot and Cardinal are reused, reference
	 * match tests within a nyad will pass. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad if
	 * so desired.
	 * 
	 * @param pS   This is the Algebra's name
	 * @param pF   This is the foot being offered for reference
	 * @param pSig This is the signature of the GProduct
	 * @param pDiv This is the UnitAbstract to imitate when the Foot tracks Cardinals
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public Algebra(String pS, Foot pF, String pSig, UnitAbstract pDiv)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, pDiv.getCardinal(), CladosGBuilder.createGProduct(pSig));
	}

	/**
	 * This is the raw constructor that assumes only the number type has been
	 * instantiated. It takes in three strings (two names and a product signature)
	 * and the example UnitAbstract and produces an Algebra. If anything is wrong with
	 * the signature it throws one of two exceptions.
	 * 
	 * This is the constructor that ensures algebra reference match failures even
	 * when exactly the same string names are used to construct all its parts.
	 * Because the Foot object is constructed within, the algebra will be distinct
	 * by definition.
	 * 
	 * @param pS        This is the Algebra's name
	 * @param pFootName This is the Foot's name
	 * @param pSig      This is the signature of the GProduct
	 * @param pF        This is the number type to use expressed as a UnitAbstract
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public Algebra(String pS, String pFootName, String pSig, UnitAbstract pF)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, CladosGBuilder.createFoot(pFootName, pF.getCardinalString()), pF.getCardinal(),
				CladosGBuilder.createGProduct(pSig));
	}

	/**
	 * This method appends a frame name to the list of known frames for this foot.
	 * It will silently terminate IF the frame is already in the list.
	 * 
	 * @param pRF String Reference Frame name to append
	 */
	public void appendFrame(String pRF) {
		if (rFrames.contains(pRF))
			return;
		rFrames.ensureCapacity(rFrames.size() + 1);
		rFrames.add(pRF);
	}

	/**
	 * This method is present to enable sorting of lists of algebras. It isn't
	 * critical in the geometric sense, but it might be useful in certain physical
	 * models.
	 * <p>
	 * 
	 * @param pAnother Algebra This is the algebra to be name compared
	 * @return int -1 if the name of 'this' algebra is 'less' than that of pAnother.
	 *         0 if the two names are the same +1 if the name of this algebra is
	 *         'greater' than that of pAnother.
	 */
	@Override
	public int compareTo(Algebra pAnother) {
		if (this.name == null)
			if (pAnother.name == null)
				return 0;
			else
				return +1; // A null name is considered larger than a non-null name
		else if (pAnother.name == null)
			return -1;
		else if (this.name.equals(pAnother.name))
			return 0;
		else // At this point neither name is null nor are they equal
		{
			char[] first = this.name.toCharArray();
			char[] second = pAnother.name.toCharArray();
			int loopLimit = (first.length <= second.length) ? first.length : second.length;
			for (int j = 0; j < loopLimit; j++) {
				if (first[j] < second[j])
					return -1;
				if (first[j] > second[j])
					return +1;
			}
			// At this point we know this.name and pAnother.name are
			// the same up through all characters in the smaller one.
			if (first.length < second.length)
				return -1;
			if (first.length > second.length)
				return +1;
			return 0; // The only way to get here is if they are actually equal.
						// Shouldn't happen since it was caught earlier, but
						// there is no harm catching it here too.
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Algebra other = (Algebra) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	/**
	 * This method returns the Algebra's name.
	 * 
	 * @return String
	 */
	public String getAlgebraName() {
		return name;
	}

	/**
	 * This is a short-hand method providing the blade count on the canonical basis.
	 * A Frame's blade count is limited at the upper end by this blade count.
	 * 
	 * @return short This is the size of a monad's coefficient array, but more
	 *         importantly it is the number of dimensions in the vector space
	 *         represented by the canonical basis.
	 */
	public int getBladeCount() {
		return gBasis.getBladeCount();
	}

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * 
	 * @return Foot
	 */
	public Foot getFoot() {
		return foot;
	}

	/**
	 * Simple gettor
	 * 
	 * This will change soon. Don't rely upon it.
	 * 
	 * @return ArrayList of string names for the frames
	 */
	public ArrayList<String> getFrames() {
		return rFrames;
	}

	/**
	 * Return the entire basis definition object.
	 * 
	 * @return gBasis
	 */
	public CanonicalBasis getGBasis() {
		return gBasis;
	}

	/**
	 * Return the entire product definition object.
	 * 
	 * @return gProduct
	 */
	public CliffordProduct getGProduct() {
		return gProduct;
	}

	/**
	 * This is a short-hand method providing the grade count on the canonical basis.
	 * A Frame's grade count is limited at the upper end by this grade count.
	 * 
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
	 * 
	 * @param pInd short integer describing the grade to be selected from the basis.
	 * @return int[] This is an integer index between 0 and bladeCount inclusive.
	 */
	public int[] getGradeRange(byte pInd) {
		return gProduct.getGradeRange(pInd);
	}

	/**
	 * Simple gettor for the kind of UnitAbstract in use in the algebra as a 'number.'
	 * 
	 * @return CladosField instance that matches the type of UnitAbstract in use
	 */
	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Simple gettor
	 * 
	 * This will change soon. Don't rely upon it.
	 * 
	 * @return ArrayList of string names for the frames
	 */
	public ArrayList<String> getReferenceFrames() {
		return rFrames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * This method removes a frame name from the list of known frames for this foot.
	 * If the frame is not found in the list, no action is taken. This silent
	 * failure is intentional.
	 * 
	 * @param pRF String Reference Frame name to remove
	 */
	public void removeFrame(String pRF) {
		rFrames.remove(pRF);
	}

	/**
	 * Resetting the algebra name is mildly useful when its purpose in a model
	 * shifts. Otherwise, it will probably not be used. Once it is set by the
	 * constructor, it will probably remain.
	 * 
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
	 * 
	 * @param footPoint Foot for the Algebra to use as its 'tangent' contact point.
	 */
	public void setFoot(Foot footPoint) {
		foot = footPoint;
	}

	/**
	 * Simple setter for the kind of UnitAbstract in use in the algebra as a 'number.'
	 * 
	 * @param pMode CladosField instance that matches the type of UnitAbstract in use
	 */
	public void setMode(CladosField pMode) {
		this.mode = pMode;
	}

	/**
	 * This is really just a gettor, but it reaches into the protoNumber and
	 * retrieves the Cardinal.
	 * 
	 * @return Cardinal of the protoNumber
	 */
	@Override
	public final Cardinal getCardinal() {
		return protoNumber.getCardinal();
	}

	/**
	 * This is really just a gettor for the protoNumber.
	 * 
	 * @return UnitAbstract protoNumber
	 */
	public final UnitAbstract getProtoNumber() {
		return protoNumber;
	}

	/**
	 * This method is a little dangerous and should use used only with great
	 * caution. Ideally, it would never be used because an algebra is defined
	 * relative to a tangent point and the coordinates there form both a geometric
	 * basis on which a geometric product is defined. Sometimes, however, one might
	 * find that two seemingly distinct feet are actually the same. In this limited
	 * case it should be possible to adjust an algebra to at a different gProduct
	 * after construction.
	 * 
	 * @param pGP GProduct GProduct object for the Algebra to use as its geometric
	 *            product operation on the canonical basis.
	 */
	protected void setGProduct(CliffordProduct pGP) {
		gProduct = pGP;
		gBasis = pGP.getBasis();
	}
}
