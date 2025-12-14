/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Blade<br>
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
 * ---org.interworldtransport.cladosG.Blade<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.interworldtransport.cladosG.CladosConstant.*;

/**
 * A (@code Blade) is essentially an outer product space built from 0 to many vectors.If the vectors aren't parallel, 
 * the blade is of higher rank than a vector. At this low level, though, there is no concept of an inner product, thus no
 * sense of 'parallel'. That leaves a blade as an ordered 'set' of distinct directions and a few supporting elements 
 * including a long integer key useful for comparisons and byte integer necessary for knowing how many possible 
 * directions might ever be added to this blade.
 * <br><br>
 * The directions are simply Generators from an enumeration class. They are kept in an EnumSet which uses as its sense of
 * order the same order generators are enumerated in their class. At present, the supported number of 'directions' 
 * is 0 to 15, so the enumeration class lists 15 possible generators.
 * <br><br>
 * The EnumSet keeps Generators in their natural order. If a new direction is added, the EnumSet will handle it 'late' in 
 * the computational sense. In other words, it doesn't matter where the new generator gets added. It matters ONLY when 
 * generators are iterated later when establishing a product table or generating a blade key. Iterators will always 
 * deliver directions in the same order.
 * <br><br>
 * For example, if a sub-manifold has six possible directions from a point, a Blade will contain zero to six of them 
 * represented as generators E1 through E6. If only 3 are in the Blade (making it a 3-blade) then ANY three will be in the 
 * EnumSet. Perhaps E2, E3, E5. If the EnumSet is empty, zero directions are contained and the blade represents a scalar.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class Blade implements CanonicalBlade, Comparable<Blade> {
	/**
	 * Nothing fancy here. Just giving a name to -1 for use when flipping signs when
	 * generators are transposed. This is to make it clear WHY a sign flip occurs.
	 * Human readability is the issue.
	 */
	protected static final byte FLIP = -1;

	/**
	 * This method will try to deliver a Blade with one more generator in its internal set. The maximum generator 
	 * will be set to the larger of the offered generator or the largest one in the blade being copied. If the old 
	 * Blade already had the generator, it comes back as a copy.
	 * <br><br>
	 * Note that once maxGen is set for a Blade it CANNOT be changed.
	 * <br><br>
	 * @param pB Blade offered to be augmented.
	 * @param pG Generator offered to be added to pB.
	 * @return Blade is returned, but there is a chance it is pB.
	 */
	public final static Blade augmentBlade(Blade pB, Generator pG) {
		return new Blade(pB, pG);
	}

	/**
	 * Deliver a blade of the size specified by the byte integer... but might get
	 * a scalar blade if the integer is out of supported range.
	 * <br><br>
	 * @param pMaxGen 	This is the byte integer representation of the largest
	 *                	generator that will be used in this blade being created.
	 * @return Blade 	is returned... but it might be capable of only holding scalars
	 * 					if the byte offered is outside the supported range..
	 */
	public final static Blade createBlade(byte pMaxGen) {
		return new Blade(pMaxGen);
	}

	/**
	 * This method is very similar to createBlade(byte) but because it uses an full Blade it can bypass the 
	 * safety check for support validity. The Generator enumeration is assumed to have ONLY generators that 
	 * can be supported by internal representations of blades, bases, and products.
	 * <br>
	 * @param pB This is the Blade that will be copied.
	 * @return Blade that references all the same generators as the offered one.
	 */
	public final static Blade createBlade(Blade pB) {
		return new Blade(pB);
	}

	/**
	 * This method is very similar to createBlade(byte) but because it uses an actual generator it can bypass 
	 * the safety check for support validity. 
	 * <br><br>
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with one generator contained and a max set at the same generator.
	 */
	public final static Blade createBlade(Generator pGen) {
		return (new Blade(pGen));	 //Makes 0-blade then a 1-blade with pGen
	}

	/**
	 * This method is very similar to createBlade(byte) but because it uses an actual generator it can bypass 
	 * the safety check for support validity. 
	 * <br><br>
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with one generator contained and a max set at the same generator.
	 */
	public final static Blade createBladePlus(Generator pGen) {
		return (new Blade(pGen)).add(pGen);	 //Makes 0-blade then a 1-blade with pGen
	}
	
	/**
	 * This method is very similar to createPScalarBlade(byte) but because it uses an actual generator it can 
	 * bypass the safety check for support validity. 
	 * <br><br>
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with all generators contained. Basically, a pscalar in the
	 *         implied space.
	 */
	public final static Blade createPScalarBlade(Generator pGen) {
		Blade returnIt = new Blade(pGen);
		Generator.stream(pGen.ord).forEach(g -> returnIt.add(g));
		return returnIt;
	}

	/**
	 * This is a specialty name that does the same thing as createBlade(gen), but
	 * allows for humans to correctly read the intent of a developer when the result
	 * Blade is expected to be a scalar.
	 * <br>
	 * @param pGen This points to the highest generator that could be used.
	 * @return Blade with no generators contained. Basically, a scalar in the
	 *         implied space.
	 */
	public final static Blade createScalarBlade(Generator pGen) {
		return new Blade(pGen);
	}

	/**
	 * Simple grade tester. Does the Blade contain ALL generators for the implied
	 * container basis?
	 * <br>
	 * The maximum generator of the implied space could be ZERO causing scalars and
	 * pscalars to be the same. This method will return TRUE in that case too.
	 * <br>
	 * @param blade Blade to be tested
	 * @return TRUE if the blade all the generators implied by its own maxGen value.
	 *         FALSE otherwise.
	 */
	public final static boolean isPScalar(Blade blade) {
		return CanonicalBlade.isNBlade(blade, blade.maxGenerator());
	}

	/**
	 * Simple grade tester. Does the Blade contain NO generators?
	 * <br>
	 * The maximum generator of the implied space could be ZERO causing scalars and
	 * pscalars to be the same. This method will return TRUE in that case too.
	 * <br>
	 * @param blade Blade to be tested
	 * @return TRUE if the blade has no generators. FALSE otherwise.
	 */
	public final static boolean isScalar(Blade blade) {
		return CanonicalBlade.isNBlade(blade, (byte) 0);
	}

	/**
	 * bitKey is the base-2 representation of the blade's generators.
	 * <br>
	 * For example... a blade using E2 will have +2 added to the bitKey because the
	 * ordinal for E2 is 2 and the key builder method adds (1<<(E2.ord -1)) for this
	 * generator. For E5 (ord=5) bitKey picks up a contribution of 2^4.
	 */
	private int bitKey = 0;

	/**
	 * This is the internal representation of the generators involved in the blade.
	 * If a generator is found in the EnumSet, it is part of the blade.
	 * <br>
	 * For example, a set holding E1, E4, and E9 implies this is the E1,E4,E9 blade.
	 */
	private EnumSet<Generator> genSet;

	/**
	 * This is the old key representing the blade that is sure to increase in a way
	 * that ensures the correct sort order in comparisons between blades. This
	 * sorting happens most often when constructing a basis.
	 * <br><br>
	 * The bitKey can also be used but k-blades that use the same generators from
	 * different spaces with different sized pscalars will have the same key. 
	 * This key does not suffer that duplication because the exponential base uses
	 * the ordinal+1 from the largest generator instead of base two.
	 * <br><br>
	 * Sum on Math.pow( maxGen+1, a digit location -1 ) VS (1 << (digit location - 1))
	 * <br>
	 * Look it up in the code of the private makeKey() method.
	 * <br><br>
	 */
	private long key = 0L;

	/**
	 * This is the maximum expected size of the internal EnumSet for the blade. It
	 * is also the ordinal of the largest generator one expects to find in the
	 * EnumSet.
	 * <br>
	 * Once set, this value should never change.
	 */
	protected final byte maxGen; // This should be gradeCount-1 in a related basis

	/**
	 * This byte integer would be a single bit as it is never expected to be
	 * anything other than +1 or -1. It represents whether the blade has been
	 * inverted or not. Blades do NOT have a sense of magnitude, so this inversion
	 * is ONLY about the order of the generators in the EnumSet.
	 * <br><br>
	 * When this is +1, the blade is assumed to be in a state where the EnumSet
	 * represents the natural order of generators OR in a state where an even number
	 * of transpositions have occurred (after all pairs of transpostions that would
	 * cancel each other are removed) away from the natural order.
	 * <br><br>
	 * When this is -1, the blade is assumed to be in a state where the EnumSet
	 * represents an odd number of transpositions (after canceling pairs are
	 * removed) away from the natural order of the generators in the set.
	 * <br><br>
	 * The ONLY time this sign changes is when a Cayley table in constructed in the
	 * GProduct class. The BladeDuet class handles generator manipulation and passes
	 * information back to its consumer through a Blade. If that blade's sign is 
	 * examined, it informs the consumer whether the generator permutation is even,
	 * odd, or collapsed to ZERO because of degenerate generator pairs. Beyond this
	 * short-lived use, a blade's sign is ignored.
	 */
	private byte sign = 1;

	/**
	 * This is a copy constructor that builds an identical blade.
	 * <br>
	 * @param pB The Blade to copy
	 */
	public Blade(Blade pB) {
		maxGen = pB.maxGenerator();
		genSet = EnumSet.noneOf(Generator.class);
		genSet.addAll(pB.getGenerators());
		key = pB.key();
		bitKey = pB.bitKey();
	}

/**
	 * This is a copy constructor that builds an identical blade, but with a maximum generator size just big enough 
	 * to make room for adding the offered generator.
	 * <br><br>
	 * IF the new blade would be bigger than the maximum supported size then a copy of pB is constructed instead.
	 * <br><br>
	 * @param pB The Blade to copy
	 * @param pGen The Generator to add to the list.
	 */
	public Blade(Blade pB, Generator pGen) {
		genSet = EnumSet.noneOf(Generator.class);
		genSet.addAll(pB.getGenerators());
		
		if (genSet.stream().anyMatch(e -> e.ord >= pGen.ord))	//pGen not larger than a generator in the blade
				maxGen = pB.maxGenerator();						//so preserve pB's max geneator
		else 	maxGen = pGen.ord;								//or use pGen AS the max generator
		
		if (!genSet.contains(pGen))
			genSet.add(pGen);									//Now append pGen
				
		makeKey();
	}

	/**
	 * Simple constructor that establishes a SCALAR BLADE with room to expand up to as many generators as the offered integer. 
	 * <br><br>
	 * The constructor converts the offered byte intger to a Generator and then calls the constructor that uses a Generator.
	 * If the byte offered is outside the supported range, a scalar blade with only room for a scalar will be generated.
	 * <br><br>
	 * @param pMaxGen byte integer for the number of possible directions that might appear in this blade.
	 */
	public Blade(byte pMaxGen) {
		this(Generator.get(pMaxGen));
	}

	/**
	 * Simplest constructor that establishes a SCALAR BLADE by naming the largest generator that will ever be used within it.
	 * <br><br>
	 * If the generator offered is null, a scalar blade with only room for a scalar will be generated.
	 * <br><br>
	 * @param pMaxGen Generator that is the largest of the possible directions that might appear in this blade.
	 */
	public Blade(Generator pMaxGen) {
		genSet = EnumSet.noneOf(Generator.class);
		if (pMaxGen != null) 	maxGen = pMaxGen.ord;
		else 					maxGen = 0;
		key = 0L;
		bitKey = 0;
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen expectations AND provides an array of 
	 * directions to load into the blade.
	 * <br><br>
	 * @param pGen Generator used to get ordinal for the number of possible directions that might appear in this blade.
	 * @param pDirs  Contains an enumset of generators to append to the blade.
	 */
	public Blade(Generator pGen, EnumSet<Generator> pDirs) {
		this(pGen);
		pDirs.forEach(g -> genSet.add(g));
		makeKey();
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen expectations AND provides an array of 
	 * directions to load into the blade.
	 * <br><br>
	 * @param pMaxGen byte integer for the number of possible directions that might appear in this blade.
	 * @param pDirs  Contains an enumset of generators to append to the blade.
	 */
	public Blade(byte pMaxGen, EnumSet<Generator> pDirs) {
		this(pMaxGen);
		pDirs.forEach(g -> genSet.add(g));
		makeKey();
	}

	/**
	 * This is a maximal constructor that establishes the blade's future maxGen
	 * expectations AND provides an array of directions to load into the blade.
	 * <br>
	 * @param pMaxGen byte integer for the number of possible directions that might
	 *                appear in this blade.
	 * @param pDirs   Generator[] containing directions to append to the blade.
	 */
	public Blade(byte pMaxGen, Generator[] pDirs) {
		this(pMaxGen);
		Stream.of(pDirs).forEach(g -> genSet.add(g));
		makeKey();
	}

	/**
	 * This 'add' method assumes an entire enumerated set of generators has been
	 * produced elsewhere and all of the set elements are to be added to this blade.
	 * <br>
	 * A check is performed first to see if the blade is already a pscalar. If it
	 * is, it silently returns and does nothing. If it isn't, the method tries to
	 * add all set elements even if doing so might make the blade a pscalar along
	 * the way. Because the blade's internal representation relies on an EnumSet, it
	 * de-duplicates generators added here.
	 * <br>
	 * @param pS An EnumSet of Generators to add to this blade
	 * @return this blade
	 */
	public Blade add(EnumSet<Generator> pS) {
		if (isPScalar(this))
			return this;
		else {
			genSet.addAll(pS);
			makeKey();
			return this;
		}
	}

	/**
	 * The generator represents a 'direction' in the blade to be added. The blade is
	 * checked to see if it is at maximum size and whether the offered generator is
	 * beyond masGrade. If either fails, the add silently returns the Blade
	 * unchanged. If both pass, the generator is added to the set.
	 * <br>
	 * @param pS Generator that will be added to the set.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator pS) {
		if (isPScalar(this) | pS.ord > maxGen)
			return this;
		else {
			genSet.add(pS);
			makeKey();
			return this;
		}
	}

	/**
	 * The generators represent 'directions' in the blade to be added. The blade is
	 * checked to see if it is at maximum size. If it is, the add silently returns
	 * the Blade unchanged. If it passes, the generators are added to the set if
	 * they pass through the filter that blocks generators larger than maxGen.
	 * <br>
	 * @param pS Generators that will be added to the set.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade add(Generator[] pS) {
		if (isPScalar(this))
			return this;
		else {
			Stream.of(pS).filter(g -> g.ord <= maxGen).forEach(g -> genSet.add(g));
			makeKey();
			return this;
		}
	}

	/**
	 * This is just a getter method named to support consumers at the end of streams
	 * of blades. This is how one gets a stream of blade keys.
	 * <br>
	 * @return key Returns the blade's bit integer key.
	 */
	public int bitKey() {
		return bitKey;
	}

	/**
	 * The method needs to be as efficient as possible because it is used in comparators.
	 * It determines the sort order of blades in a basis, thus the streaming order for
	 * coefficients in a Scale.
	 * <br>
	 * This function is WHY Blades keep a Long key. The bit key suffices to distinguish
	 * blades, but not to sort them into grades in a basis.
	 * <br>
	 * @param pIn Blade to be compared to this one
	 * @return int in {-1, 0, 1} depending on the size of the offered blade's long
	 *         integer key relative to this one. If the incoming long key is larger
	 * 		   the integer returned is +1.
	 */
	@Override
	public int compareTo(Blade pIn) {
		return (key() < pIn.key()) ? -1 : (key() > pIn.key() ? 1 : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)					return true;
		if (obj == null)					return false;
		if (getClass() != obj.getClass())	return false;
		if (key != ((Blade) obj).key)		return false;
		if (maxGen != ((Blade) obj).maxGen)	return false;
		return true;
	}

	/**
	 * This method essentially tests whether a particular generator is present in
	 * the blade, but reports the fact with an Optional instead of a boolean. If the
	 * generator is present, the optional is engaged. If not, the optional is
	 * disengaged.
	 * <br>
	 * @param pG The Generator to 'get'.
	 * @return an Optional of the Generator requested.
	 */
	public Optional<Generator> get(Generator pG) {
		return Optional.ofNullable(genSet.contains(pG) ? pG : null);
	}

	/**
	 * This is just a getter method that returns the EnumSet of generators. It gets
	 * used mostly to populate lists, maps, and in export functions.
	 * <br>
	 * @return key Returns the blade's enumerated set of generators.
	 */
	protected EnumSet<Generator> getGenerators() {
		return genSet;
	}

	/**
	 * This is the streaming version of the getGenerators() method. Consumers of a 
	 * list of the generators often work with streams of them.
	 * <br>
	 * @return Stream of generators used in the blade.
	 */
	public Stream<Generator> generatorStream() {
		return genSet.stream();
	}

	@Override
	public int hashCode() {
		final int prime = 67;
		int result = 1;
		result = prime + (int) (key ^ (key >>> 32));
		result = prime * result + sign;
		return result;
	}

	/**
	 * This is just a getter method named to support consumers at the end of streams
	 * of blades. This is how one gets a stream of blade keys.
	 * <br>
	 * @return key Returns the blade's long integer key.
	 */
	public long key() {
		return key;
	}

	/**
	 * This is a gettor for the maximum generator that can be placed in this blade.
	 * <br>
	 * @return byte integer that would be the ordinal of the largest Generator that
	 *         could be placed in this blade.
	 */
	public byte maxGenerator() {
		return maxGen;
	}

	/**
	 * This method reports the size of the EnumSet of generators that represents
	 * this blade. This is the 'rank' of the blade.
	 * <br>
	 * @return byte integer number of generators involved in this blade.
	 */
	public byte rank() {
		return (byte) genSet.size();
	}

	/**
	 * This 'remove' method assumes an entire enumerated set of generators has been
	 * produced elsewhere and all of the set elements are to be removed from this
	 * blade.
	 * <br>
	 * A check is performed first to see if the blade is already a scalar. If it is,
	 * it silently returns and does nothing. If it isn't, the method tries to remove
	 * all set elements even if doing so might make the blade a scalar along the
	 * way. Because the blade's internal representation relies on an EnumSet, it
	 * won't object to attempts to remove set elements not present in the set.
	 * Trying is inefficient, but does no harm.
	 * <br>
	 * @param pS An EnumSet of Generators to remove from this blade
	 * @return this blade
	 */
	public Blade remove(EnumSet<Generator> pS) {
		if (isScalar(this))
			return this;
		else {
			genSet.removeAll(pS);
			makeKey(); // Removing anything changes the key.
			return this;
		}
	}

	/**
	 * The generator represents a 'direction' in the blade to be removed. If
	 * anything is found to be removed, the key is recomputed.
	 * <br>
	 * @param pS Generator representing the 'direction' to remove from the blade.
	 * @return Blade The blade itself is returned to support stream calls.
	 */
	public Blade remove(Generator pS) {
		if (genSet.remove(pS))
			makeKey();
		return this;
	}

	/**
	 * Flip the order of multiplication of the generators. This doesn't actually
	 * alter the EnumSet containing generators, though. It computes the effect of a
	 * reversal as a sign flip since the effect is to scale the blade by +1 or -1.
	 * <br>
	 * No Sign flip for blade size = 0, 1, 4, 5, 8, 9, Sign flip for blade.size = 2,
	 * 3, 6, 7,
	 * <br>
	 * No Sign flip for blade.size()/2 = 0, 2, 4, Sign flip for blade.size()/2 = 1,
	 * 3,
	 * <br>
	 * So sign flips when (blade.size()/2) %2 == 1
	 * <br>
	 * @return Blade This one after the action is complete. Supporting streams.
	 */
	//public Blade reverse() {
	//	if ((genSet.size() / 2) % 2 == 1)
	//		sign *= FLIP;
	//	return this;
	//}

	/**
	 * A simple gettor for the sign of the blade
	 * <br>
	 * @return byte integer represeting the sign. Should be +1 or -1.
	 */
	public byte sign() {
		return sign;
	}

	/*
	 * Base (maxGen+1) representation of Eddington Number
	 * <br>
	 * Ex: 3 generators implies Base-4 keys stuffed into Base-10 number.
	 * <br>
	 * Last generator in the list is the one's digit, the next to last is the 4's
	 * digit and the one before that is the 16's digit. The actual byte stored in
	 * the list is multiplied by that power
	 * <br>
	 * Ex: 8 generators implies Base-9 keys stuffed into a Base-10 number.
	 * <br><br>
	 * pow() method is in CladosConstant as a replacement for Math.pow()
	 */
	private void makeKey() {
		key = 0L;
		bitKey = 0;
		int counter = 0;
		Iterator<Generator> cursor = genSet.iterator();
		Generator g;
		while (cursor.hasNext()) {
			g = cursor.next();
			key += g.ord * pow((byte)(maxGen+1), (genSet.size() - 1 - counter)).longValue();
			bitKey += (1 << (g.ord - 1));
			counter++;
		}
	}
	
	/**
	 * Simple settor for the sign of this blade.
	 * <br>
	 * @param pSign byte integer should be +1, -1, or 0. If it isn't, sign is set to 0.
	 * @return this blade
	 */
	protected Blade setSign(byte pSign) {
		sign = (pSign == (byte) 1) ? (byte) 1 : ((pSign == (byte) -1) ? (byte) -1 : (byte) 0);
		return this;
	}

}