/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.Foot<br>
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
 * ---com.interworldtransport.cladosG.Foot<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.ListIterator;

import com.interworldtransport.cladosF.*;

/**
 * Objects within the cladosG package have a number of attributes in common that
 * relate to a point that describes where the geometry of the algebra is tangent
 * to the manifold underneath it. In a flat space it is not really necessary to
 * use a foot point, but a simple one is still used in the cladosG package to
 * help avoid the trap of assuming transport of objects along the manifold
 * doesn't cause them to re-orient and change into other objects.
 * 
 * At a minimum, a footPoint is the name of an event or a point and the type of
 * numbers used for scale along the coordinate paths. At this location it is
 * assumed the geometry of the algebra is aligned with the local geometry one
 * would assign to the coordinate space that is the manifold at that point. This
 * makes a footPoint the root reference frame.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class Foot
{
	/**
	 * This String is the name the footPoint of the Reference Frame of the Monad
	 */
	private String				footName;
	/**
	 * This object defines the type of numbers used by all objects that share
	 * this footPoint.
	 */
	private DivFieldType		numType;
	/**
	 * This is the list of known reference frames defined elsewhere against this
	 * footPoint.
	 */
	private ArrayList<String>	rFrames;

	/**
	 * Build the footPoint object from scratch.
	 * 
	 * @param pName
	 *            String This string will be the name of the foot point.
	 * @param pFT
	 *            DivFieldType This object defines the kind of numbers that are
	 *            meaningful for this foot point
	 */
	public Foot(String pName, DivFieldType pFT)
	{
		setFootName(pName);
		setNumberType(pFT);
		rFrames = new ArrayList<String>(1);
		rFrames.add(pName);
	}

	/**
	 * This method appends a frame name from the list of known frames for this
	 * foot.
	 * 
	 * @param pRF
	 *            String Reference Frame name to remove
	 */
	protected void appendIfUniqueRFrame(String pRF)
	{
		for (String tS : rFrames)
			if (tS.equals(pRF)) return;
		rFrames.ensureCapacity(rFrames.size() + 1);
		rFrames.add(pRF);
	}

	/**
	 * This method appends a frame name from the list of known frames for this
	 * foot.
	 * 
	 * @param pRF
	 *            String Reference Frame name to remove
	 */
	protected void appendRFrame(String pRF)
	{
		rFrames.ensureCapacity(rFrames.size() + 1);
		rFrames.add(pRF);
	}

	public String getFootName()
	{
		return footName;
	}

	protected DivFieldType getNumberType()
	{
		return numType;
	}

	protected ArrayList<String> getReferenceFrames()
	{
		return rFrames;
	}
	
	/**
	 * This method removes a frame name from the list of known frames for this
	 * foot. If the frame is not found in the list, no action is taken. This
	 * silent failure is intentional.
	 * 
	 * @param pRF
	 *            String Reference Frame name to remove
	 */
	protected void removeRFrames(String pRF)
	{
		ListIterator<String> li = rFrames.listIterator();
		do
		{
			if (li.next().equals(pRF))
			{
				li.remove();
				break;
			}
		}
		while (li.hasNext());
	}

	public void setFootName(String footName)
	{
		this.footName = footName;
	}

	protected void setNumberType(DivFieldType fN)
	{
		this.numType = fN;
	}

	public String toXMLString()
	{
		StringBuffer rB = new StringBuffer("<Foot name=\"" + getFootName()
						+ "\" type=\"" + numType.getType() + "\">\n");
		rB.append("\t<ReferenceFrames number=\"" + rFrames.size()
						+ "\" >\n");
		for (short k = 0; k < rFrames.size(); k++)
		{
			rB.append("\t\t<Frame number=\"" + k + "\" name=\""
							+ rFrames.get(k) + "\" />\n");
		}
		rB.append("\t</ReferenceFrames>\n");
		rB.append("</Foot>\n");
		return rB.toString();
	}

}
