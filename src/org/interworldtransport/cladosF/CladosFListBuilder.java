/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
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
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.ArrayList;

/**
 * This builder gets basic information and constructs any of the DivFields and their supporting
 * classes like a Cardinal. The builder can return arrays or ArrayLists.
 * 
 * This is facilitated by the CladosField enumeration.
 * 	
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFListBuilder 
{	// This has an implicit private constructor we won't override.
	INSTANCE;
	
	public final static ArrayList<DivField> copyOf(CladosField pField, ArrayList<DivField> pCD)
	{
		ArrayList<DivField> tSpot = new ArrayList<DivField>(pCD.size());	
		switch (pField)
		{
			case REALF:		for (short j = 0; j<pCD.size(); j++)
								tSpot.add(RealF.copyOf((RealF)pCD.get(j)));
							return tSpot;
			case REALD:		for (short j = 0; j<pCD.size(); j++)
								tSpot.add(RealD.copyOf((RealD)pCD.get(j)));
							return tSpot;
			case COMPLEXF:	for (short j = 0; j<pCD.size(); j++)
								tSpot.add(ComplexF.copyOf((ComplexF)pCD.get(j)));
							return tSpot;
			case COMPLEXD:	for (short j = 0; j<pCD.size(); j++)
								tSpot.add(ComplexD.copyOf((ComplexD)pCD.get(j)));
							return tSpot;
			default:		return null;
		}
	}	
	public final static ComplexD[] copyOf(ComplexD[] pCD)
	{
		ComplexD[] tSpot = new ComplexD[pCD.length];
		int j=0;
		for (ComplexD point : pCD)
		{
			tSpot[j] =ComplexD.copyOf(point);
			j++;
		}
		return tSpot;
	}
	public final static ComplexF[] copyOf(ComplexF[] pCF)
	{
		ComplexF[] tSpot = new ComplexF[pCF.length];
		int j=0;
		for (ComplexF point : pCF)
		{
			tSpot[j] = ComplexF.copyOf(point);
			j++;
		}
		return tSpot;
	}
	public final static RealD[] copyOf(RealD[] pRD)
	{
		RealD[] tSpot = new RealD[pRD.length];
		int j=0;
		for (RealD point : pRD)
		{
			tSpot[j] = RealD.copyOf(point);
			j++;
		}
		return tSpot;
	}
	public final static RealF[] copyOf(RealF[] pRF)
	{
		RealF[] tSpot = new RealF[pRF.length];
		int j=0;
		for (RealF point : pRF)
		{
			tSpot[j] = RealF.copyOf(point);
			j++;
		}
		return tSpot;
	}
	//
	public final static ComplexD[] createComplexD(Cardinal pCard, short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new ComplexD(pCard, 0d, 0d);
		return tSpot;
	}
	public final static ComplexD[] createComplexD(short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new ComplexD(Cardinal.generate("C|D"), 0d, 0d);
		return tSpot;
	}
	public final static ComplexD[] createComplexDONE(Cardinal pC, short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexD.newONE(pC);
		return tSpot;
	}
	public final static ComplexD[] createComplexDONE(String pS, short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexD.newONE(pS);
		return tSpot;
	}
	public final static ComplexD[] createComplexDZERO(Cardinal pC, short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexD.newZERO(pC);
		return tSpot;
	}
	public final static ComplexD[] createComplexDZERO(String pS, short pSize)
	{
		ComplexD[] tSpot = new ComplexD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexD.newZERO(pS);
		return tSpot;
	}
	//
	public final static ComplexF[] createComplexF(Cardinal pCard, short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new ComplexF(pCard, 0f, 0f);
		return tSpot;
	}
	public final static ComplexF[] createComplexF(short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new ComplexF(Cardinal.generate("C|F"), 0f, 0f);
		return tSpot;
	}
	public final static ComplexF[] createComplexFONE(Cardinal pC, short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexF.newONE(pC);
		return tSpot;
	}
	public final static ComplexF[] createComplexFONE(String pS, short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexF.newONE(pS);
		return tSpot;
	}
	public final static ComplexF[] createComplexFZERO(Cardinal pC, short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexF.newZERO(pC);
		return tSpot;
	}
	public final static ComplexF[] createComplexFZERO(String pS, short pSize)
	{
		ComplexF[] tSpot = new ComplexF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = ComplexF.newZERO(pS);
		return tSpot;
	}
	//
	public final static RealD[] createRealD(Cardinal pCard, short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new RealD(pCard, 0d);
		return tSpot;
	}
	public final static RealD[] createRealD(short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new RealD(Cardinal.generate("R|D"), 0d);
		return tSpot;
	}
	public final static RealD[] createRealDONE(Cardinal pC, short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealD.newONE(pC);
		return tSpot;
	}
	public final static RealD[] createRealDONE(String pS, short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealD.newONE(pS);
		return tSpot;
	}
	public final static RealD[] createRealDZERO(Cardinal pC, short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealD.newZERO(pC);
		return tSpot;
	}
	public final static RealD[] createRealDZERO(String pS, short pSize)
	{
		RealD[] tSpot = new RealD[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealD.newZERO(pS);
		return tSpot;
	}
	//
	public final static RealF[] createRealF(Cardinal pCard, short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new RealF(pCard, 0f);
		return tSpot;
	}
	public final static RealF[] createRealF(short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = new RealF(Cardinal.generate("R|F"), 0f);
		return tSpot;
	}
	public final static RealF[] createRealFONE(Cardinal pC, short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealF.newONE(pC);
		return tSpot;
	}
	public final static RealF[] createRealFONE(String pS, short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealF.newONE(pS);
		return tSpot;
	}
	public final static RealF[] createRealFZERO(Cardinal pC, short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealF.newZERO(pC);
		return tSpot;
	}
	public final static RealF[] createRealFZERO(String pS, short pSize)
	{
		RealF[] tSpot = new RealF[pSize];
		for (short j = 0; j<pSize; j++)
			tSpot[j] = RealF.newZERO(pS);
		return tSpot;
	}
	//
}
