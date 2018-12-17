/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.Basis<br>
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
 * ---com.interworldtransport.cladosG.Basis<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosGExceptions.*;

/**
 * All geometry objects within the cladosG package have elements that form a
 * basis to span the vector space related to the algebra. This basis is
 * represented in the algebra as various products of the generators. This class
 * holds a representation of all those products of generators and a few methods
 * that help to manipulate the list of indexes.
 * <p>
 * The data in this class is stored in shorts instead of ints. This is meant to
 * save some memory in applications where a number of geometric objects must be
 * active at the same time. In this case, memory footprint will influence method
 * speeds due to the amount of information stored in each geometric object like
 * a monad. In algebras with a moderate number of generators this will matter a
 * great deal.
 * <p>
 * The choice of shorts limits this class to algebras with between 1 and 14
 * generators inclusive (with 16284 blades) because 2^15-1 is the max for 
 * signed shorts and 15 generators produce 2^15 blades. Switching to int's 
 * enables up to 30 generators and 1,073,741,824 blades. Switching to long's 
 * enables up to 62 generators and 4,611,686,018,427,387,904 blades. Perhaps 
 * when that kind of hardware is available, this class should be updated. 8)
 * <p>
 * There is a speed improvement for sorting keys in an Eddington basis
 * if we use a 3-way median quicksort algorithm instead of the heapsort
 * algorithm. This improvement comes at the cost of memory used, so there
 * might be a penalty in memory allocation overhead. Heapsort is stingy
 * with extra memory being used, so careful implementation of the 
 * algorithm should avoid the overhead. Heapsort has been chosen here to
 * leave room for the physical model being implemented.
 * <p>
 * The penalty for heap sort for 4 generator algebras in establishing
 * a basis is about 2x the number of swaps, but the total swaps amount
 * to about 60. On even an old machine, this will be lightning fast.
 * That means it isn't worth worrying about which sort is faster in
 * filling a basis.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class Basis
{
	/**
	 * This is just a factory method to help name a particular constructor.
	 * It is used in place of 'new Basis(short)'.
	 * 
	 * @param numberOfGenerators Short representing unique algebraic directions
	 * @return Basis Factory method returns a Basis with numberOfGenerators
	 * @throws CladosMonadException
	 */
	public static final Basis using(short numberOfGenerators)
			throws CladosMonadException
	{
		return new Basis(numberOfGenerators);	
	}
	/**
	 * This is just a factory method to help name the copy constructor for Basis.
	 * It is used in place of 'new Basis(Basis)'.
	 * 
	 * @param pBasis Basis object to be copied
	 * @return Basis Factory method returns a new, independent pBasis copy
	 */
	public static final Basis copyOf(Basis pBasis)
	{
		return new Basis(pBasis);
	}
	/**
	 * This is the maximum number of generators (14) a basis can support for now.
	 * This maximum is a result of the choice to represent bladeCount with 16-bit
	 * signed shorts.
	 */
	public static final short MAX_GEN=14;
	/**
	 * This integer is the number of grades in the algebra. It is one more than
	 * the number of generators and is used often enough to be worth keeping.
	 */
	private final short	gradeCount;
	/**
	 * This integer is the number of independent blades in an algebra. It is a
	 * count of the number of vBasis rows and is used often enough to be worth
	 * keeping around.
	 */
	private final short	bladeCount;
	/**
	 * This array holds the representation of the vBasis. The vBasis is a
	 * complete list of unique blades for an algebra.
	 * 
	 * A column entry in a row is a generator (short) used to construct a blade.
	 * The generators in a row are sorted in ascending order. Only positive,
	 * non-zero integers represent generators.
	 * 
	 * A row of the array is a blade. The number of rows is the linear
	 * dimension of an algebra using this basis because only one permutation of
	 * each distinct subset of generators is found in the rows of the array.
	 * 
	 * So... this is the Eddington Basis.
	 */
	private final short[][]	vBasis;
	/**
	 * This array holds the integer keys for each member of the Basis. The key
	 * for each blade is an integer built from sums of powers of the number of
	 * generators.
	 * 
	 * vKey[k]={Sum over m} vBasis[k][m]* (GradeCount)^(GradeCount-1-m);
	 * 
	 * The other kind of key that gets used in other implementations is a base 2
	 * key that is BladeCount digits long. The key used here holds the same
	 * information in a shorter integer by using a higher numeric base.
	 * BladeCount = 2 ^ (GradeCount-1) is the maximum entry in the vBasis array,
	 * thus it is the lowest possible numeric base to use for vKey to avoid
	 * overlap of vKeys across grades. The actual numeric base used is
	 * GradeCount ^ (GradeCount-1) as this ensures algebras with one generator
	 * don't require special handling.
	 * 
	 * The vKey has nothing to do with geometric algebra. It is simply easier to
	 * compare keys than actual Basis elements. This trick leads to some
	 * efficiencies in calculations and sorting because basis elements of
	 * similar grade sort together.
	 */
	private final long[]	vKey;
	/**
	 * This array is used for keeping track of where grades start and stop in
	 * the vBasis. The difference between GradeRange[k] and GradeRange[k+1] is
	 * binomial(GradeCount-1, k) = (GradeCount-1)! / (k! * ((GradeCount-1)-k)!)
	 * GradeRange[j] is the first position for a blade of grade j.
	 * 
	 * This array enables the CladosG library to avoid implementing a factorial
	 * method to determine binomial coefficients. Construction of the basis
	 * creates the information for coefficients that would get calculated in
	 * regular methods of a monad. Efficiency demands that they be stored here
	 * for later use.
	 * 
	 * The size of the gradeRange array is always generators+2.
	 * gradeRange[0] is always 0
	 * gradeRange[1] is always 1
	 * gradeRange[gradeCount-1] is always bladeCount-1
	 * The way to interpret these fixed entries is scalars are always in the
	 * first slot of a basis, vectors always start in the second slot, and the
	 * pscalar is always found in the last slot. All other entries in this 
	 * array have to be calculated and clados does it using the vKey.
	 */
	private final short[]	gradeRange;

	/**
	 * This is the basic constructor. It takes the number of generators as it's
	 * only parameter. It can be instantiated on it's own for demonstration
	 * purposes, but it has no awareness of the addition and multiplication
	 * operations in an algebra, so all it does is show the basis.
	 * 
	 * @param pGens
	 * 		short This is the number of generators that make up the basis
	 * @throws 
	 * 		CladosMonadException Exception thrown if not 0 to 14 generators
	 */
	public Basis(short pGens) throws CladosMonadException
	{
		if (pGens <0 | pGens > MAX_GEN)
			throw new CladosMonadException(null, "Supported range is 0<->14 using 16 bit integers");
		
		gradeCount = (short) (pGens + 1);
		bladeCount = (short) (1 << pGens);
		//bladeCount = (short) Math.pow(2, pGens);
		vKey = new long[bladeCount];
		gradeRange = new short[gradeCount];
		if (pGens==0)
		{
			vBasis = new short[bladeCount][gradeCount];
			vBasis[0][0]=(short) 0;
			vKey[0]=0;
			gradeRange[0]=0;
		}
		else
		{
			vBasis = new short[bladeCount][pGens];
			fillBasis();
			fillGradeRange();
		}
	}

	/**
	 * This is the copy constructor. It takes a previously generated Basis and
	 * makes another independent copy.
	 * 
	 * @param pB
	 *            Basis
	 */
	public Basis(Basis pB)
	{
		gradeCount = pB.getGradeCount();
		bladeCount = pB.getBladeCount();
		vBasis = pB.getBasis();
		vKey = pB.getBasisKey();
		gradeRange = pB.getGradeRange();
	}

	/**
	 * Return the number of grades in the basis. Since there is no geometry in
	 * the basis this is a measure of the number of distinct generator subset
	 * types that can be formed where the element count determines the type.
	 * Because the empty set includes no generators, GradeCount will always be
	 * one more than the number of generators.
	 * 
	 * @return short
	 */
	public short getGradeCount()
	{
		return gradeCount;
	}

	/**
	 * Return the number of independent blades in the basis. This is the same as
	 * the linear dimension of an algebra that uses this basis.
	 * 
	 * @return short
	 */
	public short getBladeCount()
	{
		return bladeCount;
	}

	/**
	 * Return the array holding the EddingtonBasis. It is a list of linearly
	 * independent objects that can be generated through multiplication of the
	 * generators.
	 * 
	 * @return short[][]
	 */
	public short[][] getBasis()
	{
		return vBasis;
	}

	/**
	 * Return the row of shorts at p1 in the array holding the basis. One row is
	 * one blade of an algebra.
	 * 
	 * @param p1
	 *            short This is the desired blade number within the basis.
	 * 
	 * @return short[] This is the returned blade.
	 */
	public short[] getBasis(short p1)
	{
		return vBasis[p1];
	}

	/**
	 * Return the short at (x,y) in the array holding the basis.
	 * @param p1
	 * 				short This is the desired blade within the basis.
	 * @param p2
	 * 				short This is the desired index within the p1 blade.
	 * @return short
	 */
	public short getBasis(short p1, short p2)
	{
		return vBasis[p1][p2];
	}

	/**
	 * Return the integer array holding the EddingtonKey's.
	 * 
	 * @return long[]
	 */
	public long[] getBasisKey()
	{
		return vKey;
	}

	/**
	 * Return the long at p1 in the EddingtonKey array.
	 * @param p1
	 * 				short This is the desired key at p1 .
	 * @return long
	 */
	public long getBasisKey(short p1)
	{
		return vKey[p1];
	}

	/**
	 * Get the array used for keeping track of where grades start in the Basis
	 * array.
	 * 
	 * @return short[]
	 */
	public short[] getGradeRange()
	{
		return gradeRange;
	}

	/**
	 * Get the array used for keeping track of where grades start in the Basis
	 * array.
	 * 
	 * @param p1
	 * 			short This is for choosing which grade index range to return. 
	 * @return short
	 */
	public short getGradeRange(short p1)
	{
		return gradeRange[p1];
	}

	/**
	 * Set the array used for representing the Eddington Basis. This method
	 * produces an integer array list of Eddington numbers in order of ascending
	 * grade and ascending component.
	 * 
	 * This method should only be called once the Basis is initialized.
	 * Construction of the full basis array scales as grades * blades =
	 * generators * 2^generators, so it could take a long time and a lot of
	 * memory for a large basis. Efforts to streamline code and memory footprint
	 * in this method could have a large impact.
	 */
	private void fillBasis()
	{
		short tempPermTest = 0;
		short tempPermFilter = 0;
		short q = (short) (gradeCount - 2);
		short k=0;
		short m=0;
		short[] tempGens = new short[gradeCount - 1];
		
		for (k = 0; k < bladeCount; k++) 
		// basis row counter 0 thru BladeCount-1
		{
			tempPermTest = k; 
			// Starts as row counter but gets decremented whenever
			// a particular generator is NOT chosen for the k'th row
			// of the basis. It is decremented by the size of the filter
			// that detects when to drop a generator from the list.
			
			for (m = 0; m < gradeCount - 1; m++) 
			// generator (column) counter 0 thru GradeCount-2
			{
				tempPermFilter = (short) (1 << gradeCount - 2 - m);
				//tempPermFilter = (short) Math.pow(2, gradeCount - 2 - m);
				// This is the filter that is used to detect when to drop
				// a generator from the k'th row of the basis.
				
				if (tempPermTest < tempPermFilter)
				{
					tempGens[m] = (short) (m+1); // m+1'th generator (Never 0)
					// Writing to tempGens but will copy back to vBasis shortly
				}
				else
				{
					tempPermTest -= tempPermFilter; 
					// decrement tempPermTest because the m'th generator
					// was dropped from this row of the basis.
					
					tempGens[m] = 0;
					// and here we actually drop the m'th generator
				}
			}
			// tempGens filled but unsorted. If generator m appears in a row 
			// it will always appear in column m-1. All that is needed next 
			// is to slice the 0's out of the row. We start on the right of 
			// tempGens and look for columns that aren't zero and copy them 
			// back to vBasis.
			
			q = (short) (gradeCount - 2);
			for (m = (short) (gradeCount - 2); m>-1; m--) 
			// generator (column) counter GradeCount-2 through 0 [decreasing]
			// Only one pass is needed
			{
				if (tempGens[m] != 0) // Found one to copy.
				{
					vBasis[k][q] = tempGens[m];
					// This causes vBasis to fill from the bottom up.
					// This is closer to how it will sort out later.
					tempGens[m] = 0; // Clears tempGens for use with next k.
					q--; // Slide left in vBasis for next possible entry.
				}
			}
			// vBasis row filled properly. The generators used in row k will
			// appear in ascending order with 0's padding the lowest columns. 
			// Thus a vector blade will have a single generator in the 
			// GradeCount-2 column and 0's in the earlier columns.
		
			// Base (GradeCount) representation of Eddington Number
			// Ex: 3 generators implies Base-4 keys stuffed into Base-10 array
			// Right-most generator is the one's digit
			// Middle generator is the 4's digit
			// Left-most generator is the 16's digit
			// Ex: 8 generators implies Base-9 keys stuffed into a Base-10 array
			
			vKey[k] = 0;
			for (m = 0; m < gradeCount - 1; m++)
				vKey[k] += (long) vBasis[k][m]*Math.pow(gradeCount, gradeCount -2-m);
		}
		
		// All Eddington Numbers are written. The Keys are written too.
		// It is time to sort the blades by key and call it a basis.
		// Sort method relies on Heap Sort.
		
		// Build heap (rearrange array) 
		for (k = (short) (bladeCount / 2 - 1); k >= 0; k--) 
			heapifyKey(vKey, bladeCount, k); 
		
		// One by one extract an element from heap 
		// working from the far end of the key array back to the top.
		for (k= (short) (bladeCount-1); k>=0; k--) 
		{ 
			// Move current root to end 
			long tempRoot = vKey[0]; 
			vKey[0] = vKey[k]; 
			vKey[k] = tempRoot; 
					
			// Move corresponding basis row with its key
			for (m = 0; m < gradeCount - 1; m++) // Swap the Basis rows.
			{
				short tempBasisSpot = vBasis[0][m];
				vBasis[0][m] = vBasis[k][m];
				vBasis[k][m] = tempBasisSpot;
			}

			// call max heapify on the reduced heap 
			heapifyKey(vKey, k, (short) 0); 
		} 
		// All Eddington Numbers are now written, calculated, and sorted.
	}

	/**
	 * Set the array used for keeping track of where grades start in the Basis
	 * array. GradeRange[j] is the first position for a blade of grade j. This
	 * function used to use factorials and binomial coefficients to find how
	 * many blades existed for a particular grade. Now it uses the Key and its
	 * Base(GradeCount) encoding to do it better and faster.
	 * 
	 * Do not call this function unless the vBasis is properly sorted first.
	 * That sort causes the vKey array to sort in ascending order and that is
	 * crucial to this function. Any other arrangement utterly invalidates
	 * the gradeRange array.
	 * 
	 */
	private void fillGradeRange()
	{
		gradeRange[0] = 0; // The scalar
		gradeRange[1] = 1; // The scalar
		gradeRange[gradeCount - 1] = (short) (bladeCount - 1); // The pscalar
		short m = 0;
		
		for (short j = 2; j < gradeCount - 1; j++) 
		// Loop through the grades skipping scalar, vector, and pscalar
		{
			long test = (long) Math.pow(gradeCount, j - 1); 
			// Ceiling Key for grade j-1
			
			while (m < bladeCount)
			{
				if (vKey[m] > test)
				{
					gradeRange[j] = m;
					break; // Found first key. Move to next grade.
				}
				// Otherwise move to next blade.
				m++;
			} // All blades searched
		} // All blades ranged as grades. Get along little dogie.
	}

	/**
	 * To heapify a subtree rooted with node i. 
	 * @param vKey long[]
	 * @param heapSize int
	 * @param point int
	 * 			re-useable index on vKey needed because we recurse
	 */
	private	void heapifyKey(long vKey[], short heapSize, short point) 
		{ 
			short largest = point; // Initialize largest as root 
			short left = (short) (2*point + 1);  
			short right = (short) (2*point + 2);  

			// If left child is larger than root 
			if (left < heapSize && vKey[left] > vKey[largest]) 
				largest = left; 

			// If right child is larger than largest so far 
			if (right < heapSize && vKey[right] > vKey[largest]) 
				largest = right; 
			
			// If largest is not root 
			if (largest != point) 
			{ 
				short tempBasisSpot;
				long swap = vKey[point]; 
				vKey[point] = vKey[largest]; 
				vKey[largest] = swap; 
				
				for (int m = 0; m < gradeCount - 1; m++) // Swap the Basis rows.
				{
					tempBasisSpot = vBasis[point][m];
					vBasis[point][m] = vBasis[largest][m];
					vBasis[largest][m] = tempBasisSpot;
				}
				// Recursively heapify the affected sub-tree 
				heapifyKey(vKey, heapSize, largest); 
			} 
		}
	
	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @return String
	 */
	public String toXMLString()
	{
		StringBuffer rB = new StringBuffer("<Basis>\n");

		rB.append("\t<Grades count=\"" + getGradeCount() + "\">\n");
		for (short k = 0; k <= gradeCount - 2; k++)
		{
			rB.append("\t\t<Grade number=\"" + k + "\" range=\""
							+ getGradeRange(k) + "-"
							+ (getGradeRange((short) (k + 1)) - 1) + "\" />\n");
		}
		rB.append("\t\t<Grade number=\"" + (getGradeCount() - 1)
						+ "\" range=\""
						+ getGradeRange((short) (gradeCount - 1)) + "-"
						+ getGradeRange((short) (gradeCount - 1)) + "\" />\n");
		rB.append("\t</Grades>\n");

		rB.append("\t<Blades count=\"" + getBladeCount() + "\">\n");
		for (short k = 0; k < bladeCount; k++) // Appending blades
		{
			rB.append("\t\t<Blade number=\"" + (k + 1) + "\" key=\""
							+ getBasisKey(k) + "\" generators=\"");
			for (short m = 0; m < gradeCount - 1; m++)
			{
				if (vBasis[k][m] > 0)
				{
					rB.append(getBasis(k, m));
					rB.append(",");
				}
			}
			if (k > 0) rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append("\t</Blades>\n");

		rB.append("</Basis>\n");
		return rB.toString();
	}

}
