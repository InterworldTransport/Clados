/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Monad<br>
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
 * ---org.interworldtransport.cladosG.Monad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosFListBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.UnitAbstract;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * A CladosG Monad is better known as a multivector to anyone with experience
 * with geometric algebras. Think of them as vectors, but with higher ranked
 * elements also represented. There is more to it than that, but that is the
 * nutshell version.
 * <p>
 * Caution | To the software community, 'vector' is generally understood to be a
 * dynamic array data structure. To a physicist, it is a thing that belongs to a
 * vector space and follows extra rules regarding allowed transformations. It's
 * 'thing' nature is much more important than how it acts as a data structure.
 * <p>
 * The 'allowed' transformations are operations that do not change the 'thing'
 * nature of the object represented. For example, a meter stick is what it is no
 * matter how a coordinate system used to represent it is rotated. A
 * representation of the meter stick would have to be identifiable as the same
 * thing after a rotation, meaning many apparently different sets of data in the
 * structure are actually the same thing.
 * <p>
 * A 'multivector' can represent 'things' that are of higher geometric rank than
 * lines. A monad has a data structure inside to support both coordinates and
 * reference information. 'Allowed' transformations on the reference frame
 * should cause the coefficients to shift the correct way leaving the monad as
 * it was from an external perspective. That's HOW a monad represents a thing.
 * It is expected to be invariant under 'allowed' transformations of the
 * contained data.
 * <p>
 * Why 'monad' instead of 'multivector'? Try typing it yourself a few thousand
 * times and you'll understand. The name doesn't actually matter, but it is from
 * an old tradition when physicists wrote linear transformations as dyads. Two
 * vectors were written side by side, but no simplifying action could be taken.
 * The dyad was described as an operator, so the vectors were applied to another
 * operand rathan than each other. Few textbooks do that anymore (we have matrix
 * algebra now) and the name has fallen into dis-use. It is revived here because
 * we need something short and useful. A 'dyad' will be two monads in a list
 * which is generalized in another CladosG class called nyad.
 * <p>
 * Doesn't 'monad' conflict with mathemetician's usage in Category Theory and
 * with Functional Programming advocates bringing that mathematics to the real
 * world? Yes. What the functional programmers are doing is terribly important,
 * so don't confuse their monad with what a physicist needs. This shouldn't be
 * too hard. Y'all have been doing it for 'vector' for a few decades. You'd
 * rather something else? 'Unad'? Make your case by helping out.
 * <p>
 * NOTE | Regarding suppressed unchecked type casting warnings, they are
 * restricted to the casting that happens in CladosFBuilder and
 * CladosFListBuilder classes mostly. This happens when we copy number objects
 * to avoid mutability using a generic copyOf() method. As long as the
 * coefficients in a monad are valid UnitAbstract children implementing Field
 * and Normalizable, the copyOf() functions will work fine. There are two cases
 * where things can go awry, though.
 * <p>
 * 1. It is probably possible for someone to mix UnitAbstract children in a
 * Scale object containing a mondad's coefficients. The copyOf() functions will
 * faithfully copy them as they are. The scale() methods and others will
 * faithfully pass them to a Scale object to be used as appropriate there. For
 * example, Scale's scale() method will will try to use them as given. What
 * happens, though, is the inbound number gets multiplied against others AS THEY
 * UNDERSTAND MULTIPLICATION. Scaling a complex by a real will work all right
 * unless one thought the scaling was between two complex numbers. THAT'S why
 * Scale AND Monad implement Modal, but nothing is not enforced yet.
 * <p>
 * 2. If someone invents a new UnitAbstract child, there is a ton of work to do
 * as the builders and other enumerations have to be adapted. Any class
 * implementing Model might have methods that switch on CladosField values.
 * So... be cautious about inventing new CladosF numbers. Lots of work will have
 * to be done.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class Monad implements Modal {
	/**
	 * Return a boolean if the grade being checked is non-zero in the Monad.
	 * <p>
	 * @param pM     Monad
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean hasGrade(Monad pM, int pGrade) {
		if (pM.getGradeKey() == 1 & pGrade == 0)
			return true;

		long tSpot = (pM.getGradeKey()) / ((long) Math.pow(10, pGrade));
		if (tSpot % 2 == 1)
			return true;

		return false;
	}

	/**
	 * Return a boolean if the grade being checked is the grade of the Monad. False
	 * is returned otherwise.
	 * <p>
	 * @param pM     Monad
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean isGrade(Monad pM, int pGrade) {
		if (Math.pow(10, pGrade) == pM.getGradeKey())
			return true;

		return false;
	}

	/**
	 * Return true if more the monad is a ZERO scalar.
	 * <p>
	 * @param pM MonadComplexD This is the monad to be tested.
	 * @return boolean
	 */
	public static boolean isGZero(Monad pM) {
		return (pM.getGradeKey() == 1 & pM.getScales().isScalarZero());
	}

	/**
	 * Return true if the Monad an idempotent
	 * <p>
	 * @return boolean
	 * @param pM Monad
	 */
	public static boolean isIdempotent(Monad pM) {
		if (isGZero(pM))
			return true;
		return CladosGBuilder.copyOfMonad(pM).multiplyLeft(pM).isGEqual(pM);
	}

	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * <p>
	 * The strategy for this method is as follows.
	 * <p>
	 * 1. If the monad is an actual idempotent, return true. This is trivial case.
	 * 2. If not, find first non-zero coefficient of the square of the monad and...
	 * a) re-scale monad by inverse of that coefficient. Use copy to avoid change.
	 * b) test the re-scaled monad to see if it is idenpotent. If so, return true.
	 * 3. Return false.
	 * <p>
	 * Since the map internal to a Monad's Scale can accept any of the CladosF
	 * numbers as values, there is a cast to a 'generic' type within this method.
	 * This would normally cause warnings by the compiler since the generic named in
	 * the internal map IS a UnitAbstract child AND casting an unchecked type could
	 * fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param pM  Monad
	 * @param <T> UnitAbstract number from CladosF with all interfaces this time.
	 * @return boolean
	 * @throws FieldException This exception is thrown when the method can't copy
	 *                        the field used by the monad to be checked.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UnitAbstract & Field & Normalizable> boolean isScaledIdempotent(Monad pM)
			throws FieldException {
		if (isIdempotent(pM))
			return true;
		else if (Monad.isNilpotent(pM, 2))
			return false;

		Monad check1 = CladosGBuilder.copyOfMonad(pM);
		check1.multiplyLeft(check1);
		Optional<Blade> first = check1.bladeStream().filter(blade -> check1.getScales().isNotZeroAt(blade)).sequential()
				.findFirst();
		if (first.isPresent()) {
			return isIdempotent(CladosGBuilder.copyOfMonad(pM)
					.scale((T) CladosFBuilder.copyOf(check1.getScales().get(first.get())).invert()));
		} else
			return false;
	}

	/**
	 * Return true if the Monad is nilpotent at a particular integer power.
	 * <p>
	 * @return boolean
	 * @param pM     Monad The monad to be tested
	 * @param pPower int The integer power to test
	 */
	public static boolean isNilpotent(Monad pM, int pPower) {
		if (isGZero(pM))
			return true;
		Monad check1 = CladosGBuilder.copyOfMonad(pM);
		while (pPower > 1) {
			check1.multiplyLeft(pM);
			if (isGZero(check1))
				return true;
			pPower--;
		}
		return false;
	}

	/**
	 * Return true if more than one blade is present in the Monad. This method makes
	 * use of the grade key which is a sum of powers of 10, thus the base-10
	 * logarithm will be an integer for pure grade monads and a non-integer for
	 * multigrade monads.
	 * <p>
	 * @param pM This parameter offers the Monad being tested.
	 * @return boolean
	 */
	public static boolean isMultiGrade(Monad pM) {
		if (pM.getGradeKey() != 0) {
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return false;

			return true;
		}
		return false; // Special case of a zero Monad which is not multigrade.
	}

	/**
	 * Return true if the Monad shares the same Reference frame as the passed Monad.
	 * A check is made on frameName, FootName, Signature, and FrameFoot for
	 * equality. No check is made for equality between Mnames and Coeffs and the
	 * product Table
	 * <p>
	 * @param pM Monad
	 * @param pN Monad
	 * @return boolean
	 */
	public static boolean isReferenceMatch(Monad pM, Monad pN) {
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
		else if (!pM.getScales().getCardinal().equals(pN.getScales().getCardinal()))
			return false;

		return true;
	}

	/**
	 * Return true if one blade is present in the Monad. This method makes use of
	 * the grade key which is a sum of powers of 10, thus the base-10 logarithm will
	 * be an integer for pure grade monads and a non-integer for multigrade monads.
	 * <p>
	 * @param pM This parameter offers the Monad being tested.
	 * @return boolean
	 */
	public static boolean isUniGrade(Monad pM) {
		if (pM.getGradeKey() != 0) {
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return true;

			return false;
		}
		return true;
	}

	/**
	 * Display XML string that represents the Monad
	 * <p>
	 * @param pM     MonadRealF This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * @return String
	 */
	public static String toXMLFullString(Monad pM, String indent) {
		if (indent == null)
			indent = "\t\t\t";
		StringBuilder rB = new StringBuilder(indent + "<Monad ");
		rB.append("gradeKey=\"").append(pM.getGradeKey()).append("\" ");
		rB.append("sparseFlag=\"").append(pM.getSparseFlag()).append("\" ");
		rB.append(">\n");
		rB.append(indent + "\t<Name>").append(pM.getName()).append("</Name>\n");
		rB.append(Algebra.toXMLString(pM.getAlgebra(), indent + "\t"));
		rB.append(indent + "\t<ReferenceFrame>\"").append(pM.getFrameName()).append("\"</ReferenceFrame>\n");
		rB.append(indent).append(pM.scales.toXMLString("\t"));
		rB.append(indent + "</Monad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Monad
	 * <p>
	 * @param pM     Monad This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * @return String
	 */
	public static String toXMLString(Monad pM, String indent) {
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
	 * All clados objects are elements of some algebra. That algebra has a name.
	 */
	protected Algebra algebra;

	private byte foundGrades;

	/**
	 * This String is the name of the Reference Frame of the Monad
	 */
	private String frameName;

	/**
	 * This long holds a key that shows which grades are present in the monad. The
	 * key is a sum over powers of 10 with the grade as the exponent.
	 */
	private long gradeKey;
	/**
	 * This is just a flag specifying the field type one should expect for
	 * coefficients of the monad.
	 */
	private final CladosField mode;
	/**
	 * All objects of this class have a name independent of all other features.
	 */
	private String name;

	/**
	 * This is the new coefficient 'array'. It's size should always match
	 * bladeCount. It is keyed to the blades in a monad's basis. It is fundamentally
	 * an IdentityHashMap with some frosting.
	 */
	protected Scale<? extends UnitAbstract> scales;

	/**
	 * This boolean is a flag used internally by multiplication methods to make
	 * those methods a little more efficient. A sparse monad has mostly zero
	 * coefficients and is directed to multiply grade by grade instead of using the
	 * generic algorithm.
	 */
	protected boolean sparseFlag = true;

	/**
	 * Simple copy constructor of Monad. Passed Monad will be copied in all details.
	 * This contructor is used most often to get around operations that alter a
	 * Monad when the developer does not wish it to be altered.
	 * <p>
	 * @param <T> CladosF number is a UnitAbstract child that implemnts Field and
	 *            Normalizable.
	 * @param pM  Monad
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field & Normalizable> Monad(Monad pM) {
		setName(pM.getName());
		setAlgebra(pM.getAlgebra());
		setFrameName(pM.getFrameName());
		mode = pM.mode;
		scales = new Scale<T>((Scale<T>) pM.getScales());
		setGradeKey();
	}

	/**
	 * Main copy constructor of Monad. Passed Monad will be copied in all details
	 * except its name. This constructor is used most often as a starting point to
	 * generate new Monads based on an old one.
	 * <p>
	 * @param pName String
	 * @param pM    Monad
	 */
	public Monad(String pName, Monad pM) {
		this(pM);
		setName(pName);
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create the default 'Zero' Monad.
	 * <p>
	 * @param <T>          CladosF number is a UnitAbstract child that implemnts
	 *                     Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           UnitAbstract Used to construct number
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
	public <T extends UnitAbstract & Field & Normalizable> Monad(String pMonadName, String pAlgebraName,
			String pFrameName, String pFootName, String pSig, T pF)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		this(pMonadName, pAlgebraName, pFrameName, CladosGBuilder.createFootLike(pFootName, pF), pSig, pF);
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create a default 'Zero' Monad while re-using the Foot of another.
	 * <p>
	 * @param <T>          CladosF number is a UnitAbstract child that implemnts
	 *                     Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFoot        Foot
	 * @param pSig         String
	 * @param pF           T generic for a CladosF number
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
	public <T extends UnitAbstract & Field & Normalizable> Monad(String pMonadName, String pAlgebraName,
			String pFrameName, Foot pFoot, String pSig, T pF)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		setName(pMonadName);
		setAlgebra(CladosGBuilder.createAlgebraWithFoot(pFoot, pF, pAlgebraName, pSig));
		setFrameName(pFrameName);

		if (pF instanceof RealF)
			mode = CladosField.REALF;
		else if (pF instanceof RealD)
			mode = CladosField.REALD;
		else if (pF instanceof ComplexF)
			mode = CladosField.COMPLEXF;
		else if (pF instanceof ComplexD)
			mode = CladosField.COMPLEXD;
		else
			throw new IllegalArgumentException("Offered Number must be a child of CladosF/UnitAbstract");

		switch (mode) {
		case COMPLEXD -> {
			scales = new Scale<ComplexD>(mode, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
			setGradeKey();
		}
		case COMPLEXF -> {
			scales = new Scale<ComplexF>(mode, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
			setGradeKey();
		}
		case REALD -> {
			scales = new Scale<RealD>(mode, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
			setGradeKey();
		}
		case REALF -> {
			scales = new Scale<RealF>(mode, this.getAlgebra().getGBasis(), pF.getCardinal()).zeroAll();
			setGradeKey();
		}
		}
	}

	/**
	 * Special constructor of Monad with most information passed in. 'Special Case'
	 * strings determine the coefficients automatically. 'Unit Scalar' and 'Unit
	 * PScalar' are recognized special cases. All unrecognized strings create a
	 * 'Zero' Monad by default.
	 * <p>
	 * @param <T>          CladosF number is a UnitAbstract child that implemnts
	 *                     Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           T generic for a CladosF number
	 * @param pSpecial     String
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered the default
	 *                                 constructor. The issues could involve null
	 *                                 coefficients or a coefficient array of the
	 *                                 wrong size.
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 */
	public <T extends UnitAbstract & Field & Normalizable> Monad(String pMonadName, String pAlgebraName,
			String pFrameName, String pFootName, String pSig, T pF, String pSpecial)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		this(pMonadName, pAlgebraName, pFrameName, pFootName, pSig, pF);
		// Default ZERO Monad is constructed already. Now handle the special cases.
		if (CladosConstant.MONAD_SPECIAL_CASES.contains(pSpecial)) {
			switch (mode) {
			case COMPLEXD -> {
				switch (pSpecial) {
				case "Unit Scalar" -> ((ComplexD) scales.getScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -Scalar" -> ((ComplexD) scales.getScalar()).setReal(CladosConstant.MINUS_ONE_F);
				case "Unit PScalar" -> ((ComplexD) scales.getPScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -PScalar" -> ((ComplexD) scales.getPScalar()).setReal(CladosConstant.MINUS_ONE_F);
				}
			}
			case COMPLEXF -> {
				switch (pSpecial) {
				case "Unit Scalar" -> ((ComplexF) scales.getScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -Scalar" -> ((ComplexF) scales.getScalar()).setReal(CladosConstant.MINUS_ONE_F);
				case "Unit PScalar" -> ((ComplexF) scales.getPScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -PScalar" -> ((ComplexF) scales.getPScalar()).setReal(CladosConstant.MINUS_ONE_F);
				}
			}
			case REALD -> {
				switch (pSpecial) {
				case "Unit Scalar" -> ((RealD) scales.getScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -Scalar" -> ((RealD) scales.getScalar()).setReal(CladosConstant.MINUS_ONE_F);
				case "Unit PScalar" -> ((RealD) scales.getPScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -PScalar" -> ((RealD) scales.getPScalar()).setReal(CladosConstant.MINUS_ONE_F);
				}
			}
			case REALF -> {
				switch (pSpecial) {
				case "Unit Scalar" -> ((RealF) scales.getScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -Scalar" -> ((RealF) scales.getScalar()).setReal(CladosConstant.MINUS_ONE_F);
				case "Unit PScalar" -> ((RealF) scales.getPScalar()).setReal(CladosConstant.PLUS_ONE_F);
				case "Unit -PScalar" -> ((RealF) scales.getPScalar()).setReal(CladosConstant.MINUS_ONE_F);
				}
			}
			}
		} // failure to find matching special case defaults to ZERO monad by doing
			// nothing.
		setGradeKey();
	}

	/**
	 * Main constructor of Monad with all information passed in.
	 * <p>
	 * @param <T>          CladosF number is a UnitAbstract child that implemnts
	 *                     Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pScale       Scale of CladosF numbers
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
	public <T extends UnitAbstract & Field & Normalizable> Monad(String pMonadName, String pAlgebraName,
			String pFrameName, String pFootName, String pSig, Scale<T> pScale)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		this(pMonadName, CladosGBuilder.createAlgebra(pScale.getScalar(), pAlgebraName, pFootName, pSig), pFrameName,
				pScale);
	}

	/**
	 * Main constructor of Monad with pre-constructed objects not already part of
	 * another Monad.
	 * <p>
	 * @param <T>        CladosF number is a UnitAbstract child that implemnts Field
	 *                   and Normalizable.
	 * @param pMonadName String
	 * @param pAlgebra   Algebra
	 * @param pFrameName String
	 * @param pScale     Scale of CladosF numbers
	 * @throws CladosMonadException This exception is thrown if there is an issue
	 *                              with the coefficients offered. The issues could
	 *                              involve null coefficients or a coefficient array
	 *                              of the wrong size.
	 */
	public <T extends UnitAbstract & Field & Normalizable> Monad(String pMonadName, Algebra pAlgebra, String pFrameName,
			Scale<T> pScale) throws CladosMonadException {
		if (pScale.getMap().size() != pAlgebra.getBladeCount())
			throw new CladosMonadException(this,
					"Coefficient array size does not match bladecount from offered Algebra.");

		setName(pMonadName);
		setAlgebra(pAlgebra);
		setFrameName(pFrameName);
		mode = pScale.getMode();
		scales = new Scale<T>(pScale);
		setGradeKey();
	}

	/**
	 * Monad Addition: (this + pM) This operation is allowed when the two monads use
	 * the same field and satisfy the Reference Matching test.
	 * <p>
	 * @param pM Monad
	 * @return Monad
	 */
	public Monad add(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Can't add monads when frames don't match.");
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
	 * This method returns the actual blades the underlying basis as a stream.
	 * <p>
	 * @return Stream of Blades in the underlying CanonicalBasis
	 */
	public Stream<Blade> bladeStream() {
		return algebra.getGBasis().bladeStream();
	}

	/**
	 * This method causes all coefficients of a monad to be conjugated.
	 * <p>
	 * @return Monad after operation.
	 */
	public Monad conjugate() {
		scales.conjugate();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract number from CladosF with all interfaces this time.
	 * @return Monad after operation.
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field & Normalizable> Monad dualLeft() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();

		int row = tBasis.getBladeCount() - 1; // row points at the PScalar blade
		Scale<T> newScales = new Scale<T>(mode, tBasis, scales.getCardinal()).zeroAll();
		bladeStream().filter(blade -> getScales().isNotZeroAt(blade)).parallel().forEach(blade -> {
			int col = tBasis.find(blade) - 1; // col points at a non-zero blade
			Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
			newScales.put(bMult,
					(T) CladosFBuilder.copyOf(getScales().get(blade)).scale(Float.valueOf(tProd.getSign(row, col))));
		});
		scales = newScales;

		setGradeKey();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract number from CladosF with all interfaces this time.
	 * @return Monad after operation.
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field & Normalizable> Monad dualRight() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();

		int row = tBasis.getBladeCount() - 1; // row points at the PScalar blade
		Scale<T> newScales = new Scale<T>(mode, tBasis, scales.getCardinal()).zeroAll();
		bladeStream().filter(blade -> getScales().isNotZeroAt(blade)).parallel().forEach(blade -> {
			int col = tBasis.find(blade) - 1; // col points at a non-zero blade
			Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
			newScales.put(bMult,
					(T) CladosFBuilder.copyOf(getScales().get(blade)).scale(Float.valueOf(tProd.getSign(col, row))));
		});
		scales = newScales;

		setGradeKey();
		return this;
	}

	/**
	 * This method returns the Algebra for this Monad.
	 * <p>
	 * @return Algebra
	 */
	public Algebra getAlgebra() {
		return algebra;
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract number from CladosF without the interfaces this time.
	 * @return UnitAbstract[]
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract> T[] getCoeff() {
		return (T[]) scales.getCoefficients();
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param i   int This points at the coefficient at the equivalent tuple
	 *            location.
	 * @param <T> UnitAbstract number from CladosF without the interfaces this time.
	 * @return UnitAbstract
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract> T getCoeff(int i) {
		if (i >= 0 & i < getAlgebra().getBladeCount())
			return (T) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		return null;
	}

	/**
	 * Return the name of the Reference Frame for this Monad
	 * <p>
	 * @return String
	 */
	public String getFrameName() {
		return frameName;
	}

	/**
	 * Return the grade key for the monad
	 * <p>
	 * @return long
	 */
	public long getGradeKey() {
		return gradeKey;
	}

	/**
	 * This answers a question concerning which type of UnitAbstract children are
	 * used.
	 * <p>
	 * @return CladosField mode for this monad
	 */
	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Simple gettor for name of the monad.
	 * <p>
	 * @return String Contains the name of the Monad.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the map relating basis blades to coefficients.
	 * <p>
	 * @return Scale of Blades and UnitAbstract children. This is the 'coefficients'
	 *         object.
	 */
	public Scale<? extends UnitAbstract> getScales() {
		return scales;
	}

	/**
	 * This method returns the sparse flag of the monad in case someone wants to
	 * know. It is just a gettor method, though.
	 * <p>
	 * @return boolean
	 */
	public boolean getSparseFlag() {
		return sparseFlag;
	}

	/**
	 * This method suppresses grades in the Monad not equal to the integer passed.
	 * <p>
	 * @param pGrade byte integer of the grade TO KEEP.
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 */
	public Monad gradePart(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAllButGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * This integer stream is OFTEN used internally in monads for calculations.
	 * Rather than type it out in long form, it is aliases to this method.
	 * <p>
	 * @return Integer stream ranging through all the grades of the algebra
	 */
	public IntStream gradeStream() {
		return IntStream.range(0, getAlgebra().getGradeCount());
	}

	/**
	 * This method suppresses the grade in the Monad equal to the integer passed.
	 * <p>
	 * @param pGrade byte integer of the grade TO SUPPRESS.
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 */
	public Monad gradeSuppress(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAtGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * Mirror the sense of all geometry generators in the Monad.
	 * <p>
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 */
	public Monad invert() {
		scales.invert();
		return this;
	}

	/**
	 * This method does a deep check for the equality of two monads. It is not meant
	 * for checking that two monad references actually point to the same object
	 * since that is easily handled with ==. This one checks algebras, foot names,
	 * frame names, and the coefficients. Each object owned by the monad has its own
	 * specialized isEqual() method that gets called.
	 * <p>
	 * Note that this could be done by override Object's equals() method. That might
	 * happen in the future, but thought will have to be given to how to override
	 * the hashing method too.
	 * <p>
	 * @param pM Monad
	 * @return boolean
	 */
	public boolean isGEqual(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			return false;
		switch (scales.getMode()) {
		case COMPLEXD -> {
			return bladeStream()
					.allMatch(blade -> ComplexD.isEqual((ComplexD) scales.get(blade), (ComplexD) pM.scales.get(blade)));
		}
		case COMPLEXF -> {
			return bladeStream()
					.allMatch(blade -> ComplexF.isEqual((ComplexF) scales.get(blade), (ComplexF) pM.scales.get(blade)));
		}
		case REALD -> {
			return bladeStream()
					.allMatch(blade -> RealD.isEqual((RealD) scales.get(blade), (RealD) pM.scales.get(blade)));
		}
		case REALF -> {
			return bladeStream()
					.allMatch(blade -> RealF.isEqual((RealF) scales.get(blade), (RealF) pM.scales.get(blade)));
		}
		default -> {
			return false;
		}
		}
	}

	/**
	 * Return the magnitude of the Monad
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract number from CladosF with the Field interface this
	 *            time.
	 * @return UnitAbstract but in practice it is always a child of UnitAbstract
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field> T magnitude() {
		return (T) scales.modulusSum();
	}

	/**
	 * Monad antisymmetric multiplication: 1/2(pM this - this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * <p>
	 * @param pM Monad
	 * @return Monad
	 */
	public Monad multiplyAntisymm(Monad pM) {
		if (!isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Symm multiply fails reference match.");
		Monad halfTwo = CladosGBuilder.copyOfMonad(this).multiplyRight(pM);
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			multiplyLeft(pM).subtract(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case COMPLEXF -> {
			multiplyLeft(pM).subtract(halfTwo).scale(ComplexF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		case REALD -> {
			multiplyLeft(pM).subtract(halfTwo).scale(RealD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case REALF -> {
			multiplyLeft(pM).subtract(halfTwo).scale(RealF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		}
		setGradeKey();
		return this;
	}

	/**
	 * Monad leftside multiplication: (pM this) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * <p>
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to
	 * find the non-zero grades. For example: gradeKey=101 means the monad is a sum
	 * of bivector and scalar because 10^2+10^0 = 101.
	 * <p>
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all
	 * blades less optimal. Instead, we parse gradeKey and loop through the blades
	 * for grades that could be non-ZERO.
	 * <p>
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd
	 * behavior if one sends in a complex numbers expecting against real numbers.
	 * What IS checked is the cardinal and that likely traps most errors that can be
	 * made. It's not perfect, though. If someone intentionally builds different
	 * number types using the same cardinal, they will get around the detection in
	 * place here.
	 * <p>
	 * What will happen in that case? The inbound numbers will be multiplied against
	 * coefficients as THEY understand multiplication. The inbound numbers gets cast
	 * to the other, so imaginary components won't get used in real number
	 * multiplication.
	 * <p>
	 * @param pM  Monad
	 * @param <T> UnitAbstract number from CladosF with all interfaces this time.
	 * @return Monad
	 */
	public <T extends UnitAbstract & Field & Normalizable> Monad multiplyLeft(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Left multiply fails reference match.");
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();

		Scale<T> newScales = new Scale<T>(mode, tBasis, scales.getCardinal()).zeroAll();
		if (sparseFlag) {
			long slideKey = gradeKey;
			byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
			while (logKey >= 0) {
				tBasis.bladeOfGradeStream(logKey).filter(blade -> getScales().isNotZeroAt(blade)).forEach(blade -> {
					int col = tBasis.find(blade) - 1;
					pM.bladeStream().filter(blade2 -> pM.getScales().isNotZeroAt(blade2)).parallel().forEach(blade2 -> {
						int row = tBasis.find(blade2) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
						switch (mode) {
						case COMPLEXD -> {
							try {
								ComplexD tAgg = ComplexD
										.multiply((ComplexD) getScales().get(blade),
												(ComplexD) pM.getScales().get(blade2))
										.scale(Double.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case COMPLEXF -> {
							try {
								ComplexF tAgg = ComplexF
										.multiply((ComplexF) getScales().get(blade),
												(ComplexF) pM.getScales().get(blade2))
										.scale(Float.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case REALD -> {
							try {
								RealD tAgg = RealD
										.multiply((RealD) getScales().get(blade), (RealD) pM.getScales().get(blade2))
										.scale(Double.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case REALF -> {
							try {
								RealF tAgg = RealF
										.multiply((RealF) getScales().get(blade), (RealF) pM.getScales().get(blade2))
										.scale(Float.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						}
					});
				});
				slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
				if (slideKey == 0)
					break; // we've processed all grades including scalar.
				logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
			} // while loop completion -> all grades in 'this' processed
		} else {
			bladeStream().filter(blade -> getScales().isNotZeroAt(blade)).forEach(blade -> {
				int col = tBasis.find(blade) - 1;
				pM.bladeStream().filter(blade2 -> pM.getScales().isNotZeroAt(blade2)).parallel().forEach(blade2 -> {
					int row = tBasis.find(blade2) - 1;
					Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
					switch (mode) {
					case COMPLEXD -> {
						try {
							ComplexD tAgg = ComplexD
									.multiply((ComplexD) getScales().get(blade), (ComplexD) pM.getScales().get(blade2))
									.scale(Double.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case COMPLEXF -> {
						try {
							ComplexF tAgg = ComplexF
									.multiply((ComplexF) getScales().get(blade), (ComplexF) pM.getScales().get(blade2))
									.scale(Float.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case REALD -> {
						try {
							RealD tAgg = RealD
									.multiply((RealD) getScales().get(blade), (RealD) pM.getScales().get(blade2))
									.scale(Double.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case REALF -> {
						try {
							RealF tAgg = RealF
									.multiply((RealF) getScales().get(blade), (RealF) pM.getScales().get(blade2))
									.scale(Float.valueOf(tProd.getSign(row, col))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					}
				});
			});
		}
		scales = newScales;
		setGradeKey();
		return this;
	}

	/**
	 * Monad rightside multiplication: (this pM) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * <p>
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to
	 * find the non-zero grades. For example: gradeKey=101 means the monad is a sum
	 * of bivector and scalar because 10^2+10^0 = 101.
	 * <p>
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all
	 * blades less optimal. Instead, we parse gradeKey and loop through the blades
	 * for grades that could be non-ZERO.
	 * <p>
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd
	 * behavior if one sends in a complex numbers expecting against real numbers.
	 * What IS checked is the cardinal and that likely traps most errors that can be
	 * made. It's not perfect, though. If someone intentionally builds different
	 * number types using the same cardinal, they will get around the detection in
	 * place here.
	 * <p>
	 * What will happen in that case? The inbound numbers will be multiplied against
	 * coefficients as THEY understand multiplication. The inbound numbers gets cast
	 * to the other, so imaginary components won't get used in real number
	 * multiplication.
	 * <p>
	 * @param pM  Monad
	 * @param <T> UnitAbstract number from CladosF with all interfaces this time.
	 * @return Monad
	 */
	public <T extends UnitAbstract & Field & Normalizable> Monad multiplyRight(Monad pM) {
		if (!isReferenceMatch(this, pM)) // Don't try if not a reference match
			throw new IllegalArgumentException("Right multiply fails reference match.");
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();

		Scale<T> newScales = new Scale<T>(mode, tBasis, scales.getCardinal()).zeroAll();
		if (sparseFlag) {
			long slideKey = gradeKey;
			byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
			while (logKey >= 0) {
				tBasis.bladeOfGradeStream(logKey).filter(blade -> getScales().isNotZeroAt(blade)).forEach(blade -> {
					int col = tBasis.find(blade) - 1;
					pM.bladeStream().filter(blade2 -> pM.getScales().isNotZeroAt(blade2)).parallel().forEach(blade2 -> {
						int row = tBasis.find(blade2) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
						switch (mode) {
						case COMPLEXD -> {
							try {
								ComplexD tAgg = ComplexD
										.multiply((ComplexD) getScales().get(blade),
												(ComplexD) pM.getScales().get(blade2))
										.scale(Double.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case COMPLEXF -> {
							try {
								ComplexF tAgg = ComplexF
										.multiply((ComplexF) getScales().get(blade),
												(ComplexF) pM.getScales().get(blade2))
										.scale(Float.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case REALD -> {
							try {
								RealD tAgg = RealD
										.multiply((RealD) getScales().get(blade), (RealD) pM.getScales().get(blade2))
										.scale(Double.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						case REALF -> {
							try {
								RealF tAgg = RealF
										.multiply((RealF) getScales().get(blade), (RealF) pM.getScales().get(blade2))
										.scale(Float.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
								newScales.put(bMult, tAgg);
							} catch (FieldBinaryException e) {
								throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
							}
						}
						}
					});
				});
				slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
				if (slideKey == 0)
					break; // we've processed all grades including scalar.
				logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
			} // while loop completion -> all grades in 'this' processed
		} else {
			bladeStream().filter(blade -> getScales().isNotZeroAt(blade)).forEach(blade -> {
				int col = tBasis.find(blade) - 1;
				pM.bladeStream().filter(blade2 -> pM.getScales().isNotZeroAt(blade2)).parallel().forEach(blade2 -> {
					int row = tBasis.find(blade2) - 1;
					Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
					switch (mode) {
					case COMPLEXD -> {
						try {
							ComplexD tAgg = ComplexD
									.multiply((ComplexD) getScales().get(blade), (ComplexD) pM.getScales().get(blade2))
									.scale(Double.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case COMPLEXF -> {
						try {
							ComplexF tAgg = ComplexF
									.multiply((ComplexF) getScales().get(blade), (ComplexF) pM.getScales().get(blade2))
									.scale(Float.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case REALD -> {
						try {
							RealD tAgg = RealD
									.multiply((RealD) getScales().get(blade), (RealD) pM.getScales().get(blade2))
									.scale(Double.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					case REALF -> {
						try {
							RealF tAgg = RealF
									.multiply((RealF) getScales().get(blade), (RealF) pM.getScales().get(blade2))
									.scale(Float.valueOf(tProd.getSign(col, row))).add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException ex) {
							throw new IllegalArgumentException("Left multiply fails UnitAbstract reference match.");
						}
					}
					}
				});
			});
		}
		scales = newScales;
		setGradeKey();
		return this;
	}

	/**
	 * Monad symmetric multiplication: 1/2(pM this + this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * <p>
	 * @param pM Monad
	 * @return Monad
	 */
	public Monad multiplySymm(Monad pM) {
		if (!isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Symm multiply fails reference match.");
		Monad halfTwo = CladosGBuilder.copyOfMonad(this).multiplyRight(pM);
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			multiplyLeft(pM).add(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case COMPLEXF -> {
			multiplyLeft(pM).add(halfTwo).scale(ComplexF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		case REALD -> {
			multiplyLeft(pM).add(halfTwo).scale(RealD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case REALF -> {
			multiplyLeft(pM).add(halfTwo).scale(RealF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		}
		setGradeKey();
		return this;
	}

	/**
	 * Normalize the monad. A <b>CladosMonadException</b> is thrown if the Monad has
	 * a zero magnitude.
	 * <p>
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 * @throws FieldException This exception is thrown when normalizing a zero or
	 *                        field conflicted monad is tried.
	 */
	public Monad normalize() throws FieldException {
		if (gradeKey == 0L & scales.isScalarZero())
			throw new FieldException(null, "Normalizing a zero magnitude Monad isn't possible");
		scales.normalize();
		return this;
	}

	/**
	 * This method is a concession to the old notation for the PScalar Part of a
	 * monad. It returns the pscalar part coefficient.
	 * <p>
	 * @return UnitAbstract but in practice it is always a child of UnitAbstract
	 */
	@Deprecated
	public UnitAbstract PSPc() {
		return scales.getPScalar();
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of all
	 * the permutation, so the easiest thing to do is to change the coefficients
	 * instead.
	 * <p>
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 */
	public Monad reverse() {
		scales.reverse();
		return this;
	}

	/**
	 * Monad Scaling: (this * real number) Only the Monad coefficients are scaled by
	 * the real number.
	 * <p>
	 * NOTE that the mode of the inbound scaling number is NOT checked. That can
	 * lead to odd behavior if one sends in a complex number expecting to scale a
	 * real number. What IS checked is the cardinal and that likely traps most
	 * errors that can be made. It's not perfect, though. If someone intentionally
	 * builds different number types using the same cardinal, they will get around
	 * the detection in place here. What will happen in that case? The inbound
	 * number will be multiplied against coefficients as THEY understand
	 * multiplication. The inbound number gets cast to the other, so imaginary
	 * components won't get used in real number multiplication.
	 * <p>
	 * @param pScale UnitAbstract to use for scaling the monad
	 * @param <T>    UnitAbstract number from CladosF with the Field interface.
	 * @return Monad after the scaling is complete.
	 */
	public <T extends UnitAbstract & Field> Monad scale(T pScale) {
		scales.scale((T) pScale);
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
	 * <p>
	 * @param <T> UnitAbstract child
	 * @param ppC UnitAbstract child array
	 * @return Monad after setting the coefficients to the offered array.
	 * @throws CladosMonadException This exception is thrown when the array offered
	 *                              for coordinates is of the wrong length.
	 */
	public <T extends UnitAbstract & Field & Normalizable> Monad setCoeff(T[] ppC) throws CladosMonadException {
		if (ppC.length != getAlgebra().getBladeCount())
			throw new CladosMonadException(this, "Coefficient array passed for coefficient copy is wrong length");
		scales.setCoefficientArray(CladosFListBuilder.copyOf(mode, (T[]) ppC));
		setGradeKey();
		return this;
	}

	/**
	 * Reset the name used for the Reference Frame for this Monad This operation
	 * would take place to point out a passive rotation or translation or any other
	 * alteration to the reference frame.
	 * <p>
	 * @param pRName String
	 */
	public void setFrameName(String pRName) {
		getAlgebra().removeFrame(frameName);
		frameName = pRName;
		getAlgebra().appendFrame(pRName);
	}

	/**
	 * Set the grade key for the monad. Never accept an externally provided key.
	 * Always recalculate it after any of the unary or binary operations.
	 * <p>
	 * While we are here, we ALSO set the sparseFlag. The nonZero coeff detection
	 * loop that fills gradeKey is a grade detector, so if foundGrade is less than
	 * or equal to half gradeCount, sparseFlag is set to true and false otherwise.
	 */
	public void setGradeKey() {
		foundGrades = 0;
		gradeKey = 0;

		gradeStream().forEach(grade -> {
			if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
					.filter(blade -> getScales().isNotZeroAt(blade)).parallel().findAny().isPresent()) {
				foundGrades++;
				gradeKey += Math.pow(10, grade);
			}
		});

		if (gradeKey == 0) {
			foundGrades++;
			gradeKey++;
		}
		sparseFlag = (foundGrades < getAlgebra().getGradeCount() / 2) ? true : false;
	}

	/**
	 * Simple setter of the name of the monad.
	 * <p>
	 * @param pName String name of the monad to set
	 */
	public void setName(String pName) {
		name = pName;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * <p>
	 * @return UnitAbstract but in practice it is always a child of UnitAbstract
	 */
	@Deprecated
	public UnitAbstract SPc() {
		return scales.getScalar();
	}

	/**
	 * Return the magnitude squared of the Monad
	 * <p>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a UnitAbstract child AND casting an unchecked type could fail
	 * at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract number from CladosF without the interfaces this time.
	 * @return UnitAbstract but in practice it is always a child of UnitAbstract
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract> T sqMagnitude() {
		return (T) scales.modulusSQSum();
	}

	/**
	 * Monad Subtraction: (this - pM) This operation is allowed when the two monads
	 * use the same field and satisfy the Reference Matching test.
	 * <p>
	 * @param pM Monad
	 * @return Monad
	 */
	public Monad subtract(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Can't subtract monads without a reference match.");
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

	/**
	 * Simple setter method of the algebra for this monad.
	 * <p>
	 * It is NOT advisable to re-set algebras lightly. They carry the meaning of
	 * 'directions' in the underlying basis.
	 * <p>
	 * @param pA Algebra to set
	 */
	protected void setAlgebra(Algebra pA) {
		algebra = pA;
	}

	/**
	 * This method is called every time the gradeKey is set to determine whether the
	 * sparseFlag should be set. The technique involves taking the log10 of the
	 * gradeKey and truncating it. The first time through, one can get any integer
	 * between 1 and gradeCount inclusive. Before the loop iterates, that integer is
	 * used to subtract 10^logKey from gradeKey. That ensures the next pass through
	 * the loop will produce an integer between 1 and the next lower grade unless
	 * the one just found was the scalar grade. Once the scalar grade is found,
	 * logKey=0, tempGradeKey=0, and the loop breaks out.
	 * <p>
	 * If the number of found grades is less than or equal to half the grades the
	 * sparse flag is set to true. Otherwise it is set to false.
	 * <p>
	 * This method isn't actually used by child classes because the method for
	 * setting the gradeKey does the same detection one coefficient at a time
	 * breaking out when a non-zero coeff is found. Incrementing foundGrades in that
	 * loop suffices. Still... there might be a need for this method elsewhere
	 * later.
	 * <p>
	 * @param pGrades short The parameter is the gradeCount for the monad. It is
	 *                passed into this method rather than looked up in order to
	 *                allow this method to reside in the Monad class. If it were in
	 *                one of the child monad classes, it would work just as well,
	 *                but it would have to know the child algebra class too in order
	 *                to avoid UnitAbstract confusion. Since a monad can be sparse
	 *                or not independent of the UnitAbstract used, the method is
	 *                placed here in the abstract parent.
	 */
	protected void setSparseFlag(byte pGrades) {
		long slideKey = gradeKey;
		byte logSlide = (byte) Math.log10(slideKey); // highest grade found
		foundGrades = 0; // This will be the number of grades found in the key.
		// There will always be one trip through the next while loop because zero is a
		// scalar and its logSlide=1.
		while (logSlide >= 0) {
			foundGrades++;
			if (logSlide == 0)
				break; // we processed all grades including scalar.
			// logSlide grade processed so remove it from slideKey and recompute logSlide.
			slideKey -= Math.pow(10, logSlide);
			logSlide = (byte) Math.log10(slideKey);
		}
		if (gradeKey > 1)
			foundGrades--; // Don't get credit for scalar when other grades present.
		if (foundGrades < pGrades / 2)
			sparseFlag = true;
		else
			sparseFlag = false;
	}
}