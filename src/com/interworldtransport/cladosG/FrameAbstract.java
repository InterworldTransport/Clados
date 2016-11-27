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

/**
 * The frame object holds all basis details that support the reference frame for
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
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
		for (String pM : pRF.nameList)
			if (pM.equals(pName)) return pRF.nameList.indexOf(pM);
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
		for (String pM : pRF.nameList)
			if (pM.equals(pName)) return true;
		return false;
	}

	/**
	 * The algebra for a frame holds the default references for a basis
	 * constructed from the generators of the algebra. The default basis and
	 * default product table are kept at this level as they do not depend on the
	 * division field used to represent scale.
	 */
	protected AlgebraAbstract	algebra;
	
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
	 * the reference monads, so there is no danger of a loop occurring. The frame
	 * must be able to resolve operations without calling on the operations of
	 * the reference monads.
	 * 
	 */
	protected ArrayList<?>		fBasis;
	/**
	 * The name of the frame is used in other places as a key. It should tell a
	 * reader what the frame was meant for with a quick glance.
	 */
	protected String			name;
	
	/**
	 * The nameList holds the names of the monads in a full basis that
	 * represent the 'vector' directions. This is the only list that can
	 * distinguish between a reference frame's so-called vectors and the vectors
	 * of the default basis.
	 * <p>
	 * The monads in this list need not be grade=1 according to the default
	 * basis. Grade state of a monad will depend on which subspaces it occupies
	 * relative to the outer products of the vectors in this list.
	 */
	protected ArrayList<String>	nameList;
	
	/**
	 * The reciprocal frame can be referenced from here if it is known. There is
	 * no plan to construct one automatically from this frame.
	 */
	protected FrameAbstract		reciprocal;

	public AlgebraAbstract getAlgebra()
	{
		return algebra;
	}

	protected abstract ArrayList<?> getFBasis();

	/**
	 * Return the order of this Frame
	 * 
	 * @return short
	 */
	public short getFrameOrder()
	{
		return (short) nameList.size();
	}

	public String getName()
	{
		return name;
	}

	public FrameAbstract getReciprocal() {
		return reciprocal;
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

	protected void setReciprocal(FrameAbstract reciprocal) {
		this.reciprocal = reciprocal;
	}
}
