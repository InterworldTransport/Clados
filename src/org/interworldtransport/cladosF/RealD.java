/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.RealD<br>
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
 * ---org.interworldtransport.cladosF.RealD<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import org.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Real Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and RealD is one
 * possibility.
 * <p>
 * There is no doubt that the overhead related to this class is a waste of
 * resources. However, it allows one to plug fields into the algebra classes
 * without having to maintain many different types of monads and nyads. If Java
 * came with primitive types for complex and quaternion fields, and other
 * primitives implemented a 'Field' interface, I wouldn't bother writing this
 * object or any of the other descendants of DivFieldD.
 * <p>
 * Applications requiring speed should use the monads and nyads that implement
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Double and implement an interface called
 * DivFieldD. That can't be done, though, because Double is final.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class RealD extends UnitAbstract implements Field, Normalizable {
	/**
	 * Static add method that creates a new RealD with the sum pF1 + pF2.
	 * 
	 * @param pF1 RealD
	 * @param pF2 RealD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return RealD
	 */
	public static RealD add(RealD pF1, RealD pF2) throws FieldBinaryException {
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2) && !RealD.isInfinite(pF1)
				&& !RealD.isInfinite(pF2))
			return new RealD(pF1.getCardinal(), pF1.getReal() + pF2.getReal());
		throw (new FieldBinaryException(pF1, "Static Addition error found", pF2));
	}

	/**
	 * Static method that creates a new RealD with the conjugate of the parameter.
	 * Since the conjugate of a real number is the real number, this method is
	 * functionally identical to #copy.
	 * 
	 * @param pF RealD
	 * @return RealD
	 */
	public static RealD conjugate(RealD pF) {
		return new RealD(pF);
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD that
	 * has a value that is equal to the square root of the sum of the SQModulus of
	 * each entry on the list. Because these are real numbers, though, we get away
	 * with simply summing the moduli instead. It does not perform a cardinal safety
	 * check and will throw the exception if that test fails.
	 * 
	 * @param pL RealD[]
	 * 
	 * @throws FieldBinaryException This exception is thrown when sqMagnitude fails
	 *                              with the RealF array
	 * @return RealD
	 */
	public final static RealD copyFromModuliSum(RealD[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form Modulus Sum from empty array.");
		RealD tR = RealD.copyZERO(pL[0]);
		for (RealD point : pL)
			tR.add((RealD.copyONE(point).scale(point.modulus())));
		return tR;
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD that
	 * has a value that is equal to the sum of the SQModulus of each entry on the
	 * list. It does not perform a cardinal safety check and will throw the
	 * exception if that test fails.
	 * 
	 * @param pL RealD[]
	 * 
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch. It should never happen but the
	 *                              implementation uses multiplication, thus it is
	 *                              technically possible.
	 * 
	 * @return RealD
	 */
	public final static RealD copyFromSQModuliSum(RealD[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form SQ Modulus Sum from empty array.");
		RealD tR = RealD.copyZERO(pL[0]);
		for (RealD point : pL)
			tR.add((RealD.copyONE(point).scale(point.sqModulus())));
		return tR;
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy reuses the cardinal reference to ensure it will pass a type match test.
	 * 
	 * @param pF RealD
	 * @return RealD
	 */
	public static RealD copyOf(RealD pF) {
		return new RealD(pF);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * 
	 * @param pR RealD
	 * 
	 * @return RealD
	 */
	public static RealD copyONE(UnitAbstract pR) {
		return new RealD(pR.getCardinal(), 1.0D);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * 
	 * @param pR RealD
	 * 
	 * @return RealD
	 */
	public static RealD copyZERO(UnitAbstract pR) {
		return new RealD(pR.getCardinal(), 0.0D);
	}

	/**
	 * Static method that creates a new RealD with a copy of the parameter. This
	 * copy does not reuse a cardinal reference so it is likely to fail type
	 * mismatch tests.
	 * 
	 * @param pR double
	 * 
	 * 
	 * @return RealD
	 */
	public static RealD create(double pR) {
		return new RealD(pR);
	}

	/**
	 * Static divide method that creates a new RealD with the product pF1 / pF2.
	 * 
	 * @param pF1 RealD
	 * @param pF2 RealD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealD
	 */
	public static RealD divide(RealD pF1, RealD pF2) throws FieldBinaryException {
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isZero(pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2)
				&& !RealD.isInfinite(pF1) && !RealD.isInfinite(pF2))
			return new RealD(pF1.getCardinal(), pF1.getReal() / pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Division error found", pF2));

	}

	/**
	 * Check for the equality of this object with that of the argument. This checks
	 * for exact equality using no tolerances. The FieldObject types must match
	 * first.
	 * 
	 * @param pE RealD
	 * @param pF RealD
	 * @return boolean <i>true</i> if both components are the same; <i>false</i>,
	 *         otherwise.
	 */
	public static boolean isEqual(RealD pE, RealD pF) {
		return UnitAbstract.isTypeMatch(pE, pF) && (pE.getReal() == pF.getReal());

	}

	/**
	 * This method checks to see if the value is infinite.
	 * 
	 * @param pF RealD
	 * @return boolean
	 */
	public static boolean isInfinite(RealD pF) {
		return Double.isInfinite(pF.getReal());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 * 
	 * @param pF RealD
	 * @return boolean
	 */
	public static boolean isNaN(RealD pF) {
		return Double.isNaN(pF.getReal());
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * 
	 * @param pF RealD
	 * @return boolean
	 */
	public static boolean isZero(RealD pF) {
		return (pF.getReal() == 0.0D);
	}

	/**
	 * Static multiply method that creates a new RealD with the product pF1 * pF2.
	 * product.
	 * 
	 * @param pF1 RealD
	 * @param pF2 RealD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealD
	 */
	public static RealD multiply(RealD pF1, RealD pF2) throws FieldBinaryException {
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2) && !RealD.isInfinite(pF1)
				&& !RealD.isInfinite(pF2))
			return new RealD(pF1.getCardinal(), pF1.getReal() * pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}

	/**
	 * Static one construction method
	 * 
	 * @param pS String
	 * 
	 * @return RealD
	 */
	public static RealD newONE(String pS) {
		return new RealD(Cardinal.generate(pS), 1.0D);
	}

	/**
	 * Static zero construction method
	 * 
	 * @param pS String
	 * 
	 * @return RealD
	 */
	public static RealD newZERO(String pS) {
		return new RealD(Cardinal.generate(pS), 0.0D);
	}

	/**
	 * Static one construction method
	 * 
	 * @param pC Cardinal
	 * 
	 * @return RealD
	 */
	public static RealD newONE(Cardinal pC) {
		return new RealD(pC, 1.0D);
	}

	/**
	 * Static zero construction method
	 * 
	 * @param pC Cardinal
	 * 
	 * @return RealD
	 */
	public static RealD newZERO(Cardinal pC) {
		return new RealD(pC, 0.0D);
	}

	/**
	 * Static subtract method that creates a new RealD with the difference pF1-pF2.
	 * 
	 * @param pF1 RealD
	 * @param pF2 RealD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealD
	 */
	public static RealD subtract(RealD pF1, RealD pF2) throws FieldBinaryException {
		if (RealD.isTypeMatch(pF1, pF2) && !RealD.isNaN(pF1) && !RealD.isNaN(pF2) && !RealD.isInfinite(pF1)
				&& !RealD.isInfinite(pF2))
			return new RealD(pF1.getCardinal(), pF1.getReal() - pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Subtraction error found", pF2));
	}

	/**
	 * These are the actual java primitives within the UnitAbstract child that as as
	 * 'the number.'
	 */
	protected double[] vals;

	/**
	 * Basic Constructor with no values to initialize.
	 */
	public RealD() {
		super(Cardinal.generate(CladosField.REALD));
		vals = new double[1];
		setReal(0.0D);

	}

	/**
	 * Basic Constructor with only the cardinal to initialize.
	 * 
	 * @param pT Cardinal
	 */
	public RealD(Cardinal pT) {
		super(pT);
		vals = new double[1];
		setReal(0.0D);

	}

	/**
	 * Basic Constructor with everything to initialize.
	 * 
	 * @param pT Cardinal
	 * @param pR double
	 */
	public RealD(Cardinal pT, double pR) {
		super(pT);
		vals = new double[1];
		setReal(pR);

	}

	/**
	 * Basic Constructor with only the number to initialize.
	 * 
	 * @param pR double
	 * 
	 * 
	 */
	public RealD(double pR) {
		super(Cardinal.generate("Real"));
		vals = new double[1];
		setReal(pR);

	}

	/**
	 * Copy Constructor that reuses the cardinal reference.
	 * 
	 * @param pR RealD
	 */
	public RealD(RealD pR) {
		super(pR.getCardinal());
		vals = new double[1];
		setReal(pR.getReal());

	}

	/**
	 * Copy Constructor that reuses the cardinal reference while allowing the value
	 * to be set.
	 * 
	 * @param pR RealD
	 * 
	 * 
	 * @param pD float
	 */
	public RealD(UnitAbstract pR, double pD) {
		super(pR.getCardinal());
		vals = new double[1];
		setReal(pD);

	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * 
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when a field mismatch
	 *                              happens
	 * 
	 * @return RealD
	 */
	@Override
	public RealD add(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF)
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Addition failed type match test", (UnitAbstract) pF));

		setReal(getReal() + ((RealD) pF).getReal());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all of
	 * its imaginary members are set to their additive inverses.
	 * 
	 * @return RealD
	 */
	@Override
	public RealD conjugate() {

		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the result.
	 * 
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches or
	 *                              division by zero happens
	 * 
	 * @return RealD
	 */
	@Override
	public RealD divide(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF)
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Divide failed type match test", (UnitAbstract) pF));

		if (RealD.isZero((RealD) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (UnitAbstract) pF));

		setReal(getReal() / ((RealD) pF).getReal());
		return this;
	}

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * 
	 * @return Double
	 */
	@Override
	public Double modulus() {
		return Double.valueOf((double) Math.sqrt(sqModulus().doubleValue()));
	}

	/**
	 * Get the real numeric value from the value array
	 * 
	 * @return double
	 */
	public double getReal() {
		return vals[0];
	}

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense to
	 * calculate this before the modulus itself.
	 * 
	 * @return Double
	 */
	@Override
	public Double sqModulus() {
		double tR = 0d;
		for (double point : vals)
			tR += point * point;
		return Double.valueOf(tR);
	}

	/**
	 * This method inverts real numbers.
	 * 
	 * @throws FieldException This exception is thrown if someone tries to invert a
	 *                        ZERO.
	 * 
	 * @return RealD
	 */
	@Override
	public RealD invert() throws FieldException {
		if (RealD.isZero(this))
			throw new FieldException(this, "Can't invert a zero RealD");

		setReal(1.0D / getReal());
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the result.
	 * 
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * 
	 * @return RealD
	 */
	@Override
	public RealD multiply(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF)
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match test", (UnitAbstract) pF));

		setReal(getReal() * ((RealD) pF).getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS Number
	 * 
	 * @return RealD
	 */
	@Override
	public RealD scale(Number pS) {
		setReal(pS.doubleValue() * getReal());

		return this;
	}

	/**
	 * Set the real numeric value
	 * 
	 * @param preal double
	 */
	public void setReal(double preal) {
		vals[0] = preal;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the result.
	 * 
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * 
	 * @return RealD
	 */
	@Override
	public RealD subtract(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) && !RealD.isNaN(this) && !RealD.isNaN((RealD) pF)
				&& !RealD.isInfinite(this) && !RealD.isInfinite((RealD) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match test", (UnitAbstract) pF));

		setReal(getReal() - ((RealD) pF).getReal());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return (getReal() + "DR");
	}

	/**
	 * Return a string representation of the real value.
	 * 
	 * @return String
	 */
	@Override
	public String toXMLString() {
		return ("<RealD cardinal=\"" + getCardinalString() + "\" realvalue=\"" + getReal() + "\" />");
	}
}