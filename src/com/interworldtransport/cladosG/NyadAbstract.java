/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.NyadAbstract<br>
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
 * ---com.interworldtransport.cladosG.NyadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;

/**
 * Many math objects within the cladosG package have a number of attributes in
 * common. They are named objects from named algebras and with named feet. The
 * abstracted nyad covers those common elements and methods shared by objects in
 * potentially more than one algebra.
 * <p>
 * (Single monad nyads are essentially monads, but can be expanded.)
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class NyadAbstract // implements Iterable<MonadAbstract>
{

	/**
	 * Return an integer pointing to the part of the nyad expressed in the frame
	 * named in the parameter.
	 * 
	 * @param pN
	 * 			NyadAbstract
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static int findFrame(NyadAbstract pN, String pFrame)
	{
		for (MonadAbstract pM : pN.monadList)
			if (pFrame.equals(pM.getFrameName()))
				return pN.monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return an integer pointing to the part of the nyad with the expressed
	 * name.
	 * 
	 * @param pN
	 * 			NyadAbstract
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static int findName(NyadAbstract pN, String pName)
	{
		for (MonadAbstract pM : pN.monadList)
			if (pName.equals(pM.getName())) return pN.monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad is expressed in the
	 * frame named in the parameter.
	 * 
	 * @param pN
	 * 			NyadAbstract
	 * @param pFrame
	 *            String
	 * @return boolean
	 */
	public static boolean hasFrame(NyadAbstract pN, String pFrame)
	{
		for (MonadAbstract pM : pN.monadList)
			if (pFrame.equals(pM.getFrameName())) return true;
		return false;
	}

	/**
	 * Return a boolean stating whether or not the nyad contained the named
	 * monad.
	 * 
	 * @param pN
	 * 			NyadAbstract
	 * @param pName
	 *            String
	 * @return boolean
	 */
	public static boolean hasName(NyadAbstract pN, String pName)
	{
		for (MonadAbstract pM : pN.monadList)
			if (pName.equals(pM.getName())) return true;
		return false;
	}

	/**
	 * This array is the list of names for the Monads that make up the
	 * NyadRealD.
	 */
	// protected String[] algebraKey;
	/**
	 * This String is the name the footPoint of the Reference Frame of the Monad
	 */
	protected Foot						footPoint;
	/**
	 * This array is the list of frame names for the Monads that make up the
	 * NyadRealD.
	 */
	// protected String[] frameKey;
	/**
	 * This element holds holds the field's multiplicative unity. It gets used
	 * when constructing other parts of the NyadRealD to ensure field type
	 * safety.
	 */
	// protected DivField protoOne;
	/**
	 * This array is the list of Monads that makes up the NyadRealD. It will be
	 * tied to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<MonadAbstract>	monadList;
	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String					Name;

	/**
	 * This array is the list of names for the Monads that make up the
	 * NyadRealD.
	 */
	// protected String[] nameKey;
	/**
	 * This integer keeps track of the number of Monads in this NyadRealD
	 */
	// protected short nyadOrder;

	/**
	 * Return the array of algebra names
	 * 
	 * @return String[]
	 */
	// protected String[] getAlgebraKey()
	// {
	// return algebraKey;
	// }

	public Foot getFootPoint()
	{
		return footPoint;
	}

	/**
	 * Return the array of Monad frame names
	 * 
	 * @return String[]
	 */
	// protected String[] getFrameKey()
	// {
	// return frameKey;
	// }

	public String getName()
	{
		return Name;
	}

	/**
	 * Return the array of Monad names
	 * 
	 * @return String[]
	 */
	// protected String[] getNameKey()
	// {
	// return nameKey;
	// }

	/**
	 * Return the order of this Nyad
	 * 
	 * @return short
	 */
	public short getNyadOrder()
	{
		return (short) monadList.size();
	}

	/**
	 * Set the algebra array of this NyadRealD. The Monad List must be set
	 * first.
	 */
	// protected void setAlgebraKey()
	// {
	// if (monadList == null | monadList.isEmpty())
	// algebraKey = null;
	// else
	// {
	// algebraKey = new String[monadList.size()];
	// int j = 0;
	// for (MonadAbstract tSpot : monadList)
	// {
	// if (tSpot != null)
	// algebraKey[j] = tSpot.getAlgebraName();
	// else
	// algebraKey[j] = "null";
	// j++;
	// }
	// }
	// }

	// protected void setAllKeys()
	// {
	// //setAlgebraKey();
	// //setFrameKey();
	// //setNameKey();
	// }

	/**
	 * 
	 * @param pF
	 * 			This parameter sets the foot of the nyad.
	 */
	protected void setFootPoint(Foot pF)
	{
		footPoint = pF;
	}

	/**
	 * Set the Frame array of this NyadRealD. The Monad List must be set first.
	 */
	// protected void setFrameKey()
	// {
	// if (monadList == null | monadList.isEmpty())
	// frameKey = null;
	// else
	// {
	// frameKey = new String[monadList.size()];
	// int j = 0;
	// for (MonadAbstract tSpot : monadList)
	// {
	// if (tSpot != null)
	// frameKey[j] = tSpot.getFrameName();
	// else
	// frameKey[j] = "null";
	// j++;
	// }
	// }
	// }

	/**
	 * Set the name of this NyadRealD
	 * 
	 * @param name
	 *            String
	 */
	protected void setName(String name)
	{
		Name = name;
	}

	/**
	 * Set the name array of this NyadRealD. The Monad List must be set first.
	 */
	// protected void setNameKey()
	// {
	// if (monadList == null | monadList.isEmpty())
	// nameKey = null;
	// else
	// {
	// nameKey = new String[monadList.size()];
	// int j = 0;
	// for (MonadAbstract tSpot : monadList)
	// {
	// if (tSpot != null)
	// nameKey[j] = tSpot.getName();
	// else
	// nameKey[j] = "null";
	// j++;
	// }
	// }
	// }

	/**
	 * Set the order of this NyadRealD
	 */
	// protected void setNyadOrder()
	// {
	// nyadOrder = (short) monadList.size();
	// }
}
