/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.CladosGBuilder<br>
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
 * ---com.interworldtransport.cladosF.CladosGBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.Cardinal;
import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * This builder gets basic information and constructs many Clados Geometry objects.
 * 
 * This builder is an enforced singleton, but it HAS NO INTERNAL STATE to change.
 * That may change when frames and the physics package are built.
 *	
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGBuilder 
{	// This has an implicit private constructor we won't override.
	INSTANCE;
	public final static Foot copyLike(String pName, Cardinal pCard)
	{
		return Foot.buildAsType(pName, pCard);
	}
	public final static Foot copyLike(String pName, DivField pDiv)
	{
		return Foot.buildAsType(pName, pDiv.getCardinal());
	}
	public final static Foot copyLike(String pName, Foot pF, int pSpot)
	{
		return Foot.buildAsType(pName, pF.getCardinal(pSpot));
	}
	public final static Foot copyOf(Foot pF, int pSpot)
	{
		return Foot.buildAsType(pF.getFootName(), pF.getCardinal(pSpot));
	}
	public final static Foot create(String pName, String pCardName)
	{
		return Foot.buildAsType(pName, Cardinal.generate(pCardName));
	}
	public final static Basis create(short pGens) throws GeneratorRangeException
	{
		return Basis.using(pGens);
	}
	public final static Basis copyOf(Basis pB)
	{
		return Basis.copyOf(pB);
	}
	//public final static GProduct create(String pSig)
	//public final static GProduct copyBasis(GProduct pGP)
	//public final static boolean validateSignature(String pSig)
	//{
	//	This one tests both the length AND characters.
	//}
	//public final static String cleanSignature(String pSig)
	//{
	//	This one removes illegal characters.
	//}
	
	
	
}
