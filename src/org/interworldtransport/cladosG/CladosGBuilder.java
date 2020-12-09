/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosGBuilder<br>
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
 * ---org.interworldtransport.cladosF.CladosGBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Optional;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFCache;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * This builder gets basic information and constructs many Clados Geometry
 * objects.
 * 
 * This enumeration has NO non-static element for the instance, thus
 * CladosGBuilder HAS NO INTERNAL STATE that can change.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGBuilder { // This has an implicit private constructor we won't override.
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	INSTANCE;

	/**
	 * Tests the signature string to see if it contains the correct chars and no
	 * more of them than can be supported.
	 * 
	 * This method just calls the method of the same name in the CliffordProduct
	 * interface. It is here for convenience.
	 * 
	 * @param pSig String signature to be tested
	 * @return TRUE if string is composed of '+' and '-' chars, but not too many.
	 *         FALSE otherwise.
	 */
	public final static boolean validateSignature(String pSig) {
		return CliffordProduct.validateSignature(pSig);
	}

	/**
	 * Tests the byte integer of generators to be used to see if it can be
	 * supported.
	 * 
	 * This method just calls the method of the same name in the CanonicalBasis
	 * interface. It is here for convenience.
	 * 
	 * @param pGen byte integer of number of generators for the test
	 * @return TRUE if integer is in the supported range. FALSE otherwise.
	 */
	public final static boolean validateSize(byte pGen) {
		return CanonicalBasis.validateSize(pGen);
	}

	/**
	 * Cleans the signature string to ensure it passes the validateSignature() test.
	 * 
	 * Any char in the string that isn't '+' or '-' is simply removed. If the
	 * resulting string is too long, it is clipped at the supported length.
	 * 
	 * @param pSig String signature to be cleaned
	 * @return String that has only + or - characters in it.
	 */
	public final static String cleanSignature(String pSig) {
		if (validateSignature(pSig))
			return pSig;
		else {
			StringBuffer tSpot = new StringBuffer();
			for (char j : pSig.toCharArray())
				switch (j) {
				case '+' -> tSpot.append(j); // good character
				case '-' -> tSpot.append(j); // good character
				}
			if (tSpot.length() > CladosConstant.MAXGRADE)
				return tSpot.substring(0, CladosConstant.MAXGRADE).toString();
			return tSpot.toString();
		}
	}

	/**
	 * This method creates a new Foot object with one Cardinal re-used from the Foot
	 * to be imitated.
	 * 
	 * @param pF    Foot object to copy
	 * @param pSpot indexed location in offered Foot to find a Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot copyOfFoot(Foot pF, int pSpot) {
		return createFootLike(pF.getFootName(), pF, pSpot);
	}

	/**
	 * This method creates a new Foot object and a new Cardinal to go with it.
	 * 
	 * NOTE this method checks the Cardinal cache first. If one is found that
	 * matches the offered name, it is re-used instead of creating a new Cardinal.
	 * 
	 * @param pName     String name of new Foot
	 * @param pCardName String name of new Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot createFoot(String pName, String pCardName) {
		Optional<Cardinal> find = CladosFCache.INSTANCE.findCardinal(pCardName);
		if (find.isPresent())
			return createFootLike(pName, find.get());
		return Foot.buildAsType(pName, Cardinal.generate(pCardName));
	}

	/**
	 * This method creates a new Foot object using the Cardinal offered.
	 * 
	 * @param pName String name of new Foot
	 * @param pCard Cardinal to be re-used.
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, Cardinal pCard) {
		return Foot.buildAsType(pName, pCard);
	}

	/**
	 * This method creates a new Foot object using the Cardinal offered.
	 * 
	 * @param pName String name of new Foot
	 * @param pDiv  DivField holding Cardinal to be re-used.
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, DivField pDiv) {
		return createFootLike(pName, pDiv.getCardinal());
	}

	/**
	 * This method creates a new Foot object with one Cardinal re-used from the Foot
	 * to be imitated but the Foot has a new name too.
	 * 
	 * @param pName New string name for Foot to be created.
	 * @param pF    Foot object to copy
	 * @param pSpot indexed location in offered Foot to find a Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, Foot pF, int pSpot) {
		return Foot.buildAsType(pName, pF.getCardinal(pSpot));
	}

	/**
	 * This method creates a basis and caches it.
	 * 
	 * @param pGen integer number of generators to use in constructing the basis.
	 * @return CanonicalBasis constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 CanonicalBasis and see why it complains.
	 */
	public final static CanonicalBasis createBasis(byte pGen) throws GeneratorRangeException {
		Optional<CanonicalBasis> tB = CladosGCache.INSTANCE.findBasisList(pGen);
		if (tB.isPresent())
			return tB.get();
		else {
			CanonicalBasis tSpot = Basis.using(pGen);
			CladosGCache.INSTANCE.appendBasis(tSpot);
			return tSpot;
		}

	}

	/**
	 * This method constructs a CliffordProduct using the offered basis and
	 * signature. It first checks the product cache and returns a matching product
	 * instead of constructing a new one IF it is found. If not, it deposits the
	 * offered Basis in the cache and then calls the method for creating a product
	 * that does not try to re-use a basis. The net result works the same, though,
	 * since that other method checks the basis cache before making a new basis. By
	 * the end of the method, both basis and product caches are populated with
	 * anything that had to be constructed.
	 * 
	 * @param pB   Basis to re-use in constructing product
	 * @param pSig String form of the product's signature
	 * @return CliffordProduct constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 CanonicalBasis and see why it complains.
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 */
	public final static CliffordProduct createGProduct(CanonicalBasis pB, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		Optional<CliffordProduct> tSpot = CladosGCache.INSTANCE.findGProductMap(pSig);
		if (tSpot.isPresent())
			return tSpot.get();
		else {
			if (pB != null)
				CladosGCache.INSTANCE.appendBasis(pB); // won't have to pass it now.
			tSpot = Optional.ofNullable(createGProduct(pSig));
			return tSpot.get();
		}
	}

	/**
	 * This method constructs a CliffordProduct using the offered signature String.
	 * It first checks the product cache and returns a matching product instead of
	 * constructing a new one IF it is found. If not, it checks the basis cache for
	 * a match to decide which product constructor to use. By the end of the method,
	 * both basis and product caches are populated with anything that had to be
	 * constructed.
	 * 
	 * @param pSig String form of the product's signature
	 * @return CliffordProduct constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 CanonicalBasis and see why it complains.
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 */
	public final static CliffordProduct createGProduct(String pSig) throws BadSignatureException, GeneratorRangeException {
		Optional<CliffordProduct> tSpot = CladosGCache.INSTANCE.findGProductMap(pSig);
		if (tSpot.isPresent())
			return tSpot.get(); // GProduct already created. return it.
		else {
			// Create a new GProduct, but might still find a cached Basis.
			Optional<CanonicalBasis> tB = CladosGCache.INSTANCE.findBasisList((byte) pSig.length());
			CliffordProduct tSpot2;
			if (tB.isPresent())
				tSpot2 = new GProduct(tB.get(), pSig);
			else
				tSpot2 = new GProduct(pSig);

			if (tSpot2 != null) {
				CladosGCache.INSTANCE.appendBasis(tSpot2.getBasis());
				CladosGCache.INSTANCE.appendGProduct(tSpot2);
			}
			return tSpot2;
		}
	}
}