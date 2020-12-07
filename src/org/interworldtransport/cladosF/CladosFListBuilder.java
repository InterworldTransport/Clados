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

import java.util.ArrayList;

/**
 * This builder gets basic information and constructs any of the DivFields and
 * their supporting classes like a Cardinal. The builder can return arrays or
 * ArrayLists.
 * 
 * This is facilitated by the CladosField enumeration.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFListBuilder {
	/**
	 * There is an implicit private constructor for this singleton, but we won't
	 * override it.
	 */
	INSTANCE;

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pField CladosField enumeration hint for DivField child to be created.
	 * @param pCD    ArrayList of DivField Numbers to be copied.
	 * @return ArrayList of Numbers holds constructed copies of incoming numbers
	 */
	public final static ArrayList<DivField> copyOf(CladosField pField, ArrayList<DivField> pCD) {
		ArrayList<DivField> tSpot = new ArrayList<DivField>(pCD.size());
		switch (pField) {
		case REALF:
			for (int j = 0; j < pCD.size(); j++)
				tSpot.add(RealF.copyOf((RealF) pCD.get(j)));
			return tSpot;
		case REALD:
			for (int j = 0; j < pCD.size(); j++)
				tSpot.add(RealD.copyOf((RealD) pCD.get(j)));
			return tSpot;
		case COMPLEXF:
			for (int j = 0; j < pCD.size(); j++)
				tSpot.add(ComplexF.copyOf((ComplexF) pCD.get(j)));
			return tSpot;
		case COMPLEXD:
			for (int j = 0; j < pCD.size(); j++)
				tSpot.add(ComplexD.copyOf((ComplexD) pCD.get(j)));
			return tSpot;
		default:
			return null;
		}
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pCD Array of Numbers to be copied.
	 * @return ComplexD[] Newly constructed copies of incoming numbers
	 */
	public final static ComplexD[] copyOf(ComplexD[] pCD) {
		ComplexD[] tSpot = new ComplexD[pCD.length];
		int j = 0;
		for (ComplexD point : pCD) {
			tSpot[j] = ComplexD.copyOf(point);
			j++;
		}
		return tSpot;
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pCF Array of Numbers to be copied.
	 * @return ComplexF[] Newly constructed copies of incoming numbers
	 */
	public final static ComplexF[] copyOf(ComplexF[] pCF) {
		ComplexF[] tSpot = new ComplexF[pCF.length];
		int j = 0;
		for (ComplexF point : pCF) {
			tSpot[j] = ComplexF.copyOf(point);
			j++;
		}
		return tSpot;
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pRD Array of Numbers to be copied.
	 * @return RealD[] Newly constructed copies of incoming numbers
	 */
	public final static RealD[] copyOf(RealD[] pRD) {
		RealD[] tSpot = new RealD[pRD.length];
		int j = 0;
		for (RealD point : pRD) {
			tSpot[j] = RealD.copyOf(point);
			j++;
		}
		return tSpot;
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pRF Array of Numbers to be copied.
	 * @return RealF[] Newly constructed copies of incoming numbers
	 */
	public final static RealF[] copyOf(RealF[] pRF) {
		RealF[] tSpot = new RealF[pRF.length];
		int j = 0;
		for (RealF point : pRF) {
			tSpot[j] = RealF.copyOf(point);
			j++;
		}
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ZERO
	 */
	public final static ComplexD[] createComplexD(Cardinal pCard, int pSize) {
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new ComplexD(pCard, 0d, 0d);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ZERO
	 */
	public final static ComplexD[] createComplexD(int pSize) {
		Cardinal tC = Cardinal.generate("C|D");
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new ComplexD(tC, 0d, 0d);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pC Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ONE
	 */
	public final static ComplexD[] createComplexDONE(Cardinal pC, int pSize) {
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexD.newONE(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ONE
	 */
	public final static ComplexD[] createComplexDONE(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexD.newONE(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pC Cardinal to re-use.
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ZERO
	 */
	public final static ComplexD[] createComplexDZERO(Cardinal pC, int pSize) {
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexD.newZERO(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexD's all set to ZERO.
	 */
	public final static ComplexD[] createComplexDZERO(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		ComplexD[] tSpot = new ComplexD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexD.newZERO(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ZERO
	 */
	public final static ComplexF[] createComplexF(Cardinal pCard, int pSize) {
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new ComplexF(pCard, 0f, 0f);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ZERO
	 */
	public final static ComplexF[] createComplexF(int pSize) {
		Cardinal tC = Cardinal.generate("C|F");
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new ComplexF(tC, 0f, 0f);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pC Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ONE
	 */
	public final static ComplexF[] createComplexFONE(Cardinal pC, int pSize) {
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexF.newONE(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ONE
	 */
	public final static ComplexF[] createComplexFONE(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexF.newONE(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pC Cardinal to re-use.
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ZERO
	 */
	public final static ComplexF[] createComplexFZERO(Cardinal pC, int pSize) {
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexF.newZERO(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of ComplexF's all set to ZERO.
	 */
	public final static ComplexF[] createComplexFZERO(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		ComplexF[] tSpot = new ComplexF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = ComplexF.newZERO(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ZERO
	 */
	public final static RealD[] createRealD(Cardinal pCard, int pSize) {
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new RealD(pCard, 0d);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ZERO
	 */
	public final static RealD[] createRealD(int pSize) {
		Cardinal tC = Cardinal.generate("R|D");
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new RealD(tC, 0d);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pC Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ONE
	 */
	public final static RealD[] createRealDONE(Cardinal pC, int pSize) {
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealD.newONE(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ONE
	 */
	public final static RealD[] createRealDONE(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealD.newONE(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pC Cardinal to re-use.
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ZERO
	 */
	public final static RealD[] createRealDZERO(Cardinal pC, int pSize) {
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealD.newZERO(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealD's all set to ZERO.
	 */
	public final static RealD[] createRealDZERO(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		RealD[] tSpot = new RealD[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealD.newZERO(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ZERO
	 */
	public final static RealF[] createRealF(Cardinal pCard, int pSize) {
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new RealF(pCard, 0f);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ZERO
	 */
	public final static RealF[] createRealF(int pSize) {
		Cardinal tC = Cardinal.generate("R|F");
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = new RealF(tC, 0f);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pC Cardinal to re-use
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ONE
	 */
	public final static RealF[] createRealFONE(Cardinal pC, int pSize) {
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealF.newONE(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ONE
	 */
	public final static RealF[] createRealFONE(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealF.newONE(tC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pC Cardinal to re-use.
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ZERO
	 */
	public final static RealF[] createRealFZERO(Cardinal pC, int pSize) {
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealF.newZERO(pC);
		return tSpot;
	}

	/**
	 * This method returns an array of numbers using newly generated Cardinal.
	 * 
	 * No Cardinal caching occurs.
	 * 
	 * @param pS String name for Cardinal to generate
	 * @param pSize Size of the array of numbers to be returned
	 * @return Array of RealF's all set to ZERO.
	 */
	public final static RealF[] createRealFZERO(String pS, int pSize) {
		Cardinal tC = Cardinal.generate(pS);
		RealF[] tSpot = new RealF[pSize];
		for (int j = 0; j < pSize; j++)
			tSpot[j] = RealF.newZERO(tC);
		return tSpot;
	}
	//
}
