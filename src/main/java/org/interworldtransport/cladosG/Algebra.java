/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
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
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosF.UnitAbstract;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x UnitAbstract} except for the
 * actual field. That makes this a partial abstraction of an algebra. Once an
 * actual division field is in the mix we are there, but that structure is
 * reserved for the Monad class.
 * <p>
 * The primary data structures in a Algebra are a CanonicalBasis and a GProduct.
 * Between them they define the structure of operations an Algebra can support.
 * The basis provides for most behaviors people know from vector spaces. The
 * product provides the other behaviors people know from differential forms.
 * Together, though, they enable linear combinations of multi-ranked sums, thus
 * they step beyond familiar ground from forms and outer products AND the
 * familiar ground of scalar-only multiplication in vector spaces. All elements
 * of an algebra an participate in addition and multiplication and
 * multiplicative commutativity is NOT expected.
 * <p>
 * This isn't the place to explain what Clifford Algebras are and what they do.
 * This IS the place to point that that Clados extends the idea slightly in
 * order to support future uses.
 * <p>
 * 1. An Algebra references a 'Foot' object to imitate a location where the
 * algebra's geometry is expected to be a tangent space to some underlying
 * curved sub-manifold. No attempt at curvature is made here, but the Foot
 * object IS used in reference match tests. This is intentionally done to
 * prevent different tangent spaces being compared. In a model that assumes
 * curvature on the manifold, one must first transport their frame before making
 * comparisons. No 'transport' capability is written for Clados, but it might be
 * some day.
 * <p>
 * Anyone wanting to get around this feature need only declare one 'Foot' and
 * then re-use it everywhere. The computational penalty is miniscule.
 * <p>
 * 2. An Algebra has a CladosField mode. Whether the field is real or complex
 * matters. For computational reasons, the floating point precision technique in
 * use also matters. For those reasons, an Algebra maintains an internal mode
 * reference.
 * <p>
 * 3. An Algebra has a UnitAbstract element too in order to contain the Cardinal
 * within it and to use it combined with Mode to generate field numbers. This
 * might change in the future as the builder classes mature. It used to be used
 * as an operand in a copy function frequently in Monad in Clados V1.0, but is
 * largely bypassed in V2.0. If a complete bypass happens, the UnitAbstract element
 * may be reduced to it's contained Cardinal.
 * <p>
 * 4. There is a residual reference to a list of frame names with related
 * settors and gettors. This is changing in V2.0 as frames are better described
 * by linear combinations of basis elements, which makes them sets of Scale's.
 * Algebra's WILL track them, but by reference in a more complicated manner
 * since they will be used to 'cut out' the meanings of multiplication and
 * addition.
 * <p>
 * 5. There is a UUID string kept internally for use an XML variant of
 * serialization. It has no geometric meaning. Think of it as a digital name.
 * <p>
 * 6. There is also a 'name' string for the human readable name of an algebra.
 * It has no geometric meaning and is not used for anything important.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Algebra implements Unitized, Modal, Comparable<Algebra> {
	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <p>
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
	 * but there is no issue with using a child of UnitAbstract. Just be careful because
	 * setting protoNumber with a child of UnitAbstract should also set the Mode.
	 * <p>
	 * This is where the primary cardinal for an algebra is found.
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
	 * <p>
	 * 
	 */
	protected String uuid;

	/**
	 * This is the constructor that assumes a full Algebra has already been
	 * constructed. This new one re-uses the objects in the one offered. No
	 * independent objects are made in this constructor except the algebra itself
	 * <p>
	 * THIS CONSTRUCTOR is one that enables algebras to function as light weight
	 * frames.
	 * <p>
	 * @param pS This is the Algebra's name
	 * @param pA This is the other Algebra to copy.
	 */
	public Algebra(String pS, Algebra pA) {
		this(pS, pA.getFoot(), pA.getGProduct(), pA.getCardinal());
		setMode(pA.getMode());
	}

	/**
	 * This is the constructor that assumes a Foot, Cardinal, and GProduct have been
	 * instantiated. It appends the Cardinal to the Foot and points at the offered
	 * GProduct. It takes in one string for the algebra name as well and then
	 * produces the algebra. Nothing can be wrong with the signature since the
	 * GProduct is already constructed.
	 * <p>
	 * THIS CONSTRUCTOR is the one that most enables algebras to function as light
	 * weight frames. Two algebras can have different names but share everything
	 * else and cause reference matches to fail. The effect is that the canonical
	 * basis in both algebras is the same, but the name differences ensure the
	 * mismatch needed to prevent unphysical operations.
	 * <p>
	 * Note that the constructed algebra has no declared Mode yet.
	 * <p>
	 * @param pS    This is the Algebra's name
	 * @param pF    This is the foot being offered for reference
	 * @param pCard This is the Cardinal to use as a protoNumber
	 * @param pGP   This is the geometric product being offered for reference
	 */
	public Algebra(String pS, Foot pF, CliffordProduct pGP, Cardinal pCard) {
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
	 * takes the CladosF number type from there. It takes in two strings (one name
	 * and a product signature) and the Foot and produces an Algebra. If
	 * anything is wrong with the signature it throws an exception. Any other error
	 * throws a general monad exception.
	 * <p>
	 * THIS CONSTRUCTOR is the one that enables algebras to function as medium
	 * weight frames. Two algebras can have different names and GProducts but share
	 * a Foot and cause reference matches to fail. This is the behavior necessary to
	 * prevent unintended operations between monads expressed using different
	 * signatures in their geometric products.
	 * <p>
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'number' in a nyad. Because the Foot is reused, the Cardinal will match
	 * and no field mismatches will happen. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad.
	 * [This may result in eliminating this class.]
	 * <p>
	 * Note that the constructed algebra has no declared Mode yet.
	 * <p>
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
	public Algebra(String pS, Foot pF, String pSig, Cardinal pCard)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, GBuilder.createGProduct(pSig), pCard);
	}

	/**
	 * This is the constructor that assumes a Foot and UnitAbstract have been
	 * instantiated. It takes in two strings (one name and a product signature), the
	 * Foot and Cardinal and produces an Algebra.
	 * <p>
	 * If anything is wrong with the signature it throws one of two exception,
	 * though both errors can be manifest in the signature string.
	 * <p>
	 * THIS CONSTRUCTOR is one that enables algebras to function as medium weight
	 * frames. Two algebras can have different names and GProducts but share a Foot
	 * and Cardinal causing reference matches to fail.
	 * <p>
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'scale' in a nyad. Because a Foot and Cardinal are reused, reference
	 * match tests within a nyad will pass. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad if
	 * so desired.
	 * <p>
	 * @param <D>
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
	public <D extends UnitAbstract & Field & Normalizable> Algebra(String pS, Foot pF, String pSig, D pDiv)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, GBuilder.createGProduct(pSig), pDiv.getCardinal());
		protoNumber = UnitAbstract.copyMaybe((D) pDiv).get();
		setMode(pDiv);
	}

	/**
	 * This is the raw constructor that assumes only the number type has been
	 * instantiated. It takes in three strings (two names and a product signature)
	 * and the example UnitAbstract and produces an Algebra. If anything is wrong with
	 * the signature it throws one of two exceptions.
	 * <p>
	 * This is the constructor that ensures algebra reference match failures even
	 * when exactly the same string names are used to construct all its parts.
	 * Because the Foot object is constructed within, the algebra will be distinct
	 * by definition.
	 * <p>
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
	public <D extends UnitAbstract & Field & Normalizable>  Algebra(String pS, String pFootName, String pSig, D pF)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, GBuilder.createFoot(pFootName, pF.getCardinalString()),
				GBuilder.createGProduct(pSig), pF.getCardinal());
		protoNumber = UnitAbstract.copyMaybe((D) pF).get();
		setMode(pF);
	}

	/**
	 * This method appends a frame name to the list of known frames for this foot.
	 * It will silently terminate IF the frame is already in the list.
	 * <p>
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
			return -1; // A null name is considered larger than a non-null name
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
			/* We know this.name and pAnother.name are the same up to all characters
			*  in the smaller one. We also know they can't be equal.
			*/
			if (first.length < second.length)
				return -1;
			else return +1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		return false;
	}

	/**
	 * This method returns the Algebra's name.
	 * <p>
	 * @return String
	 */
	public String getAlgebraName() {
		return name;
	}

	/**
	 * This is a short-hand method providing the blade count on the canonical basis.
	 * A Frame's blade count is limited at the upper end by this blade count.
	 * <p>
	 * @return short This is the size of a monad's coefficient array, but more
	 *         importantly it is the number of dimensions in the vector space
	 *         represented by the canonical basis.
	 */
	public int getBladeCount() {
		return gBasis.getBladeCount();
	}

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * <p>
	 * @return Foot
	 */
	public Foot getFoot() {
		return foot;
	}

	/**
	 * Simple gettor
	 * <p>
	 * This will change soon. Don't rely upon it.
	 * <p>
	 * @return ArrayList of string names for the frames
	 */
	public ArrayList<String> getFrames() {
		return rFrames;
	}

	/**
	 * Return the entire basis definition object.
	 * <p>
	 * @return gBasis
	 */
	public CanonicalBasis getGBasis() {
		return gBasis;
	}

	/**
	 * Return the entire product definition object.
	 * <p>
	 * @return gProduct
	 */
	public CliffordProduct getGProduct() {
		return gProduct;
	}

	/**
	 * This is a short-hand method providing the grade count on the canonical basis.
	 * A Frame's grade count is limited at the upper end by this grade count.
	 * <p>
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
	 * <p>
	 * @param pInd short integer describing the grade to be selected from the basis.
	 * @return int[] This is an integer index between 0 and bladeCount inclusive.
	 */
	public int[] getGradeRange(byte pInd) {
		return gProduct.getGradeRange(pInd);
	}

	/**
	 * Simple gettor for the kind of UnitAbstract in use in the algebra as a 'number.'
	 * <p>
	 * @return CladosField instance that matches the type of UnitAbstract in use
	 */
	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Simple gettor
	 * <p>
	 * This will change soon. Don't rely upon it.
	 * <p>
	 * @return ArrayList of string names for the frames
	 */
	public ArrayList<String> getReferenceFrames() {
		return rFrames;
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	/**
	 * This method removes a frame name from the list of known frames for this foot.
	 * If the frame is not found in the list, no action is taken. This silent
	 * failure is intentional.
	 * <p>
	 * @param pRF String Reference Frame name to remove
	 */
	public void removeFrame(String pRF) {
		rFrames.remove(pRF);
	}

	/**
	 * Resetting the algebra name is mildly useful when its purpose in a model
	 * shifts. Otherwise, it will probably not be used. Once it is set by the
	 * constructor, it will probably remain.
	 * <p>
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
	 * <p>
	 * @param footPoint Foot for the Algebra to use as its 'tangent' contact point.
	 */
	public void setFoot(Foot footPoint) {
		foot = footPoint;
	}

	/**
	 * Simple setter for the algebra's mode that uses an offered mode.
	 * <p>
	 * @param pMode CladosField instance that matches the type of UnitAbstract in use
	 */
	public void setMode(CladosField pMode) {
		this.mode = pMode;
	}

	/**
	 * Simple setter for the algebra's mode that uses a child of UnitAbstract.
	 * to figure out the mode.
	 * <p>
	 * @param <D> pNumber must be a child of UnitAbstract for anything to happen here.
	 */
	public <D extends UnitAbstract & Field & Normalizable> void setMode(D pNumber) {
		protoNumber = FBuilder.copyOf(pNumber);
		if (pNumber instanceof RealF) setMode(CladosField.REALF);
		else if (pNumber instanceof RealD) setMode(CladosField.REALD);
		else if (pNumber instanceof ComplexF) setMode(CladosField.COMPLEXF);
		else if (pNumber instanceof ComplexD) setMode(CladosField.COMPLEXD);
	}

	/**
	 * This is really just a gettor, but it reaches into the protoNumber and
	 * retrieves the Cardinal.
	 * <p>
	 * @return Cardinal of the protoNumber
	 */
	@Override
	public final Cardinal getCardinal() {
		return protoNumber.getCardinal();
	}

	/**
	 * This is really just a gettor for the protoNumber.
	 * <p>
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
	 * <p>
	 * @param pGP GProduct GProduct object for the Algebra to use as its geometric
	 *            product operation on the canonical basis.
	 */
	protected void setGProduct(CliffordProduct pGP) {
		gProduct = pGP;
		gBasis = pGP.getBasis();
	}
}
