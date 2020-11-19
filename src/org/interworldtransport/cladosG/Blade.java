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
	protected static final short FLIP = -1;
	protected static final short MAX = 14;
	protected static final short MIN = 0;
	private ArrayList<Generator> blade = new ArrayList<>(1);
	private long key = 0L;
	private int sign = 1;
	protected final short maxGrade; // This should be gradeCount-1 in a related basis

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
		this(pB.maxGrade);
		// blade.ensureCapacity(maxGrade); // Done by other constructor
		// Collections.copy(blade, pB.get()); // Can't? We need different instances?
		for (Generator pG : pB.get())
			blade.add(pG);
		sign = pB.sign();
		key = pB.key();
	}

	/**
	 * This is a minimal constructor that establishes the blade's future
	 * expectations regarding how many generators it might have to append to the
	 * blade list.
	 * 
	 * @param pMaxGrade short integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers.
	 */
	public Blade(short pMaxGrade) throws GeneratorRangeException {
		super();
		if (pMaxGrade < MIN | pMaxGrade > MAX)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pMaxGrade);
		maxGrade = pMaxGrade;
		blade.ensureCapacity(maxGrade);
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGrade
	 * expectations AND provides the short integer array of directions to load into
	 * the ArrayList. These shorts need not be sorted since this constructor uses
	 * the add() method which will handle sorting.
	 * 
	 * @param pMaxGrade short integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @param pDirs     short array containing directions to append to the blade's
	 *                  list.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers.
	 */
	public Blade(short pMaxGrade, Generator[] pDirs) throws GeneratorRangeException {
		this(pMaxGrade);
		for (Generator tG : pDirs)
			blade.add(tG);
		bubbleFlipSort();
		makeKey();
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGrade
	 * expectations AND provides the short integer array of directions to load into
	 * the ArrayList. These shorts need not be sorted since this constructor uses
	 * the add() method which will handle sorting.
	 * 
	 * @param pMaxGrade short integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @param pDirs     short array containing directions to append to the blade's
	 *                  list.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers.
	 */
	public Blade(short pMaxGrade, short[] pDirs) throws GeneratorRangeException {
		this(pMaxGrade);
		for (short tS : pDirs)
			blade.add(Generator.get(tS));
		bubbleFlipSort();
		makeKey();
	}

	/**
	 * The generator represents a 'direction' in the blade to be added. The blade is
	 * first checked to see if it is at maximu size or whether ghe generator is
	 * already in the list. It is then added to the blade list.
	 * 
	 * NO SORT of generators is performed. It is assumed here that the calling
	 * routine has knowledge that supercedes the need to sort the new generator into
	 * this blade.
	 * 
	 * @param pS Generator that will be added to the list with NO SORTING.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator pS) {
		if (isPScalar())
			return this;
		if (blade.contains(pS))
			return this;
		if (pS.ord > maxGrade) 
			return this;
		blade.add(pS);
		makeKey();
		return this;
	}

	public Blade add(Generator[] pS) {
		if (isPScalar())
			return this;

		for (Generator pT : pS) {
			if (blade.contains(pT))
				continue;
			blade.add(pT);
		}
		makeKey();
		return this;
	}

	/**
	 * The unboxed short represents a 'direction' in the blade to be added. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * NO SORT of generators is performed. It is assumed here that the calling
	 * routine has knowledge that supercedes the need to sort the new generator into
	 * this blade.
	 * 
	 * @param pS unboxed short integer that will be boxed immediate and passed to
	 *           the other version of this method.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See add(Short pS)
	 */
	public Blade add(short pS) throws GeneratorRangeException {
		add(Short.valueOf(pS));
		return this;
	}

	/**
	 * The boxed short represents a 'direction' in the blade to be added. It is
	 * first checked to see if the unboxed short is within the supported range.
	 * Next, the blade is checked to see if it already represents a pscalar. If it
	 * does, the blade's key is reset and nothing else is done. If not, the boxed
	 * short is added from the blade's list and the key is reset.
	 * 
	 * NO SORT of generators is performed. It is assumed here that the calling
	 * routine has knowledge that supercedes the need to sort the new generator into
	 * this blade.
	 * 
	 * @param pS Short is a boxed short integer representing the 'direction' to add
	 *           to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(Short pS) throws GeneratorRangeException {
		if (pS.shortValue() < Generator.MIN.ord | pS.shortValue() > Generator.MAX.ord)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		if (blade.size() >= MAX)
			throw new GeneratorRangeException("Max Generators for a blade is 14.");

		// If this is a pscalar, there is no way to add. Accept that and move on.
		if (isPScalar()) {
			makeKey();
			return this;
		}
		// Check if the generator is already in the list. If it is, just move on.
		for (Generator pt : blade)
			if (pt.ord == pS.shortValue())
				return this;

		blade.add(Generator.get(pS.shortValue()));
		// bubbleFlipSort();
		makeKey();

		return this;
	}

	/**
	 * An array of unboxed shorts representing 'directions' in the blade to be
	 * added. Each is first checked to see if the unboxed short is within the
	 * supported range. Next, the blade is checked to see if it already represents a
	 * pscalar. If it does, the blade's key is reset and nothing else more is done.
	 * If not, the boxed short is added from the blade's list and the key is reset.
	 * 
	 * NO SORT of generators is performed. It is assumed here that the calling
	 * routine has knowledge that supercedes the need to sort the new generator into
	 * this blade.
	 * 
	 * @param pS Short[] is an array of boxed short integers representing the
	 *           'directions' to add to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(short[] pS) throws GeneratorRangeException {
		for (short pt : pS)
			add(Short.valueOf(pt));
		return this;
	}

	/**
	 * An array of boxed shorts representing 'directions' in the blade to be added.
	 * Each is first checked to see if the unboxed short is within the supported
	 * range. Next, the blade is checked to see if it already represents a pscalar.
	 * If it does, the blade's key is reset and nothing else more is done. If not,
	 * the boxed short is added from the blade's list and the key is reset.
	 * 
	 * NO SORT of generators is performed. It is assumed here that the calling
	 * routine has knowledge that supercedes the need to sort the new generator into
	 * this blade.
	 * 
	 * @param pS Short[] is an array of boxed short integers representing the
	 *           'directions' to add to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(Short[] pS) throws GeneratorRangeException {
		for (Short pt : pS)
			add(pt);
		return this;
	}

	/**
	 * The generator represents a 'direction' in the blade to be added. The blade is
	 * first checked to see if it is at maximu size or whether ghe generator is
	 * already in the list. It is then added to the blade list and sorted.
	 * 
	 * @param pS Generator that will be added to the list with NO SORTING.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade addSort(Generator pS) {
		if (isPScalar())
			return this;
		if (blade.contains(pS))
			return this;
		blade.add(pS);
		bubbleFlipSort();
		makeKey();
		return this;
	}

	public Blade addSort(Generator[] pS) {
		if (isPScalar())
			return this;

		for (Generator pT : pS) {
			if (blade.contains(pT))
				continue;
			blade.add(pT);
		}
		bubbleFlipSort();
		makeKey();
		return this;
	}

	/**
	 * The unboxed short represents a 'direction' in the blade to be added. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * @param pS unboxed short integer that will be boxed immediate and passed to
	 *           the other version of this method.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See add(Short pS)
	 */
	public Blade addSort(short pS) throws GeneratorRangeException {
		addSort(Short.valueOf(pS));
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
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade addSort(Short pS) throws GeneratorRangeException {
		if (pS.shortValue() < Generator.MIN.ord | pS.shortValue() > Generator.MAX.ord)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		if (blade.size() >= MAX)
			throw new GeneratorRangeException("Max Generators for a blade is 14.");

		// If this is a pscalar, there is no way to add. Accept that and move on.
		if (isPScalar()) {
			makeKey();
			return this;
		}
		// Check if the generator is already in the list. If it is, just move on.
		for (Generator pt : blade)
			if (pt.ord == pS.shortValue())
				return this;

		blade.add(Generator.get(pS.shortValue()));
		bubbleFlipSort();
		makeKey();

		return this;
	}

	/**
	 * An array of unboxed shorts representing 'directions' in the blade to be
	 * added. Each is first checked to see if the unboxed short is within the
	 * supported range. Next, the blade is checked to see if it already represents a
	 * pscalar. If it does, the blade's key is reset and nothing else more is done.
	 * If not, the boxed short is added from the blade's list and the key is reset.
	 * 
	 * @param pS Short[] is an array of boxed short integers representing the
	 *           'directions' to add to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade addSort(short[] pS) throws GeneratorRangeException {
		for (short pt : pS)
			addSort(Short.valueOf(pt));
		return this;
	}

	/**
	 * An array of boxed shorts representing 'directions' in the blade to be added.
	 * Each is first checked to see if the unboxed short is within the supported
	 * range. Next, the blade is checked to see if it already represents a pscalar.
	 * If it does, the blade's key is reset and nothing else more is done. If not,
	 * the boxed short is added from the blade's list and the key is reset.
	 * 
	 * @param pS Short[] is an array of boxed short integers representing the
	 *           'directions' to add to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade addSort(Short[] pS) throws GeneratorRangeException {
		for (Short pt : pS)
			addSort(pt);
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
		if (key != ((Blade) obj).key)
			return false;
		if (sign != ((Blade) obj).sign)
			return false;
		// if (maxGrade != other.span) // Not needed because key encodes maxGrade
		// return false;
		return true;
	}

	/**
	 * This is just a getter method named to support calls from within streams.
	 * 
	 * @return key Returns the blade's ArrayList of boxed shorts.
	 */
	public ArrayList<Generator> get() {
		return blade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (key ^ (key >>> 32));
		result = prime * result + sign;
		result = prime * result + maxGrade;
		return result;
	}

	public boolean isOneBlade() {
		if (blade.size() == 1)
			return true;
		return false;
	}

	public boolean isPScalar() {
		if (blade.size() == maxGrade)
			return true;
		return false;
	}

	public boolean isScalar() {
		if (blade.isEmpty())
			return true;
		return false;
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
	 * @return Blade The blade itself is returned to support stream calls.
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
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to remove from the blade. For
	 *                                 example, trying to remove 22 or -5 will cause
	 *                                 this exception to be thrown.
	 */
	public Blade remove(Short pS) throws GeneratorRangeException {
		if (pS.shortValue() < MIN | pS.shortValue() > MAX)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		// If this is a scalar, there is nothing to remove. Accept that and move on.
		if (blade.isEmpty()) {
			key = 0L;
			return this;
		}
		blade.remove(Generator.get(pS.shortValue()));
		// No sort needed as the blade should already be in natural order and will
		// remain so after removal of a generator.
		makeKey();

		return this;
	}

	/**
	 * The generator represents a 'direction' in the blade to be removed. The blade
	 * is first checked to see if it already represents a scalar. If it does, the
	 * blade's key is set to zero and nothing else is done. If not, the generator is
	 * removed from the blade's list and the key is reset.
	 * 
	 * @param pS Generator representing the 'direction' to remove from the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade remove(Generator pS) {
		// If this is a scalar, there is nothing to remove. Accept that and move on.
		if (blade.isEmpty()) {
			key = 0L;
			return this;
		}
		blade.remove(pS);
		// No sort needed as the blade should already be in natural order and will
		// remain so after removal of a generator.
		makeKey();

		return this;
	}

	/**
	 * The unboxed short array represents 'directions' in the blade to be removed.
	 * If the blade is already a scalar, the method resets the key and returns.
	 * After that each is checked to see if the unboxed short is within the
	 * supported range. Next the generator is removed on the assumption that the
	 * list removal method will do NOTHING if the element is not found. Next, the
	 * blade is checked to see if it represents a scalar. If it does, the blade's
	 * key is set to zero and the method returns. If not, the loop proceeds to the
	 * next unboxed short.
	 * 
	 * @param pS Unboxed short integer array of directions to remove from this
	 *           blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(short[] pS) throws GeneratorRangeException {
		if (blade.isEmpty()) {
			key = 0L;
			return this;
		}
		for (short tS : pS) {
			if (tS < 1 | tS > MAX)
				throw new GeneratorRangeException("Index out of Range as a generator for blade.");

			blade.remove(Generator.get(tS));

			if (blade.isEmpty()) {
				key = 0L;
				return this;
			}
		}
		makeKey();
		return this;
	}

	/**
	 * The boxed short array represents 'directions' in the blade to be removed. If
	 * the blade is already a scalar, the method resets the key and returns. After
	 * that each is checked to see if the unboxed short is within the supported
	 * range. Next the generator is removed on the assumption that the list removal
	 * method will do NOTHING if the element is not found. Next, the blade is
	 * checked to see if it represents a scalar. If it does, the blade's key is set
	 * to zero and the method returns. If not, the loop proceeds to the next boxed
	 * short.
	 * 
	 * @param pS Boxed short integer array of directions to remove from this blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(Short[] pS) throws GeneratorRangeException {
		if (isScalar()) {
			key = 0L;
			return this;
		}
		for (Short tS : pS) {
			if (tS.shortValue() < 1 | tS.shortValue() > Basis.MAX_GEN)
				throw new GeneratorRangeException("Index out of Range as a generator for blade.");

			blade.remove(Generator.get(tS.shortValue()));

			if (blade.isEmpty()) {
				key = 0L;
				return this;
			}
		}
		makeKey();
		return this;
	}

	/**
	 * This method reverses the list of directions in the arrayList. Doing this
	 * causes the sign to flip sometimes, so this must be tracked.
	 * 
	 * A second list is created and then the directions are read from the first in
	 * reverse order and added to the second. Once the second list is filled, it is
	 * assigned to the first. AFTER that the sign flip is considered.
	 * 
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade reverse() {
		Collections.reverse(blade);
		// We get away with using Collections because we can calculate the sign flip.
		// sign does not flip for lists of size 0, 1,       4, 5,       8, 9,         12, 13
		// sign does     flip for lists of size       2, 3,       6, 7,       10, 11,         14
		sign *= FLIP * ((blade.size() % 4) / 2);
		makeKey();

		return this;
	}

	public int sign() {
		return sign;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<Blade sign=\"" + sign + "\" maxGrade=\"" + maxGrade + "\" key=\"" + key() + "\" generators=\"");
		for (short m = 0; m < blade.size(); m++)
			if (blade.get(m) != null)
				rB.append(blade.get(m).toString() + ",");
		if (blade.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	public String toXMLOrdString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<Blade sign=\"" + sign + "\" maxGrade=\"" + maxGrade + "\" key=\"" + key() + "\" generators=\"");
		for (short m = 0; m < blade.size(); m++)
			if (blade.get(m) != null)
				rB.append(blade.get(m).ord + ",");
		if (blade.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/*
	 * This is a Bubble Sort that keeps track of the number of swaps % 2. Swaps
	 * within the list should only occur with neighboring Shorts and only when the
	 * unboxed shorts imply that the previous Short is greater in value than the
	 * next.
	 */
	private void bubbleFlipSort() {
		if (isScalar() | isOneBlade())
			return;
		// Collections.sort(blade); // Can't do this because swaps must be tracked.
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
	 * Base (maxGrade+1) representation of Eddington Number
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
		for (Generator pT : blade)
			key += pT.ord * Math.pow(maxGrade + 1, blade.size() - 1 - blade.indexOf(pT));
	}
}
