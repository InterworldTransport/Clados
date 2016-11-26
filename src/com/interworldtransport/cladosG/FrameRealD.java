/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.FrameRealD<br>
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
 * ---com.interworldtransport.cladosG.FrameRealD<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;
import com.interworldtransport.cladosGExceptions.CladosFrameException;

/**
 * The frame object holds all basis details that support the reference frame for
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class FrameRealD extends FrameAbstract 
{
	/**
	 * Return an integer pointing to the part of the nyad that covers the
	 * algebra named in the parameter. Coverage is true if a monad can be found
	 * in the nyad that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealD
	 * @param pMonad
	 *            MonadRealD
	 * @return int
	 */
	public static int findMonad(FrameRealD pRF, MonadRealD pMonad)
	{
		for (MonadRealD pM : pRF.fBasis)
			if (pM.isGEqual(pMonad)) return pRF.fBasis.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealD
	 * @param pMonad
	 *            MonadRealD
	 * @return boolean
	 */
	public static boolean hasMonad(FrameRealD pRF, MonadRealD pMonad)
	{
		for (MonadRealD pM : pRF.fBasis)
			if (pM.isGEqual(pMonad)) return true;
		return false;
	}

	public static boolean isREqual(FrameRealD pRF1, FrameRealD pRF2)
	{

		// Check to see if the Algebras match
		if (pRF1.getAlgebra() != pRF2.getAlgebra()) return false;

		// Check first to see if the Frames are of the same order. Return false
		// if they are not.
		if (pRF1.getFrameOrder() != pRF2.getFrameOrder()) return false;

		// Now check the monad lists.
		boolean check = false;
		for (MonadRealD tSpot : pRF1.getFBasis())
		{
			check = false;
			String tName1 = tSpot.getName();
			for (MonadRealD tSpot2 : pRF2.getFBasis())
			{
				if (tName1.equals(tSpot2.getName()))
				{
					check = true;
					if (!tSpot.isGEqual(tSpot2)) return false;
					break;
				}
			}
			// if check is true a match was found (by monad name)
			// if check is false, we have a dangling monad (by monad name),
			// so they can't be equal.
			if (!check) return false;
		}
		// To get this far, all Monads in one list must pass the equality
		// test for their counterparts (by name) in the other list.
		return true;
	}

	/**
	 * The fBasis holds vector monads that represent the reference directions to
	 * be used by any monad that refers to this frame object. Multiplication and
	 * addition in the monad are performed relative to these reference
	 * directions. Addition at the monad level can be done directly with no
	 * reference to the default basis in the algebra. Multiplication, however,
	 * is handed off to the reference frame since the product of two reference
	 * directions might result in a combination result.
	 * <p>
	 * The monads in the frame use the default basis constructed from the
	 * generators in the algebra. This is represented by the fact that the
	 * reference monads refer to the frame to which they are attached.
	 * Multiplication at the frame level never calls on the multiplication of
	 * the reference monads, so there is no danger of a loop occuring. The frame
	 * must be able to resolve operations without calling on the operations of
	 * the reference monads.
	 * 
	 */
	public ArrayList<MonadRealD>	fBasis;

	/**
	 * Frame constructor with an empty basis list.
	 * 
	 * @param pName
	 * 			This is the name of the Frame being constructed
	 * @param pAlg
	 * 			This is the algebra referenced in the Frame
	 */
	public FrameRealD(String pName, AlgebraRealD pAlg)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealD>(
						algebra.getGBasis().getBladeCount() - 1);
		vectorList = null;
	}

	/**
	 * Frame Constructor with a full basis list.
	 * 
	 * @param pName
	 *            String
	 * @param pAlg
	 *            AlgebraRealD
	 * @param pML
	 * 			  ArrayList 
	 * 			  List contains MonadRealD entries used in construction.
	 */
	public FrameRealD(String pName, AlgebraRealD pAlg, ArrayList<MonadRealD> pML)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealD>(pML);
	}

	/**
	 * Add another Monad to the list of monads in this frame. This method does
	 * not create a new copy of the Monad offered as a parameter. The Frame DOES
	 * wind up referencing the passed Monad.
	 * 
	 * Not sure why the method returns the Frame after the appending operation.
	 * 
	 * @param pM
	 *            MonadRealD this is the referenced monad for the Frame.
	 * @throws CladosFrameException 
	 * 	Monads in a Frame must satisfy ReferenceMatch
	 * @return FrameRealD
	 */
	public FrameRealD appendVMonad(MonadRealD pM) throws CladosFrameException
	{
		// This method works if the foot of pM matches the foot of this nyad
		// but the algebra of pM is not already used in the monadList.

		// A check should be made to ensure pM is OK to append.
		// The footPoint objects must match.
		if (!pM.getAlgebra().equals(getAlgebra()))
			throw new CladosFrameException(this,
							"Monads in a Frame should have algebras match");

		// Add Monad to the ArrayList
		fBasis.ensureCapacity(fBasis.size() + 1);

		pM.setFrameName(this.name);
		pM.frame = this;
		fBasis.add(new MonadRealD(pM));

		return this;
	}

	/**
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadRealD> getFBasis()
	{
		return fBasis;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealD
	 */
	public MonadRealD getFBasis(int pj)
	{
		return fBasis.get(pj);
	}

	/**
	 * Return the element of the array of Monads at the jth index of vectors.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealD
	 */
	public MonadRealD getVBasis(int pj)
	{
		String tName = vectorList.get(pj);
		int tSpot = FrameAbstract.findName(this, tName);
		return fBasis.get(tSpot);
	}

	/**
	 * Return the element of the array of Monads with the name pName.
	 * 
	 * @param pName
	 *            String Name of the monad to be found and an index returned.
	 * @return MonadRealD
	 */
	public MonadRealD getVBasis(String pName)
	{
		int tSpot = FrameAbstract.findName(this, pName);
		return fBasis.get(tSpot);
	}

	/**
	 * Monad leftside multiplication: (pM, index direction). The Frame resolves
	 * what monad would result if the product was between pM and a monad with a
	 * single blade described by the indexed direction. The indexed monad
	 * happens to be in the fBasis list at that index.
	 * <p>
	 * Multiplication between pM
	 * 
	 * @param pReferenceIndex
	 *            short
	 * @param pM
	 *            MonadRealD
	 * @return MonadRealD
	 */
	protected MonadRealD multiplyLeft(short pReferenceIndex, MonadRealD pM)
	{
		return null;
	}

	@Override
	protected void orthogonalizeOn(MonadAbstract pM)
	{

	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pthisone
	 *            int
	 * @throws CladosFrameException Monad removal failed. Couldn't find it.
	 * 
	 * @return FrameRealD
	 */
	private FrameRealD removeVMonad(int pthisone) throws CladosFrameException
	{
		MonadRealD test = null;
		try
		{
			test = fBasis.remove(pthisone);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new CladosFrameException(this, "Specific Monad removal at ["
							+ pthisone + "] didn't work.");
		}
		finally
		{
			if (test != null) fBasis.trimToSize();
		}

		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pM
	 *            MonadRealD
	 * @throws CladosFrameException Happens when removal fails.
	 * @return FrameRealD
	 */
	public FrameRealD removeVMonad(MonadRealD pM) throws CladosFrameException
	{
		int testfind = findMonad(this, pM);
		if (testfind >= 0)
			removeVMonad(testfind);
		else
			throw new CladosFrameException(this,
							"Can't find the Monad to remove.");
		return this;
	}

	/**
	 * Set the Monad List array of this FrameRealD.
	 * 
	 * @param pML
	 * 			This is the monad array passed to create this frame.
	 */
	protected void setFBasis(ArrayList<MonadRealD> pML)
	{
		if (pML == null)
			fBasis = null;
		else
			fBasis = pML;

	}
}
