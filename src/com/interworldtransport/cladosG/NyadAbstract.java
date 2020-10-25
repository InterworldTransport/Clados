/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.NyadAbstract<br>
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
 * ---com.interworldtransport.cladosG.NyadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

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
public abstract class NyadAbstract
{
	/**
	 * If the monads listed within a nyad are all of the same algebra,
	 * the strongFlag should be set to false AND the oneAlgebra flag should
	 * be set to True. This method returns that the oneAlgebra flag.
	 * <p>
	 * In the future, the Frame classes will override this as it is likely
	 * that other tests are required to ensure a monad list is actually a
	 * reference frame. At the NyadAbstract leve, therefore, it is best to
	 * to think of a true response to this method as suggesting the nyad 
	 * is a frame candidate.
	 * <p>
	 * @param pN	NyadAbstract to be tested 
	 * @return boolean
	 * 	True if nyad's monads are all of the same algebra
	 */
	public static final boolean isFrame(NyadAbstract pN)
	{
		if (pN._strongFlag) return !pN._strongFlag;
		return pN._oneAlgebra;
	}
	/**
	 * If the monads listed within a nyad are all of a different algebra,
	 * the strongFlag should be set to true. This method returns that flag.
	 * <p>
	 * @param pN	NyadAbstract to be tested 
	 * @return boolean
	 * 	True if nyad is strong meaning each Monad is of a different algebra
	 * 	False if nyad's monads double up on any particular algebra
	 */
	public static final boolean	isStrong(NyadAbstract pN)
	{
		return pN._strongFlag;
	}
	/**
	 * If the monads listed within a nyad are all of a different algebra,
	 * the strongFlag should be set to true. This method returns that the
	 * inverse of that flag.
	 * <p>
	 * @param pN	NyadAbstract to be tested 
	 * @return boolean
	 * 	False if nyad is strong meaning each Monad is of a different algebra
	 * 	True if nyad's monads double up on any particular algebra
	 */
	public static final boolean	isWeak(NyadAbstract pN)
	{
		return !pN._strongFlag;
	}
	
	protected boolean					_oneAlgebra;

	protected boolean					_strongFlag;
	
	/**
	 * This String is the name the footPoint of the Reference Frame of the Monad
	 */
	protected Foot						footPoint;
	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String					Name;

	public Foot getFootPoint()
	{
		return footPoint;
	}

	public String getName()
	{
		return Name;
	}

	/**
	 * Set the name of this NyadRealD
	 * <p>
	 * @param name
	 *            String
	 */
	public void setName(String name)
	{
		Name = name;
	}

	/**
	 * Set the Foot for the nyad using this method. It is called a  FootPoint 
	 * because that was the old name, but one should not confuse the use of 
	 * 'point' here with any geometric meaning. A Foot merely labels where 
	 * an algebra is expected to be tangent to an underlying manifold.
	 * <p>
	 * @param pF	Foot to set for the nyad.
	 */
	protected void setFootPoint(Foot pF)
	{
		footPoint = pF;
	}
}
