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
import java.util.Optional;
import java.util.Set;

/**
 * Any classes within CladosF which would benefit from a supporting cache make
 * use of this singleton enumeration as a 'builder'. Nothing fancy here
 * otherwise. Just simple create, append, find, and remove capabilities backed
 * by ArrayLists of cached objects.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFCache {
	/**
	 * There is an implicit private constructor for this singleton, but we won't
	 * override it.
	 */
	INSTANCE;

	/**
	 * Heads Up! This ArrayList ensures this 'enumeration' is mutable. It's a cache,
	 * so this should surprise no one. It is supposed to be instantiated once and
	 * then used to keep track of Cardinals in use.
	 */
	private ArrayList<Cardinal> listOfCardinals = new ArrayList<Cardinal>(1);

	/**
	 * Method appends offered Cardinal to cache IF one by that name is not already
	 * present. If it IS, nothing is done and the method silently returns.
	 * 
	 * @param pIn Cardinal to be appended to the cache IF it isn't already present.
	 */
	public void appendCardinal(Cardinal pIn) {
		Optional<Cardinal> test = findCardinal(pIn.getUnit());
		if (test.isEmpty())
			listOfCardinals.add(pIn);
	}

	/**
	 * Method appends offered Cardinals to cache IF not already present. If are ARE,
	 * nothing is done and the method silently loops through remaining Cardinals.
	 * 
	 * @param pIn Cardinal to be appended to the cache IF it isn't already present.
	 */
	public void appendCardinal(Set<Cardinal> pIn) {
		pIn.stream().forEach(pC -> {
			Optional<Cardinal> test = findCardinal(pC.getUnit());
			if (test.isEmpty())
				listOfCardinals.add(pC);
		});
	}

	/**
	 * This method returns an Optional of Cardinal using the string name offered for
	 * the search. If found, the optional will be engaged. If not, it will be
	 * disengaged. IF by some chance there are two cardinals in the cache by the
	 * same name (which should NOT happen) the first one found will be returned.
	 * 
	 * @param pName String name of a Cardinal to be found in the cache
	 * @return Optional of Cardinal matching the name offered.
	 */
	public Optional<Cardinal> findCardinal(String pName) {
		return listOfCardinals.stream().filter(x -> x.getUnit().equals(pName)).findFirst();
	}

	/**
	 * This method clears the Cardinal cache.
	 */
	public void clearCardinal() {
		listOfCardinals.clear();
	}

	/**
	 * This is a brute force way of retrieving Cardinals in the cache. It should
	 * rarely be used.
	 * 
	 * @param pLoc integer index to be used when returning the listed Cardinal
	 * @return Cardinal at pLoc index will be returned.
	 */
	public Cardinal getCardinal(int pLoc) {
		if (listOfCardinals.size() < 1 | listOfCardinals.size() < pLoc)
			return null;
		return listOfCardinals.get(pLoc);
	}

	/**
	 * This method reports the size of the Cardinal cache.
	 * 
	 * @return int size of the Cardinal cache.
	 */
	public int getCardinalListSize() {
		return listOfCardinals.size();
	}

	/**
	 * This method supports the removal of a Cardinal from the cache.
	 * 
	 * @param pCard Cardinal to be removed
	 * @return boolean True if Cardinal found and removed. False if not found or
	 *         removal fails.
	 */
	public boolean removeCardinal(Cardinal pCard) {
		return listOfCardinals.remove(pCard);
	}
}
