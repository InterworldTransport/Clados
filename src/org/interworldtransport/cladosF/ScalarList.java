/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ScalarList<br>
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
 * ---org.interworldtransport.cladosF.ScalarList<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.ArrayList;

import org.interworldtransport.cladosG.Basis;

/**
 * This class wraps an ArrayList of DivField descendants.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public class ScalarList {

	public static final ScalarList create() {
		return new ScalarList();
	}

	public static final ScalarList create(CladosField pMode) {
		return (new ScalarList()).setMode(pMode);
	}

	private CladosField mode;
	private ArrayList<Scale> scales;

	public ScalarList() {
		;
	}

	/**
	 * Primary constructor of the list of DivField descendents.
	 * 
	 * @param pMode CladosField enum value is the DivField child in the list
	 * @param pSize Size of the list to declare. It IS A power of 2 and must be in
	 *              the range of supported short integers for basis generators in
	 *              CladosG.Basis.
	 */
	public ScalarList(CladosField pMode, short pSize) {
		mode = pMode;
		if (pSize < 0)
			scales = new ArrayList<>(1);
		else if (pSize > Basis.MAX_GEN)
			scales = new ArrayList<>((short) Basis.MAX_GEN);
		else
			scales = new ArrayList<>((short) Math.pow(2, pSize));
	}

	public ScalarList add(Scale pS) {
		if (scales.contains(pS))
			return this;
		scales.ensureCapacity(scales.size() + 1);

		switch (mode) {
		case REALF -> {
			if (pS instanceof RealF)
				scales.add(pS);
		}
		case REALD -> {
			if (pS instanceof RealD)
				scales.add(pS);
		}
		case COMPLEXF -> {
			if (pS instanceof ComplexF)
				scales.add(pS);
		}
		case COMPLEXD -> {
			if (pS instanceof ComplexD)
				scales.add(pS);
		}
		}
		return this;
	}

	public CladosField getMode() {
		return mode;
	}

	public ArrayList<Scale> getScales() {
		return scales;
	}

	public ScalarList remove(Scale pS) {
		if (scales.contains(pS))
			scales.remove(pS);
		scales.trimToSize();
		return this;
	}

	public ScalarList replace(ScalarList pS) {
		scales.clear();
		int tSize = pS.getScales().size();
		scales.ensureCapacity(tSize);

		for (Scale point : pS.getScales()) {
			if (scales.contains(point))
				continue;
			switch (mode) {
			case REALF -> {
				if (point instanceof RealF)
					scales.add(point);
			}
			case REALD -> {
				if (point instanceof RealD)
					scales.add(point);
			}
			case COMPLEXF -> {
				if (point instanceof ComplexF)
					scales.add(point);
			}
			case COMPLEXD -> {
				if (point instanceof ComplexD)
					scales.add(point);
			}
			}
		}
		scales.trimToSize();
		return this;
	}

	public ScalarList setMode(CladosField mode) {
		this.mode = mode;
		return this;
	}

	public ScalarList setScales(ArrayList<Scale> scales) {
		this.scales = scales;
		return this;
	}
}
