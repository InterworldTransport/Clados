/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Monad<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Monad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.interworldtransport.cladosG.CladosConstant.*;

import org.interworldtransport.cladosF.FBuilder;		//Number builder
import org.interworldtransport.cladosF.FListBuilder;	//List of numbers builder
import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;		//Numeric modes enumerated
import static org.interworldtransport.cladosF.CladosField.*;
import org.interworldtransport.cladosF.ComplexD;		//Complex doubles
import org.interworldtransport.cladosF.ComplexF;		//Complex floats
import org.interworldtransport.cladosF.RealD;			//Real doubles
import org.interworldtransport.cladosF.RealF;			//Real floats
import org.interworldtransport.cladosF.ProtoN;			//Unitized Number parent
import org.interworldtransport.cladosF.Field;			//Contract specifying division field
import org.interworldtransport.cladosF.Normalizable;	//Contract for modulus construction
														//Numbers obeying both contracts
														//are used as monad weights.

import org.interworldtransport.cladosFExceptions.*;
import org.interworldtransport.cladosGExceptions.*;

/**
 * A CladosG Monad is better known as a multivector to anyone with experience with geometric algebras. 
 * Think of them as vectors, but with higher ranked elements also represented. There is more to it than that, 
 * but that is the nutshell version.
 * <br><br>
 * Caution | To the software community, 'vector' is generally understood to be a dynamic array data structure. 
 * To a physicist, it is a thing that belongs to a vector space and follows extra rules regarding allowed 
 * transformations. It's 'thing' nature is much more important than how it acts as a data structure.
 * <br><br>
 * The 'allowed' transformations are operations that do not change the 'thing' nature of the object represented. 
 * For example, a meter stick is what it is no matter how a coordinate system used to represent it is rotated. A
 * representation of the meter stick would have to be identifiable as the same thing after a rotation, meaning 
 * many apparently different sets of data in the structure are actually the same thing.
 * <br><br>
 * A 'multivector' can represent 'things' that are of higher geometric rank than lines. A monad has a data 
 * structure inside to support both coordinates and reference information. 'Allowed' transformations on the 
 * reference frame should cause the coefficients to shift the correct way leaving the monad as it was from an 
 * external perspective. That's HOW a monad represents a thing. It is expected to be invariant under 'allowed' 
 * transformations of the contained data.
 * <br><br>
 * Why 'monad' instead of 'multivector'? Try typing it yourself a few thousand times and you'll understand. The name 
 * doesn't actually matter, but it is from an old tradition when physicists wrote linear transformations as dyads. 
 * Two vectors were written side by side, but no simplifying action could be taken. The dyad was described as an 
 * operator, so the vectors were applied to another operand rather than each other. Few textbooks do that anymore 
 * (we have matrix algebra now) and the name has fallen into dis-use. It is revived here because we need something 
 * short and useful. A 'dyad' will be two monads in a list which is generalized in another CladosG class called nyad.
 * <br><br>
 * Doesn't 'monad' conflict with mathemetician's usage in Category Theory and with Functional Programming advocates
 * bringing that mathematics to the real world? Yes. What the functional programmers are doing is terribly important,
 * so don't confuse their monad with what a physicist needs. This shouldn't be too hard. Y'all have been doing it 
 * for 'vector' for a few decades. You'd rather something else? 'Unad'? Make your case by helping out.
 * <br><br>
 * NOTE | Regarding suppressed unchecked type casting warnings, they are restricted to the casting that happens in 
 * FBuilder and CladosFListBuilder classes mostly. This happens when we copy number objects to avoid mutability 
 * using a generic copyOf() method. As long as the coefficients in a monad are valid ProtoN children implementing 
 * Field and Normalizable, the copyOf() functions will work fine. There are two cases where things can go awry.
 * <br><br>
 * 1. It is probably possible for someone to mix ProtoN children in a Scale object containing a mondad's 
 * coefficients. The copyOf() functions will faithfully copy them as they are. The scale() methods and others will 
 * faithfully pass them to a Scale object to be used as appropriate there. For example, Scale's scale() method will 
 * try to use them as given. What happens, though, is the inbound number gets multiplied against others 
 * AS THEY UNDERSTAND MULTIPLICATION. Scaling a complex by a real will work all right unless one thought the 
 * scaling was between two complex numbers. THAT'S why Scale AND Monad implement Modal, but nothing is enforced yet.
 * <br><br>
 * 2. If someone invents a new ProtoN child, there is a ton of work to do as the builders and other enumerations 
 * have to be adapted. Any class implementing Modal might have methods that switch on CladosField values. 
 * So... be cautious about inventing new CladosF numbers. Lots of work will have to be done.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class Monad implements Modal, Unitized {
	/**
	 * This method sifts through the gradeKey and builds a boolean array. Entries are true/false at an index where the grade is 
	 * present/not present.
	 * <br><br>
	 * @param pM Monad to examine when building the grade mask
	 * @return boolean array as long as there are grades in the offered monad. Entries are true/false if grade is/isn't present.
	 */
	public static final boolean[] getGradeMask(Monad pM) {
		boolean[] mask = new boolean[pM.getAlgebra().getGradeCount()];
		long slideKey = pM.getGradeKey();					//The trick involves picking off powers of 10 from gradeKey
		int logKey = (int) Math.log10(slideKey); 			//logKey has the highest grade with non-zero blades
		while (logKey >= 0) {
			mask[logKey] = true;							//put that grade in the mask
			if (logKey == 0)					break;		//All grades are done if true.
			slideKey -= pow((byte) 10, logKey); 			//Subtracting 10^logKey sets the stage for the next lower grade
			logKey = (int) Math.log10(slideKey); 			//Reset logKey for the loop condition. If zero it will be the last time through.
		} 													//While loop complete -> all non-zero grades set TRUE in mask
		return mask;
	}

	/**
	 * Return a boolean if the grade being checked is non-zero in the Monad.
	 * <br><br>
	 * The grade key is checked using a bit of trickery with integer math. Divide the key by 10^grade and toss the remainder. If the 
	 * result is odd the grade is present. If even, it isn't. The depends on the technique used to build the key in the first place.
	 * <br><br>
	 * @param pM     Monad to be checked for a grade
	 * @param pGrade int grade to be checked
	 * @return boolean answering the question whether the grade is present.
	 */
	public static boolean hasGrade(Monad pM, int pGrade) {
		if (pM.getGradeKey() == 1 & pGrade == 0) 						return true;
		if (((pM.getGradeKey()) / (pow((byte) 10, pGrade))) % 2 == 1)	return true;
		else 															return false;
	}

	/**
	 * Return a boolean if the grade being checked is the grade of the Monad. False
	 * is returned otherwise.
	 * <br>
	 * The grade key is checked. A simple power of 10 is a single grade. No special 
	 * carve-out is needed for the scalar because 10^0 == 1.
	 * <br>
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
	 * <br>
	 * @param pM MonadComplexD This is the monad to be tested.
	 * @return boolean
	 */
	public static boolean isGZero(Monad pM) {
		return (pM.getGradeKey() == 1 & pM.getWeights().isScalarZero());
	}

	/**
	 * Return true if the Monad an idempotent
	 * <br>
	 * @return boolean
	 * @param pM Monad
	 */
	public static boolean isIdempotent(Monad pM) {
		if (isGZero(pM))
			return true;
		return (GBuilder.copyOfMonad(pM)).multiplyLeft(pM).isGEqual(pM);
	}

	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * <br>
	 * The strategy for this method is as follows.
	 * <br>
	 * 1. If the monad is an actual idempotent, return true. This is trivial case.
	 * 2. If not, find first non-zero coefficient of the square of the monad and...
	 * a) re-scale monad by inverse of that coefficient. Use copy to avoid change.
	 * b) test the re-scaled monad to see if it is idenpotent. If so, return true.
	 * 3. Return false.
	 * <br>
	 * Since the map internal to a Monad's Scale can accept any of the CladosF
	 * numbers as values, there is a cast to a 'generic' type within this method.
	 * This would normally cause warnings by the compiler since the generic named in
	 * the internal map IS a ProtoN child AND casting an unchecked type could
	 * fail at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param pM  Monad
	 * @param <T> ProtoN number from CladosF with all interfaces this time.
	 * @return boolean
	 * @throws FieldException This exception is thrown when the method can't copy
	 *                        the field used by the monad to be checked.
	 */
	public static <T extends ProtoN & Field & Normalizable> boolean isScaledIdempotent(Monad pM)
			throws FieldException {
		if (isIdempotent(pM))					return true;
		if (Monad.isNilpotent(pM, 2))	return false;

		Monad check1 = GBuilder.copyOfMonad(pM);				//Work with a copy of the monad to avoid altering it
		check1.multiplyLeft(check1);							//Multiply it by itself. Doesn't matter which side.
		Optional<Blade> first = check1.bladesNotZeroStream().sequential().findFirst();				//Find first non-zero blade
		if (first.isPresent())					return isIdempotent(GBuilder.copyOfMonad(pM)		//Return an idepotent test of a copy
					/* scaled by that non-zero weight */					.scale((T) FBuilder	.copyOf(check1.get(first.get()))
					/* inverted */																.invert()));
		else									return false;
	}

	/**
	 * Return true if the Monad is nilpotent at a particular integer power.
	 * <br>
	 * @return boolean
	 * @param pM     Monad The monad to be tested
	 * @param pPower int The integer power to test
	 */
	public static boolean isNilpotent(Monad pM, int pPower) {
		if (isGZero(pM))
			return true;
		Monad check1 = GBuilder.copyOfMonad(pM);
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
	 * <br>
	 * @param pM This parameter offers the Monad being tested.
	 * @return boolean
	 */
	public static boolean isMultiGrade(Monad pM) {
		if (pM.getGradeKey() == 0)	//Special case should never happen, 
			return false;			//but if it does it is fatal.
		float temp = (float) Math.log10(pM.getGradeKey());
		if (Math.floor(temp) == temp)	//This avoids precision trap?
			return false;

		return true;
	}

	/**
	 * Return true if the monads share the same algebra, modes, and cardinal/units.
	 * <br>
	 * A check is made on Algebra, Mode, and Scale Cardinals for equality. No check is made for equality 
	 * between the monad names, numeric weights, and the Cayley Table.
	 * <br>
	 * @param pM Monad to be tested
	 * @param pN Monad to be tested
	 * @return boolean True if they pass algebra and cardinal/unit match tests
	 */
	public static boolean isReferenceMatch(Monad pM, Monad pN) {
		
		if (pM.getAlgebra() != pN.getAlgebra())		// The algebras must be the same object to match.
			return false;							// If they are not, this test fails.

		return Monad.isUnitMatch(pM, pN);			// The weights might share different cardinals or modes.
													// If so, we'd be comparing apples to oranges.
	}

	/**
	 * Return true if one blade is present in the Monad. This method makes use of
	 * the grade key which is a sum of powers of 10, thus the base-10 logarithm will
	 * be an integer for pure grade monads and a non-integer for multigrade monads.
	 * <br>
	 * @param pM This parameter offers the Monad being tested.
	 * @return boolean
	 */
	public static boolean isUniGrade(Monad pM) {
		if (pM.getGradeKey() == 0)		return false;	//Edge case shouldn't happen, but if it does it is fatal.

		float temp = (float) Math.log10(pM.getGradeKey());
		if (Math.floor(temp) == temp)	return true;	//This avoids precision trap?

		return false;
	}

	/**
	 * Return true if the monads share the same Cardinal/units and Mode
	 * A check is made on Scale Cardinals for equality. No check is made for equality 
	 * between the monad names, numeric weights, and algebras.
	 * <br>
	 * @param pM Monad to be tested
	 * @param pN Monad to be tested
	 * @return boolean True if they pass cardinal/unit match test
	 */
	public static boolean isUnitMatch(Monad pM, Monad pN) {
		if (pM.getMode() != pN.getMode() )			// Modes must match to avoid FieldBinaryExceptions elsewhere.
			return false;							// If they don't, fail the test.
		return pM.getWeights().getCardinal().equals(pN.getWeights().getCardinal());
													// Different cardinals means comparing apples to oranges.
	}

	/**
	 * Project the second Monad into the algebra of the first where it is assumed that the two algebras share the same basis. 
	 * In that rare case, the algebra distinctions are merely bookkeeping tricks.
	 * <br><br>
	 * Also project onto the units of the first monad. Basically point at the other cardinal.
	 * <br><br>
	 * @param pLeft the monad acting as a source of an algebra to project into
	 * @param pRight the monad to be projected
	 * @return Monad which has been pressed into the other algebra
	 */
	public static Monad projectReference(Monad pLeft, Monad pRight) {

		//Scale<T> tempRightWeights = pRight.getWeights();
		//Algebra tempLeftAlg = pLeft.getAlgebra();
		//Basis tempLeftBasis = tempLeftAlg.getGBasis();
		//Scale<T> newRightScale = new Scale<>(pRight.getMode(), tempLeftBasis, tempRightWeights.getCardinal());

		//tempLeftBasis.bladeStream().forEach(blade -> {
		//	newRightScale.put(blade, (T) tempRightWeights.get(blade));
		//	});;

		// Because 'blade' is the same in left and right monads, there is no need to recast the Scale for pRight.
		// If this is EVER to work with different bases, there must be a map (a frame?) supporting calculation
		// of linear combination weight from the old basis to use for each blade in the new basis. 
		
		// Ken's old connector idea had the bases line up. Algebra distinctions were bookkeeping methods.
		// Truth is... we can probably recover that without nyads by using a dual generator to double a basis size
		// and place one of the monads in the degenerate extension. Weird, but it might work.

		pRight.setAlgebra(pLeft.getAlgebra());
		pRight.getWeights().setCardinal(pLeft.getWeights().getCardinal());

		return pRight;
	}

	/**
	 * All clados objects are elements of some algebra. That algebra has a name.
	 */
	protected Algebra algebra;

	/*
	 * Grades found among the parts of this monad.
	 */
	private byte foundGrades;

	/**
	 * This long holds a key that shows which grades are present in the monad. The
	 * key is a sum over powers of 10 with the grade as the exponent.
	 */
	private long gradeKey;

	/**
	 * All objects of this class have a name independent of all other features.
	 */
	private String name;

	/**
	 * This is the new coefficient 'array'. It's size should always match
	 * bladeCount. It is keyed to the blades in a monad's basis. It is fundamentally
	 * an TreeMap with some frosting.
	 */
	protected Scale<? extends ProtoN> scales;

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
	 * <br><br>
	 * @param pM  Monad to be copied
	 */
	public Monad(Monad pM) {
		setName(pM.getName());
		setAlgebra(pM.getAlgebra());
		scales = new Scale<>(pM.getWeights());
		setGradeKey();
	}

	/**
	 * Main copy constructor of Monad. Passed Monad will be copied in all details
	 * except its name. This constructor is used most often as a starting point to
	 * generate new Monads based on an old one.
	 * <br><br>
	 * @param pName String
	 * @param pM    Monad
	 */
	public Monad(String pName, Monad pM) {
		this(pM);			//Defer to constructor #1
		setName(pName);
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create the default 'Zero' Monad.
	 * <br><br>
	 * @param <T>          CladosF number is a ProtoN child that implemnts Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           ProtoN Used to construct number
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad(	String pMonadName, 
															String pAlgebraName,
															String pFootName, 
															String pSig, 
															T pF)
			throws BadSignatureException, CladosMonadException {
		this(	pMonadName, 
				pAlgebraName,
				GBuilder.createFootLike(pFootName, pF), 
				pSig, 
				pF);		//Defer to constructor #4
	}

	/**
	 * Special constructor of Monad with most information passed in. This one will
	 * create a default 'Zero' Monad while re-using the Foot of another.
	 * <br>
	 * @param <T>          CladosF number is a ProtoN child that implemnts Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFoot        Foot
	 * @param pSig         String
	 * @param pF           T generic for a CladosF number
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad(	String pMonadName, 
															String pAlgebraName,
															Foot pFoot, 
															String pSig, 
															T pF)
			throws BadSignatureException, CladosMonadException {
		setName(pMonadName);
		setAlgebra(GBuilder.createAlgebraWithFoot(pFoot, pAlgebraName, pSig));

		switch (pF.getClass().getCanonicalName()){
			case "org.interworldtransport.cladosF.RealF" -> {
				scales = new Scale<RealF>(REALF, this.getAlgebra().getBasis(), pF.getCardinal()).zeroAll();
				break;
			}
			case "org.interworldtransport.cladosF.RealD" -> {
				scales = new Scale<RealD>(REALD, this.getAlgebra().getBasis(), pF.getCardinal()).zeroAll();
				break;
			}
			case "org.interworldtransport.cladosF.ComplexF" -> {
				scales = new Scale<ComplexF>(COMPLEXF, this.getAlgebra().getBasis(), pF.getCardinal()).zeroAll();
				break;
			}
			case "org.interworldtransport.cladosF.ComplexD" -> {
				scales = new Scale<ComplexD>(COMPLEXD, this.getAlgebra().getBasis(), pF.getCardinal()).zeroAll();
				break;
			}
			default -> throw new IllegalArgumentException("Offered Number must be a child of CladosF/ProtoN");
		}
		setGradeKey();
	}

	/**
	 * Special constructor of Monad with most information passed in. 'Special Case'
	 * strings determine the coefficients automatically. 'Unit Scalar' and 'Unit
	 * PScalar' are recognized special cases. All unrecognized strings create a
	 * 'Zero' Monad by default.
	 * <br>
	 * @param <T>          CladosF number is a ProtoN child that implemnts
	 *                     Field and Normalizable.
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pF           T generic for a CladosF number
	 * @param pSpecial     String
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered the default
	 *                                 constructor. The issues could involve null
	 *                                 coefficients or a coefficient array of the wrong size.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad(	String pMonadName, 
															String pAlgebraName,
															String pFootName, 
															String pSig, 
															T pF, 
															String pSpecial)
			throws BadSignatureException, CladosMonadException {
		this(	pMonadName, 
				pAlgebraName, 
				GBuilder.createFootLike(pFootName, pF), 
				pSig, 
				pF);	//Defer to Constructor #4
						// Default ZERO Monad is complete. 
						// Now handle the special cases.
		if (MONAD_SPECIAL_CASES.contains(pSpecial)) {
			switch (getMode()) {
				case COMPLEXD -> {
					switch (pSpecial) {
						case "Unit Scalar" -> {
							((ComplexD) scales.getScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -Scalar" -> {
							((ComplexD) scales.getScalar()).setReal(MINUS_ONE_F);
							break;
						}
						case "Unit PScalar" -> {
							((ComplexD) scales.getPScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -PScalar" -> {
							((ComplexD) scales.getPScalar()).setReal(MINUS_ONE_F);
							break;
						}
					}
					break;
				}
				case COMPLEXF -> {
					switch (pSpecial) {
						case "Unit Scalar" -> {
							((ComplexF) scales.getScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -Scalar" -> {
							((ComplexF) scales.getScalar()).setReal(MINUS_ONE_F);
							break;
						}
						case "Unit PScalar" -> {
							((ComplexF) scales.getPScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -PScalar" -> {
							((ComplexF) scales.getPScalar()).setReal(MINUS_ONE_F);
							break;
						}
					}
				}
				case REALD -> {
					switch (pSpecial) {
						case "Unit Scalar" -> {
							((RealD) scales.getScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -Scalar" -> {
							((RealD) scales.getScalar()).setReal(MINUS_ONE_F);
							break;
						}
						case "Unit PScalar" -> {
							((RealD) scales.getPScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -PScalar" -> {
							((RealD) scales.getPScalar()).setReal(MINUS_ONE_F);
							break;
						}
					}
				}
				case REALF -> {
					switch (pSpecial) {
						case "Unit Scalar" -> {
							((RealF) scales.getScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -Scalar" -> {
							((RealF) scales.getScalar()).setReal(MINUS_ONE_F);
							break;
						}
						case "Unit PScalar" -> {
							((RealF) scales.getPScalar()).setReal(PLUS_ONE_F);
							break;
						}
						case "Unit -PScalar" -> {
							((RealF) scales.getPScalar()).setReal(MINUS_ONE_F);
							break;
						}
					}
				}
			}
		} // failure to find matching special case defaults to ZERO monad by doing nothing.
		setGradeKey();
	}

	/**
	 * Main constructor of Monad with all information passed in.
	 * <br><br>
	 * @param pMonadName   String
	 * @param pAlgebraName String
	 * @param pFootName    String
	 * @param pSig         String
	 * @param pScale       Scale of CladosF numbers
	 * @throws BadSignatureException   This exception is thrown if the signature
	 *                                 string offered is rejected.
	 * @throws CladosMonadException    This exception is thrown if there is an issue
	 *                                 with the coefficients offered. The issues
	 *                                 could involve null coefficients or a
	 *                                 coefficient array of the wrong size.
	 */
	public Monad(String pMonadName, String pAlgebraName, String pFootName,  String pSig, Scale<?> pScale)
			throws BadSignatureException, CladosException {

		this(	pMonadName, 
				GBuilder.createAlgebraWithFootGP(
										GBuilder.createFoot(pFootName, pScale.getCardinal().getUnit()), 
										GBuilder.createGProduct(Optional.ofNullable(pScale.getBasis()), pSig),
										pAlgebraName), 
				pScale);				//Defer to constructor #7 safely
										//because pScale's Basis was used building Algebra.
	}

	/**
	 * Main constructor of Monad with pre-constructed objects not already part of another Monad.
	 * <br><br>
	 * This one is very important to GBuilder for ensuring reference matches occur correctly, but it
	 * does reject construction if the bases in Algebra and Scale do not match.
	 * <br><br>
	 * @param pMonadName String
	 * @param pAlgebra   Algebra
	 * @param pScale     Scale of CladosF numbers
	 * @throws CladosMonadException This exception is thrown if there is an issue
	 *                              with the coefficients offered. The issues could
	 *                              involve null coefficients or a coefficient array
	 *                              of the wrong size.
	 */
	public Monad(String pMonadName, Algebra pAlgebra, Scale<?> pScale) throws CladosException {
		if (pScale.getBasis() != pAlgebra.getBasis())
			throw new CladosException("Scale basis must match exactly the basis in Algebra.");

		setName(pMonadName);
		setAlgebra(pAlgebra);
		scales = new Scale<>(pScale);
		setGradeKey();
	}

	/**
	 * Monad Subtraction: (this + pM) The two monads must be reference matches and use the same ProtoN 
	 * child. The first check involves a reference match which will spot algebra mismatches. 
	 * The next step involves trying addition and possibly catching exceptions that result from Scales 
	 * containing mutable weights.
	 * <br>
	 * @param pM Monad to be added to this one
	 * @return Monad that is the result of the addition operation
	 */
	public Monad add(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Can't add monads when Algebras or Cardinals don't match.");
		pM.bladesNotZeroStream().forEach(blade -> {
			try {										//but their Scales don't realize that and we have
				scales.get(blade).add(pM.scales.get(blade));
			} catch (FieldBinaryException e) {			//to check again because weights are mutable.
				throw new IllegalArgumentException("Can't add when cardinals don't match.");
			}
		});
		setGradeKey();
		return this;
	}

	/**
	 * This method returns the blades the underlying basis as a stream. It is just a shortcut to the basis.
	 * <br><br>
	 * @return Stream of Blades in the underlying Basis that is parallelized.
	 */
	public Stream<Blade> bladeStream() {
		return algebra.getBasis().bladeStream();
	}

	/**
	 * It is often the case that streams of values are needed for math operations and those streams contain
	 * zeroes leading to wasted cycles in addition operations or chances to terminate multiplication operations.
	 * This method streams blades where the associated weight is NOT zero. Its complement streams the other blades.
	 * <br><br>
	 * @param pIn single byte indicating the grade of the blades in the stream
	 * @return Stream of Blades where the associated weight is NOT zero
	 */
	public Stream<Blade> bladeOfGradeStream(byte pIn) {
		return algebra.getBasis().bladeOfGradeStream(pIn);
	}

	/**
	 * The stream returned contains blades that match the grades requested in the mask parameter. Instead of
	 * matching grade bytes, though, a boolean mask is sent that is long enough to cover all possible grades
	 * in the bases. Where the mask array is true, that grade will be selected FOR in the stream. Where the 
	 * mask array is false, that grade will be selected AGAINST (filtered out) in the stream.
	 * <br><br>
	 * @return Stream of Blades from the grades present in the monad.
	 */
	//@Override
	public Stream<Blade> bladeOfGradesStream() {
		return algebra.getBasis().bladeOfGradesStream(getGradeMask(this));
	}

	/**
	 * It is often the case that streams of values are needed for math operations and those streams contain
	 * zeroes leading to wasted cycles in addition operations or chances to terminate multiplication operations.
	 * This method streams blades where the associated weight is NOT zero. Its complement streams the other blades.
	 * <br><br>
	 * @return Stream of Blades where the associated weight is NOT zero
	 */
	public Stream<Blade> bladesNotZeroStream() {
		return getWeights().bladesNotZeroStream();
	}

	/**
	 * It is often the case that streams of values are needed for math operations and those streams contain
	 * zeroes leading to wasted cycles in addition operations or chances to terminate multiplication operations.
	 * This method streams blades where the associated weight IS zero. Its complement streams the other blades.
	 * <br><br>
	 * @return Stream of Blades where the associated weight IS zero
	 */
	public Stream<Blade> bladesZeroStream() {
		return getWeights().bladesZeroStream();
	}

	/**
	 * This method causes all weights of a monad to be conjugated.
	 * <br>
	 * @return Monad after operation.
	 */
	public Monad conjugate() {
		scales.conjugateNumbers();
		return this;
	}

	/**
	 * Overridden Equals method from Object.
	 * This ensures reference equality is the standard. They must literally be the same object to be equal.
	 * @return boolean check for reference equality
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj);
	}

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * <br><br>
	 * In metrics where one or more of the generators squares to zero, this isn't really
	 * a dual operation.
	 * <br><br>
	 * @return Monad after operation.
	 */
	public Monad multiplyByPSLeft() {	
		this.multiplyLeft(GBuilder.pscalarOfMonad(this));
		setGradeKey();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with right side multiplication by pscalar.
	 * <br><br>
	 * In metrics where one or more of the generators squares to zero, this isn't really
	 * a dual operation.
	 * <br><br>
	 * @return Monad after operation.
	 */
	public Monad multiplyByPSRight() {
		this.multiplyRight(GBuilder.pscalarOfMonad(this));
		setGradeKey();
		return this;
	}

	/**
	 * This method returns the Algebra for this Monad.
	 * <br>
	 * @return Algebra
	 */
	public Algebra getAlgebra() {
		return algebra;
	}

	/**
	 * Simple gettor method for the Cardinal associated with this object.
	 * <br><br>
	 * @return Cardinal in use in this.
	 */
	@Override
	public Cardinal getCardinal() {
		return scales.getCardinal();
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * <br>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a ProtoN child AND casting an unchecked type could fail
	 * at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param <T> ProtoN number from CladosF without the interfaces this time.
	 * @return ProtoN[]
	 */
	//public <T extends ProtoN & Field & Normalizable> T[] getCoeff() {
	//	return (T[]) scales.getNumbers();
	//}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * <br>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a ProtoN child AND casting an unchecked type could fail
	 * at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param i   int This points at the coefficient at the equivalent tuple
	 *            location.
	 * @param <T> ProtoN number from CladosF.
	 * @return ProtoN
	 */
	public <T extends ProtoN & Field & Normalizable> T getCoeff(int i) {
		if (getAlgebra().getBasis().validateBladeIndex(i))
			return (T) scales.getNumbers()[i];
		return null;
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the weights making linear combinations 
	 * of basis elements. Use this method to get a weight at a particular blade.
	 * <br><br>
	 * The map internal to Scale can accept any of the CladosF numbers as values, there is a cast to a 'generic' 
	 * type within this method. This would normally cause warnings by the compiler since the generic named in the
	 * internal map IS a ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br><br>
	 * That won't happen here when CladosF builders are used. They can't build anything that is NOT a ProtoN child. 
	 * They can't even build a ProtoN instance directly. Therefore, only children can arrive as the value parameter 
	 * of the 'put' function. Thus, there is no danger of a failed cast operation... until someone creates a 
	 * new ProtoN child class and fails to update all builders.
	 * <br><br>
	 * @param pB	Blade that points at the coefficient in the equivalent tuple location.
	 * @param <T> 	ProtoN number from CladosF.
	 * @return ProtoN child number acting as a weight at the blade.
	 */
	public <T extends ProtoN & Field & Normalizable> T get(Blade pB) {
		if (getAlgebra().getBasis().hasBlade(pB))
			return (T) scales.get(pB);
		return null;
	}

	/**
	 * Return the grade key for the monad
	 * <br>
	 * @return long
	 */
	public long getGradeKey() {
		return gradeKey;
	}

	/**
	 * This answers a question concerning which type of ProtoN children are used. The monad itself
	 * isn't modal, but its weights are so the monad has an implicit dependence.
	 * <br><br>
	 * @return CladosField mode for this monad
	 */
	@Override
	public CladosField getMode() {
		return scales.getMode();
	}

	/**
	 * Simple gettor for name of the monad.
	 * <br>
	 * @return String Contains the name of the Monad.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the map relating basis blades to coefficients.
	 * <br><br>
	 * @return Scale of Blades and ProtoN children. This is the 'coefficients' object.
	 */
	//public Scale<?> getWeights() {
	//	return scales;
	//}

	/**
	 * This method returns the map relating basis blades to coefficients.
	 * <br><br>
	 * Typed accessor for the monad's Scale. This one avoids repeated unchecked casts at call sites.<br>
	 * If one knows/does not know the concrete ProtoN numeric type of the Monad, this method is called like <br>
	 * 1. monad.&lt;ProtoN child type&gt;getWeightsAs()<br>
	 * 2. monad.getWeightsAs()
	 * <br><br>
	 * @param <T> ProtoN child type used by the caller
	 * @return Scale typed to the caller's numeric type (unchecked cast)
	 */
	//@SuppressWarnings("unchecked")
	public <T extends ProtoN & Field & Normalizable> Scale<T> getWeights() {
		return (Scale<T>) scales;
	}

	/**
	 * This method returns the sparse flag of the monad in case someone wants to
	 * know. It is just a gettor method, though.
	 * <br>
	 * @return boolean
	 */
	public boolean getSparseFlag() {
		return sparseFlag;
	}

	/**
	 * This method suppresses grades in the Monad not equal to the integer passed.
	 * <br>
	 * @param pGrade byte integer of the grade TO KEEP.
	 * @return Monad but in practice it will always be a child of MonadAbtract
	 */
	public Monad gradePart(byte pGrade) {
		if (pGrade >= getAlgebra().getGradeCount() | pGrade < 0)
			return this;
		scales.zeroAllButGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * This integer stream is OFTEN used internally in monads for calculations.
	 * Rather than type it out in long form, it is aliases to this method.
	 * <br>
	 * @return Integer stream ranging through all the grades of the algebra
	 */
	public IntStream gradeStream() {
		return IntStream.range(0, getAlgebra().getGradeCount());
	}

	/**
	 * This method suppresses the grade in the Monad equal to the integer passed.
	 * <br>
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
	 * <br>
	 * @return Monad after the main involution is complete.
	 */
	public Monad mainInvolution() {
		scales.conjugateShirokov(1);
		return this;
	}

	/**
	 * This method does a deep check for the equality of two monads. It is not meant
	 * for checking that two monad references actually point to the same object
	 * since that is easily handled with ==. This one checks algebras, cardinals, and 
	 * weihts. Each object owned by a monad has its own specialized isEqual() that is called.
	 * <br>
	 * Note that this could be done by override Object's equals() method. That might
	 * happen in the future, but thought will have to be given to how to override
	 * the hashing method too.
	 * <br>
	 * @param pM Monad
	 * @return boolean
	 */
	public boolean isGEqual(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			return false;
		switch (getMode()) {
			case COMPLEXD -> {return bladeStream().allMatch(blade -> 
							ComplexD.isEqual((ComplexD) scales.get(blade), (ComplexD) pM.scales.get(blade)));}
			case COMPLEXF -> {return bladeStream().allMatch(blade -> 
							ComplexF.isEqual((ComplexF) scales.get(blade), (ComplexF) pM.scales.get(blade)));}
			case REALD -> {return bladeStream().allMatch(blade -> 
							RealD.isEqual((RealD) scales.get(blade), (RealD) pM.scales.get(blade)));}
			case REALF -> {return bladeStream().allMatch(blade -> 
							RealF.isEqual((RealF) scales.get(blade), (RealF) pM.scales.get(blade)));}
			default -> {return false;}
		}
	}

	/**
	 * Return the magnitude of the Monad
	 * <br>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a ProtoN child AND casting an unchecked type could fail
	 * at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param <T> ProtoN number from CladosF with the Field interface this
	 *            time.
	 * @return ProtoN but in practice it is always a child of ProtoN
	 */
	public <T extends ProtoN & Field & Normalizable> T magnitude() {
		return (T) scales.modulusSum();
	}

	/**
	 * Monad antisymmetric multiplication: (pM this - this pM) 
	 * This operation is allowed when the two monads use the same field and satisfy the Reference Matching test.
	 * <br><br>
	 * @param pM Monad brought in to the commutator product
	 * @return Monad (this one) returned after the operation
	 */
	public Monad commutator(Monad pM) {			
		if (!isReferenceMatch(this, pM))		throw new IllegalArgumentException("Commutator fails reference match.");
		Monad rightSide = (GBuilder.copyOfMonad(this)).multiplyRight(pM);
		(this.multiplyLeft(pM)).subtract(rightSide);
		setGradeKey();
		return this;
	}

	/**
	 * Monad leftside multiplication: (pM this) This operation is allowed when the two monads use the same field and satisfy the Reference Match test.
	 * <br>
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to  find the non-zero grades. For example: gradeKey=101 means the monad 
	 * is a sum  of bivector and scalar because 10^2+10^0 = 101.
	 * <br>
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all blades less optimal. Instead, we parse gradeKey and loop through the 
	 * blades for grades that could be non-ZERO.
	 * <br>
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd behavior if one sends in a complex numbers expecting against real 
	 * numbers. What IS checked is the cardinal and that likely traps most errors that can be made. It's not perfect, though. If someone intentionally 
	 * builds different number types using the same cardinal, they will get around the detection in
	 * place here.
	 * <br>
	 * What will happen in that case? The inbound numbers will be multiplied against coefficients as THEY understand multiplication. The inbound numbers 
	 * get cast to the other, so imaginary components won't get used in real number multiplication.
	 * <br>
	 * @param pM  Monad
	 * @param <T> ProtoN number from CladosF with all interfaces this time.
	 * @return Monad
	 */
	public <T extends ProtoN & Field & Normalizable> Monad multiplyLeft(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))		throw new IllegalArgumentException("Left multiply fails reference match.");
		GProduct GP = getAlgebra().getGP();
		Scale<T> newScales = new Scale<T>(getMode(), getAlgebra().getBasis(), scales.getCardinal()).zeroAll();
		if (sparseFlag) {										//If grade coverage is sparse, multiply blades BY grades present
			bladeOfGradesStream().forEach(blade0 -> { 						//blade0's are in a single grade of THIS monad
				pM.getWeights().bladesNotZeroStream().forEach(blade2 -> { 												//blade2's are in ANY grade of the OTHER monad
					Blade bMult = GP.getResult(blade2, blade0);															//the two blades determine the result blade
					try {
						switch (getMode()) {					//get the number at the result blade and +/- it with the product of numbers at the two blades.
							case COMPLEXD -> newScales.get(bMult).add(ComplexD	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case COMPLEXF -> newScales.get(bMult).add(ComplexF	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case REALD -> 	 newScales.get(bMult).add(RealD		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case REALF -> 	 newScales.get(bMult).add(RealF		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
						}
					} catch (FieldBinaryException e) {			//Number reference match failures for multiply and add are caught here.
						throw new IllegalArgumentException("Left multiply fails ProtoN reference match.");
					}
				});
			});
		} else {												//If grade coverage is NOT sparse, multiply blades in order if non-zero. No gradeKey trickery.
			getWeights().bladesNotZeroStream().forEach(blade0 -> {  													//blade0's are in ANY grade of THIS monad
				pM.getWeights().bladesNotZeroStream().forEach(blade2 -> { 												//blade2's are in ANY grade of the OTHER monad
					Blade bMult = GP.getResult(blade2, blade0);															//the two blades determine the result blade
					try {
						switch (getMode()) {					//get the number at the result blade and +/- it with the product of numbers at the two blades.
							case COMPLEXD -> newScales.get(bMult).add(ComplexD	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case COMPLEXF -> newScales.get(bMult).add(ComplexF	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case REALD -> 	 newScales.get(bMult).add(RealD		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
							case REALF -> 	 newScales.get(bMult).add(RealF		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade2, blade0)));	//here is the +/- decision
						}
					} catch (FieldBinaryException e) {			//Number reference match failures for multiply and add are caught here.
						throw new IllegalArgumentException("Left multiply fails ProtoN reference match.");
					}
				});
			});
		}
		scales = newScales;										//newScales has the correct map between blades and numbers to be the multiplication result
		setGradeKey();											//Let the monad sift through its numbers to reset the keys.
		return this;
	}

	/**
	 * Monad rightside multiplication: (this pM) This operation is allowed when the two monads use the same field and satisfy the Reference Match test.
	 * <br><br>
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to find the non-zero grades. For example: gradeKey=101 means the monad 
	 * is a sum of bivector and scalar because 10^2+10^0 = 101.
	 * <br><br>
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all blades less optimal. Instead, we parse gradeKey and loop through the 
	 * blades for grades that could be non-ZERO.
	 * <br><br>
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd behavior if one sends in a complex numbers expecting against 
	 * real numbers. What IS checked is the cardinal and that likely traps most errors that can be made. It's not perfect, though. If someone 
	 * intentionally builds different number types using the same cardinal, they will get around the detection in place here.
	 * <br><br>
	 * What will happen in that case? The inbound numbers will be multiplied against coefficients as THEY understand multiplication. 
	 * The inbound numbers gets cast to the other, so imaginary components won't get used in real number multiplication.
	 * <br><br>
	 * @param pM  Monad to be right multiplied with this one
	 * @param <T> ProtoN number from CladosF with all interfaces this time.
	 * @return Monad result after this monad is multiplied by the other.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad multiplyRight(Monad pM) {
		if (!isReferenceMatch(this, pM)) 			throw new IllegalArgumentException("Right multiply fails reference match.");
		GProduct GP = getAlgebra().getGP();
		Scale<T> newScales = new Scale<T>(getMode(), getAlgebra().getBasis(), scales.getCardinal()).zeroAll();
		if (sparseFlag) {										//If grade coverage is sparse, multiply blades BY grades present
			bladeOfGradesStream().forEach(blade0 -> { 							//blade0's are in a single grade of THIS monad
				pM.getWeights().bladesNotZeroStream().forEach(blade2 -> {  												//blade2's are in ANY grade of the OTHER monad
					Blade bMult = GP.getResult(blade0, blade2);	// NOTE the reversal from left multiplication			//the two blades determine the result blade
					try {
						switch (getMode()) {					//get the number at the result blade and +/- it with the product of numbers at the two blades.
							case COMPLEXD -> newScales.get(bMult).add(ComplexD	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case COMPLEXF -> newScales.get(bMult).add(ComplexF	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case REALD -> 	 newScales.get(bMult).add(RealD		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case REALF ->	 newScales.get(bMult).add(RealF		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
						}
					} catch (FieldBinaryException e) {			//Number reference match failures for multiply and add are caught here.
						throw new IllegalArgumentException("Right multiply fails ProtoN reference match.");
					}
				});
			});
		} else {												//If grade coverage is NOT sparse, multiply blades in order if non-zero. No gradeKey trickery.
			getWeights().bladesNotZeroStream().forEach(blade0 -> {  													//blade0's are in ANY grade of THIS monad
				pM.getWeights().bladesNotZeroStream().forEach(blade2 -> {  												//blade2's are in ANY grade of the OTHER monad
					Blade bMult = GP.getResult(blade0, blade2);	// NOTE the reversal from left multiplication			//the two blades determine the result blade
					try {
						switch (getMode()) {					//get the number at the result blade and +/- it with the product of numbers at the two blades.
							case COMPLEXD -> newScales.get(bMult).add(ComplexD	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case COMPLEXF -> newScales.get(bMult).add(ComplexF	.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case REALD -> 	 newScales.get(bMult).add(RealD		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
							case REALF -> 	 newScales.get(bMult).add(RealF		.multiply(get(blade0), pM.get(blade2))
																				.scale(GP.getSign(blade0, blade2)));	//here is the +/- decision
						}
					} catch (FieldBinaryException e) {			//Number reference match failures for multiply and add are caught here.
						throw new IllegalArgumentException("Right multiply fails ProtoN reference match.");
					}
				});
			});
		}
		scales = newScales;										//newScales has the correct map between blades and numbers to be the multiplication result
		setGradeKey();											//Let the monad sift through its numbers to reset the keys.
		return this;
	}

	/**
	 * Monad symmetric multiplication: (pM this + this pM) 
	 * This operation is allowed when the two monads use the same field and satisfy the Reference Matching test.
	 * <br>
	 * @param pM Monad brought in to the anticommutator product
	 * @return Monad (this one) returned after the operation
	 */
	public Monad anticommutator(Monad pM) {
		if (!isReferenceMatch(this, pM))		throw new IllegalArgumentException("Anticommutator fails reference match.");
		Monad rightSide = (GBuilder.copyOfMonad(this)).multiplyRight(pM);
		(this.multiplyLeft(pM)).add(rightSide);
		setGradeKey();
		return this;
	}

	/**
	 * Normalize the monad using the definition that is being called a spinor norm.
	 * <br><br>
	 * @return Monad this after the operation is complete
	 * @throws FieldException 	This exception is thrown when normalizing a zero-sized or field-conflicted monad. 
	 * 							The object throwing it is one of the ProtoN children in Scale
	 */
	public Monad normalize() throws FieldException {
		Monad tRev = (GBuilder.copyOfMonad(this)).reverse().conjugate();	//This is GP reversal and complex conjugation.								
		(tRev.multiplyRight(this)).gradePart((byte) 0); 					//The scalar part will be real.

		switch (getMode()) {
			case COMPLEXD -> {
				ComplexD tMagCD = (tRev.<ComplexD>getWeights().getScalar().invert()); //img part == 0
				tMagCD.setReal(Math.sqrt(Math.abs(tMagCD.getReal())));
				this.scale(tMagCD);
			}	
			case COMPLEXF -> {
				ComplexF tMagCF = (tRev.<ComplexF>getWeights().getScalar().invert()); //img part == 0
				tMagCF.setReal((float) Math.sqrt(Math.abs(tMagCF.getReal())));
				this.scale(tMagCF);	
			}
			case REALD -> {
				RealD tMagRD = (tRev.<RealD>getWeights().getScalar().invert());
				tMagRD.setReal(Math.sqrt(Math.abs(tMagRD.getReal())));
				this.scale(tMagRD);		
			}
			case REALF -> {
				RealF tMagRF = (tRev.<RealF>getWeights().getScalar().invert());
				tMagRF.setReal((float) Math.sqrt(Math.abs(tMagRF.getReal())));
				this.scale(tMagRF);
			}
			default -> {}
		}
		setGradeKey();
		return this;
	}

	/**
	 * Normalize the monad as if all its basis blades were 'vectors' in the 
	 * 2^n-dimensional vector space we can form using the basis from the 
	 * n-dimensional algebra.
	 * <br>
	 * @return Monad after normalization effort is attempted.
	 * @throws FieldException This exception is thrown when normalizing a zero-sized
	 *                        or field-conflicted monad. The object throwing it
	 * 						  is the Scale on behalf of one of its entries.
	 */
	public Monad normalizeOnVS() throws FieldException {
		scales.normalize();
		return this;
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of all
	 * the permutation, so the easiest thing to do is to change the coefficients
	 * instead.
	 * <br>
	 * @return Monad returns itself when done to support streaming operations.
	 */
	public Monad reverse() {
		scales.conjugateShirokov(2);
		return this;
	}

	/**
	 * Monad Scaling: (this * CladosF number) 
	 * The monad's weight are scaled by the unitized number.
	 * <br><br>
	 * NOTE that the mode of the inbound scaling number is NOT checked. That can lead to odd behavior if 
	 * one sends in a complex number expecting to scale a real number. What IS checked is the cardinal 
	 * and that likely traps most errors that can be made. It's not perfect, though. If someone intentionally
	 * builds different number types using the same cardinal, they will get around the detection in place 
	 * here. What will happen in that case? The inbound number will be multiplied against coefficients as 
	 * THEY understand multiplication. The inbound number gets cast to the other, so imaginary components 
	 * won't get used in real number multiplication.
	 * <br><br>
	 * @param pScale ProtoN to use for scaling the monad
	 * @param <T>    ProtoN number from CladosF with the Field interface.
	 * @return Monad after the scaling is complete.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad scale(T pScale) {
		scales.scale(pScale);
		setGradeKey();
		return this;
	}

	/**
	 * Monad Scaling: (this * Number)
	 * The monad's weights are scaled by the non-unitized number.
	 * <br><br>
	 * NOTE there is no unit protection needed here.<br>
	 * ALSO there is a risk of precision errors creeping in here since the
	 * monad's mode might be single precision while the incoming parameter
	 * might be double precision. No check is made or enforced as the typical
	 * use for this method should involve integer inputs and their inverses.
	 * <br><br>
	 * @param pN 	Number (a Java superclass) to use for scaling the monad
	 * @return Monad after the scaling is complete.
	 */
	protected Monad scale(Number pN) {
		scales.weightsStream().forEach(x -> x.scale(pN));
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
	 * <br>
	 * @param <T>  ProtoN number from CladosF with all interfaces this time.
	 * @param ppC ProtoN child array for weights
	 * @return Monad after setting the coefficients to the offered array.
	 * @throws CladosException gets thrown when the coefficient array is not suitable.
	 */
	public <T extends ProtoN & Field & Normalizable> Monad setCoeff(T[] ppC) throws CladosException {
		if (ppC.length != getAlgebra().getBladeCount() | ppC.length == 0)
			throw new CladosMonadException(this, "Coefficient array passed for coefficient copy is wrong length");

		if 	(
			(ppC[0] instanceof RealF) & (getMode() == REALF)
		| 	(ppC[0] instanceof RealD) & (getMode() == REALD)
		| 	(ppC[0] instanceof ComplexF) & (getMode() == COMPLEXF)
		| 	(ppC[0] instanceof ComplexD) & (getMode() == COMPLEXD)
			)
		{
			scales.setNumbers(FListBuilder.copyOf(getMode(), ppC));
			setGradeKey();
		} 
		else 
			throw new CladosMonadException(this, "Coefficient array passed for coefficient copy is different mode.");

		return this;
	}

	/**
	 * Set the grade key for the monad. Never accept an externally provided key. Always recalculate it 
	 * after any of the unary or binary operations.
	 * <br><br>
	 * While we are here, we ALSO set the sparseFlag. The nonZero coeff detection loop that fills gradeKey 
	 * is a grade detector, so if foundGrade is less than or equal to half gradeCount, sparseFlag is set 
	 * to true and false otherwise.
	 * <br><br>
	 * Use this IF you set one of the weights manually by reaching into the scales.
	 * <br><br>
	 * @return Monad this one after the grade key is set.
	 */
	public Monad setGradeKey() {
		foundGrades = 0;
		gradeKey = 0;

		gradeStream().forEach(grade -> {
			if (bladeOfGradeStream((byte) grade).anyMatch(blade -> getWeights().isNotZeroAt(blade))){
				foundGrades++;
				gradeKey += (long) Math.pow(10, grade);
			}
		});

		if (gradeKey == 0) {	//Special case for scalars. If no grades detected, scalar it must be.
			foundGrades++;
			gradeKey++;
		}
		sparseFlag = (foundGrades <= getAlgebra().getGradeCount() / 2) ? true : false;
		return this;
	}

	/**
	 * Simple setter of the name of the monad.
	 * <br>
	 * @param pName String name of the monad to set
	 * @return Monad after setting the name.
	 */
	public Monad setName(String pName) {
		name = pName;
		return this;
	}

	/**
	 * Reset the weights for this Monad. Use of this method is not encouraged, 
	 * but there are reasonable use cases. Ideally one uses the Monad's own operation 
	 * methods to alter weights, but that applies mostly to physical models. In cases
	 * where a user directly manipulates weights, this method and the one for direct
	 * handling of weights is more suitable.
	 * <br>
	 * This method fails with an exception if the Scale object references a different
	 * basis than the one in the Algebra. No basis change is tolerated because the 
	 * scales relate to a basis which only makes sense with respect to an algebra.
	 * Future version will relax this requirement by tolerating Scales referencing a 
	 * Frame instead of requiring a connection to the canonical basis. 
	 * <br>
	 * Using this set method encourages developers to reuse old objects. While this
	 * is useful for avoiding object construction overhead, it is dangerous in that
	 * old references might linger enabling unexpected opportunities to edit weights.
	 * Caution is advised when this method is used while frequent reuse occurs.
	 * <br>
	 * @param pScale The Scale to change to... constructed on the same Basis as the current Scale
	 * @return Monad after setting the coefficients to the offered array.
	 * @throws CladosMonadException This exception is thrown when the scale offered
	 *                              doesn't share exactly the same Basis as the one it replaces.
	 */
	public Monad setScale(Scale<?> pScale) throws CladosMonadException {
		if (pScale.getBasis() != scales.getBasis())
			throw new CladosMonadException(this, "Coefficient array offered uses a different basis ");
		
		scales = pScale;
		setGradeKey();

		return this;
	}

	/**
	 * Return the magnitude squared of the Monad
	 * <br>
	 * Since the map internal to Scale can accept any of the CladosF numbers as
	 * values, there is a cast to a 'generic' type within this method. This would
	 * normally cause warnings by the compiler since the generic named in the
	 * internal map IS a ProtoN child AND casting an unchecked type could fail
	 * at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param <T> ProtoN number from CladosF without the interfaces this time.
	 * @return ProtoN but in practice it is always a child of ProtoN
	 */
	public <T extends ProtoN & Field & Normalizable> T sqMagnitude() {
		return (T) scales.modulusSQSum();
	}

	/**
	 * Monad Subtraction: (this - pM) The two monads must be reference matches and use the same ProtoN 
	 * child. The first check involves a reference match which will spot algebra mismatches. 
	 * The next step involves trying subtraction and possibly catching exceptions that result from Scales 
	 * containing mutable weights.
	 * <br>
	 * @param pM Monad to be subtracted from this one
	 * @return Monad returned that has the passed monad subtracted from it.
	 */
	public Monad subtract(Monad pM) {
		if (!Monad.isReferenceMatch(this, pM))
			throw new IllegalArgumentException("Can't subtract monads without a reference match.");
		pM.bladesNotZeroStream().forEach(blade -> {		//Monads are reference matches now
			try {										//but their Scales don't realize that and we have
				scales.get(blade).subtract(pM.scales.get(blade));
			} catch (FieldBinaryException e) {			//to check again because weights are mutable.
				throw new IllegalArgumentException("Can't subtract when cardinals don't match.");
			}
		});
		setGradeKey();
		return this;
	}

	/**
	 * Simple setter method of the algebra for this monad.
	 * <br>
	 * It is NOT advisable to re-set algebras lightly. Avoid nulling them out.
	 * They carry the meaning of 'directions' in the underlying basis.
	 * <br>
	 * @param pA Algebra to set
	 * @return Monad after setting the algebra.
	 */
	protected Monad setAlgebra(Algebra pA) {
		algebra = pA;
		return this;
	}
}