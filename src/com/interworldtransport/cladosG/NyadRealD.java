/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.NyadRealD<br>
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
 * ---com.interworldtransport.cladosG.NyadRealD<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import static com.interworldtransport.cladosG.MonadRealD.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.interworldtransport.cladosF.CladosField;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosGExceptions.*;

/**
 * Multivector Lists over a shared division field and shared algebraic foot.
 * {Cl(p1,q1)x...xCl(pn,qn) over R, C or Q and @Foot}.
 * <p>
 * Nyads encapsulate related multivectors from Clifford Algebras and the rules
 * relating them. This class keeps a list of Monads and definitions for legal
 * mathematical operations upon them.
 * <p>
 * Proper use of this class is accomplished when one views a physical property
 * as a self-contained entity and a list of properties as a description of a
 * real physical object. An electron's motion demonstrates a need for a
 * multivector list because the properties of the electron can be listed
 * independent of its motion. Enclosing related properties in a list enables a
 * complete instance representing an observable and prevents one from making
 * programmatic errors allowed by the programming language but disallowed by the
 * physics.
 * <p>
 * NyadRealD objects must be declared with at least one Monad that has at least
 * two generators of geometry. Each Monad on the list must have the same
 * footPoint. At present, the NyadRealD permits the encapsulated Monads to be
 * members of geometric algebras of different metric and dimensionality.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class NyadRealD extends NyadAbstract
{
	/**
	 * Return an integer pointing to a monad in the nyad that uses the
	 * algebra named in the parameter. 
	 * 
	 * @param pN	NyadRealD
	 * @param pAlg	String
	 * @return int
	 */
	public static final int findAlgebra(NyadRealD pN, AlgebraRealD pAlg)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pAlg.equals(pM.getAlgebra())) return pN.monadList.indexOf(pM);
		return -1;
	}
	
	/**
	 * Return an integer larger than pStart pointing to a monad in the nyad that uses the
	 * algebra named in the parameter. 
	 * 
	 * @param pN		NyadRealF
	 * @param pAlg		String
	 * @param pStart	int
	 * @return int
	 */
	public static final int findNextAlgebra(NyadRealD pN, AlgebraRealD pAlg, int pStart)
	{
		if(pN.getMonadList().size()<pStart) return -1;
		for (int j=pStart; j < pN.getMonadList().size(); j++)
			if (pAlg.equals(pN.getMonadList(j).getAlgebra())) return j;
		return -1;
	}
	 
	/**
	 * Return an integer pointing to the part of the nyad expressed in the frame
	 * named in the parameter.
	 * 
	 * @param pN
	 * 			NyadRealD
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static final int findFrame(NyadRealD pN, String pFrame)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return pN.monadList.indexOf(pM);
		return -1;
	}
	
	/**
	 * Return the index for monad matching requested name within the nyad if found. 
	 * 
	 * @param pN
	 * 			NyadRealD
	 * @param pName
	 *            String
	 * @return int
	 */
	public static final int findName(NyadRealD pN, String pName)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pName.equals(pM.getName())) return pN.monadList.indexOf(pM);
		return -1;
	}
	
	/**
	 * Return the index for monad within the nyad if found. 
	 * 
	 * @param pN
	 * 			NyadRealD
	 * @param pIn   
	 * 			MonadRealD       
	 * @return int
	 */
	public static final int findMonad(NyadRealD pN, MonadRealD pIn)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pIn == pM) return pN.monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param pN
	 * 			NyadAbstract
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static final boolean hasAlgebra(NyadRealD pN, AlgebraRealD pAlg)
	{
		for (AlgebraRealD pM : pN.getAlgebraList())
			if (pAlg.equals(pM)) return true;
		return false;
	}
	
	/**
	 * Return a boolean stating whether or not the nyad is expressed in the
	 * frame named in the parameter.
	 * 
	 * @param pN
	 * 			NyadRealD
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static final boolean hasFrame(NyadRealD pN, String pFrame)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pFrame.equals(pM.getFrameName())) return true;
		return false;
	}
	
	/**
	 * This method finds how often a particular algebra shows up in use by 
	 * monads in the nyad. Results could range from zero to nyadOrder.
	 * <p>
	 * @param pN	NyadRealD
	 * @param pAlg	AlgebraRealD
	 * @return int
	 * 	This method counts how many instances of the algebra are present in
	 *  monads in the nyad
	 */
	public static final int howManyAtAlgebra(NyadRealD pN, AlgebraRealD pAlg)
	{
		if (pN.getNyadOrder() == 0) return 0;
		if (pAlg == null) return 0;
		int found=0;
		int test=0;
		while (test>=0)
		{
			test = findNextAlgebra(pN, pAlg, test);
			if (test>=0) found++;
		}
		return found;
	}
	
	/**
	 * Return a boolean stating whether or not the nyad contained the named
	 * monad.
	 * 
	 * @param pN
	 * 			NyadRealD
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static final boolean hasName(NyadRealD pN, String pName)
	{
		for (MonadRealD pM : pN.getMonadList())
			if (pName.equals(pM.getName())) return true;
		return false;
	}

	/**
	 * Return true if the Monads in the two lists are GEqual and the nyads are
	 * reference matches. Only monads sharing the same algebra name need to be
	 * checked against each other. No check is to be made for equality between
	 * the monad names.
	 * <p>
	 * This method is needed to compare Nyads since comparing instances via
	 * their variable names only checks to see if both variables reference the
	 * same place in memory
	 * 
	 * @param pTs
	 *            NyadRealD
	 * @param pN
	 *            NyadRealD
	 * @return boolean
	 */
	public static final boolean isMEqual(NyadRealD pTs, NyadRealD pN)
	{
		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder()) return false;

		// Check to see if the foot names match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now check the monad lists.
		boolean forwardCheck = false;
		for (MonadRealD tSpot : pTs.getMonadList())
		{
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			forwardCheck = false;
			// Get the Algebra for tSpot
			AlgebraRealD tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadRealD tSpot2 : pN.getMonadList())
			{
				if (tSpotAlg1 == tSpot2.getAlgebra())
				{
					// ah ha! Found an algebra match in tSpot2
					forwardCheck = true;
					// Now check of tSpot is GEqual to tSpot2
					if (!tSpot.isGEqual(tSpot2)) 
						return false;
					// If we get here, tSpot is GEqual to tSpot2
					// and there is no need to look further in the second nyad
					// for an algebra match
					break;
				}
			}
			// if check is true a match was found and it is time to move to next monad in first nyad
			// if check is false, we have a dangling monad, thus nyads can't be equal.
			if (!forwardCheck) return false;
		}
		
		// To get this far, all Monads in first must pass equality test for counterparts in the second.
		// So... now we we test for reflexivity by checking that the second list passes the same test
		// against the first list.		

		boolean backwardCheck = false;
		for (MonadRealD tSpot : pN.getMonadList())
		{
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			backwardCheck = false;
			// Get the Algebra for tSpot
			AlgebraRealD tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadRealD tSpot2 : pTs.getMonadList())
			{
				if (tSpotAlg1 == tSpot2.getAlgebra())
				{
					// ah ha! Found an algebra match in tSpot2
					backwardCheck = true;
					// Now check of tSpot is GEqual to tSpot2
					if (!tSpot.isGEqual(tSpot2)) 
						return false;
					// If we get here, tSpot is GEqual to tSpot2
					// and there is no need to look further in the second nyad
					// for an algebra match
					break;
				}
			}
			// if check is true a match was found and it is time to move to next monad in first nyad
			// if check is false, we have a dangling monad, thus nyads can't be equal.
			if (!backwardCheck) return false;
		}
		
		// To get this far, all Monads in the second must pass equality test for counterparts in the first.
		// The other direction has already been checked, thus reflexivity is assured.
		return true;
	}

	/**
	 * This method determines whether or not the Nyad is a pscalar in the algebra in question.
	 * It works essentially the same way as isScalarAt.
	 * 
	 * @param 	pN		NyadRealD
	 * @param 	pAlg	AlgebraRealD
	 * @return 	boolean
	 */
	public static final boolean isPScalarAt(NyadRealD pN, AlgebraRealD pAlg)
	{
		boolean test = false;	// Assume test fails
		if (pN.getMonadList().size()<=0) return false;	// No monads? Fails.
		int maxGrade = pAlg.getGProduct().getGradeCount()-1; // find pAlg's max grade
		int j=0;
		int tSpot = 0;
		while (j<pN.getMonadList().size())	// loop through monads
		{
			tSpot = findNextAlgebra(pN, pAlg, j);	// find a monad using pAlg
			if (tSpot < 0) break;					// none found?  break out of loop
			else if (!isGrade(pN.getMonadList(tSpot), maxGrade)) return false;	//found and not pscalar? Fails.
			else 
			{
				test = true;	// found and IS pscalar? test=true because one was found.
				j=tSpot+1;		// increment loop var to where Alg match was found
			}
		}						// loop all done
		return test;	// if test is true, no non-pscalar(s) found at pAlg, but pscalar WAS found.
	}
	
	/**
	 * This method determines whether or not the Nyad is a scalar in the algebra in question.
	 * 
	 * The method looks for Algebra matches in the monad list. If none are found the test fails.
	 * If one is found and the related monad is a scalar, the test is temporarily true, 
	 * but the search continues until one exhausts the monad list for algebra matches.
	 * If exhaustion occurs before another match is found with a non-scalar monad, the test is 
	 * solidly true. If a non-scalar is found before the monad list is exhausted, the test fails.
	 * 
	 * @param 	pN		NyadRealD
	 * @param 	pAlg	AlgebraRealD
	 * @return 	boolean
	 */
	public static final boolean isScalarAt(NyadRealD pN, AlgebraRealD pAlg)
	{
		boolean test = false;	// Assume test fails
		if (pN.getMonadList().size()<=0) return false;	// No monads? Fails.
		
		int j=0;
		int tSpot = 0;
		while (j<pN.getMonadList().size())	// loop through monads
		{
			tSpot = findNextAlgebra(pN, pAlg, j);	// find a monad using pAlg
			if (tSpot < 0) break;					// none found?  break out of loop
			else if (!isGrade(pN.getMonadList(tSpot), 0)) return false;	//found and not scalar? Fails.
			else 
			{
				test = true;	// found and IS scalar? test=true because one was found.
				j=tSpot+1;		// increment loop var to where Alg match was found
			}
		}						// loop all done
		return test;	// if test is true, no non-scalar(s) found at pAlg, but scalar WAS found.
	}
	
	/**
	 * This method performs a strong test for a reference match. All properties
	 * of the Nyads must match except for the NyadRealD names. The monads within
	 * the NyadRealD must also be reference matches for pairs from the same
	 * algebra. There must also be NO unpaired monads, so the algebra keys have
	 * to be identical to within sorting. Only monads sharing the same algebra
	 * name need to be checked against each other for reference matches. For
	 * those in the same algebra, we make use of the isRefereceMatch method and
	 * compare the two.
	 * 
	 * @param pTs
	 *            NyadRealD
	 * @param pN
	 *            NyadRealD
	 * @return boolean
	 */
	public static final boolean isStrongReferenceMatch(NyadRealD pTs, NyadRealD pN)
	{
		// Return false if the Nyads are not of the same order. 
		if (pTs.getNyadOrder() != pN.getNyadOrder()) return false;
		// and if they are, return false if the nyad's algebra orders are not the same.
		else if (pTs.getNyadAlgebraOrder() != pN.getNyadAlgebraOrder()) return false;

		// Check to see if the foot names match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now we start into the Monad lists.

		boolean check = false;
		for (MonadRealD tSpot : pTs.getMonadList())
		{
			check = false;
			for (MonadRealD tSpot2 : pN.getMonadList())
			{
				if (tSpot.getAlgebra().equals(tSpot2.getAlgebra()))
				{
					check = true;	// this is a temporary truth. More to check
					if (!isReferenceMatch(tSpot, tSpot2)) return false;
					//break; 
					// Don't break and we catch the case for weak nyads.
				}
			}
			// if check is true match(es) were found and passed isReferenceMatch
			// if check is false, we have a dangling monad in pTs, thus a fail.
			if (!check) return false;
		}
		// Making it this far implies that all tests have passed in forward order. 
		// Now for reverse order.
		
		check = false;
		for (MonadRealD tSpot : pN.getMonadList())
		{
			check = false;
			for (MonadRealD tSpot2 : pTs.getMonadList())
			{
				if (tSpot.getAlgebra().equals(tSpot2.getAlgebra()))
				{
					check = true;	// this is a temporary truth. More to check
					if (!isReferenceMatch(tSpot, tSpot2)) return false;
					//break; 
					// Don't break and we catch the case for weak nyads.
				}
			}
			// if check is true match(es) were found and passed isReferenceMatch
			// if check is false, we have a dangling monad in pTs, thus a fail.
			if (!check) return false;
		}
		// Making it this far implies that all tests have passed in reverse order. 
		// SINCE they have also passed in the forward order...
		// pN is a strong reference match for pTs.
		return true;
	}

	/**
	 * This method performs a weak test for a reference match. All properties of
	 * the Nyads must match except for the NyadRealD names and orders. The
	 * monads within the NyadRealD must also be reference matches for pairs from
	 * the same algebra. It is NOT required that monads from this nyad have
	 * counterparts in the other nyad, so the passed NyadRealDs may have a
	 * different order than this one. Unpaired monads are counted as matches
	 * against scalars from the field. Only monads sharing the same algebra name
	 * need to be checked against each other for reference matches. For those in
	 * the same algebra, we make use of the isRefereceMatch method and compare
	 * the two.
	 * 
	 * @param pTs
	 *            NyadRealD
	 * @param pN
	 *            NyadRealD
	 * @return boolean
	 */
	public static final boolean isWeakReferenceMatch(NyadRealD pTs, NyadRealD pN)
	{
		// Check to see if the foot objects match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now we start into the Monad lists. Find a monad from pTs and its
		// counterpart in other. If they are a reference match, move on. If
		// not return a false result.

		for (MonadRealD tSpot : pTs.getMonadList())
		{
			AlgebraRealD tAlg1 = tSpot.getAlgebra();
			for (MonadRealD tSpot2 : pN.getMonadList())
			{
				if (tAlg1.equals(tSpot2.getAlgebra()))
				{
					if (!isReferenceMatch(tSpot, tSpot2)) return false;
					break;
				}
			}
		}
		// Making it this far implies that tests have failed. There could be
		// dangling monads in either nyad or they could all be dangling, but
		// that is sufficient for a weak match because one can pad the nyads
		// with scalar (zero) monads to plug the gaps, thus turning each nyad
		// into one that is essentially the same until the pair pass as strong
		// reference matches. pN is a weak reference match for pTs.
		return true;
	}
	
	/**
	 * Display XML string that represents the Nyad
	 * 
	 * @param pN
	 * 			NyadRealD This is the nyad to be converted to XML.
	 * 
	 * @return String
	 */
	public static String toXMLString(NyadRealD pN)
	{
		StringBuilder rB = new StringBuilder("<Nyad name=\"" + pN.getName()
						+ "\" ");
		rB.append("name=\"" + pN.getName() + "\" ");
		rB.append("foot=\"" + pN.getFootPoint().getFootName() + "\" ");
		//rB.append("protoOne=\"" + pN.protoOne.getCardinalString() + "\" ");
		rB.append(">\n");
		rB.append(pN.getFootPoint().toXMLString());
		//rB.append(pN.protoOne.toXMLString());
		
		for (MonadRealD tSpot : pN.getMonadList())
			rB.append(MonadRealD.toXMLString(tSpot));

		rB.append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Nyad
	 * 
	 * @param pN
	 * 			NyadRealD This is the nyad to be converted to XML.
	 * 
	 * @return String
	 */
	public static String toXMLFullString(NyadRealD pN)
	{
		StringBuilder rB = new StringBuilder("<Nyad name=\"" + pN.getName()
						+ "\" ");
		rB.append("name=\"" + pN.getName() + "\" ");
		rB.append("foot=\"" + pN.getFootPoint().getFootName() + "\" ");
		//rB.append("protoOne=\"" + pN.protoOne.getCardinalString() + "\" ");
		rB.append(">\n");
		rB.append(pN.getFootPoint().toXMLString());
		//rB.append(pN.protoOne.toXMLString());
		
		for (MonadRealD tSpot : pN.getMonadList())
			rB.append(MonadRealD.toXMLFullString(tSpot));

		rB.append("</Nyad>\n");
		return rB.toString();
	}
	
	/**
	 * This array is the list of Monads that makes up the NyadRealD. It will be
	 * tied to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<MonadRealD>		monadList;
	
	/**
	 * This array is the list of algebras used in the NyadReald. 
	 */
	protected ArrayList<AlgebraRealD>	algebraList;

	/**
	 * This element holds holds the field's multiplicative unity. It gets used
	 * when constructing other parts of the NyadRealD to ensure cardinal
	 * safety.
	 */
	//protected RealD					protoOne;

	/**
	 * Simple copy constructor of a NyadRealD. The passed NyadRealD will be
	 * copied in detail. This contructor is used most often to get around
	 * operations that alter one of the nyads when the developer does not wish
	 * it to be altered.
	 * 
	 * @param pN
	 *            NyadRealD
	 * @throws CladosNyadException This exception is thrown when the offered Nyad
	 * is malformed. Make no assumptions!
	 */
	public NyadRealD(NyadRealD pN) throws CladosNyadException
	{
		this(pN.getName(), pN);
	}

	/**
	 * A basic constructor of a NyadRealD that starts with a Monad. The Monad
	 * will be copied and placed at the top of the list. The footPoint, however,
	 * will be used exactly as is.
	 * 
	 * @param pName
	 *            String
	 * @param pM
	 *            MonadRealD
	 * @throws CladosNyadException This exception is thrown when the offered Nyad
	 * is malformed. Make no assumptions!
	 */
	public NyadRealD(String pName, MonadRealD pM) throws CladosNyadException
	{
		setName(pName);
		setFootPoint(pM.getAlgebra().getFoot());
		monadList = new ArrayList<MonadRealD>(1);
		algebraList = new ArrayList<AlgebraRealD>(1);
		appendMonadCopy(pM);
	}

	/**
	 * A simple copy constructor of a NyadRealD. The passed NyadRealD will be
	 * copied without the name. This constructor is used most often to clone
	 * other objects in every way except name.
	 * 
	 * The Foot object is re-used.
	 * The Algebra object is re-used.
	 * The Nyad's proto-number object is re-used.
	 * The Nyad's monad objects are NOT re-used. Clones are created that...
	 * 		copy the monad name
	 * 		re-use the monad's algebra object
	 * 		copy the monad's frame name
	 * 		create new RealF's that clone the monad's coefficients such that they...
	 * 			re-use the RealF's Cardinal object
	 * 			merely copy the val array
	 * 
	 * @param pName	String
	 * @param pN	NyadRealD
	 * @throws CladosNyadException This exception is thrown when the offered Nyad
	 * is malformed. Make no assumptions!
	 */
	public NyadRealD(String pName, NyadRealD pN) throws CladosNyadException
	{
		setName(pName);
		setFootPoint(pN.getFootPoint());
		if (pN.getMonadList() != null)
		{
			monadList = new ArrayList<MonadRealD>(pN.getMonadList().size());
			algebraList = new ArrayList<AlgebraRealD>(pN.getAlgebraList().size());
			for (MonadRealD tSpot : pN.getMonadList())
				appendMonadCopy(tSpot);	
		}
	}

	/**
	 * Dyadic antisymmetric compression: 1/2 (left right - right left) Monads
	 * are placed in the same algebra and antisymmetrically multiplied to each
	 * other. A reference match test must pass for both after the algebra names
	 * have been changed.
	 * 
	 * @param pInto
	 * 		AlgebraRealD
	 * @param pFrom
	 * 		AlgebraRealD
	 * @throws CladosNyadException
	 * 	This exception is thrown when the monads to be compressed fail a field match test or 
	 * 	a reference match test used in multiplication.
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD antisymmCompress(AlgebraRealD pInto, AlgebraRealD pFrom)
					throws CladosNyadException
	{
		// The strings refer to particular algebras. Find them in the AlgebraKey
		// to know which two monads are being compress. Once that is done, do
		// the operation.
		int tempInto = findAlgebra(this, pInto);
		int tempFrom = findAlgebra(this, pFrom);

		try
		{
			antisymmCompress(tempInto, tempFrom);
			return this;
		}
		catch (FieldBinaryException e)
		{
			throw new CladosNyadException(this,
							"Monad cardinals must match for compression.");
		}
		catch (CladosMonadBinaryException e)
		{
			throw new CladosNyadException(this,
							"Monad mutliplication failed for compression.");
		}
	}

	/**
	 * Dyad anymmetric compression: 1/2 (left right - right left) Monads are
	 * placed in the same algebra and antisymmetrically multiplied to eachother.
	 * A reference match test must pass for both after the algebra names have
	 * been changed.
	 * 
	 * @param pInto
	 * 		int
	 * @param pFrom
	 * 		int
	 * 
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the monads to be compressed fail the Field match test
	 * @throws CladosMonadBinaryException
	 * 	This exception is thrown when the monads to be compressed fail a reference match test
	 */
	private void antisymmCompress(int pInto, int pFrom)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		MonadRealD tempLeft = monadList.get(pInto);
		MonadRealD tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplyAntisymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method re-uses
	 * the Monad offered as a parameter, so the NyadRealF DOES reference it.
	 * 
	 * @param pM	MonadRealD
	 * 
	 * @throws CladosNyadException
	 * 		This exception is thrown if the foot of the new monad fails to match
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD appendMonad(MonadRealD pM) throws CladosNyadException
	{
		// This method works if the foot of pM matches the foot of this nyad
		if (!pM.getAlgebra().getFoot().equals(getFootPoint()))
			throw new CladosNyadException(this,	"Monads is a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(pM);
		resetAlgebraList();
		return this;
	}
	
	/**
	 * Add another Monad to the list of monads in this nyad. This method creates
	 * a new copy of the Monad offered as a parameter, so the NyadRealD does not
	 * wind up referencing the passed Monad.
	 * 
	 * @param pM	MonadRealD
	 * 
	 * @throws CladosNyadException
	 * 		This exception is thrown if the foot of the new monad fails to match
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD appendMonadCopy(MonadRealD pM) throws CladosNyadException
	{
		// This method works if the foot of pM matches the foot of this nyad
		// The footPoint objects must match.
		if (!pM.getAlgebra().getFoot().equals(getFootPoint()))
			throw new CladosNyadException(this,	"Monads is a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(new MonadRealD(pM));
		resetAlgebraList();
		return this;
	}

	/**
	 * Create a new monad for this nyad using the prototype field and then
	 * append it to the end of the monadList. A 'zero' for the new algebra will
	 * be added to the list.
	 * This method creates a new algebra using the offered name and signature.
	 * It also creates a new frame using the offered name.
	 * It is not a copy method.
	 * 
	 * @param pName String
	 * @param pAlgebra String
	 * @param pFrame String
	 * @param pSig String
	 * @param pCard String
	 * 
	 * @throws CladosMonadException
	 * 		This exception is thrown when the new monad constructor fails.
	 * @throws BadSignatureException
	 * 		This exception is thrown when signature is rejected as invalid.
	 * @throws CladosNyadException
	 * 		This exception is thrown when the new monad cannot be appended.
	 * 		Perhaps there is a reference mismatch or the new monad failed construction.
	 * @throws GeneratorRangeException 
	 * 			This exception is thrown when the integer number of generators
	 * 			for the basis is out of the supported range. {0, 1, 2, ..., 14}
	 * @return NyadRealD
	 */
	public NyadRealD createMonad(String pName, String pAlgebra, String pFrame, String pSig, String pCard) 
			throws BadSignatureException, CladosMonadException, CladosNyadException, GeneratorRangeException
	{
		MonadRealD tM ;
		if (pCard == null)
			tM = new MonadRealD(pName, 
								pAlgebra, 
								pFrame, 
								getFootPoint(), 
								pSig, 
								(RealD) CladosField.REALD.createZERO(getFootPoint().getCardinal(0)));			
		else
			tM = new MonadRealD(pName, 
								pAlgebra, 
								pFrame, 
								getFootPoint(), 
								pSig, 
								(RealD) CladosField.REALD.createZERO(pCard));
		appendMonad(tM);
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the left.
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD dualLeft()
	{
		for (MonadRealD tSpot : monadList)
			tSpot.dualLeft();
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the right.
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD dualRight()
	{
		for (MonadRealD tSpot : monadList)
			tSpot.dualRight();
		return this;
	}
	
	/**
	 * Return the array of Algebras
	 * 
	 * @return ArrayList (of Algebras)
	 */
	public ArrayList<AlgebraRealD> getAlgebraList()
	{
		return algebraList;
	}
	
	/**
	 * Return the element of the array of Algebras at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return AlgebraRealD
	 */
	public AlgebraRealD getAlgebraList(int pj)
	{
		return algebraList.get(pj);
	}

	/**
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadRealD> getMonadList()
	{
		return monadList;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealD
	 */
	public MonadRealD getMonadList(int pj)
	{
		return monadList.get(pj);
	}
	
	/**
	 * Return the order of this Nyad
	 * 
	 * @return short
	 */
	public int getNyadOrder()
	{
		return monadList.size();
	}
	
	/**
	 * Return the algebra order of this Nyad
	 * 
	 * @return short
	 */
	public int getNyadAlgebraOrder()
	{
		return algebraList.size();
	}
	
	/**
	 * This method takes the Monad at the k'th position in the list and swaps it
	 * for the one in the k-1 position if there is one there. If the the key
	 * points to the first Monad, this function silently fails to pop it since
	 * it can't be popped.
	 * 
	 * @param key
	 *            int
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD pop(int key)
	{
		int limit = monadList.size();
		if (key > 0 && key < limit)
		{
			MonadRealD temp = monadList.remove(key - 1);
			monadList.add(key, temp);
		}
		return this;
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it
	 * for the one in the k+1 position there is one there. If the the key points
	 * to the last Monad, this function silently fails to push it since it can't
	 * be pushed.
	 * 
	 * @param key
	 *            int
	 * 
	 * @return NyadRealD
	 */
	public NyadRealD push(int key)
	{
		int limit = monadList.size();
		if (key >= 0 && key < limit - 1)
		{
			MonadRealD temp = monadList.remove(key);
			monadList.add(key + 1, temp);
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pthisone
	 *            int
	 * @throws CladosNyadException
	 * 		This exception is thrown when the monad to be removed can't be found.
	 * @return NyadRealD
	 */
	public NyadRealD removeMonad(int pthisone) throws CladosNyadException
	{
		MonadRealD test = null;
		try
		{
			test = monadList.remove(pthisone);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new CladosNyadException(this, "Can't find the Monad to remove.");
		}
		finally
		{
			if (test != null) 
			{
				monadList.trimToSize();
				resetAlgebraList();
			}
		}

		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pM
	 *            MonadRealD
	 * @throws CladosNyadException
	 * 		This exception is thrown when the monad to be removed can't be found.         
	 * @return NyadRealD
	 */
	public NyadRealD removeMonad(MonadRealD pM) throws CladosNyadException
	{
		int testfind = findMonad(this, pM);
		if (testfind < 0)
			throw new CladosNyadException(this,	"Can't find the Monad to remove.");
		removeMonad(testfind);
		return this;
	}

	/**
	 * NyadRealD Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            String
	 * @param pMag
	 *            RealD
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 * @return NyadRealD
	 */
	public NyadRealD scale(AlgebraRealD pk, RealD pMag)
					throws FieldBinaryException
	{
		int tF = findAlgebra(this, pk);
		if (tF >= 0 && tF <= monadList.size()) monadList.get(tF).scale(pMag);
		return this;
	}

	/**
	 * NyadRealD Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            int
	 * @param pMag
	 *            RealD
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 * @return NyadRealD
	 */
	public NyadRealD scale(int pk, RealD pMag) throws FieldBinaryException
	{
		if (pk >= 0 && pk < monadList.size()) monadList.get(pk).scale(pMag);
		return this;
	}

	/**
	 * Set the Monad List array of this NyadRealD.  A new ArrayList is created,
	 * but the Monads list the list are reused.
	 * @param pML
	 * 		ArrayList Contains the list of monads for the nyad
	 */
	protected void setMonadList(ArrayList<MonadRealD> pML)
	{
		if (pML == null)
			monadList = null;
		else
			monadList = new ArrayList<MonadRealD>(pML);

	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are
	 * placed in the same algebra and symmetrically multiplied to each other. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @param pInto
	 * 			AlgebraRealD
	 * @param pFrom
	 * 			AlgebraRealD
	 * 
	 * @throws CladosNyadException
	 * 		This exception is thrown when the monads being compressed fail a field match or
	 * 		reference match test used in multiplication.
	 * @return NyadRealD
	 */
	public NyadRealD symmCompress(AlgebraRealD pInto, AlgebraRealD pFrom)
					throws CladosNyadException
	{
		// The strings refer to particular algebras. Find them in the AlgebraKey
		// to know which two monads are being compress. Once that is done, do
		// the operation.
		int tempInto = findAlgebra(this, pInto);
		int tempFrom = findAlgebra(this, pFrom);

		try
		{
			symmCompress(tempInto, tempFrom);
			return this;
		}
		catch (FieldBinaryException e)
		{
			throw new CladosNyadException(this,
							"Monad cardinals must match for compression.");
		}
		catch (CladosMonadBinaryException e)
		{
			throw new CladosNyadException(this,
							"Monad cardinals must match for compression.");
		}

	}

	private void resetAlgebraList()
	{
		algebraList.clear();
		algebraList.ensureCapacity(monadList.size());
		for (MonadRealD point : monadList)
			if (!algebraList.contains(point.getAlgebra())) algebraList.add(point.getAlgebra());
		// 1 <= algebraList.size() <= monadList.size()
		// AlgebraList is reset to show which algebras are used by monads in this nyad
		Comparator<AlgebraRealD> comp = (AlgebraRealD a, AlgebraRealD b) -> {return b.compareTo(a);};
		Collections.sort(algebraList, comp);	// and now that list is sorted by name

		if (monadList.size()==1) 
		{
			_strongFlag = true;
			_oneAlgebra = true;
			return;
		}
		// We know monadList.size()>1 at this point
		if (algebraList.size()==1)
		{
			_strongFlag = false;
			_oneAlgebra = true;
			return;
		}
		else if (monadList.size() == algebraList.size())
		{
			_strongFlag = true;
			_oneAlgebra = false;
			return;
		}
		// We know monadList.size()>algebraList.size()>1 at this point
		_strongFlag = false;
		_oneAlgebra = false;		
	}
	
	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are
	 * placed in the same algebra and symmetrically multiplied to each other. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @param pInto
	 * 			  int
	 * @param pFrom
	 * 			  int
	 * 
	 * @throws CladosMonadBinaryException
	 * 		This exception is thrown when the monads being compressed fail a reference test
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 */
	private void symmCompress(int pInto, int pFrom)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		MonadRealD tempLeft = monadList.get(pInto);
		MonadRealD tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplySymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}
}