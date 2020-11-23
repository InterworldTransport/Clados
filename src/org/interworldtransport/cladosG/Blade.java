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

import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Stream;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * A Blade is essentially an outer product space built from 0 to many vectors.
 * If the vectors aren't parallel, the blade is of higher rank than a vector. At
 * this low level, though, there is no concept of an inner product, thus no
 * sense of 'parallel'. That leaves a blade as an ordered 'set' of distinct
 * directions and a few supporting elements including a long integer key useful
 * for comparisons and byte integer necessary for knowing how many possible
 * directions might ever be added to this blade.
 * 
 * The directions are simply Generators from an enumeration class. They are kept
 * in an EnumSet which uses as its sense of order the same order generators are
 * enumerated in their class. At present, the supported number of 'directions'
 * is 0 to 14, so the enumeration class lists 14 possible generators.
 * 
 * The EnumSet keeps Generators in their natural order. If a new direction is
 * added, the EnumSet will handle it 'late' in the computational sense. In other
 * words, it doesn't matter where the new generator gets added. It matters ONLY
 * when generators are iterated later when establishing a product table or
 * generating a blade key. Iterators will always deliver directions in the same
 * order.
 * 
 * For example, if a sub-manifold has six possible directions, a Blade will
 * contain zero to six of them represented as generators E1 through E6. If only
 * 3 are in the Blade (making it a 3-blade) then only E1, E2, and E3 will be in
 * the EnumSet. If the EnumSet is empty, zero directions are contained and the
 * blade represents a scalar.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public final class Blade {
	protected static final byte FLIP = -1;
	protected static final byte MAX = 14;
	protected static final byte MIN = 0;
	protected static final long MAX_KEY = 2234152501943159L;
	private EnumSet<Generator> blade;
	private long key = 0L;
	private byte sign = 1;
	protected final byte maxGrade; // This should be gradeCount-1 in a related basis

	/**
	 * This is a copy constructor that builds an identical blade with new boxed byte
	 * integers containing the same byte integer values.
	 * 
	 * @param pB The Blade to copy
	 * @throws GeneratorRangeException This can happen a few different ways, but
	 *                                 really really shouldn't since we are copying
	 *                                 another Blade here.
	 */
	public Blade(Blade pB) throws GeneratorRangeException {
		this(pB.maxGrade);
		blade = EnumSet.noneOf(Generator.class);
		for (Generator pG : pB.get())
			blade.add(pG);
		sign = pB.sign();
		key = pB.key();
	}

	/**
	 * This is a minimal constructor that establishes the blade's future
	 * expectations regarding how many generators it might have to add to the set.
	 * 
	 * @param pMaxGrade byte integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGrade) throws GeneratorRangeException {
		super();
		if (pMaxGrade < MIN | pMaxGrade > MAX)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pMaxGrade);
		blade = EnumSet.noneOf(Generator.class);
		maxGrade = pMaxGrade;
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGrade
	 * expectations AND provides an array of directions to load into the blade.
	 * 
	 * @param pMaxGrade byte integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @param pDirs     Generator[] containing directions to append to the blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGrade, Generator[] pDirs) throws GeneratorRangeException {
		this(pMaxGrade);
		Stream.of(pDirs).forEach(g -> blade.add(g));
		makeKey();
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGrade
	 * expectations AND provides the byte integer array of directions to load into
	 * the ArrayList. These bytes need not be sorted since this constructor uses the
	 * add() method which will handle sorting.
	 * 
	 * @param pMaxGrade byte integer for the number of possible directions that
	 *                  might appear in this blade.
	 * @param pDirs     byte array containing directions to append to the blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGrade, byte[] pDirs) throws GeneratorRangeException {
		this(pMaxGrade);
		for (byte tS : pDirs)
			blade.add(Generator.get(tS));
		makeKey();
	}

	/**
	 * The generator represents a 'direction' in the blade to be added. The blade is
	 * checked to see if it is at maximum size and whether the offered generator is
	 * beyond masGrade. If either fails, the add silently returns the Blade
	 * unchanged. If both pass, the generator is added to the set.
	 * 
	 * @param pS Generator that will be added to the set.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator pS) {
		if (isPScalar())
			return this;
		else if (pS.ord > maxGrade)
			return this;
		else {
			blade.add(pS);
			makeKey();
			return this;
		}
	}

	/**
	 * The generators represent 'directions' in the blade to be added. The blade is
	 * checked to see if it is at maximum size. If it is, the add silently returns
	 * the Blade unchanged. If it passes, the generators are added to the set if
	 * they pass through the filter that blocks generators larger than maxGrade.
	 * 
	 * @param pS Generators that will be added to the set.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator[] pS) {
		if (isPScalar())
			return this;
		else {
			Stream.of(pS).filter(g -> g.ord <= maxGrade).forEach(g -> blade.add(g));
			makeKey();
			return this;
		}
	}

	/**
	 * The unboxed byte represents a 'direction' in the blade to be added. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * @param pS unboxed byte integer that will be boxed immediate and passed to the
	 *           other version of this method.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See add(Byte pS)
	 */
	public Blade add(byte pS) throws GeneratorRangeException {
		add(Byte.valueOf(pS));
		return this;
	}

	/**
	 * The boxed byte represents a 'direction' in the blade to be added. It is first
	 * checked to see if the unboxed byte is within the supported range. Next, the
	 * blade is checked to see if it already represents a pscalar. If it does, the
	 * method silently returns doing nothing. If not, the boxed byte is added to the
	 * blade's set (which de-duplicates) and the key is reset.
	 * 
	 * @param pS Boxed byte representing the 'direction' to add to the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a byte integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to add 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(Byte pS) throws GeneratorRangeException {
		if (pS.byteValue() < Generator.MIN.ord | pS.byteValue() > Generator.MAX.ord)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");
		else if (isPScalar())
			return this;
		else {
			blade.add(Generator.get(pS.byteValue()));
			makeKey();
			return this;
		}
	}

	/**
	 * An array of unboxed bytes representing 'directions' in the blade to be added.
	 * Each is immediately boxed and passed to the similarly named method for Bytes.
	 * 
	 * @param pS Array of boxed byte integers representing the 'directions' to add.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a byte integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to add 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(byte[] pS) throws GeneratorRangeException {
		for (byte pt : pS)
			add(Byte.valueOf(pt)); // Do NOT convert to Generator here. Validate first.
		return this;
	}

	/**
	 * An array of boxed bytes representing 'directions' in the blade to be added.
	 * Each is first checked to see if the unboxed byte is within the supported
	 * range. Next, the blade is checked to see if it already represents a pscalar.
	 * If it does, the blade's key is reset and nothing else more is done. If not,
	 * the boxed byte is added from the blade's list and the key is reset.
	 * 
	 * @param pS Array of boxed byte integers representing the 'directions' to add.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a byte integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade add(Byte[] pS) throws GeneratorRangeException {
		for (Byte pt : pS)
			add(pt); // Do NOT convert to Generator here. Let the other validate first.
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
		return (key() < pIn.key()) ? -1 : (key() > pIn.key() ? 1 : 0);
	}

	/**
	 * This method is overridden Object equality test. As long as blades are being
	 * tested, what is needed to pass this test is for them to have the same key and
	 * sign values.
	 * 
	 * NOTE that the maximum possible grade for a blade is encoded in the key, thus
	 * it gets tested when key values are compared. The sign value could be too, but
	 * there is an advantage when building product tables NOT to have negative keys.
	 * 
	 * @param obj The object to test
	 * @return boolean True implies two blades are the same. False implies they
	 *         aren't.
	 */
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
		return true;
	}

	/**
	 * This method is very similar to the base object's equality test. The
	 * difference is the sign of the blade is not checked. As long as blades are
	 * being tested, all that is needed to pass this test is for them to have the
	 * same key value.
	 * 
	 * @param obj The object to test
	 * @return boolean True implies two blades are equal to within a sign while
	 *         False implies they aren't equal even when signs are ignored.
	 */
	public boolean equalsAbs(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (key != ((Blade) obj).key)
			return false;
		return true;
	}

	/**
	 * This is just a getter method named to support calls from within streams.
	 * 
	 * @return key Returns the blade's ArrayList of boxed bytes.
	 */
	public EnumSet<Generator> get() {
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
		return (blade.size() == 1);
	}

	public boolean isPScalar() {
		if (blade.isEmpty())
			return false;
		else
			return (blade.size() == maxGrade);
	}

	public boolean isScalar() {
		return blade.isEmpty();
	}

	/**
	 * This is just a getter method named to support consumers at the end of streams
	 * of blades. This is how one gets a stream of blade keys.
	 * 
	 * @return key Returns the blade's long integer key.
	 */
	public long key() {
		return key;
	}

	/**
	 * The generator represents a 'direction' in the blade to be removed. If
	 * anything is found to be removed, the key is recomputed.
	 * 
	 * @param pS Generator representing the 'direction' to remove from the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade remove(Generator pS) {
		if (blade.remove(pS))
			makeKey();
		return this;
	}

	/**
	 * The unboxed byte represents a 'direction' in the blade to be removed. It is
	 * immediately boxed and delivered to the similarly named method for handling.
	 * When that method returns the new blade list, this one finishes by returning
	 * it to the calling object.
	 * 
	 * @param pS unboxed byte integer that will be boxed immediate and passed to the
	 *           other version of this method.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(byte pS) throws GeneratorRangeException {
		return remove(Byte.valueOf(pS));
	}

	/**
	 * The boxed byte represents a 'direction' in the blade to be removed. It is
	 * first checked to see if the unboxed byte is within the supported range. If
	 * anything is found to be removed, the key is recomputed.
	 * 
	 * @param pS Short is a boxed byte integer representing the 'direction' to
	 *           remove from the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException This occurs when a byte integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to remove from the blade. For
	 *                                 example, trying to remove 22 or -5 will cause
	 *                                 this exception to be thrown.
	 */
	public Blade remove(Byte pS) throws GeneratorRangeException {
		if (pS.byteValue() < MIN + 1 | pS.byteValue() > MAX)
			throw new GeneratorRangeException("Unsupported Generator for Blade.");

		if (blade.remove(Generator.get(pS.byteValue())))
			makeKey();
		return this;
	}

	/**
	 * The unboxed byte array represents 'directions' in the blade to be removed. If
	 * the blade is already a scalar, the method resets the key and returns. After
	 * that each is checked to see if the unboxed byte is within the supported
	 * range. Next the generator is removed on the assumption that the list removal
	 * method will do NOTHING if the element is not found. Next, the blade is
	 * checked to see if it represents a scalar. If it does, the blade's key is set
	 * to zero and the method returns. If not, the loop proceeds to the next unboxed
	 * byte.
	 * 
	 * @param pS Unboxed byte integer array of directions to remove from this blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(byte[] pS) throws GeneratorRangeException {
		if (isScalar()) {
			return this;
		} else
			for (byte tS : pS)
				remove(Byte.valueOf(tS));
		// No need to re-compute key here. It was done by the other remove method.
		return this;
	}

	/**
	 * The boxed byte array represents 'directions' in the blade to be removed. If
	 * the blade is already a scalar, the method resets the key and returns. After
	 * that each is checked to see if the unboxed byte is within the supported
	 * range. Next the generator is removed on the assumption that the list removal
	 * method will do NOTHING if the element is not found. Next, the blade is
	 * checked to see if it represents a scalar. If it does, the blade's key is set
	 * to zero and the method returns. If not, the loop proceeds to the next boxed
	 * byte.
	 * 
	 * @param pS Boxed byte integer array of directions to remove from this blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 * @throws GeneratorRangeException See remove(Short pS)
	 */
	public Blade remove(Byte[] pS) throws GeneratorRangeException {
		if (isScalar()) {
			return this;
		} else
			for (Byte tS : pS)
				remove(tS);
		// No need to re-compute key here. It was done by the other remove method.
		return this;
	}

	public byte sign() {
		return sign;
	}

	public String toXMLOrdString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		//Iterator<Generator> cursor = blade.iterator();
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<Blade sign=\"" + sign + "\" maxGrade=\"" + maxGrade + "\" key=\"" + key()
				+ "\" generators=\"");
		
		blade.stream().forEachOrdered(g -> rB.append(g.ord + ","));
		//while (cursor.hasNext())
		//	rB.append(cursor.next().ord + ",");

		if (blade.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		//Iterator<Generator> cursor = blade.iterator();
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<Blade sign=\"" + sign + "\" maxGrade=\"" + maxGrade + "\" key=\"" + key()
				+ "\" generators=\"");
		
		blade.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		//while (cursor.hasNext())
		//	rB.append(cursor.next().toString() + ",");

		if (blade.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/*
	 * Base (maxGrade+1) representation of Eddington Number
	 * 
	 * Ex: 3 generators implies Base-4 keys stuffed into Base-10 number.
	 * 
	 * Last generator in the list is the one's digit, the next to last is the 4's
	 * digit and the one before that is the 16's digit. The actual byte stored in
	 * the list is multiplied by that power
	 * 
	 * Ex: 8 generators implies Base-9 keys stuffed into a Base-10 number.
	 */
	private void makeKey() {
		key = 0L;
		int counter = 0;
		Iterator<Generator> cursor = blade.iterator();
		while (cursor.hasNext()) {
			key += cursor.next().ord * Math.pow((maxGrade + 1), (blade.size() - 1 - counter));
			counter++;
		}
	}

	protected Blade setSign(byte pSign) {
		sign = pSign;
		return this;
	}
}
