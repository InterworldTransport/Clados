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
import java.util.Optional;
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
 * @version 2.0
 * @author Dr Alfred W Differ
 *
 */
public final class Blade implements Comparable<Blade> {
	/**
	 * Nothing fancy here. Just giving a name to -1 for use when flipping signs when
	 * generators are transposed. This is to make it clear WHY a sign flip occurs.
	 * Human readability is the issue.
	 */
	protected static final byte FLIP = -1;

	/**
	 * Deliver a blade of the size specified by the byte integer.
	 * 
	 * A check is made of the parameter that could fail resulting in no blade being
	 * returned. That's why an Optional of Blade is returned. That check is made
	 * deeper in with the constructor, though.
	 * 
	 * @param pMaxGen This is the byte integer representation of the largest
	 *                genertor that will be used in this blade being created.
	 * @return Blade is returned. 
	 */
	public final static Blade createBlade(byte pMaxGen) {
		Blade returnIt;
		try {
			returnIt = new Blade(pMaxGen);
		} catch (GeneratorRangeException e) {
			returnIt = null;
		}
		return returnIt;

	}

	/**
	 * This method is very similar to createBlade(byte) but because it uses an
	 * actual generator it can bypass the safety check for support validity. The
	 * Generator enumeration is assumed to have ONLY generators that can be
	 * supported by internal representations of blades, bases, and products.
	 * 
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with no generators contained. Basically, a scalar in the
	 *         implied space.
	 */
	public final static Blade createBlade(Generator pGen) {
		return new Blade(pGen);
	}

	/**
	 * Deliver a blade of the size specified by the byte integer that is also filled
	 * with the necessary number of generators to make it a pscalar.
	 * 
	 * A check is made of the parameter that could fail resulting in no blade being
	 * returned. That's why an Optional of Blade is returned.
	 * 
	 * @param pMaxGen This is the byte integer representation of the largest
	 *                genertor that will be used in this blade being created.
	 * @return Optional Blade is returned. If Blade was constructed it is contained
	 *         within the Optional.
	 * 
	 *         public final static Optional<Blade> createPScalarBlade(byte pMaxGen)
	 *         { if (CanonicalBasis.validateSize(pMaxGen)) { Blade returnIt = null;
	 *         if (pMaxGen == 0) { try { returnIt = new Blade((byte) 0,
	 *         EnumSet.noneOf(Generator.class)); } catch (GeneratorRangeException e)
	 *         { returnIt = null; } } else { try { returnIt = new Blade(pMaxGen,
	 *         EnumSet.range(Generator.E1, Generator.get(pMaxGen))); } catch
	 *         (GeneratorRangeException e) { returnIt = null; } } return
	 *         Optional.of(returnIt); } return Optional.of(null); }
	 */
	/**
	 * This method is very similar to createPScalarBlade(byte) but because it uses
	 * an actual generator it can bypass the safety check for support validity. The
	 * Generator enumeration is assumed to have ONLY generators that can be
	 * supported by internal representations of blades, bases, and products.
	 * 
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with all generators contained. Basically, a pscalar in the
	 *         implied space.
	 */
	public final static Blade createPScalarBlade(Generator pGen) {
		Blade returnIt = new Blade(pGen);
		Generator.flow(pGen.ord).forEach(g -> returnIt.add(g));
		return returnIt;
	}

	/**
	 * This is a specialty name that does the same thing as createBlade(), but
	 * allows for humans to correctly read the intend of a developer when the result
	 * Blade is expected to be a scalar.
	 * 
	 * @param pMaxGen This is the byte integer representation of the largest
	 *                genertor that will be used in this blade being created.
	 * @return Optional Blade is returned. If Blade was constructed it is contained
	 *         within the Optional.
	 * 
	 *         public final static Optional<Blade> createScalarBlade(byte pMaxGen) {
	 *         return Blade.createBlade(pMaxGen); }
	 */
	/**
	 * This is a specialty name that does the same thing as createBlade(gen), but
	 * allows for humans to correctly read the intend of a developer when the result
	 * Blade is expected to be a scalar.
	 * 
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with no generators contained. Basically, a scalar in the
	 *         implied space.
	 */
	public final static Blade createScalarBlade(Generator pGen) {
		return createBlade(pGen);
	}

	/**
	 * Simple grade tester. Does the Blade contain 'n' generators?
	 * 
	 * @param blade Blade to be tested
	 * @param n     grade value
	 * @return TRUE if the blade has a number of generators matching the grade value
	 *         being tested. FALSE otherwise.
	 */
	public final static boolean isNBlade(Blade blade, byte n) {
		return blade.getGenerators().size() == n;
	}

	/**
	 * Simple grade tester. Does the Blade contain ALL generators for the implied
	 * container basis?
	 * 
	 * The maximum generator of the implied space could be ZERO causing scalars and
	 * pscalars to be the same. This method will return TRUE in that case too.
	 * 
	 * @param blade Blade to be tested
	 * @return TRUE if the blade all the generators implied by its own maxGen value.
	 *         FALSE otherwise.
	 */
	public final static boolean isPScalar(Blade blade) {
		return (blade.getGenerators().size() == blade.maxGen);
	}

	/**
	 * Simple grade tester. Does the Blade contain NO generators?
	 * 
	 * The maximum generator of the implied space could be ZERO causing scalars and
	 * pscalars to be the same. This method will return TRUE in that case too.
	 * 
	 * @param blade Blade to be tested
	 * @return TRUE if the blade has no generators. FALSE otherwise.
	 */
	public final static boolean isScalar(Blade blade) {
		return blade.getGenerators().isEmpty();
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Blade in a human readable form.
	 * 
	 * This variation uses a Generator's ordinal to name it in the generator list.
	 * 
	 * @param blade  The Blade to be exported to XML.
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String The XML formated String representing the Blade.
	 */
	public final static String toXMLOrdString(Blade blade, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent).append("<Blade key=\"").append(blade.key()).append("\" bitKey=\"0b").append(blade.bitKey())
				.append("\" sign=\"").append(blade.sign()).append("\" generators=\"");

		blade.getGenerators().stream().forEachOrdered(gen -> rB.append(gen.ord).append(","));

		if (blade.getGenerators().size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Blade in a human readable form.
	 * 
	 * This variation uses a Generator's name in the generator list.
	 * 
	 * @param blade  The Blade to be exported to XML.
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String The XML formated String representing the Blade.
	 */
	public final static String toXMLString(Blade blade, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent).append("<Blade key=\"").append(blade.key()).append("\" bitKey=\"0b")
				.append(Integer.toBinaryString(blade.bitKey())).append("\" sign=\"").append(blade.sign())
				.append("\" generators=\"");

		blade.getGenerators().stream().forEachOrdered(g -> rB.append(g.toString()).append(","));

		if (blade.getGenerators().size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/**
	 * bitKey is the base-2 representation of the blade's generators.
	 * 
	 * For example... a blade using E2 will have +2 added to the bitKey because the
	 * ordinal for E2 is 2 and the key builder method adds (1<<(E2.ord -1)) for this
	 * generator. For E5 (ord=5) bitKey picks up a contribution of 2^4.
	 */
	private int bitKey = 0;

	/**
	 * This is the internal representation of the generators involved in the blade.
	 * If a generator is found in the EnumSet, it is part of the blade.
	 * 
	 * For example, a set holding E1, E4, and E9 implies this is the E1,E4,E9 blade.
	 */
	private EnumSet<Generator> blade;

	/**
	 * This is the old key representing the blade that is sure to increase in a way
	 * that ensures the correct sort order in comparisons between blades. This
	 * sorting happens most often when constructing a basis.
	 */
	private long key = 0L;

	/**
	 * This is the maximum expected size of the internal EnumSet for the blade. It
	 * is also the ordinal of the largest generator one expects to find in the
	 * EnumSet.
	 * 
	 * Once set, this value should never change.
	 */
	private final byte maxGen; // This should be gradeCount-1 in a related basis

	/**
	 * This byte integer would be a single bit as it is never expected to be
	 * anything other than +1 or -1. It represents whether the blade has been
	 * inverted or not. Blades do NOT have a sense of magnitude, so this inversion
	 * is ONLY about the order of the generators in the EnumSet.
	 * 
	 * When this is +1, the blade is assumed to be in a state where the EnumSet
	 * represents the natural order of generators OR in a state where an even number
	 * of transpositions have occurred (after all pairs of transpostions that would
	 * cancel each other are removed) away from the natural order.
	 * 
	 * When this is -1, the blade is assumed to be in a state where the EnumSet
	 * represents an odd number of transpositions (after canceling pairs are
	 * removed) away from the natural order of the generators in the set.
	 */
	private byte sign = 1;

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
		this(pB.maxGen);
		blade.addAll(pB.getGenerators());
		sign = pB.sign();
		key = pB.key();
		bitKey = pB.bitKey();
	}

	/**
	 * This is a minimal constructor that establishes the blade's future
	 * expectations regarding how many generators it might have to add to the set.
	 * 
	 * @param pMaxGen byte integer for the number of possible directions that might
	 *                appear in this blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGen) throws GeneratorRangeException {
		if (!CanonicalBasis.validateSize(pMaxGen))
			throw new GeneratorRangeException("Unsupported Size for Blade " + pMaxGen);
		blade = EnumSet.noneOf(Generator.class);
		maxGen = pMaxGen;
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen
	 * expectations AND provides an array of directions to load into the blade.
	 * 
	 * @param pMaxGen byte integer for the number of possible directions that might
	 *                appear in this blade.
	 * @param pDirs   Generator[] containing directions to append to the blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGen, EnumSet<Generator> pDirs) throws GeneratorRangeException {
		this(pMaxGen);
		pDirs.forEach(g -> blade.add(g));
		makeKey();
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen
	 * expectations AND provides the byte integer array of directions to load into
	 * the ArrayList. These bytes need not be sorted since this constructor uses the
	 * add() method which will handle sorting.
	 * 
	 * @param pMaxGen byte integer for the number of possible directions that might
	 *                appear in this blade.
	 * @param pDirs   byte array containing directions to append to the blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	//public Blade(byte pMaxGen, byte[] pDirs) throws GeneratorRangeException {
	//	this(pMaxGen);
	//	for (byte tS : pDirs)
	//		blade.add(Generator.get(tS));
	//	makeKey();
	//}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen
	 * expectations AND provides an array of directions to load into the blade.
	 * 
	 * @param pMaxGen byte integer for the number of possible directions that might
	 *                appear in this blade.
	 * @param pDirs   Generator[] containing directions to append to the blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 byte integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for byte
	 *                                 integers.
	 */
	public Blade(byte pMaxGen, Generator[] pDirs) throws GeneratorRangeException {
		this(pMaxGen);
		Stream.of(pDirs).forEach(g -> blade.add(g));
		makeKey();
	}

	/**
	 * Simplest constructor that establishes a Blade by naming directly the largest
	 * generator that will ever be used within it. This sets the internal size
	 * without having to validate an integer passed in to determine if it is in the
	 * supported range because Generator enumeration is assumed ALL supported.
	 * 
	 * @param pMaxGen Generator that is the largest of the possible directions that
	 *                might appear in this blade.
	 */
	public Blade(Generator pMaxGen) {
		blade = EnumSet.noneOf(Generator.class);
		maxGen = pMaxGen.ord;
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
	//public Blade add(byte pS) throws GeneratorRangeException {
	//	add(Byte.valueOf(pS));
	//	return this;
	//}

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
	//public Blade add(Byte pS) throws GeneratorRangeException {
	//	if (pS.byteValue() < CladosConstant.GENERATOR_MIN.ord | pS.byteValue() > CladosConstant.GENERATOR_MAX.ord)
	//		throw new GeneratorRangeException("Index out of Range as a generator for blade.");
	//	else if (isPScalar(this) | pS.byteValue() > maxGen)
	//		return this;
	//	else {
	//		if (blade.add(Generator.get(pS.byteValue())))
	//			makeKey();
	//		return this;
	//	}
	//}

	/**
	 * This 'add' method assumes an entire enumerated set of generators has been
	 * produced elsewhere and all of the set elements are to be added to this blade.
	 * 
	 * A check is performed first to see if the blade is already a pscalar. If it
	 * is, it silently returns and does nothing. If it isn't, the method tries to
	 * add all set elements even if doing so might make the blade a pscalar along
	 * the way. Because the blade's internal representation relies on an EnumSet, it
	 * de-duplicates generators added here.
	 * 
	 * @param pS An EnumSet of Generators to add to this blade
	 * @return this blade
	 */
	public Blade add(EnumSet<Generator> pS) {
		if (isPScalar(this))
			return this;
		else {
			blade.addAll(pS);
			return this;
		}
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
		if (isPScalar(this) | pS.ord > maxGen)
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
	 * they pass through the filter that blocks generators larger than maxGen.
	 * 
	 * @param pS Generators that will be added to the set.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator[] pS) {
		if (isPScalar(this))
			return this;
		else {
			Stream.of(pS).filter(g -> g.ord <= maxGen).forEach(g -> blade.add(g));
			makeKey();
			return this;
		}
	}

	/**
	 * This is just a getter method named to support consumers at the end of streams
	 * of blades. This is how one gets a stream of blade keys.
	 * 
	 * @return key Returns the blade's bit integer key.
	 */
	public int bitKey() {
		return bitKey;
	}

	/**
	 * The intended use for this method is with Comparators.
	 * 
	 * @param pIn Blade to be compared to this one
	 * @return int in {-1, 0, 1} depending on the numeric order of each blade's long
	 *         integer key.
	 */
	@Override
	public int compareTo(Blade pIn) {
		return (key() < pIn.key()) ? -1 : (key() > pIn.key() ? 1 : 0);
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
		if (maxGen != other.maxGen)
			return false;
		if (sign != other.sign)
			return false;
		return true;
	}

	/**
	 * This method is very similar to the base object's equality test. The
	 * difference is the sign of the blade is not checked. As long as blades are
	 * being tested, all that is needed to pass this test is for them to have the
	 * same key and maxGen values.
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
		if (maxGen != ((Blade) obj).maxGen)
			return false;
		return true;
	}

	/**
	 * This method essentially tests whether a particular generator is present in
	 * the blade, but reports the fact with an Optional instead of a boolean. If the
	 * generator is present, the optional is engaged. If not, the optional is
	 * disengaged.
	 * 
	 * @param pG The Generator to 'get'.
	 * @return an Optional of the Generator requested.
	 */
	public Optional<Generator> get(Generator pG) {
		return Optional.ofNullable(blade.contains(pG) ? pG : null);
	}

	/**
	 * This is just a getter method named to support calls from within streams.
	 * 
	 * @return key Returns the blade's ArrayList of boxed bytes.
	 */
	public EnumSet<Generator> getGenerators() {
		return blade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (key ^ (key >>> 32));
		result = prime * result + sign;
		return result;
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
	 * This is a gettor for the maximum generator that can be placed in this blade.
	 * 
	 * @return byte integer that would be the ordinal of the largest Generator that
	 *         could be placed in this blade.
	 */
	public byte maxGenerator() {
		return maxGen;
	}

	/**
	 * This method reports the size of the EnumSet of generators that represents
	 * this blade. This is the 'rank' of the blade.
	 * 
	 * @return byte integer number of generators involved in this blade.
	 */
	public byte rank() {
		return (byte) blade.size();
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
	//public Blade remove(byte pS) throws GeneratorRangeException {
	//	return remove(Byte.valueOf(pS));
	//}

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
	//public Blade remove(Byte pS) throws GeneratorRangeException {
	//	if (pS.byteValue() < CladosConstant.GENERATOR_MIN.ord | pS.byteValue() > CladosConstant.GENERATOR_MAX.ord)
	//		throw new GeneratorRangeException("Unsupported Generator for Blade.");
	//	else if (blade.remove(Generator.get(pS.byteValue())))
	//		makeKey();
	//	return this;
	//}

	/**
	 * This 'remove' method assumes an entire enumerated set of generators has been
	 * produced elsewhere and all of the set elements are to be removed from this
	 * blade.
	 * 
	 * A check is performed first to see if the blade is already a scalar. If it is,
	 * it silently returns and does nothing. If it isn't, the method tries to remove
	 * all set elements even if doing so might make the blade a scalar along the
	 * way. Because the blade's internal representation relies on an EnumSet, it
	 * won't object to attempts to remove set elements not present in the set.
	 * Trying is inefficient, but does no harm.
	 * 
	 * @param pS An EnumSet of Generators to remove from this blade
	 * @return this blade
	 */
	public Blade remove(EnumSet<Generator> pS) {
		if (isScalar(this))
			return this;
		else {
			blade.removeAll(pS);
			return this;
		}
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
	 * Flip the order of multiplication of the generators. This doesn't actually
	 * alter the EnumSet containing generators, though. It computes the effect of a
	 * reversal as a sign flip since the effect is to scale the blade by +1 or -1.
	 * 
	 * No Sign flip for blade size = 0, 1, 4, 5, 8, 9, Sign flip for blade.size = 2,
	 * 3, 6, 7,
	 * 
	 * No Sign flip for blade.size()/2 = 0, 2, 4, Sign flip for blade.size()/2 = 1,
	 * 3,
	 * 
	 * So sign flips when (blade.size()/2) %2 == 1
	 * 
	 * @return Blade This one after the action is complete. Supporting streams.
	 */
	public Blade reverse() {
		if ((blade.size() / 2) % 2 == 1)
			sign *= FLIP;
		return this;
	}

	/**
	 * A simple gettor for the sign of the blade
	 * 
	 * @return byte integer represeting the sign. Should be +1 or -1.
	 */
	public byte sign() {
		return sign;
	}

	/*
	 * Base (maxGen+1) representation of Eddington Number
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
		bitKey = 0;
		int counter = 0;
		Iterator<Generator> cursor = blade.iterator();
		while (cursor.hasNext()) {
			Generator g = cursor.next();
			key += g.ord * Math.pow((maxGen + 1), (blade.size() - 1 - counter));
			bitKey += (1 << (g.ord - 1));
			counter++;
		}
	}

	/**
	 * Simple settor for the sign of this blade.
	 * 
	 * @param pSign byte integer should be +1 or -1. If it isn't, nothing is done.
	 * @return this blade
	 */
	protected Blade setSign(byte pSign) {
		sign = (pSign == (byte) 1) ? (byte) 1 : ((pSign == (byte) -1) ? (byte) -1 : sign);
		return this;
	}

}
