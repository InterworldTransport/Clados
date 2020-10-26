/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.CladosGAlgebra<br>
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
 * ---com.interworldtransport.cladosG.CladosGAlgebra<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosF.Cardinal;
import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosF.ComplexF;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Algebra construction is essentially the same across all of them so a builder
 * pattern makes sense as an enumeration where each instance relies on shared 
 * methods that 'switch' on their identity to determine what gets built and returned.
 * 
 * This enumeration has non-static methods for each instance, but they don't cause
 * a state change. CladosGAlgebra HAS NO INTERNAL STATE to change. That may change
 * when frames and the physics package are built because Frame tracking will likely 
 * move down from a Foot to an Algebra.
 *	
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGAlgebra 
{	// All of these have implicit private constructors
	COMPLEXD,
	COMPLEXF,
	REALD,
	REALF;

	public static final boolean isClassMatch(AlgebraAbstract pA1, AlgebraAbstract pA2)
	{
		return pA1.getClass().equals(pA2.getClass());
	}
	
	public final AlgebraAbstract copyOf(AlgebraAbstract pA, String pName)
	{	//Algebra Constructor #1 covered with this
		switch (this)
		{
			case REALF: 	if(pA instanceof AlgebraRealF) 		return new AlgebraRealF(pName, (AlgebraRealF) pA);
							else return null;
			case REALD: 	if(pA instanceof AlgebraRealD)		return new AlgebraRealD(pName, (AlgebraRealD) pA);
							else return null;
			case COMPLEXF: 	if(pA instanceof AlgebraComplexF)	return new AlgebraComplexF(pName, (AlgebraComplexF) pA);
							else return null;
			case COMPLEXD: 	if(pA instanceof AlgebraComplexD)	return new AlgebraComplexD(pName, (AlgebraComplexD) pA);
							else return null;
			default:		return null;
		}
	}
	
	public final AlgebraAbstract create( DivField pNumber, String pName, String pAName, String pSig) 
			throws BadSignatureException, GeneratorRangeException
	{	//Algebra Constructor #5 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber instanceof RealF) 	return new AlgebraRealF(pName, pAName, pSig, (RealF) pNumber);
							else return null;
			case REALD: 	if(pNumber instanceof RealD) 	return new AlgebraRealD(pName, pAName, pSig, (RealD) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber instanceof ComplexF) return new AlgebraComplexF(pName, pAName, pSig, (ComplexF) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber instanceof ComplexD) return new AlgebraComplexD(pName, pAName, pSig, (ComplexD) pNumber);
							else return null;
			default:		return null;
		}
	}
	
	public final AlgebraAbstract createWithFoot(Foot pF, Cardinal pCard, String pName, String pSig)
			throws BadSignatureException, GeneratorRangeException
	{	//Algebra Constructor #3 covered with this
		switch (this)
		{
			case REALF: 	return new AlgebraRealF(pName, pF, pCard, pSig);
			case REALD: 	return new AlgebraRealD(pName, pF, pCard, pSig);
			case COMPLEXF: 	return new AlgebraComplexF(pName, pF, pCard, pSig);
			case COMPLEXD: 	return new AlgebraComplexD(pName, pF, pCard, pSig);
			default:		return null;
		}
	}
	
	public final AlgebraAbstract createWithFoot(Foot pF, DivField pNumber, String pName, String pSig)
			throws BadSignatureException, GeneratorRangeException
	{	//Algebra Constructor #4 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber instanceof RealF) 	return new AlgebraRealF(pName, pF, pSig, (RealF) pNumber);
							else return null;
			case REALD: 	if(pNumber instanceof RealD) 	return new AlgebraRealD(pName, pF, pSig, (RealD) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber instanceof ComplexF) return new AlgebraComplexF(pName, pF, pSig, (ComplexF) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber instanceof ComplexD) return new AlgebraComplexD(pName, pF, pSig, (ComplexD) pNumber);
							else return null;
			default:		return null;
		}
	}
	
	public final AlgebraAbstract createWithFootPlus(Foot pF, Cardinal pCard, GProduct pGP, String pName)
	{	//Algebra Constructor #2 covered with this
		switch (this)
		{
			case REALF: 	return new AlgebraRealF(pName, pF, pCard, pGP);
			case REALD: 	return new AlgebraRealD(pName, pF, pCard, pGP);
			case COMPLEXF: 	return new AlgebraComplexF(pName, pF, pCard, pGP);
			case COMPLEXD: 	return new AlgebraComplexD(pName, pF, pCard, pGP);
			default:		return null;
		}
	}
}