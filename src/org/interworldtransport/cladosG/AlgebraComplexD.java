/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.AlgebraComplexD<br>
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
 * ---org.interworldtransport.cladosG.AlgebraComplexD<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.UUID;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * The algebra object holds all geometric details that support the definition of
 * an algebra over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class AlgebraComplexD extends AlgebraAbstract {
	public final static ComplexD generateNumber(AlgebraComplexD pA, double pF1, double pF2) {
		return new ComplexD(pA.protoNumber, pF1, pF2);
	}

	public final static Cardinal shareCardinal(AlgebraComplexD pA) {
		return pA.protoNumber.getCardinal();
	}

	public final static ComplexD shareProtoNumber(AlgebraComplexD pA) {
		return pA.protoNumber;
	}

	public final static String toXMLString(AlgebraComplexD pA) {
		String indent = "\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Algebra UUID=\"" + pA.uuid + "\" >\n");
		rB.append(indent + "\t<Name>" + pA.getAlgebraName() + "</Name>\n");
		rB.append(indent + "\t" + pA.protoNumber.toXMLString() + "\n");
		// -----------------------------------------------------------------------
		rB.append(indent + "\t<Frames number=\"" + pA.rFrames.size() + "\" >\n");
		for (String tip : pA.rFrames)
			rB.append(indent + "\t\t<Frame number=\"" + pA.rFrames.indexOf(tip) + "\" name=\"" + tip + "\" />\n");
		rB.append(indent + "\t</Frames>\n");
		// -----------------------------------------------------------------------
		rB.append(pA.getFoot().toXMLString("\t\t\t"));
		rB.append(pA.getGProduct().toXMLString());
		rB.append(indent + "</Algebra>\n");
		return rB.toString();
	}

	/**
	 * When an algebra's number system is examined at this level, the algebra knows
	 * the DivField class object, the name within that, and the fact that doubles
	 * are being used to represent complex numbers.
	 * 
	 * That's all.
	 */
	protected ComplexD protoNumber;

	/**
	 * This is the constructor that assumes a full Algebra has already been
	 * constructed. This new one re-uses the objects in the one offered. No
	 * independent objects are made in this constructor except the algebra itself
	 * 
	 * THIS CONSTRUCTOR is one that enables algebras to function as light weight
	 * frames.
	 * 
	 * @param pS This is the Algebra's name
	 * @param pA This is the other Algebra to copy.
	 */
	public AlgebraComplexD(String pS, AlgebraComplexD pA) {
		this(pS, pA.getFoot(), pA.shareCardinal(), pA.getGProduct());
	}

	/**
	 * This is the constructor that assumes a Foot, Cardinal, and GProduct have been
	 * instantiated. It appends the Cardinal to the Foot and points at the offered
	 * GProduct. It takes in one string for the algebra name as well and then
	 * produces the algebra. Nothing can be wrong with the signature since the
	 * GProduct is already constructed.
	 * 
	 * THIS CONSTRUCTOR is the one that most enables algebras to function as light
	 * weight frames. Two algebras can have different names but share everything
	 * else and cause reference matches to fail. The effect is that the canonical
	 * basis in both algebras is the same, but the name differences ensure the
	 * mismatch needed to prevent unphysical operations.
	 * 
	 * @param pS    This is the Algebra's name
	 * @param pF    This is the foot being offered for reference
	 * @param pCard This is the Cardinal to use as a protoNumber
	 * @param pGP   This is the geometric product being offered for reference
	 */
	public AlgebraComplexD(String pS, Foot pF, Cardinal pCard, GProduct pGP) {
		setAlgebraName(pS);
		protoNumber = (ComplexD) CladosField.COMPLEXD.createONE(pCard);
		setFoot(pF);
		foot.appendCardinal(protoNumber.getCardinal());
		setGProduct(pGP);
		gBasis = pGP.getBasis();
		rFrames = new ArrayList<String>(1);
		rFrames.add("canonical");
		uuid = UUID.randomUUID().toString();
	}

	/**
	 * This is the constructor that assumes a Foot has been instantiated, so it
	 * takes the ComplexD number type from there. It takes in two strings (one name
	 * and a product signature) and the Foot and produces an AlgebraComplexD. If
	 * anything is wrong with the signature it throws an exception. Any other error
	 * throws a general monad exception.
	 * 
	 * THIS CONSTRUCTOR is the one that enables algebras to function as medium
	 * weight frames. Two algebras can have different names and GProducts but share
	 * a Foot and cause reference matches to fail. This is the behavior necessary to
	 * prevent unintended operations between monads expressed using different
	 * signatures in their geometric products.
	 * 
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'number' in a nyad. Because the Foot is reused, the Cardinal will match
	 * and no field mismatches will happen. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad.
	 * [This may result in eliminating this class.]
	 * 
	 * @param pS    This is the Algebra's name
	 * @param pF    This is the foot being offered for reference
	 * @param pCard This is the Cardinal to use as a protoNumber
	 * @param pSig  This is the signature of the GProduct
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public AlgebraComplexD(String pS, Foot pF, Cardinal pCard, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, pCard, CladosGBuilder.INSTANCE.createGProduct(pSig));
	}

	/**
	 * This is the constructor that assumes a Foot and DivField have been
	 * instantiated. It takes in two strings (one name and a product signature), the
	 * Foot and Cardinal and produces an Algebra.
	 * 
	 * If anything is wrong with the signature it throws one of two exception,
	 * though both errors can be manifest in the signature string.
	 * 
	 * THIS CONSTRUCTOR is one that enables algebras to function as medium weight
	 * frames. Two algebras can have different names and GProducts but share a Foot
	 * and Cardinal causing reference matches to fail.
	 * 
	 * This is also the one that enables a zero or one generator algebra to be used
	 * as a 'scale' in a nyad. Because a Foot and Cardinal are reused, reference
	 * match tests within a nyad will pass. The small algebra can be set up to
	 * imitate real or complex numbers and take on the role of 'scale' in a nyad if
	 * so desired.
	 * 
	 * @param pS   This is the Algebra's name
	 * @param pF   This is the foot being offered for reference
	 * @param pSig This is the signature of the GProduct
	 * @param pDiv This is the DivField to imitate when the Foot tracks Cardinals
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public AlgebraComplexD(String pS, Foot pF, String pSig, DivField pDiv)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, pF, pDiv.getCardinal(), CladosGBuilder.INSTANCE.createGProduct(pSig));
	}

	/**
	 * This is the raw constructor that assumes only the number type has been
	 * instantiated. It takes in three strings (two names and a product signature)
	 * and the example DivField and produces an Algebra. If anything is wrong with
	 * the signature it throws one of two exceptions.
	 * 
	 * This is the constructor that ensures algebra reference match failures even
	 * when exactly the same string names are used to construct all its parts.
	 * Because the Foot object is constructed within, the algebra will be distinct
	 * by definition.
	 * 
	 * @param pS        This is the Algebra's name
	 * @param pFootName This is the Foot's name
	 * @param pSig      This is the signature of the GProduct
	 * @param pF        This is the number type to use expressed as a DivField
	 * @throws BadSignatureException   This constructor creates a new GProduct which
	 *                                 requires a signature for the generators. This
	 *                                 signature string must be parse-able or this
	 *                                 exception is thrown.
	 * @throws GeneratorRangeException This exception catches when the supported
	 *                                 number of generators is out of range.
	 */
	public AlgebraComplexD(String pS, String pFootName, String pSig, DivField pF)
			throws BadSignatureException, GeneratorRangeException {
		this(pS, CladosGBuilder.createFoot(pFootName, pF.getCardinalString()), pF.getCardinal(),
				CladosGBuilder.INSTANCE.createGProduct(pSig));
	}

	public Cardinal shareCardinal() {
		return protoNumber.getCardinal();
	}

	public ComplexD shareProtoNumber() {
		return protoNumber;
	}
}