/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Blade<br>
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
 * ---org.interworldtransport.cladosG.Blade<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * A Blade is essentially an outer product space built from 0 to many vectors.
 * If the vectors aren't parallel, the blade is of higher rank than a vector. At
 * this low level, though, there is no concept of an inner product, thus no
 * sense of 'parallel'. That leaves a blade as a 'list' of distinct directions
 * and a few supporting elements including an long integer key useful for
 * comparisons and short integer necessary for knowing how many possible
 * directions might be added to this blade.
 * 
 * The directions are simply boxed short integers. They are boxed instead of
 * unboxed in order to make use of ArrayList objects which can't 'list' java
 * primitives. At present, the supported number of 'directions' is 0 to 14, so
 * the performance penalty between boxed and unboxed short integers is minor.
 * 
 * The ArrayList keeps the Shorts in natural order associated with short
 * integers. If a new direction is added, the Blade will sort the Shorts from
 * lowest to highest immediately. This ensures later retrieval will always
 * deliver directions in the same order.
 * 
 * For example, if a problem states there are six possible directions, a Blade
 * will contain zero to six of them represented as boxed Shorts 1 through 6. If
 * only 3 are in the Blade (making it a 3-blade) then only 3 will be in the
 * ArrayList. If the ArrayList is empty, zero directions are contained and the
 * blade represents a scalar.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public final class Blade {
	private static final short FLIP = -1;
	private ArrayList<Short> blade = new ArrayList<>(1);
	private long key = 0L;
	private int sign = 1;
	private final short span; // This should be gradeCount-1 in a related basis

	/**
	 * This is a copy constructor that builds an identical blade with new boxed
	 * short integers containing the same short integer values.
	 * 
	 * @param pB The Blade to copy
	 * @throws GeneratorRangeException This can happen a few different ways, but
	 *                                 really really shouldn't since we are copying
	 *                                 another Blade here.
	 */
	public Blade(Blade pB) throws GeneratorRangeException {
		this(pB.span);
		blade.ensureCapacity(pB.get().size());
		for (Short pS : pB.get())
			blade.add(pS);
		makeKey();
	}

	/**
	 * This is a minimal constructor that establishes the blade's future
	 * expectations regarding how many generators it might have to append to the
	 * blade list.
	 * 
	 * @param pDim short integer for the number of possible directions that might
	 *             appear in this blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers.
	 */
	public Blade(short pDim) throws GeneratorRangeException {
		super();
		if (pDim < 0 | pDim > Basis.MAX_GEN)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pDim);
		span = pDim;
	}

	/**
	 * This is a maximal constructor that establishes the blade's future span
	 * expectations AND provides the short integer array of directions to load into
	 * the ArrayList. These shorts need not be sorted since this constructor uses
	 * the add() method which will handle sorting.
	 * 
	 * @param pDim  short integer for the number of possible directions that might
	 *              appear in this blade.
	 * @param pDirs short array containing directions to append to the blade's list.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers.
	 */
	public Blade(short pDim, short[] pDirs) throws GeneratorRangeException {
		this(pDim);
		for (short tS : pDirs)
			blade.add(tS);
	}

	/**
	 * The unboxed short represents a 'direction' in the blade to be added. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * @param pS unboxed short integer that will be boxed immediate and passed to
	 *           the other version of this method.
	 * @return ArrayList of Short produced after boxing the parameter and using
	 *         add(Short pS)
	 * @throws GeneratorRangeException See add(Short pS)
	 */
	public Blade add(short pS) throws GeneratorRangeException {
		add(Short.valueOf(pS));
		return this;
	}

	public Blade add(short[] pS) throws GeneratorRangeException {
		for (short pt : pS)
			add(Short.valueOf(pt));
		return this;
	}

	public Blade add(Short[] pS) throws GeneratorRangeException {
		for (Short pt : pS)
			add(pt);
		return this;
	}

	/**
	 * The boxed short represents a 'direction' in the blade to be added. It is
	 * first checked to see if the unboxed short is within the supported range.
	 * Next, the blade is checked to see if it already represents a pscalar. If it
	 * does, the blade's key is reset and nothing else is done. If not, the boxed
	 * short is added from the blade's list and the key is reset.
	 * 
	 * @param pS Short is a boxed short integer representing the 'direction' to add
	 *           to the blade.
	 * @return ArrayList of Short is the List representation of the blade's
	 *         directions.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(Short pS) throws GeneratorRangeException {
		if (pS.shortValue() < 1 | pS.shortValue() > Basis.MAX_GEN)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		if (blade.size() >= Basis.MAX_GEN)
			throw new GeneratorRangeException("Max Generators for a blade is 14.");

		// If this is a pscalar, there is no way to add. Accept that and move on.
		if (blade.size() == span) {
			makeKey();
			return this;
		}
		// Check if the unboxed short is already in the list. If it is, just move on.
		for (Short pt : blade)
			if (pt.shortValue() == pS.shortValue())
				return this;

		blade.ensureCapacity(blade.size() + 1);
		blade.add(pS);
		bubbleFlipSort();
		makeKey();

		return this;
	}

	/**
	 * The intended use for this method is with Comparators.
	 * 
	 * @param pIn Blade to be compared to this one
	 * @return int in {-1, 0, 1} depending on the numeric order of each blade's long
	 *         integer key.
	 */
	public int compareTo(Blade pIn) {
		if (key() < pIn.key())
			return -1;
		if (key() > pIn.key())
			return +1;
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blade other = (Blade) obj;
		if (key != other.key)
			return false;
		if (sign != other.sign)
			return false;
		if (span != other.span)
			return false;
		return true;
	}

	/**
	 * This is just a getter method named to support calls from within streams.
	 * 
	 * @return key Returns the blade's ArrayList of boxed shorts.
	 */
	public ArrayList<Short> get() {
		blade.trimToSize();
		return blade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (key ^ (key >>> 32));
		result = prime * result + sign;
		result = prime * result + span;
		return result;
	}

	/**
	 * This is just a getter method named to support calls from within streams.
	 * 
	 * @return key Returns the blade's long integer key.
	 */
	public long key() {
		return key;
	}

	/**
	 * The unboxed short represents a 'direction' in the blade to be removed. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * @param pS unboxed short integer that will be boxed immediate and passed to
	 *           the other version of this method.
	 * @return ArrayList of Short produced after boxing the parameter and using
	 *         remove(Short pS)
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(short pS) throws GeneratorRangeException {
		return remove(Short.valueOf(pS));
	}

	/**
	 * The boxed short represents a 'direction' in the blade to be removed. It is
	 * first checked to see if the unboxed short is within the supported range.
	 * Next, the blade is checked to see if it already represents a scalar. If it
	 * does, the blade's key is set to zero and nothing else is done. If not, the
	 * boxed short is removed from the blade's list and the key is reset.
	 * 
	 * @param pS Short is a boxed short integer representing the 'direction' to
	 *           remove from the blade.
	 * @return ArrayList of Short is the List representation of the blade's
	 *         directions.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to remove from the blade. For
	 *                                 example, trying to remove 22 or -5 will cause
	 *                                 this exception to be thrown.
	 */
	public Blade remove(Short pS) throws GeneratorRangeException {
		if (pS.shortValue() < 1 | pS.shortValue() > Basis.MAX_GEN)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		// If this is a scalar, there is nothing to remove. Accept that and move on.
		if (blade.size() == 0) {
			key = 0L;
			return this;
		}

		for (Short pt : blade)
			if (pt.shortValue() == pS.shortValue()) {
				blade.remove(pt);
				blade.trimToSize();
				break;
			}

		// No sort needed as the blade should already be in natural order and will
		// remain so after removal of a generator.
		makeKey();

		return this;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<Blade sign=\""+sign+"\" span=\"" + span + "\" key=\"" + key() + "\" generators=\"");
		for (short m = 0; m < blade.size(); m++)
			if (blade.get(m) != null)
				rB.append(blade.get(m).toString() + ",");
		if (blade.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/*
	 * This is a Bubble Sort that keeps track of the number of swaps % 2. Swaps
	 * within the list should only occur with neighboring Shorts and only when the
	 * unboxed shorts imply that the earlier Short is greater in value than the
	 * next.
	 */
	private void bubbleFlipSort() {
		if (blade.size() == 0 | blade.size() == 1)
			return;
		for (short m = 0; m < blade.size() - 1; m++) {
			for (short k = 0; k < blade.size() - m - 1; k++) {
				if (blade.get(k).compareTo(blade.get(k + 1)) > 0) {
					Collections.swap(blade, k, k + 1);
					sign *= FLIP;
				}
			}
		}
	}

	/*
	 * Base (span+1) representation of Eddington Number
	 * 
	 * Ex: 3 generators implies Base-4 keys stuffed into Base-10 number.
	 * 
	 * Last generator in the list is the one's digit, the next to last is the 4's
	 * digit and the one before that is the 16's digit. The actual short stored in
	 * the list is multiplied by that power
	 * 
	 * Ex: 8 generators implies Base-9 keys stuffed into a Base-10 number.
	 */
	private void makeKey() {
		key = 0L;
		for (Short pT : blade)
			key += pT.longValue() * Math.pow(span + 1, blade.size() - 1 - blade.indexOf(pT));
	}
}
