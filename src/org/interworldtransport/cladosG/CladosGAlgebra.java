/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CladosGAlgebra<br>
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
 * ---org.interworldtransport.cladosG.CladosGAlgebra<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * Algebra construction is essentially the same across all of them so a builder
 * pattern makes sense as an enumeration where each instance relies on shared
 * methods that 'switch' on their identity to determine what gets built and
 * returned.
 * <p>
 * This enumeration has non-static methods for each instance, but they don't
 * cause a state change. CladosGAlgebra HAS NO INTERNAL STATE to change. That
 * may change when frames and the physics package are built because Frame
 * tracking will likely move down from a Foot to an Algebra.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGAlgebra { // All of these have implicit private constructors
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	REALF,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	REALD,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	COMPLEXF,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	COMPLEXD;

	/**
	 * This is effectively an 'instanceof' operator that avoids naming the actual
	 * class type. It compares to algebras to see if they are of the same class.
	 * 
	 * @param pA1 First algebra to test
	 * @param pA2 Second algebra to test
	 * @return TRUE if algebras are of the same type. FALSE otherwise.
	 */
	public static final boolean isClassMatch(Algebra pA1, Algebra pA2) {
		return pA1.getClass().equals(pA2.getClass());
	}

	/**
	 * Algebra Constructor #1 covered with this
	 * 
	 * @param pA    The Algebra to be copied.
	 * @param pName A String for the new algebra's name.
	 * @return Algebra
	 */
	public final Algebra copyOf(Algebra pA, String pName) {
		return new Algebra(pName, pA);

	}

	/**
	 * Algebra Constructor #5 covered with this
	 * 
	 * @param pNumber The DivField to be re-used.
	 * @param pName   A String for the new algebra's name.
	 * @param pFTName A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Algebra
	 * @throws BadSignatureException   Thrown by an algebra constructor if the pSig
	 *                                 parameter is malformed
	 * @throws GeneratorRangeException Thrown by an algebra constructor if the pSig
	 *                                 parameter is too long
	 */
	public final Algebra create(DivField pNumber, String pName, String pFTName, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		switch (this) {
		case REALF -> {
			return new Algebra(pName, pFTName, pSig, CladosFBuilder.REALF.createZERO(pNumber.getCardinal()));
		}
		case REALD -> {
			return new Algebra(pName, pFTName, pSig, CladosFBuilder.REALD.createZERO(pNumber.getCardinal()));
		}
		case COMPLEXF -> {
			return new Algebra(pName, pFTName, pSig, CladosFBuilder.COMPLEXF.createZERO(pNumber.getCardinal()));
		}
		case COMPLEXD -> {
			return new Algebra(pName, pFTName, pSig, CladosFBuilder.COMPLEXD.createZERO(pNumber.getCardinal()));
		}
		default -> throw new IllegalArgumentException("Unexpected value as an Algebra mode: " + this);
		}
	}

	/**
	 * Algebra Constructor #3 covered with this
	 * 
	 * @param pF    A Foot to be referenced so a new one is NOT created.
	 * @param pCard The Cardinal to be re-used.
	 * @param pName A String for the new algebra's name.
	 * @param pSig  A String for the new algebra's signature.
	 * @return Algebra
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final Algebra createWithFoot(Foot pF, Cardinal pCard, String pName, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		return new Algebra(pName, pF, pCard, pSig);
	}

	/**
	 * Algebra Constructor #4 covered with this
	 * 
	 * @param pF      A Foot to be referenced so a new one is NOT created.
	 * @param pNumber The DivField to be re-used.
	 * @param pName   A String for the new algebra's name.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Algebra
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public final Algebra createWithFoot(Foot pF, DivField pNumber, String pName, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		switch (this) {
		case REALF -> {
			return new Algebra(pName, pF, pSig, CladosFBuilder.REALF.createZERO(pNumber.getCardinal()));
		}
		case REALD -> {
			return new Algebra(pName, pF, pSig, CladosFBuilder.REALD.createZERO(pNumber.getCardinal()));
		}
		case COMPLEXF -> {
			return new Algebra(pName, pF, pSig, CladosFBuilder.COMPLEXF.createZERO(pNumber.getCardinal()));
		}
		case COMPLEXD -> {
			return new Algebra(pName, pF, pSig, CladosFBuilder.COMPLEXD.createZERO(pNumber.getCardinal()));
		}
		default -> throw new IllegalArgumentException("Unexpected value as an Algebra mode: " + this);
		}
	}

	/**
	 * Algebra Constructor #2 covered with this method
	 * 
	 * @param pF    A Foot to be referenced so a new one is NOT created.
	 * @param pCard The Cardinal to be re-used.
	 * @param pGP   The GProduct to be re-used.
	 * @param pName A String for the new algebra's name.
	 * @return Algebra
	 */
	public final Algebra createWithFootPlus(Foot pF, Cardinal pCard, CliffordProduct pGP, String pName) {
		return new Algebra(pName, pF, pCard, pGP);
	}
}
