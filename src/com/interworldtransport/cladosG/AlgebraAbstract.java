/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
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
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x DivField} except the actual 
 * field.  That makes this an abstraction of an algebra.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class AlgebraAbstract
{
	public Foot		footPoint;
	public Basis	gBasis;
	public GProduct	gProduct;
	public String	name;
	
	public String getAlgebraName()
	{
		return name;
	}

	public Foot getFootPoint()
	{
		return footPoint;
	}

	/**
	 * Return the entire basis definition object.
	 * 
	 * @return gBasis
	 */
	public Basis getGBasis()
	{
		return gBasis;
	}

	/**
	 * Return the entire product definition object.
	 * 
	 * @return gProduct
	 */
	public GProduct getGProduct()
	{
		return gProduct;
	}

	protected void setAlgebraName(String pName)
	{
		name = pName;
	}

	protected void setFootPoint(Foot footPoint)
	{
		this.footPoint = footPoint;
	}

	/**
	 * Set the entire product definition object.
	 * 
	 * @param pGP
	 *            GProduct
	 */
	protected void setGProduct(GProduct pGP)
	{
		gProduct = pGP;
		gBasis = pGP.getBasis();
	}
}
