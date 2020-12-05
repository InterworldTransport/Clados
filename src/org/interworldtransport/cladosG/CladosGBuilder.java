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
	INSTANCE;

	public final static boolean validateSignature(String pSig) {
		return CliffordProduct.validateSignature(pSig);
	}

	public final static boolean validateSize(short pGen) {
		return CanonicalBasis.validateSize(pGen);
	}

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
			return tSpot.toString();
		}
	}

	public final static Foot copyOfFoot(Foot pF, int pSpot) {
		return Foot.buildAsType(pF.getFootName(), pF.getCardinal(pSpot));
	}

	// public final static GProduct copyOfGProduct(GProduct pGP) throws
	// BadSignatureException {
	// return new GProduct(pGP);
	// }

	public final static Foot createFoot(String pName, String pCardName) {
		return Foot.buildAsType(pName, Cardinal.generate(pCardName));
	}

	public final static Foot createFootLike(String pName, Cardinal pCard) {
		return Foot.buildAsType(pName, pCard);
	}

	public final static Foot createFootLike(String pName, DivField pDiv) {
		return Foot.buildAsType(pName, pDiv.getCardinal());
	}

	public final static Foot createFootLike(String pName, Foot pF, int pSpot) {
		return Foot.buildAsType(pName, pF.getCardinal(pSpot));
	}

	public CanonicalBasis createBasis(byte pGen) throws GeneratorRangeException {
		Optional<CanonicalBasis> tB = CladosGCache.INSTANCE.findBasisList(pGen);
		if (tB.isPresent())
			return tB.get();
		else {
			CanonicalBasis tSpot = BasisList.using(pGen);
			CladosGCache.INSTANCE.appendBasis(tSpot);
			return tSpot;
		}

	}

	public CliffordProduct createGProduct(CanonicalBasis pB, String pSig)
			throws GeneratorRangeException, BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		CliffordProduct tSpot;
		if (pB == null)
			tSpot = createGProduct(pSig);
		else {
			Optional<CliffordProduct> tSpot2 = CladosGCache.INSTANCE.findGProductMap(pSig);
			if (tSpot2.isPresent())
				return tSpot2.get(); // GProduct already created, so just offer it instead of making a new one
			tSpot = new GProductMap(pB, pSig);// Make a new GProduct and return it
			CladosGCache.INSTANCE.appendBasis(pB);
			CladosGCache.INSTANCE.appendGProduct(tSpot);
		}
		return tSpot;
	}

	public CliffordProduct createGProduct(String pSig)
			throws GeneratorRangeException, BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		Optional<CliffordProduct> tSpot2 = CladosGCache.INSTANCE.findGProductMap(pSig);
		if (tSpot2.isPresent())
			return tSpot2.get();
		// GProduct already created, so just offer it instead of making a new one. At
		// this point we have to create a new GProduct, but might still share a Basis.
		// So... look for it.

		CliffordProduct tSpot;
		Optional<CanonicalBasis> tB = CladosGCache.INSTANCE.findBasisList((short) pSig.length());
		if (tB.isPresent())
			tSpot = new GProductMap(tB.get(), pSig); // Basis is found, so use re-use constructor.
		else {
			tSpot = new GProductMap(pSig); // Make a new GProduct with implied new Basis.
			CladosGCache.INSTANCE.appendBasis(tSpot.getBasis());
		}
		CladosGCache.INSTANCE.appendGProduct(tSpot);
		return tSpot;
	}
}