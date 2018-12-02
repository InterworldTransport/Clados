/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.AlgebraRealD<br>
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
 * ---com.interworldtransport.cladosG.AlgebraRealD<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a algebra over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class AlgebraRealD extends AlgebraAbstract
{
	public static RealD generateNumber(AlgebraRealD pA, double pF)
	{
		return new RealD(pA.protoNumber, pF);
	}

	public static String toXMLString(AlgebraRealD pA)
	{
		StringBuffer rB = new StringBuffer("<Algebra name=\""
						+ pA.getAlgebraName() + "\" ");
		rB.append(">\n");
		rB.append(pA.protoNumber.toXMLString()+"\n");
		rB.append(pA.getFoot().toXMLString());
		rB.append(pA.getGProduct().toXMLString());
		rB.append("</Algebra>\n");
		return rB.toString();
	}

	/**
	 * When an algebra's number system is examined at this level, the algebra 
	 * knows the DivField class object, the name within that, and the fact that 
	 * doubles are being used to represent real numbers.
	 * 
	 * That's all.
	 */
	protected RealD	protoNumber;

	/**
	 * This is the constructor that assumes a Foot and GProduct have been instantiated. 
	 * It takes the RealD number type from the Foot and points at the offered GProduct causing
	 * it to be shared. It takes in one string for the algebra name as well and then produces 
	 * an AlgebraRealD. If anything is wrong with the signature it throws an exception. 
	 * Any other error throws a general monad exception.
	 * 
	 * THIS CONSTRUCTOR is the one that enables algebras to function as light weight frames.
	 * Two algebras can have different names but share a Foot and GProduct and cause reference
	 * matches to fail. This is the behavior necessary to prevent unintended operations between
	 * monads in different frames.
	 * 
	 * @param pS			This is the Algebra's name
	 * @param pF			This is the foot being offered for reference
	 * @param pGP			This is the geometric product being offered for reference
	 */
	public AlgebraRealD(String pS, 
						Foot pF, 
						GProduct pGP)
	{
		setAlgebraName(pS);
		setFoot(pF);
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		protoNumber = new RealD(pF.getNumberType(), 1.0d);
	}

	/**
	 * This is the constructor that assumes a Foot has been instantiated, so it takes
	 * the RealD number type from there. It takes in two strings (one name and a 
	 * product signature) and the Foot and produces an AlgebraRealD. If anything is wrong 
	 * with the signature it throws an exception. Any other error throws a general monad
	 * exception.
	 * 
	 * THIS CONSTRUCTOR is the one that enables algebras to function as medium weight frames.
	 * Two algebras can have different names and GProducts but share a Foot and cause reference
	 * matches to fail. This is the behavior necessary to prevent unintended operations between
	 * monads expressed using different signatures in their geometric products.
	 * 
	 * This is also the one that enables a zero or one generator algebra to be used as a 'number'
	 * in a nyad. Because the Foot is reused, the DivFieldType will match and no field mismatches
	 * will happen. The small algebra can be set up to imitate real or complex numbers and take
	 * on the role of 'scale' in a nyad.
	 * 
	 * @param pS			This is the Algebra's name
	 * @param pFoot			This is the foot being offered for reference
	 * @param pSig			This is the signature of the GProduct
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	public AlgebraRealD(String pS, 
						Foot pFoot, 
						String pSig)
					throws 	BadSignatureException, 
							CladosMonadException
	{
		setAlgebraName(pS);
		setFoot(pFoot);
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealD(pFoot.getNumberType(), 1.0d);
	}

	/**
	 * This is the raw constructor that assumes only the number type has been
	 * instantiated. It takes in three strings (two names and a product signature)
	 * and the example RealD and produces an AlgebraRealD. If anything is wrong with
	 * the signature it throws an exception. Any other error throws a general monad
	 * exception.
	 * 
	 * This is the constructor that ensures algebra reference match failures even
	 * when exact same string names are used to construct all its parts. Because the
	 * Foot object is constructed within, the algebra will be distinct by definition.
	 * 
	 * @param pS			This is the Algebra's name
	 * @param pFootName		This is the Foot's name
	 * @param pSig			This is the signature of the GProduct
	 * @param pF			This is the number type to use expressed as a RealD
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	public AlgebraRealD(String pS, 
						String pFootName, 
						String pSig, 
						RealD pF)
					throws 	BadSignatureException, 
							CladosMonadException
	{
		setAlgebraName(pS);
		setFoot(new Foot(pFootName, pF.getFieldType()));
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealD(foot.getNumberType(), 1.0d);
	}
}