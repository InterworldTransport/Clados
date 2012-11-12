/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosG.MonadComplexD<br>
 * ----
 * ----------------------------------------------------------------------------
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
 * <br> ---com.interworldtransport.cladosG.MonadComplexD<br>
 * --------------------
 * ------------------------------------------------------------
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosGExceptions.*;
import com.interworldtransport.cladosF.ComplexD;
import com.interworldtransport.cladosFExceptions.*;
import static com.interworldtransport.cladosF.ComplexD.*;

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
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 */
public class MonadComplexD extends MonadAbstract
{
	/**
	 * Return true if more than one blade is present in the Monad. This method
	 * makes use of the grade key which is a sum of powers of 10, thus the
	 * base-10 logarithm will be an integer for pure grade monads and a
	 * non-integer for multigrade monads.
	 * 
	 * @return boolean
	 */
	public static boolean isGZero(MonadComplexD pM)
	{
		if (pM.getGradeKey() == 1 & isZero(pM.getCoeff(0)))
			return true;
		else
			return false;
	}

	/**
	 * Return true if the Monad an idempotent
	 * 
	 * @return boolean
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public static boolean isIdempotent(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadException
	{
		if (isGZero(pM)) return true;
		MonadComplexD check1 = new MonadComplexD(pM);
		check1.multiplyLeft(pM);
		if (check1.isGEqual(pM)) return true;
		return false;
	}

	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * 
	 * @return boolean
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldException
	 */
	public static boolean isIdempotentMultiple(MonadComplexD pM)
					throws CladosMonadException, FieldException
	{
		if (isGZero(pM)) return true;
		if (isIdempotent(pM)) return true;
		if (isNilpotent(pM)) return false;

		// What we want is to square 'this', find the first non-zero
		// coefficient, and rescale the original 'this' to see if it
		// would be an idempotent that way. If it is, then the original
		// 'this' is an idempotent multiple.

		MonadComplexD check1 = new MonadComplexD(pM);
		check1.multiplyLeft(pM);

		int k = 0;
		ComplexD fstnzeroC = ZERO("Place Holder");
		while (isZero(fstnzeroC)
						& k <= pM.getAlgebra().getGProduct().getBladeCount() - 1)
		{
			fstnzeroC = copy(pM.getCoeff(k));
			k++;
		}

		// check1 = new MonadRealD(this);
		fstnzeroC.invert();
		check1.scale(fstnzeroC); // monad scaling is does with RealD's and not
									// doubles.

		if (check1.isGEqual(pM)) return true;
		return false;
	}

	/**
	 * Return true if the Monad is nilpotent
	 * 
	 * @return boolean
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public static boolean isNilpotent(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadException
	{
		if (isGZero(pM)) return true;
		MonadComplexD check1 = new MonadComplexD(pM);
		check1.multiplyLeft(pM);
		if (isGZero(check1)) return true;
		return false;
	}

	/**
	 * Return true if the Monad shares the same Reference frame as the passed
	 * Monad. A check is made on frameName, FootName, Signature, and FrameFoot
	 * for equality. No check is made for equality between Mnames and Coeffs and
	 * the product Table
	 * 
	 * @param pM
	 *            MonadAbstract
	 * @param pN
	 *            MonadAbstract
	 * @return boolean
	 */
	public static boolean isReferenceMatch(MonadComplexD pM, MonadComplexD pN)
	{
		// The algebras must actually be the same object to match.
		if (!pM.getAlgebra().equals(pN.getAlgebra())) return false;

		// There is a possibility that the coefficients are of different field
		// types but that is unlikely if the algebras match. The problem is that
		// someone can write new coefficients and break the consistency with the
		// Algebra's protonumber.
		if (!pM.getCoeff(0).getFieldType()
						.equals(pN.getCoeff(0).getFieldType())) return false;

		if (!pM.getFrameName().equals(pN.getFrameName())) return false;
		return true;
	}

	/**
	 * Display XML string that represents the Monad
	 * 
	 * @return String
	 */
	public static String toXMLString(MonadComplexD pM)
	{
		StringBuffer rB = new StringBuffer("<Monad name=\"" + pM.getName()
						+ "\" ");
		rB.append("algebra=\"" + pM.getAlgebra().getAlgebraName() + "\" ");
		rB.append("frame=\"" + pM.getFrameName() + "\" ");
		rB.append("gradekey=\"" + pM.getGradeKey() + "\" ");
		rB.append(">\n");

		rB.append(AlgebraComplexD.toXMLString(pM.getAlgebra()));

		rB.append("<Coefficients number=\"" + pM.getCoeff().length
						+ "\" gradeKey=\"" + pM.getGradeKey() + "\">\n");
		for (int k = 0; k < pM.getCoeff().length; k++)
			// Appending coefficients
			rB.append("\t" + pM.getCoeff()[k].toXMLString() + "\n");

		rB.append("</Coefficients>\n");
		rB.append("</Monad>\n");
		return rB.toString();
	}

	/**
	 * All clados objects are elements of some algebra. That algebra has a name.
	 */
	public AlgebraComplexD	algebra;
	/**
	 * This array holds the coefficients of the Monad.
	 */
	protected ComplexD[]	cM;

	/**
	 * Simple copy constructor of Monad. Passed Monad will be copied in all
	 * details. This contructor is used most often to get around operations that
	 * alter a Monad when the developer does not wish it to be altered.
	 * 
	 * @param pM
	 *            MonadComplexD
	 */
	public MonadComplexD(MonadComplexD pM)
	{
		setName(pM.getName());
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());

		cM = new ComplexD[getAlgebra().getGProduct().getBladeCount()]; // length
																		// of cM
																		// =
																		// bladecount

		setCoeffInternal(pM.getCoeff());
		setGradeKey();
	}

	/**
	 * Main copy constructor of Monad. Passed Monad will be copied in all
	 * details except its name. This constructor is used most often as a
	 * starting point to generate new Monads based on an old one.
	 * 
	 * @param pName
	 *            String
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws BadSignatureException
	 */
	public MonadComplexD(String pName, MonadComplexD pM)
					throws BadSignatureException, CladosMonadException
	{
		// setFieldName(pM.getFieldName());
		setName(pName);
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());

		cM = new ComplexD[getAlgebra().getGProduct().getBladeCount()];
		setCoeff(pM.getCoeff());
		setGradeKey();
	}

	/**
	 * Special constructor of Monad with most information passed in. This one
	 * will create the default 'Zero' Monad.
	 * 
	 * @param pMonadName
	 *            String
	 * @param pAlgebraName
	 *            String
	 * @param pFrameName
	 *            String
	 * @param pFootName
	 *            String
	 * @param pSig
	 *            String
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig,
					ComplexD pF) throws BadSignatureException,
					CladosMonadException
	{
		setAlgebra(new AlgebraComplexD(pAlgebraName, new Foot(pFootName,
						pF.getFieldType()), pSig));

		setName(pMonadName);
		setFrameName(pFrameName);
		//algebra.getFootPoint().appendIfUniqueRFrame(pFrameName);

		cM = new ComplexD[getAlgebra().getGProduct().getBladeCount()];
		ComplexD tR = ZERO(pF);
		for (int k = 0; k < cM.length; k++)
			cM[k] = copy(tR);
		// cM array now filled with zeros that all share the same DivFieldType
		setGradeKey();
	}

	/**
	 * Special constructor of Monad with most information passed in. 'Special
	 * Case' strings determine the coefficients automatically. 'Unit Scalar' and
	 * 'Unit PScalar' are recognized special cases. All unrecognized strings
	 * create a 'Zero' Monad by default.
	 * 
	 * @param pMonadName
	 *            String
	 * @param pAlgebraName
	 *            String
	 * @param pFrameName
	 *            String
	 * @param pFootName
	 *            String
	 * @param pSig
	 *            String
	 * @param pSpecial
	 *            String
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig,
					ComplexD pF, String pSpecial) throws BadSignatureException,
					CladosMonadException
	{
		this(pMonadName, pAlgebraName, pFrameName, pFootName, pSig, pF);
		// Default ZERO Monad is constructed already.
		// Now handle the special cases and make adjustments to the cM array.

		if (pSpecial.equals("Unit Scalar"))
		{
			int tSpot = getAlgebra().getGProduct().getBasis()
							.getGradeRange((short) 0);
			cM[tSpot] = ONE(cM[tSpot]);
		}
		if (pSpecial.equals("Unit -Scalar"))
		{
			int tSpot = getAlgebra().getGProduct().getBasis()
							.getGradeRange((short) 0);
			cM[tSpot] = ONE(cM[tSpot]);
			cM[tSpot].scale(-1.0D);
		}
		if (pSpecial.equals("Unit PScalar"))
		{
			int tSpot = getAlgebra()
							.getGProduct()
							.getBasis()
							.getGradeRange((short) (getAlgebra().getGProduct()
											.getGradeCount() - 1));
			cM[tSpot] = ONE(cM[tSpot]);
		}
		if (pSpecial.equals("Unit -PScalar"))
		{
			int tSpot = getAlgebra()
							.getGProduct()
							.getBasis()
							.getGradeRange((short) (getAlgebra().getGProduct()
											.getGradeCount() - 1));
			cM[tSpot] = ONE(cM[tSpot]);
			cM[tSpot].scale(-1.0D);
		}
		if (pSpecial.equals("Zero"))
		{
			; // Already done by default
		}
		setGradeKey();
	}

	/**
	 * Main constructor of Monad with all information passed in.
	 * 
	 * @param pMonadName
	 *            String
	 * @param pAlgebraName
	 *            String
	 * @param pFrameName
	 *            String
	 * @param pFootName
	 *            String
	 * @param pSig
	 *            String
	 * @param pC
	 *            ComplexD[]
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	public MonadComplexD(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig,
					ComplexD[] pC) throws BadSignatureException,
					CladosMonadException
	{
		if (pC[0] != null)
		{
			setAlgebra(new AlgebraComplexD(pAlgebraName, new Foot(pFootName,
							pC[0].getFieldType()), pSig));
		}
		else
			throw new CladosMonadException(this,
							"ComplexD cM[0] is to be set to null.  There could be more nulls too.");
		setName(pMonadName);
		setFrameName(pFrameName);
		//algebra.getFootPoint().appendIfUniqueRFrame(pFrameName);

		cM = new ComplexD[getAlgebra().getGProduct().getBladeCount()];

		if (pC.length == getAlgebra().getGProduct().getBladeCount())
			setCoeff(pC);
		else
			throw new CladosMonadException(this,
							"ComplexD cM array size does not match bladecount appropriate for Signature.");
		setGradeKey();
	}

	/**
	 * Monad Addition: (this + pM) This operation is allowed when the two monads
	 * use the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws FieldBinaryException
	 * @throws CladosMonadBinaryException
	 */
	public MonadComplexD add(MonadComplexD pM) throws FieldBinaryException,
					CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM))
		{
			for (int i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++)
				cM[i].add(pM.getCoeff()[i]);
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't add when frames don't match.", pM);
		}
		setGradeKey();
		return this;
	}

	/**
	 * This method conjugates all the coefficients, but leaves the generators of
	 * the algebra untouched
	 */
	@Override
	public MonadComplexD conjugate()
	{
		for (short k = 0; k < getAlgebra().getGProduct().getGradeCount(); k++)
			cM[k].conjugate();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with a leftside multiplication by the
	 * pscalar.
	 */
	@Override
	public MonadComplexD dualLeft()
	{
		short[] tSpot = getAlgebra().getGProduct()
						.getGradeRange((short) (getAlgebra().getGProduct()
										.getGradeCount() - 1));
		ComplexD[] tNewCoeff = new ComplexD[getAlgebra().getGProduct()
						.getBladeCount()];
		// new coeff array

		for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) // column
																				// blade
																				// in
		// product array
		{
			// resulting blade of product
			short prd = (short) (Math.abs(getAlgebra().getGProduct().getResult(
							tSpot[0], j)) - 1);
			// new coefficient is +/- of old coeff, but in new blade prd.
			tNewCoeff[prd] = copy(cM[j]);
			// this is the possible sign flip next
			tNewCoeff[prd].scale(getAlgebra().getGProduct()
							.getSign(tSpot[0], j));
		}
		// tNewCoeff now has a copy of the coefficents needed for 'this'.
		setCoeffInternal(tNewCoeff); // set the coeffs for this product result
		setGradeKey();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with a rightside multiplication by the
	 * pscalar.
	 */
	@Override
	public MonadComplexD dualRight()
	{
		short[] tSpot = getAlgebra().getGProduct()
						.getGradeRange((short) (getAlgebra().getGProduct()
										.getGradeCount() - 1));
		ComplexD[] tNewCoeff = new ComplexD[getAlgebra().getGProduct()
						.getBladeCount()];
		// new coeff array

		for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) // column
																				// blade
																				// in
		// product array
		{
			// resulting blade of product
			short drp = (short) (Math.abs(getAlgebra().getGProduct().getResult(
							j, tSpot[0])) - 1);
			// new coefficient is +/- of old coeff, but in new blade prd.
			tNewCoeff[drp] = copy(cM[j]);
			// this is the possible sign flip next
			tNewCoeff[drp].scale(getAlgebra().getGProduct()
							.getSign(j, tSpot[0]));
		} // t2 now has a copy of the coefficents needed for 'this'.
		setCoeffInternal(tNewCoeff); // set the coeffs for this product result
		setGradeKey();
		return this;
	}

	/**
	 * This method returns the Algebra for this Monad.
	 * 
	 * @return
	 */
	public AlgebraComplexD getAlgebra()
	{
		return algebra;
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @return ComplexD[]
	 */
	public ComplexD[] getCoeff()
	{
		return cM;
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @return ComplexD
	 */
	public ComplexD getCoeff(int pB)
	{
		if (pB >= 0 & pB < getAlgebra().getGProduct().getBladeCount())
			return cM[pB];
		else
			return null;
	}

	/**
	 * This method suppresses all grades in the Monad not equal to the integer
	 * passed. Example: The Scalar Part operation is performed by calling
	 * GradePart(0)
	 * 
	 * @param pGrade
	 *            short
	 */
	@Override
	public MonadComplexD gradePart(short pGrade)
	{
		if (pGrade >= 0
						& pGrade <= getAlgebra().getGProduct().getGradeCount() - 1)
		{
			short j = 0;
			while (j <= getAlgebra().getGProduct().getGradeCount() - 1)
			{
				if (j != pGrade)
				{
					short[] tSpot = getAlgebra().getGProduct().getGradeRange(j);
					for (short l = tSpot[0]; l <= tSpot[1]; l++)
						cM[l].scale(0.0);
				}
				j++;
			}
		}
		else
			return this;
		setGradeKey();
		return this;
	}

	/**
	 * This method suppresses the grade in the Monad equal to the integer
	 * passed. Example: Suppression of the bivector part of a Monad is performed
	 * by calling GradePart(2)
	 * 
	 * @param pGrade
	 *            int
	 */
	@Override
	public MonadComplexD gradeSupress(int pGrade)
	{
		if (pGrade >= 0
						& pGrade <= getAlgebra().getGProduct().getGradeCount() - 1)
		{
			short[] tSpot = getAlgebra().getGProduct().getGradeRange(
							(short) pGrade);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(0.0);
		}
		else
			return this;
		setGradeKey();
		return this;
	}

	/**
	 * Mirror the sense of all geometry generators in the Monad. This operation
	 * alters all grades other than scalar. In some grades the affects cancel
	 * out. Active Invert: All odd grades switch signs, so those coefficients
	 * are altered.
	 */
	@Override
	public MonadComplexD invert()
	{
		short[] tSpot;
		for (short j = 1; j < getAlgebra().getGProduct().getGradeCount(); j += 2)
		{
			tSpot = getAlgebra().getGProduct().getGradeRange(j);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(-1.0d);
		}
		return this;
	}

	/**
	 * This method does a deep check for the equality of two monads. It is not
	 * meant for checking that two monad references actually point to the same
	 * oject since that is easily handled with ==. This one checks algebras,
	 * foot names, frame names and product definitions along with the
	 * coefficients. Each object owned by the monad has its own isequal() method
	 * that gets called.
	 * 
	 * @param pM
	 *            MonadAbstract
	 * @return boolean
	 */
	public boolean isGEqual(MonadComplexD pM)
	{
		if (!isReferenceMatch(this, pM)) return false;

		for (int i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++)
		{
			if (!isEqual(cM[i], pM.getCoeff(i))) return false;
		}
		return true;
	}

	/**
	 * Return the magnitude of the Monad
	 * 
	 * @return ComplexD
	 * @throws CladosMonadException
	 */
	@Override
	public ComplexD magnitude() throws CladosMonadException
	{
		try
		{
			return ModulusList(cM);
		}
		catch (FieldBinaryException e)
		{
			throw new CladosMonadException(this,
							"Coefficients of Monad must be from the same field.");
		}
	}

	/**
	 * Monad antisymmetric multiplication: 1/2(pM this - this pM) This operation
	 * is allowed when the two monads use the same field and satisfy the
	 * Reference Matching test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public MonadComplexD multiplyAntisymm(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM)) // Don't try if not a reference match
		{
			MonadComplexD halfTwo = new MonadComplexD(this);
			halfTwo.multiplyRight(pM);

			multiplyLeft(pM);
			subtract(halfTwo);

			scale(new ComplexD(cM[0], 0.5d, 0.0d));
			setGradeKey();
			return this;
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't symm multiply when frames don't match.", pM);
		}
	}

	/**
	 * Monad leftside multiplication: (pM this) This operation is allowed when
	 * the two monads use the same field and satisfy the Reference Matching
	 * test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public MonadComplexD multiplyLeft(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM)) // Don't try if not a reference match
		{
			ComplexD[] tNewCoeff = new ComplexD[getAlgebra().getGProduct()
							.getBladeCount()];
			// new coeff array
			for (short k = 0; k < getAlgebra().getGProduct().getBladeCount(); k++)
				tNewCoeff[k] = copyzero(cM[0]);

			for (short i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++) // row
																					// blade
																					// in
			// product array
			{
				for (short j = 0; j < getAlgebra().getGProduct()
								.getBladeCount(); j++) // column
				// blade in
				// product
				// array
				{
					// multiply the coefficients first
					ComplexD tCtrbt = multiply(pM.getCoeff(i), cM[j]);
					// find the blade to which this partial product contributes
					short prd = (short) (Math.abs(getAlgebra().getGProduct()
									.getResult(i, j)) - 1);
					// Adjust sign of contribution for product sign of blades
					tCtrbt.scale(getAlgebra().getGProduct().getSign(i, j));
					// Add the contribution to new coeff array
					tNewCoeff[prd].add(tCtrbt);
				}// pM multiplied by blade i in 'this' is done.
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
			setCoeffInternal(tNewCoeff); // set the coeffs for this product
											// result
			setGradeKey();
			return this;
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't left multiply when frames don't match.", pM);
		}
	}

	/**
	 * Monad rightside multiplication: (pM this) This operation is allowed when
	 * the two monads use the same field and satisfy the Reference Matching
	 * test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public MonadComplexD multiplyRight(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM)) // Don't try if not a reference match
		{
			ComplexD[] tNewCoeff = new ComplexD[getAlgebra().getGProduct()
							.getBladeCount()];
			// new coeff array
			for (short k = 0; k < getAlgebra().getGProduct().getBladeCount(); k++)
				tNewCoeff[k] = ZERO(cM[0]);

			for (short i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++) // row
																					// blade
																					// in
			// product array
			{
				for (short j = 0; j < getAlgebra().getGProduct()
								.getBladeCount(); j++) // column
				// blade in
				// product
				// array
				{
					// multiply the coefficients first
					ComplexD tCtrbt = multiply(pM.getCoeff(i), cM[j]);
					// find the blade to which this partial product contributes
					short drp = (short) (Math.abs(getAlgebra().getGProduct()
									.getResult(j, i)) - 1);
					// Adjust sign of contribution for product sign of blades
					tCtrbt.scale(getAlgebra().getGProduct().getSign(j, i));
					// Add the contribution to new coeff array
					tNewCoeff[drp].add(tCtrbt);
				}// pM multiplied by blade i in 'this' is done.
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
			setCoeffInternal(tNewCoeff); // set the coeffs for this product
											// result
			setGradeKey();
			return this;
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't left multiply when frames don't match.", pM);
		}
	}

	/**
	 * Monad symmetric multiplication: 1/2(pM this + this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws CladosMonadException
	 * @throws FieldBinaryException
	 */
	public MonadComplexD multiplySymm(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM)) // Don't try if not a reference match
		{
			MonadComplexD halfTwo = new MonadComplexD(this);
			halfTwo.multiplyRight(pM);

			multiplyLeft(pM);
			add(halfTwo);

			scale(new ComplexD(cM[0], 0.5d, 0.0d));
			setGradeKey();
			return this;
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't symm multiply when frames don't match.", pM);
		}
	}

	/**
	 * Normalize the monad. A <b>CladosMonadException</b> is thrown if the Monad
	 * has a zero magnitude.
	 * 
	 * @throws CladosMonadException
	 */
	@Override
	public MonadComplexD normalize() throws CladosMonadException
	{
		if (gradeKey != 0L)
		{
			try
			{
				ComplexD temp = magnitude();
				temp.invert();
				scale(temp);
			}
			catch (FieldException e)
			{
				throw new CladosMonadException(this,
								"Normalizing a zero magnitude or Field conflicted Monad isn't possible");
			}
		}
		else
		{
			throw new CladosMonadException(this,
							"Normalizing a zero magnitude Monad isn't possible");
		}
		return this;
	}

	/**
	 * This method is a concession to the old notation for the Pseudo Scalar
	 * Part of a monad. it calls the gradePart method with the gradecount for
	 * the specified grade to keep.
	 */
	@Override
	public MonadComplexD PSP()
	{
		gradePart(getAlgebra().getGProduct().getGradeCount());
		return this;
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of
	 * all the permutation, so the easiest thing to do is to change the
	 * coefficients instead.
	 */
	@Override
	public MonadComplexD reverse()
	{
		short[] tSpot;
		short k = 0;
		for (short j = 0; j <= getAlgebra().getGProduct().getGradeCount() - 1; j++)
		{
			k = (short) (j % 4);
			if (k < 2) continue; // This ensures the remainder must be 2 or 3
			tSpot = getAlgebra().getGProduct().getGradeRange(j);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(-1.0d);
		}
		return this;
	}

	/**
	 * Monad Scaling: (this * real number) Only the Monad coefficients are
	 * scaled by the real number.
	 * 
	 * @param pScale
	 *            ComplexD
	 * @throws FieldBinaryException
	 */
	public MonadComplexD scale(ComplexD pScale) throws FieldBinaryException
	{
		for (int j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++)
		{
			cM[j].multiply(pScale);
		}
		setGradeKey();
		return this;
	}

	protected void setAlgebra(AlgebraComplexD pA)
	{
		algebra = pA;
	}

	/**
	 * Reset the Coefficient array used for this Monad. Use of this method is
	 * discouraged, but occasionally necessary. The ideal way of setting up the
	 * coefficient array is to build a new Monad with the new coefficient array.
	 * Using this set method encourages developers to reuse old objects. While
	 * this is useful for avoiding object construction overhead, it is dangerous
	 * in that the old meaning of the object might linger in the various name
	 * attributes. Caution is advised if this method is used while frequent
	 * reuse should be considered bad form.
	 * 
	 * @param ppC
	 *            ComplexD[]
	 * @throws CladosMonadException
	 */
	public void setCoeff(ComplexD[] ppC) throws CladosMonadException
	{
		if (ppC.length == getAlgebra().getGProduct().getBladeCount())
		{
			for (int k = 0; k < ppC.length; k++)
				cM[k] = new ComplexD(ppC[k]);
		}
		else
			throw new CladosMonadException(this,
							"ComplexD array passed in for coefficient copy is the wrong length");
	}

	/**
	 * Reset the Coefficient array used for this Monad. Don't bother checking
	 * for array lengths because this method is only called from within the
	 * Monad with coeff copies.
	 * 
	 * @param ppC
	 *            ComplexD[]
	 */
	private void setCoeffInternal(ComplexD[] ppC)
	{
		if (ppC.length == getAlgebra().getGProduct().getBladeCount())
		{
			for (int k = 0; k < ppC.length; k++)
				cM[k] = new ComplexD(ppC[k]);
		}
	}

	@Override
	public void setFrameName(String pRName)
	{
		getAlgebra().getFootPoint().removeRFrames(frameName);
		frameName = pRName;
		getAlgebra().getFootPoint().appendIfUniqueRFrame(pRName);
	}

	/**
	 * Set the grade key for the monad. Never accept an externally provided key.
	 * Always recalculate it after after an one of the unary or binary
	 * operations.
	 */
	@Override
	protected void setGradeKey()
	{
		gradeKey = 0;
		for (short j = 0; j < getAlgebra().getGProduct().getGradeCount(); j++)
		{
			short[] tSpot = getAlgebra().getGProduct().getGradeRange(j);
			for (short k = tSpot[0]; k <= tSpot[1]; k++)
			{
				if (!isZero(cM[k]))
				{
					gradeKey += Math.pow(10, j);
					break; // Grade j found. Move to grade j+1
				}
			}
		}
		if (gradeKey == 0) gradeKey = 1;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It calls the gradePart method with a zero for the specified grade
	 * to keep.
	 */
	@Override
	public MonadComplexD SP()
	{
		gradePart((short) 0);
		return this;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * 
	 * @return ComplexD
	 */
	@Override
	public ComplexD SPc()
	{
		return cM[0];
	}

	/**
	 * Return the magnitude squared of the Monad
	 * 
	 * @return ComplexD
	 * @throws CladosMonadException
	 */
	@Override
	public ComplexD sqMagnitude() throws CladosMonadException
	{
		try
		{
			return SQModulusList(cM);
		}
		catch (FieldBinaryException e)
		{
			throw new CladosMonadException(this,
							"Coefficients of Monad must be from the same field.");
		}
	}

	/**
	 * Monad Subtraction: (this - pM) This operation is allowed when the two
	 * monads use the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM
	 *            MonadComplexD
	 * @throws FieldBinaryException
	 * @throws CladosMonadBinaryException
	 */
	public MonadComplexD subtract(MonadComplexD pM)
					throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM))
		{
			for (int i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++)
				cM[i].subtract(pM.getCoeff()[i]);
		}
		else
		{
			throw new CladosMonadBinaryException(this,
							"Can't subtract when frames don't match.", pM);
		}
		setGradeKey();
		return this;
	}
}
