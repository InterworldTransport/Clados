/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraComplexF<br>
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
 * ---com.interworldtransport.cladosG.AlgebraComplexF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class AlgebraComplexF extends AlgebraAbstract
{
	public static ComplexF generateNumber(AlgebraComplexF pA, float pF1, float pF2)
	{
		return new ComplexF(pA.protoNumber, pF1, pF2);
	}

	public static String toXMLString(AlgebraComplexF pA)
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

	/**
	 * When an algebra's number system is examined at this level, the algebra 
	 * knows the DivField class object, the name within that, and the fact that 
	 * floats are being used to represent complex numbers.
	 * 
	 * That's all.
	 */
	public ComplexF	protoNumber;

	public AlgebraComplexF(String pS, Foot pF, GProduct pGP)
	{
		setAlgebraName(pS);
		setFootPoint(pF);
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		protoNumber = new ComplexF(pF.getNumberType(), 1.0f, 0.0f);
	}

	public AlgebraComplexF(String pS, Foot pFoot, String pSig)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(pFoot);
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new ComplexF(pFoot.getNumberType(), 1.0f, 0.0f);
	}

	public AlgebraComplexF(String pS, String pFootName, String pSig, ComplexF pF)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(new Foot(pFootName, pF.getFieldType()));
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new ComplexF(footPoint.getNumberType(), 1.0f, 0.0f);
	}
}
