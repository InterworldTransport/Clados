/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CladosGMonad<br>
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
 * ---org.interworldtransport.cladosG.CladosGMonad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.interworldtransport.cladosGExceptions.GradeOutOfRangeException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Monad construction is essentially the same across all of them so a builder
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
public enum CladosGMonad { // All of these have implicit private constructors
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	INSTANCE;

	/**
	 * This is effectively an 'instanceof' operator that avoids naming the actual
	 * class type. It compares to monads to see if they are of the same class.
	 * 
	 * @param pM1 First monad to test
	 * @param pM2 Second monad to test
	 * @return TRUE if monads are of the same type. FALSE otherwise.
	 */
	public static final boolean isClassMatch(MonadAbstract pM1, MonadAbstract pM2) {
		return pM1.getClass().equals(pM2.getClass());
	}

	/**
	 * Monad Constructor #1 covered with this method
	 * 
	 * @param pA The monad to be copied. USE A CONCRETE Monad here or nada.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 */
	public final MonadAbstract copyOf(MonadAbstract pA) throws CladosMonadException {
		return new MonadAbstract(pA);
	}

	/**
	 * Monad Constructor #2 covered with this method
	 * 
	 * @param pA    The monad to be copied. USE A CONCRETE Monad here or nada.
	 * @param pName A String for the new monad's name.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 */
	public final MonadAbstract copyRename(MonadAbstract pA, String pName) throws CladosMonadException {
		return new MonadAbstract(pName, pA);
	}

	/**
	 * Monad Constructor #6 covered with this method
	 * 
	 * @param pNumber The DivField to be re-used. USE A CONCRETE one here or nada.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pFrame  A String for the new frame name.
	 * @param pFoot   A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final MonadAbstract createOnlyCoeffs(DivField[] pNumber, String pName, String pAName, String pFrame,
			String pFoot, String pSig) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new MonadAbstract(pName, pAName, pFrame, pFoot, pSig, pNumber);
	}

	/**
	 * Monad Constructor #5 covered with this method
	 * 
	 * @param pNumber  The DivField to be re-used. USE A CONCRETE one here or nada.
	 * @param pName    A String for the new monad's name.
	 * @param pAName   A String for the new algebra's name.
	 * @param pFrame   A String for the new frame name.
	 * @param pFoot    A String to name a new Foot.
	 * @param pSig     A String for the new algebra's signature.
	 * @param pSpecial A String for special handling constructor. ex: "Zero", "One"
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException    Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException     Thrown for a general monad constructor error
	 * @throws GeneratorRangeException  Thrown if the pSig parameter is too long
	 * @throws GradeOutOfRangeException Thrown on an internal error if special case
	 *                                  handler glitches
	 */
	public final MonadAbstract createSpecial(DivField pNumber, String pName, String pAName, String pFrame, String pFoot,
			String pSig, String pSpecial)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException, GradeOutOfRangeException {
		return new MonadAbstract(pName, pAName, pFrame, pFoot, pSig, pNumber, pSpecial);
	}

	/**
	 * Monad Constructor #7 covered with this method
	 * 
	 * @param pNumber The DivField to be re-used. USE A CONCRETE one here or nada.
	 * @param pA      The Algebra to be re-used. USE A CONCRETE on here or nada.
	 * @param pName   A String for the new monad's name.
	 * @param pFrame  A String for the new frame name.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final MonadAbstract createWithAlgebra(DivField[] pNumber, Algebra pA, String pName, String pFrame)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new MonadAbstract(pName, pA, pFrame, pNumber);
	}

	/**
	 * Monad Constructor #3 covered with this method
	 * 
	 * @param pNumber The DivField to be re-used. USE A CONCRETE one here or nada.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pFrame  A String for the new frame name.
	 * @param pFoot   A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final MonadAbstract createZero(DivField pNumber, String pName, String pAName, String pFrame, String pFoot,
			String pSig) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new MonadAbstract(pName, pAName, pFrame, pFoot, pSig, pNumber);
	}

	/**
	 * Monad Constructor #4 covered with this method
	 * 
	 * @param pNumber The DivField to be re-used.
	 * @param pFt     A Foot to be referenced so a new one is NOT created.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pFrame  A String for the new frame name.
	 * @param pSig    A String for the new algebra's signature.
	 * @return MonadAbstract (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final MonadAbstract createWithFoot(DivField pNumber, Foot pFt, String pName, String pAName, String pFrame,
			String pSig) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new MonadAbstract(pName, pAName, pFrame, pFt, pSig, pNumber);
	}
}
