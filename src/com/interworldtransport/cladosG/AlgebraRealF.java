/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraRealF<br>
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
 * ---com.interworldtransport.cladosG.AlgebraRealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * The algebra object holds all geometric details that support the definition of
 * an algebra over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class AlgebraRealF extends AlgebraAbstract
{
	public static RealF generateNumber(AlgebraRealF pA, float pF)
	{
		return new RealF(pA.protoNumber, pF);
	}

	public static String toXMLString(AlgebraRealF pA)
	{
		StringBuffer rB = new StringBuffer("<Algebra name=\""
						+ pA.getAlgebraName() + "\" ");
		rB.append(">\n");
		rB.append(pA.protoNumber.toXMLString()+"\n");
		rB.append(pA.getFootPoint().toXMLString());
		rB.append(pA.getGProduct().toXMLString());
		rB.append("\n</Algebra>\n");
		return rB.toString();
	}

	protected RealF	protoNumber;

	public AlgebraRealF(String pS, Foot pF, GProduct pGP)
	{
		setAlgebraName(pS);
		setFootPoint(pF);
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		protoNumber = new RealF(pF.getNumberType(), 1.0f);
	}

	public AlgebraRealF(String pS, Foot pFoot, String pSig)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(pFoot);
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealF(pFoot.getNumberType(), 1.0f);
	}

	public AlgebraRealF(String pS, String pFootName, String pSig, RealF pF)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(new Foot(pFootName, pF.getFieldType()));
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealF(footPoint.getNumberType(), 1.0f);
	}
}
