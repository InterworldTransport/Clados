/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * --------------------------------------------------------------------------
 * <br>---com.interworldtransport.cladosG.FrameRealF---<br>
 * --------------------------------------------------------------------------
 * <p> Interworld Transport grants you ("Licensee") a license to this software
 * under the terms of the GNU General Public License.<br> A full copy of the
 * license can be found bundled with this package or code file. <p> If the
 * license file has become separated from the package, code file, or binary
 * executable, the Licensee is still expected to read about the license at the
 * following URL before accepting this material.
 * <blockquote><code>http://www.opensource
 * .org/gpl-license.html</code></blockquote> <p> Use of this code or executable
 * objects derived from it by the Licensee states their willingness to accept
 * the terms of the license. <p> A prospective Licensee unable to find a copy of
 * the license terms should contact Interworld Transport for a free copy. <p>
 * ----
 * --------------------------------------------------------------------------
 * <br>---com.interworldtransport.cladosG.FrameRealF---<br>
 * --------------------------------------------------------------------------
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;

import com.interworldtransport.cladosFExceptions.FieldBinaryException;
import com.interworldtransport.cladosGExceptions.CladosFrameException;
import com.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;
import com.interworldtransport.cladosGExceptions.CladosNyadException;

/**
 * The frame object holds all basis details that support the reference frame for
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class FrameRealF extends FrameAbstract
{
	/**
	 * Return an integer pointing to the part of the nyad that covers the
	 * algebra named in the parameter. Coverage is true if a monad can be found
	 * in the nyad that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealF
	 * @param pMonad
	 *            MonadRealF
	 * @return int
	 */
	public static int findMonad(FrameRealF pRF, MonadRealF pMonad)
	{
		for (MonadRealF pM : pRF.fBasis)
			if (pM.isGEqual(pMonad)) return pRF.fBasis.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealF
	 * @param pMonad
	 *            MonadRealF
	 * @return boolean
	 */
	public static boolean hasMonad(FrameRealF pRF, MonadRealF pMonad)
	{
		for (MonadRealF pM : pRF.fBasis)
			if (pM.isGEqual(pMonad)) return true;
		return false;
	}

	public static boolean isREqual(FrameRealF pRF1, FrameRealF pRF2)
	{

		// Check to see if the Algebras match
		if (pRF1.getAlgebra() != pRF2.getAlgebra()) return false;

		// Check first to see if the Frames are of the same order. Return false
		// if they are not.
		if (pRF1.getFrameOrder() != pRF2.getFrameOrder()) return false;

		// Now check the monad lists.
		boolean check = false;
		for (MonadRealF tSpot : pRF1.getFBasis())
		{
			check = false;
			String tName1 = tSpot.getName();
			for (MonadRealF tSpot2 : pRF2.getFBasis())
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
	 * <p>
	 * 
	 */
	public ArrayList<MonadRealF>	fBasis;

	/**
	 * Frame constructor with an empty basis list.
	 * 
	 * @param pName
	 * @param pAlg
	 */
	public FrameRealF(String pName, AlgebraRealF pAlg)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealF>(
						algebra.getGBasis().getBladeCount() - 1);
		vectorList = null;
	}

	/**
	 * Frame Constructor with a full basis list.
	 * 
	 * @param pName
	 *            String
	 * @param pAlg
	 *            AlgebraRealF
	 * @param pML
	 *            ArrayList<MonadRealF>
	 */
	public FrameRealF(String pName, AlgebraRealF pAlg, ArrayList<MonadRealF> pML)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealF>(pML);
	}

	/**
	 * Add another Monad to the list of monads in this frame. This method does
	 * not create a new copy of the Monad offered as a parameter. The Frame DOES
	 * wind up referencing the passed Monad.
	 * 
	 * @param pM
	 *            MonadRealF
	 */
	public FrameRealF appendVMonad(MonadRealF pM) throws CladosFrameException
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
		fBasis.add(new MonadRealF(pM));

		return this;
	}

	/**
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadRealF> getFBasis()
	{
		return fBasis;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealF
	 */
	public MonadRealF getFBasis(int pj)
	{
		return fBasis.get(pj);
	}

	/**
	 * Return the element of the array of Monads at the jth index of vectors.
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealF
	 */
	public MonadRealF getVBasis(int pj)
	{
		String tName = vectorList.get(pj);
		int tSpot = FrameAbstract.findName(this, tName);
		return fBasis.get(tSpot);
	}

	/**
	 * Return the element of the array of Monads at the jth index of vectors.
	 * 
	 * @param pj
	 *            String
	 * @return MonadRealF
	 */
	public MonadRealF getVBasis(String pName)
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
	 *            MonadRealF
	 */
	protected MonadRealF multiplyLeft(short pReferenceIndex, MonadRealF pM)
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
	 * @throws CladosFrameException
	 */
	private FrameRealF removeVMonad(int pthisone) throws CladosFrameException
	{
		MonadRealF test = null;
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
	 *            MonadRealF
	 */
	public FrameRealF removeVMonad(MonadRealF pM) throws CladosFrameException
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
	 * Set the Monad List array of this FrameRealF.
	 */
	protected void setFBasis(ArrayList<MonadRealF> pML)
	{
		if (pML == null)
			fBasis = null;
		else
			fBasis = pML;

	}
}
