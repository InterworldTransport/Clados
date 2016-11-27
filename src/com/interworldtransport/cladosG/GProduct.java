/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.GProduct<br>
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
	 * This string holds the signature information describing the squares of all
	 * geometry generators present on the multiplication table.
	 */
	protected String	signature;
	/**
	 * This integer array is an internal translation of the product signature.
	 * Generators with a positive square appear as a zero (0) while those with
	 * negative squares appear as one (1). This array is kept to increase the
	 * speed of product calculations.
	 */
	protected short[]	nSignature;
	/**
	 * This basis holds a representation of all the elements that can be built
	 * from the generators that space the algebra's vector space.
	 */
	protected Basis		aBasis;
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
	protected short[][]	result;

	/**
	 * Copy constructor of GProduct with other GProduct passed in. This
	 * constructor enables a multivector to have its own GProduct object that
	 * happens to share a Basis with some other GProduct object. This saves
	 * construction time and memory, while preventing multivectors with
	 * different reference frames from performing improper math operations that
	 * can't be stopped through type safety.
	 * 
	 * @param pGP
	 *            GProduct
	 */
	public GProduct(GProduct pGP)
	{
		signature = new String(pGP.getSignature());
		validateSignature(signature);
		aBasis = pGP.getBasis();
		result = new short[getBladeCount()][getBladeCount()];
		result = pGP.getResult();
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
		boolean check = validateSignature(pSig);
		if (check)
		{
			// Figure out linear dimension and grade count. Both are needed
			// often.
			aBasis = new Basis((short) pSig.length());

			// Fill the ProductResult array with integers representing Vector
			// Basis elements that show the product of two other such elements.

			result = new short[aBasis.getBladeCount()][aBasis.getBladeCount()];

			for (int j = 0; j < aBasis.getBladeCount(); j++)
			{
				result[0][j] = (short) (j + 1);
				result[j][0] = (short) (j + 1);
			} // Scalar section of product result finished early
			for (short j = 1; j < aBasis.getBladeCount(); j++)
			{// counter for row element in product
				for (short k = 1; k < aBasis.getBladeCount(); k++)
				{// counter for column element in product
					fillResult(j, k);
				}
			}
		}
		else
		{
			throw new BadSignatureException(this, "Valid signature expected.");
		}
		// Fill in any other helpful things to be kept here.
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
	protected void fillResult(short j, short k)
	{
		int[] bothOps = new int[2 * (aBasis.getGradeCount() - 1)];
		int signFlip = 0;
		long bothOpsKey = 0L;

		int tempBubbleSpot = 0;

		signFlip = 0;
		bothOpsKey = 0L;
		// Set up row with all generators for each basis element j and k
		for (short m = 0; m < aBasis.getGradeCount() - 1; m++)
		{
			// Copy VectorBasis' into doubleSort to find new element
			bothOps[m] = aBasis.getBasis(j, m);
			bothOps[m + aBasis.getGradeCount() - 1] = aBasis.getBasis(k, m);
		}

		for (int m = 0; m < 2 * aBasis.getGradeCount() - 2; m++)
		{
			for (int n = 0; n < 2 * aBasis.getGradeCount() - 3; n++)
			{
				// Swap on doubleSort
				if (bothOps[n] > bothOps[n + 1])
				{
					tempBubbleSpot = bothOps[n];
					bothOps[n] = bothOps[n + 1];
					bothOps[n + 1] = tempBubbleSpot;
					if (!(bothOps[n] == 0 || bothOps[n + 1] == 0))
					{
						signFlip += 1;
					}
				}
			}
		} // end of doubleSort sort

		signFlip = signFlip % 2; // commutation sign tracking is being done.

		// Now we need to remove generator pairs and track signs.
		for (int m = 2 * (aBasis.getGradeCount() - 1) - 1; m >= 1; m--)
		{
			if (bothOps[m] == 0) continue;
			if (bothOps[m] == bothOps[m - 1])
			{
				signFlip += nSignature[bothOps[m] - 1];
				bothOps[m] = 0;
				bothOps[m - 1] = 0;
				m -= 1;
				// flip sign again if generator has negative square.
			}
		}
		signFlip = signFlip % 2; // commutation sign tracking is being done.

		// Now sort again.
		for (int m = 0; m < 2 * aBasis.getGradeCount() - 2; m++)
		{
			for (int n = 0; n < 2 * aBasis.getGradeCount() - 3; n++)
			{
				// Swap on doubleSort
				if (bothOps[n] > bothOps[n + 1])
				{
					tempBubbleSpot = bothOps[n];
					bothOps[n] = bothOps[n + 1];
					bothOps[n + 1] = tempBubbleSpot;
					if (!(bothOps[n] == 0 || bothOps[n + 1] == 0))
					{
						signFlip += 1;
					}
				}
			}// end of inside doublesort pass
		} // end of outside doubleSort sort

		signFlip = signFlip % 2; // commutation sign tracking is being done.

		// At this point doubleSort should be fully sorted and have no
		// duplicate generators. Now we need to Key the basis element in
		// doubleSort to identify it
		for (int m = 0; m < 2 * (aBasis.getGradeCount() - 1); m++)
		{// temporary counter
			bothOpsKey += (int) bothOps[m]
							* Math.pow(aBasis.getGradeCount(),
											2 * (aBasis.getGradeCount()) - 3
															- m);
		} // Base (GradeCount) representation of Vector Number is in doubleKey

		// Compare doubleKey against vKey to find match
		result[j][k] = 0;
		for (short q = 0; q < aBasis.getBladeCount(); q++)
		{// temporary counter
			if (bothOpsKey == aBasis.getBasisKey(q))
			{ // We have a match!
				result[j][k] = (short) ((q + 1) * Math.pow(-1.0, signFlip));

				break; // Good enough. Go on to next ProductResult piece
			}

		}// Found result[j][k] and sign successfully
		signFlip = 0;
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
		if (result[pj][pk] == result[pk][pj] * -1)
			return 1;
		else
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
		return aBasis;
	}

	/**
	 * Get the linear dimension of the vector space that uses the associated
	 * Basis.
	 * 
	 * @return short
	 */
	public short getBladeCount()
	{
		return aBasis.getBladeCount();
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
		else
			return 0;
	}

	/**
	 * Get the grade count of the algebra that uses this GProduct.
	 * 
	 * @return short
	 */
	public short getGradeCount()
	{
		return aBasis.getGradeCount();
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
		tR[0] = aBasis.getGradeRange(pGrade);

		if (pGrade == aBasis.getGradeCount() - 1)
			tR[1] = tR[0];
		else
			tR[1] = (short) (aBasis.getGradeRange((short) (pGrade + 1)) - 1);

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
		if (result[pj][pk] >= 0)
			return 1;
		else
			return -1;
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
		rB.append(aBasis.toXMLString());
		rB.append("<ProductTable rows=\"" + aBasis.getBladeCount() + "\">\n");
		for (short k = 0; k < aBasis.getBladeCount(); k++) // Appending rows
		{
			rB.append("\t<row number=\"" + k + "\" entries=\"");
			for (short m = 0; m < aBasis.getBladeCount(); m++)
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
		nSignature = new short[pSg.length()];
		for (int j = 0; j < pSg.length(); j++)
		{
			if (pSg.substring(j, j + 1).equals("+"))
			{
				nSignature[j] = 0;
			}
			else
			{
				if (pSg.substring(j, j + 1).equals("-"))
				{
					nSignature[j] = 1;
				}
				else
				{
					nSignature=null;
					return false;
					// Yes... it was possible nSignature would be partially
					// constructed when this happens.
				}
			}
		}
		signature = pSg;
		return true;
	}
}
