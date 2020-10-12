/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.DivField<br>
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
 * ---com.interworldtransport.cladosF.DivField<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

/**
 * This class supports the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivField descendants as a result.
 * <p>
 * DivField's capture only a reference frame type, so they appear to be named.
 * However, they are named with Cardinal objects so their names can be shared
 * as references, thus survive reference frame match tests.
 * <p>
 * The number to be plugged in, though, doesn't appear until later in a child
 * of this class. This matters because the number of reals involved in the field
 * varies. Complex numbers require two. Quaternions would require four.
 * <p>
 * The only reason this class isn't an interface is the presence of the 
 * Cardinal data member.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public abstract class DivField
{
	public static final String COMPLEXD="ComplexD";
	public static final String COMPLEXF="ComplexF";
	public static final String REALD="RealD";
	public static final String REALF="RealF";
	/**
	 * Check to see if the two argument are of the same cardinal.
	 * 
	 * @param pE
	 *            DivField
	 * @param pF
	 *            DivField
	 * @return boolean
	 */
	public static final boolean isTypeMatch(DivField pE, DivField pF)
	{
		if(pE._card==null && pF._card==null)
			return true;
		return pE._card == pF._card;
	}

	/**
	 * Object for the cardinal. A string used to be used here, but an object
	 * lets us reuse the object through a reference allowing all coefficients in
	 * the monads of a nyad to point to the same place.
	 */
	protected Cardinal	_card;

	/**
	 * Get method for _card
	 * 
	 * @return Cardinal (A cardinal name for a DivField)
	 */
	public Cardinal getCardinal()
	{
		return _card;
	}

	/**
	 * Get method for _card
	 * 
	 * @return Cardinal (A cardinal name for a DivField)
	 */
	public String getCardinalString()
	{
		return _card.getType();
	}

	/**
	 * Set method for _card
	 * 
	 * @param pType
	 *            Cardinal
	 */
	protected void setCardinal(Cardinal pType)
	{
		_card = pType;
	}



}
