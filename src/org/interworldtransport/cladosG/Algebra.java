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

/**
 * This interface represents the 'contract' obeyed by an Algebra in cladosG.
 * Most of the interface focuses upon gettor methods that return pieces of a
 * Algebra except for the scaling magnitudes represented by CladosF objects.
 * However, there are a few convenience methods too that support grade
 * information and ranges within the underlying basis.
 * 
 * The farther one gets from basic methods referencing Algebra components, the
 * more one is wandering off into conveniences.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public interface Algebra {
	/**
	 * This method returns the Algebra's name.
	 * 
	 * @return String
	 */
	public abstract String getAlgebraName();

	/**
	 * This is a short-hand method providing the blade count on the canonical basis.
	 * A Frame's blade count is limited at the upper end by this blade count.
	 * 
	 * @return short This is the size of a monad's coefficient array, but more
	 *         importantly it is the number of dimensions in the vector space
	 *         represented by the canonical basis.
	 */
	public abstract int getBladeCount();

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * 
	 * @return Foot
	 */
	public abstract Foot getFoot();

	/**
	 * Return the entire basis definition object.
	 * 
	 * @return CanonicalBasis
	 */
	public abstract CanonicalBasis getGBasis();

	/**
	 * Return the entire product definition object.
	 * 
	 * @return CliffordProduct
	 */
	public abstract CliffordProduct getGProduct();

	/**
	 * This is a short-hand method providing the grade count on the canonical basis.
	 * A Frame's grade count is limited at the upper end by this grade count.
	 * 
	 * @return byte This is the length of a monad's grade key. In an algebra with N
	 *         generators it will always be N+1.
	 */
	public abstract byte getGradeCount();

	/**
	 * This is a short-hand method providing where a particular grade starts and
	 * ends on the canonical basis. The GProduct is asked instead of the Basis
	 * because the basis tracks where they start. GProduct already knows where.
	 * 
	 * @param pInd short integer describing the grade to be selected from the basis.
	 * @return int[] This is an integer index between 0 and bladeCount inclusive.
	 */
	public abstract int[] getGradeRange(byte pInd);

	/**
	 * Resetting the algebra name is mildly useful when its purpose in a model
	 * shifts. Otherwise, it will probably not be used. Once it is set by the
	 * constructor, it will probably remain.
	 * 
	 * @param pName String for the Algebra's name.
	 */
	public abstract void setAlgebraName(String pName);

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
	public abstract void setFoot(Foot footPoint);

}
