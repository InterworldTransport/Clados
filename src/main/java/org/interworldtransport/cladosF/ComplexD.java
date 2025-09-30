/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ComplexD<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ComplexD<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import org.interworldtransport.cladosFExceptions.*;

/**
 * This class implements the concept of a Complex Field from mathematics. Field
 * objects within the cladosF package are used as 'numbers' in the definition of
 * an algebra. All Clados objects use DivFieldElements and ComplexD is one
 * possibility.
 * <br>
 * There is no doubt that the overhead related to this class is a waste of
 * resources. However, it allows one to plug fields into the algebra classes
 * without having to maintain many different types of monads and nyads. If Java
 * came with primitive types for complex and quaternion fields, and other
 * primitives implemented a 'Field' interface, I wouldn't bother writing this
 * object or any of the other descendants of DivFieldD.
 * <br>
 * Applications requiring speed should use the monads and nyads that implement
 * numbers as primitives. Those classes are marked as such within the library.
 * <br>
 * Ideally, this would extend java.lang.Double and implement an interface called
 * DivFieldD. That can't be done, though, because Double is final.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class ComplexD extends UnitAbstract implements Field, Normalizable {
	/**
	 * Static add method that creates a new ComplexD with the sum pF1 + pF2.
	 * <br>
	 * @param pF1 ComplexD
	 * @param pF2 ComplexD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return ComplexD
	 */
	public static ComplexD add(ComplexD pF1, ComplexD pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2) && !ComplexD.isInfinite(pF1)
				&& !ComplexD.isInfinite(pF2))
			return ComplexD.create(pF1.getCardinal(), pF1.getReal() + pF2.getReal(), pF1.getImg() + pF2. getImg());
		throw (new FieldBinaryException(pF1, "Static Addition error found", pF2));
	}

	/**
	 * Static method that creates a new ComplexD with the conjugate of the
	 * parameter. Since the conjugate of a real number is the real number, this
	 * method is functionally identical to #copy.
	 * <br>
	 * @param pF ComplexD
	 * @return ComplexD
	 */
	public static ComplexD conjugate(ComplexD pF) {
		return ComplexD.create(pF.getCardinal(), pF.getReal(), -1.0D * pF.getImg());
	}

	/**
	 * This static method takes a list of ComplexD objects and returns one ComplexD
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each entry on the list.
	 * <br>
	 * It does not perform a cardinal safety check and will throw the exception if
	 * that test fails.
	 * <br>
	 * @param pL ComplexD[]
	 * @throws FieldBinaryException This exception happens when there is a field
	 *                              mismatch. It shouldn't happen but it is
	 *                              technically possible because of the dependence
	 *                              on sqMagnitude.
	 * @return ComplexD
	 */
	public final static ComplexD copySumModulus(ComplexD[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form Modulus Sum from empty array.");
		ComplexD tR = ComplexD.copyZERO(pL[0]);
		for (ComplexD point : pL)
			tR.add((ComplexD.copyONE(point).scale(point.modulus())));
		tR.setReal(Math.sqrt(tR.getReal()));
		return tR;
	}

	/**
	 * This static method takes a list of RealD objects and returns one RealD that
	 * has a value that is equal to the sum of the SQModulus of each entry on the
	 * list. It does not perform a cardinal safety check and will throw the
	 * exception if that test fails.
	 * <br>
	 * @param pL ComplexD[]
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch. It should never happen but the
	 *                              implementation uses multiplication, thus it is
	 *                              technically possible.
	 * @return ComplexD
	 */
	public final static ComplexD copySumSQModulus(ComplexD[] pL) throws FieldBinaryException {
		if (pL.length == 0) throw new IllegalArgumentException("Can't form SQ Modulus Sum from empty array.");
		ComplexD tR = ComplexD.copyZERO(pL[0]);
		for (ComplexD point : pL)
			tR.add((ComplexD.copyONE(point).scale(point.sqModulus())));
		return tR;
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the parameter. This
	 * copy reuses the cardinal reference to ensure it will pass a type match test.
	 * <br>
	 * @param pF ComplexD
	 * @return ComplexD
	 */
	public static ComplexD copyOf(ComplexD pF) {
		return new ComplexD(pF);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <br>
	 * @param pR ComplexD
	 * @return ComplexD
	 */
	public static ComplexD copyONE(UnitAbstract pR) {
		return ComplexD.create(pR.getCardinal(), 1.0d, 0.0d);
	}

	/**
	 * Static zero construction method with copied cardinal
	 * <br>
	 * @param pR ComplexD
	 * @return ComplexD
	 */
	public static ComplexD copyZERO(UnitAbstract pR) {
		return ComplexD.create(pR.getCardinal(), 0.0d, 0.0d);
	}

	/**
	 * Static method that creates a new ComplexD with a copy of the parameter. This
	 * copy does not reuse a cardinal reference so it is likely to fail type
	 * mismatch tests.
	 * <br>
	 * @param pR double
	 * @param pI double
	 * @return ComplexD
	 */
	public static ComplexD create(double pR, double pI) {
		return new ComplexD(pR, pI);
	}

/**
	 * Static method that creates a new ComplexD with doubles and a Cardinal. This
	 * copy DOES reuse the cardinal so it is likely to pass type mismatch tests.
	 * <br>
	 * @param pCard Cardinal
	 * @param pR double
	 * @param pI double
	 * @return ComplexD
	 */
	public static ComplexD create(Cardinal pCard, double pR, double pI) {
		return new ComplexD(pCard, pR, pI);
	}

	/**
	 * Static divide method that creates a new ComplexD with the product pF1 / pF2.
	 * <br>
	 * @param pF1 ComplexD
	 * @param pF2 ComplexD
	 * @throws FieldBinaryException This exception is thrown when there is a field
	 *                              mismatch
	 * @return ComplexD
	 */
	public static ComplexD divide(ComplexD pF1, ComplexD pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexD.isZero(pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2)
				&& !ComplexD.isInfinite(pF1) && !ComplexD.isInfinite(pF2)) {
			ComplexD tZ = ComplexD.copyOf(pF1);
			ComplexD tZ2 = ComplexD.copyOf(pF2);
			tZ2.conjugate();
			tZ.multiply(tZ2);
			tZ2.conjugate();
			tZ.scale(Double.valueOf(1.0D / tZ2.sqModulus()));
			return tZ;
		}
		throw (new FieldBinaryException(pF1, "Static Division error found", pF2));
	}

	/**
	 * Check for the equality of this object with that of the argument. This checks
	 * for exact equality using no tolerances. The FieldObject types must match
	 * first.
	 * <br>
	 * @param pE ComplexD
	 * @param pF ComplexD
	 * @return boolean <i>true</i> if both components are the same; <i>false</i>,
	 *         otherwise.
	 */
	public static boolean isEqual(ComplexD pE, ComplexD pF) {
		return UnitAbstract.isTypeMatch(pE, pF) && (pE.getReal() == pF.getReal()) && (pE.getImg() == pF.getImg());
	}

	/**
	 * Returns true if the real part is zero and imaginary part is not zero.
	 * <br>
	 * @param pF ComplexD
	 * @return boolean
	 */
	public static boolean isImaginary(ComplexD pF) {
		return (pF.getReal() == 0.0d && pF.getImg() != 0.0d);
	}

	/**
	 * This method checks to see if either value is infinite.
	 * <br>
	 * @param pF ComplexDF
	 * @return boolean
	 */
	public static boolean isInfinite(ComplexD pF) {
		return Double.isInfinite(pF.getReal()) || Double.isInfinite(pF.getImg());
	}

	/**
	 * This method checks to see if either value is not a number at all. NAN
	 * <br>
	 * @param pF ComplexD
	 * @return boolean
	 */
	public static boolean isNaN(ComplexD pF) {
		return Double.isNaN(pF.getReal()) || Double.isNaN(pF.getImg());
	}

	/**
	 * Returns true if the imaginary part is zero
	 * <br>
	 * @param pF ComplexD
	 * @return boolean
	 */
	public static boolean isReal(ComplexD pF) {
		return (pF.getImg() == 0.0d);
	}

	/**
	 * This method checks to see if the number is exactly zero.
	 * <br>
	 * @param pF ComplexD
	 * @return boolean
	 */
	public static boolean isZero(ComplexD pF) {
		return (pF.getReal() == 0.0D && pF.getImg() == 0.0D);
	}

	/**
	 * Static multiply method that creates a new ComplexD with product pF1 * pF2.
	 * <br>
	 * @param pF1 ComplexD
	 * @param pF2 ComplexD
	 * @throws FieldBinaryException This exception happens when there is a field
	 *                              mismatch.
	 * @return complexD
	 */
	public static ComplexD multiply(ComplexD pF1, ComplexD pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2) && !ComplexD.isInfinite(pF1)
				&& !ComplexD.isInfinite(pF2)) {
			double tempR = pF1.getReal() * pF2.getReal() - pF1.getImg() * pF2.getImg();
			double tempI = pF1.getReal() * pF2.getImg() + pF1.getImg() * pF2.getReal();
			return ComplexD.create(pF1.getCardinal(), tempR, tempI);
		}
		throw (new FieldBinaryException(pF1, "Static Multiplication error found", pF2));
	}

	/**
	 * Static one construction method
	 * <br>
	 * @param pS String
	 * @return ComplexD
	 */
	public static ComplexD newONE(String pS) {
		return ComplexD.create(Cardinal.generate(pS), 1.0d, 0.0d);
	}

	/**
	 * Static zero construction method
	 * <br>
	 * @param pS String
	 * @return ComplexD
	 */
	public static ComplexD newZERO(String pS) {
		return ComplexD.create(Cardinal.generate(pS), 0.0D, 0.0D);
	}

	/**
	 * Static one construction method
	 * <br>
	 * @param pC Cardinal
	 * @return ComplexD
	 */
	public static ComplexD newONE(Cardinal pC) {
		return ComplexD.create(pC, 1.0d, 0.0d);
	}

	/**
	 * Static zero construction method
	 * <br>
	 * @param pC Cardinal
	 * @return ComplexD
	 */
	public static ComplexD newZERO(Cardinal pC) {
		return ComplexD.create(pC, 0.0D, 0.0D);
	}

	/**
	 * Static subtract method that creates a new ComplexD with the difference
	 * pF1-pF2.
	 * <br>
	 * @param pF1 ComplexD
	 * @param pF2 ComplexD
	 * @throws FieldBinaryException This exception occurs when there is a field
	 *                              mismatch.
	 * @return ComplexD
	 */
	public static ComplexD subtract(ComplexD pF1, ComplexD pF2) throws FieldBinaryException {
		if (UnitAbstract.isTypeMatch(pF1, pF2) && !ComplexD.isNaN(pF1) && !ComplexD.isNaN(pF2) && !ComplexD.isInfinite(pF1)
				&& !ComplexD.isInfinite(pF2))
			return ComplexD.create(pF1.getCardinal(), pF1.getReal() - pF2.getReal(), pF1.getImg() - pF2.getImg());

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
	public ComplexD() {
		super(Cardinal.generate(CladosField.COMPLEXD));
		vals = new double[2];
		setReal(0.0D);
		setImg(0.0D);
	}

	/**
	 * Copy Constructor that reuses the v reference.
	 * <br>
	 * @param pC ComplexD
	 */
	public ComplexD(ComplexD pC) {
		super(pC.getCardinal());
		vals = new double[2];
		setReal(pC.getReal());
		setImg(pC.getImg());
	}

	/**
	 * Copy Constructor that reuses the cardinal reference while allowing the values
	 * to be set.
	 * <br>
	 * @param pC ComplexD
	 * @param pR double
	 * @param pI double
	 */
	public ComplexD(UnitAbstract pC, double pR, double pI) {
		super(pC.getCardinal());
		vals = new double[2];
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the cardinal to initialize.
	 * <br>
	 * @param pT Cardinal
	 */
	public ComplexD(Cardinal pT) {
		super(pT);
		vals = new double[2];
		setReal(0.0D);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize but the imaginary.
	 * <br>
	 * @param pT Cardinal
	 * @param pR double
	 */
	public ComplexD(Cardinal pT, double pR) {
		super(pT);
		vals = new double[2];
		setReal(pR);
		setImg(0.0D);
	}

	/**
	 * Basic Constructor with everything to initialize it.
	 * <br>
	 * @param pT Cardinal
	 * @param pR double
	 * @param pI double
	 */
	public ComplexD(Cardinal pT, double pR, double pI) {
		super(pT);
		vals = new double[2];
		setReal(pR);
		setImg(pI);
	}

	/**
	 * Basic Constructor with only the number to initialize.
	 * <br>
	 * @param pR double
	 * @param pI double
	 */
	public ComplexD(double pR, double pI) {
		super(Cardinal.generate(CladosField.COMPLEXD));
		vals = new double[2];
		setReal(pR);
		setImg(pI);
	}

	/**
	 * This method adds real numbers together and changes this object to be the
	 * result.
	 * <br>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when a field mismatch
	 *                              happens
	 * @return ComplexF
	 */
	@Override
	public ComplexD add(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexD.isNaN(this) || ComplexD.isNaN((ComplexD) pF)
				|| ComplexD.isInfinite(this) || ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Addition failed type match test", (UnitAbstract) pF));
		setReal(getReal() + ((ComplexD) pF).getReal());
		setImg(getImg() + ((ComplexD) pF).getImg());
		return this;
	}

	/**
	 * This is the self-altering conjugate method. This object changes when all of
	 * its imaginary members are set to their additive inverses.
	 * <br>
	 * @return ComplexD
	 */
	@Override
	public ComplexD conjugate() {
		setImg(-1.0D * getImg());
		return this;
	}

	/**
	 * This method divides real numbers and changes this object to be the result.
	 * <br>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches or
	 *                              division by zero happens
	 * @return ComplexD
	 */
	@Override
	public ComplexD divide(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexD.isNaN(this) || ComplexD.isNaN((ComplexD) pF)
				|| ComplexD.isInfinite(this) || ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Divide failed type match or size test", (UnitAbstract) pF));
		if (ComplexD.isZero((ComplexD) pF))
			throw (new FieldBinaryException(this, "Divide by Zero detected", (UnitAbstract) pF));

		ComplexD tempZ = ComplexD.copyOf((ComplexD) pF);
		tempZ.conjugate();
		multiply(tempZ);
		tempZ.conjugate();
		scale(Double.valueOf(1.0D / tempZ.sqModulus()));
		return this;
	}

	/**
	 * Get method for the argument of the complex number. This function uses the
	 * arctangent function, so its range and domain are the same.
	 * <br>
	 * @return double
	 */
	public double getArgument() {
		if (!ComplexD.isImaginary(this))
			return Math.atan(getImg() / getReal());

		return (getImg() >= 0) ? Math.PI / 2.0D : 3.0D * Math.PI / 2.0D;
	}

	/**
	 * Get the imaginary numeric value from the value array
	 * <br>
	 * @return double
	 */
	public double getImg() {
		return vals[1];
	}

	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * <br>
	 * @return Double
	 */
	@Override
	public Double modulus() {
		return Double.valueOf(Math.sqrt(sqModulus()));
	}

	/**
	 * Get the real numeric value from the value array
	 * <br>
	 * @return double
	 */
	public double getReal() {
		return vals[0];
	}

	/**
	 * This function delivers the sum of the squares of the numeric values. (x^2+y^2)
	 * Many times it is the modulus squared that is actually needed so it makes 
	 * sense to calculate this before the modulus itself.
	 * <br>
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
	 * <br>
	 * @throws FieldException This exception is thrown when someone tries to invert
	 *                        ZERO.
	 * @return ComplexD
	 */
	@Override
	public ComplexD invert() throws FieldException {
		if (ComplexD.isZero(this))
			throw new FieldException(this, "Can't invert a zero ComplexD");

		double tM = 1.0 / modulus();
		double tA = -1.0 * getArgument();
		setReal(tM * Math.cos(tA));
		setImg(tM * Math.sin(tA));
		return this;
	}

	/**
	 * This method multiplies real numbers and changes this object to be the result.
	 * <br>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * @return ComplexD
	 */
	@Override
	public ComplexD multiply(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexD.isNaN(this) || ComplexD.isNaN((ComplexD) pF)
				|| ComplexD.isInfinite(this) || ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Multiply failed type match test", (UnitAbstract) pF));
		ComplexD tempZ = (ComplexD) pF;
		setReal(getReal() * tempZ.getReal() - getImg() * tempZ.getImg());
		setImg(getReal() * tempZ.getImg() + getImg() * tempZ.getReal());
		return this;
	}

	/**
	 * Scale method multiplies the modulus by the scale
	 * <br>
	 * @param pS Number
	 * @return ComplexD
	 */
	@Override
	public ComplexD scale(Number pS) {
		setReal(pS.doubleValue() * getReal());
		setImg(pS.doubleValue() * getImg());
		return this;
	}

	/**
	 * Set the imaginary numeric value
	 * <br>
	 * @param pimg double
	 */
	public void setImg(double pimg) {
		vals[1] = pimg;
	}

	/**
	 * Set the real numeric value
	 * <br>
	 * @param preal double
	 */
	public void setReal(double preal) {
		vals[0] = preal;
	}

	/**
	 * This method subtracts real numbers and changes this object to be the result.
	 * <br>
	 * @param pF Field
	 * @throws FieldBinaryException This exception occurs when field mismatches
	 *                              happen
	 * @return ComplexD
	 */
	@Override
	public ComplexD subtract(Field pF) throws FieldBinaryException {
		if (!UnitAbstract.isTypeMatch(this, (UnitAbstract) pF) || ComplexD.isNaN(this) || ComplexD.isNaN((ComplexD) pF)
				|| ComplexD.isInfinite(this) || ComplexD.isInfinite((ComplexD) pF))
			throw (new FieldBinaryException(this, "Subtraction failed type match or size test", (UnitAbstract) pF));
		
		setReal(getReal() - ((ComplexD) pF).getReal());
		setImg(getImg() - ((ComplexD) pF).getImg());
		return this;
	}

	/**
	 * Return a string representation of the real value.
	 * <br>
	 * @return String
	 */
	@Override
	public String toString() {
		return (getReal() + "DR, " + getImg() + "DI");
	}

	/**
	 * Return a string representation of the real value.
	 * <br>
	 * @return String
	 */
	@Override
	public String toXMLString() {
		return ("<ComplexD cardinal=\"" + getCardinalString() + "\" realvalue=\"" + getReal() + "\" imgvalue=\""
				+ getImg() + "\"/>");
	}
}