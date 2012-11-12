/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosG.AlgebraRealD<br>
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
 * <br> ---com.interworldtransport.cladosG.AlgebraRealD<br>
 * ------------------------
 * --------------------------------------------------------
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.BadSignatureException;
import com.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * The algebra object holds all geometric details that support the definition of
 * a algebra over a division field {Cl(p,q) x DivField}.
 * 
 * @version 0.90, $Date$
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
		rB.append(pA.getFootPoint().toXMLString());
		rB.append(pA.getGProduct().toXMLString());
		rB.append("\n</Algebra>\n");
		return rB.toString();
	}

	public RealD	protoNumber;

	public AlgebraRealD(String pS, Foot pF, GProduct pGP)
	{
		setAlgebraName(pS);
		setFootPoint(pF);
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		protoNumber = new RealD(pF.getNumberType(), 1.0d);
	}

	public AlgebraRealD(String pS, Foot pFoot, String pSig)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(pFoot);
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealD(pFoot.getNumberType(), 1.0d);
	}

	public AlgebraRealD(String pS, String pFootName, String pSig, RealD pF)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebraName(pS);
		setFootPoint(new Foot(pFootName, pF.getFieldType()));
		setGProduct(new GProduct(pSig));
		gBasis = gProduct.getBasis();
		protoNumber = new RealD(footPoint.getNumberType(), 1.0d);
	}
}
