/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.CladosGMonad<br>
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
 * ---com.interworldtransport.cladosG.CladosGMonad<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosF.ComplexF;
import com.interworldtransport.cladosF.RealD;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Monad construction is essentially the same across all of them so a builder
 * pattern makes sense as an enumeration where each instance relies on shared 
 * methods that 'switch' on their identity to determine what gets built and returned.
 * 
 * This enumeration has non-static methods for each instance, but they don't cause
 * a state change. CladosGMonad HAS NO INTERNAL STATE to change at this time.
 *	
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGMonad 
{	// All of these have implicit private constructors
	COMPLEXD,
	COMPLEXF,
	REALD,
	REALF;

	public static final boolean isClassMatch(MonadAbstract pM1, MonadAbstract pM2)
	{
		return pM1.getClass().equals(pM2.getClass());
	}
	
	public final MonadAbstract copyOf(MonadAbstract pA) throws BadSignatureException, CladosMonadException
	{	//Monad Constructor #1 covered with this
		switch (this)
		{
			case REALF: 	if(pA instanceof MonadRealF) 	return new MonadRealF((MonadRealF) pA);
							else return null;
			case REALD: 	if(pA instanceof MonadRealD) 	return new MonadRealD((MonadRealD) pA);
							else return null;
			case COMPLEXF: 	if(pA instanceof MonadComplexF) return new MonadComplexF((MonadComplexF) pA);
							else return null;
			case COMPLEXD: 	if(pA instanceof MonadComplexD) return new MonadComplexD((MonadComplexD) pA);
							else return null;
			default:		return null;
		}
	}	
	
	public final MonadAbstract copyRename(MonadAbstract pA, String pName) throws BadSignatureException, CladosMonadException
	{	//Monad Constructor #2 covered with this
		switch (this)
		{
			case REALF: 	if(pA instanceof MonadRealF) 	return new MonadRealF(pName, (MonadRealF) pA);
							else return null;
			case REALD: 	if(pA instanceof MonadRealD) 	return new MonadRealD(pName, (MonadRealD) pA);
							else return null;
			case COMPLEXF: 	if(pA instanceof MonadComplexF) return new MonadComplexF(pName, (MonadComplexF) pA);
							else return null;
			case COMPLEXD: 	if(pA instanceof MonadComplexD) return new MonadComplexD(pName, (MonadComplexD) pA);
							else return null;
			default:		return null;
		}
	}
	
	public final MonadAbstract createOnlyCoeffs( DivField[] pNumber, String pName, String pAName, String pFrame, String pFoot, String pSig) 
			throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{//Monad Constructor #6 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber[0] instanceof RealF) 	return new MonadRealF(pName, pAName, pFrame, pFoot, pSig, (RealF[]) pNumber);
							else return null;
			case REALD: 	if(pNumber[0] instanceof RealD) 	return new MonadRealD(pName, pAName, pFrame, pFoot, pSig, (RealD[]) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber[0] instanceof ComplexF) 	return new MonadComplexF(pName, pAName, pFrame, pFoot, pSig, (ComplexF[]) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber[0] instanceof ComplexD) 	return new MonadComplexD(pName, pAName, pFrame, pFoot, pSig, (ComplexD[]) pNumber);
							else return null;
			default:		return null;
		}
	}
	
	public final MonadAbstract createSpecial( DivField pNumber, String pName, String pAName, String pFrame, String pFoot, String pSig, String pSpecial) 
			throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{//Monad Constructor #5 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber instanceof RealF) 	return new MonadRealF(pName, pAName, pFrame, pFoot, pSig, (RealF) pNumber, pSpecial);
							else return null;
			case REALD: 	if(pNumber instanceof RealD) 	return new MonadRealD(pName, pAName, pFrame, pFoot, pSig, (RealD) pNumber, pSpecial);
							else return null;
			case COMPLEXF: 	if(pNumber instanceof ComplexF) return new MonadComplexF(pName, pAName, pFrame, pFoot, pSig, (ComplexF) pNumber, pSpecial);
							else return null;
			case COMPLEXD: 	if(pNumber instanceof ComplexD) return new MonadComplexD(pName, pAName, pFrame, pFoot, pSig, (ComplexD) pNumber, pSpecial);
							else return null;
			default:		return null;
		}
	}
	
	public final MonadAbstract createWithAlgebra( DivField[] pNumber, AlgebraAbstract pA, String pName,  String pFrame) 
			throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{//Monad Constructor #7 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber[0] instanceof RealF 		& pA instanceof AlgebraRealF) 	
								return new MonadRealF(pName, (AlgebraRealF) pA, pFrame, (RealF[]) pNumber);
							else return null;
			case REALD: 	if(pNumber[0] instanceof RealD 		& pA instanceof AlgebraRealD)
								return new MonadRealD(pName, (AlgebraRealD) pA, pFrame, (RealD[]) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber[0] instanceof ComplexF 	& pA instanceof AlgebraComplexF) 	
								return new MonadComplexF(pName, (AlgebraComplexF) pA, pFrame, (ComplexF[]) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber[0] instanceof ComplexD 	& pA instanceof AlgebraComplexD) 	
								return new MonadComplexD(pName, (AlgebraComplexD) pA, pFrame, (ComplexD[]) pNumber);
							else return null;
			default:		return null;
		}
	}
	
	public final MonadAbstract createWithFoot( DivField pNumber, String pName, String pAName, String pFrame, String pFoot, String pSig) 
			throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{//Monad Constructor #3 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber instanceof RealF) 	return new MonadRealF(pName, pAName, pFrame, pFoot, pSig, (RealF) pNumber);
							else return null;
			case REALD: 	if(pNumber instanceof RealD) 	return new MonadRealD(pName, pAName, pFrame, pFoot, pSig, (RealD) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber instanceof ComplexF) return new MonadComplexF(pName, pAName, pFrame, pFoot, pSig, (ComplexF) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber instanceof ComplexD) return new MonadComplexD(pName, pAName, pFrame, pFoot, pSig, (ComplexD) pNumber);
							else return null;
			default:		return null;
		}
	}
	
	public final MonadAbstract createZero( DivField pNumber, Foot pFt, String pName, String pAName, String pFrame, String pSig) 
			throws BadSignatureException, CladosMonadException, GeneratorRangeException
	{//Monad Constructor #4 covered with this
		switch (this)
		{
			case REALF: 	if(pNumber instanceof RealF) 	return new MonadRealF(pName, pAName, pFrame, pFt, pSig, (RealF) pNumber);
							else return null;
			case REALD: 	if(pNumber instanceof RealD) 	return new MonadRealD(pName, pAName, pFrame, pFt, pSig, (RealD) pNumber);
							else return null;
			case COMPLEXF: 	if(pNumber instanceof ComplexF) return new MonadComplexF(pName, pAName, pFrame, pFt, pSig, (ComplexF) pNumber);
							else return null;
			case COMPLEXD: 	if(pNumber instanceof ComplexD) return new MonadComplexD(pName, pAName, pFrame, pFt, pSig, (ComplexD) pNumber);
							else return null;
			default:		return null;
		}
	}
}

