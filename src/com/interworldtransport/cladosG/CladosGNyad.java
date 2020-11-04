/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.CladosGNyad<br>
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
 * ---com.interworldtransport.cladosG.CladosGNyad<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;
import com.interworldtransport.cladosGExceptions.CladosNyadException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Nyad construction is essentially the same across all of them so a builder
 * pattern makes sense as an enumeration where each instance relies on shared
 * methods that 'switch' on their identity to determine what gets built and
 * returned.
 * <p>
 * This enumeration has non-static methods for each instance, but they don't
 * cause a state change. CladosGMonad HAS NO INTERNAL STATE to change at this
 * time.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGNyad { // All of these have implicit private constructors
	COMPLEXD, COMPLEXF, REALD, REALF;

	public static final boolean isClassMatch(NyadAbstract pN1, NyadAbstract pN2) {
		return pN1.getClass().equals(pN2.getClass());
	}

	/**
	 * Nyad Constructor #1 covered with this method
	 * 
	 * @param pN The nyad to be copied. USE A CONCRETE Nyad here or nada
	 * @return NyadAbstract (Cast this as the concrete nyad to be used)
	 * @throws BadSignatureException Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final NyadAbstract copyOf(NyadAbstract pN)
			throws BadSignatureException, CladosMonadException, CladosNyadException {
		switch (this) {
		case REALF:
			if (pN instanceof NyadRealF)
				return new NyadRealF((NyadRealF) pN);
			else
				return null;
		case REALD:
			if (pN instanceof NyadRealD)
				return new NyadRealD((NyadRealD) pN);
			else
				return null;
		case COMPLEXF:
			if (pN instanceof NyadComplexF)
				return new NyadComplexF((NyadComplexF) pN);
			else
				return null;
		case COMPLEXD:
			if (pN instanceof NyadComplexD)
				return new NyadComplexD((NyadComplexD) pN);
			else
				return null;
		default:
			return null;
		}
	}

	/**
	 * Nyad Constructor #3 covered with this method
	 * 
	 * @param pN    The nyad to copy causing all listed monads TO BE CONSTRUCTED.
	 *              USE A CONCRETE Nyad here or nada.
	 * @param pName A String for the new Nyad's name.
	 * @return NyadAbstract (Cast this as the concrete nyad to be used)
	 * @throws BadSignatureException Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final NyadAbstract copyRename(NyadAbstract pN, String pName)
			throws BadSignatureException, CladosMonadException, CladosNyadException {
		switch (this) {
		case REALF:
			if (pN instanceof NyadRealF)
				return new NyadRealF(pName, (NyadRealF) pN, true);
			else
				return null;
		case REALD:
			if (pN instanceof NyadRealD)
				return new NyadRealD(pName, (NyadRealD) pN, true);
			else
				return null;
		case COMPLEXF:
			if (pN instanceof NyadComplexF)
				return new NyadComplexF(pName, (NyadComplexF) pN, true);
			else
				return null;
		case COMPLEXD:
			if (pN instanceof NyadComplexD)
				return new NyadComplexD(pName, (NyadComplexD) pN, true);
			else
				return null;
		default:
			return null;
		}
	}

	/**
	 * Nyad Constructor #2 covered with this method
	 * 
	 * @param pM    The monad to be COPIED as the first in the list in a new nyad.
	 *              USE A CONCRETE Monad here or nada.
	 * @param pName A String for the new Nyad's name.
	 * @return NyadAbstract (Cast this as the concrete nyad to be used)
	 * @throws BadSignatureException Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final NyadAbstract copyWithMonad(MonadAbstract pM, String pName)
			throws BadSignatureException, CladosMonadException, CladosNyadException {
		switch (this) {
		case REALF:
			if (pM instanceof MonadRealF)
				return new NyadRealF(pName, (MonadRealF) pM, true);
			else
				return null;
		case REALD:
			if (pM instanceof MonadRealD)
				return new NyadRealD(pName, (MonadRealD) pM, true);
			else
				return null;
		case COMPLEXF:
			if (pM instanceof MonadComplexF)
				return new NyadComplexF(pName, (MonadComplexF) pM, true);
			else
				return null;
		case COMPLEXD:
			if (pM instanceof MonadComplexD)
				return new NyadComplexD(pName, (MonadComplexD) pM, true);
			else
				return null;
		default:
			return null;
		}
	}
}
