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
public enum CladosFBuilder { // This has an implicit private constructor we won't override.
	INSTANCE;

	public final static ComplexD copyOf(ComplexD pCD) {
		return ComplexD.copyOf(pCD);
	}

	public final static ComplexF copyOf(ComplexF pCF) {
		return ComplexF.copyOf(pCF);
	}

	public final static RealD copyOf(RealD pRD) {
		return RealD.copyOf(pRD);
	}

	public final static RealF copyOf(RealF pRF) {
		return RealF.copyOf(pRF);
	}

	public final static ComplexD createComplexD(Cardinal pCard) {
		return new ComplexD(pCard, 0d, 0d);
	}

	public final static ComplexD createComplexDONE(String pS) {
		return ComplexD.newONE(pS);
	}

	public final static ComplexD createComplexDZERO(String pS) {
		return ComplexD.newONE(pS);
	}

	public final static ComplexF createComplexF(Cardinal pCard) {
		return new ComplexF(pCard, 0f, 0f);
	}

	public final static ComplexF createComplexFONE(String pS) {
		return ComplexF.newONE(pS);
	}

	public final static ComplexF createComplexFZERO(String pS) {
		return ComplexF.newONE(pS);
	}

	public final static RealD createRealD(Cardinal pCard) {
		return new RealD(pCard, 0d);
	}

	public final static RealD createRealDONE(String pS) {
		return RealD.newONE(pS);
	}

	public final static RealD createRealDZERO(String pS) {
		return RealD.newZERO(pS);
	}

	public final static RealF createRealF(Cardinal pCard) {
		return new RealF(pCard, 0f);
	}

	public final static RealF createRealFONE(String pS) {
		return RealF.newONE(pS);
	}

	public final static RealF createRealFZERO(String pS) {
		return RealF.newZERO(pS);
	}

	public final static Cardinal createCardinal(String pName) {
		Cardinal test = CladosFCache.INSTANCE.findCardinal(pName);
		if (test == null) {
			test = Cardinal.generate(pName);
			CladosFCache.INSTANCE.appendCardinal(test);
		}
		return test;
	}

	public final static ComplexD createComplexD() {
		return new ComplexD(createCardinal("C|D"), 0d, 0d);
	}

	public final static ComplexF createComplexF() {
		return new ComplexF(createCardinal("C|F"), 0f, 0f);
	}

	public final static RealD createRealD() {
		return new RealD(createCardinal("R|D"), 0d);
	}

	public final static RealF createRealF() {
		return new RealF(createCardinal("R|F"), 0f);
	}

}
