/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
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
 * representation uses sets of generators as Blades and and a list of Blades. It
 * also maintains a few methods that help manipulate them.
 * 
 * The gradeCount is tracked using byte integers as it isn't expected that
 * anyone will work with algebras of more than 254 generators any time soon.
 * 
 * The bladeCount is computed as (1 &lt;&lt; gradeCount - 1) instead of stored.
 * It is reported as an integer though that DOES limit the size of a basis to 31
 * generators. Again, it isn't expected anyone will need more any time soon.
 * 
 * The bladeList is stored as an ArrayList of Blades that should be no longer
 * than bladeCount. There is nothing to stop it from being longer or shorter,
 * though, so this is a potential source of errors. It really SHOULD be
 * immutable once constructed correctly. Same goes for the Blades contained in
 * the ArrayList.
 * 
 * Blade keys are now stored inside the Blades. There is no separate array for
 * them in the Basis. The primary use for keys is sorting the Blades and
 * detecting which blade is found at the head of the list for each grade. This
 * is useful when multiplying multivectors that are sparsely populated in terms
 * of their coefficients. The multiplication algorithm can skip over grades not
 * present in the multivector and skip potentially large blocks in the sums with
 * vanishing contributions. However, sparseness is detected using a
 * multivector's key and NOT a Blade's key. Once Blades are sorted into a basis,
 * blade keys are only one more time. This occurs when a product table is
 * constructed. Blade keys are used to identify the resulting Blade for re-use.
 * 
 * NOTE that Blade keys are currently kept as long integers. The key for a
 * pscalar in a 14 generator basis is 2234152501943159L. For 15 generator
 * algebras the key is 81985529216486895L. Long integers can't hold keys much
 * larger than that. At some point keys will shift to Java's BigInteger class
 * and impose another performance penalty. Not yet, though. Best practice would
 * be to avoid computations demanding heavy use of blade keys.
 * 
 * The data in this class is stored in objects instead of arrays of primitives.
 * This is intentional. Doing so allows a system to lay them out in memory in
 * any way it finds convenient. There IS an overhead associated with this plan,
 * but it is in recognition that virtualization puts distance between an
 * application and the hardware on which it runs.
 * 
 * It is expected that Basis objects will be cached, though. There is no reason
 * to create copies within running applications. One MAY do so as no singleton
 * enforcement occurs, but every basis of the same number of generators passes
 * the equality test. A convenient cache already exists in the singleton
 * CladosGBuilder.
 * 
 * The choice limit of 14 generators produces a maximum basis size of 16,384.
 * More can be used, but one must change the 'magic numbers' in the
 * CladosConstant class and recompile.
 * 
 * There IS a sort buried in the constructor for a Basis. After Blades are
 * generated using the private powerSet method, they are sorted on the 'natural
 * order'. Blades implement Comparable and use their keys for compareTo(). THAT
 * is why blade keys are now buried in Blade objects. Doing so enables one line
 * of code here to use Java's Collections class to decide what sorting algorithm
 * to use. That means this class no longer implements its own sort algorithm. It
 * is likely the developer community is much better at writing sort algorithms,
 * so this is recognition of that reality.
 * 
 * This class probably should be implemented as a Java enumeration. It might be
 * some day. The problem with that is construction time scales as O(N^2).
 * Pre-construction of small basis objects makes good sense, but larger ones
 * become problematic especially for users who never intend to use them. If
 * one's primary interest involves Euclidian 3-space, what need is there of a
 * basis with 14 geometric directions and 16384 linear dimensions that takes a
 * sizeable fraction of a minute to construct? So it is suggested that Best
 * Practice among those who build physical models is to prebuild what you need
 * and load it all to the cache in CladosGBuilder. Better yet, use
 * CladosGBuilder to do it for you. That said, it should be easy to understand
 * why the copy constructor has been removed.
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Basis implements CanonicalBasis {

	/**
	 * This is just a factory method to help name a particular constructor. It is
	 * used in place of 'new Basis(short)'.
	 * 
	 * @param numberOfGenerators Short representing unique algebraic directions
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public static final Basis using(byte numberOfGenerators) throws GeneratorRangeException {
		return new Basis(numberOfGenerators);
	}

	/*
	 * Deliver the powerset of generators present in the pscalar of a basis. Members
	 * of this set ARE the other blades.
	 * 
	 * This is O(n^2), so improvements here matter.
	 */
	private final static Set<EnumSet<Generator>> powerSet(Set<Generator> inSet) {
		Set<EnumSet<Generator>> sets = new HashSet<EnumSet<Generator>>();
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
	 * 
	 * Every list entry is a blade. ArrayList DOES allow for null entries, but
	 * methods in this class MUST prevent that from happening.
	 * 
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

	//private final TreeMap<Blade, Integer> gradeIndexMap;

	/**
	 * This list is used for tracking of where grades start and stop in a bladeList.
	 * The difference from grade k to k+1 is binomial(GradeCount-1, k) =
	 * (GradeCount-1)! / (k! * ((GradeCount-1)-k)!) GradeRange[j] is the first
	 * position for a blade of grade j.
	 * 
	 * This list enables the CladosG library to avoid implementing a factorial
	 * method to repeatedly calculate binomial coefficients. Construction of the
	 * basis provides the information, so for the sake of efficiency they be stored
	 * here for later use.
	 * 
	 * The size of this list is always the same as the grade count.
	 * 
	 * grade 0: The first entry always points to the start as that is where the
	 * first (and only) scalar blade is found.
	 * 
	 * grade 1: The second entry always points to the start of the one-blades.
	 * 
	 * grade 2: N+2 where N is the number of generators is where two-blades start.
	 * 
	 * grade N: The last entry always points to the blade that represents the
	 * pscalar in the basis.
	 * 
	 * All other entries in this list are calculated using a Blade's key.
	 */
	private final ArrayList<Integer> gradeList;

	/**
	 * This map connects a Blade's internal key to its indexed location in the
	 * basis.
	 */
	private final TreeMap<Long, Integer> keyIndexMap;

	/**
	 * This is the basic constructor. It takes the number of generators as it's only
	 * parameter. It can be instantiated on it's own for demonstration purposes, but
	 * it has no awareness of the addition and multiplication operations in an
	 * algebra, so all it does is show the basis.
	 * 
	 * @param pGens short This is the number of generators that make up the basis
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public Basis(byte pGens) throws GeneratorRangeException {
		if (!CanonicalBasis.validateSize(pGens))
			throw new GeneratorRangeException("Supported range is 0<->14 using 8 bit integers");
		// ------Initialize
		gradeCount = (byte) (pGens + 1);
		gradeList = new ArrayList<Integer>(gradeCount);
		//gradeIndexMap = new TreeMap<>();
		bladeList = new ArrayList<Blade>(1 << pGens);
		keyIndexMap = new TreeMap<>();
		// ------Build bladeList
		if (pGens == 0) {
			bladeList.add(Blade.createBlade(pGens).get());
			gradeList.add(Integer.valueOf(0));
			//gradeIndexMap.put(bladeList.get(0), Integer.valueOf(0));
			keyIndexMap.put(0L, 0);
		} else if (pGens == 1) {
			bladeList.add(Blade.createScalarBlade(Generator.E1));
			gradeList.add(Integer.valueOf(0));
			//gradeIndexMap.put(bladeList.get(0), Integer.valueOf(0));
			keyIndexMap.put(0L, 0);
			bladeList.add(Blade.createPScalarBlade(Generator.E1));
			gradeList.add(Integer.valueOf(1));
			//gradeIndexMap.put(bladeList.get(1), Integer.valueOf(1));
			keyIndexMap.put(1L, 0);
		} else {
			EnumSet<Generator> offer = EnumSet.range(CladosConstant.GENERATOR_MIN, Generator.get(pGens));
			TreeSet<Blade> sorted = new TreeSet<>(); // Expects things that have a natural order
			for (EnumSet<Generator> pG : powerSet(offer))
				sorted.add(new Blade(pGens, pG)); // Adds in SORTED ORDER because... TreeSet
			sorted.iterator().forEachRemaining(blade -> { // Iterator works in ascending order
				bladeList.add(blade); // causing bladeList to be in ascending (by key) order
				keyIndexMap.put(blade.key(), Integer.valueOf(bladeList.indexOf(blade) + 1));
				//gradeIndexMap.put(blade, blade.getGenerators().size());
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
	 * The stream returned contains blades that match the grade requested in the
	 * parameter.
	 * 
	 * There are silent fail behaviors in this method. If the requested grade falls
	 * outside the range expected in the basis, the returned stream will be empty.
	 * This happens for negative grades and grades larger than the pscalar.
	 * 
	 * Otherwise, this method works simply filters what bladeStream() would produce
	 * on blade.rank().
	 */
	@Override
	public Stream<Blade> bladeOfGradeStream(byte pIn) {
		if (pIn >= CladosConstant.SCALARGRADE & pIn <= gradeCount)
			return bladeList.stream().filter(blade -> blade.rank() == pIn);
		else
			return null;
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
	 * 
	 * @param pIn Blade to be found
	 * @return integer index pointing to element of a list containing the Blade OR
	 *         -1 if the blade wasn't found.
	 */
	@Override
	public int find(Blade pIn) {
		Integer loc = keyIndexMap.get(pIn.key());
		if (loc == null)
			return -1;
		return loc.intValue();
	}

	/**
	 * Return the number of independent blades in the basis. This is the same as the
	 * linear dimension of an algebra that uses this basis.
	 * 
	 * @return int
	 */
	@Override
	public int getBladeCount() {
		return (1 << gradeCount - 1);
	}

	/**
	 * There is a similar sounding method in the old basis that isn't being used, so
	 * it is replaced by this one that returns the enumerated set of generators in
	 * the requested blade.
	 * 
	 * BEWARE | This method used to throw an exception when the parameter was out of
	 * range. Now it just returns an empty set of generators which will look like a
	 * scalar blade to the unwary. This change allows for a possible silent fail
	 * someone's code, but it also allows for non-exception handling approaches to
	 * termination of loops and streams.
	 * 
	 * @param p1 integer pointing to the blade in the internal list
	 * @return EnumSet of Generator representing the blade without the context
	 *         necessary for knowing much about the enclosing space for the blade.
	 */
	@Override
	public EnumSet<Generator> getBladeSet(int p1) {
		if (p1 < CladosConstant.SCALARGRADE | p1 > getBladeCount())
			return EnumSet.noneOf(Generator.class);
		return bladeList.get(p1).getGenerators();
	}

	/**
	 * Return the number of grades in the basis. Since there is no geometry in the
	 * basis this is a measure of the number of distinct generator subset types that
	 * can be formed where the element count determines the type. Because the empty
	 * set includes no generators, GradeCount will always be one more than the
	 * number of generators.
	 * 
	 * @return byte
	 */
	@Override
	public byte getGradeCount() {
		return gradeCount;
	}

	/**
	 * This method simply delivers the otherwise private grade range list. Useful
	 * for testing purposes, but should be avoided as much as possible.
	 * 
	 * @return ArrayList of Integers A list of grades boxed as Integers.
	 */
	@Override
	public ArrayList<Integer> getGrades() {
		return gradeList;
	}

	/**
	 * Get an index to the first blade of grade specified by the parameter.
	 * 
	 * @param p1 byte This is for choosing which grade index range to return.
	 * @return int Index within the basis where requested grade starts.
	 */
	@Override
	public int getGradeStart(byte p1) {
		if (p1 < CladosConstant.SCALARGRADE | p1 > gradeCount)
			return -1;
		return gradeList.get(p1).intValue();
	}

	/**
	 * Return the long at p1 in the EddingtonKey array.
	 * 
	 * IllegalArgumentException can be thrown if the requested blade is NOT between
	 * 0 and bladeCount inclusive.
	 * 
	 * @param p1 short This is the desired key at p1 .
	 * @return long
	 */
	@Override
	public long getKey(int p1) {
		if (p1 < CladosConstant.SCALARGRADE | p1 > getBladeCount())
			throw new IllegalArgumentException("Requested Blade # {" + p1 + "} is not in basis");
		return bladeList.get(p1).key();
	}

	/**
	 * Simple gettor method for the Map connecting Blade keys to their indexed
	 * position in a basis.
	 */
	@Override
	public TreeMap<Long, Integer> getKeyIndexMap() {
		return keyIndexMap;
	}

	/**
	 * This is a special version of getGradeStart() that finds the highest grade.
	 * 
	 * @return int Index within the basis where pscalar grade starts.
	 */
	@Override
	public int getPScalarStart() {
		return gradeList.get(gradeCount);
	}

	/**
	 * Simple gettor method retrieves the Blade at the indexed position in the
	 * Basis.
	 * 
	 * Note that a null can be returned from here if the index is out of range.
	 * 
	 * @param p1 integer index
	 * @return Blade at the indexed position.
	 */
	@Override
	public Blade getSingleBlade(int p1) {
		if (p1 < CladosConstant.SCALARGRADE | p1 > getBladeCount())
			return null;
		return bladeList.get(p1);
	}

	@Override
	public IntStream gradeStream() {
		return IntStream.rangeClosed(0, gradeCount);
	}

	@Override
	public int hashCode() {
		return 137 + gradeCount;
	}

	@Override
	public LongStream keyStream() {
		return bladeList.stream().mapToLong(blade -> blade.key());
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
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
		rB.append(indent).append("\t<Grades count=\"").append(getGradeCount() + "\">\n");
		for (short k = 0; k <= gradeCount - 2; k++) // loop to get all but the highest grade
			rB.append(indent).append("\t\t<Grade number=\"").append(k).append("\" range=\"").append(gradeList.get(k))
					.append("-").append((gradeList.get(k + 1) - 1)).append("\" />\n");
		// Handle last grade separate. There is no k+1 index for the largest grade
		rB.append(indent).append("\t\t<Grade number=\"").append((getGradeCount() - 1)).append("\" range=\"")
				.append(gradeList.get(gradeCount - 1)).append("-").append(gradeList.get(gradeCount - 1))
				.append("\" />\n");
		rB.append(indent).append("\t</Grades>\n");
		// ------------------------------------------------------------------
		rB.append(indent).append("\t<Blades count=\"").append(getBladeCount()).append("\">\n");
		for (short k = 0; k < bladeList.size(); k++) // Appending blades
			rB.append(Blade.toXMLString(bladeList.get(k), indent + "\t\t"));
		rB.append(indent).append("\t</Blades>\n");
		// ------------------------------------------------------------------
		rB.append(indent).append("</Basis>\n");
		return rB.toString();
	}

	/**
	 * This is a validator detects blade out of range issues. If one tries to name a
	 * blade by its index, it is always possible for the offered integer to be out
	 * of range.
	 * 
	 * @param pIn Short representing the integer index of the blade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	@Override
	public final boolean validateBladeIndex(short pIn) {
		return (pIn >= CladosConstant.SCALARGRADE & pIn < getBladeCount());
	}

}
