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
 * This builder gets basic information and constructs many Clados Geometry objects.
 * 
 * This enumeration has a non-static element for the instance, t
 * hus CladosGBuilder HAS INTERNAL STATE that can change unlike 
 * CladosGAlgebra, CladosGMonad, and CladosGNyad.
 *	
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosGBuilder 
{	// This has an implicit private constructor we won't override.
	INSTANCE;
	public final static String cleanSignature(String pSig)
	{
		if (GProduct.validateSignature(pSig)) return pSig;
		else 
		{
			StringBuffer tSpot = new StringBuffer();
			for (char j : pSig.toCharArray())
			switch (j)
				{
					case '+':	tSpot.append(j);	// good character
								break;
					case '-':	tSpot.append(j);	// good character
				}								
			return tSpot.toString();
		}		
	}
	public final static Foot copyOfFoot(Foot pF, int pSpot)
	{
		return Foot.buildAsType(pF.getFootName(), pF.getCardinal(pSpot));
	}
	public final static GProduct copyOfGProduct(GProduct pGP) throws BadSignatureException
	{
		return new GProduct(pGP);
	}
	public final static Foot createFoot(String pName, String pCardName)
	{
		return Foot.buildAsType(pName, Cardinal.generate(pCardName));
	}
	public final static Foot createFootLike(String pName, Cardinal pCard)
	{
		return Foot.buildAsType(pName, pCard);
	}
	public final static Foot createFootLike(String pName, DivField pDiv)
	{
		return Foot.buildAsType(pName, pDiv.getCardinal());
	}
	public final static Foot createFootLike(String pName, Foot pF, int pSpot)
	{
		return Foot.buildAsType(pName, pF.getCardinal(pSpot));
	}
	public final static boolean validateSignature(String pSig)
	{
		return GProduct.validateSignature(pSig);
	}
	
	/**
	 * Heads Up! These ArrayList ensures this 'enumeration' is mutable.
	 * This class isn't intended to be static/immutable.
	 * It is supposed to be instantiated once and then used to keep 
	 * track of the CladosG objects that can be safely share in use
	 * while performing a useful function as a Builder. It is doing DOUBLE DUTY.
	 */
	private ArrayList<Basis>	listOfBases;
	private ArrayList<GProduct>	listOfGProducts;
	
	public void appendBasis(Basis pB)
	{
		if (findBasis((short)(pB.getGradeCount()-1)) != null) return; // Already in ArrayList
		listOfBases.ensureCapacity(listOfBases.size()+1);
		listOfBases.add(pB);
	}
	public void appendGProduct(GProduct pGP)
	{
		if (findGProduct(pGP) != null) return; // Already in ArrayList
		listOfGProducts.ensureCapacity(listOfGProducts.size()+1);
		listOfGProducts.add(pGP);
	}
	public Basis createBasis(short pGen) throws GeneratorRangeException
	{
		if (findBasis(pGen) != null) return findBasis(pGen); 
		Basis tSpot = Basis.using(pGen);
		listOfBases.add(tSpot);
		return tSpot;
	}
	public GProduct createGProduct(Basis pB, String pSig) throws GeneratorRangeException, BadSignatureException
	{
		if (!validateSignature(pSig)) throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		GProduct tFirst = findGProduct(pSig);
		if (tFirst != null) return tFirst; 		// GProduct already created, so just offer it instead of making a new one
		GProduct tSpot = new GProduct(pB, pSig);// Make a new GProduct and return it
		listOfGProducts.add(tSpot);
		return tSpot;
	}
	public GProduct createGProduct(String pSig) throws GeneratorRangeException, BadSignatureException
	{
		if (!validateSignature(pSig)) throw new BadSignatureException(null, "Signature validation failed in GProduct Builder");
		GProduct tFirst = findGProduct(pSig);
		if (tFirst != null) return tFirst; 		// GProduct already created, so just offer it instead of making a new one
		GProduct tSpot = new GProduct(pSig);	// Make a new GProduct and return it
		listOfGProducts.add(tSpot);
		return tSpot;
	}
	public Basis findBasis(short pGen)
	{
		for (Basis point : listOfBases)
			if (point.getGradeCount()-1 == pGen ) return point;
		return null;
	}
	public GProduct findGProduct(GProduct pGP) 
	{
		for (GProduct point : listOfGProducts)
			if (point == pGP ) return point; 
		return null;
	}
	public GProduct findGProduct(String pSig) throws BadSignatureException
	{
		if (!validateSignature(pSig)) throw new BadSignatureException(null, "Signature validation failed in GProduct Finder");
		for (GProduct point : listOfGProducts)
			if (point.getGradeCount()-1 == pSig.length() ) 
				if (point.getSignature().equals(pSig)) return point;
			else continue;
		return null;
	}
	public short getBasisListSize()	// shouldn't ever be larger than Basis.MAX_GEN
	{
		return (short) listOfBases.size();
	}
	public int getGProductListSize()	// shouldn't ever be larger than 2^(Basis.MAX_GEN+1).
	{
		return (int) listOfGProducts.size();
	}
	public boolean removeBasis(Basis pB)
	{
		boolean test = false;
		for (Basis point : listOfBases)
			if (point == pB) test = listOfBases.remove(point);
		return test;
	}
	public boolean removeBasis(short pGen)
	{
		boolean test = false;
		for (Basis point : listOfBases)
			if (point.getGradeCount()-1 == pGen) test = listOfBases.remove(point);
		return test;
	}
	public boolean removeGProduct(GProduct pGP)
	{
		if (listOfGProducts.size()==0) return false;
		boolean test = false;
		for (GProduct point : listOfGProducts)
			if (point == pGP) test = listOfGProducts.remove(point);
		return test;
	}
	public boolean removeGProduct(String pSig) throws BadSignatureException
	{
		if (!validateSignature(pSig)) throw new BadSignatureException(null, "Signature validation failed in GProduct Remover");
		if (listOfGProducts.size()==0) return false;
		boolean test = false;
		for (GProduct point : listOfGProducts)
			if (point.getGradeCount()-1 == pSig.length())
				if(point.getSignature().equals(pSig))
				{
					test = listOfGProducts.remove(point);
					break;
				}
			else continue;
		return test;
	}
}