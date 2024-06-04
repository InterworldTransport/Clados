/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Basis<br>
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
 * ---org.interworldtransport.cladosG.Basis<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * All geometry objects within the cladosG package have elements that form a
 * basis to span the vector space related to the algebra. This basis is
 * represented in the algebra as various products of the generators. This class
 * representation uses sets of generators as Blades and a list of Blades. It
 * also maintains a few methods that help manipulate them.
 * <p>
 * The gradeCount is tracked using byte integers as it isn't expected that
 * anyone will work with algebras of more than 126 generators any time soon.
 * <p>
 * The bladeCount is computed as (1 &lt;&lt; gradeCount - 1) instead of stored.
 * It is reported as an integer though that DOES limit the size of a basis to 31
 * generators. Again, it isn't expected anyone will need more any time soon.
 * <p>
 * The bladeList is stored as an ArrayList of Blades that should be no longer
 * than bladeCount. There is nothing to stop it from being longer or shorter,
 * though, so this is a potential source of error. It really SHOULD be
 * immutable once constructed correctly. Same goes for the Blades contained in
 * the ArrayList.
 * <p>
 * Blade keys are now stored inside the Blades. There is no separate array for
 * them in the Basis. The primary use for keys is sorting the Blades and
 * detecting which blade is found at the head of the list for each grade. This
 * is useful when multiplying multivectors that are sparsely populated in terms
 * of their coefficients. The multiplication algorithm can skip over grades not
 * present in the multivector and skip potentially large blocks in the sums with
 * vanishing contributions. However, sparseness is detected using a
 * multivector's key and NOT a Blade's key. Once Blades are sorted into a basis,
 * blade keys are only used to name them in maps.
 * <p>
 * NOTE that Blade keys are currently kept as long integers. The key for a
 * pscalar in a 14 generator basis is 2234152501943160L. For 15 generator
 * algebras the key is 81985529216486896L. Long integers can't hold keys much
 * larger than that. At some point keys will shift to Java's BigInteger class
 * and impose another performance penalty. Not yet, though. Best practice would
 * be to avoid computations demanding heavy use of blade keys.
 * <p>
 * The data in this class is stored in objects instead of arrays of primitives.
 * This is intentional. Doing so allows a system to lay them out in memory in
 * any way it finds convenient. There IS an overhead associated with this plan,
 * but it is in recognition that virtualization puts distance between an
 * application and the hardware on which it runs.
 * <p>
 * It is expected that Basis objects will be cached, though. There is no reason
 * to create copies within running applications. One MAY do so as no singleton
 * enforcement occurs, but every basis of the same number of generators passes
 * the equality test. A convenient cache already exists in the singleton
 * CladosGCache.
 * <p>
 * The choice limit of 15 generators produces a maximum basis size of 32,768.
 * More can be used, but one must change the 'magic numbers' in the
 * CladosConstant class and recompile.
 * <p>
 * There IS a sort buried in the constructor for a Basis. After Blades are
 * generated using the private powerSet method, they are sorted on the 'natural
 * order'. Blades implement Comparable and use their keys for compareTo(). THAT
 * is why blade keys are now buried in Blade objects. Doing so enables one line
 * of code here to use Java's Collections class to decide what sorting algorithm
 * to use. That means this class no longer implements its own sort algorithm. It
 * is likely the developer community is much better at writing sort algorithms,
 * so this is recognition of that reality.
 * <p>
 * This class probably should be implemented as a Java enumeration. It might be
 * some day. The problem with that is construction time scales as O(N^2).
 * Pre-construction of small basis objects makes good sense, but larger ones
 * become problematic especially for users who never intend to use them. If
 * one's primary interest involves Euclidian 3-space, what need is there of a
 * basis with 14 geometric directions and 16384 linear dimensions that takes a
 * sizeable fraction of a minute to construct? So it is suggested that Best
 * Practice among those who build physical models is to prebuild what you need
 * and load it all to the cache. Use CladosGBuilder to do it for you. 
 * That said, it is obvious why the copy constructor was removed.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Basis implements CanonicalBasis {

	/**
	 * This is just a factory method to help name a particular constructor. It is
	 * used in place of 'new Basis(byte)'.
	 * <p>
	 * @param numberOfGenerators Byte representing unique algebraic directions
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. 
	 */
	public static final Basis using(byte numberOfGenerators) throws GeneratorRangeException {
		return new Basis(numberOfGenerators);
	}

	/**
	 * This is just a factory method to help name a particular constructor. It is
	 * used in place of 'new Basis(byte)'.
	 * <p>
	 * @param mxBlade Generator representing a pscalar with all directions.
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range.
	 */
	public static final Basis using(Generator mxBlade) throws GeneratorRangeException {
		if (CanonicalBasis.validateSize(mxBlade.ord))	// Never use Generator's ordinal
			return new Basis(mxBlade); 
		throw new GeneratorRangeException("Supported range is 0<->CladosConstant.MAXGRADE");
	}

	/*
	 * Deliver the powerset of generators present in the pscalar of a basis. Members
	 * of this set ARE the other blades.
	 * <p>
	 * This is O(n^2), so improvements here matter.
	 */
	private final static Set<EnumSet<Generator>> powerSet(Set<Generator> inSet) {
		Set<EnumSet<Generator>> sets = new HashSet<EnumSet<Generator>>(inSet.size(), 0.75f);
		if (inSet.isEmpty()) {
			sets.add(EnumSet.noneOf(Generator.class));
			return sets;
		}
		List<Generator> list = new ArrayList<Generator>(inSet);
		Generator head = list.get(0);
		Set<Generator> tailset = new HashSet<Generator>(list.subList(1, list.size()));
		for (EnumSet<Generator> set : powerSet(tailset)) {
			EnumSet<Generator> newSet = EnumSet.noneOf(Generator.class);
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	/**
	 * This list holds the representation of a Basis for the 'vector space'
	 * associated with an algebra. The list size is ALWAYS bladeCount which is
	 * ALWAYS 2^(# of generators available). Also, the list is ALWAYS ordered such
	 * that Blade keys ascend as one iterates along the list.
	 * <p>
	 * Every list entry is a blade. ArrayList DOES allow for null entries, but
	 * methods in this class MUST prevent that from happening.
	 * <p>
	 * While modern lingo refers to this as the canonical basis, Ken Greider tended
	 * to describe it as the EDDINGTON BASIS. The reason for that comes from the
	 * physical interpretation which he felt traced to Eddington. Greider preferred
	 * to avoid interpreting all the elements as 'vector' directions because that
	 * confused and conflated the geometric meaning for 'vector'. Most of them are
	 * much bigger than one-blades, so a different name that directed students
	 * toward Physics history seemed appropriate. In Clados, "Eddington Basis" is
	 * used if there is a chance of confusing an algebra's "Generator Basis" with
	 * the basis of the vector space spanned by all the blades.
	 */
	private final ArrayList<Blade> bladeList;

	/**
	 * This integer is the number of grades in the algebra. It is one more than the
	 * number of generators and is used often enough to be worth keeping.
	 */
	private final byte gradeCount;

	/**
	 * This list is used for tracking of where grades start and stop in a bladeList.
	 * The difference from grade k to k+1 is binomial(GradeCount-1, k) =
	 * (GradeCount-1)! / (k! * ((GradeCount-1)-k)!) GradeRange[j] is the first
	 * position for a blade of grade j.
	 * <p>
	 * This list enables the CladosG library to avoid implementing a factorial
	 * method to repeatedly calculate binomial coefficients. Construction of the
	 * basis provides the information, so for the sake of efficiency they be stored
	 * here for later use.
	 * <p>
	 * The size of this list is always the same as the grade count.
	 * <p>
	 * grade 0: The first entry always points to the start as that is where the
	 * first (and only) scalar blade is found.
	 * <p>
	 * grade 1: The second entry always points to the start of the one-blades.
	 * <p>
	 * grade 2: N+2 where N is the number of generators is where two-blades start.
	 * <p>
	 * grade N: The last entry always points to the blade that represents the
	 * pscalar in the basis.
	 * <p>
	 * All other entries in this list are calculated using a Blade's key.
	 */
	private final ArrayList<Integer> gradeList;

	/**
	 * This map connects a Blade's internal key to its indexed location in the
	 * basis. This map is what allows us to put the indexed position in the
	 * multiplicaton table instead of the blade's key which is longer and would 
	 * inflate the size of the Cayley table unnecessarily.
	 * <p>
	 * The indexed position "1" is always the scalar of the basis. That's why
	 * a "1" in the Cayley table (called result[][] in GProduct) refers to a 
	 * scalar... or the 'no generators' blade. The fact that a "0" is NOT used
	 * is what will enable the introduction of degenerate generators.
	 */
	private final TreeMap<Long, Integer> keyIndexMap;

	/**
	 * This is the basic constructor. It takes the number of generators as its only
	 * parameter. It can be instantiated on its own for demonstration purposes, but
	 * it has no awareness of the addition and multiplication operations in an
	 * algebra, so all it does is show the basis.
	 * <p>
	 * @param pGens byte This is the number of generators that make up the basis
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, ..., CladosConstant.MAXGRADE}
	 */
	public Basis(byte pGens) throws GeneratorRangeException {
		if (!CanonicalBasis.validateSize(pGens))
			throw new GeneratorRangeException("Supported range is 0<->CladosConstant.MAXGRADE using 8 bit integers");
		// ------Initialize
		gradeCount = (byte) (pGens + 1);
		gradeList = new ArrayList<Integer>(gradeCount);
		bladeList = new ArrayList<Blade>(1 << pGens);
		keyIndexMap = new TreeMap<>();
		// ------Build bladeList
		if (pGens == 0) {
			bladeList.add(Blade.createBlade(pGens));
			gradeList.add(Integer.valueOf(0));
			keyIndexMap.put(0L, 1);
		} else if (pGens == 1) {
			bladeList.add(Blade.createScalarBlade(Generator.E1));
			gradeList.add(Integer.valueOf(0));
			keyIndexMap.put(0L, 1);
			bladeList.add(Blade.createPScalarBlade(Generator.E1));
			gradeList.add(Integer.valueOf(1));
			keyIndexMap.put(1L, 2);
		} else {
			EnumSet<Generator> offer = EnumSet.range(CladosConstant.GENERATOR_MIN, Generator.get(pGens));
			TreeSet<Blade> sorted = new TreeSet<>(); // Expects things that have a natural order
			for (EnumSet<Generator> pG : powerSet(offer))
				sorted.add(new Blade(pGens, pG)); // Adds in SORTED ORDER because... TreeSet
				
			sorted.iterator().forEachRemaining(blade -> { // Iterator works in ascending order
				bladeList.add(blade); // causing bladeList to be in ascending (by key) order
				keyIndexMap.put(blade.key(), Integer.valueOf(bladeList.indexOf(blade) + 1));
			});

			// ------Build gradeList
			gradeList.add(Integer.valueOf(0)); // First entry in gradeList is for scalar grade
			gradeList.add(Integer.valueOf(1)); // Second entry in gradeList is for vector grade
			IntStream.range(2, gradeCount - 1).forEachOrdered(i -> {
				gradeList
						.add(keyIndexMap.ceilingEntry(Long.valueOf((long) Math.pow(gradeCount, i - 1))).getValue() - 1);
				}); // keyIndexMap uses bladeKey (known to blade) to get bladeIndex for products.
			gradeList.add(getBladeCount() - 1); // Last entry in gradeList is for pscalar grade
		}
	}

	/**
	 * This is a 'from point' constructor. It takes a generator and treats it as the pscalar to factor.
	 * It can be instantiated on its own for demonstration purposes, but it has no awareness of the 
	 * addition and multiplication operations in an algebra, so all it does is show the basis.
	 * <p>
	 * @param pGen Generator This is the pscalar to factor to make up the basis
	 */
	public Basis(Generator pGen) {
		// ------Initialize
		gradeCount = (byte) (pGen.ord + 1);
		gradeList = new ArrayList<Integer>(gradeCount);
		bladeList = new ArrayList<Blade>(1 << pGen.ord);
		keyIndexMap = new TreeMap<>();
		// ------Build bladeList
		switch (pGen.ord) {
			case 0 -> {
				bladeList.add(Blade.createBlade(pGen.ord));
				gradeList.add(Integer.valueOf(0));
				keyIndexMap.put(0L, 1);
				break;
			}
			case 1 -> {
				bladeList.add(Blade.createScalarBlade(Generator.E1));
				gradeList.add(Integer.valueOf(0));
				keyIndexMap.put(0L, 1);
				bladeList.add(Blade.createPScalarBlade(Generator.E1));
				gradeList.add(Integer.valueOf(1));
				keyIndexMap.put(1L, 2);
				break;
			}
			default -> {
				EnumSet<Generator> offer = EnumSet.range(CladosConstant.GENERATOR_MIN, pGen);
				TreeSet<Blade> sorted = new TreeSet<>(); // Expects things that have a natural order
				for (EnumSet<Generator> pG : powerSet(offer))
					sorted.add(new Blade(pGen, pG)); // Adds in SORTED ORDER because... TreeSet
				
				sorted.iterator().forEachRemaining(blade -> { // Iterator works in ascending order
					bladeList.add(blade); // causing bladeList to be in ascending (by key) order
					keyIndexMap.put(blade.key(), Integer.valueOf(bladeList.indexOf(blade) + 1));
				});

				// ------Build gradeList
				gradeList.add(Integer.valueOf(0)); // First entry in gradeList is for scalar grade
				gradeList.add(Integer.valueOf(1)); // Second entry in gradeList is for vector grade
				IntStream.range(2, gradeCount - 1).forEachOrdered(i -> {
					gradeList
						.add(keyIndexMap.ceilingEntry(Long.valueOf((long) Math.pow(gradeCount, i - 1))).getValue() - 1);
					}); // keyIndexMap uses bladeKey (known to blade) to get bladeIndex for products.
				gradeList.add(getBladeCount() - 1); // Last entry in gradeList is for pscalar grade
			}
		}
	}

	/**
	 * The stream returned contains blades that match the grade requested in the
	 * parameter.
	 * <p>
	 * There are silent fail behaviors in this method. If the requested grade falls
	 * outside the range expected in the basis, the returned stream will be empty.
	 * This happens for negative grades and grades larger than the pscalar.
	 * <p>
	 * Otherwise, this method works simply filters what bladeStream() would produce
	 * on blade.rank().
	 */
	@Override
	public Stream<Blade> bladeOfGradeStream(byte pIn) {
		return bladeList.stream().filter(blade -> blade.rank() == pIn);
	}

	@Override
	public Stream<Blade> bladeStream() {
		return bladeList.stream();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Basis other = (Basis) obj;
		if (gradeCount != other.gradeCount)
			return false;
		return true;
	}

	/**
	 * Old-fashioned 'find' method that reports index location in the basis where a
	 * Blade is found OR -1 if it wasn't found.
	 * <p>
	 * @param pIn Blade to be found
	 * @return integer index pointing to element of a list containing the Blade OR
	 *         -1 if the blade wasn't found.
	 */
	@Override
	public int find(Blade pIn) {
		if (!(pIn == null)) {
			Integer loc = keyIndexMap.get(pIn.key());
			if (!(loc == null))
				return loc.intValue();
		}
		return -1;
	}

	/**
	 * Return the number of independent blades in the basis. This is the same as the
	 * linear dimension of an algebra that uses this basis.
	 * <p>
	 * @return int
	 */
	@Override
	public int getBladeCount() {
		return bladeList.size();
	}

	/**
	 * There is a similar sounding method in the old basis that isn't being used, so
	 * it is replaced by this one that returns the enumerated set of generators in
	 * the requested blade.
	 * <p>
	 * BEWARE | This method used to throw an exception when the parameter was out of
	 * range. Now it just returns an empty set of generators which will look like a
	 * scalar blade to the unwary. This change allows for a possible silent fail
	 * someone's code, but it also allows for non-exception handling approaches to
	 * termination of loops and streams.
	 * <p>
	 * @param p1 integer pointing to the blade in the internal list
	 * @return EnumSet of Generator representing the blade without the context
	 *         necessary for knowing much about the enclosing space for the blade.
	 */
	@Override
	public EnumSet<Generator> getBladeSet(int p1) {
		if (this.validateBladeIndex(p1))
			return bladeList.get(p1).getGenerators();
		return EnumSet.noneOf(Generator.class);
	}

	/**
	 * Return the number of grades in the basis. Since there is no geometry in the
	 * basis this is a measure of the number of distinct generator subset types that
	 * can be formed where the element count determines the type. Because the empty
	 * set includes no generators, GradeCount will always be one more than the
	 * number of generators.
	 * <p>
	 * @return byte
	 */
	@Override
	public byte getGradeCount() {
		return gradeCount;
	}

	/**
	 * This method simply delivers the otherwise private grade range list. Useful
	 * for testing purposes, but should be avoided as much as possible.
	 * <p>
	 * @return ArrayList of Integers A list of grades boxed as Integers.
	 */
	@Override
	public ArrayList<Integer> getGrades() {
		return gradeList;
	}

	/**
	 * Get an index to the first blade of grade specified by the parameter.
	 * <p>
	 * @param p1 byte This is for choosing which grade index range to return.
	 * @return int Index within the basis where requested grade starts.
	 */
	@Override
	public int getGradeStart(byte p1) {
		if (this.validateGradeIndex(p1))
			return gradeList.get(p1).intValue();
		return -1;
	}

	/**
	 * Return the long key for the blade at p1 in keyIndexMap.
	 * <p>
	 * There is no telling what blade is at the indexed location.
	 * This just returns the key for it assuming it is there.
	 * <p>
	 * @param p1 int This is the desired key for the value p1 .
	 * @return long
	 */
	@Override
	public long getKey(int p1) {
		if (this.validateBladeIndex(p1))
			return bladeList.get(p1).key();
		return -1;
	}

	@Override
	public Blade getPScalarBlade() {
		return bladeList.get(getBladeCount() - 1);
	}

	/**
	 * This is a special version of getGradeStart() that finds the highest grade.
	 * <p>
	 * @return int Index within the basis where pscalar grade starts.
	 */
	@Override
	public int getPScalarStart() {
		return gradeList.get(gradeCount - 1);
	}

	@Override
	public Blade getScalarBlade() {
		return bladeList.get(0);
	}

	/**
	 * Simple gettor method retrieves the Blade at the indexed position in the
	 * Basis.
	 * <p>
	 * Note that a null can be returned from here if the index is out of range.
	 * <p>
	 * @param p1 integer index
	 * @return Blade at the indexed position.
	 */
	@Override
	public Blade getSingleBlade(int p1) {
		if (this.validateBladeIndex(p1))
			return bladeList.get(p1);
		return null;
	}

	@Override
	public IntStream gradeStream() {
		return IntStream.rangeClosed(0, gradeCount - 1);
	}

	@Override
	public int hashCode() {
		return (int) getGradeCount();
	}

	@Override
	public LongStream keyStream() {
		return bladeList.stream().mapToLong(blade -> blade.key());
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * <p>
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String
	 */
	@Override
	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Basis>\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("\t<Grades count=\"")
			.append(getGradeCount() + "\">\n");
		for (int k = 0; k <= gradeCount - 2; k++) // loop to get all but the highest grade
			rB.append(indent)
				.append("\t\t<Grade number=\"")
				.append(k)
				.append("\" range=\"")
				.append(gradeList.get(k))
				.append("-")
				.append((gradeList.get(k + 1) - 1))
				.append("\" />\n");
		// Handle last grade separate. There is no k+1 index for the largest grade
		rB.append(indent)
			.append("\t\t<Grade number=\"")
			.append((getGradeCount() - 1))
			.append("\" range=\"")
			.append(gradeList.get(gradeCount - 1))
			.append("-")
			.append(gradeList.get(gradeCount - 1))
			.append("\" />\n");
		rB.append(indent)
			.append("\t</Grades>\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("\t<Blades count=\"")
			.append(getBladeCount())
			.append("\">\n");
		for (int k = 0; k < bladeList.size(); k++) // Appending blades
			rB.append(Blade.toXMLString(bladeList.get(k), indent + "\t\t"));
		rB.append(indent)
			.append("\t</Blades>\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("</Basis>\n");
		return rB.toString();
	}

	/**
	 * This is a validator detects blade out of range issues. If one tries to name a
	 * blade by its index, it is always possible for the offered integer to be out
	 * of range.
	 * <p>
	 * @param pIn Short representing the integer index of the blade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	@Override
	public final boolean validateBladeIndex(int pIn) {
		return (pIn >= CladosConstant.SCALARGRADE & pIn < getBladeCount());
	}

	/**
	 * This is a validator detects grade out of range issues. If one tries to name a
	 * grade by its index, it is always possible for the offered integer to be out
	 * of range.
	 * <p>
	 * @param pIn int representing the integer index of the grade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	@Override
	public final boolean validateGradeIndex(int pIn) {
		return (pIn >= CladosConstant.SCALARGRADE & pIn < getGradeCount());
	}

}