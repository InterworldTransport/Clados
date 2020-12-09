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
import java.util.List;

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
	 * The implicit private constructor will not be overridden.
	 */
	COMPLEXD,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	COMPLEXF,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	REALD,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	REALF;

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
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pC    Cardinal to re-use
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use.
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use.
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use.
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use
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
	 * @param pS    String name for Cardinal to generate
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
	 * @param pC    Cardinal to re-use.
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
	 * @param pS    String name for Cardinal to generate
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

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pDV List of CladosF Numbers to be copied.
	 * @return List of DivField children Newly constructed copies of incoming
	 *         numbers
	 */
	public <T> List<? extends DivField> copyListOf(List<? extends DivField> pDV) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = RealF.copyOf((RealF) pDV.get(j));
			return List.of(tSpot);
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = RealD.copyOf((RealD) pDV.get(j));
			return List.of(tSpot);
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = ComplexF.copyOf((ComplexF) pDV.get(j));
			return List.of(tSpot);
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = ComplexD.copyOf((ComplexD) pDV.get(j));
			return List.of(tSpot);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pDV Array of Numbers to be copied.
	 * @return DivField[] Newly constructed copies of incoming numbers
	 */
	public <T> DivField[] copyOf(T[] pDV) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = RealF.copyOf((RealF) pDV[j]);
			return tSpot;
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = RealD.copyOf((RealD) pDV[j]);
			return tSpot;
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = ComplexF.copyOf((ComplexF) pDV[j]);
			return tSpot;
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = ComplexD.copyOf((ComplexD) pDV[j]);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all DivField child objects
	 * @param pSize The size of the array to create.
	 * @return List of DivField children set to ZERO using incoming cardinal.
	 */
	public <T> List<? extends DivField> createListOf(Cardinal pCard, int pSize) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createZERO(pCard);
			return List.of(tSpot);
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createZERO(pCard);
			return List.of(tSpot);
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createZERO(pCard);
			return List.of(tSpot);
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createZERO(pCard);
			return List.of(tSpot);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all DivField child objects
	 * @param pSize The size of the array to create.
	 * @return DivField[] Newly constructed ZEROS using incoming cardinal.
	 */
	public <T> DivField[] create(Cardinal pCard, int pSize) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createZERO(pCard);
			return tSpot;
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createZERO(pCard);
			return tSpot;
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createZERO(pCard);
			return tSpot;
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createZERO(pCard);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all DivField child objects
	 * @param pSize The size oF the array to create.
	 * @return List of DivField children as ZEROS with default cardinals.
	 */
	public <T> List<? extends DivField> createList(int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(CladosField.REALF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(CladosField.REALD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all DivField child objects
	 * @param pSize The size oF the array to create.
	 * @return DivField[] Newly constructed ZEROS with default cardinals.
	 */
	public <T> DivField[] create(int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(CladosField.REALF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(CladosField.REALD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}
}
