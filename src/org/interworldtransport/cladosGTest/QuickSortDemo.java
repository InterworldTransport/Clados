/**
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
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
 */

package org.interworldtransport.cladosGTest;

/**
 * There is a speed improvement for sorting keys in an canonical basis if we use
 * a 3-way median quicksort algorithm instead of the heapsort algorithm. This
 * improvement comes at the cost of memory used, so there might be a penalty in
 * memory allocation overhead. Heapsort is stingy with extra memory being used,
 * so careful implementation of the algorithm should avoid the overhead.
 * 
 * The penalty for heap sort for 4 generator algebras in establishing a basis is
 * about 2x the number of swaps, but the total swaps amount to about 60. On even
 * an old machine, this will be lightning fast. That means it isn't worth
 * worrying about which sort is faster in filling a basis. It might make more
 * sense to focus on sorts for the geometric product instead.
 * 
 * @author Dr Alfred W Differ
 *
 */
public class QuickSortDemo {
	// Driver program
	public static void main(String args[]) {
		short pGens = 1;
		QuickSortDemo ob = new QuickSortDemo(pGens);
		ob.printKey(0);
	}

	public int counter;
	/**
	 * This integer is the number of independent blades in an algebra. It is a count
	 * of the number of vBasis rows and is used often enough to be worth keeping
	 * around.
	 */
	protected short bladeCount;
	/**
	 * This integer is the number of grades in the algebra. It is one more than the
	 * number of generators and is used often enough to be worth keeping.
	 */
	protected short gradeCount;
	/**
	 * This array is used for keeping track of where grades start and stop in the
	 * vBasis. The difference between GradeRange[k] and GradeRange[k+1] is
	 * binomial(GradeCount-1, k) = (GradeCount-1)! / (k! * ((GradeCount-1)-k)!)
	 * GradeRange[j] is the first position for a blade of grade j.
	 * 
	 * This array enables the CladosG library to avoid implementing a factorial
	 * method to determine binomial coefficients. Construction of the basis creates
	 * the information for coefficients that would get calculated in regular methods
	 * of a monad. Efficiency demands that they be stored here for later use.
	 * 
	 * The size of the gradeRange array is always generators+2. gradeRange[0] is
	 * always 0 gradeRange[1] is always 1 gradeRange[gradeCount-1] is always
	 * bladeCount-1 The way to interpret these fixed entries is scalars are always
	 * in the first slot of a basis, vectors always start in the second slot, and
	 * the pscalar is always found in the last slot. All other entries in this array
	 * have to be calculated and clados does it using the vKey.
	 */
	protected short[] gradeRange;
	/**
	 * This array holds the representation of the vBasis. The vBasis is a complete
	 * list of unique blades for an algebra.
	 * 
	 * A column entry in a row is a generator (int) used to construct a blade. The
	 * generators in a row are sorted in ascending order. Only positive, non-zero
	 * integers represent generators.
	 * 
	 * A row of the matrix is a blade. The number of rows is the linear dimension of
	 * an algebra using this basis because only one permutation of each distinct
	 * subset of generators is found in the rows of the array.
	 */
	protected short[][] vBasis;

	/**
	 * This array holds the integer keys for each member of the Basis. The key for
	 * each blade is an integer built from sums of powers of the number of
	 * generators.
	 * 
	 * vKey[k]={Sum over m} vBasis[k][m]* (GradeCount)^(GradeCount-1-m);
	 * 
	 * The other kind of key that gets used in other implementations is a base 2 key
	 * that is BladeCount digits long. The key used here holds the same information
	 * in a shorter integer by using a higher numeric base. BladeCount = 2 ^
	 * (GradeCount-1) is the maximum entry in the vBasis array, thus it is the
	 * lowest possible numeric base to use for vKey to avoid overlap of vKeys across
	 * grades. The actual numeric base used is GradeCount ^ (GradeCount-1) as this
	 * ensures algebras with one generator don't require special handling.
	 * 
	 * The vKey has nothing to do with geometric algebra. It is simply easier to
	 * compare keys than actual Basis elements. This trick leads to some
	 * efficiencies in calculations and sorting because basis elements of similar
	 * grade sort together.
	 */
	protected long[] vKey;

	/**
	 * This is the basic constructor. It takes the number of generators as it's only
	 * parameter. It can be instantiated on it's own for demonstration purposes, but
	 * it has no awareness of the addition and multiplication operations in an
	 * algebra, so all it does is show the basis.
	 * 
	 * @param pGens short This is the number of generators that make up the basis
	 */
	public QuickSortDemo(short pGens) {
		gradeCount = (short) (pGens + 1);
		bladeCount = (short) Math.pow(2, pGens);
		vBasis = new short[bladeCount][pGens];
		vKey = new long[bladeCount];
		gradeRange = new short[gradeCount];
		fillBasis();
		// fillGradeRange();
	}

	/**
	 * Set the array used for representing the Eddington Basis. This method produces
	 * an integer array list of Eddington numbers in order of ascending grade and
	 * ascending component.
	 * 
	 * This method should only be called once the Basis is initialized. Construction
	 * of the full basis array scales as grades * blades = generators *
	 * 2^generators, so it could take a long time and a lot of memory for a large
	 * basis. Efforts to streamline code and memory footprint in this method could
	 * have a large impact.
	 */
	public void fillBasis() {
		short tempPermTest = 0;
		short tempPermFilter = 0;
		short q = (short) (gradeCount - 2);
		short k = 0;
		short m = 0;
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
				tempPermFilter = (short) Math.pow(2, gradeCount - 2 - m);
				// This is the filter that is used to detect when to drop
				// a generator from the k'th row of the basis.

				if (tempPermTest < tempPermFilter) {
					tempGens[m] = (short) (m + 1); // m+1'th generator (Never 0)
					// Writing to tempGens but will copy back to vBasis shortly
				} else {
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
			for (m = (short) (gradeCount - 2); m > -1; m--)
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
			for (m = 0; m < gradeCount - 1; m++) {
				vKey[k] += (long) vBasis[k][m] * Math.pow(gradeCount, gradeCount - 2 - m);
			}
		}
		printKey(0);
		sortOnKey(0, bladeCount - 1);
	}

	public long getMedian(int left, int right) {
		int center = (left + right) / 2;
		if (vKey[left] > vKey[center])
			swap(left, center);
		if (vKey[left] > vKey[right])
			swap(left, right);
		if (vKey[center] > vKey[right])
			swap(center, right);
		swap(center, right);

		return vKey[right];
	}

	/**
	 * This function takes last element as pivot, places the pivot element at its
	 * correct position in sorted array, and places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements to right of pivot
	 * 
	 * @param low   Low end index
	 * @param high  High end index
	 * @param pivot Pivot point for recursive partition
	 * @return int Remember that this sort is recursive. I'm not going to document
	 *         how it works here. Go look it up.
	 */
	public int partition(int low, int high, long pivot) {
		// long pivot = vKey[high];

		int i = (low - 1); // index of smaller element
		for (int j = low; j < high; j++) {

			// If current element is smaller than or equal to pivot
			if (vKey[j] <= pivot) {
				i++;
				swap(i, j);
				counter++;
				printKey(i);
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		swap(i + 1, high);
		counter++;
		printKey(i);
		return i + 1;
	}

	/* A utility function to print array of size n */
	public void printKey(int j) {
		System.out.print("swap "+counter + "|" + j + "\t(\t");
		for (int i = 0; i < bladeCount; ++i) {
			System.out.print(vKey[i] + " ");
		}
		System.out.println(")");
	}

	/**
	 * The main function that implements QuickSort() low -- Starting index, high --
	 * Ending index
	 * 
	 * @param low  Low end index
	 * @param high High end index
	 */
	public void sortOnKey(int low, int high) {
		if (low < high) {
			long pivot = getMedian(low, high);

			// pi is partitioning index, vKey[pi] is
			// now at right place
			int pi = partition(low, high, pivot);

			// Recursively sort elements before
			// partition and after partition
			sortOnKey(low, pi - 1);
			sortOnKey(pi + 1, high);
		}
	}

	public void swap(int first, int second) {
		long temp = vKey[first];
		vKey[first] = vKey[second];
		vKey[second] = temp;
	}

}
