/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
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
 * ---com.interworldtransport.cladosG.AlgebraAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;

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
	protected Foot		footPoint;
	protected Basis		gBasis;
	protected GProduct	gProduct;
	protected String	name;
	protected DivField	protoNumber;
	
	/**
	 * This method returns the Algebra's name.
	 * @return String
	 */
	public String getAlgebraName()
	{
		return name;
	}

	/**
	 * This method returns a reference to the Foot of the algebra.
	 * @return Foot
	 */
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
