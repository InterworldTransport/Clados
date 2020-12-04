/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosGCache<br>
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
 * ---org.interworldtransport.cladosF.CladosGCache<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Optional;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public enum CladosGCache {
	INSTANCE;

	public final static boolean validateSignature(String pSig) {
		return GProduct.validateSignature(pSig);
	}

	public final static boolean validateSize(short pGen) {
		return Basis.validateSize(pGen);
	}

	/**
	 * Heads Up! These ArrayList ensures this 'enumeration' is mutable. This class
	 * is a cache, so this should suprise no one. It is supposed to keep track of
	 * the CladosG objects that can be safely shared in use.
	 */
	//private ArrayList<Basis> listOfBases = new ArrayList<Basis>(1);
	//private ArrayList<GProduct> listOfGProducts = new ArrayList<GProduct>(1);
	private ArrayList<CanonicalBasis> listOfBasisLists = new ArrayList<>(1);
	private ArrayList<CliffordProduct> listOfGProductMaps = new ArrayList<>(1);

	//public void appendBasis(Basis pB) {
	//	if (listOfBases.contains(pB))
	//		return; // Already in ArrayList
	//	listOfBases.ensureCapacity(listOfBases.size() + 1);
	//	listOfBases.add(pB);
	//}
	
	public void appendBasis(CanonicalBasis pB) {
		if (listOfBasisLists.contains(pB))
			return; // Already in ArrayList
		listOfBasisLists.ensureCapacity(listOfBasisLists.size() + 1);
		listOfBasisLists.add(pB);
	}

	//public void appendGProduct(GProduct pGP) {
	//	if (listOfGProducts.contains(pGP))
	//		return; // Already in ArrayList
	//	listOfGProducts.ensureCapacity(listOfGProducts.size() + 1);
	//	listOfGProducts.add(pGP);
	//}
	public void appendGProduct(CliffordProduct pGP) {
		if (listOfGProductMaps.contains(pGP))
			return; // Already in ArrayList
		listOfGProductMaps.ensureCapacity(listOfGProductMaps.size() + 1);
		listOfGProductMaps.add(pGP);
	}

	//public Basis findBasis(short pGen) throws GeneratorRangeException {
	//	if (!validateSize(pGen))
	//		throw new GeneratorRangeException("Unsupported number of generators in findBasis(short)");
	//	return listOfBases.stream().filter(x -> (x.getGradeCount() - 1) == pGen).findFirst().orElse(null);
		// Deliver Basis OR null
	//}
	
	public Optional<CanonicalBasis> findBasisList(short pGen) throws GeneratorRangeException {
		if (!validateSize(pGen))
			throw new GeneratorRangeException("Unsupported number of generators in findBasis(short)");
		return listOfBasisLists.stream().filter(x -> (x.getGradeCount() - 1) == pGen).findFirst();
		// Deliver Basis OR null
	}

	//public GProduct findGProduct(String pSig) throws BadSignatureException {
	//	if (!validateSignature(pSig))
	//		throw new BadSignatureException(null, "Signature validation failed in GProduct Finder");
	//	return listOfGProducts.stream().filter(x -> x.getSignature().equals(pSig)).findFirst().orElse(null);
		// Deliver GProduct OR null
	//}
	public Optional<CliffordProduct> findGProductMap(String pSig) throws BadSignatureException {
		if (!validateSignature(pSig))
			throw new BadSignatureException(null, "Signature validation failed in GProduct Finder");
		return listOfGProductMaps.stream().filter(x -> x.signature().equals(pSig)).findFirst();
		// Deliver GProduct OR null
	}

	public short getBasisListSize() // shouldn't ever be larger than Basis.MAX_GEN
	{
		return (short) listOfBasisLists.size();
	}

	public int getGProductListSize() // shouldn't ever be larger than 2^(Basis.MAX_GEN+1). +1 TOO BIG for shorts.
	{
		return listOfGProductMaps.size();
	}

	public boolean removeBasis(CanonicalBasis pB) {
		return listOfBasisLists.remove(pB);
	}

	public boolean removeBasis(short pGen) throws GeneratorRangeException {
		Optional<CanonicalBasis> B = findBasisList(pGen); // This function validates the support status of the number of generators
		if (B.isEmpty())
			return true;
		return removeBasis(B.get());
	}

	public boolean removeGProduct(CliffordProduct pGP) {
		return listOfGProductMaps.remove(pGP);
	}

	public boolean removeGProduct(String pSig) throws BadSignatureException {
		Optional<CliffordProduct> GP = findGProductMap(pSig); // This function validates the passed signature
		if (GP.isEmpty())
			return true;
		return removeGProduct(GP.get());
	}
}
