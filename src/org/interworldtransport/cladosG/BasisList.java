/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.BasisList<br>
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
 * ---org.interworldtransport.cladosG.BasisList<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.interworldtransport.cladosGExceptions.BladeOutOfRangeException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.interworldtransport.cladosGExceptions.GradeOutOfRangeException;

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
 * The bladeCount is computed as (1 << gradeCount - 1) instead of stored. It is
 * reported as an integer though that DOES limit the size of a basis to 31
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
public final class BasisList {

	/**
	 * This is a validator to help ensure no construction occurs outside the
	 * currently supported range of generators.
	 * 
	 * @param pGens Short representing the number of unique algebraic directions
	 * @return boolean True if parameter in the supported range [0, 14]
	 */
	public static final boolean validateSize(int pGens) {
		return (pGens >= CladosConstant.BLADE_SCALARGRADE & pGens <= CladosConstant.BLADE_MAXGRADE);
	}

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
	public static final BasisList using(byte numberOfGenerators) throws GeneratorRangeException {
		return new BasisList(numberOfGenerators);
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
	 * This list enables a lookup technique on the blades in bladeList. The keys
	 * MUST be in the same order as the blades.
	 */
	private final ArrayList<Long> keyList;

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
	public BasisList(byte pGens) throws GeneratorRangeException {
		if (!validateSize(pGens))
			throw new GeneratorRangeException("Supported range is 0<->14 using 8 bit integers");

		gradeCount = (byte) (pGens + 1);
		gradeList = new ArrayList<Integer>(gradeCount);
		bladeList = new ArrayList<Blade>(1 << pGens);
		keyList = new ArrayList<Long>(1 << pGens);
		if (pGens == 0) {
			gradeList.add(Integer.valueOf(0));
			bladeList.add(Blade.createScalarBlade(pGens).setBasisIndex(1));
		} else if (pGens == 1) {
			bladeList.add(Blade.createScalarBlade(pGens).setBasisIndex(0));
			gradeList.add(Integer.valueOf(0));
			bladeList.add(Blade.createPScalarBlade(pGens).setBasisIndex(1));
			gradeList.add(Integer.valueOf(1));
		} else {
			EnumSet<Generator> offer = EnumSet.range(Generator.E1, Generator.get(pGens));
			TreeSet<Blade> sorted = new TreeSet<>(); // Expects things that have a natural order
			for (EnumSet<Generator> pG : powerSet(offer))
				sorted.add(new Blade(pGens, pG)); // Adds in SORTED ORDER because... TreeSet
			sorted.iterator().forEachRemaining(b -> {
				bladeList.add(b);
				keyList.add(Long.valueOf(b.key()));
				b.setBasisIndex(bladeList.indexOf(b)+1);
			}); // exploit already sorted.
		}
		fillGradeList();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasisList other = (BasisList) obj;
		if (gradeCount != other.gradeCount)
			return false;
		return true;
	}

	public int find(Blade pIn) {
		for (Blade b : bladeList)
			if (b.equalsAbs(pIn))
				return bladeList.indexOf(b);
		return -1;
	}

	public int findKey(Long pIn) {
		for (Long l : keyList)
			if (l.equals(pIn))
				return keyList.indexOf(l);
		return -1;
	}

	/**
	 * Return the row of shorts at p1 in the array holding the basis. One row is one
	 * blade of an algebra.
	 * 
	 * NOTE this is a backwards compatibility method and really shouldn't be used
	 * for anything serious in clados v2. It WILL go away fairly soon. Just get a
	 * blade from the bladeList instead. (Nothing in clados uses this.)
	 * 
	 * @param p1 short This is the desired blade number within the basis.
	 * 
	 * @return short[] This is the returned blade.
	 * @throws BladeOutOfRangeException
	 */
	@Deprecated
	public short[] getBlade(short p1) throws BladeOutOfRangeException {
		if (p1 < CladosConstant.BLADE_SCALARGRADE | p1 > getBladeCount())
			throw new BladeOutOfRangeException(null, "Requested Blade # {" + p1 + "} is not in basis");

		short[] tBS = new short[gradeCount - 1]; // prepare the array to return
		EnumSet<Generator> tG = bladeList.get(p1).getGenerators(); // new representation of blade

		// Now convert new representation of blade to the old one
		byte counter = (byte) (gradeCount - 1 - tG.size()); // find first non-zero slot to fill
		Iterator<Generator> cursor = tG.iterator(); // and iterate through new representation
		while (cursor.hasNext()) {
			tBS[counter] = cursor.next().ord; // to fill the old representation being returned
			counter++;
		}
		return tBS;
	}

	public int getBladeCount() {
		return (1 << gradeCount - 1);
	}

	/**
	 * There is a similar sounding method in the old basis that isn't being used, so
	 * it is replaced by this one that returns the enumerated set of generators in
	 * the requested blade.
	 * 
	 * @param p1 short integer pointing to the blade in the internal list
	 * @return EnumSet of Generator representing the blade without the context
	 *         necessary for knowing much about the enclosing space for the blade.
	 * @throws BladeOutOfRangeException
	 */
	public EnumSet<Generator> getBladeSet(short p1) throws BladeOutOfRangeException {
		if (p1 < CladosConstant.BLADE_SCALARGRADE | p1 > getBladeCount())
			throw new BladeOutOfRangeException(null, "Blade key requested {" + p1 + "} is not in the basis.");
		return bladeList.get(p1).getGenerators();
	}

	public byte getGradeCount() {
		return gradeCount;
	}

	/**
	 * This method simply delivers the otherwise private grade range list. Useful
	 * for testing purposes, but should be avoided as much as possible.
	 * 
	 * @return ArrayList of Integers A list of grades boxed as Integers.
	 */
	public ArrayList<Integer> getGrades() {
		return gradeList;
	}

	/**
	 * Get an index to the first blade of grade specified by the parameter.
	 * 
	 * NOTE this method will shift away from short integers to some other primitive
	 * in the near future.
	 * 
	 * @param p1 byte This is for choosing which grade index range to return.
	 * @return int Index within the basis where requested grade starts.
	 * @throws GradeOutOfRangeException This exception is thrown if the requested
	 *                                  grade is NOT between 0 and gradeCount
	 *                                  inclusive.
	 */
	public int getGradeStart(byte p1) throws GradeOutOfRangeException {
		if (p1 < CladosConstant.BLADE_SCALARGRADE | p1 > gradeCount)
			throw new GradeOutOfRangeException(null, "Grade requested {" + p1 + "} is not in the basis.");
		return gradeList.get(p1).intValue();
	}

	/**
	 * Return the long at p1 in the EddingtonKey array.
	 * 
	 * @param p1 short This is the desired key at p1 .
	 * @return long
	 * @throws BladeOutOfRangeException This exception is thrown if the requested
	 *                                  blade is NOT between 0 and bladeCount
	 *                                  inclusive.
	 */
	public long getKey(int p1) throws BladeOutOfRangeException {
		if (p1 < CladosConstant.BLADE_SCALARGRADE | p1 > getBladeCount())
			throw new BladeOutOfRangeException(null, "Requested Blade # {" + p1 + "} is not in basis");
		return bladeList.get(p1).key();
	}

	/**
	 * Return the integer array holding the EddingtonKey's.
	 * 
	 * @return long[]
	 */
	@Deprecated
	public long[] getKeys() {
		long[] vKey = new long[getBladeCount()];
		ArrayList<Long> keyList = new ArrayList<>(getBladeCount());
		bladeList.stream().forEachOrdered(b -> keyList.add(b.key()));
		for (Long l : keyList)
			vKey[keyList.indexOf(l)] = l.longValue();
		return vKey;
	}

	public Blade getSingleBlade(int p1) {
		if (p1 < CladosConstant.BLADE_SCALARGRADE | p1 > getBladeCount())
			return null;
		return bladeList.get(p1);
	}

	@Override
	public int hashCode() {
		return 137 + gradeCount;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @return String
	 */
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
	public final boolean validateBladeIndex(short pIn) {
		return (pIn >= CladosConstant.BLADE_SCALARGRADE & pIn < getBladeCount());
	}

	/**
	 * Set the array used for keeping track of where grades start in the Basis
	 * array. GradeRange[j] is the first position for a blade of grade j. This
	 * function used to use factorials and binomial coefficients to find how many
	 * blades existed for a particular grade. Now it uses the Key and its
	 * Base(GradeCount) encoding to do it better and faster.
	 * 
	 * Do not call this function unless the vBasis is properly sorted first. That
	 * sort causes the vKey array to sort in ascending order and that is crucial to
	 * this function. Any other arrangement utterly invalidates the gradeRange
	 * array.
	 * 
	 */
	private void fillGradeList() {
		gradeList.add(0); // Will be auto-boxed by JVM.
		gradeList.add(1); // Will be auto-boxed by JVM.

		int m = 0;
		for (byte j = 2; j < gradeCount - 1; j++) {
			// Loop through the grades skipping scalar, vector, and pscalar
			long test = (long) Math.pow(gradeCount, j - 1);// Ceiling Key for grade j-1

			while (m < (1 << (gradeCount - 1))) {
				if (bladeList.get(m).key() > test) {
					gradeList.add(m); // Will be auto-boxed by JVM.
					break; // Found first key above ceiling. Move to next grade.
				}
				m++; // Otherwise move to next blade.
			}
		}
		gradeList.add((1 << (gradeCount - 1)) - 1); // Just bladeCount - 1
	}
}
