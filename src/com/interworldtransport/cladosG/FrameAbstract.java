/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.FrameAbstract<br>
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
 * ---com.interworldtransport.cladosG.FrameAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;

import com.interworldtransport.cladosGExceptions.CladosFrameException;

/**
 * The frame object holds all basis details that support the reference frame for
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public abstract class FrameAbstract
{
	/**
	 * Find a 'vector' name in the list of directions in this frame.
	 * 
	 * @param pRF
	 *            FrameAbstract
	 * @param pName
	 *            String
	 * @return int
	 */
	public static int findName(FrameAbstract pRF, String pName)
	{
		for (String pM : pRF.vectorList)
			if (pM.equals(pName)) return pRF.vectorList.indexOf(pM);
		return -1;
	}

	/**
	 * Report on whether a 'vector' name is in the list of directions for this
	 * frame.
	 * 
	 * @param pRF
	 *            FrameAbstract
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static boolean hasName(FrameAbstract pRF, String pName)
	{
		for (String pM : pRF.vectorList)
			if (pM.equals(pName)) return true;
		return false;
	}

	/**
	 * The algebra for a frame holds the default references for a basis
	 * constructed from the generators of the algebra. The default basis and
	 * default product table are kept at this level as they do not depend on the
	 * division field used to represent scale.
	 */
	public AlgebraAbstract		algebra;
	/**
	 * The name of the frame is used in other places as a key. It should tell a
	 * reader what the frame was meant for with a quick glance.
	 */
	public String				name;
	/**
	 * The vectorList holds the names of the monads in a full basis that
	 * represent the 'vector' directions. This is the only list that can
	 * distinguish between a reference frames so-called vectors and the vectors
	 * of the default basis.
	 * <p>
	 * The monads in this list need not be grade=1 according to the default
	 * basis. Grade state of a monad will depend on which subspaces it occupies
	 * relative to the outer products of the vectors in this list.
	 */
	public ArrayList<String>	vectorList;

	/**
	 * Add another name to the list that signifies which monads represent
	 * 'vector' directions in a frame. The method returns false if the vector is
	 * already represented in the list or if the list is full and true if it
	 * wasn't in the list but is now.
	 * <p>
	 * One error condition can occur where a vectorList is already full as far
	 * as the algebra is concerned. This occurs when the vectorList size matches
	 * the gradeCount-1 of the Algebra. Appending is not allowed at this point
	 * and an exception is thrown.
	 * <p>
	 * The boolean response enables a reference frame to rebuild the higher
	 * order blades after this new one is added.
	 * 
	 * @param pM
	 *            MonadAbstract
	 * @return boolean
	 * @throws CladosFrameException
	 */
	protected boolean appendMonad(MonadAbstract pM) throws CladosFrameException
	{
		if (getFrameOrder() >= algebra.getGProduct().getGradeCount() - 1)
			throw new CladosFrameException(this,
							"Frame already full when another append was tried.");
		for (String tS : vectorList)
			if (tS.equals(pM.getName())) return false;
		vectorList.ensureCapacity(vectorList.size() + 1);
		vectorList.add(pM.getName());
		return true;
	}

	public AlgebraAbstract getAlgebra()
	{
		return algebra;
	}

	/**
	 * Return the order of this Frame
	 * 
	 * @return short
	 */
	public short getFrameOrder()
	{
		return (short) vectorList.size();
	}

	public String getName()
	{
		return name;
	}

	protected abstract void orthogonalizeOn(MonadAbstract pM);

	protected void setAlgebra(AlgebraAbstract pAlg)
	{
		algebra = pAlg;
	}

	protected void setName(String pName)
	{
		name = pName;
	}
}
