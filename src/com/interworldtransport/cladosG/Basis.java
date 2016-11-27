/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.Basis<br>
 * -------------------------------------------------------------------- <p>
 * You ("Licensee") are granted a license to this software under the terms of 
 * the GNU General Public License. A full copy of the license can be found 
 * bundled with this package or code file. If the license file has become 
 * separated from the package, code file, or binary executable, the Licensee is
 * still expected to read about the license at the following URL before 
 * accepting this material. 
 * <code>http://www.opensource.org/gpl-license.html</code><p> 
 * Use of this code or executable objects derived from it by the Licensee states
 * their willingness to accept the terms of the license. <p> 
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
 * The choice of shorts limits this class to supporting products with between 1
 * and 14 generators inclusive.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class Basis
{
	/**
	 * This integer is the number of grades in the algebra. It is one more than
	 * the number of generators and is used often enough to be worth keeping.
	 */
	protected short		gradeCount;
	/**
	 * This integer is the number of independent blades in an algebra. It is a
	 * count of the number of vBasis rows and is used often enough to be worth
	 * keeping around.
	 */
	protected short		bladeCount;
	/**
	 * This array holds the representation of the vBasis. The vBasis is a
	 * complete list of unique blades for an algebra.
	 * 
	 * A column entry in a row is a generator (int) used to construct a blade.
	 * The generators in a row are sorted in ascending order. Only positive,
	 * non-zero integers represent generators.
	 * 
	 * A row of the matrix is a blade. The number of rows is the linear
	 * dimension of an algebra using this basis because only one permutation of
	 * each distinct subset of generators is found in the rows of the array.
	 */
	protected short[][]	vBasis;
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
	protected long[]	vKey;
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
	 */
	protected short[]	gradeRange;

	/**
	 * This is the basic constructor. It takes the number of generators as it's
	 * only parameter. It can be instantiated on it's own for demonstration
	 * purposes, but it has no awareness of the addition and multiplication
	 * operations in an algebra, so all it does is show the basis.
	 * 
	 * @param pGens
	 * 		short This is the number of generators that make up the basis
	 * @throws 
	 * 		CladosMonadException Exception thrown with 15 or more generators
	 */
	public Basis(short pGens) throws CladosMonadException
	{
		if (pGens > 14)
			throw new CladosMonadException(null,
							"Can't support more than 14 generators");
		gradeCount = (short) (pGens + 1);
		bladeCount = (short) Math.pow(2, pGens);
		vBasis = new short[bladeCount][pGens];
		vKey = new long[bladeCount];
		gradeRange = new short[gradeCount];
		fillBasis();
		fillGradeRange();
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
	 * Return the integer at (x) in the array holding the EddingtonKey.
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
	//@SuppressWarnings("unused")
	protected void fillBasis()
	{
		int tempPermTest = 0;
		int tempPermFilter = 0;
		long tempBubbleSpotL = 0;
		short tempBubbleSpotI = 0;

		for (int k = 0; k < bladeCount; k++) 
		// basis row counter 0 thru BladeCount-1
		{
			vKey[k] = 0;
			tempPermTest = k; // Starts as row counter
			for (int m = 0; m < gradeCount - 1; m++) 
			// generator (column) counter 0 thru GradeCount-2
			{
				tempPermFilter = (int) Math.pow(2, gradeCount - 2 - m);
				;
				if (tempPermTest < tempPermFilter)
				{
					vBasis[k][m] = (short) (m + 1); // m+1 generator in this
													// row. (Never 0)
				}
				else
				{
					vBasis[k][m] = 0; // null (0) generator if filter fails
					tempPermTest -= tempPermFilter; // decrement test if filter
													// fails
				}
			}
			// Row filled but unsorted. If generator m appears in a row it will
			// always
			// appear in column m-1 at this point. Next up is to sort the
			// generators
			// but they are already mostly sorted.

			if (true) 
			// Trying to speed up this section with a different sort
			// TODO Work out the 'else' sort routine until it matches
			{
				for (int m = 0; m <= gradeCount - 3; m++)
				{
					for (int p = m + 1; p <= gradeCount - 2; p++)
					{
						if (vBasis[k][m] > vBasis[k][p])
						{
							tempBubbleSpotI = vBasis[k][m];
							vBasis[k][m] = vBasis[k][p];
							vBasis[k][p] = (short) tempBubbleSpotI;
							tempBubbleSpotI = 0;
						}
					}
				}
			}
			else
			{
				// All that is needed is to slice the 0's out of the row so we
				// start on the right and look for columns that aren't zero.
				// If they aren't, they get copied to a temporary array which
				// gets
				// copied back once all the zero's in the first round are sliced
				// out.
				int q = gradeCount - 1;
				short[] tempGens = new short[q];
				q--;
				for (int m = gradeCount - 2; m >= 0; m--) 
				// Only one pass is needed?
				{
					if (vBasis[k][m] != 0)
					{
						tempGens[q] = vBasis[k][m];
						q--;
					}

				}
				for (int m = 0; m > gradeCount - 2; m++)
				{
					vBasis[k][m] = tempGens[m];
				}
			}

			// Row filled and sorted. The generators used in row k will now
			// appear in
			// ascending order with 0's padding the lowest columns. Thus a
			// vector blade
			// will have a single generator in the GradeCount-2 column and 0's
			// in the
			// earlier columns.

			for (int m = 0; m <= gradeCount - 2; m++)
			{
				vKey[k] += (long) vBasis[k][m]
								* Math.pow(gradeCount, gradeCount - 2 - m);
			}
			// Base (GradeCount) representation of Eddington Number is now
			// complete
			// Ex: 3 generators implies Base-4 keys stuffed into Base-10 array
			// Right-most generator is the one's digit
			// Middle generator is the 4's digit
			// Left-most generator is the 16's digit
			// Ex: 8 generators implies Base-9 keys stuffed into a Base-10 array

		}
		// All Eddington Numbers are now written, sorted horizontally, and
		// calculated.

		// It is now time to sort them vertically and call it a basis.
		int p = 0;
		int m = 0;
		for (int k = 0; k < bladeCount - 1; k++)
		{
			for (p = bladeCount - 1; p > k; p--)
			// Done from bottom up for speed. We know a bit about how the keys
			// are initially populated and this way should be faster.
			{
				if (vKey[k] > vKey[p])
				{
					tempBubbleSpotL = vKey[k]; // Swap the Keys.
					vKey[k] = vKey[p];
					vKey[p] = tempBubbleSpotL;

					for (m = 0; m < gradeCount - 1; m++) // Swap the Basis rows.
					{
						tempBubbleSpotI = vBasis[k][m];
						vBasis[k][m] = vBasis[p][m];
						vBasis[p][m] = tempBubbleSpotI;
					}
					tempBubbleSpotL = 0;
					tempBubbleSpotI = 0;
					m = 0;
				}// end of pair swap
			}// end of inside sorting pass
			p = bladeCount - 1;
		}// end of outside sorting pass

		// All Eddington Numbers are now written, calculated, and sorted.
	}

	/**
	 * Set the array used for keeping track of where grades start in the Basis
	 * array. GradeRange[j] is the first position for a blade of grade j. This
	 * function used to use factorials and binomial coefficients to find how
	 * many blades existed for a particular grade. Now it uses the Key and its
	 * Base(GradeCount) encoding to do it better and faster.
	 */
	protected void fillGradeRange()
	{
		gradeRange[0] = 0; // The scalar
		gradeRange[1] = 1; // The scalar
		gradeRange[gradeCount - 1] = (short) (bladeCount - 1); // The pscalar
		short m = 0;
		for (int j = 2; j < gradeCount - 1; j++) // Skipping scalar and pscalar
		{
			long test = (long) Math.pow(gradeCount, j - 1); // Floor Key for
															// Grade j
			while (m < bladeCount)
			{
				if (vKey[m] > test)
				{
					gradeRange[j] = m;
					break; // Found first key. Move to next grade.
				}
				m++;
			} // All blades searched
		} // All grades ranged
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
