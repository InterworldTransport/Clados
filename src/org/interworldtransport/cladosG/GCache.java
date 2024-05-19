/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GCache<br>
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
 * ---org.interworldtransport.cladosG.GCache<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Any classes within CladosG which would benefit from a supporting cache make
 * use of this singleton enumeration as a 'builder'. Nothing fancy here
 * otherwise. Just simple create, append, find, and remove capabilities backed
 * by ArrayLists of cached objects.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public enum GCache {
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	INSTANCE;

	/**
	 * Heads Up! These ArrayLists ensure this 'enumeration' is mutable. This class
	 * is a cache, so this should suprise no one. It is supposed to keep track of
	 * the CladosG objects that can be safely shared in use.
	 */
	private ArrayList<CanonicalBasis> listOfBases = new ArrayList<>(1);
	/**
	 * Heads Up! These ArrayLists ensure this 'enumeration' is mutable. This class
	 * is a cache, so this should suprise no one. It is supposed to keep track of
	 * the CladosG objects that can be safely shared in use.
	 */
	private ArrayList<CliffordProduct> listOfGProducts = new ArrayList<>(1);

	/**
	 * Method appends offered basis to cache IF one like it is not already present.
	 * If it IS, nothing is done and the method silently returns.
	 * <p>
	 * By 'like it' we mean the basis objects have the same number of generators
	 * used to construct them. It doesn't matter what signatures are used as a basis
	 * isn't aware of products except as lists. NO generator duplications should
	 * exist in blades within a basis, thus all N-generator basis objects are
	 * structurally the same even if represented by different instances.
	 * <p>
	 * Important NOTE. Basis objects capture structural meaning implied by a list of
	 * generators of an algebra. They do NOT capture the meaning of the generators
	 * themselves. Generators in one algebra need not mean the same thing as
	 * generators in another algebra, but the structure created in a basis is the
	 * same between algebras if the basis for each shares the same number of
	 * generators.
	 * <p>
	 * @param pB CanonicalBasis to be appended to the cache IF not already present.
	 */
	public void appendBasis(CanonicalBasis pB) {
		if (!listOfBases.contains(pB))
			listOfBases.add(pB);
	}

	/**
	 * Method appends offered product to cache IF one like it is not already
	 * present. If it IS, nothing is done and the method silently returns.
	 * <p>
	 * By 'like it' we mean the product objects have the same number of generators
	 * and identical signatures used to construct them. NO generator duplications
	 * will survive in a product table after reducing blade combinations using the
	 * product's signature, so there is structural similarity between product tables
	 * that use the same number of generators (because of basis similarities) and
	 * the same signatures.
	 * <p>
	 * Important Note. CliffordProduct objects capture structural meaning implied in
	 * a product table of elements of a CanonicalBasis. As with a basis, no meaning
	 * to the generators or blades is implied in a product table.
	 * <p>
	 * @param pGP CliffordProduct to be appended to the cache IF not already
	 *            present.
	 */
	public void appendGProduct(CliffordProduct pGP) {
		if (!listOfGProducts.contains(pGP))
			listOfGProducts.add(pGP);
	}

	/**
	 * This is for resetting the cache of basis objects. It should rarely be used
	 * since they don't take up a lot of space, but it is faster than removing one
	 * at a time.
	 */
	public void clearBases() {
		listOfBases = new ArrayList<>(1);
	}

	/**
	 * This is for resetting the cache of gproduct objects. It should be used sparingly
	 * since gproducts can be time consuming to recreate for large algebras. It 
	 * shouldn't HAVE to be done, but it is faster than removing one at a time.
	 */
	public void clearGProducts() {
		new ArrayList<>(1);
	}

	/**
	 * This method returns an Optional of CanonicalBasis using the integer number of
	 * generators offered for the search. If found, the optional will be engaged. If
	 * not, it will be disengaged. IF by some chance there are two basis instances
	 * in the cache by the same number of generators (which should NOT happen) the
	 * first one found will be returned in the Optional.
	 * <p>
	 * @param pGen byte integer of generators in a basis to be found in the cache
	 * @return Optional of CanonicalBasis matching the number of generators offered.
	 */
	public Optional<CanonicalBasis> findBasisList(byte pGen) {
		return listOfBases.stream().filter(x -> (x.getGradeCount() - 1) == pGen).findFirst();
	}

	/**
	 * This method returns an Optional of CliffordProduct using the String signature
	 * offered for the search. If found, the optional will be engaged. If not, it
	 * will be disengaged. IF by some chance there are two product instances in the
	 * cache by the same signatures (which should NOT happen) the first one found
	 * will be returned in the Optional.
	 * <p>
	 * @param pSig String signature in a product to be found in the cache
	 * @return Optional of CliffordProduct matching the signature offered.
	 */
	public Optional<CliffordProduct> findGProductMap(String pSig) {
		return listOfGProducts.stream().filter(x -> x.signature().equals(pSig)).findFirst();
	}

	/**
	 * Simple gettor for the size of the basis cache. Since there is a limit to the
	 * Generator enumeration, there is also a limit to the basis cache. One should
	 * NEVER see more than CladosConstant.MAXGRADE basis objects in the cache.
	 * <p>
	 * @return byte integer of the size of the cache of basis instances.
	 */
	public byte getBasisListSize() {
		return (byte) listOfBases.size();
	}

	/**
	 * Simple gettor for the size of the product cache. Since there is a limit to
	 * the Generator enumeration, there is also a limit to the product cache. One
	 * should NEVER see more than 2^(CladosConstant.MAXGRADE+1)-1 product objects in
	 * the cache.
	 * <p>
	 * @return integer of the size of the cache of basis instances.
	 */
	public int getGProductListSize() {
		return listOfGProducts.size();
	}

	/**
	 * Method removes explicit basis from cache IF present. If it IS NOT, nothing is
	 * done and the method silently returns.
	 * <p>
	 * @param pB CanonicalBasis to remove from the cache IF present.
	 * @return boolean TRUE if removal succeed. FALSE otherwise.
	 */
	public boolean removeBasis(CanonicalBasis pB) {
		return listOfBases.remove(pB);
	}

	/**
	 * Method removes implied basis from cache IF one like it is present. If it IS
	 * NOT, nothing is done and the method silently returns.
	 * <p>
	 * An implied basis is simply one that matches the integer number of generators
	 * passed in as a parameter. If no basis is found, nothing is done. That covers
	 * error conditions too. For example, no basis exists with -1 generators, but
	 * this method will report TRUE as though it removed it. There is no harm in
	 * this since the point of this method is to clean out the cache and NOT to
	 * error check the calling object.
	 * <p>
	 * So... Generator size quality is NOT checked.
	 * <p>
	 * @param pGen byte integer number of generators in a basis to remove from the
	 *             cache IF present.
	 * @return boolean TRUE if removal succeed. FALSE otherwise.
	 */
	public boolean removeBasis(byte pGen) {
		Optional<CanonicalBasis> B = findBasisList(pGen);
		if (B.isEmpty())
			return true;
		return removeBasis(B.get());
	}

	/**
	 * Method removes explicit product from cache IF present. If it IS NOT, nothing
	 * is done and the method silently returns.
	 * <p>
	 * @param pGP CliffordProduct to remove from the cache IF present.
	 * @return boolean TRUE if removal succeed. FALSE otherwise.
	 */
	public boolean removeGProduct(CliffordProduct pGP) {
		return listOfGProducts.remove(pGP);
	}

	/**
	 * Method removes implied product from cache IF one like it is present. If it IS
	 * NOT, nothing is done and the method silently returns.
	 * <p>
	 * An implied product is simply one that matches the signature parameter and its
	 * integer size. If no product is found, nothing is done. That covers error
	 * conditions too. For example, no CliffordProduct exists with -1 generators (no
	 * matter the signature) or 3 generators with "+*-" signature, but this method
	 * will report TRUE as though it removed them. There is no harm in this since
	 * the point of this method is to clean out the cache and NOT to error check the
	 * calling object.
	 * <p>
	 * So... Signature quality is NOT checked.
	 * <p>
	 * @param pSig String signature of product to remove from the cache IF present.
	 * @return boolean TRUE if removal succeed. FALSE otherwise.
	 */
	public boolean removeGProduct(String pSig) {
		Optional<CliffordProduct> GP = findGProductMap(pSig); // This function validates the passed signature
		if (GP.isEmpty())
			return true;
		return removeGProduct(GP.get());
	}
}
