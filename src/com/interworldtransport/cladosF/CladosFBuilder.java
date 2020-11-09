/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.CladosFBuilder<br>
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
 * ---com.interworldtransport.cladosF.CladosFBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;

import java.util.ArrayList;

/**
 * This builder gets basic information and constructs any of the DivFields and
 * their supporting classes like a Cardinal. Some features are supported by the
 * CladosField enumeration.
 * 
 * The DivField builder is a singleton enforced as an enumeration that keeps
 * track of Cardinals and builds them as needed.
 * 
 * This enumeration has a non-static element for the instance, thus
 * CladosFBuilder HAS INTERNAL STATE that can change unlike the CladosField
 * enumerator of DivFields in the same package.
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

	/**
	 * Heads Up! This ArrayList ensures this 'enumeration' is mutable. This class
	 * isn't intended to be static/immutable. It is supposed to be instantiated once
	 * and then used to keep track of the Cardinals in use while performing a useful
	 * function as a Builder. It is doing DOUBLE DUTY.
	 */
	private ArrayList<Cardinal> listOfCardinals;

	//
	public Cardinal createCardinal(String pName) {
		Cardinal test = findCardinal(pName);
		if (test != null)
			return test;
		Cardinal tSpot = Cardinal.generate(pName);
		listOfCardinals.add(tSpot);
		return tSpot;
	}

	public ComplexD createComplexD() {
		return new ComplexD(createCardinal("C|D"), 0d, 0d);
	}

	public ComplexF createComplexF() {
		return new ComplexF(createCardinal("C|F"), 0f, 0f);
	}

	public RealD createRealD() {
		return new RealD(createCardinal("R|D"), 0d);
	}

	public RealF createRealF() {
		return new RealF(createCardinal("R|F"), 0f);
	}

	public Cardinal findCardinal(String pName) {
		return listOfCardinals.stream().filter(x -> x.getType().equals(pName)).findFirst().orElse(null);
	}

	public Cardinal getCardinal(short pLoc) {
		if (listOfCardinals.size() < 1 | listOfCardinals.size() < pLoc)
			return null;
		return listOfCardinals.get(pLoc);
	}

	public short getCardinalListSize() {
		return (short) listOfCardinals.size();
	}

	public boolean removeCardinal(Cardinal pCard) {
		return listOfCardinals.remove(pCard);
	}

}
