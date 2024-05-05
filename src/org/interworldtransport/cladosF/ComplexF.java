/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ComplexF<br>
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
 * ---org.interworldtransport.cladosF.ComplexF<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import org.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Complex Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and ComplexF is one
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
public class ComplexF extends UnitAbstract implements Field, Normalizable {
	/**
	 * Static add method that creates a new ComplexF with the sum pF1 + pF2.
	 * <p>
	 * @param pF1 ComplexF
	 * @param pF2 ComplexF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return ComplexF
	 */
	public static ComplexF add(ComplexF pF1, ComplexF pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexF.isNaN(pF1) && !ComplexF.isNaN(pF2) && !ComplexF.isInfinite(pF1)
				&& !ComplexF.isInfinite(pF2))
			return ComplexF.create(pF1.getCardinal(), pF1.getReal() + pF2.getReal(), pF1.getImg() + pF2.getImg());
		throw (new FieldBinaryException(pF1, "Static Addition error found", pF2));
	}

	/**
	 * Static method that creates a new ComplexF with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * <p>
	 * @param pF ComplexF
	 * @return ComplexF
	 */
	public static ComplexF conjugate(ComplexF pF) {
		return ComplexF.create(pF.getCardinal(), pF.getReal(), -1.0f * pF.getImg());
	}

	/**
	 * This static method takes a list of ComplexF objects and returns one ComplexF
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list.
	 * <p>
	 * It does not perform a cardinal safety check and will throw the exception if
	 * that test fails.
	 * <p>
	 * @param pL ComplexF[]
	 * @throws FieldBinaryException This exception happens when there is a field
	 *                              mismatch. It shouldn't happen but it is
	 *                              technically possible because of the dependence
	 *                              on sqMagnitude.
	 * @return complexF
	 */
	public final static ComplexF copySumModulus(ComplexF[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form Modulus Sum from empty array.");
		ComplexF tR = ComplexF.copyZERO(pL[0]);
		for (ComplexF point : pL)
			tR.add((ComplexF.copyONE(point).scale(point.modulus())));
		tR.setReal((float) Math.sqrt(tR.getReal()));
		return tR;
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD that
	 * has a value that is equal to the sum of the SQModulus of each entry on the
	 * list. It does not perform a cardinal safety check and will throw the
	 * exception if that test fails.
	 * <p>
	 * @param pL ComplexF[]
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch. It should never happen but the
	 *                              implementation uses multiplication, thus it is
	 *                              technically possible.
	 * @return ComplexF
	 */
	public final static ComplexF copySumSQModulus(ComplexF[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form SQ Modulus Sum from empty array.");
		ComplexF tR = ComplexF.copyZERO(pL[0]);
		for (ComplexF point : pL)
			tR.add((ComplexF.copyONE(point).scale(point.sqModulus())));
		return tR;
	}

	/**
	 * Static method that creates a new ComplexF with a copy of the parameter. This
	 * copy reuses the cardinal reference to ensure it will pass a type match test.
	 * <p>
	 * @param pF ComplexF
	 * @return ComplexF
	 */
	public static ComplexF copyOf(ComplexF pF) {
		return new ComplexF(pF);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <p>
	 * @param pR ComplexF
	 * @return ComplexF
	 */
	public static ComplexF copyONE(UnitAbstract pR) {
		return ComplexF.create(pR.getCardinal(), 1.0f, 0.0f);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <p>
	 * @param pR ComplexF
	 * @return ComplexF
	 */
	public static ComplexF copyZERO(UnitAbstract pR) {
		return ComplexF.create(pR.getCardinal(), 0.0f, 0.0f);
	}

	/**
	 * Static method that creates a new ComplexF with a copy of the parameter. This
	 * copy does not reuse a cardinal reference so it is likely to fail type
	 * mismatch tests.
	 * <p>
	 * @param pR float
	 * @param pI float
	 * @return ComplexF
	 */
	public static ComplexF create(float pR, float pI) {
		return new ComplexF(pR, pI);
	}

	/**
	 * Static method that creates a new ComplexF with floats and a Cardinal. This
	 * copy DOES reuse the cardinal so it is likely to pass type mismatch tests.
	 * <p>
	 * @param pCard Cardinal
	 * @param pR float
	 * @param pI float
	 * @return ComplexF
	 */
	public static ComplexF create(Cardinal pCard, float pR, float pI) {
		return new ComplexF(pCard, pR, pI);
	}

	/**
	 * Static divide method that creates a new ComplexF with the product pF1 / pF2.
	 * <p>
	 * @param pF1 ComplexF
	 * @param pF2 ComplexF
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return ComplexF
	 */
	public static ComplexF divide(ComplexF pF1, ComplexF pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexF.isZero(pF2) && !ComplexF.isNaN(pF1) && !ComplexF.isNaN(pF2)
				&& !ComplexF.isInfinite(pF1) && !ComplexF.isInfinite(pF2)) {
			ComplexF tZ = ComplexF.copyOf(pF1);
			ComplexF tZ2 = ComplexF.copyOf(pF2);
			tZ2.conjugate();
			tZ.multiply(tZ2);
			tZ2.conjugate();
			tZ.scale(Float.valueOf(1.0f / tZ2.sqModulus()));
			return tZ;
		}
		throw (new FieldBinaryException(pF1, "Static Division error found", pF2));
	}

	/**
	 * Check for the equality of this object with that of the argument. This checks
	 * for exact equality using no tolerances. The FieldObject types must match
	 * first.
	 * <p>
	 * @param pE ComplexF
	 * @param pF ComplexF
	 * @return boolean <i>true</i> if both components are the same; <i>false</i>,
	 *         otherwise.
	 */
	public static boolean isEqual(ComplexF pE, ComplexF pF) {
		return UnitAbstract.isTypeMatch(pE, pF) && (pE.getReal() == pF.getReal()) && (pE.getImg() == pF.getImg());
	}

	/**
	 * Returns true if the real part is zero and imaginary part is not zero.
	 * <p>
	 * @param pF ComplexF
	 * @return boolean
	 */
	public static boolean isImaginary(ComplexF pF) {
		return (pF.getReal() == 0.0f && pF.getImg() != 0.0f);
	}

	/**
	 * This method checks to see if either value is infinite.
	 * <p>
	 * @param pF ComplexF
	 * @return boolean
	 */
	public static boolean isInfinite(ComplexF pF) {
		return Float.isInfinite(pF.getReal()) || Float.isInfinite(pF.getImg());
	}

	/**
	 * This method checks to see if either value is not a number at all. NAN
	 * <p>
	 * @param pF ComplexF
	 * @return boolean
	 */
	public static boolean isNaN(ComplexF pF) {
		return Float.isNaN(pF.getReal()) || Float.isNaN(pF.getImg());
	}

	/**
	 * Returns true if the imaginary part is zero
	 * <p>
	 * @param pF ComplexF
	 * @return boolean
	 */
	public static boolean isReal(ComplexF pF) {
		return (pF.getImg() == 0.0f);
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * <p>
	 * @param pF ComplexF
	 * @return boolean
	 */
	public static boolean isZero(ComplexF pF) {
		return (pF.getReal() == 0.0f && pF.getImg() == 0.0f);
	}

	/**
	 * Static multiply method that creates a new ComplexF with the product pF1 *
	 * pF2.
	 * <p>
	 * @param pF1 ComplexF
	 * @param pF2 ComplexF
	 * @throws FieldBinaryException This exception happens when there is a field
	 *                              mismatch.
	 * @return complexF
	 */
	public static ComplexF multiply(ComplexF pF1, ComplexF pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexF.isNaN(pF1) && !ComplexF.isNaN(pF2) && !ComplexF.isInfinite(pF1)
				&& !ComplexF.isInfinite(pF2)) {
			float tempR = pF1.getReal() * pF2.getReal() - pF1.getImg() * pF2.getImg();
			float tempI = pF1.getReal() * pF2.getImg() + pF1.getImg() * pF2.getReal();
			return ComplexF.create(pF1.getCardinal(), tempR, tempI);
		}
		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}

	/**
	 * Static one construction method
	 * <p>
	 * @param pS String
	 * @return ComplexF
	 */
	public static ComplexF newONE(String pS) {
		return ComplexF.create(Cardinal.generate(pS), 1.0f, 0.0f);
	}

	/**
	 * Static zero construction method
	 * <p>
	 * @param pS String
	 * @return ComplexF
	 */
	public static ComplexF newZERO(String pS) {
		return ComplexF.create(Cardinal.generate(pS), 0.0f, 0.0f);
	}

	/**
	 * Static one construction method
	 * <p>
	 * @param pC Cardinal
	 * @return ComplexF
	 */
	public static ComplexF newONE(Cardinal pC) {
		return ComplexF.create(pC, 1.0f, 0.0f);
	}

	/**
	 * Static zero construction method
	 * <p>
	 * @param pC Cardinal
	 * @return ComplexF
	 */
	public static ComplexF newZERO(Cardinal pC) {
		return ComplexF.create(pC, 0.0f, 0.0f);
	}

	/**
	 * Static subtract method that creates a new ComplexF with the difference
	 * pF1-pF2.
	 * <p>
	 * @param pF1 ComplexF
	 * @param pF2 ComplexF
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch.
	 * @return ComplexF
	 */
	public static ComplexF subtract(ComplexF pF1, ComplexF pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexF.isNaN(pF1) && !ComplexF.isNaN(pF2) && !ComplexF.isInfinite(pF1)
				&& !ComplexF.isInfinite(pF2))
			return ComplexF.create(pF1.getCardinal(), pF1.getReal() - pF2.getReal(), pF1.getImg() - pF2.getImg());

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
	public ComplexF() {
		super(Cardinal.generate(CladosField.COMPLEXF));
		vals = new float[2];
		setReal(0.0f);
		setImg(0.0f);
	}

	/**
	 * Copy Constructor that reuses the cardinal reference.
	 * <p>
	 * @param pC ComplexF
	 */
	public ComplexF(ComplexF pC) {
		super(pC.getCardinal());
		vals = new float[2];
		setReal(pC.getReal());
		setImg(pC.getImg());
	}

	/**
	 * Copy Constructor that reuses the cardinal reference while allowing the values
	 * to be set.
	 * <p>
	 * @param pC ComplexF
	 * @param pR float
	 * @param pI float
	 */
	public ComplexF(UnitAbstract pC, float pR, float pI) {
		super(pC.getCardinal());
		vals = new float[2];
		setCardinal(pC.getCardinal());
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the cardinal to initialize.
	 * <p>
	 * @param pT Cardinal
	 */
	public ComplexF(Cardinal pT) {
		super(pT);
		vals = new float[2];
		setReal(0.0f);
		setImg(0.0f);
	}

	/**
	 * Basic Constructor with everything to initialize but the imaginary.
	 * <p>
	 * @param pT Cardinal
	 * @param pR float
	 */
	public ComplexF(Cardinal pT, float pR) {
		super(pT);
		vals = new float[2];
		setReal(pR);
		setImg(0.0f);
	}

	/**
	 * Basic Constructor with everything to initialize it.
	 * <p>
	 * @param pT Cardinal
	 * @param pR float
	 * @param pI float
	 */
	public ComplexF(Cardinal pT, float pR, float pI) {
		super(pT);
		vals = new float[2];
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the number to initialize.
	 * <p>
	 * @param pR float
	 * @param pI float
	 */
	public ComplexF(float pR, float pI) {
		super(Cardinal.generate(CladosField.COMPLEXF));
		vals = new float[2];
		setReal(pR);
		setImg(pI);
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 *<p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when a field mismatch
	 *                              happens
	 * @return ComplexF
	 */
	@Override
	public ComplexF add(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexF.isNaN(this) || ComplexF.isNaN((ComplexF) pF)
				|| ComplexF.isInfinite(this) || ComplexF.isInfinite((ComplexF) pF))
			throw (new FieldBinaryException(this, "Addition failed type match or size test", (UnitAbstract) pF));
		setReal(getReal() + ((ComplexF) pF).getReal());
		setImg(getImg() + ((ComplexF) pF).getImg());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all of
	 * its imaginary members are set to their additive inverses.
	 * <p>
	 * @return ComplexF
	 */
	@Override
	public ComplexF conjugate() {
		setImg(-1.0f * getImg());
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches or
	 *                              division by zero happens
	 * @return ComplexF
	 */
	@Override
	public ComplexF divide(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexF.isNaN(this) || ComplexF.isNaN((ComplexF) pF)
				|| ComplexF.isInfinite(this) || ComplexF.isInfinite((ComplexF) pF))
			throw (new FieldBinaryException(this, "Divide failed type match or size test", (UnitAbstract) pF));
		if (ComplexF.isZero((ComplexF) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (UnitAbstract) pF));

		ComplexF tempZ = ComplexF.copyOf((ComplexF) pF);
		tempZ.conjugate();
		multiply(tempZ);
		tempZ.conjugate();
		scale(Float.valueOf(1.0f / tempZ.sqModulus()));
		return this;
	}

	/**
	 * Get method for the argument of the complex number. This function uses the
	 * arctangent function, so its range and domain are the same.
	 * <p>
	 * @return float
	 */
	public float getArgument() {
		if (!ComplexF.isImaginary(this))
			return (float) Math.atan(getImg() / getReal());

		return (float) ((getImg() >= 0f) ? Math.PI / 2.0f : 3.0D * Math.PI / 2.0f);
	}

	/**
	 * Get the imaginary numeric value from the value array
	 * <p>
	 * @return float
	 */
	public float getImg() {
		return vals[1];
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
	 * @throws FieldException This exception is thrown when someone tries to invert
	 *                        ZERO.
	 * @return ComplexF
	 */
	@Override
	public ComplexF invert() throws FieldException {
		if (ComplexF.isZero(this))
			throw new FieldException(this, "Can't invert a zero ComplexF");

		float tM = 1.0f / modulus();
		float tA = -1.0f * getArgument();
		setReal((float) (tM * Math.cos(tA)));
		setImg((float) (tM * Math.sin(tA)));
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the result.
	 * <p>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * @return ComplexF
	 */
	@Override
	public ComplexF multiply(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexF.isNaN(this) || ComplexF.isNaN((ComplexF) pF)
				|| ComplexF.isInfinite(this) || ComplexF.isInfinite((ComplexF) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match or size test", (UnitAbstract) pF));
		ComplexF tempZ = (ComplexF) pF;
		setReal(getReal() * tempZ.getReal() - getImg() * tempZ.getImg());
		setImg(getReal() * tempZ.getImg() + getImg() * tempZ.getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * <p>
	 * @param pS Number
	 * @return ComplexF
	 */
	@Override
	public ComplexF scale(Number pS) {
		setReal(pS.floatValue() * getReal());
		setImg(pS.floatValue() * getImg());
		return this;
	}

	/**
	 * Set the imaginary numeric value
	 * <p>
	 * @param pimg float
	 */
	public void setImg(float pimg) {
		vals[1] = pimg;
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
	 * @return ComplexF
	 */
	@Override
	public ComplexF subtract(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexF.isNaN(this) || ComplexF.isNaN((ComplexF) pF)
				|| ComplexF.isInfinite(this) || ComplexF.isInfinite((ComplexF) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match or size test", (UnitAbstract) pF));
		
		setReal(getReal() - ((ComplexF) pF).getReal());
		setImg(getImg() - ((ComplexF) pF).getImg());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * <p>
	 * @return String
	 */
	@Override
	public String toString() {
		return (getReal() + "FR, " + getImg() + "FI");
	}

	/**
	 * Return a string representation of the real value.
	 * <p>
	 * @return String
	 */
	@Override
	public String toXMLString() {
		return ("<ComplexF cardinal=\"" + getCardinalString() + "\" realvalue=\"" + getReal() + "\" imgvalue=\""
				+ getImg() + "\"/>");
	}
}