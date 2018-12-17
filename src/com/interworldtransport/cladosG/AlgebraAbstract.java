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
	/**
	 * All algebra types share some elements that are not dependent on number types.
	 * The first among them is the 'tangent point' of the sub-manifold represented
	 * by the algebra. This is the Foot.
	 */
	protected Foot		foot;
	/**
	 * The second among the common elements is the Eddington basis formed from all 
	 * blades that can be produced through  exterior products of generating 
	 * 'coordinate' vectors. For N generators, there are 2^N blades.
	 */
	protected Basis		gBasis;
	/**
	 * The third among the common elements is the geometric product table formed
	 * by every product possible using members of the Eddington basis. This class
	 * has a few helper methods for dealing with symmetric and antisymmetric products
	 * and detection of other useful conditions.
	 */
	protected GProduct	gProduct;
	/**
	 * Finally, the algebra has a name because this helps distinguish different 
	 * reference frames associated with the same tangent point Foot.
	 */
	protected String	name;
	
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
	public Foot getFoot()
	{
		return foot;
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

	/**
	 * Resetting the algebra name is mildly useful when its purpose
	 * in a model shifts. Otherwise, it will probably not be used.
	 * Once it is set by the constructor, it will probably remain.
	 * 
	 * @param pName
	 */
	public void setAlgebraName(String pName)
	{
		name = pName;
	}

	/**
	 * This method is a little dangerous and should use used only
	 * with great caution. Ideally, it would never be used because 
	 * an algebra is defined relative to a tangent point on a 
	 * sub-manifold. Sometimes, however, one might find that two 
	 * seemingly distinct feet are actually the same. In this limited 
	 * case it should be possible for a model writer to adjust an
	 * algebra to point at a different foot after construction.
	 * 
	 * @param footPoint
	 */
	protected void setFoot(Foot footPoint)
	{
		foot = footPoint;
	}

	/**
	 * This method is a little dangerous and should use used only
	 * with great caution. Ideally, it would never be used because 
	 * an algebra is defined relative to a tangent point and the
	 * coordinates there form both a geometric basis on which a
	 * geometric product is defined. Sometimes, however, one might 
	 * find that two seemingly distinct feet are actually the same. 
	 * In this limited case it should be possible to adjust an
	 * algebra to at a different gProduct after construction.
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
