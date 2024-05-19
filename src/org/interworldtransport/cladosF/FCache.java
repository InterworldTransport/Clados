/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.FCache<br>
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
 * ---org.interworldtransport.cladosF.FCache<br>
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
 * <p>
 * At present, the only thing in CladosF being cached is Cardinals. cladosG.Algebra
 * has its protonumber which is little more than a Cardinal. cladosG.Foot has its
 * list of cardinals too. So there are a number of places where cardinals get attached.
 * The point of this cache, therefore, is to provide a global list to the modeler
 * who wants to ensure object references match perfectly when they should. That is
 * accomplished by setting the protonumber's Cardinal to one in this list BY REFERENCE
 * instead of by string.
 * <p>
 * Be aware that the list of cached objects is inheriently mutable. Alteration of an
 * object here alters it everywhere it is referenced. ALSO... because of this cache, 
 * it is unlikely that removal of object references elsewhere (set to null) will 
 * cause the allocated memory to be garbage collected. Any linked here in the cache
 * will persist while the cache does or the reference to it in the cache does.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public enum FCache {
	/**
	 * There is an implicit private constructor for this singleton, but we won't
	 * override it.
	 * <p>
	 * This ensures only one of these is active at a time. All CladosFCache references
	 * must refer back to this one cache.
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
	 * <p>
	 * @param pIn Cardinal to be appended to the cache IF it isn't already present.
	 */
	public void appendCardinal(Cardinal pIn) {
		Optional<Cardinal> test = findCardinal(pIn.getUnit());
		if (test.isEmpty())
			listOfCardinals.add(pIn);
	}

	/**
	 * Method appends offered Cardinals to cache IF not already present. If any ARE,
	 * nothing is done and the method silently loops through remaining Cardinals
	 * inserting ones that aren't.
	 * <p>
	 * @param pIn Cardinal Set to be appended to the cache IF they aren't already present.
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
	 * <p>
	 * @param pName String name of a Cardinal to be found in the cache
	 * @return Optional of Cardinal matching the name offered.
	 */
	public Optional<Cardinal> findCardinal(String pName) {
		return listOfCardinals.stream().filter(x -> x.getUnit().equals(pName)).findFirst();
	}

	/**
	 * This method clears the Cardinal cache.
	 */
	public void clearCardinals() {
		listOfCardinals.clear();
	}

	/**
	 * This is a brute force way of retrieving Cardinals in the cache. It should
	 * rarely be used.
	 * <p>
	 * @param pLoc integer index to be used when returning the listed Cardinal
	 * @return Cardinal at pLoc index will be returned.
	 */
	public Cardinal getCardinal(int pLoc) {
		if (listOfCardinals.size() < 1 | listOfCardinals.size() < pLoc+1)
			return null;
		return listOfCardinals.get(pLoc);
	}

	/**
	 * This method reports the size of the Cardinal cache.
	 * <p>
	 * @return int size of the Cardinal cache.
	 */
	public int getCardinalListSize() {
		return listOfCardinals.size();
	}

	/**
	 * This is just a pass-thru method to test whether the cache of Cardinals
	 * is empty.
	 * <p> 
	 * @return boolean True if the cache of Cardinals is empty. False otherwise.
	 */
	public boolean isEmpty() {
		return listOfCardinals.isEmpty();
	}

	/**
	 * This method supports the removal of a Cardinal from the cache.
	 * <p>
	 * @param pCard Cardinal to be removed
	 * @return boolean True if Cardinal found and removed. False if not found or
	 *         removal fails.
	 */
	public boolean removeCardinal(Cardinal pCard) {
		return listOfCardinals.remove(pCard);
	}
}
