/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.MonadAbstract<br>
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
 * ---org.interworldtransport.cladosG.MonadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosFListBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.CladosMonadBinaryException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;

/**
 * Many math objects within the cladosG package have a number of attributes in
 * common. They are named objects from named algebras and with named feet. The
 * abstracted monad covers those common elements and methods shared by objects
 * in only one algebra.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class MonadAbstract {
	/**
	 * Return a boolean if the grade being checked is non-zero in the Monad.
	 * 
	 * @param pM     MonadAbstract
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean hasGrade(MonadAbstract pM, int pGrade) {
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
	 * 
	 * @param pM     MonadAbstract
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean isGrade(MonadAbstract pM, int pGrade) {
		if (Math.pow(10, pGrade) == pM.getGradeKey())
			return true;

		return false;
	}

	/**
	 * Return true if more the monad is a ZERO scalar.
	 * 
	 * @param pM MonadComplexD This is the monad to be tested.
	 * 
	 * @return boolean
	 */
	public static boolean isGZero(MonadAbstract pM) {
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			return (pM.getGradeKey() == 1 & ComplexD.isZero((ComplexD) pM.getCoeff(0)));
		}
		case COMPLEXF -> {
			return (pM.getGradeKey() == 1 & ComplexF.isZero((ComplexF) pM.getCoeff(0)));
		}
		case REALD -> {
			return (pM.getGradeKey() == 1 & RealD.isZero((RealD) pM.getCoeff(0)));
		}
		case REALF -> {
			return (pM.getGradeKey() == 1 & RealF.isZero((RealF) pM.getCoeff(0)));
		}
		default -> {
			return false;
		}
		}
	}

	/**
	 * Return true if the Monad an idempotent
	 * 
	 * @return boolean
	 * @param pM MonadAbstract
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldBinaryException This exception is thrown when the method can't
	 *                              multiply two fields used by the monad to be
	 *                              checked.
	 */
	public static boolean isIdempotent(MonadAbstract pM) throws FieldBinaryException, CladosMonadException {
		if (isGZero(pM))
			return true;
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			return CladosGMonad.COMPLEXD.copyOf(pM).multiplyLeft(pM).isGEqual(pM);
		}
		case COMPLEXF -> {
			return CladosGMonad.COMPLEXF.copyOf(pM).multiplyLeft(pM).isGEqual(pM);
		}
		case REALD -> {
			return CladosGMonad.REALD.copyOf(pM).multiplyLeft(pM).isGEqual(pM);
		}
		case REALF -> {
			return CladosGMonad.REALF.copyOf(pM).multiplyLeft(pM).isGEqual(pM);
		}
		default -> {
			return false;
		}

		}
	}
	
	/**
	 * Return true if the Monad is a multiple of an idempotent
	 * 
	 * The strategy for this method is as follows.
	 * 
	 * 1. If the monad is an actual idempotent, return true. This is trivial case.
	 * 2. If not, find first non-zero coefficient of the square of the monad and...
	 * a) re-scale monad by inverse of that coefficient. Use copy to avoid change.
	 * b) test the re-scaled monad to see if it is idenpotent. If so, return true.
	 * 3. Return false.
	 * 
	 * @return boolean
	 * @param pM MonadAbstract
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldException       This exception is thrown when the method can't
	 *                              copy the field used by the monad to be checked.
	 */
	public static boolean isScaledIdempotent(MonadAbstract pM) throws CladosMonadException, FieldException {
		if (isIdempotent(pM))
			return true;
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			MonadAbstract check1 = CladosGMonad.COMPLEXD.copyOf(pM);
			check1.multiplyLeft(pM); // We now have check1 = pM ^ 2
			if (MonadAbstract.isGZero(check1)) return false; // pM is nilpotent at power=2
			ComplexD fstnzeroC = (ComplexD) CladosFBuilder.COMPLEXD.copyOf(pM.getCoeff(0)); // Grab copy of Scalar part
			int k = 1;// skipping over check1.SP() != 0
			while (ComplexD.isZero(fstnzeroC) & k <= pM.getAlgebra().getBladeCount() - 1) {
				if (!ComplexD.isZero((ComplexD) pM.getCoeff(k))) {
					fstnzeroC = ComplexD.copyOf((ComplexD) pM.getCoeff(k)); // Grab a copy of it instead
					break; // Good enough. Move on.
				}
				k++; // If next coeff is zero, look at next next
			}
			check1 = CladosGMonad.COMPLEXD.copyOf(pM).scale(fstnzeroC.invert()); // No risk of inverting a zero.
			return isIdempotent(check1);
		}
		case COMPLEXF -> {
			MonadAbstract check1 = CladosGMonad.COMPLEXF.copyOf(pM);
			check1.multiplyLeft(pM); // We now have check1 = pM ^ 2
			if (MonadAbstract.isGZero(check1)) return false; // pM is nilpotent at power=2
			ComplexF fstnzeroC = (ComplexF) CladosFBuilder.COMPLEXF.copyOf(pM.getCoeff(0)); // Grab copy of Scalar part
			int k = 1;// skipping over check1.SP() != 0
			while (ComplexF.isZero(fstnzeroC) & k <= pM.getAlgebra().getBladeCount() - 1) {
				if (!ComplexF.isZero((ComplexF) pM.getCoeff(k))) {
					fstnzeroC = ComplexF.copyOf((ComplexF) pM.getCoeff(k)); // Grab a copy of it instead
					break; // Good enough. Move on.
				}
				k++; // If next coeff is zero, look at next next
			}
			check1 = CladosGMonad.COMPLEXF.copyOf(pM).scale(fstnzeroC.invert()); // No risk of inverting a zero.
			return isIdempotent(check1);
		}
		case REALD -> {
			MonadAbstract check1 = CladosGMonad.REALD.copyOf(pM);
			check1.multiplyLeft(pM); // We now have check1 = pM ^ 2
			if (MonadAbstract.isGZero(check1)) return false; // pM is nilpotent at power=2
			RealD fstnzeroC = (RealD) CladosFBuilder.REALD.copyOf(pM.getCoeff(0)); // Grab copy of Scalar part
			int k = 1;// skipping over check1.SP() != 0
			while (RealD.isZero(fstnzeroC) & k <= pM.getAlgebra().getBladeCount() - 1) {
				if (!RealD.isZero((RealD) pM.getCoeff(k))) {
					fstnzeroC = RealD.copyOf((RealD) pM.getCoeff(k)); // Grab a copy of it instead
					break; // Good enough. Move on.
				}
				k++; // If next coeff is zero, look at next next
			}
			check1 = CladosGMonad.REALD.copyOf(pM).scale(fstnzeroC.invert()); // No risk of inverting a zero.
			return isIdempotent(check1);
		}
		case REALF -> {
			MonadAbstract check1 = CladosGMonad.REALF.copyOf(pM);
			check1.multiplyLeft(pM); // We now have check1 = pM ^ 2
			if (MonadAbstract.isGZero(check1)) return false; // pM is nilpotent at power=2
			RealF fstnzeroC = (RealF) CladosFBuilder.REALF.copyOf(pM.getCoeff(0)); // Grab copy of Scalar part
			int k = 1;// skipping over check1.SP() != 0
			while (RealF.isZero(fstnzeroC) & k <= pM.getAlgebra().getBladeCount() - 1) {
				if (!RealF.isZero((RealF) pM.getCoeff(k))) {
					fstnzeroC = RealF.copyOf((RealF) pM.getCoeff(k)); // Grab a copy of it instead
					break; // Good enough. Move on.
				}
				k++; // If next coeff is zero, look at next next
			}
			check1 = CladosGMonad.REALF.copyOf(pM).scale(fstnzeroC.invert()); // No risk of inverting a zero.
			return isIdempotent(check1);
		}
		default -> {
			return false;
		}
		}
		
		
	}
	
	/**
	 * Return true if the Monad is nilpotent at a particular integer power.
	 * 
	 * @return boolean
	 * @param pM     MonadAbstract The monad to be tested
	 * @param pPower int The integer power to test
	 * @throws CladosMonadException This exception is thrown when the method can't
	 *                              create a copy of the monad to be checked.
	 * @throws FieldBinaryException This exception is thrown when the method can't
	 *                              multiply two fields used by the monad to be
	 *                              checked.
	 */
	public static boolean isNilpotent(MonadAbstract pM, int pPower) throws FieldBinaryException, CladosMonadException {
		if (isGZero(pM))
			return true;
		
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			MonadAbstract check1 = CladosGMonad.COMPLEXD.copyOf(pM);
			while (pPower > 1) {
				check1.multiplyLeft(pM);
				if (isGZero(check1))
					return true;
				pPower--;
			}
		}
		case COMPLEXF -> {
			MonadAbstract check1 = CladosGMonad.COMPLEXF.copyOf(pM);
			while (pPower > 1) {
				check1.multiplyLeft(pM);
				if (isGZero(check1))
					return true;
				pPower--;
			}
		}
		case REALD -> {
			MonadAbstract check1 = CladosGMonad.REALD.copyOf(pM);
			while (pPower > 1) {
				check1.multiplyLeft(pM);
				if (isGZero(check1))
					return true;
				pPower--;
			}
		}
		case REALF -> {
			MonadAbstract check1 = CladosGMonad.REALF.copyOf(pM);
			while (pPower > 1) {
				check1.multiplyLeft(pM);
				if (isGZero(check1))
					return true;
				pPower--;
			}
		}
		}
		return false;
	}
	

	/**
	 * Return true if more than one blade is present in the Monad. This method makes
	 * use of the grade key which is a sum of powers of 10, thus the base-10
	 * logarithm will be an integer for pure grade monads and a non-integer for
	 * multigrade monads.
	 * 
	 * @param pM This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isMultiGrade(MonadAbstract pM) {
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
	 * 
	 * @param pM MonadAbstract
	 * @param pN MonadAbstract
	 * @return boolean
	 */
	public static boolean isReferenceMatch(MonadAbstract pM, MonadAbstract pN) {
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
	 * 
	 * @param pM This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isUniGrade(MonadAbstract pM) {
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
	 * 
	 * @param pM     MonadRealF This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * 
	 * @return String
	 */
	public static String toXMLFullString(MonadAbstract pM, String indent) {
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
	 * Display XML string that represents the Monad
	 * 
	 * @param pM     MonadAbstract This is the monad to be converted to XML.
	 * @param indent String of tab characters to assign with human readability
	 * 
	 * @return String
	 */
	public static String toXMLString(MonadAbstract pM, String indent) {
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

	protected byte foundGrades;

	/**
	 * This String is the name of the Reference Frame of the Monad
	 */
	protected String frameName;

	/**
	 * This long holds a key that shows which grades are present in the monad. The
	 * key is a sum over powers of 10 with the grade as the exponent.
	 */
	protected long gradeKey;
	/**
	 * This is just a flag specifying the field type one should expect for
	 * coefficients of the monad.
	 */
	protected CladosField mode;
	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String name;

	/**
	 * This is the new coefficient 'array'. It's size should always match
	 * bladeCount. It is keyed to the blades in a monad's basis. It is fundamentally
	 * an IdentityHashMap with some frosting.
	 */
	protected Scale<? extends DivField> scales;

	/**
	 * This boolean is a flag used internally by multiplication methods to make
	 * those methods a little more efficient. A sparse monad has mostly zero
	 * coefficients and is directed to multiply grade by grade instead of using the
	 * generic algorithm.
	 */
	protected boolean sparseFlag = true;
	
	/**
	 * Monad Addition: (this + pM) This operation is allowed when the two monads use
	 * the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM MonadAbstract
	 * @throws CladosMonadBinaryException This exception is thrown when the monads
	 *                                    fail a reference match.
	 * @return MonadAbstract
	 */
	public MonadAbstract add(MonadAbstract pM) throws CladosMonadBinaryException {
		if (!MonadAbstract.isReferenceMatch(this, pM))
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
	 * This integer stream is OFTEN used internally in monads for calculations.
	 * Rather than type it out in long form, it is aliases to this method.
	 * 
	 * NOTE that it is not forced to be parallel() here. Whether that makes sense is
	 * decided by the method using it.
	 * 
	 * @return Integer stream ranging through all the blades of the algebra
	 */
	public IntStream bladeIntStream() {
		return IntStream.range(0, getAlgebra().getBladeCount());
	}

	/**
	 * This method returns the actual blades the underlying basis as a stream.
	 * 
	 * @return Stream of Blades in the underlying CanonicalBasis
	 */
	public Stream<Blade> bladeStream() {
		return algebra.getGBasis().bladeStream();
	}

	/**
	 * This method causes all coefficients of a monad to be conjugated.
	 * 
	 * @return Monad after operation.
	 */
	public MonadAbstract conjugate() {
		scales.conjugate();
		return this;
	}

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * 
	 * @return Monad after operation.
	 */
	public MonadAbstract dualLeft() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		int row = tBasis.getBladeCount() - 1; // row points at the PScalar blade
		
		switch (scales.getMode()) {
		case COMPLEXD -> {
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
		}
		case COMPLEXF -> {
			Scale<ComplexF> newScales = new Scale<ComplexF>(CladosField.COMPLEXF, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !ComplexF.isZero((ComplexF) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
						try {
							ComplexF tAgg = ((ComplexF) CladosFBuilder.COMPLEXF.copyOf((ComplexF) e.getValue()))
									.scale(Float.valueOf(tProd.getSign(row, col)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Left dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		case REALD -> {
			Scale<RealD> newScales = new Scale<RealD>(CladosField.REALD, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !RealD.isZero((RealD) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
						try {
							RealD tAgg = ((RealD) CladosFBuilder.REALD.copyOf((RealD) e.getValue()))
									.scale(Double.valueOf(tProd.getSign(row, col)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Left dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		case REALF -> {
			Scale<RealF> newScales = new Scale<RealF>(CladosField.REALF, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !RealF.isZero((RealF) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
						try {
							RealF tAgg = ((RealF) CladosFBuilder.REALF.copyOf((RealF) e.getValue()))
									.scale(Float.valueOf(tProd.getSign(row, col)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Left dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		default -> {
			// Do NOTHING
			return this;
		}
		}
		setGradeKey();
		return this;
	}
	
	

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * 
	 * @return Monad after operation.
	 */
	public MonadAbstract dualRight() {
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		int row = tBasis.getBladeCount() - 1; // row points at the PScalar blade
		
		switch (scales.getMode()) {
		case COMPLEXD -> {
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
		}
		case COMPLEXF -> {
			Scale<ComplexF> newScales = new Scale<ComplexF>(CladosField.COMPLEXF, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !ComplexF.isZero((ComplexF) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
						try {
							ComplexF tAgg = ((ComplexF) CladosFBuilder.COMPLEXF.copyOf((ComplexF) e.getValue()))
									.scale(Float.valueOf(tProd.getSign(col, row)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Right dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		case REALD -> {
			Scale<RealD> newScales = new Scale<RealD>(CladosField.REALD, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !RealD.isZero((RealD) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
						try {
							RealD tAgg = ((RealD) CladosFBuilder.REALD.copyOf((RealD) e.getValue()))
									.scale(Double.valueOf(tProd.getSign(col, row)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Right dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		case REALF -> {
			Scale<RealF> newScales = new Scale<RealF>(CladosField.REALF, tBasis, scales.getCardinal()).zeroAll();
			getScales().getMap().entrySet().stream().filter(e -> !RealF.isZero((RealF) e.getValue())).parallel()
					.forEach(e -> {
						int col = tBasis.find(e.getKey()) - 1;
						Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
						try {
							RealF tAgg = ((RealF) CladosFBuilder.REALF.copyOf((RealF) e.getValue()))
									.scale(Float.valueOf(tProd.getSign(col, row)))
									.add(newScales.get(bMult));
							newScales.put(bMult, tAgg);
						} catch (FieldBinaryException e1) {
							throw new IllegalArgumentException("Right dual fails DivField reference match.");
						}
					});
			scales = newScales;
		}
		default -> {
			// Do NOTHING
			return this;
		}
		}
		setGradeKey();
		return this;
	}

	/**
	 * This method returns the Algebra for this Monad.
	 * 
	 * @return Algebra
	 */
	public Algebra getAlgebra() {
		return algebra;
	}

	/**
	 * Return the field Coefficients for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @return DivField[]
	 */
	public DivField[] getCoeff() {
		switch (scales.getMode()) {
		case COMPLEXD -> {
			return (ComplexD[]) scales.getCoefficients();
		}
		case COMPLEXF -> {
			return (ComplexF[]) scales.getCoefficients();
		}
		case REALD -> {
			return (RealD[]) scales.getCoefficients();
		}
		case REALF -> {
			return (RealF[]) scales.getCoefficients();
		}
		default -> {
			return null;
		}

		}
	}

	/**
	 * Return a field Coefficient for this Monad. These coefficients are the
	 * multipliers making linear combinations of the basis elements.
	 * 
	 * @param i int This points at the coefficient at the equivalent tuple location.
	 * 
	 * @return DivField
	 */
	public DivField getCoeff(int i) {
		switch (scales.getMode()) {
		case COMPLEXD -> {
			if (i >= 0 & i < getAlgebra().getBladeCount())
				return (ComplexD) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		}
		case COMPLEXF -> {
			if (i >= 0 & i < getAlgebra().getBladeCount())
				return (ComplexF) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		}
		case REALD -> {
			if (i >= 0 & i < getAlgebra().getBladeCount())
				return (RealD) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		}
		case REALF -> {
			if (i >= 0 & i < getAlgebra().getBladeCount())
				return (RealF) scales.get(getAlgebra().getGBasis().getSingleBlade(i));
		}
		default -> {
			return null;
		}
		}
		return null;
	}

	/**
	 * Return the name of the Reference Frame for this Monad
	 * 
	 * @return String
	 */
	public String getFrameName() {
		return frameName;
	}

	/**
	 * Return the grade key for the monad
	 * 
	 * @return long
	 */
	public long getGradeKey() {
		return gradeKey;
	}

	/**
	 * 
	 * @return String Contains the name of the Monad.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the map relating basis blades to coefficients in the
	 * monad.
	 * 
	 * @return Scale of Blades and DivField children. This is the 'coefficients'
	 *         object.
	 */
	public Scale<? extends DivField> getScales() {
		return scales;
	}

	/**
	 * This method returns the sparse flag of the monad in case someone wants to
	 * know. It is just a gettor method, though.
	 * 
	 * @return boolean
	 */
	public boolean getSparseFlag() {
		return sparseFlag;
	}

	/**
	 * This method suppresses grades in the Monad not equal to the integer passed.
	 * 
	 * @param pGrade byte integer of the grade TO KEEP.
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public MonadAbstract gradePart(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAllButGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * This IntStream ranges across an integer index of blades of the same grade.
	 * Which grade is decided elsewhere and then the gradeRange result is passed to
	 * this method to get the integer stream.
	 * 
	 * @param pIn A two-cell array of integers that represent a span of blades of
	 *            the same grade.
	 * @return InStream of blade index integers covering a particular grade.
	 */
	public IntStream gradeSpanStream(int[] pIn) {
		if (pIn.length == 2)
			return IntStream.rangeClosed(pIn[0], pIn[1]);
		return IntStream.empty();
	}

	/**
	 * This integer stream is OFTEN used internally in monads for calculations.
	 * Rather than type it out in long form, it is aliases to this method.
	 * 
	 * @return Integer stream ranging through all the grades of the algebra
	 */
	public IntStream gradeStream() {
		return IntStream.range(0, getAlgebra().getGradeCount());
	}

	/**
	 * This method suppresses the grade in the Monad equal to the integer passed.
	 * 
	 * @param pGrade byte integer of the grade TO SUPPRESS.
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public MonadAbstract gradeSuppress(byte pGrade) {
		if (pGrade < 0 | pGrade >= getAlgebra().getGradeCount())
			return this;
		scales.zeroAtGrade(pGrade);
		setGradeKey();
		return this;
	}

	/**
	 * Mirror the sense of all geometry generators in the Monad.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public MonadAbstract invert() {
		scales.invert();
		return this;
	}

	/**
	 * This method does a deep check for the equality of two monads. It is not meant
	 * for checking that two monad references actually point to the same object
	 * since that is easily handled with ==. This one checks algebras, foot names,
	 * frame names, and the coefficients. Each object owned by the monad has its own
	 * specialized isEqual() method that gets called.
	 * 
	 * Note that this could be done by override Object's equals() method. That might
	 * happen in the future, but thought will have to be given to how to override
	 * the hashing method too.
	 * 
	 * @param pM MonadAbstract
	 * @return boolean
	 */
	public boolean isGEqual(MonadAbstract pM) {
		if (!MonadAbstract.isReferenceMatch(this, pM))
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
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 * @throws CladosMonadException This exception is thrown when the monad's
	 *                              coefficients aren't in the same field. This
	 *                              should be caught during monad construction, but
	 *                              field coefficients are references so there is
	 *                              always a chance something will happen to alter
	 *                              the object referred to in a list of
	 *                              coefficients.
	 */
	public DivField magnitude() throws CladosMonadException {
		try {
			switch (scales.getMode()) {
			case COMPLEXD -> {
				return (ComplexD) scales.magnitude();
			}
			case COMPLEXF -> {
				return (ComplexF) scales.magnitude();
			}
			case REALD -> {
				return (RealD) scales.magnitude();
			}
			case REALF -> {
				return (RealF) scales.magnitude();
			}
			default -> {
				return null;
			}
			}
		} catch (FieldBinaryException e) {
			throw new CladosMonadException(this, "Coefficients of Monad must be from the same field.");
		}
	}
	
	/**
	 * Monad antisymmetric multiplication: 1/2(pM this - this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM MonadAbstract
	 * @return MonadAbstract
	 * @throws FieldBinaryException This exception is thrown when the field match
	 *                              test fails with the two monads
	 * @throws CladosMonadException
	 */
	public MonadAbstract multiplyAntisymm(MonadAbstract pM) throws FieldBinaryException, CladosMonadException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Symm multiply fails reference match.", pM);
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			MonadAbstract halfTwo = CladosGMonad.COMPLEXD.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).subtract(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case COMPLEXF -> {
			MonadAbstract halfTwo = CladosGMonad.COMPLEXF.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).subtract(halfTwo).scale(ComplexF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		case REALD -> {
			MonadAbstract halfTwo = CladosGMonad.REALD.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).subtract(halfTwo).scale(RealD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case REALF -> {
			MonadAbstract halfTwo = CladosGMonad.REALF.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).subtract(halfTwo).scale(RealF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		}
		setGradeKey();
		return this;
	}

	/**
	 * Monad leftside multiplication: (pM this) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * 
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to
	 * find the non-zero grades. For example: gradeKey=101 means the monad is a sum
	 * of bivector and scalar because 10^2+10^0 = 101.
	 * 
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all
	 * blades less optimal. Instead, we parse gradeKey and loop through the blades
	 * for grades that could be non-ZERO.
	 * 
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd
	 * behavior if one sends in a complex numbers expecting against real numbers.
	 * What IS checked is the cardinal and that likely traps most errors that can be
	 * made. It's not perfect, though. If someone intentionally builds different
	 * number types using the same cardinal, they will get around the detection in
	 * place here.
	 * 
	 * What will happen in that case? The inbound numbers will be multiplied against
	 * coefficients as THEY understand multiplication. The inbound numbers gets cast
	 * to the other, so imaginary components won't get used in real number
	 * multiplication.
	 * 
	 * @param pM MonadAbstract
	 * @throws CladosMonadBinaryException This exception is thrown when the
	 *                                    reference match test fails with the two
	 *                                    monads
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @return MonadAbstract
	 */
	public MonadAbstract multiplyLeft(MonadAbstract pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!MonadAbstract.isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Left multiply fails reference match.", pM);
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		
		switch (scales.getMode()) {
		case COMPLEXD -> {
			Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
				while (logKey >= 0) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !ComplexD.isZero((ComplexD) getScales().get(blade))).parallel()
							.forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream()
										.filter(f -> !ComplexD.isZero((ComplexD) f.getValue())).forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis
													.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
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
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				} // while loop completion -> all grades in 'this' processed
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
			}
			scales = newScales;
		}
		case COMPLEXF -> {
			Scale<ComplexF> newScales = new Scale<ComplexF>(CladosField.COMPLEXF, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
				while (logKey >= 0) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !ComplexF.isZero((ComplexF) getScales().get(blade))).parallel()
							.forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream()
										.filter(f -> !ComplexF.isZero((ComplexF) f.getValue())).forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis
													.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
											try {
												ComplexF tAgg = ComplexF
														.multiply((ComplexF) getScales().get(blade), (ComplexF) f.getValue())
														.scale(Float.valueOf(tProd.getSign(row, col)))
														.add(newScales.get(bMult));
												newScales.put(bMult, tAgg);
											} catch (FieldBinaryException e) {
												throw new IllegalArgumentException(
														"Left multiply fails DivField reference match.");
											}
										});
							});
					slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				} // while loop completion -> all grades in 'this' processed
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !ComplexF.isZero((ComplexF) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.getScales().getMap().entrySet().stream().filter(f -> !ComplexF.isZero((ComplexF) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
										try {
											ComplexF tAgg = ComplexF.multiply((ComplexF) e.getValue(), (ComplexF) f.getValue())
													.scale(Float.valueOf(tProd.getSign(row, col)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Left multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		case REALD -> {
			Scale<RealD> newScales = new Scale<RealD>(CladosField.REALD, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
				while (logKey >= 0) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !RealD.isZero((RealD) getScales().get(blade))).parallel()
							.forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream()
										.filter(f -> !RealD.isZero((RealD) f.getValue())).forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis
													.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
											try {
												RealD tAgg = RealD
														.multiply((RealD) getScales().get(blade), (RealD) f.getValue())
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
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				} // while loop completion -> all grades in 'this' processed
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !RealD.isZero((RealD) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.getScales().getMap().entrySet().stream().filter(f -> !RealD.isZero((RealD) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
										try {
											RealD tAgg = RealD.multiply((RealD) e.getValue(), (RealD) f.getValue())
													.scale(Double.valueOf(tProd.getSign(row, col)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Left multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		case REALF -> {
			Scale<RealF> newScales = new Scale<RealF>(CladosField.REALF, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey); // logKey is the highest grade with non-zero blades
				while (logKey >= 0) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !RealF.isZero((RealF) getScales().get(blade))).parallel()
							.forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream()
										.filter(f -> !RealF.isZero((RealF) f.getValue())).forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis
													.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
											try {
												RealF tAgg = RealF
														.multiply((RealF) getScales().get(blade), (RealF) f.getValue())
														.scale(Float.valueOf(tProd.getSign(row, col)))
														.add(newScales.get(bMult));
												newScales.put(bMult, tAgg);
											} catch (FieldBinaryException e) {
												throw new IllegalArgumentException(
														"Left multiply fails DivField reference match.");
											}
										});
							});
					slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				} // while loop completion -> all grades in 'this' processed
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !RealF.isZero((RealF) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.getScales().getMap().entrySet().stream().filter(f -> !RealF.isZero((RealF) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(row, col)) - 1);
										try {
											RealF tAgg = RealF.multiply((RealF) e.getValue(), (RealF) f.getValue())
													.scale(Float.valueOf(tProd.getSign(row, col)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Left multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		default -> {
			// Do NOTHING
			return this;
		}
		
		}
		setGradeKey();
		return this;
	}
	
	/**
	 * Monad rightside multiplication: (this pM) This operation is allowed when the
	 * two monads use the same field and satisfy the Reference Match test.
	 * 
	 * WHEN SPARSE | Use gradeKey (a base 10 representation of grades present) to
	 * find the non-zero grades. For example: gradeKey=101 means the monad is a sum
	 * of bivector and scalar because 10^2+10^0 = 101.
	 * 
	 * In a sparse monad, the gradeKey will have few 1's, making looping on all
	 * blades less optimal. Instead, we parse gradeKey and loop through the blades
	 * for grades that could be non-ZERO.
	 * 
	 * NOTE that the mode of the inbound monad is NOT checked. That can lead to odd
	 * behavior if one sends in a complex numbers expecting against real numbers.
	 * What IS checked is the cardinal and that likely traps most errors that can be
	 * made. It's not perfect, though. If someone intentionally builds different
	 * number types using the same cardinal, they will get around the detection in
	 * place here.
	 * 
	 * What will happen in that case? The inbound numbers will be multiplied against
	 * coefficients as THEY understand multiplication. The inbound numbers gets cast
	 * to the other, so imaginary components won't get used in real number
	 * multiplication.
	 * 
	 * @param pM MonadAbstract
	 * @throws CladosMonadBinaryException This exception is thrown when the
	 *                                    reference match test fails with the two
	 *                                    monads
	 * @throws FieldBinaryException       This exception is thrown when the field
	 *                                    match test fails with the two monads
	 * @return MonadAbstract
	 */
	public MonadAbstract multiplyRight(MonadAbstract pM) throws FieldBinaryException, CladosMonadBinaryException {
		if (!isReferenceMatch(this, pM)) // Don't try if not a reference match
			throw new CladosMonadBinaryException(this, "Right multiply fails reference match.", pM);
		CliffordProduct tProd = getAlgebra().getGProduct();
		CanonicalBasis tBasis = getAlgebra().getGBasis();
		
		switch (scales.getMode()) {
		case COMPLEXD -> {
			Scale<ComplexD> newScales = new Scale<ComplexD>(CladosField.COMPLEXD, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey);// logKey is the highest grade with non-zero blades
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
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				}
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
			}
			scales = newScales;
		}
		case COMPLEXF -> {
			Scale<ComplexF> newScales = new Scale<ComplexF>(CladosField.COMPLEXF, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey);// logKey is the highest grade with non-zero blades
				while (logKey >= 0.0D) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !ComplexF.isZero((ComplexF) getScales().get(blade)))
							.parallel().forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream().filter(f -> !ComplexF.isZero((ComplexF) f.getValue()))
										.forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
											try {
												ComplexF tAgg = ComplexF
														.multiply((ComplexF) getScales().get(blade), (ComplexF) f.getValue())
														.scale(Float.valueOf(tProd.getSign(col, row)))
														.add(newScales.get(bMult));
												newScales.put(bMult, tAgg);
											} catch (FieldBinaryException e) {
												throw new IllegalArgumentException(
														"Right multiply fails DivField reference match.");
											}
										});
							});
					slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				}
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !ComplexF.isZero((ComplexF) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.scales.getMap().entrySet().stream().filter(f -> !ComplexF.isZero((ComplexF) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
										try {
											ComplexF tAgg = ComplexF.multiply((ComplexF) e.getValue(), (ComplexF) f.getValue())
													.scale(Float.valueOf(tProd.getSign(col, row)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Right multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		case REALD -> {
			Scale<RealD> newScales = new Scale<RealD>(CladosField.REALD, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey);// logKey is the highest grade with non-zero blades
				while (logKey >= 0.0D) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !RealD.isZero((RealD) getScales().get(blade)))
							.parallel().forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream().filter(f -> !RealD.isZero((RealD) f.getValue()))
										.forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
											try {
												RealD tAgg = RealD
														.multiply((RealD) getScales().get(blade), (RealD) f.getValue())
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
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				}
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !RealD.isZero((RealD) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.scales.getMap().entrySet().stream().filter(f -> !RealD.isZero((RealD) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
										try {
											RealD tAgg = RealD.multiply((RealD) e.getValue(), (RealD) f.getValue())
													.scale(Double.valueOf(tProd.getSign(col, row)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Right multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		case REALF -> {
			Scale<RealF> newScales = new Scale<RealF>(CladosField.REALF, tBasis, scales.getCardinal()).zeroAll();
			if (sparseFlag) {
				long slideKey = gradeKey;
				byte logKey = (byte) Math.log10(slideKey);// logKey is the highest grade with non-zero blades
				while (logKey >= 0.0D) {
					tBasis.bladeOfGradeStream(logKey).filter(blade -> !RealF.isZero((RealF) getScales().get(blade)))
							.parallel().forEach(blade -> {
								int col = tBasis.find(blade) - 1;
								pM.getScales().getMap().entrySet().stream().filter(f -> !RealF.isZero((RealF) f.getValue()))
										.forEach(f -> {
											int row = tBasis.find(f.getKey()) - 1;
											Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
											try {
												RealF tAgg = RealF
														.multiply((RealF) getScales().get(blade), (RealF) f.getValue())
														.scale(Float.valueOf(tProd.getSign(col, row)))
														.add(newScales.get(bMult));
												newScales.put(bMult, tAgg);
											} catch (FieldBinaryException e) {
												throw new IllegalArgumentException(
														"Right multiply fails DivField reference match.");
											}
										});
							});
					slideKey -= Math.pow(10, logKey); // Subtract 10^logKey marking grade as done.
					if (slideKey == 0) break; // we've processed all grades including scalar.
					logKey = (byte) Math.log10(slideKey); // if zero it will be the last in loop
				}
			} else { // loop through ALL the blades in 'this' individually.
				getScales().getMap().entrySet().stream().filter(e -> !RealF.isZero((RealF) e.getValue())).parallel()
						.forEach(e -> {
							int col = tBasis.find(e.getKey()) - 1;
							pM.scales.getMap().entrySet().stream().filter(f -> !RealF.isZero((RealF) f.getValue()))
									.forEach(f -> {
										int row = tBasis.find(f.getKey()) - 1;
										Blade bMult = tBasis.getSingleBlade(Math.abs(tProd.getResult(col, row)) - 1);
										try {
											RealF tAgg = RealF.multiply((RealF) e.getValue(), (RealF) f.getValue())
													.scale(Float.valueOf(tProd.getSign(col, row)))
													.add(newScales.get(bMult));
											newScales.put(bMult, tAgg);
										} catch (FieldBinaryException e1) {
											throw new IllegalArgumentException(
													"Right multiply fails DivField reference match.");
										}
									});
						});
			}
			scales = newScales;
		}
		default -> {
			// Do NOTHING
			return this;
		}
		}
		setGradeKey();
		return this;
	}
	
	/**
	 * Monad symmetric multiplication: 1/2(pM this + this pM) This operation is
	 * allowed when the two monads use the same field and satisfy the Reference
	 * Matching test.
	 * 
	 * @param pM MonadAbstract
	 * @return MonadAbstract
	 * @throws FieldBinaryException This exception is thrown when the field match
	 *                              test fails with the two monads
	 * @throws CladosMonadException
	 */
	public MonadAbstract multiplySymm(MonadAbstract pM) throws FieldBinaryException, CladosMonadException {
		if (!isReferenceMatch(this, pM))
			throw new CladosMonadBinaryException(this, "Symm multiply fails reference match.", pM);
		switch (pM.getScales().getMode()) {
		case COMPLEXD -> {
			MonadAbstract halfTwo = CladosGMonad.COMPLEXD.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).add(halfTwo).scale(ComplexD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case COMPLEXF -> {
			MonadAbstract halfTwo = CladosGMonad.COMPLEXF.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).add(halfTwo).scale(ComplexF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		case REALD -> {
			MonadAbstract halfTwo = CladosGMonad.REALD.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).add(halfTwo).scale(RealD.newONE(scales.getCardinal()).scale(CladosConstant.BY2_D));
		}
		case REALF -> {
			MonadAbstract halfTwo = CladosGMonad.REALF.copyOf(this).multiplyRight(pM);
			multiplyLeft(pM).add(halfTwo).scale(RealF.newONE(scales.getCardinal()).scale(CladosConstant.BY2_F));
		}
		}
		setGradeKey();
		return this;
	}
	
	/**
	 * Normalize the monad. A <b>CladosMonadException</b> is thrown if the Monad has
	 * a zero magnitude.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 * @throws CladosMonadException This exception is thrown when normalizing a zero
	 *                              or field conflicted monad is tried.
	 */
	public MonadAbstract normalize() throws CladosMonadException {
		switch (scales.getMode()) {
		case COMPLEXD -> {
			if (gradeKey == 0L & ComplexD.isZero((ComplexD) scales.getScalar()))
				throw new CladosMonadException(this, "Normalizing a zero magnitude Monad isn't possible");
		}
		case COMPLEXF -> {
			if (gradeKey == 0L & ComplexF.isZero((ComplexF) scales.getScalar()))
				throw new CladosMonadException(this, "Normalizing a zero magnitude Monad isn't possible");
		}
		case REALD -> {
			if (gradeKey == 0L & RealD.isZero((RealD) scales.getScalar()))
				throw new CladosMonadException(this, "Normalizing a zero magnitude Monad isn't possible");
		}
		case REALF -> {
			if (gradeKey == 0L & RealF.isZero((RealF) scales.getScalar()))
				throw new CladosMonadException(this, "Normalizing a zero magnitude Monad isn't possible");
		}
		}
		try {
			scales.normalize();
		} catch (FieldException e) {
			throw new CladosMonadException(this,
					"Normalizing a zero magnitude or Field conflicted Monad isn't possible");
		}
		return this;
	}

	/**
	 * This method is a concession to the old notation for the PScalar Part of a
	 * monad. It returns the pscalar part coefficient.
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 */
	@Deprecated
	public DivField PSPc() {
		return scales.getPScalar();
	}

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of all
	 * the permutation, so the easiest thing to do is to change the coefficients
	 * instead.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public MonadAbstract reverse() {
		scales.reverse();
		return this;
	}

	/**
	 * Monad Scaling: (this * real number) Only the Monad coefficients are scaled by
	 * the real number.
	 * 
	 * NOTE that the mode of the inbound scaling number is NOT checked. That can
	 * lead to odd behavior if one sends in a complex number expecting to scale a
	 * real number. What IS checked is the cardinal and that likely traps most
	 * errors that can be made. It's not perfect, though. If someone intentionally
	 * builds different number types using the same cardinal, they will get around
	 * the detection in place here. What will happen in that case? The inbound
	 * number will be multiplied against coefficients as THEY understand
	 * multiplication. The inbound number gets cast to the other, so imaginary
	 * components won't get used in real number multiplication.
	 * 
	 * @param pScale DivField to use for scaling the monad
	 * @return MonadAbstract after the scaling is complete.
	 */
	public MonadAbstract scale(DivField pScale) {

		switch (scales.getMode()) {
		case COMPLEXD -> {
			scales.scale((ComplexD) pScale);
		}
		case COMPLEXF -> {
			scales.scale((ComplexF) pScale);
		}
		case REALD -> {
			scales.scale((RealD) pScale);
		}
		case REALF -> {
			scales.scale((RealF) pScale);
		}
		}
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
	 * @param ppC DivField[]
	 * @throws CladosMonadException This exception is thrown when the array offered
	 *                              for coordinates is of the wrong length.
	 */
	public void setCoeff(DivField[] ppC) throws CladosMonadException {
		if (ppC.length != getAlgebra().getBladeCount())
			throw new CladosMonadException(this,
					"Coefficient array passed in for coefficient copy is the wrong length");
		switch (scales.getMode()) {
		case COMPLEXD -> {
			scales.setCoefficientArray((ComplexD[]) CladosFListBuilder.COMPLEXD.copyOf((ComplexD[]) ppC));
		}
		case COMPLEXF -> {
			scales.setCoefficientArray((ComplexF[]) CladosFListBuilder.COMPLEXF.copyOf((ComplexF[]) ppC));
		}
		case REALD -> {
			scales.setCoefficientArray((RealD[]) CladosFListBuilder.REALD.copyOf((RealD[]) ppC));
		}
		case REALF -> {
			scales.setCoefficientArray((RealF[]) CladosFListBuilder.REALF.copyOf((RealF[]) ppC));
		}
		}
		setGradeKey();
	}

	/**
	 * Reset the name used for the Reference Frame for this Monad This operation
	 * would take place to point out a passive rotation or translation or any other
	 * alteration to the reference frame.
	 * 
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
	 * 
	 * While we are here, we ALSO set the sparseFlag. The nonZero coeff detection
	 * loop that fills gradeKey is a grade detector, so if foundGrade is less than
	 * or equal to half gradeCount, sparseFlag is set to true and false otherwise.
	 */
	public void setGradeKey() {
		foundGrades = 0;
		gradeKey = 0;
		switch (scales.getMode()) {
		case COMPLEXD -> {
			gradeStream().forEach(grade -> {
				if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
						.filter(blade -> !ComplexD.isZero((ComplexD) scales.get(blade))).findAny().isPresent()) {
					foundGrades++;
					gradeKey += Math.pow(10, grade);
				}
			});
		}
		case COMPLEXF -> {
			gradeStream().forEach(grade -> {
				if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
						.filter(blade -> !ComplexF.isZero((ComplexF) scales.get(blade))).findAny().isPresent()) {
					foundGrades++;
					gradeKey += Math.pow(10, grade);
				}
			});
		}
		case REALD -> {
			gradeStream().forEach(grade -> {
				if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
						.filter(blade -> !RealD.isZero((RealD) scales.get(blade))).findAny().isPresent()) {
					foundGrades++;
					gradeKey += Math.pow(10, grade);
				}
			});
		}
		case REALF -> {
			gradeStream().forEach(grade -> {
				if (getAlgebra().getGBasis().bladeOfGradeStream((byte) grade)
						.filter(blade -> !RealF.isZero((RealF) scales.get(blade))).findAny().isPresent()) {
					foundGrades++;
					gradeKey += Math.pow(10, grade);
				}
			});
		}
		}
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
	 * Simple setter of the name of the monad.
	 * 
	 * @param pName String name of the monad to set
	 */
	public void setName(String pName) {
		name = pName;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 */
	@Deprecated
	public DivField SPc() {
		return scales.getScalar();
	}

	/**
	 * Return the magnitude squared of the Monad
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 * @throws CladosMonadException This exception is thrown when the monad's
	 *                              coefficients aren't in the same field. This
	 *                              should be caught during monad construction, but
	 *                              field coefficients are references so there is
	 *                              always a chance something will happen to alter
	 *                              the object referred to in a list of
	 *                              coefficients.
	 */
	public DivField sqMagnitude() throws CladosMonadException {
		try {
			switch (scales.getMode()) {
			case COMPLEXD -> {
				return (ComplexD) scales.sqMagnitude();
			}
			case COMPLEXF -> {
				return (ComplexF) scales.sqMagnitude();
			}
			case REALD -> {
				return (RealD) scales.sqMagnitude();
			}
			case REALF -> {
				return (RealF) scales.sqMagnitude();
			}
			default -> {
				return null;
			}
			}
		} catch (FieldBinaryException e) {
			throw new CladosMonadException(this, "Coefficients of Monad must be from the same field.");
		}
	}
	
	/**
	 * Monad Subtraction: (this - pM) This operation is allowed when the two monads
	 * use the same field and satisfy the Reference Matching test.
	 * 
	 * @param pM MonadAbstract
	 * @throws CladosMonadBinaryException This exception is thrown when the monads
	 *                                    fail a reference match.
	 * @return MonadAbstract
	 */
	public MonadAbstract subtract(MonadAbstract pM) throws CladosMonadBinaryException {
		if (!MonadAbstract.isReferenceMatch(this, pM))
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

	/**
	 * Simple setter method of the algebra for this monad.
	 * 
	 * It is NOT advisable to re-set algebras lightly. They carry the meaning of
	 * 'directions' in the underlying basis.
	 * 
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
	 * 
	 * If the number of found grades is less than or equal to half the grades the
	 * sparse flag is set to true. Otherwise it is set to false.
	 * 
	 * This method isn't actually used by child classes because the method for
	 * setting the gradeKey does the same detection one coefficient at a time
	 * breaking out when a non-zero coeff is found. Incrementing foundGrades in that
	 * loop suffices. Still... there might be a need for this method elsewhere
	 * later.
	 * 
	 * @param pGrades short The parameter is the gradeCount for the monad. It is
	 *                passed into this method rather than looked up in order to
	 *                allow this method to reside in the MonadAbstract class. If it
	 *                were in one of the child monad classes, it would work just as
	 *                well, but it would have to know the child algebra class too in
	 *                order to avoid DivField confusion. Since a monad can be sparse
	 *                or not independent of the DivField used, the method is placed
	 *                here in the abstract parent.
	 */
	protected void setSparseFlag(short pGrades) {
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