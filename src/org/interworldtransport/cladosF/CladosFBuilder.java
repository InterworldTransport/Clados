/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
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
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.Optional;

/**
 * This builder gets basic information and constructs any of the DivFields and
 * their supporting classes like a Cardinal. Some features are supported by the
 * CladosField enumeration.
 * 
 * The DivField builder is a singleton enforced as an enumeration.
 * 
 * This enumeration has NO non-static element for the instance, thus
 * CladosFBuilder HAS NO INTERNAL STATE that can change.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFBuilder {
	/**
	 * There is an implicit private constructor for this singleton, but we won't
	 * override it.
	 */
	INSTANCE;

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pCD Number to be copied.
	 * @return ComplexD Newly constructed copy of incoming number
	 */
	public final static ComplexD copyOf(ComplexD pCD) {
		return ComplexD.copyOf(pCD);
	}

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pCF Number to be copied.
	 * @return ComplexF Newly constructed copy of incoming number
	 */
	public final static ComplexF copyOf(ComplexF pCF) {
		return ComplexF.copyOf(pCF);
	}

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pRD Number to be copied.
	 * @return RealD Newly constructed copy of incoming number
	 */
	public final static RealD copyOf(RealD pRD) {
		return RealD.copyOf(pRD);
	}

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pRF Number to be copied.
	 * @return RealR Newly constructed copy of incoming number
	 */
	public final static RealF copyOf(RealF pRF) {
		return RealF.copyOf(pRF);
	}

	/**
	 * Method copies the incoming cardinal into a distinct 'zero' number.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return ComplexD Newly constructed 'zero' number.
	 */
	public final static ComplexD createComplexD(Cardinal pCard) {
		return new ComplexD(pCard, 0d, 0d);
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return ComplexD number created
	 */
	public final static ComplexD createComplexDONE(String pS) {
		return ComplexD.newONE(pS);
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return ComplexD number created
	 */
	public final static ComplexD createComplexDZERO(String pS) {
		return ComplexD.newZERO(pS);
	}

	/**
	 * Method copies the incoming cardinal into a distinct 'zero' number.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return ComplexF Newly constructed 'zero' number.
	 */
	public final static ComplexF createComplexF(Cardinal pCard) {
		return new ComplexF(pCard, 0f, 0f);
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return ComplexF number created
	 */
	public final static ComplexF createComplexFONE(String pS) {
		return ComplexF.newONE(pS);
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return ComplexF number created
	 */
	public final static ComplexF createComplexFZERO(String pS) {
		return ComplexF.newZERO(pS);
	}

	/**
	 * Method copies the incoming cardinal into a distinct 'zero' number.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return RealD Newly constructed 'zero' number.
	 */
	public final static RealD createRealD(Cardinal pCard) {
		return new RealD(pCard, 0d);
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return RealD number created
	 */
	public final static RealD createRealDONE(String pS) {
		return RealD.newONE(pS);
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return RealD number created
	 */
	public final static RealD createRealDZERO(String pS) {
		return RealD.newZERO(pS);
	}

	/**
	 * Method copies the incoming cardinal into a distinct 'zero' number.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return RealF Newly constructed 'zero' number.
	 */
	public final static RealF createRealF(Cardinal pCard) {
		return new RealF(pCard, 0f);
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return RealF number created
	 */
	public final static RealF createRealFONE(String pS) {
		return RealF.newONE(pS);
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return RealF number created
	 */
	public final static RealF createRealFZERO(String pS) {
		return RealF.newZERO(pS);
	}

	/**
	 * Method creates a new Cardinal using the string provided IF one by that name
	 * is not present in the cache. If it IS in the cache, the cached Cardinal is
	 * returned instead.
	 * 
	 * @param pName String name for the associated Cardinal
	 * @return Cardinal unit cardinal created or retrieved
	 */
	public final static Cardinal createCardinal(String pName) {
		Optional<Cardinal> test = CladosFCache.INSTANCE.findCardinal(pName);
		if (test.isEmpty()) {
			test = Optional.ofNullable(Cardinal.generate(pName));
			CladosFCache.INSTANCE.appendCardinal(test.get());
		}
		return test.get();
	}

	/**
	 * Method creates a new number as distinct ZERO object using the default
	 * cardinal name.
	 * 
	 * @return ComplexD Newly constructed number
	 */
	public final static ComplexD createComplexD() {
		return new ComplexD(createCardinal("C|D"), 0d, 0d);
	}

	/**
	 * Method creates a new number as distinct ZERO object using the default
	 * cardinal name.
	 * 
	 * @return ComplexF Newly constructed number
	 */
	public final static ComplexF createComplexF() {
		return new ComplexF(createCardinal("C|F"), 0f, 0f);
	}

	/**
	 * Method creates a new number as distinct ZERO object using the default
	 * cardinal name.
	 * 
	 * @return RealD Newly constructed number
	 */
	public final static RealD createRealD() {
		return new RealD(createCardinal("R|D"), 0d);
	}

	/**
	 * Method creates a new number as distinct ZERO object using the default
	 * cardinal name.
	 * 
	 * @return RealF Newly constructed number
	 */
	public final static RealF createRealF() {
		return new RealF(createCardinal("R|F"), 0f);
	}

}
