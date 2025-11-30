/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Basis<br>
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

import static org.interworldtransport.cladosG.CladosConstant.*;

/**
 * All geometry objects within the cladosG package have elements that form a
 * basis to span the vector space related to the algebra. This basis is
 * represented in the algebra as various products of the generators. This class
 * representation uses sets of generators as Blades and a list of Blades. It
 * also maintains a few methods that help manipulate them.
 * <br><br>
 * The gradeCount is tracked using byte integers as it isn't expected that
 * anyone will work with algebras of more than 126 generators any time soon.
 * <br><br>
 * The bladeCount is computed as (1 &lt;&lt; gradeCount - 1) instead of stored.
 * It is reported as an integer though that DOES limit the size of a basis to 31
 * generators. Again, it isn't expected anyone will need more any time soon.
 * <br><br>
 * The bladeList is stored as an ArrayList of Blades that should be no longer
 * than bladeCount. There is nothing to stop it from being longer or shorter,
 * though, so this is a potential source of error. It really SHOULD be
 * immutable once constructed correctly. Same goes for the Blades contained in
 * the ArrayList.
 * <br><br>
 * Blade keys are now stored inside the Blades. There is no separate array for
 * them in the Basis. The primary use for keys is sorting the Blades and
 * detecting which blade is found at the head of the list for each grade. This
 * is useful when multiplying multivectors that are sparsely populated in terms
 * of their coefficients. The multiplication algorithm can skip over grades not
 * present in the multivector and skip potentially large blocks in the sums with
 * vanishing contributions. However, sparseness is detected using a
 * multivector's key and NOT a Blade's key. Once Blades are sorted into a basis,
 * blade keys are only used to name them in maps.
 * <br><br>
 * NOTE that Blade keys are currently kept as long integers. The key for a
 * pscalar in a 14 generator basis is 2234152501943160L. For 15 generator
 * algebras the key is 81985529216486896L. Long integers can't hold keys much
 * larger than that. At some point keys will shift to Java's BigInteger class
 * and impose another performance penalty. Not yet, though. Best practice would
 * be to avoid computations demanding heavy use of blade keys.
 * <br><br>
 * The data in this class is stored in objects instead of arrays of primitives.
 * This is intentional. Doing so allows a system to lay them out in memory in
 * any way it finds convenient. There IS an overhead associated with this plan,
 * but it is in recognition that virtualization puts distance between an
 * application and the hardware on which it runs.
 * <br><br>
 * It is expected that Basis objects will be cached, though. There is no reason
 * to create copies within running applications. One MAY do so as no singleton
 * enforcement occurs, but every basis of the same number of generators passes
 * the equality test. A convenient cache already exists in the singleton
 * GCache.
 * <br><br>
 * The choice a limit of 15 generators produces a maximum basis size of 32,768.
 * More can be used, but one must change the 'magic numbers' in the
 * CladosConstant class and recompile.
 * <br><br>
 * There IS a sort buried in the constructor for a Basis. After Blades are
 * generated using the private powerSet method, they are sorted on the 'natural
 * order'. Blades implement Comparable and use their keys for compareTo(). THAT
 * is why blade keys are now buried in Blade objects. Doing so enables one line
 * of code here to use Java's Collections class to decide what sorting algorithm
 * to use. That means this class no longer implements its own sort algorithm. It
 * is likely the developer community is much better at writing sort algorithms,
 * so this is recognition of that reality.
 * <br><br>
 * This class probably should be implemented as a Java enumeration. It might be
 * some day. The problem with that is construction time scales as O(N^2).
 * Pre-construction of small basis objects makes good sense, but larger ones
 * become problematic especially for users who never intend to use them. If
 * one's primary interest involves Euclidian 3-space, what need is there of a
 * basis with 14 geometric directions and 16384 linear dimensions that takes a
 * sizeable fraction of a minute to construct? So it is suggested that Best
 * Practice among those who build physical models is to prebuild what you need
 * and load it all to the cache. Use GBuilder to do it for you. 
 * That said, it is obvious why the copy constructor was removed.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Basis implements CanonicalBasis {

	/**
	 * This is just a factory method to help name a particular constructor. It is
	 * used in place of 'new Basis(byte)'.
	 * <br>
	 * @param numberOfGenerators Byte representing unique algebraic directions
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 */
	public static final Basis using(byte numberOfGenerators) {
		return new Basis(Generator.get(numberOfGenerators));
	}

	/**
	 * This is just a factory method to help name a particular constructor. It is
	 * used in place of 'new Basis(byte)'.
	 * <br>
	 * @param mxBlade Generator representing a pscalar with all directions.
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 */
	public static final Basis using(Generator mxBlade) {
		return new Basis(mxBlade); 
	}

	/*
	 * Deliver the powerset of generators present in the pscalar of a basis. Members
	 * of this set ARE the other blades.
	 * <br>
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
	 * <br>
	 * Every list entry is a blade. ArrayList DOES allow for null entries, but
	 * methods in this class MUST prevent that from happening.
	 * <br>
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
	 * <br>
	 * This list enables the CladosG library to avoid implementing a factorial
	 * method to repeatedly calculate binomial coefficients. Construction of the
	 * basis provides the information, so for the sake of efficiency they are 
	 * stored here for later use.
	 * <br>
	 * The size of this list is always the same as the grade count.
	 * <br>
	 * grade 0: The first entry always points to the start as that is where the
	 * first (and only) scalar blade is found.
	 * <br>
	 * grade 1: The second entry always points to the start of the one-blades.
	 * <br>
	 * grade 2: N+2 where N is the number of generators is where two-blades start.
	 * <br>
	 * grade N: The last entry always points to the blade that represents the
	 * pscalar in the basis.
	 * <br>
	 * All other entries in this list are calculated using a Blade's key.
	 */
	private final ArrayList<Integer> gradeList;

	/**
	 * This map connects a Blade's internal key to its indexed location in the
	 * basis. This map is what allows us to put the indexed position in the
	 * multiplicaton table instead of the blade's key which is longer and would 
	 * inflate the size of the Cayley table unnecessarily.
	 * <br>
	 * The index "1" always refers to the scalar (no generators blade) of the basis. 
	 * The index 2^(p+q+r) always refers to the pscalar of the basis.
	 * <br>
	 * Nothing should be indexed at "0" because a "0" in the Cayley Table is used 
	 * to refer to an outcome of products of degenerate generators.
	 */
	private final TreeMap<Long, Integer> keyIndexMap;

	/**
	 * This is the basic constructor. It takes the number of generators as its only
	 * parameter. It can be instantiated on its own for demonstration purposes, but
	 * it has no awareness of the addition and multiplication operations in an
	 * algebra, so all it does is show the basis.
	 * <br>
	 * @param pGens byte This is the number of generators that make up the basis
	 */
	public Basis(byte pGens) {
		this(Generator.get(pGens));
	}

	/**
	 * This is the constructor that takes one generator and assumes all generators with smaller ordinals
	 * are to be used to create the basis. For example, offering E3 will result in a basis for {E1, E2, E3}.
	 * <br><br>
	 * It is not possible to construct a 'no generator' algebra with this method. Use Basis(byte pGens) for that.
	 * <br><br>
	 * @param pGen Generator that is the largest one in the set of generators to use to create the basis
	 */
	public Basis(Generator pGen) {
		if (pGen != null) {
			gradeCount = (byte) (pGen.ord + 1);									//Initializing starts here
			gradeList = new ArrayList<Integer>(gradeCount);						//Exactly as many entries as basis grades
			bladeList = new ArrayList<Blade>(1 << pGen.ord);					//Exactly as many entries as 2^(gradeCount-1)
			keyIndexMap = new TreeMap<>();										//Relates the two blade keys
			
			switch (pGen.ord) {													//Now build the bladeList
				case 1 -> {
					bladeList.add(Blade.createScalarBlade(Generator.E1));		//One generator is easier
					gradeList.add(Integer.valueOf(0));						//to set up manually rather
					keyIndexMap.put(0L, 1);							//than use loops.
					bladeList.add(Blade.createPScalarBlade(Generator.E1));
					gradeList.add(Integer.valueOf(1));
					keyIndexMap.put(1L, 2);
					break;
				}
				default -> {													//Two or more generators -> use loops
					EnumSet<Generator> offer = EnumSet.range(GENERATOR_MIN, pGen); //Set of generators to use for basis
					TreeSet<Blade> sorted = new TreeSet<>(); 					//Natural order! This is how sort is avoided.
					for (EnumSet<Generator> pG : powerSet(offer))				//Magic(!) creating every blade as a set
						sorted.add(new Blade(pGen, pG)); 						//Adds in SORTED ORDER because... TreeSet
					
					sorted.iterator().forEachRemaining(blade -> { 				//Iterator works in ascending order causing
						bladeList.add(blade); 									//bladeList to be in key ascending order
						keyIndexMap.put(blade.key(), 							//as blades are added to key map.
										Integer.valueOf(bladeList.indexOf(blade) + 1));
					});
																				//Now build the gradeList
					gradeList.add(Integer.valueOf(0)); 						//First entry is 0-blade grade (scalar)
					gradeList.add(Integer.valueOf(1)); 						//Second entry is 1-blade grade
					IntStream.range(2, gradeCount - 1)			//IntStream handles 2-blade to pscalar-1
						.forEachOrdered(i -> {gradeList.add(					//add starting indexes in order...
							keyIndexMap.ceilingEntry(							//getting least key greater than or equal to
									//Long.valueOf((long) Math.pow(gradeCount, i - 1))).getValue() - 1
									pow(gradeCount, i - 1)).getValue() - 1		//a possible key slightly below or at the next grade
							);
						}); 													//keyIndexMap links a blade's bladeKey to its bladeIndex 
																				//Useful for the Cayley table which stores bladeIndex values.
					gradeList.add(getBladeCount() - 1); 						//Last entry is pscalar grade
				}
			}
		} else {
			gradeCount = 1;
			gradeList = new ArrayList<Integer>(gradeCount);
			gradeList.add(Integer.valueOf(0));
			bladeList = new ArrayList<Blade>(1);
			bladeList.add(Blade.createBlade((byte)0));
			keyIndexMap = new TreeMap<>();
			keyIndexMap.put(0L, 1);
		}
	}

	/**
	 * The stream returned contains blades that match the grade requested in the
	 * parameter.
	 * <br>
	 * There are silent fail behaviors in this method. If the requested grade falls
	 * outside the range expected in the basis, the returned stream will be empty.
	 * This happens for negative grades and grades larger than the pscalar.
	 * <br>
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
		if (this == obj) 							return true;
		if (obj == null) 							return false;
		if (getClass() != obj.getClass())			return false;
		if (gradeCount != ((Basis) obj).gradeCount)	return false;
													return true;
	}

	/**
	 * Old-fashioned 'find' method that reports index location in the basis where a
	 * Blade is found OR -1 if it wasn't found.
	 * <br>
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
	 * <br>
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
	 * <br>
	 * BEWARE | This method used to throw an exception when the parameter was out of
	 * range. Now it just returns an empty set of generators which will look like a
	 * scalar blade to the unwary. This change allows for a possible silent fail
	 * someone's code, but it also allows for non-exception handling approaches to
	 * termination of loops and streams.
	 * <br>
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
	 * <br>
	 * @return byte
	 */
	@Override
	public byte getGradeCount() {
		return gradeCount;
	}

	/**
	 * This method simply delivers the otherwise private grade range list. Useful
	 * for testing purposes, but should be avoided as much as possible.
	 * <br>
	 * @return ArrayList of Integers A list of grades boxed as Integers.
	 */
	@Override
	public ArrayList<Integer> getGrades() {
		return gradeList;
	}

	/**
	 * Get an index to the first blade of grade specified by the parameter.
	 * <br>
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
	 * <br>
	 * There is no telling what blade is at the indexed location.
	 * This just returns the key for it assuming it is there.
	 * <br>
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
	 * <br>
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
	 * <br>
	 * Note that a null can be returned from here if the index is out of range.
	 * <br>
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
	public boolean hasBlade(Blade pB) {
		return keyIndexMap.containsKey(pB.key());
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
	 * <br>
	 * @param pB The Basis to export as XML
	 * @param indent String of 'tab' characters that help space the output correctly
	 *               visually. It's not actually necessary except for human
	 *               readability of the output.
	 * @return String
	 */
	public static String toXMLString(Basis pB, String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Basis>\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("\t<Grades count=\"")
			.append(pB.getGradeCount() + "\">\n");
		for (int k = 0; k <= pB.gradeCount - 2; k++) // loop to get all but the highest grade
			rB.append(indent)
				.append("\t\t<Grade number=\"")
				.append(k)
				.append("\" range=\"")
				.append(pB.gradeList.get(k))
				.append("-")
				.append((pB.gradeList.get(k + 1) - 1))
				.append("\" />\n");
		// Handle last grade separate. There is no k+1 index for the largest grade
		rB.append(indent)
			.append("\t\t<Grade number=\"")
			.append((pB.getGradeCount() - 1))
			.append("\" range=\"")
			.append(pB.gradeList.get(pB.gradeCount - 1))
			.append("-")
			.append(pB.gradeList.get(pB.gradeCount - 1))
			.append("\" />\n");
		rB.append(indent)
			.append("\t</Grades>\n");
		// ------------------------------------------------------------------
		rB.append(indent)
			.append("\t<Blades count=\"")
			.append(pB.getBladeCount())
			.append("\">\n");
		for (int k = 0; k < pB.bladeList.size(); k++) // Appending blades
			rB.append(Blade.toXMLString(pB.bladeList.get(k), indent + "\t\t"));
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
	 * <br>
	 * @param pIn Short representing the integer index of the blade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	@Override
	public final boolean validateBladeIndex(int pIn) {
		return (pIn >= SCALARGRADE & pIn < getBladeCount());
	}

	/**
	 * This is a validator detects grade out of range issues. If one tries to name a
	 * grade by its index, it is always possible for the offered integer to be out
	 * of range.
	 * <br>
	 * @param pIn int representing the integer index of the grade
	 * @return boolean True if parameter in the supported range [0, bladeCount]
	 */
	@Override
	public final boolean validateGradeIndex(int pIn) {
		return (pIn >= SCALARGRADE & pIn < getGradeCount());
	}

}