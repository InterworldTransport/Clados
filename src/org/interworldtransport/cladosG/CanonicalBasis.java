/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CanonicalBasis<br>
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
 * ---org.interworldtransport.cladosG.CanonicalBasis<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.TreeMap;

import org.interworldtransport.cladosGExceptions.BladeOutOfRangeException;

/**
 * This interface represents the 'contract' obeyed by a canonical basis in
 * cladosG. Most of the interface focuses upon gettor methods that return blade
 * information, but there are a few convenience methods too that support grade
 * information and ranges as detected by the number of generators found in a
 * blade. Finally there are also a few methods for dealing with keys, basis size
 * validation, and XML output.
 * 
 * The farther one gets from basic methods dealing with blades and span, the
 * more one is wandering off into conveniences. The various keys matter, but as
 * computational short-cuts for product tables having to deal with very large
 * vector spaces.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public interface CanonicalBasis {

	/**
	 * This method detects whether or not the number of generators being used is
	 * currently supported by Clados. The point for it is to prevent internal errors
	 * in other classes.
	 * 
	 * For example, someone attempted to create a basis with a negative number of
	 * generators should be stopped up front without having to check for that
	 * situation themselves.
	 * 
	 * The trickier one, though, involves a number of generators that will blow up
	 * an internal representation in a basis or CliffordProduct class. For example,
	 * trying to build a basis with 64 generators will create a list of 2E64
	 * blades... or at least the library will try if the size isn't validated first.
	 * Perhaps the list representation of a Basis can hold that many Blades, but the
	 * keys identifying them might not if they rely on java primitives. Also, the
	 * related CliffordProduct holds a product table of all blades. Its internal
	 * representation might break too.
	 * 
	 * This method provides a single place to validate the intended size of a basis
	 * WITHOUT placing magic numbers in this interface. The actual limits are in the
	 * CladosConstant class as static elements.
	 * 
	 * @param pGens int This is the number of generators of the basis.
	 * @return TRUE returned if the number of generators is in the supported range.
	 *         FALSE otherwise.
	 */
	public static boolean validateSize(int pGens) {
		return (pGens >= CladosConstant.SCALARGRADE & pGens <= CladosConstant.MAXGRADE);
	}

	/**
	 * Old-fashioned 'find' method that reports index location in the basis where a
	 * Blade is found OR -1 if it wasn't found.
	 * 
	 * @param pIn Blade to be found
	 * @return integer index pointing to element of a list containing the Blade OR
	 *         -1 if the blade wasn't found.
	 */
	public abstract int find(Blade pIn);

	/**
	 * Return the number of independent blades in the basis. This is the same as the
	 * linear dimension of an algebra that uses this basis.
	 * 
	 * @return int
	 */
	public abstract int getBladeCount();

	/**
	 * There is a similar sounding method in the old basis that isn't being used, so
	 * it is replaced by this one that returns the enumerated set of generators in
	 * the requested blade.
	 * 
	 * @param p1 integer pointing to the blade in the internal list
	 * @return EnumSet of Generator representing the blade without the context
	 *         necessary for knowing much about the enclosing space for the blade.
	 * @throws BladeOutOfRangeException This exception is thrown when the integer
	 *                                  index would result in an IndexOutOfRange
	 *                                  exception on an array of blades.
	 */
	public abstract EnumSet<Generator> getBladeSet(int p1) throws BladeOutOfRangeException;

	/**
	 * Return the number of grades in the basis. Since there is no geometry in the
	 * basis this is a measure of the number of distinct generator subset types that
	 * can be formed where the element count determines the type. Because the empty
	 * set includes no generators, GradeCount will always be one more than the
	 * number of generators.
	 * 
	 * @return byte
	 */
	public abstract byte getGradeCount();

	/**
	 * This method simply delivers the otherwise private grade range list. Useful
	 * for testing purposes, but should be avoided as much as possible.
	 * 
	 * @return ArrayList of Integers A list of grades boxed as Integers.
	 */
	public abstract ArrayList<Integer> getGrades();

	/**
	 * Get an index to the first blade of grade specified by the parameter.
	 * 
	 * @param p1 byte This is for choosing which grade index range to return.
	 * @return int Index within the basis where requested grade starts.
	 */
	public abstract int getGradeStart(byte p1);

	/**
	 * Return the long at p1 in the EddingtonKey array.
	 * 
	 * @param p1 short This is the desired key at p1 .
	 * @return long
	 * @throws BladeOutOfRangeException This exception is thrown if the requested
	 *                                  blade is NOT between 0 and bladeCount
	 *                                  inclusive.
	 */
	public abstract long getKey(int p1) throws BladeOutOfRangeException;

	/**
	 * Simple gettor method for the Map connecting Blade keys to their indexed
	 * position in a basis.
	 * 
	 * @return TreeMap of Long blade keys and their Integer indexed positions in the
	 *         internal list of blades in this basis.
	 */
	public abstract TreeMap<Long, Integer> getKeyIndexMap();

	/**
	 * This is a special version of getGradeStart() that finds the highest grade.
	 * 
	 * @return int Index within the basis where pscalar grade starts.
	 */
	public abstract int getPScalarStart();

	/**
	 * Simple gettor method retrieves the Blade at the indexed position in the
	 * Basis.
	 * 
	 * @param p1 integer index
	 * @return Blade at the indexed position.
	 */
	public abstract Blade getSingleBlade(int p1);

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String
	 */
	public abstract String toXMLString(String indent);

	/**
	 * This is a validator detects blade out of range issues. If one tries to name a
	 * blade by its index, it is always possible for the offered integer to be out
	 * of range.
	 * 
	 * @param pIn Short representing the integer index of the blade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	public abstract boolean validateBladeIndex(short pIn);

}
