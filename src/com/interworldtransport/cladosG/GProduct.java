/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.GProduct<br>
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
 * ---com.interworldtransport.cladosG.GProduct<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosGExceptions.*;

/**
 * This class defines a geometric product for an associated basis.
 * <p>
 * GProduct encapsulates Clifford algebra geometric multiplication. GProduct
 * objects are stored as a separate class to support flat space multivectors
 * that can afford to share their product definition objects.
 * <p>
 * Operations in the GProduct should return logicals for tests the objects can
 * answer. They should alter inbound objects for complex requests. At no point
 * should a GProduct object have to make a copy of itself or inbound objects
 * except for private, temporary use.
 * <p>
 * A GProduct object will actually assume it is OK to perform the requested
 * operations and throw an exception if it discovers later that it isn't. Error
 * discovery isn't likely to be complete, so it should not be expected to work
 * well. Only checks against primitive elements can be done. Physical sense
 * checks need to be performed at the level of the object referencing the
 * GProduct or higher.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class GProduct
{
	/**
	 * This basis holds a representation of all the elements that can be built
	 * from the generators to span the algebra's vector space. It is the
	 * Eddington Basis.
	 */
	private final Basis		eddingtonBasis;
	/**
	 * This integer array is an internal translation of the product signature.
	 * Generators with a positive square appear as a zero (0) while those with
	 * negative squares appear as one (1). This array is kept to increase the
	 * speed of product calculations.
	 */
	private final short[]	nSignature;
	/**
	 * This array holds the geometric multiplication table for a Clifford
	 * algebra using the associated basis. The array contains numbers that
	 * represent the blade # one would produce with a product of blades (row+1)
	 * and (column+1) of result.
	 * 
	 * Negative results in the array imply the resulting blade is scaled by -1.
	 * 
	 * The +1 offsets are present because java arrays start with an index of 0,
	 * while the lowest rank blade is #1.
	 */
	private final short[][]	result;
	/**
	 * This string holds the signature information describing the squares of all
	 * geometry generators present on the multiplication table.
	 */
	private final String	signature;

	/**
	 * Copy constructor of GProduct with other GProduct passed in. This
	 * constructor enables a multivector to have its own GProduct object that
	 * happens to share a Basis with some other GProduct object. This saves
	 * construction time and memory because the basis isn't really the physical
	 * distinction between reference frames. This constuctor enables two 
	 * different algebras to share a basis without forcing them to share
	 * the GProduct object.
	 * 
	 * @param pGP
	 *            GProduct
	 */
	public GProduct(GProduct pGP) throws BadSignatureException
	{
		signature = new String(pGP.getSignature());
		if (!validateSignature(signature))
			throw new BadSignatureException(this, "Valid signature expected.");
		
		if (signature.length() == 0)
			nSignature = new short[1];
		else	
			nSignature = new short[signature.length()];
		
		int m=0;
		for (char b : signature.toCharArray())
		{
			switch (b)
			{
				case '+':
					nSignature[m] = 0;
					m++;
					break;
				case '-':
					nSignature[m] = 1;
					m++;
					break;
			}
		}
		
		eddingtonBasis = pGP.getBasis();
		
		result = pGP.getResult().clone();
	}

	/**
	 * Main constructor of ProductTable with signature information passed in. It
	 * figures out the rest of what it needs.
	 * 
	 * @param pSig
	 *            String
   	 * @throws CladosMonadException
	 * 			This exception is thrown when a Basis fails to form.
	 * @throws BadSignatureException
	 * 			This exception is thrown when an invalid signature is found
	 */
	public GProduct(String pSig) 
			throws BadSignatureException, CladosMonadException
	{
		// Validate pSig to ensure it has only the information we want. Then
		// save it internally
		//boolean check = validateSignature(pSig);
		if (!validateSignature(pSig))
			throw new BadSignatureException(this, "Valid signature expected.");
		signature = pSig;
		if (signature.length() == 0)
			nSignature = new short[1];
		else
			nSignature = new short[signature.length()];
		
		int m=0;
		for (char b : signature.toCharArray())
		{
			switch (b)
			{
				case '+':
					nSignature[m] = 0;
					m++;
					break;
				case '-':
					nSignature[m] = 1;
					m++;
					break;
			}
		}
		
		eddingtonBasis = new Basis((short) pSig.length());

		// Fill the ProductResult array with integers representing Vector
		// Basis elements that show the product of two other such elements.
		result = new short[eddingtonBasis.getBladeCount()][eddingtonBasis.getBladeCount()];
		
		for (short j = 0; j < eddingtonBasis.getBladeCount(); j++)
		{
			result[0][j] = (short) (j + 1);
			result[j][0] = (short) (j + 1);
		} // Scalar section of product result finished early
		
		for (short j = 1; j < eddingtonBasis.getBladeCount(); j++) 
			for (short k = 1; k < eddingtonBasis.getBladeCount(); k++) 
				fillResult(j, k);	
	}

	/**
	 * Return a measure of whether blades pj and pk anticommute. Return a 1 if
	 * they anticommute. Return a 0 otherwise.
	 * 
	 * @param pj
	 *            short
	 * @param pk
	 *            short
	 * @return int
	 */
	public short getACommuteSign(short pj, short pk)
	{
		short left=result[pj][pk];
		short right=result[pk][pj];
		
		if (left == right * -1)
			return 1;
		
		return 0;
	}

	/**
	 * Basic Get method for the Basis generated by the signature of this
	 * GProduct.
	 * 
	 * @return Basis
	 */
	public Basis getBasis()
	{
		return eddingtonBasis;
	}

	/**
	 * Get the linear dimension of the vector space that uses the associated
	 * Basis.
	 * 
	 * @return short
	 */
	public short getBladeCount()
	{
		return eddingtonBasis.getBladeCount();
	}

	/**
	 * Return a measure of whether blades pj and pk commute. Return a 1 if they
	 * commute. Return a 0 otherwise.
	 * 
	 * @param pj
	 *            short
	 * @param pk
	 *            short
	 * @return int
	 */
	public short getCommuteSign(short pj, short pk)
	{
		if (result[pj][pk] == result[pk][pj])
			return 1;
		
		return 0;
	}

	/**
	 * Get the grade count of the algebra that uses this GProduct.
	 * 
	 * @return short
	 */
	public short getGradeCount()
	{
		return eddingtonBasis.getGradeCount();
	}

	/**
	 * Get start and end index from the GradeRange array for grade pGrade.
	 * 
	 * @param pGrade
	 *            short
	 * @return short[]
	 */
	public short[] getGradeRange(short pGrade)
	{
		short[] tR = new short[2];
		tR[0] = eddingtonBasis.getGradeRange(pGrade);

		if (pGrade == eddingtonBasis.getGradeCount() - 1)
			tR[1] = tR[0];
		else
			tR[1] = (short) (eddingtonBasis.getGradeRange((short) (pGrade + 1)) - 1);

		return tR;
	}

	/**
	 * Return whole result array. Meant for copy constructors.
	 * 
	 * @return short[][]
	 */
	public short[][] getResult()
	{
		return result;
	}

	/**
	 * Return row pj of result array. Meant for alternate multiplication
	 * methods.
	 * 
	 * @param pj
	 *            short
	 * @return short[][]
	 */
	public short[] getResult(short pj)
	{
		return result[pj];
	}

	/**
	 * Return an element of the array holding the geometric multiplication
	 * results.
	 * 
	 * @param pj
	 *            short
	 * @param pk
	 *            short
	 * @return short
	 */
	public short getResult(short pj, short pk)
	{
		return result[pj][pk];
	}

	/**
	 * Return an element of the array holding the geometric multiplication
	 * results.
	 * 
	 * @param pj
	 *            short
	 * @param pk
	 *            short
	 * @return int
	 */
	public short getSign(short pj, short pk)
	{
		if (result[pj][pk] < 0)
			return -1;
		
		return 1;
	}

	/**
	 * Return the signature of the generator geometry. This lists the squares of
	 * the generators in their numeric order.
	 * 
	 * @return String
	 */
	public String getSignature()
	{
		return signature;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * Basis in a human readable form. return String
	 * 
	 * @return String This is the XML string export of an object.
	 */
	public String toXMLString()
	{
		StringBuffer rB = new StringBuffer("<GProduct signature=\"" + signature
						+ "\">\n");
		rB.append(eddingtonBasis.toXMLString());
		rB.append("<ProductTable rows=\"" + eddingtonBasis.getBladeCount() + "\">\n");
		for (short k = 0; k < eddingtonBasis.getBladeCount(); k++) // Appending rows
		{
			rB.append("\t<row number=\"" + k + "\" entries=\"");
			for (short m = 0; m < eddingtonBasis.getBladeCount(); m++)
			{
				rB.append(getResult(k, m));
				rB.append(",");
			}
			rB.deleteCharAt(rB.length() - 1);
			rB.append("\" />\n");
		}
		rB.append("</ProductTable>\n");
		rB.append("</GProduct>\n");
		return rB.toString();
	}

	/**
	 * Set the array used for establishing the geometric multiplication results
	 * of pairs of blades (j and k) of the Basis.
	 * 
	 * product results are constructed by juxtaposing the generators of each
	 * blade (all of j on the left and all of k on the right) into a single
	 * array and then sorting them into ascending order from left to right and
	 * removing generator pairs from the array. Each pairwise swap made in the
	 * combined set of generators increments a counter that keeps track of the
	 * anti-commuting multiplier. Generator pairs are removed using the
	 * signature array to increment the anti-commuting counter if needed. After
	 * a final sort to move all 0 (null) generators to the left the result is
	 * keyed much like blades are in the Basis when it is constructed. Keys are
	 * finally compared to determine the identity of the resulting blade.
	 * 
	 * This method should only be called once the GProduct is initialized.
	 * Construction of the full product result array scales as
	 * blades^2=2^(2*generators), so it could take a long time and a lot of
	 * memory for a large basis. Efforts to streamline code and memory footprint
	 * in this method could have a large impact.
	 * 
	 * @param j
	 *            short
	 * @param k
	 *            short
	 */
	private void fillResult(short j, short k)
	{
		short[] bothOps = new short[2 * eddingtonBasis.getGradeCount() - 2];
		short m = 0;
		short signFlip = 0;
		short tempBubbleSpot = 0;			//yes. There is a bubble sort. Not a big one.
		
		// Set up row with all generators for each basis element j and k
		for (m = 0; m < eddingtonBasis.getGradeCount() - 1; m++)
		{
			// Copy VectorBasis' into doubleSort to find new element
			bothOps[m] = eddingtonBasis.getBasis(j, m);
			bothOps[m + eddingtonBasis.getGradeCount() - 1] = eddingtonBasis.getBasis(k, m);
		}
		// bothOps filled but unsorted. That means the zero slots in the kth blade (if any)
		// will be to the right of non-zero indexes in the jth blade. Basically, the first
		// blade is in the first half of the array, the second blade in the second half.
		// All that is needed next is to slice the 0's out of the row and then pad them back
		// on the left.
		// We start on the right of bothOps and look for columns that aren't zero and copy 
		// them out.
		
		bothOps=settleGenerators(bothOps);
		
		// bothOps filled and partially sorted. That means the zero slots in both blades (if any)
		// will be to the left of all of non-zero indexes in the both blades.
		// All that is needed next is to sort the non-zero's on the right of bothOps.
		
		for (m = 0; m < 2 * eddingtonBasis.getGradeCount() - 2; m++)
		{
			if (bothOps[m] == 0) continue;
			
			for (short n = 0; n < 2 * eddingtonBasis.getGradeCount() - 3; n++)
			{
				if (bothOps[n] <= bothOps[n + 1]) continue;
				
				tempBubbleSpot = bothOps[n];
				bothOps[n] = bothOps[n + 1];
				bothOps[n + 1] = tempBubbleSpot;
				signFlip += 1;
			}
		} 
		// bothOps filled and sorted, but index pairs are still possible.

		signFlip = (short) (signFlip % 2); // commutation sign tracking is being done.
		
		//**************************************************************************

		// Now we need to remove generator pairs and track signs.
		for (m = (short) (2 * eddingtonBasis.getGradeCount() - 3); m >= 1; m--)
		{
			//if (bothOps[m] == 0) continue;
			if (bothOps[m] == bothOps[m - 1] && bothOps[m] != 0)
			{
				signFlip += nSignature[bothOps[m] - 1];
				bothOps[m] = 0;
				bothOps[m - 1] = 0;
				m -= 1;
				// flip sign again if generator has negative square.
			}
		}
		signFlip = (short) (signFlip % 2); // commutation sign tracking is being done.

		// Now settle again.
		bothOps=settleGenerators(bothOps);
		
		//**************************************************************************

		// At this point bothOps should be fully sorted and have no
		// duplicate generators. Now we need to Key the discovered basis element
		
		// Base (GradeCount) representation of Eddington Number
		// Ex: 3 generators implies Base-4 keys stuffed into Base-10 array
		// Right-most generator is the one's digit
		// Middle generator is the 4's digit
		// Left-most generator is the 16's digit
		// Ex: 8 generators implies Base-9 keys stuffed into a Base-10 array
					
		long bothOpsKey = 0L;
		for (m = 0; m < 2 * eddingtonBasis.getGradeCount() - 2; m++)
			bothOpsKey += (long) bothOps[m]*Math.pow(eddingtonBasis.getGradeCount(),
													2*(eddingtonBasis.getGradeCount()) -3-m);
		
		// Compare bothOpsKey against vKey to find match
		result[j][k] = 0;
		long[] pKey = eddingtonBasis.getBasisKey();
		for (m = 0; m < eddingtonBasis.getBladeCount(); m++)
		{
			if (bothOpsKey == pKey[m])
			{
				result[j][k] = (short) ((m + 1) * Math.pow(-1.0, signFlip));
				break; // Good enough. Done identifying resulting blade & its sign
			}
		}
		assert(result[j][k]!=0);	// if result entry is not zero, fill is complete.
		// This assertion prevents us from using light-like generators
	}
	
	/**
	 * This is a private method used to settle generators to the end during
	 * the fillResult method.
	 * 
	 * @param sortArray
	 * @return
	 */
	private short[] settleGenerators(short[] sortArray)
	{
		short[] tempBothOps = new short[2 * eddingtonBasis.getGradeCount() - 2];
		short q = (short) (2 * eddingtonBasis.getGradeCount() - 3);
		
		for (short m = (short) (2 * eddingtonBasis.getGradeCount() - 3); m>-1; m--) 
		// generator (column) counter GradeCount-2 through 0 [decreasing]
		// Only one pass is needed
		{
			if (sortArray[m] != 0) // Found one to copy.
			{
				tempBothOps[q] = sortArray[m];
				q--; // Slide left in vBasis for next possible entry.
			}
		}
		return tempBothOps;
		
	}

	/**
	 * Return a measure of the validity of the Signature string. A string with
	 * +'s and -'s will pass. No other one does.
	 * 
	 * This method also establishes the internal integer representation of the
	 * signature.
	 * 
	 * @param pSg
	 *            String
	 * @return boolean This boolean states whether the GProduct signature is valid.
	 */
	protected boolean validateSignature(String pSg)
	{
		
		if (pSg == null)
			return false;
		if (pSg.length() == 0)
			return true;
		
		for (short j = 0; j < pSg.length(); j++)
		{
			if (pSg.substring(j, j + 1).equals("+"))
				continue;
			if (pSg.substring(j, j + 1).equals("-"))
				continue;
			return false;
		}
		return true;	
	}
}
