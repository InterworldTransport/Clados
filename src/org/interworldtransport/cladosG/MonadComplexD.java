/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.MonadComplexD<br>
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
 * ---org.interworldtransport.cladosG.MonadComplexD<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosFExceptions.*;
import org.interworldtransport.cladosGExceptions.*;
import static org.interworldtransport.cladosF.ComplexD.*;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosFListBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosF.ComplexD;

/**
 * Multivector over a division field {Cl(p,q) x DivField}.
 * <p>
 * Monads encapsulate Clifford algebra multivectors and the rules defining them.
 * The coefficients, reference frame, multiplication rules, and a few names are
 * contained to assist in the proper definition of physical attributes.
 * <p>
 * Proper use of this class is accomplished when one views a physical property
 * as a self-contained entity. A planet's angular momentum is an example of a
 * property that can be fully defined by a Monad if one properly encloses the
 * coefficients, geometric rules, and reference frame within the definition.
 * Enclosing properties in this manner prevents one from making programming
 * errors allowed by the language but disallowed by the physics. An example of
 * such an error would be multiplying two vectors that are defined on different
 * reference systems or in spaces with different signatures.
 * <p>
 * Operations on the Monads are designed to alter the Monad defining the
 * operation. The definitions of addition, multiplication, and all other basic
 * operations are carried within each instance of a Monad. Doing this allows the
 * definitions to vary from one physical object to another. Physical theories
 * requiring manifold curvature will demand this feature. The data members named
 * FtName and FrameFoot are initial attempts to support curvature.
 * <p>
 * Monad objects must be declared with at least one generator of geometry.
 * Properties not requiring a generator of geometry may be adequately defined on
 * Fields and are not intended to be covered in the clados package.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class MonadComplexD extends MonadAbstract {

	/**
	 * Return true if more the monad is a ZERO scalar.
	 * 
	 * @param pM MonadComplexD This is the monad to be tested.
	 * 
	 * @return boolean
	 */
	public static boolean isGZero(MonadComplexD pM) {
		return (pM.getGradeKey() == 1 & isZero(pM.getCoeff(0)));
	}

	/**
	 * Return true if the Monad an idempotent
	 * 
	 * @return boolean
	 * @param pM MonadComplexD
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldBinaryException This exception is thrown when the method can't
	 *                              multiply two fields used by the monad to be
	 *                              checked.
	 */
	public static boolean isIdempotent(MonadComplexD pM) throws FieldBinaryException, CladosMonadException {
		if (isGZero(pM))
			return true;
		return ((MonadComplexD) CladosGMonad.COMPLEXD.copyOf(pM)).multiplyLeft(pM).isGEqual(pM);
	}

	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * 
	 * @return boolean
	 * @param pM MonadComplexD
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldException       This exception is thrown when the method can't
	 *                              copy the field used by the monad to be checked.
	 */
	public static boolean isScaledIdempotent(MonadComplexD pM) throws CladosMonadException, FieldException {
		if (isIdempotent(pM))
			return true;

		// Find the first non-zero coefficient of pM^2, invert it, rescale pM by it and
		// that to see if it
		// is an idempotent that way. If it is, then pM is an idempotent multiple.

		int k = 1;
		MonadComplexD check1 = (MonadComplexD) CladosGMonad.COMPLEXD.copyOf(pM);
		check1.multiplyLeft(pM); // We now have check1 = pM ^ 2
		if (isGZero(check1))
			return false; // pM is nilpotent at power=2
		ComplexD fstnzeroC = (ComplexD) CladosFBuilder.COMPLEXD.copyOf(pM.getCoeff(0)); // Grab copy of Scalar part

		while (isZero(fstnzeroC) & k <= pM.getAlgebra().getBladeCount() - 1) { // Loop skipped if check1.SP() != 0
			if (!isZero(pM.getCoeff(k))) {
				fstnzeroC = copyOf(pM.getCoeff(k)); // Grab a copy of it instead
				break; // Good enough. Move on.
			}
			k++; // If next coeff is zero, look at next next
		}
		check1 = ((MonadComplexD) CladosGMonad.COMPLEXD.copyOf(pM)).scale(fstnzeroC.invert());
		// No risk of inverting a zero. Caught at top
		return isIdempotent(check1);
	}

	/**
	 * Return true if the Monad is nilpotent at a particular integer power.
	 * 
	 * @return boolean
	 * @param pM     MonadComplexD The monad to be tested
	 * @param pPower int The integer power to test
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldBinaryException This exception is thrown when the method can't
	 *                              multiply two fields used by the monad to be
	 *                              checked.
	 */
	public static boolean isNilpotent(MonadComplexD pM, int pPower) throws FieldBinaryException, CladosMonadException {
		if (isGZero(pM))
			return true;

		MonadComplexD check1 = new MonadComplexD(pM);
		while (pPower > 1) {
			check1.multiplyLeft(pM);
			if (isGZero(check1))
				return true;
			pPower--;
		}
		return false;
	}

	/**
	 * Return true if the Monad shares the same Reference frame as the passed Monad.
	 * A check is made on frameName, FootName, Signature, and FrameFoot for
	 * equality. No check is made for equality between Mnames and Coeffs and the
	 * product Table
	 * 
	 * @param pM MonadAbstract
	 * @param pN MonadAbstract
	 * @return boolean
	 */
	public static boolean isReferenceMatch(MonadComplexD pM, MonadComplexD pN) {
		// The algebras must actually be the same object to match.
		if ((pM.getAlgebra() != (pN.getAlgebra())))
			return false;

		// The frame names must match too
		else if (!pM.getFrameName().equals(pN.getFrameName()))
			return false;

		// There is a possibility that the coefficients are of different field
		// types but that is unlikely if the algebras match. The problem is that
		// someone can write new coefficients and break the consistency with the
		// Algebra's protonumber.
		else if (!pM.getCoeff(0).getCardinal().equals(pN.getCoeff(0).getCardinal()))
			return false;

		return true;
	}

	/**
	 * Display XML string that represents the Monad
	 * 
	 * @param pM MonadComplexD This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * 
	 * @return String
	 */
	public static String toXMLString(MonadComplexD pM, String indent) {
		if (indent == null)
			indent = "\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Monad ");
		rB.append("algebra=\"").append(pM.getAlgebra().getAlgebraName()).append("\" ");
		rB.append("frame=\"").append(pM.getFrameName()).append("\" ");
		rB.append("gradeKey=\"").append(pM.getGradeKey()).append("\" ");
		rB.append("sparseFlag=\"").append(pM.getSparseFlag()).append("\" ");
		rB.append(">\n");
		rB.append(indent + "\t<Name>").append(pM.getName()).append("</Name>\n");
		rB.append(indent).append(pM.scales.toXMLString("\t"));
		rB.append(indent + "</Monad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Monad
	 * 
	 * @param pM     MonadComplexD This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * 
	 * @return String
	 */
	public static String toXMLFullString(MonadComplexD pM, String indent) {
		if (indent == null)
			indent = "\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Monad ");
		rB.append("gradeKey=\"").append(pM.getGradeKey()).append("\" ");
		rB.append("sparseFlag=\"").append(pM.getSparseFlag()).append("\" ");
		rB.append(">\n");
		rB.append(indent + "\t<Name>").append(pM.getName()).append("</Name>\n");
		rB.append(Algebra.toXMLString(pM.getAlgebra(), indent + "\t"));
		rB.append(indent + "\t<Frame>\"").append(pM.getFrameName()).append("\"</Frame>\n");
		rB.append(indent).append(pM.scales.toXMLString("\t"));
		rB.append(indent + "</Monad>\n");
		return rB.toString();
	}

	/**
	 * This array holds the coefficients of the Monad.
	 */
	//protected ComplexD[] cM;

	/**
	 * Simple copy constructor of Monad. Passed Monad will be copied in all details.
	 * This contructor is used most often to get around operations that alter a
	 * Monad when the developer does not wish it to be altered.
	 * 
	 * @param pM MonadComplexD
	 */
	public MonadComplexD(MonadComplexD pM) {
		setName(pM.getName());
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());
		mode = pM.mode;
		scales = new Scale<ComplexD>(CladosField.COMPLEXD, this.getAlgebra().getGBasis(), pM.getScales().getCardinal());
		scales.setCoefficientMap(pM.scales.getMap());
		setGradeKey();
	}

	/**
	 * Main copy constructor of Monad. Passed Monad will be copied in all details
	 * except its name. This constructor is used most often as a starting point to
	 * generate new Monads based on an old one.
	 * 
	 * @param pName String
	 * @param pM    MonadComplexD
	 * @throws BadSignatureException This exception is thrown if the signature
	 *                               string offered is rejected.
	 * @throws CladosMonadException  This exception is thrown if there is an issue
	 *                               with the coefficients offered. The issues could
	 *                               involve null coefficients or a coefficient
	 *                               array of the wrong size.
	 */
	public MonadComplexD(String pName, MonadComplexD pM) throws CladosMonadException {
		this(pM);
		setName(pName);
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create the default 'Zero' Monad.
	 * 
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           DivField Used to construct a ComplexD
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName, String pFrameName, String pFootName, String pSig,
			DivField pF) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		setAlgebra(CladosGAlgebra.COMPLEXD.create(pF, pAlgebraName, pFootName, pSig));//proto is ComplexD
		setName(pMonadName);
		setFrameName(pFrameName);
		mode = CladosField.COMPLEXD;
		scales = new Scale<ComplexD>(CladosField.COMPLEXD, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
		setGradeKey();
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create a default 'Zero' Monad while re-using the Foot of another.
	 * 
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFoot        Foot
	 * @param pSig         String
	 * @param pF           ComplexD
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName, String pFrameName, Foot pFoot, String pSig,
			DivField pF) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		setAlgebra(CladosGAlgebra.COMPLEXD.createWithFoot(pFoot, pF, pAlgebraName, pSig));
		setName(pMonadName);
		setFrameName(pFrameName);
		mode = CladosField.COMPLEXD;
		scales = new Scale<ComplexD>(CladosField.COMPLEXD, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
		setGradeKey();
	}

	/**
	 * Special constructor of Monad with most information passed in. 'Special Case'
	 * strings determine the coefficients automatically. 'Unit Scalar' and 'Unit
	 * PScalar' are recognized special cases. All unrecognized strings create a
	 * 'Zero' Monad by default.
	 * 
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           ComplexD
	 * @param pSpecial     String
	 * @throws BadSignatureException    This exception is thrown if the signature
	 *                                  string offered is rejected.
	 * @throws CladosMonadException     This exception is thrown if there is an
	 *                                  issue with the coefficients offered the
	 *                                  default constructor. The issues could
	 *                                  involve null coefficients or a coefficient
	 *                                  array of the wrong size.
	 * @throws GeneratorRangeException  This exception is thrown when the integer
	 *                                  number of generators for the basis is out of
	 *                                  the supported range. {0, 1, 2, ..., 14}
	 *                                  return MonadComplexD
	 * @throws GradeOutOfRangeException This method is thrown if the special case
	 *                                  handler has issues with the grade implied in
	 *                                  the case. It really shouldn't happen, but
	 *                                  might if someone tinkers with the case in an
	 *                                  unsafe way.
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName, String pFrameName, String pFootName, String pSig,
			DivField pF, String pSpecial)
			throws BadSignatureException, GeneratorRangeException, CladosMonadException, GradeOutOfRangeException {
		this(pMonadName, pAlgebraName, pFrameName, pFootName, pSig, pF);
		// Default ZERO Monad is constructed already.
		// Now handle the special cases and make adjustments to the cM array.

		if (CladosConstant.MONAD_SPECIAL_CASES.contains(pSpecial)) {
			switch (pSpecial) {
			case "Unit Scalar" -> ((ComplexD) scales.getScalar()).setReal(CladosConstant.PLUS_ONE_F);
			case "Unit -Scalar" -> ((ComplexD) scales.getScalar()).setReal(CladosConstant.MINUS_ONE_F);
			case "Unit PScalar" -> ((ComplexD) scales.getPScalar()).setReal(CladosConstant.PLUS_ONE_F);
			case "Unit -PScalar" -> ((ComplexD) scales.getPScalar()).setReal(CladosConstant.MINUS_ONE_F);
			}
		} // failure to find matching special case defaults ZERO monad by doing nothing.
		setGradeKey();
	}

	/**
	 * Main constructor of Monad with all information passed in.
	 * 
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pC           ComplexD[]
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName, String pFrameName, String pFootName, String pSig,
			ComplexD[] pC) throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		if (pC == null | pC[0] == null)
			throw new CladosMonadException(this, "Missing coefficients.");
		setAlgebra(CladosGAlgebra.COMPLEXD.create(pC[0], pAlgebraName, pFootName, pSig));
		if (pC.length != getAlgebra().getBladeCount())
			throw new CladosMonadException(this, "Coefficient array size does not match bladecount for Signature.");
		setName(pMonadName);
		setFrameName(pFrameName);
		mode = CladosField.COMPLEXD;
		scales = new Scale<ComplexD>(CladosField.COMPLEXD, this.getAlgebra().getGBasis(), pC[0].getCardinal());
		scales.setCoefficientArray(CladosFListBuilder.copyOf(scales.getMode(), pC));
		setGradeKey();
	}

	/**
	 * Main constructor of Monad with pre-constructed objects not already part of
	 * another Monad..
	 * 
	 * @param pMonadName String
	 * @param pAlgebra   AlgebraComplexD
	 * @param pFrameName String
	 * @param pC         ComplexD[]
	 * @throws CladosMonadException This exception is thrown if there is an issue
	 *                              with the coefficients offered. The issues could
	 *                              involve null coefficients or a coefficient array
	 *                              of the wrong size.
	 */
	public MonadComplexD(String pMonadName, Algebra pAlgebra, String pFrameName, ComplexD[] pC)
			throws CladosMonadException {
		if (pC.length != pAlgebra.getBladeCount())
			throw new CladosMonadException(this,
					"Coefficient array size does not match bladecount from offered Algebra.");
		setAlgebra(pAlgebra);
		setName(pMonadName);
		setFrameName(pFrameName);
		mode = CladosField.COMPLEXD;
		scales = new Scale<ComplexD>(CladosField.COMPLEXD, this.getAlgebra().getGBasis(), pC[0].getCardinal());
		scales.setCoefficientArray(CladosFListBuilder.copyOf(scales.getMode(), pC));
		setGradeKey();
	}

	/**
	 * Monad Addition: (this + pM) This operation is allowed when the two monads use
	 * the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM MonadComplexD
	 * @throws FieldBinaryException       This exception is thrown when the method
	 *                                    can't multiply two fields used by the
	 *                                    monad to be checked.
	 * @throws CladosMonadBinaryException This exception is thrown when the monads
	 *                                    fail a reference match.
	 * 
	 * @return MonadComplexD
	 */
	public MonadComplexD add(MonadComplexD pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Can't add when frames don't match.", pM);
		bladeStream().parallel().forEach(blade -> {
			try {
				scales.get(blade).add(pM.scales.get(blade));
			} catch (FieldBinaryException e) {
				throw new IllegalArgumentException("Can't add when cardinals don't match.");
			}
		});
		setGradeKey();
		return this;
	}

	/**
	 * This method conjugates all the coefficients, but leaves the generators of the
	 * algebra untouched
	 */
	@Override
	public MonadComplexD conjugate() {
		scales.conjugate();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with lefttside multiplication by pscalar.
	 */
	@Override
	public MonadComplexD dualLeft() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		int row = tBasis.getBladeCount() - 1; // row points at the PScalar blade
		Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();
		getScales().getMap().entrySet().stream().filter(e -> !ComplexD.isZero((ComplexD) e.getValue())).parallel()
				.forEach(e -> {
					int col = tBasis.find(e.getKey()) - 1;
					Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
					try {
						ComplexD tAgg = ((ComplexD) CladosFBuilder.COMPLEXD.copyOf((ComplexD) e.getValue()))
								.scale(Double.valueOf(tProd.getSign(row, col)))
								.add(newScales.get(bMult));
						newScales.put(bMult, tAgg);
					} catch (FieldBinaryException e1) {
						throw new IllegalArgumentException("Left dual fails DivField reference match.");
					}
				});
		scales = newScales;
		setGradeKey();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with rightside multiplication by pscalar.
	 */
	@Override
	public MonadComplexD dualRight() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		int row = tProd.getBladeCount() - 1; // row points at the PScalar blade
		Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();
		getScales().getMap().entrySet().stream().filter(e -> !ComplexD.isZero((ComplexD) e.getValue())).parallel()
				.forEach(e -> {
					int col = tBasis.find(e.getKey()) - 1;
					Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
					try {
						ComplexD tAgg = ((ComplexD) CladosFBuilder.COMPLEXD.copyOf((ComplexD) e.getValue()))
								.scale(Double.valueOf(tProd.getSign(col, row)))
								.add(newScales.get(bMult));
						newScales.put(bMult, tAgg);
					} catch (FieldBinaryException e1) {
						throw new IllegalArgumentException("Right dual fails DivField reference match.");
					}
				});
		scales = newScales;
		setGradeKey();
		return this;
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @return ComplexD[]
	 */
	@Override
	public ComplexD[] getCoeff() {
		return (ComplexD[]) scales.getCoefficients();
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @param i int This points at the coefficient at the equivalent tuple location.
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD getCoeff(int i) {
		if (i >= 0 & i < getAlgebra().getBladeCount())
			return (ComplexD) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		return null;
	}

	/**
	 * This method suppresses all grades in the Monad not equal to the integer
	 * passed. Example: The Scalar Part operation is performed by calling
	 * GradePart(0)
	 * 
	 * If the grade to be preserved is not within the gradeRange of this monad this
	 * method silently fails. No suppression occurs.
	 * 
	 * @param pGrade byte
	 */
	@Override
	public MonadComplexD gradePart(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAllButGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * This method suppresses the grade in the Monad equal to the integer passed.
	 * Example: Suppression of the bivector part of a Monad is performed by calling
	 * gradeSupress(2)
	 * 
	 * If the grade to be suppressed is not within the gradeRange of this monad this
	 * method silently fails. No suppression occurs.
	 * 
	 * @param pGrade byte
	 */
	@Override
	public MonadComplexD gradeSuppress(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAtGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * Mirror the sense of all geometry generators in the Monad. This operation
	 * alters all grades other than scalar. In some grades the affects cancel out.
	 * Active Invert: All odd grades switch signs, so those coefficients are
	 * altered.
	 */
	@Override
	public MonadComplexD invert() {
		scales.invert();
		return this;
	}

	/**
	 * This method does a deep check for the equality of two monads. It is not meant
	 * for checking that two monad references actually point to the same object
	 * since that is easily handled with ==. This one checks algebras, foot names,
	 * frame names and product definitions along with the coefficients. Each object
	 * owned by the monad has its own isEqual() method that gets called.
	 * 
	 * @param pM MonadAbstract
	 * @return boolean
	 */
	public boolean isGEqual(MonadComplexD pM) {
		if (!isReferenceMatch(this, pM))
			return false;
		return bladeStream().allMatch(blade -> ComplexD.isEqual((ComplexD) scales.get(blade), (ComplexD) pM.scales.get(blade)));
	}

	/**
	 * Return the magnitude of the Monad
	 * 
	 * @return ComplexD
	 * @throws CladosMonadException This exception is possible because magnitudes
	 *                              are build from sqMagnitudes. That means there is
	 *                              an intermediate multiplication steps that could
	 *                              cause a FieldBinaryException, but never should.
	 *                              If this exception gets thrown here there is
	 *                              something seriously amiss with magnitude() and
	 *                              sqMagnitude().
	 */
	@Override
	public ComplexD magnitude() throws CladosMonadException {
		try {
			return (ComplexD) scales.magnitude();
		} catch (FieldBinaryException e) {
			throw new CladosMonadException(this, "Coefficients of Monad must be from the same field.");
		}
	}

	/**
	 * Monad antisymmetric multiplication: 1/2(pM this - this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM MonadComplexD
	 * @return MonadComplexD
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @throws CladosMonadException 
	 */
	public MonadComplexD multiplyAntisymm(MonadComplexD pM) throws FieldBinaryException, CladosMonadException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Symm multiply fails reference match.", pM);

		MonadComplexD halfTwo = ((MonadComplexD) CladosGMonad.COMPLEXD.copyOf(this)).multiplyRight(pM);
		multiplyLeft(pM).subtract(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		setGradeKey();
		return this;
	}

	/**
	 * Monad leftside multiplication: (pM this) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * 
	 * @param pM MonadComplexD
	 * @throws CladosMonadBinaryException This exception is thrown when the
	 *                                    reference match test fails with the two
	 *                                    monads
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @return MonadComplexD
	 */
	public MonadComplexD multiplyLeft(MonadComplexD pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Left multiply fails reference match.", pM);
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();

		if (sparseFlag) {
			/*
			 * Use gradeKey (a base 10 representation of grades present) to find the
			 * non-zero grades. For example: gradeKey=101 means the monad is a sum of
			 * bivector and scalar because 10^2+10^0 = 101.
			 * 
			 * In a sparse monad, the gradeKey will have few 1's, making looping on all
			 * blades less optimal. Instead, we parse gradeKey and loop through the blades
			 * for grades that could be non-ZERO.
			 */
			long slideKey = gradeKey;
			byte logKey = (byte) Math.log10(slideKey);

			// logKey is the highest grade with non-zero blades
			// tSpot will point at the blades of that grade
			while (logKey >= 0) {
				tBasis.bladeOfGradeStream(logKey).filter(blade -> !ComplexD.isZero((ComplexD) getScales().get(blade)))
						.parallel().forEach(blade -> {
							int col = tBasis.find(blade) - 1;
							pM.getScales().getMap().entrySet().stream()
									.filter(f -> !ComplexD.isZero((ComplexD) f.getValue())).forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
										try {
											ComplexD tAgg = ComplexD
													.multiply((ComplexD) getScales().get(blade), (ComplexD) f.getValue())
													.scale(Double.valueOf(tProd.getSign(row, col)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e) {
											throw new IllegalArgumentException(
													"Left multiply fails DivField reference match.");
										}
									});
						});
				slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
				if (slideKey == 0)
					break; // we've processed all grades including scalar.
				logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
			}
			scales = newScales;
		} else { // loop through ALL the blades in 'this' individually.
			getScales().getMap().entrySet().stream().filter(e -> !ComplexD.isZero((ComplexD) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						pM.getScales().getMap().entrySet().stream().filter(f -> !ComplexD.isZero((ComplexD) f.getValue()))
								.forEach(f -> {
									int row = tBasis.find(f.getKey()) - 1;
									Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
									try {
										ComplexD tAgg = ComplexD.multiply((ComplexD) e.getValue(), (ComplexD) f.getValue())
												.scale(Double.valueOf(tProd.getSign(row, col)))
												.add(newScales.get(bMult));
										newScales.put(bMult, tAgg);
									} catch (FieldBinaryException e1) {
										throw new IllegalArgumentException(
												"Left multiply fails DivField reference match.");
									}
								});
					});
			scales = newScales;
		}
		setGradeKey();
		return this;
	}

	/**
	 * Monad rightside multiplication: (pM this) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * 
	 * @param pM MonadComplexD
	 * @throws CladosMonadBinaryException This exception is thrown when the
	 *                                    reference match test fails with the two
	 *                                    monads
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @return MonadComplexD
	 */
	public MonadComplexD multiplyRight(MonadComplexD pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!isReferenceMatch(this, pM)) // Don't try if not a reference match
			throw new CladosMonadBinaryException(this, "Right multiply fails reference match.", pM);
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();

		if (sparseFlag) {
			/*
			 * Use gradeKey (a base 10 representation of grades present) to find the
			 * non-zero grades. For example: gradeKey=101 means the monad is a sum of
			 * bivector and scalar because 10^2+10^0 = 101.
			 * 
			 * In a sparse monad, the gradeKey will have few 1's, making looping on all
			 * blades less optimal. Instead, we parse gradeKey and loop through the blades
			 * for grades that could be non-ZERO.
			 */
			long slideKey = gradeKey;
			byte logKey = (byte) Math.log10(slideKey);

			// logKey is the highest grade with non-zero blades
			// tSpot will point at the blades of that grade
			while (logKey >= 0.0D) {
				tBasis.bladeOfGradeStream(logKey).filter(blade -> !ComplexD.isZero((ComplexD) getScales().get(blade)))
						.parallel().forEach(blade -> {
							int col = tBasis.find(blade) - 1;
							pM.getScales().getMap().entrySet().stream().filter(f -> !ComplexD.isZero((ComplexD) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
										try {
											ComplexD tAgg = ComplexD
													.multiply((ComplexD) getScales().get(blade), (ComplexD) f.getValue())
													.scale(Double.valueOf(tProd.getSign(col, row)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e) {
											throw new IllegalArgumentException(
													"Right multiply fails DivField reference match.");
										}
									});
						});
				slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
				if (slideKey == 0)
					break; // we've processed all grades including scalar.
				logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
			}
			scales = newScales;
		} else { // loop through ALL the blades in 'this' individually.
			getScales().getMap().entrySet().stream().filter(e -> !ComplexD.isZero((ComplexD) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						pM.scales.getMap().entrySet().stream().filter(f -> !ComplexD.isZero((ComplexD) f.getValue()))
								.forEach(f -> {
									int row = tBasis.find(f.getKey()) - 1;
									Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
									try {
										ComplexD tAgg = ComplexD.multiply((ComplexD) e.getValue(), (ComplexD) f.getValue())
												.scale(Double.valueOf(tProd.getSign(col, row)))
												.add(newScales.get(bMult));
										newScales.put(bMult, tAgg);
									} catch (FieldBinaryException e1) {
										throw new IllegalArgumentException(
												"Right multiply fails DivField reference match.");
									}
								});
					});
			scales = newScales;
		}
		setGradeKey();
		return this;
	}

	/**
	 * Monad symmetric multiplication: 1/2(pM this + this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM MonadComplexD
	 * @return MonadComplexD
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @throws CladosMonadException 
	 */
	public MonadComplexD multiplySymm(MonadComplexD pM) throws FieldBinaryException, CladosMonadException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Symm multiply fails reference match.", pM);

		MonadComplexD halfTwo = ((MonadComplexD) CladosGMonad.COMPLEXD.copyOf(this)).multiplyRight(pM);
		multiplyLeft(pM).add(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		setGradeKey();
		return this;
	}

	/**
	 * Normalize the monad. A <b>CladosMonadException</b> is thrown if the Monad has
	 * a zero magnitude.
	 * 
	 * @throws CladosMonadException This exception is thrown when normalizing a zero
	 *                              or field conflicted monad is tried.
	 */
	@Override
	public MonadComplexD normalize() throws CladosMonadException {
		if (gradeKey == 0L & ComplexD.isZero((ComplexD) scales.getScalar()))
			throw new CladosMonadException(this, "Normalizing a zero magnitude Monad isn't possible");
		try {
			scales.normalize();
		} catch (FieldException e) {
			throw new CladosMonadException(this,
					"Normalizing a zero magnitude or Field conflicted Monad isn't possible");
		}
		return this;
	}

	/**
	 * This method is a concession to the old notation for the Pseudo Scalar Part of
	 * a monad. It returns the pscalar part coefficient.
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD PSPc() {
		return (ComplexD) scales.getPScalar();
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of all
	 * the permutation, so the easiest thing to do is to change the coefficients
	 * instead.
	 */
	@Override
	public MonadComplexD reverse() {
		scales.reverse();
		return this;
	}

	/**
	 * Monad Scaling: (this * real number) Only the Monad coefficients are scaled by
	 * the real number.
	 * 
	 * @param pScale ComplexD
	 * @throws FieldBinaryException This exception is thrown when the scale field
	 *                              fails a field match with the coefficients.
	 * @return MonadComplexD
	 */
	public MonadComplexD scale(ComplexD pScale) throws FieldBinaryException {
		scales.scale(pScale);
		setGradeKey();
		return this;
	}

	/**
	 * Reset the Coefficient array used for this Monad. Use of this method is
	 * discouraged, but occasionally necessary. The ideal way of setting up the
	 * coefficient array is to build a new Monad with the new coefficient array.
	 * Using this set method encourages developers to reuse old objects. While this
	 * is useful for avoiding object construction overhead, it is dangerous in that
	 * the old meaning of the object might linger in the various name attributes.
	 * Caution is advised if this method is used while frequent reuse should be
	 * considered bad form.
	 * 
	 * @param ppC ComplexD[]
	 * @throws CladosMonadException This exception is thrown when the array offered
	 *                              for coordinates is of the wrong length.
	 */
	public void setCoeff(ComplexD[] ppC) throws CladosMonadException {
		if (ppC.length != getAlgebra().getBladeCount())
			throw new CladosMonadException(this,
					"Coefficient array passed in for coefficient copy is the wrong length");
		scales.setCoefficientArray((ComplexD[]) CladosFListBuilder.COMPLEXD.copyOf(ppC));
		setGradeKey();
	}

	/**
	 * Set the grade key for the monad. Never accept an externally provided key.
	 * Always recalculate it after any of the unary or binary operations.
	 * 
	 * While we are here, we ALSO set the sparseFlag. The nonZero coeff detection
	 * loop that fills gradeKey is a grade detector, so if foundGrade is less than
	 * or equal to half gradeCount, sparseFlag is set to true and false otherwise.
	 */
	@Override
	public void setGradeKey() {
		foundGrades = 0;
		gradeKey = 0;
		gradeStream().forEach(grade -> {
			if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
					.filter(blade -> !ComplexD.isZero((ComplexD) scales.get(blade))).findAny().isPresent()) {
				foundGrades++;
				gradeKey += Math.pow(10, grade);
			}
		});
		if (gradeKey == 0) {
			foundGrades++;
			gradeKey++;
		}
		if (foundGrades < getAlgebra().getGradeCount() / 2)
			sparseFlag = true;
		else
			sparseFlag = false;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD SPc() {
		return (ComplexD) scales.getScalar();
	}

	/**
	 * Return the magnitude squared of the Monad
	 * 
	 * @throws CladosMonadException This exception is thrown when the monad's
	 *                              coefficients aren't in the same field. This
	 *                              should be caught during monad construction, but
	 *                              field coefficients are references so there is
	 *                              always a chance something will happen to alter
	 *                              the object referred to in a list of
	 *                              coefficients.
	 * @return ComplexD
	 */
	@Override
	public ComplexD sqMagnitude() throws CladosMonadException {
		try {
			return (ComplexD) scales.sqMagnitude();
		} catch (FieldBinaryException e) {
			throw new CladosMonadException(this, "Coefficients of Monad must be from the same field.");
		}
	}

	/**
	 * Monad Subtraction: (this - pM) This operation is allowed when the two monads
	 * use the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM MonadComplexD
	 * @throws FieldBinaryException       This exception is thrown when the method
	 *                                    can't multiply two fields used by the
	 *                                    monad to be checked.
	 * @throws CladosMonadBinaryException This exception is thrown when the monads
	 *                                    fail a reference match.
	 * 
	 * @return MonadComplexD
	 */
	public MonadComplexD subtract(MonadComplexD pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Can't subtract without a reference match.", pM);
		bladeStream().parallel().forEach(blade -> {
			try {
				scales.get(blade).subtract(pM.scales.get(blade));
			} catch (FieldBinaryException e) {
				throw new IllegalArgumentException("Can't subtract when cardinals don't match.");
			}
		});
		setGradeKey();
		return this;
	}
}