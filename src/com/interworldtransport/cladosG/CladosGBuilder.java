/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.CladosGBuilder<br>
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
 * ---com.interworldtransport.cladosF.CladosGBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */

package com.interworldtransport.cladosG;

import java.util.ArrayList;

import com.interworldtransport.cladosF.Cardinal;
import com.interworldtransport.cladosF.DivField;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * This builder gets basic information and constructs many Clados Geometry
 * objects.
 * 
 * This enumeration has a non-static element for the instance, t hus
 * CladosGBuilder HAS INTERNAL STATE that can change unlike CladosGAlgebra,
 * CladosGMonad, and CladosGNyad.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGBuilder { // This has an implicit private constructor we won't override.
	INSTANCE;

	public final static String cleanSignature(String pSig) {
		if (GProduct.validateSignature(pSig))
			return pSig;
		else {
			StringBuffer tSpot = new StringBuffer();
			for (char j : pSig.toCharArray())
				switch (j) {
				case '+':
					tSpot.append(j); // good character
					break;
				case '-':
					tSpot.append(j); // good character
				}
			return tSpot.toString();
		}
	}

	public final static Foot copyOfFoot(Foot pF, int pSpot) {
		return Foot.buildAsType(pF.getFootName(), pF.getCardinal(pSpot));
	}

	public final static GProduct copyOfGProduct(GProduct pGP) throws BadSignatureException {
		return new GProduct(pGP);
	}

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

	public final static boolean validateSize(short pGen) {
		return Basis.validateSize(pGen);
	}

	public final static boolean validateSignature(String pSig) {
		return GProduct.validateSignature(pSig);
	}

	/**
	 * Heads Up! These ArrayList ensures this 'enumeration' is mutable. This class
	 * isn't intended to be static/immutable. It is supposed to be instantiated once
	 * and then used to keep track of the CladosG objects that can be safely share
	 * in use while performing a useful function as a Builder. It is doing DOUBLE
	 * DUTY.
	 */
	private ArrayList<Basis> listOfBases;
	private ArrayList<GProduct> listOfGProducts;

	public void appendBasis(Basis pB) {
		if (listOfBases.contains(pB))
			return; // Already in ArrayList
		listOfBases.ensureCapacity(listOfBases.size() + 1);
		listOfBases.add(pB);
	}

	public void appendGProduct(GProduct pGP) {
		if (listOfGProducts.contains(pGP))
			return; // Already in ArrayList
		listOfGProducts.ensureCapacity(listOfGProducts.size() + 1);
		listOfGProducts.add(pGP);
	}

	public Basis createBasis(short pGen) throws GeneratorRangeException {
		if (findBasis(pGen) == null) {
			Basis tSpot = Basis.using(pGen);
			listOfBases.add(tSpot);
			return tSpot;
		} else
			return findBasis(pGen);
	}

	public GProduct createGProduct(Basis pB, String pSig) throws GeneratorRangeException, BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		GProduct tFirst = findGProduct(pSig);
		if (tFirst != null)
			return tFirst; // GProduct already created, so just offer it instead of making a new one
		GProduct tSpot = new GProduct(pB, pSig);// Make a new GProduct and return it
		listOfGProducts.add(tSpot);
		return tSpot;
	}

	public GProduct createGProduct(String pSig) throws GeneratorRangeException, BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		GProduct tFirst = findGProduct(pSig);
		if (tFirst != null)
			return tFirst; // GProduct already created, so just offer it instead of making a new one
		GProduct tSpot = new GProduct(pSig); // Make a new GProduct and return it
		listOfGProducts.add(tSpot);
		return tSpot;
	}

	public Basis findBasis(short pGen) throws GeneratorRangeException {
		if (!validateSize(pGen))
			throw new GeneratorRangeException("Unsupported number of generators in findBasis(short)");
		return listOfBases.stream().filter(x -> (x.getGradeCount() - 1) == pGen).findFirst().orElse(null); // Deliver
																											// Basis OR
																											// null
	}

	public GProduct findGProduct(String pSig) throws BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Finder");
		return listOfGProducts.stream().filter(x -> (x.getGradeCount() - 1 == pSig.length())).findFirst().orElse(null); // Deliver
																														// GProduct
																														// OR
																														// null
	}

	public short getBasisListSize() // shouldn't ever be larger than Basis.MAX_GEN
	{
		return (short) listOfBases.size();
	}

	public int getGProductListSize() // shouldn't ever be larger than 2^(Basis.MAX_GEN+1). +1 TOO BIG for shorts.
	{
		return listOfGProducts.size();
	}

	public boolean removeBasis(Basis pB) {
		return listOfBases.remove(pB);
	}

	public boolean removeBasis(short pGen) throws GeneratorRangeException {
		Basis B = findBasis(pGen); // This function validates the support status of the number of generators
		if (B == null)
			return true;
		return removeBasis(B);
	}

	public boolean removeGProduct(GProduct pGP) {
		return listOfGProducts.remove(pGP);
	}

	public boolean removeGProduct(String pSig) throws BadSignatureException {
		GProduct GP = findGProduct(pSig); // This function validates the passed signature
		if (GP == null)
			return true;
		return removeGProduct(GP);
	}
}