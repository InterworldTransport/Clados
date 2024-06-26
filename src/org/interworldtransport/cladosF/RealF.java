/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.RealF<br>
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
 * ---org.interworldtransport.cladosF.RealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import org.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Real Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and RealF is one
 * possibility.
 * <p>
 * There is no doubt that the overhead related to this class is a waste of
 * resources. However, it allows one to plug fields into the algebra classes
 * without having to maintain many different types of monads and nyads. If Java
 * came with primitive types for complex and quaternion fields, and other
 * primitives implemented a 'Field' interface, I wouldn't bother writing this
 * object or any of the other descendants of DivFieldF.
 * <p>
 * Applications requiring speed should use the monads and nyads that implement
 * numbers as primitives. Those classes are marked as such within the library.
 * <p>
 * Ideally, this would extend java.lang.Float and implement an interface called
 * DivFieldF. That can't be done, though, because Float is final.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class RealF extends UnitAbstract implements Field, Normalizable {
	/**
	 * Static add method that creates a new RealF with the sum pF1 + pF2.
	 * <p>
	 * @param pF1 RealF
	 * @param pF2 RealF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return RealF
	 */
	public static RealF add(RealF pF1, RealF pF2) throws FieldBinaryException {
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2) && !RealF.isInfinite(pF1)
				&& !RealF.isInfinite(pF2))
			return RealF.create(pF1.getCardinal(), pF1.getReal() + pF2.getReal());
		throw (new FieldBinaryException(pF1, "Static Addition error found", pF2));
	}

	/**
	 * Static method that creates a new RealF with the conjugate of the parameter.
	 * Since the conjugate of a real number is the real number, this method is
	 * functionally identical to #copy.
	 * <p>
	 * @param pF RealF
	 * @return RealF
	 */
	public static RealF conjugate(RealF pF) {
		return RealF.copyOf(pF);
	}

	/**
	 * This static method takes a list of RealF objects and returns one RealF that
	 * has a value that is equal to the square root of the sum of the SQModulus of
	 * each entry on the list. Because these are real numbers, though, we get away
	 * with simply summing the moduli instead. It does not perform a cardinal safety
	 * check and will throw the exception if that test fails.
	 * <p>
	 * @param pL RealF[]
	 * @throws FieldBinaryException This exception is thrown when sqMagnitude fails
	 *                              with the RealF array
	 * @return RealF
	 */
	public final static RealF copySumModulus(RealF[] pL) throws FieldBinaryException {
		if (pL.length == 0)	throw new IllegalArgumentException("Can't form Modulus Sum from empty array.");
		RealF tR = RealF.copyZERO(pL[0]);
		for (RealF point : pL)
			tR.add((RealF.copyONE(point).scale(point.sqModulus())));
		tR.setReal((float) Math.sqrt(tR.getReal()));
		return tR;
	}

	/**
	 * This static method takes a list of RealF objects and returns one RealF that
	 * has a value that is equal to the sum of the SQModulus of each entry on the
	 * list. It does not perform a cardinal safety check and will throw the
	 * exception if that test fails.
	 * <p>
	 * @param pL RealF[]
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch. It should never happen but the
	 *                              implementation uses multiplication, thus it is
	 *                              technically possible.
	 * @return RealF
	 */
	public final static RealF copySumSQModulus(RealF[] pL) throws FieldBinaryException {
		if (pL.length == 0)	throw new IllegalArgumentException("Can't form SQ Modulus Sum from empty array.");
		RealF tR = RealF.copyZERO(pL[0]);
		for (RealF point : pL)
			tR.add((RealF.copyONE(point).scale(point.sqModulus())));
		return tR;
	}

	/**
	 * Static method that creates a new RealF with a copy of the parameter. This
	 * copy reuses the cardinal reference to ensure it will pass a type match test.
	 * <p>
	 * @param pF RealF
	 * @return RealF
	 */
	public static RealF copyOf(RealF pF) {
		return new RealF(pF);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <p>
	 * @param pR RealF
	 * @return RealF
	 */
	public static RealF copyONE(UnitAbstract pR) {
		return RealF.create(pR.getCardinal(), 1.0f);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <p>
	 * @param pR RealF
	 * @return RealF
	 */
	public static RealF copyZERO(UnitAbstract pR) {
		return RealF.create(pR.getCardinal(), 0.0f);
	}

	/**
	 * Static method that creates a new RealF with a copy of the parameter. This
	 * copy does not reuse a cardinal reference so it is likely to fail type
	 * mismatch tests.
	 * <p>
	 * @param pR float
	 * @return RealF
	 */
	public static RealF create(float pR) {
		return new RealF(pR);
	}

	/**
	 * Static method that creates a new RealF with a float and a Cardinal. This
	 * copy DOES reuse the cardinal so it is likely to pass type mismatch tests.
	 * <p>
	 * @param pCard Cardinal
	 * @param pR float
	 * @return RealF
	 */
	public static RealF create(Cardinal pCard, float pR) {
		return new RealF(pCard, pR);
	}

	/**
	 * Static divide method that creates a new RealF with the product pF1 / pF2.
	 * <p>
	 * @param pF1 RealF
	 * @param pF2 RealF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealF
	 */
	public static RealF divide(RealF pF1, RealF pF2) throws FieldBinaryException {
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isZero(pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2)
				&& !RealF.isInfinite(pF1) && !RealF.isInfinite(pF2))
			return RealF.create(pF1.getCardinal(), pF1.getReal() / pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Division error found", pF2));

	}

	/**
	 * Check for the equality of this object with that of the argument. This checks
	 * for exact equality using no tolerances. The FieldObject types must match
	 * first.
	 * <p>
	 * @param pE RealF
	 * @param pF RealF
	 * @return boolean <i>true</i> if both components are the same; <i>false</i>,
	 *         otherwise.
	 */
	public static boolean isEqual(RealF pE, RealF pF) {
		return UnitAbstract.isTypeMatch(pE, pF) && pE.getReal() == pF.getReal();

	}

	/**
	 * This method checks to see if the value is infinite.
	 * <p>
	 * @param pF RealF
	 * @return boolean
	 */
	public static boolean isInfinite(RealF pF) {
		return Float.isInfinite(pF.getReal());
	}

	/**
	 * This method checks to see if the value is not a number at all. NAN
	 * <p>
	 * @param pF RealF
	 * @return boolean
	 */
	public static boolean isNaN(RealF pF) {
		return Float.isNaN(pF.getReal());
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * <p>
	 * @param pF RealF
	 * @return boolean
	 */
	public static boolean isZero(RealF pF) {
		return (pF.getReal() == 0.0F);
	}

	/**
	 * Static multiply method that creates a new RealF with the product pF1 * pF2.
	 * product.
	 * <p>
	 * @param pF1 RealF
	 * @param pF2 RealF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealF
	 */
	public static RealF multiply(RealF pF1, RealF pF2) throws FieldBinaryException {
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2) && !RealF.isInfinite(pF1)
				&& !RealF.isInfinite(pF2))
			return RealF.create(pF1.getCardinal(), pF1.getReal() * pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}

	/**
	 * Static one construction method
	 * <p>
	 * @param pS String
	 * @return RealF
	 */
	public static RealF newONE(String pS) {
		return RealF.create(Cardinal.generate(pS), 1.0F);
	}

	/**
	 * Static zero construction method
	 * <p>
	 * @param pS String
	 * @return RealF
	 */
	public static RealF newZERO(String pS) {
		return RealF.create(Cardinal.generate(pS), 0.0f);
	}

	/**
	 * Static one construction method
	 * <p>
	 * @param pC Cardinal
	 * @return RealF
	 */
	public static RealF newONE(Cardinal pC) {
		return RealF.create(pC, 1.0f);
	}

	/**
	 * Static zero construction method
	 * <p>
	 * @param pC Cardinal
	 * @return RealF
	 */
	public static RealF newZERO(Cardinal pC) {
		return RealF.create(pC, 0.0f);
	}

	/**
	 * Static subtract method that creates a new RealF with the difference pF1-pF2.
	 * <p>
	 * @param pF1 RealF
	 * @param pF2 RealF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch.
	 * @return RealF
	 */
	public static RealF subtract(RealF pF1, RealF pF2) throws FieldBinaryException {
		if (RealF.isTypeMatch(pF1, pF2) && !RealF.isNaN(pF1) && !RealF.isNaN(pF2) && !RealF.isInfinite(pF1)
				&& !RealF.isInfinite(pF2))
			return RealF.create(pF1.getCardinal(), pF1.getReal() - pF2.getReal());

		throw (new FieldBinaryException(pF1, "Static Subtraction error found", pF2));
	}

	/**
	 * These are the actual java primitives within the UnitAbstract child that as as
	 * 'the number.'
	 */
	protected float[] vals;

	/**
	 * Basic Constructor with no values to initialize.
	 */
	public RealF() {
		super(Cardinal.generate(CladosField.REALF));
		vals = new float[1];
		setReal(0.0F);

	}

	/**
	 * Basic Constructor with only the cardinal to initialize.
	 * <p>
	 * @param pT Cardinal
	 */
	public RealF(Cardinal pT) {
		super(pT);
		vals = new float[1];
		setReal(0.0F);

	}

	/**
	 * Basic Constructor with everything to initialize.
	 * <p>
	 * @param pT Cardinal
	 * @param pR float
	 */
	public RealF(Cardinal pT, float pR) {
		super(pT);
		vals = new float[1];
		setReal(pR);

	}

	/**
	 * Basic Constructor with only the number to initialize.
	 * <p>
	 * @param pR float
	 */
	public RealF(float pR) {
		super(Cardinal.generate(CladosField.REALF));
		vals = new float[1];
		setReal(pR);

	}

	/**
	 * Copy Constructor that reuses the cardinal reference.
	 * <p>
	 * @param pR RealF
	 */
	public RealF(RealF pR) {
		super(pR.getCardinal());
		vals = new float[1];
		setReal(pR.getReal());

	}

	/**
	 * Copy Constructor that reuses the cardinal reference while allowing the value
	 * to be set.
	 * <p>
	 * @param pR RealF
	 * @param pF float
	 */
	public RealF(UnitAbstract pR, float pF) {
		super(pR.getCardinal());
		vals = new float[1];
		setReal(pF);

	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when a field mismatch
	 *                              happens
	 * @return RealF
	 */
	@Override
	public RealF add(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || RealF.isNaN(this) || RealF.isNaN((RealF) pF)
				|| RealF.isInfinite(this) || RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Addition failed type match or size test", (UnitAbstract) pF));

		setReal(getReal() + ((RealF) pF).getReal());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all of
	 * its imaginary members are set to their additive inverses.
	 * <p>
	 * @return RealF
	 */
	@Override
	public RealF conjugate() {

		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches or
	 *                              division by zero happens
	 * @return RealF
	 */
	@Override
	public RealF divide(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || RealF.isNaN(this) || RealF.isNaN((RealF) pF)
				|| RealF.isInfinite(this) || RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Divide failed type match or size test", (UnitAbstract) pF));
		if (RealF.isZero((RealF) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (UnitAbstract) pF));

		setReal(getReal() / ((RealF) pF).getReal());
		return this;
	}

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * <p>
	 * @return Float
	 */
	@Override
	public Float modulus() {
		return Float.valueOf((float) Math.sqrt(sqModulus().floatValue()));
	}

	/**
	 * Get the real numeric value from the value array
	 * <p>
	 * @return float
	 */
	public float getReal() {
		return vals[0];
	}

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense to
	 * calculate this before the modulus itself.
	 * <p>
	 * @return Float
	 */
	@Override
	public Float sqModulus() {
		float tR = 0f;
		for (float point : vals)
			tR += point * point;
		return Float.valueOf(tR);
	}

	/**
	 * This method inverts real numbers.
	 * <p>
	 * @throws FieldException This exception is thrown if someone tries to invert a
	 *                        ZERO.
	 * @return RealF
	 */
	@Override
	public RealF invert() throws FieldException {
		if (RealF.isZero(this))
			throw new FieldException(this, "Can't invert a zero RealF");

		setReal(1.0F / getReal());
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches happen
	 * @return RealF
	 */
	@Override
	public RealF multiply(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || RealF.isNaN(this) || RealF.isNaN((RealF) pF)
				|| RealF.isInfinite(this) || RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match or size test", (UnitAbstract) pF));

		setReal(getReal() * ((RealF) pF).getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * <p>
	 * @param pS Number
	 * @return RealF
	 */
	@Override
	public RealF scale(Number pS) {
		setReal(pS.floatValue() * getReal());

		return this;
	}

	/**
	 * Set the real numeric value
	 * <p>
	 * @param preal float
	 */
	public void setReal(float preal) {
		vals[0] = preal;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * @return RealF
	 */
	@Override
	public RealF subtract(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || RealF.isNaN(this) || RealF.isNaN((RealF) pF)
				|| RealF.isInfinite(this) || RealF.isInfinite((RealF) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match or size test", (UnitAbstract) pF));

		setReal(getReal() - ((RealF) pF).getReal());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * <p>
	 * @return String
	 */
	@Override
	public String toString() {
		return (getReal() + "FR");
	}

	/**
	 * Return a string representation of the real value.
	 * <p>
	 * @return String
	 */
	@Override
	public String toXMLString() {
		return ("<RealF cardinal=\"" + getCardinalString() + "\" realvalue=\"" + getReal() + "\" />");
	}
}