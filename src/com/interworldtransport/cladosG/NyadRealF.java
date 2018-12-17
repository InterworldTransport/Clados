/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.NyadRealF<br>
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
 * ---com.interworldtransport.cladosG.NyadRealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import static com.interworldtransport.cladosG.MonadRealF.*;

import java.util.ArrayList;

import com.interworldtransport.cladosF.*;
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
 * NyadRealF objects must be declared with at least one Monad that has at least
 * two generators of geometry. Each Monad on the list must have the same
 * footPoint. At present, the NyadRealF permits the encapsulated Monads to be
 * members of geometric algebras of different metric and dimensionality.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class NyadRealF extends NyadAbstract
{
	/**
	 * Return an integer pointing to the part of the nyad that covers the
	 * algebra named in the parameter. Coverage is true if a monad can be found
	 * in the nyad that belongs to the algebra.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pAlg
	 *            String
	 * @return int
	 */
	public static int findAlgebra(NyadRealF pN, AlgebraRealF pAlg)
	{
		for (MonadRealF pM : pN.getMonadList())
			if (pAlg.equals(pM.getAlgebra())) return pN.monadList.indexOf(pM);
		return -1;
	}
	
	/**
	 * Return an integer pointing to the part of the nyad expressed in the frame
	 * named in the parameter.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static int findFrame(NyadRealF pN, String pFrame)
	{
		for (MonadRealF pM : pN.getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return pN.monadList.indexOf(pM);
		return -1;
	}
	
	/**
	 * Return an integer pointing to the part of the nyad with the expressed
	 * name.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static int findName(NyadRealF pN, String pName)
	{
		for (MonadRealF pM : pN.getMonadList())
			if (pName.equals(pM.getName())) return pN.monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean hasAlgebra(NyadRealF pN, AlgebraRealF pAlg)
	{
		for (MonadRealF pM : pN.getMonadList())
			if (pAlg.equals(pM.getAlgebra())) return true;
		return false;
	}
	
	/**
	 * Return a boolean stating whether or not the nyad is expressed in the
	 * frame named in the parameter.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static boolean hasFrame(NyadRealF pN, String pFrame)
	{
		for (MonadRealF pM : pN.getMonadList())
			if (pFrame.equals(pM.getFrameName())) return true;
		return false;
	}
	
	/**
	 * Return a boolean stating whether or not the nyad contained the named
	 * monad.
	 * 
	 * @param pN
	 * 			NyadRealF
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static boolean hasName(NyadRealF pN, String pName)
	{
		for (MonadRealF pM : pN.getMonadList())
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
	 *            NyadRealF
	 * @param pN
	 *            NyadRealF
	 * @return boolean
	 */
	public static boolean isMEqual(NyadRealF pTs, NyadRealF pN)
	{
		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder()) return false;

		// Check to see if the foot names match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now check the monad lists.
		boolean forwardCheck = false;
		for (MonadRealF tSpot : pTs.getMonadList())
		{
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			forwardCheck = false;
			// Get the Algebra for tSpot
			AlgebraRealF tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadRealF tSpot2 : pN.getMonadList())
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
		for (MonadRealF tSpot : pN.getMonadList())
		{
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			backwardCheck = false;
			// Get the Algebra for tSpot
			AlgebraRealF tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadRealF tSpot2 : pTs.getMonadList())
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
	 * This method determines whether or not the Nyad is a pscalar in the
	 * particular algebra in question.
	 * 
	 * @param pN
	 *            NyadRealF
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean isPScalarAt(NyadRealF pN, AlgebraRealF pAlg)
	{
		int tSpot = findAlgebra(pN, pAlg);
		if (tSpot > 0)
		{
			MonadRealF tM = pN.getMonadList(tSpot);
			if (isGrade(tM, tM.getAlgebra().getGProduct().getGradeCount() - 1))
				return true;
			
			return false;
		}
		
		return false;
	}

	/**
	 * This method determines whether or not the Nyad is a scalar in the
	 * particular algebra in question.
	 * 
	 * @param pN
	 *            NyadRealF
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean isScalarAt(NyadRealF pN, AlgebraRealF pAlg)
	{
		int tSpot = findAlgebra(pN, pAlg);
		if (tSpot > 0)
		{
			if (isGrade(pN.getMonadList(tSpot), 0))
				return true;
			
			return false;
		}
		
		return false;
	}

	/**
	 * This method performs a strong test for a reference match. All properties
	 * of the Nyads must match except for the NyadRealF names. The monads within
	 * the NyadRealF must also be reference matches for pairs from the same
	 * algebra. There must also be NO unpaired monads, so the algebra keys have
	 * to be identical to within sorting. Only monads sharing the same algebra
	 * name need to be checked against each other for reference matches. For
	 * those in the same algebra, we make use of the isRefereceMatch method and
	 * compare the two.
	 * 
	 * @param pTs
	 *            NyadRealF
	 * @param pN
	 *            NyadRealF
	 * @return boolean
	 */
	public static boolean isStrongReferenceMatch(NyadRealF pTs, NyadRealF pN)
	{
		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder()) return false;

		// Check to see if the foot names match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now we start into the Monad lists. Find a monad from this and its
		// counterpart in other. If they are a reference match, move on. If not
		// return a false result.
		// Because the nyads must be of the same order at this point, sifting
		// through one of them will detect unmatched monads.

		boolean check = false;
		for (MonadRealF tSpot : pTs.getMonadList())
		{
			check = false;
			AlgebraRealF tAlg1 = tSpot.getAlgebra();
			for (MonadRealF tSpot2 : pN.getMonadList())
			{
				if (tAlg1.equals(tSpot2.getAlgebra()))
				{
					check = true;
					if (!isReferenceMatch(tSpot, tSpot2)) return false;
					break;
				}
			}
			// if check is true a match was found
			// if check is false, we have a dangling monad, so they can't
			// be equal.
			if (!check) return false;
		}
		// Making it this far implies that all tests have passed. pN is a
		// strong reference match for pTs.
		return true;
	}

	/**
	 * This method performs a weak test for a reference match. All properties of
	 * the Nyads must match except for the NyadRealF names and orders. The
	 * monads within the NyadRealF must also be reference matches for pairs from
	 * the same algebra. It is NOT required that monads from this nyad have
	 * counterparts in the other nyad, so the passed NyadRealFs may have a
	 * different order than this one. Unpaired monads are counted as matches
	 * against scalars from the field. Only monads sharing the same algebra name
	 * need to be checked against each other for reference matches. For those in
	 * the same algebra, we make use of the isRefereceMatch method and compare
	 * the two.
	 * 
	 * @param pTs
	 *            NyadRealF
	 * @param pN
	 *            NyadRealF
	 * @return boolean
	 */
	public static boolean isWeakReferenceMatch(NyadRealF pTs, NyadRealF pN)
	{
		// Check to see if the foot objects match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now we start into the Monad lists. Find a monad from pTs and its
		// counterpart in other. If they are a reference match, move on. If
		// not return a false result.

		for (MonadRealF tSpot : pTs.getMonadList())
		{
			AlgebraRealF tAlg1 = tSpot.getAlgebra();
			for (MonadRealF tSpot2 : pN.getMonadList())
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
	 * 			NyadRealF This is the nyad to be converted to XML.
	 * 
	 * @return String
	 */
	public static String toXMLString(NyadRealF pN)
	{
		StringBuffer rB = new StringBuffer("<Nyad name=\"" + pN.getName()
						+ "\" ");
		rB.append("name=\"" + pN.getName() + "\" ");
		rB.append("foot=\"" + pN.getFootPoint().getFootName() + "\" ");
		rB.append("protoOne=\"" + pN.protoOne.getFieldTypeString() + "\" ");
		rB.append(">\n");
		rB.append(pN.getFootPoint().toXMLString());
		rB.append(pN.protoOne.toXMLString());
		
		for (MonadRealF tSpot : pN.getMonadList())
			rB.append(MonadRealF.toXMLString(tSpot));

		rB.append("</Nyad>\n");
		return rB.toString();
	}
	
	/**
	 * Display XML string that represents the Nyad
	 * 
	 * @param pN
	 * 			NyadRealF This is the nyad to be converted to XML.
	 * 
	 * @return String
	 */
	public static String toXMLFullString(NyadRealF pN)
	{
		StringBuffer rB = new StringBuffer("<Nyad name=\"" + pN.getName()
						+ "\" ");
		rB.append("name=\"" + pN.getName() + "\" ");
		rB.append("foot=\"" + pN.getFootPoint().getFootName() + "\" ");
		rB.append("protoOne=\"" + pN.protoOne.getFieldTypeString() + "\" ");
		rB.append(">\n");
		rB.append(pN.getFootPoint().toXMLString());
		rB.append(pN.protoOne.toXMLString());
		
		for (MonadRealF tSpot : pN.getMonadList())
			rB.append(MonadRealF.toXMLFullString(tSpot));

		rB.append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * This array is the list of Monads that makes up the NyadRealF. It will be
	 * tied to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<MonadRealF>	monadList;

	/**
	 * This element holds holds the field's multiplicative unity. It gets used
	 * when constructing other parts of the NyadRealF to ensure field type
	 * safety.
	 */
	public RealF					protoOne;

	/**
	 * Simple copy constructor of a NyadRealF. The passed NyadRealF will be
	 * copied in detail. This contructor is used most often to get around
	 * operations that alter one of the nyads when the developer does not wish
	 * it to be altered.
	 * 
	 * @param pN
	 *            NyadRealF
	 * @throws CladosNyadException This exception is thrown when the offered Nyad
	 * is malformed. Make no assumptions!
	 */
	public NyadRealF(NyadRealF pN) throws CladosNyadException
	{
		this(pN.getName(), pN);
	}

	/**
	 * A basic constructor of a NyadRealF that starts with a Monad. The Monad
	 * will be copied and placed at the top of the list. The footPoint, however,
	 * will be used exactly as is.
	 * 
	 * @param pName
	 *            String
	 * @param pM
	 *            MonadRealF
	 */
	public NyadRealF(String pName, MonadRealF pM)
	{
		setName(pName);
		setFootPoint(pM.getAlgebra().getFoot());
		protoOne = new RealF(pM.getAlgebra().protoNumber, 1.0f);

		monadList = new ArrayList<MonadRealF>(1);
		try
		{
			appendMonad(pM);
		}
		catch (CladosNyadException e)
		{
			// Can't actually happen with this constructor, but the appendMonad
			// method can be called from elsewhere so it is more general.
			e.printStackTrace();
		}
	}

	/**
	 * A simple copy constructor of a NyadRealF. The passed NyadRealF will be
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
	 * 			re-use the RealF's DivFieldType object
	 * 			merely copy the val array
	 * 
	 * @param pName	String
	 * @param pN	NyadRealF
	 * @throws CladosNyadException This exception is thrown when the offered Nyad
	 * is malformed. Make no assumptions!
	 */
	public NyadRealF(String pName, NyadRealF pN) throws CladosNyadException
	{
		setName(pName);
		setFootPoint(pN.getFootPoint());
		protoOne=pN.protoOne;
		if (pN.getMonadList() != null)
		{
			monadList = new ArrayList<MonadRealF>(pN.getMonadList().size());
			for (MonadRealF tSpot : pN.getMonadList())
				appendMonad(tSpot);	
		}
	}

	/**
	 * Dyadic antisymmetric compression: 1/2 (left right - right left) Monads
	 * are placed in the same algebra and antisymmetrically multiplied to each
	 * other. A reference match test must pass for both after the algebra names
	 * have been changed.
	 * 
	 * @param pInto
	 * 		AlgebraRealF
	 * @param pFrom
	 * 		AlgebraRealF
	 * @throws CladosNyadException
	 * 	This exception is thrown when the monads to be compressed fail a field match test or 
	 * 	a reference match test used in multiplication.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF antisymmCompress(AlgebraRealF pInto, AlgebraRealF pFrom)
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
							"Monad Field types must match for compression.");
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
		MonadRealF tempLeft = monadList.get(pInto);
		MonadRealF tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplyAntisymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method creates
	 * a new copy of the Monad offered as a parameter, so the NyadRealF does not
	 * wind up referencing the passed Monad.
	 * 
	 * @param pM
	 *            MonadRealF
	 * 
	 * @throws CladosNyadException
	 * 		This exception is thrown if the foot of the new monad fails to match
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF appendMonad(MonadRealF pM) throws CladosNyadException
	{
		// This method works if the foot of pM matches the foot of this nyad
		// but the algebra of pM is not already used in the monadList.

		// A check should be made to ensure pM is OK to append.
		// The footPoint objects must match.
		if (!pM.getAlgebra().getFoot().equals(getFootPoint()))
			throw new CladosNyadException(this,	"Monads in a nyad should share a Foot");

		// Now that the feet are guaranteed the same, it is time to
		// avoid duplication of algebra names in the monad list
		if (hasAlgebra(this, pM.getAlgebra()))
			throw new CladosNyadException(this,	"Monads in a nyad should have unique Algebras");
				
		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(new MonadRealF(pM));
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
	 * @param pName
	 * 		String
	 * @param pAlgebra
	 * 		String
	 * @param pFrame
	 * 		String
	 * @param pSig
	 * 		String
	 * 
	 * @throws CladosMonadException
	 * 		This exception is thrown when the new monad constructor fails.
	 * @throws BadSignatureException
	 * 		This exception is thrown when signature is rejected as invalid.
	 * @throws CladosNyadException
	 * 		This exception is thrown when the new monad cannot be appended.
	 * 		Perhaps there is a reference mismatch or the new monad failed construction.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF createMonad(String pName, String pAlgebra, String pFrame,
					String pSig) throws BadSignatureException,
					CladosMonadException, CladosNyadException
	{
		MonadRealF tM = new MonadRealF(	pName, 
										pAlgebra, 
										pFrame, 
										getFootPoint(), 
										pSig, 
										protoOne);
		appendMonad(tM);
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the left.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF dualLeft()
	{
		for (MonadRealF tSpot : monadList)
			tSpot.dualLeft();
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the right.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF dualRight()
	{
		for (MonadRealF tSpot : monadList)
			tSpot.dualRight();
		return this;
	}

	/**
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadRealF> getMonadList()
	{
		return monadList;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealF
	 */
	public MonadRealF getMonadList(int pj)
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
	 * This method takes the Monad at the k'th position in the list and swaps it
	 * for the one in the k-1 position if there is one there. If the the key
	 * points to the first Monad, this function silently fails to pop it since
	 * it can't be popped.
	 * 
	 * @param key
	 *            int
	 *            
	 * @return NyadRealF
	 */
	public NyadRealF pop(int key)
	{
		int limit = monadList.size();
		if (key > 0 && key < limit)
		{
			MonadRealF temp = monadList.remove(key - 1);
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
	 * @return NyadRealF
	 */
	public NyadRealF push(int key)
	{
		int limit = monadList.size();
		if (key >= 0 && key < limit - 1)
		{
			MonadRealF temp = monadList.remove(key);
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
	 * @return NyadRealF
	 */
	public NyadRealF removeMonad(int pthisone) throws CladosNyadException
	{
		MonadRealF test = null;		
		try
		{
			test = monadList.remove(pthisone);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new CladosNyadException(this,
							"Can't find the Monad to remove.");
		}
		finally
		{
			if (test != null) monadList.trimToSize();
		}

		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pM
	 *            MonadRealF
	 * @throws CladosNyadException
	 * 		This exception is thrown when the monad to be removed can't be found.
	 * @return NyadRealF
	 */
	public NyadRealF removeMonad(MonadRealF pM) throws CladosNyadException
	{
		int testfind = findAlgebra(this, pM.getAlgebra());
		if (testfind >= 0)
			removeMonad(testfind);
		else
			throw new CladosNyadException(this,	"Can't find the Monad to remove.");
		return this;
	}

	/**
	 * NyadRealF Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            String
	 * @param pMag
	 *            RealF
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 * @return NyadRealF
	 */
	public NyadRealF scale(AlgebraRealF pk, RealF pMag)
					throws FieldBinaryException
	{
		int tF = findAlgebra(this, pk);
		if (tF >= 0 && tF <= monadList.size()) monadList.get(tF).scale(pMag);
		return this;
	}

	/**
	 * NyadRealF Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            int
	 * @param pMag
	 *            RealF
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 * @return NyadRealF
	 */
	public NyadRealF scale(int pk, RealF pMag) throws FieldBinaryException
	{
		if (pk >= 0 && pk < monadList.size()) monadList.get(pk).scale(pMag);
		return this;
	}

	/**
	 * Set the Monad List array of this NyadRealF. A new ArrayList is created,
	 * but the Monads list the list are reused.
	 * @param pML
	 * 		ArrayList Contains the list of monads for the nyad
	 */
	protected void setMonadList(ArrayList<MonadRealF> pML)
	{
		if (pML == null)
			monadList = null;
		else
			monadList = new ArrayList<MonadRealF>(pML);

	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are
	 * placed in the same algebra and symmetrically multiplied to each other. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @param pInto
	 * 			AlgebraRealF
	 * @param pFrom
	 * 			AlgebraRealF
	 * 
	 * @throws CladosNyadException
	 * 		This exception is thrown when the monads being compressed fail a field match or
	 * 		reference match test used in multiplication.
	 * @return NyadRealF
	 */
	public NyadRealF symmCompress(AlgebraRealF pInto, AlgebraRealF pFrom)
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
							"Monad Field types must match for compression.");
		}
		catch (CladosMonadBinaryException e)
		{
			throw new CladosNyadException(this,
							"Monad Field types must match for compression.");
		}

	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are
	 * placed in the same algebra and symmetrically multiplied to each other. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @param pInto
	 * 		int
	 * @param pFrom
	 * 		int
	 * 
	 * @throws CladosMonadBinaryException
	 * 		This exception is thrown when the monads being compressed fail a reference test
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the scale field doesn't match the nyad's field.
	 */
	private void symmCompress(int pInto, int pFrom)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		MonadRealF tempLeft = monadList.get(pInto);
		MonadRealF tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplySymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}
}