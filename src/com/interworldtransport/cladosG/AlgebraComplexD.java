/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosG.AlgebraComplexD<br>
 * ------
 * --------------------------------------------------------------------------
 * <p> Interworld Transport grants you ("Licensee") a license to this software
 * under the terms of the GNU General Public License.<br> A full copy of the
 * license can be found bundled with this package or code file. <p> If the
 * license file has become separated from the package, code file, or binary
 * executable, the Licensee is still expected to read about the license at the
 * following URL before accepting this material.
 * <blockquote><code>http://www.opensource
 * .org/gpl-license.html</code></blockquote> <p> Use of this code or executable
 * objects derived from it by the Licensee states their willingness to accept
 * the terms of the license. <p> A prospective Licensee unable to find a copy of
 * the license terms should contact Interworld Transport for a free copy. <p>
 * ----
 * ----------------------------------------------------------------------------
 * <br> ---com.interworldtransport.cladosG.AlgebraComplexD<br>
 * ------------------------
 * --------------------------------------------------------
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class AlgebraComplexD extends AlgebraAbstract
{
	public static ComplexD generateNumber(AlgebraComplexD pA, double pF1, double pF2)
	{
		return new ComplexD(pA.protoNumber, pF1, pF2);
	}

	public static String toXMLString(AlgebraComplexD pA)
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

	public ComplexD	protoNumber;

	public AlgebraComplexD(String pS, Foot pF, GProduct pGP)
	{
		setAlgebraName(pS);
		setFootPoint(pF);
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		protoNumber = new ComplexD(pF.getNumberType(), 1.0d, 0.0d);
	}

	public AlgebraComplexD(String pS, Foot pFoot, String pSig)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(pFoot);
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new ComplexD(pFoot.getNumberType(), 1.0d, 0.0d);
	}

	public AlgebraComplexD(String pS, String pFootName, String pSig, ComplexD pF)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(new Foot(pFootName, pF.getFieldType()));
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new ComplexD(footPoint.getNumberType(), 1.0d, 0.0d);
	}
}
