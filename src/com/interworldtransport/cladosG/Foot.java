/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.Foot<br>
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
 * ---com.interworldtransport.cladosG.Foot<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import java.util.ArrayList;
//import java.util.ListIterator;

import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosF.DivFieldType;

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
	 * This factory build method produces a new Foot using the offered number type.
	 * It is intend to name an otherwise opaque constructor that creates a new Foot
	 * but re-uses a DivFieldType.
	 * 
	 * @param pFootName String name for the new Foot
	 * @param pAsNumberType DivFieldType to re-use for this new Foot
	 * @return Foot Factory method builds a new Foot re-using a DivFieldType object.
	 */
	public static final Foot buildAsType(String pFootName, DivFieldType pAsNumberType)
	{
		return new Foot(pFootName, pAsNumberType);
	}
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
	 * Build the footPoint object from scratch.
	 * 
	 * @param pName
	 *            String This string will be the name of the foot point.
	 * @param pF
	 *            DivFieldType This object defines the kind of numbers that are
	 *            meaningful for this foot point
	 */
	public Foot(String pName, DivField pF)
	{
		setFootName(pName);
		setNumberType(pF.getFieldType());
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
	public void appendIfUniqueRFrame(String pRF)
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

	public DivFieldType getNumberType()
	{
		return numType;
	}

	public ArrayList<String> getReferenceFrames()
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
	public void removeRFrames(String pRF)
	{
		//for (String tS : rFrames)
		//{
		//	if (tS.equals(pRF))
				rFrames.remove(pRF);
		//}
		//ListIterator<String> li = rFrames.listIterator();
		//do
		//{
		//	if (li.next().equals(pRF))
		//	{
		//		li.remove();
		//		break;
		//	}
		//}
		//while (li.hasNext());
	}

	public void setFootName(String footName)
	{
		this.footName = footName;
	}

	public void setNumberType(DivFieldType fN)
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
