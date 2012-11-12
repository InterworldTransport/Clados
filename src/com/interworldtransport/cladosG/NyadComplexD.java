/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosG.NyadComplexD<br>
 * ------------
 * -------------------------------------------------------------------- <p>
 * Interworld Transport grants you ("Licensee") a license to this software under
 * the terms of the GNU General Public License.<br> A full copy of the license
 * can be found bundled with this package or code file. <p> If the license file
 * has become separated from the package, code file, or binary executable, the
 * Licensee is still expected to read about the license at the following URL
 * before accepting this material.
 * <blockquote><code>http://www.opensource.org/gpl
 * -license.html</code></blockquote> <p> Use of this code or executable objects
 * derived from it by the Licensee states their willingness to accept the terms
 * of the license. <p> A prospective Licensee unable to find a copy of the
 * license terms should contact Interworld Transport for a free copy. <p>
 * --------
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.NyadComplexD<br>
 * ----------------------------------
 * ----------------------------------------------
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosGExceptions.*;
import static com.interworldtransport.cladosG.MonadComplexD.*;

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
 * NyadComplexD objects must be declared with at least one Monad that has at least
 * two generators of geometry. Each Monad on the list must have the same
 * footPoint. At present, the NyadComplexD permits the encapsulated Monads to be
 * members of geometric algebras of different metric and dimensionality.
 * <p>
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class NyadComplexD extends NyadAbstract
{
	/**
	 * Return an integer pointing to the part of the nyad that covers the
	 * algebra named in the parameter. Coverage is true if a monad can be found
	 * in the nyad that belongs to the algebra.
	 * 
	 * @param NyadAbstract
	 *            pN
	 * @param pAlg
	 *            String
	 * @return int
	 */
	public static int findAlgebra(NyadComplexD pN, AlgebraComplexD pAlg)
	{
		for (MonadComplexD pM : pN.monadList)
			if (pAlg.equals(pM.getAlgebra())) return pN.monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param NyadAbstract
	 *            pN
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean hasAlgebra(NyadComplexD pN, AlgebraComplexD pAlg)
	{
		for (MonadComplexD pM : pN.monadList)
			if (pAlg.equals(pM.getAlgebra())) return true;
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
	 *            NyadComplexD
	 * @param pN
	 *            NyadComplexD
	 * @return boolean
	 */
	public static boolean isMEqual(NyadComplexD pTs, NyadComplexD pN)
	{
		// Checking for full reference matches first simply duplicates the work
		// here.
		// Each isGEqual test first tests for reference match. It is enough to
		// do
		// the front part of the reference match method.

		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder()) return false;

		// Check to see if the foot names match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now check the monad lists.
		boolean check = false;
		for (MonadComplexD tSpot : pTs.getMonadList())
		{
			check = false;
			AlgebraComplexD tAlg1 = tSpot.getAlgebra();
			for (MonadComplexD tSpot2 : pN.getMonadList())
			{
				if (tAlg1.equals(tSpot2.getAlgebra()))
				{
					check = true;
					if (!tSpot.isGEqual(tSpot2)) return false;
					break;
				}
			}
			// if check is true a match was found
			// if check is false, we have a dangling monad, so they can't
			// be equal.
			if (!check) return false;
		}
		// To get this far, all Monads in one list must pass the equality
		// test for their counterparts in the other list.
		return true;
	}

	/**
	 * This method determines whether or not the Nyad is a pscalar in the
	 * particular algebra in question.
	 * 
	 * @param pN
	 *            NyadComplexD
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean isPScalarAt(NyadComplexD pN, AlgebraComplexD pAlg)
	{
		int tSpot = findAlgebra(pN, pAlg);
		if (tSpot > 0)
		{
			MonadComplexD tM = pN.getMonadList(tSpot);
			if (isGrade(tM, tM.getAlgebra().getGProduct().getGradeCount() - 1))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * This method determines whether or not the Nyad is a scalar in the
	 * particular algebra in question.
	 * 
	 * @param pN
	 *            NyadComplexD
	 * @param pAlg
	 *            String
	 * @return boolean
	 */
	public static boolean isScalarAt(NyadComplexD pN, AlgebraComplexD pAlg)
	{
		int tSpot = findAlgebra(pN, pAlg);
		if (tSpot > 0)
		{
			if (isGrade(pN.getMonadList(tSpot), 0))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * This method performs a strong test for a reference match. All properties
	 * of the Nyads must match except for the NyadComplexD names. The monads within
	 * the NyadComplexD must also be reference matches for pairs from the same
	 * algebra. There must also be NO unpaired monads, so the algebra keys have
	 * to be identical to within sorting. Only monads sharing the same algebra
	 * name need to be checked against each other for reference matches. For
	 * those in the same algebra, we make use of the isRefereceMatch method and
	 * compare the two.
	 * 
	 * @param pTs
	 *            NyadComplexD
	 * @param pN
	 *            NyadComplexD
	 * @return boolean
	 */
	public static boolean isStrongReferenceMatch(NyadComplexD pTs, NyadComplexD pN)
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
		for (MonadComplexD tSpot : pTs.getMonadList())
		{
			check = false;
			AlgebraComplexD tAlg1 = tSpot.getAlgebra();
			for (MonadComplexD tSpot2 : pN.getMonadList())
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
	 * the Nyads must match except for the NyadComplexD names and orders. The
	 * monads within the NyadComplexD must also be reference matches for pairs from
	 * the same algebra. It is NOT required that monads from this nyad have
	 * counterparts in the other nyad, so the passed NyadComplexDs may have a
	 * different order than this one. Unpaired monads are counted as matches
	 * against scalars from the field. Only monads sharing the same algebra name
	 * need to be checked against each other for reference matches. For those in
	 * the same algebra, we make use of the isRefereceMatch method and compare
	 * the two.
	 * 
	 * @param pTs
	 *            NyadComplexD
	 * @param pN
	 *            NyadComplexD
	 * @return boolean
	 */
	public static boolean isWeakReferenceMatch(NyadComplexD pTs, NyadComplexD pN)
	{
		// Check to see if the foot objects match
		if (pTs.getFootPoint() != pN.getFootPoint()) return false;

		// Now we start into the Monad lists. Find a monad from pTs and its
		// counterpart in other. If they are a reference match, move on. If
		// not return a false result.

		for (MonadComplexD tSpot : pTs.getMonadList())
		{
			AlgebraComplexD tAlg1 = tSpot.getAlgebra();
			for (MonadComplexD tSpot2 : pN.getMonadList())
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
	 * This array is the list of Monads that makes up the NyadComplexD. It will be
	 * tied to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<MonadComplexD>	monadList;

	/**
	 * This element holds holds the field's multiplicative unity. It gets used
	 * when constructing other parts of the NyadComplexD to ensure field type
	 * safety.
	 */
	protected ComplexD					protoOne;

	/**
	 * Simple copy constructor of a NyadComplexD. The passed NyadComplexD will be
	 * copied in detail. This contructor is used most often to get around
	 * operations that alter one of the nyads when the developer does not wish
	 * it to be altered.
	 * 
	 * @param pN
	 *            NyadComplexD
	 */
	public NyadComplexD(NyadComplexD pN)
	{
		this(pN.getName(), pN);
	}

	/**
	 * A basic constructor of a NyadComplexD that starts with a Monad. The Monad
	 * will be copied and placed at the top of the list. The footPoint, however,
	 * will be used exactly as is.
	 * 
	 * @param pName
	 *            String
	 * @param pM
	 *            MonadComplexD
	 */
	public NyadComplexD(String pName, MonadComplexD pM)
	{
		setName(pName);
		setFootPoint(pM.getAlgebra().getFootPoint());
		protoOne = new ComplexD(pM.getAlgebra().protoNumber, 1.0d, 0.0d);

		monadList = new ArrayList<MonadComplexD>(1);
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
	 * A simple copy constructor of a NyadComplexD. The passed NyadComplexD will be
	 * copied without the name. This contructor is used most often to clone
	 * other objects in every way except name.
	 * 
	 * @param pName
	 *            String
	 * @param pN
	 *            NyadComplexD
	 */
	public NyadComplexD(String pName, NyadComplexD pN)
	{
		setName(pName);
		setFootPoint(pN.getFootPoint());
		protoOne = ComplexD.ZERO(getFootPoint().getFootName() + "-ComplexD");
		if (pN.getMonadList() == null)
			monadList = null;
		else
			setMonadList(pN.getMonadList());
	}

	/**
	 * Dyadic antisymmetric compression: 1/2 (left right - right left) Monads
	 * are placed in the same algebra and antisymmetrically multiplied to each
	 * other. A reference match test must pass for both after the algebra names
	 * have been changed.
	 */
	public NyadComplexD antisymmCompress(AlgebraComplexD pInto, AlgebraComplexD pFrom)
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
	 */
	private void antisymmCompress(int pInto, int pFrom)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		MonadComplexD tempLeft = monadList.get(pInto);
		MonadComplexD tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplyAntisymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method creates
	 * a new copy of the Monad offered as a parameter, so the NyadComplexD does not
	 * wind up referencing the passed Monad.
	 * 
	 * @param pM
	 *            MonadComplexD
	 */
	public NyadComplexD appendMonad(MonadComplexD pM) throws CladosNyadException
	{
		// This method works if the foot of pM matches the foot of this nyad
		// but the algebra of pM is not already used in the monadList.
		
		// A check should be made to ensure pM is OK to append.
		// The footPoint objects must match.
		if (!pM.getAlgebra().getFootPoint().equals(getFootPoint()))
			throw new CladosNyadException(this,
							"Nyads should not have foot name mismatch");
		
		// Now that the feet are guaranteed the same, it is time to 
		// avoid duplication of algebra names in the monad list
		if (!hasAlgebra(this, pM.getAlgebra()))
		{
			// Add Monad to the ArrayList
			monadList.ensureCapacity(monadList.size() + 1);
			monadList.add(new MonadComplexD(pM));
		}
		else
			throw new CladosNyadException(this,
							"Nyads should have unique algebra names");

		return this;
	}

	/**
	 * Create a new monad for this nyad using the prototype field and then
	 * append it to the end of the monadList. A 'zero' for the new algebra will
	 * be added to the list.
	 * 
	 * @throws CladosMonadException
	 * @throws BadSignatureException
	 * @throws CladosNyadException
	 */
	public NyadComplexD createMonad(String pName, String pAlgebra, String pFrame,
					String pSig) throws BadSignatureException,
					CladosMonadException, CladosNyadException
	{

		MonadComplexD tM = new MonadComplexD(pName, pAlgebra, pFrame, getFootPoint()
						.getFootName(), pSig, protoOne);
		appendMonad(tM);
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the left.
	 */
	public NyadComplexD dualLeft()
	{
		for (MonadComplexD tSpot : monadList)
			tSpot.dualLeft();
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the right.
	 */
	public NyadComplexD dualRight()
	{
		for (MonadComplexD tSpot : monadList)
			tSpot.dualRight();
		return this;
	}

	/**
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadComplexD> getMonadList()
	{
		return monadList;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return MonadComplexD
	 */
	public MonadComplexD getMonadList(int pj)
	{
		return monadList.get(pj);
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it
	 * for the one in the k-1 position if there is one there. If the the key
	 * points to the first Monad, this function silently fails to pop it since
	 * it can't be popped.
	 * 
	 * @param key
	 *            int
	 */
	public NyadComplexD pop(int key)
	{
		int limit = monadList.size();
		if (key > 0 && key < limit)
		{
			MonadComplexD temp = monadList.remove(key - 1);
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
	 */
	public NyadComplexD push(int key)
	{
		int limit = monadList.size();
		if (key >= 0 && key < limit - 1)
		{
			MonadComplexD temp = monadList.remove(key);
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
	 */
	public NyadComplexD removeMonad(int pthisone) throws CladosNyadException
	{
		MonadComplexD test = null;
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
	 *            MonadComplexD
	 */
	public NyadComplexD removeMonad(MonadComplexD pM) throws CladosNyadException
	{
		int testfind = findAlgebra(this, pM.getAlgebra());
		if (testfind >= 0)
			removeMonad(testfind);
		else
			throw new CladosNyadException(this,
							"Can't find the Monad to remove.");
		return this;
	}

	/**
	 * NyadComplexD Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            String
	 * @param pMag
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public NyadComplexD scale(AlgebraComplexD pk, ComplexD pMag)
					throws FieldBinaryException
	{
		int tF = findAlgebra(this, pk);
		if (tF >= 0 && tF <= monadList.size()) monadList.get(tF).scale(pMag);
		return this;
	}

	/**
	 * NyadComplexD Scaling: Pick a monad and scale it by the magnitude provided.
	 * Only one monad can be scaled within a nyad at a time. Note that a request
	 * to scale a monad that cannot be found in the list results in no action
	 * and no exception. The scaling is effectively performed against a 'zero'
	 * monad for the algebra not represented in the list since much monads can
	 * be appended to the list without really changing the nature of the nyad.
	 * 
	 * @param pk
	 *            int
	 * @param pMag
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public NyadComplexD scale(int pk, ComplexD pMag) throws FieldBinaryException
	{
		if (pk >= 0 && pk < monadList.size()) monadList.get(pk).scale(pMag);
		return this;
	}

	/**
	 * Set the Monad List array of this NyadComplexD.  A new ArrayList is created,
	 * but the Monads list the list are reused.
	 */
	protected void setMonadList(ArrayList<MonadComplexD> pML)
	{
		if (pML == null)
			monadList = null;
		else
			monadList = new ArrayList<MonadComplexD>(pML);

	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are
	 * placed in the same algebra and symmetrically multiplied to each other. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public NyadComplexD symmCompress(AlgebraComplexD pInto, AlgebraComplexD pFrom)
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
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	private void symmCompress(int pInto, int pFrom)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		MonadComplexD tempLeft = monadList.get(pInto);
		MonadComplexD tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplySymm(tempRight);

		tempRight = monadList.remove(pFrom);
		monadList.trimToSize();
	}

}
