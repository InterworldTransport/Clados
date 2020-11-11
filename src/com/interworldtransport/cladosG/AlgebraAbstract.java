/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
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
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x DivField} except the actual
 * field. That makes this an abstraction of an algebra.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class AlgebraAbstract {
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
	 * reference frames associated with the same tangent point Foot.
	 */
	protected String name;
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
	 * @param pAnother AlgebraAbstract This is the algebra to be name compared
	 * @return int -1 if the name of 'this' algebra is 'less' than that of pAnother.
	 *         0 if the two names are the same +1 if the name of this algebra is
	 *         'greater' than that of pAnother.
	 */
	public int compareTo(AlgebraAbstract pAnother) {
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
	public short getBladeCount() {
		return gBasis.getBladeCount();
	}

	/**
	 * Delivers an integer stream of the blades contained in the underlying basis
	 * ranged from scalar to pscalar. (0 to bladeCount)
	 * 
	 * @return IntStream
	 */
	public IntStream getBladeStream() {
		return gBasis.getBladeStream();
	}

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * 
	 * @return Foot
	 */
	public Foot getFoot() {
		return foot;
	}

	public ArrayList<String> getFrames() {
		return rFrames;
	}

	/**
	 * Return the entire basis definition object.
	 * 
	 * @return gBasis
	 */
	public Basis getGBasis() {
		return gBasis;
	}

	/**
	 * Return the entire product definition object.
	 * 
	 * @return gProduct
	 */
	public GProduct getGProduct() {
		return gProduct;
	}

	/**
	 * This is a short-hand method providing the grade count on the canonical basis.
	 * A Frame's grade count is limited at the upper end by this grade count.
	 * 
	 * @return short This is the length of a monad's grade key. In an algebra with N
	 *         generators it will always be N+1.
	 */
	public short getGradeCount() {
		return gBasis.getGradeCount();
	}

	/**
	 * This is a short-hand method providing where a particular grade starts and
	 * ends on the canonical basis. The GProduct is asked instead of the Basis
	 * because the basis tracks where they start. GProduct already knows where.
	 * 
	 * @param pInd short integer describing the grade to be selected from the basis.
	 * @return short This is an integer index between 0 and bladeCount inclusive.
	 */
	public short[] getGradeRange(short pInd) {
		return gProduct.getGradeRange(pInd);
	}

	/**
	 * Delivers an integer stream of the grades contained in the underlying basis
	 * ranged from scalar to pscalar. (0 to gradeCount)
	 * 
	 * @return IntStream
	 */
	public IntStream getGradeStream() {
		return gBasis.getGradeStream();
	}

	public ArrayList<String> getReferenceFrames() {
		return rFrames;
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
	 * @param footPoint Foot object for the Algebra to use as its 'tangent' contact
	 *                  point.
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
	 * 
	 * @param pGP GProduct GProduct object for the Algebra to use as its geometric
	 *            product operation on the canonical basis.
	 */
	protected void setGProduct(GProduct pGP) {
		gProduct = pGP;
		gBasis = pGP.getBasis();
	}
}
