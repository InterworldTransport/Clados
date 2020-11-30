/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosFCache<br>
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
 * ---org.interworldtransport.cladosF.CladosFCache<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.ArrayList;

public enum CladosFCache {
	INSTANCE;

	/**
	 * Heads Up! This ArrayList ensures this 'enumeration' is mutable. It's a cache,
	 * so this should surprise no one. It is supposed to be instantiated once and
	 * then used to keep track of Cardinals in use.
	 */
	private ArrayList<Cardinal> listOfCardinals = new ArrayList<Cardinal>(1);

	public void appendCardinal(Cardinal pIn) {
		Cardinal test = findCardinal(pIn.getUnit());
		if (test != null)
			listOfCardinals.add(pIn);
	}

	public Cardinal findCardinal(String pName) {
		return listOfCardinals.stream().filter(x -> x.getUnit().equals(pName)).findFirst().orElse(null);
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
