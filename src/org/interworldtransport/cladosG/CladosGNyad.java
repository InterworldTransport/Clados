/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CladosGNyad<br>
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
 * ---org.interworldtransport.cladosG.CladosGNyad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Nyad construction is essentially the same across all of them so a builder
 * pattern makes sense as an enumeration where each instance relies on shared
 * methods that 'switch' on their identity to determine what gets built and
 * returned.
 * <p>
 * This enumeration has non-static methods for each instance, but they don't
 * cause a state change. CladosGNyad HAS NO INTERNAL STATE to change at this
 * time.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGNyad { // All of these have implicit private constructors
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	INSTANCE;

	/**
	 * This is effectively an 'instanceof' operator that avoids naming the actual
	 * class type. It compares to nyads to see if they are of the same class.
	 * 
	 * @param pN1 First nyad to test
	 * @param pN2 Second nyad to test
	 * @return TRUE if nyads are of the same type. FALSE otherwise.
	 */
	public static final boolean isClassMatch(NyadAbstract pN1, NyadAbstract pN2) {
		return pN1.getClass().equals(pN2.getClass());
	}

	/**
	 * Nyad Constructor #3 covered with this method, but with re-use. This causes
	 * the new nyad to use EXACTLY the same monads as the one passed, so it is a
	 * second reference to the same objects. Dangerous!
	 * 
	 * @param pN    The nyad to use causing all listed monads TO BE RE-USED AS IS.
	 *              USE A CONCRETE Nyad here or nada.
	 * @param pName A String for the new Nyad's name.
	 * @return NyadAbstract (Cast this as the concrete nyad to be used)
	 * @throws BadSignatureException Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final NyadAbstract duplicateReference(NyadAbstract pN, String pName)
			throws BadSignatureException, CladosMonadException, CladosNyadException {
		return new NyadAbstract(pName, pN, false);
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
		return new NyadAbstract(pN);
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
		return new NyadAbstract(pName, pN, true);
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
	public final NyadAbstract createWithMonadCopy(Monad pM, String pName)
			throws BadSignatureException, CladosMonadException, CladosNyadException {
		return new NyadAbstract(pName, pM, true);
	}

	/**
	 * Nyad Constructor #2 covered with this method, but with re-use
	 * 
	 * @param pM    The monad to be used as the first in monadList in a new nyad.
	 *              USE A CONCRETE Monad here or nada.
	 * @param pName A String for the new Nyad's name.
	 * @return NyadAbstract (Cast this as the concrete nyad to be used)
	 * @throws BadSignatureException Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final NyadAbstract createWithMonad(Monad pM, String pName)
			throws CladosNyadException, CladosMonadException {
		return new NyadAbstract(pName, pM, false);
	}
}
