/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.MonadRealF<br>
 * -------------------------------------------------------------------- <p>
 * You ("Licensee") are granted a license to this software under the terms of 
 * the GNU General Public License. A full copy of the license can be found 
 * bundled with this package or code file. If the license file has become 
 * separated from the package, code file, or binary executable, the Licensee is
 * still expected to read about the license at the following URL before 
 * accepting this material. 
 * <code>http://www.opensource.org/gpl-license.html</code><p> 
 * Use of this code or executable objects derived from it by the Licensee states
 * their willingness to accept the terms of the license. <p> 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.MonadRealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosGExceptions.*;
import com.interworldtransport.cladosF.RealF;
import com.interworldtransport.cladosFExceptions.*;
import static com.interworldtransport.cladosF.RealF.*;

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
public class MonadRealF extends MonadAbstract
{
	/**
	 * Return true if more the monad is a ZERO scalar.
	 * 
	 * @param pM
	 * 			MonadRealF This is the monad to be tested.
	 * 
	 * @return boolean
	 */
	public static boolean isGZero(MonadRealF pM)
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
	 *            MonadRealF
	 * @throws CladosMonadException
	 * 		This exception is thrown when the method can't create a copy of the monad to be checked.
	 * @throws FieldBinaryException
	 * 		This exception is thrown when the method can't multiply two fields used by the monad to be checked.
	 */
	public static boolean isIdempotent(MonadRealF pM)
					throws FieldBinaryException, CladosMonadException
	{
		if (isGZero(pM)) return true;
		MonadRealF check1 = new MonadRealF(pM);
		check1.multiplyLeft(pM);
		if (check1.isGEqual(pM)) return true;
		return false;
	}

	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * 
	 * @return boolean
	 * @param pM
	 *            MonadRealF
	 * @throws CladosMonadException
	 * 		This exception is thrown when the method can't create a copy of the monad to be checked.
	 * @throws FieldException
	 * 		This exception is thrown when the method can't copy the field used by the monad to be checked.
	 */
	public static boolean isIdempotentMultiple(MonadRealF pM)
					throws CladosMonadException, FieldException
	{
		if (isGZero(pM)) return true;
		if (isIdempotent(pM)) return true;
		if (isNilpotent(pM)) return false;

		// What we want is to square 'this', find the first non-zero
		// coefficient, and rescale the original 'this' to see if it
		// would be an idempotent that way. If it is, then the original
		// 'this' is an idempotent multiple.

		MonadRealF check1 = new MonadRealF(pM);
		check1.multiplyLeft(pM);

		int k = 0;
		RealF fstnzeroC = ZERO("Place Holder");
		while (isZero(fstnzeroC)
						& k <= pM.getAlgebra().getGProduct().getBladeCount() - 1)
		{
			fstnzeroC = copy(pM.getCoeff(k));
			k++;
		}

		// check1 = new MonadRealF(this);
		fstnzeroC.invert();
		check1.scale(fstnzeroC); // monad scaling is does with RealF's and not
									// doubles.

		if (check1.isGEqual(pM)) return true;
		return false;
	}

	/**
	 * Return true if the Monad is nilpotent
	 * 
	 * @return boolean
	 * @param pM
	 *            MonadRealF
	 * @throws CladosMonadException
	 * 	This exception is thrown when the method can't create a copy of the monad to be checked.
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the method can't multiply two fields used by the monad to be checked.
	 */
	public static boolean isNilpotent(MonadRealF pM)
					throws FieldBinaryException, CladosMonadException
	{
		if (isGZero(pM)) return true;
		MonadRealF check1 = new MonadRealF(pM);
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
	public static boolean isReferenceMatch(MonadRealF pM, MonadRealF pN)
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
	 * @param pM
	 * 			MonadRealF This is the monad to be converted to XML.
	 * 
	 * @return String
	 */
	public static String toXMLString(MonadRealF pM)
	{
		StringBuffer rB = new StringBuffer("<Monad name=\"" + pM.getName()
						+ "\" ");
		rB.append("algebra=\"" + pM.getAlgebra().getAlgebraName() + "\" ");
		rB.append("frame=\"" + pM.getFrameName() + "\" ");
		rB.append("gradekey=\"" + pM.getGradeKey() + "\" ");
		rB.append(">\n");

		rB.append(AlgebraRealF.toXMLString(pM.getAlgebra()));

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
	protected AlgebraRealF	algebra;
	/**
	 * All monads reference a frame in order to give meaning to the coordinates.
	 */
	protected FrameRealF	frame;
	/**
	 * This array holds the coefficients of the Monad relative to the reference
	 * directions in the frame.
	 */
	protected RealF[]		cM;

	/**
	 * Simple copy constructor of Monad. Passed Monad will be copied in all
	 * details. This contructor is used most often to get around operations that
	 * alter a Monad when the developer does not wish it to be altered.
	 * 
	 * @param pM
	 *            MonadRealF
	 */
	public MonadRealF(MonadRealF pM)
	{
		setName(pM.getName());
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());

		cM = new RealF[getAlgebra().getGProduct().getBladeCount()]; // length of
																	// cM is
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
	 *            MonadRealF
	 * @throws BadSignatureException
	 * 	This exception is thrown if the signature string offered is rejected.
	 * @throws CladosMonadException
	 * 	This exception is thrown if there is an issue with the coefficients offered.
	 * 	The issues could involve null coefficients or a coefficient array of the wrong size.
	 */
	public MonadRealF(String pName, MonadRealF pM)
					throws BadSignatureException, CladosMonadException
	{
		setName(pName);
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());

		cM = new RealF[getAlgebra().getGProduct().getBladeCount()];
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
	 * @param pF
	 * 			  RealF
	 * @throws BadSignatureException
	 * 	This exception is thrown if the signature string offered is rejected.
	 * @throws CladosMonadException
	 * 	This exception is thrown if there is an issue with the coefficients offered.
	 * 	The issues could involve null coefficients or a coefficient array of the wrong size.
	 */
	public MonadRealF(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig, RealF pF)
					throws BadSignatureException, CladosMonadException
	{
		setAlgebra(new AlgebraRealF(pAlgebraName, new Foot(pFootName,
						pF.getFieldType()), pSig));

		setName(pMonadName);
		setFrameName(pFrameName);
		// algebra.getFootPoint().appendIfUniqueRFrame(pFrameName);

		cM = new RealF[getAlgebra().getGProduct().getBladeCount()];
		RealF tR = ZERO(pF);
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
	 * @param pF
	 * 			  RealF
	 * @param pSpecial
	 *            String
	 * @throws BadSignatureException
	 * 	This exception is thrown if the signature string offered is rejected.
	 * @throws CladosMonadException
	 * 	This exception is thrown if there is an issue with the coefficients offered the default constructor.
	 * 	The issues could involve null coefficients or a coefficient array of the wrong size.
	 * 
	 * return MonadRealF
	 */
	public MonadRealF(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig, RealF pF,
					String pSpecial) throws BadSignatureException,
					CladosMonadException
	{
		this(pMonadName, pAlgebraName, pFrameName, pFootName, pSig, pF);
		// Default ZERO Monad is constructed already.
		// Now handle the special cases and make adjustments to the cM array.

		if (pSpecial.equals("Unit Scalar"))
		{
			int tSpot = getAlgebra().getGBasis().getGradeRange((short) 0);
			cM[tSpot] = ONE(cM[tSpot]);
		}
		if (pSpecial.equals("Unit -Scalar"))
		{
			int tSpot = getAlgebra().getGBasis().getGradeRange((short) 0);
			cM[tSpot] = ONE(cM[tSpot]);
			cM[tSpot].scale(-1.0f);
		}
		if (pSpecial.equals("Unit PScalar"))
		{
			int tSpot = getAlgebra().getGBasis()
							.getGradeRange((short) (getAlgebra().getGProduct()
											.getGradeCount() - 1));
			cM[tSpot] = ONE(cM[tSpot]);
		}
		if (pSpecial.equals("Unit -PScalar"))
		{
			int tSpot = getAlgebra().getGBasis()
							.getGradeRange((short) (getAlgebra().getGProduct()
											.getGradeCount() - 1));
			cM[tSpot] = ONE(cM[tSpot]);
			cM[tSpot].scale(-1.0f);
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
	 *            RealF[]
	 * @throws BadSignatureException
	 * 	This exception is thrown if the signature string offered is rejected.
	 * @throws CladosMonadException
	 * 	This exception is thrown if there is an issue with the coefficients offered.
	 * 	The issues could involve null coefficients or a coefficient array of the wrong size.
	 */
	public MonadRealF(String pMonadName, String pAlgebraName,
					String pFrameName, String pFootName, String pSig, RealF[] pC)
					throws BadSignatureException, CladosMonadException
	{
		if (pC[0] != null)
		{
			setAlgebra(new AlgebraRealF(pAlgebraName, new Foot(pFootName,
							pC[0].getFieldType()), pSig));
		}
		else
			throw new CladosMonadException(this,
							"RealF cM[0] is to be set to null.  There could be more nulls too.");
		setName(pMonadName);
		setFrameName(pFrameName);
		// algebra.getFootPoint().appendIfUniqueRFrame(pFrameName);

		cM = new RealF[getAlgebra().getGProduct().getBladeCount()];

		if (pC.length == getAlgebra().getGProduct().getBladeCount())
			setCoeff(pC);
		else
			throw new CladosMonadException(this,
							"RealF cM array size does not match bladecount appropriate for Signature.");
		setGradeKey();
	}

	/**
	 * Monad Addition: (this + pM) This operation is allowed when the two monads
	 * use the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM
	 *            MonadRealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the method can't multiply two fields used by the monad to be checked.
	 * @throws CladosMonadBinaryException
	 *  This exception is thrown when the monads fail a reference match.
	 * @return MonadRealF
	 */
	public MonadRealF add(MonadRealF pM) throws FieldBinaryException,
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
	 * the algebra untouched. Real Monads don't need to do anything, though.
	 */
	@Override
	public MonadRealF conjugate()
	{
		return this;
	}

	/**
	 * The Monad is turned into its Dual with a leftside multiplication by the
	 * pscalar.
	 */
	@Override
	public MonadRealF dualLeft()
	{
		short[] tSpot = getAlgebra().getGProduct()
						.getGradeRange((short) (getAlgebra().getGProduct()
										.getGradeCount() - 1));
		RealF[] tNewCoeff = new RealF[getAlgebra().getGProduct()
						.getBladeCount()]; // new coeff
		// array

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
	public MonadRealF dualRight()
	{
		short[] tSpot = getAlgebra().getGProduct()
						.getGradeRange((short) (getAlgebra().getGProduct()
										.getGradeCount() - 1));
		RealF[] tNewCoeff = new RealF[getAlgebra().getGProduct()
						.getBladeCount()]; // new coeff
		// array

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
	 * @return AlgebraRealF
	 */
	public AlgebraRealF getAlgebra()
	{
		return algebra;
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @return RealF[]
	 */
	public RealF[] getCoeff()
	{
		return cM;
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @param pB
	 * 			int This is the coefficient at the equivalent tuple location.
	 * 
	 * @return RealF
	 */
	public RealF getCoeff(int pB)
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
	public MonadRealF gradePart(short pGrade)
	{
		if (pGrade >= 0 & pGrade < getAlgebra().getGProduct().getGradeCount())
		{
			short j = 0;
			while (j <= getAlgebra().getGProduct().getGradeCount() - 1)
			{
				if (j != pGrade)
				{
					short[] tSpot = getAlgebra().getGProduct().getGradeRange(j);
					for (short l = tSpot[0]; l <= tSpot[1]; l++)
						cM[l].scale(0.0f);
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
	public MonadRealF gradeSupress(int pGrade)
	{
		if (pGrade >= 0
						& pGrade <= getAlgebra().getGProduct().getGradeCount() - 1)
		{
			short[] tSpot = getAlgebra().getGProduct().getGradeRange(
							(short) pGrade);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(0.0f);
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
	public MonadRealF invert()
	{
		short[] tSpot;
		for (short j = 1; j < getAlgebra().getGProduct().getGradeCount(); j += 2)
		{
			tSpot = getAlgebra().getGProduct().getGradeRange(j);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(-1.0f);
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
	public boolean isGEqual(MonadRealF pM)
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
	 * @return RealF
	 * @throws CladosMonadException
	 * 	This exception is possible because magnitudes are build from sqMagnitudes. 
	 * 	That means there is an intermediate multiplication steps that could cause
	 * 	a FieldBinaryException, but never should. If this exception gets thrown here
	 * 	there is something seriously amiss with magnitude() and sqMagnitude().
	 */
	@Override
	public RealF magnitude() throws CladosMonadException
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
	 *            MonadRealF
	 * @throws CladosMonadBinaryException
	 * 	This exception is thrown when the reference match test fails with the two monads
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the field match test fails with the two monads
	 * @return MonadRealF
	 */
	public MonadRealF multiplyAntisymm(MonadRealF pM)
			throws FieldBinaryException, CladosMonadBinaryException
	{
		if (!isReferenceMatch(this, pM)) 
			throw new CladosMonadBinaryException(this,"Asymm multiply fails reference match.", pM);
		// Don't try if not a reference match
		if (isGZero(this)) return this;//obviously
		if (isGZero(pM)) return pM;//equally obvious
			
		MonadRealF halfTwo = new MonadRealF(this);
		halfTwo.multiplyRight(pM);

		multiplyLeft(pM);
		subtract(halfTwo);

		scale(new RealF(cM[0], 0.5f));
		setGradeKey();
		return this;	
	}

	/**
	 * Monad leftside multiplication: (pM this) 
	 * This operation is allowed when the two monads use the same field and satisfy 
	 * the Reference Match test.
	 * 
	 * @param pM	MonadRealF
	 * @throws CladosMonadBinaryException
	 * 	This exception is thrown when the reference match test fails with the two monads
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the field match test fails with the two monads
	 * @return MonadRealF
	 */
	public MonadRealF multiplyLeft(MonadRealF pM) 
			throws FieldBinaryException, CladosMonadBinaryException
	{
		if (!isReferenceMatch(this, pM)) 
			throw new CladosMonadBinaryException(this,"Left multiply fails reference match.", pM);
		// Don't try if not a reference match
		if (isGZero(this)) return this;//obviously
		if (isGZero(pM)) return pM;//equally obvious
		
		
		RealF[] tNewCoeff = new RealF[getAlgebra().getGProduct().getBladeCount()]; 
		// new coeff array built to hold result
		for (short k = 0; k < getAlgebra().getGProduct().getBladeCount(); k++)
			tNewCoeff[k] = ZERO(cM[0]);
		// new coeff array populated with ZEROES from the field.
			
		if (sparseFlag)
		{	
		/* Use gradeKey to find the non-zero grades.
		 * gradeKey is is a long with a 1 in a digit if the ten's power
		 * represented by that digit is represented as a grade in a monad.
		 * 
		 * For example: gradeKey=101 means the monad is a sum of bivector and scalar
		 * because 10^2+10^0 = 101.
		 * 
		 * In a sparse monad, the gradeKey will have few 1's, so multiplication
		 * can be simplified by not looping through each blade. Instead, we parse
		 * the gradeKey and only loop through the blades for grades that could
		 * be non-ZERO.
		 */
			long tempGradeKey=gradeKey;				
			short logKey=(short) Math.floor(Math.log(tempGradeKey));
			short[] tSpot={0,0};
				
			//logKey is the highest grade with non-zero blades
			//tSpot will point at the blades of that grade
			while (logKey>=0.0D)
			{
				tSpot = getAlgebra().getGProduct().getGradeRange(logKey);
				for (short i = tSpot[0]; i <= tSpot[1]; i++)
				{
					// Looping through row blades in product array for grade logKey
					if (isZero(pM.getCoeff(i))) continue;
					// This is a weak form of the sparse flag kept here.
					for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) 
					// Looping through column blades in product array
					{
						if (isZero(cM[j])) continue;
						// This is a weak form of the sparse flag repeated here.
						// Don't bother summing on zeroes.
						
						// multiply the coefficients first
						RealF tCtrbt = multiply(pM.getCoeff(i), cM[j]);
						
						// find the blade to which this partial product contributes
						short prd = (short) (Math.abs(getAlgebra().getGProduct().getResult(i, j)) - 1);
							
						// Adjust sign of contribution for product sign of blades
						tCtrbt.scale(getAlgebra().getGProduct().getSign(i, j));
							
						// Add the contribution to new coeff array
						tNewCoeff[prd].add(tCtrbt);
								
					}// blade i in 'this' multiplied by pM is done.
				}
				//Subtract 10^logKey so we can mark that the grade is done.
				tempGradeKey -= Math.pow(10, logKey);
				
				//if tempGradeKey is zero, we've processed all grades including scalar.
				if (tempGradeKey==0) break;
				
				//logKey can be zero for scalar grade.
				logKey=(short) Math.floor(Math.log(tempGradeKey));
					
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
		}
		else //loop through the blades in 'this' individually.
		{
			for (short i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++)
			// Looping through row blades in product array
			{
				if (isZero(pM.getCoeff(i))) continue;
				// This is a weak form of the sparse flag that notes a zero coefficient
				// in pM need not be processed because it won't contribute to any sums.
				
				for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) 
				// Looping through column blades in product array
				{
					// multiply the coefficients first
					RealF tCtrbt = multiply(pM.getCoeff(i), cM[j]);
						
					// find the blade to which this partial product contributes
					short prd = (short) (Math.abs(getAlgebra().getGProduct().getResult(i, j)) - 1);
						
					// Adjust sign of contribution for product sign of blades
					tCtrbt.scale(getAlgebra().getGProduct().getSign(i, j));
						
					// Add the contribution to new coeff array
					tNewCoeff[prd].add(tCtrbt);
						
				}// blade i in 'this' multiplied by pM is done.
					
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
		}
		// set the coeffs for this product result
		setCoeffInternal(tNewCoeff); 
		setGradeKey();
		return this;	
	}

	/**
	 * Monad rightside multiplication: (pM this) 
	 * This operation is allowed when the two monads use the same field and satisfy 
	 * the Reference Match test.
	 * 
	 * @param pM
	 *            MonadRealF
	 * @throws CladosMonadBinaryException
	 * 	This exception is thrown when the reference match test fails with the two monads
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the field match test fails with the two monads
	 * @return MonadRealF
	 */
	public MonadRealF multiplyRight(MonadRealF pM) throws FieldBinaryException,
					CladosMonadBinaryException
	{
		if (!isReferenceMatch(this, pM)) // Don't try if not a reference match
			throw new CladosMonadBinaryException(this,"Right multiply fails reference match.", pM);
		if (isGZero(this)) return this;//obviously
		if (isGZero(pM)) return pM;//equally obvious
			
		RealF[] tNewCoeff = new RealF[getAlgebra().getGProduct().getBladeCount()]; 
		// new coeff array
		for (short k = 0; k < getAlgebra().getGProduct().getBladeCount(); k++)
			tNewCoeff[k] = ZERO(cM[0]);
		// new coeff array built to hold result
			
		if (sparseFlag)
		{
		/* Use gradeKey to find the non-zero grades.
		 * gradeKey is is a long with a 1 in a digit if the ten's power
		 * represented by that digit is represented as a grade in a monad.
		 * 
		 * For example: gradeKey=1001 means the monad is a sum of trivector and scalar
		 * because 10^3+10^0 = 1001.
		 * 
		 * In a sparse monad, the gradeKey will have few 1's, so multiplication
		 * can be simplified by not looping through each blade. Instead, we parse
		 * the gradeKey and only loop through the blades for grades that could
		 * be non-ZERO.
		 */
			long tempGradeKey=gradeKey;				
			short logKey=(short) Math.floor(Math.log(tempGradeKey));
			short[] tSpot={0,0};
				
			//logKey is the highest grade with non-zero blades
			//tSpot will point at the blades of that grade
			while (logKey>=0.0D)
			{
				tSpot = getAlgebra().getGProduct().getGradeRange(logKey);
				for (short i = tSpot[0]; i <= tSpot[1]; i++)
				{
					// Looping through row blades in product array for grade logKey
					if (isZero(pM.getCoeff(i))) continue;
					// This is a weak form of the sparse flag kept here.
					for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) 
					// Looping through column blades in product array
					{
						if (isZero(cM[j])) continue;
						// This is a weak form of the sparse flag repeated here.
						// Don't bother summing on zeroes.
						
						// multiply the coefficients first
						RealF tCtrbt = multiply(cM[j], pM.getCoeff(i));
								
						// find the blade to which this partial product contributes
						short prd = (short) (Math.abs(getAlgebra().getGProduct().getResult(j, i)) - 1);
								
						// Adjust sign of contribution for product sign of blades
						tCtrbt.scale(getAlgebra().getGProduct().getSign(j, i));
								
						// Add the contribution to new coeff array
						tNewCoeff[prd].add(tCtrbt);
								
					}// blade i in 'this' multiplied by pM is done.
				}
				//Subtract 10^logKey so we can mark that the grade is done.
				tempGradeKey -= Math.pow(10, logKey);
				
				//if tempGradeKey is zero, we've processed all grades including scalar.
				if (tempGradeKey==0) break;
					
				//logKey can be zero for scalar grade.
				logKey=(short) Math.floor(Math.log(tempGradeKey));
					
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
				
		}
		else //loop through the blades in 'this' individually.
		{
			for (short i = 0; i < getAlgebra().getGProduct().getBladeCount(); i++) 
			// Looping through row blades in product array
			{
				if (isZero(pM.getCoeff(i))) continue;
				// This is a weak form of the sparse flag that notes a zero coefficient
				// in pM need not be processed because it won't contribute to any sums.
					
				for (short j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++) 
				// Looping through column blades in product array
				{
					// multiply the coefficients first
					RealF tCtrbt = multiply(cM[j], pM.getCoeff(i));
						
					// find the blade to which this partial product contributes
					short drp = (short) (Math.abs(getAlgebra().getGProduct().getResult(j, i)) - 1);
						
					// Adjust sign of contribution for product sign of blades
					tCtrbt.scale(getAlgebra().getGProduct().getSign(j, i));
						
					// Add the contribution to new coeff array
					tNewCoeff[drp].add(tCtrbt);
						
				}// pM multiplied by blade i in 'this' is done.
				
			}// tNewCoeff now has a copy of the coefficients needed for 'this'.
		}
		// set the coeffs for this product result
		setCoeffInternal(tNewCoeff); 
		setGradeKey();
		return this;	
	}

	/**
	 * Monad symmetric multiplication: 1/2(pM this + this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM
	 *            MonadRealF
	 * @throws CladosMonadBinaryException
	 * 	This exception is thrown when the reference match test fails with the two monads
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the field match test fails with the two monads
	 * @return MonadRealF
	 */
	public MonadRealF multiplySymm(MonadRealF pM) 
			throws FieldBinaryException, CladosMonadBinaryException
	{
		if (isReferenceMatch(this, pM)) 
			throw new CladosMonadBinaryException(this,"Symm multiply fails reference match.", pM);
		// Don't try if not a reference match
		if (isGZero(this)) return this;//obviously
		if (isGZero(pM)) return pM;//equally obvious
			
		MonadRealF halfTwo = new MonadRealF(this);
		halfTwo.multiplyRight(pM);

		multiplyLeft(pM);
		add(halfTwo);

		scale(new RealF(cM[0], 0.5f));
		setGradeKey();
		return this;
	}

	/**
	 * Normalize the monad. A <b>CladosMonadException</b> is thrown if the Monad
	 * has a zero magnitude.
	 * 
	 * @throws CladosMonadException
	 * 	This exception is thrown when normalizing a zero or field conflicted monad is tried.
	 */
	@Override
	public MonadRealF normalize() throws CladosMonadException
	{
		try
		{
			RealF temp = magnitude();
			temp.invert();
			scale(temp);
		}
		catch (FieldException e)
		{
			throw new CladosMonadException(this,
							"Normalizing a zero magnitude or Field conflicted Monad isn't possible");
		}
		return this;
	}

	/**
	 * This method is a concession to the old notation for the Pseudo Scalar
	 * Part of a monad. it calls the gradePart method with the gradecount for
	 * the specified grade to keep.
	 */
	@Override
	public MonadRealF PSP()
	{
		gradePart(getAlgebra().getGProduct().getGradeCount());
		return this;
	}
	
	/**
	 * This method is a concession to the old notation for the Pseudo Scalar
	 * Part of a monad. It returns the pscalar part coefficient.
	 * @return RealF
	 */
	@Override
	public RealF PSPc()
	{
		return cM[getAlgebra().getGProduct().getGradeCount()];
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of
	 * all the permutation, so the easiest thing to do is to change the
	 * coefficients instead.
	 */
	@Override
	public MonadRealF reverse()
	{
		short[] tSpot;
		short k = 0;
		for (short j = 0; j <= getAlgebra().getGProduct().getGradeCount() - 1; j++)
		{
			k = (short) (j % 4);
			if (k < 2) continue; // This ensures the remainder must be 2 or 3
			tSpot = getAlgebra().getGProduct().getGradeRange(j);
			for (short l = tSpot[0]; l <= tSpot[1]; l++)
				cM[l].scale(-1.0f);
		}
		return this;
	}

	/**
	 * Monad Scaling: (this * real number) Only the Monad coefficients are
	 * scaled by the real number.
	 * 
	 * @param pScale
	 *            RealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the scale field fails a field match with the coefficients.
	 * @return MonadRealF
	 */
	public MonadRealF scale(RealF pScale) throws FieldBinaryException
	{
		for (int j = 0; j < getAlgebra().getGProduct().getBladeCount(); j++)
		{
			cM[j].multiply(pScale);
		}
		setGradeKey();
		return this;
	}

	protected void setAlgebra(AlgebraRealF pA)
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
	 *            RealF[]
	 * @throws CladosMonadException
	 * 	This exception is thrown when the array offered for coordinates is of the wrong length.
	 */
	public void setCoeff(RealF[] ppC) throws CladosMonadException
	{
		if (ppC.length == getAlgebra().getGProduct().getBladeCount())
		{
			for (int k = 0; k < ppC.length; k++)
				cM[k] = new RealF(ppC[k]);
		}
		else
			throw new CladosMonadException(this,
							"RealF array passed in for coefficient copy is the wrong length");
	}

	/**
	 * Reset the Coefficient array used for this Monad. Don't bother checking
	 * for array lengths because this method is only called from within the
	 * Monad with coeff copies.
	 * 
	 * @param ppC
	 *            RealF[]
	 */
	private void setCoeffInternal(RealF[] ppC)
	{
		if (ppC.length == getAlgebra().getGProduct().getBladeCount())
		{
			for (int k = 0; k < ppC.length; k++)
				cM[k] = new RealF(ppC[k]);
		}
	}

	protected void setFrame(FrameRealF pF)
	{
		frame = pF;
		setFrameName(pF.getName());
	}

	@Override
	public void setFrameName(String pRName)
	{
		getAlgebra().getFootPoint().removeRFrames(frameName);
		//frameName = pRName;
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
				if (!isZero(cM[k])) //cM[] is the coefficient array
				{
					gradeKey += Math.pow(10, j);
					break; 
					// Grade j found. Don't need to look at the other k's, 
					// so move to grade j+1
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
	public MonadRealF SP()
	{
		gradePart((short) 0);
		return this;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * 
	 * @return RealF
	 */
	@Override
	public RealF SPc()
	{
		return cM[0];
	}

	/**
	 * Return the magnitude squared of the Monad
	 * 
	 * @throws CladosMonadException
	 *  This exception is thrown when their is a field mismatch between the parts being squared.
	 *  This should never happen, but the multiplication method checks for it and SqMag makes use 
	 *  of multiplication... thus it could theoretically be thrown.
	 * @return RealF
	 */
	@Override
	public RealF sqMagnitude() throws CladosMonadException
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
	 *            MonadRealF
	 * @throws FieldBinaryException
	 * 	This exception is thrown when the method can't multiply two fields used by the monad to be checked.
	 * @throws CladosMonadBinaryException
	 *  This exception is thrown when the monads fail a reference match.
	 * 
	 * @return MonadRealF
	 */
	public MonadRealF subtract(MonadRealF pM) throws FieldBinaryException,
					CladosMonadBinaryException
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