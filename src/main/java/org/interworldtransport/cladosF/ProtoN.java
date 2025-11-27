/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ProtoN<br>
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
 * ---org.interworldtransport.cladosF.ProtoN<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.Optional;

/**
 * ProtoN is short for 'proto number'. 
 * This is the parent class supporting the notion of a division field
 * from mathematics. Division fields are used as 'numbers' to scale blades 
 * in an algebra, but they are better known as scalars in the sense of 
 * linear combinations in vector spaces.
 * <br><br>
 * ProtoN handles unit references common to all its children. When you imagine
 * the difference between five pigs and five meters you have the role played 
 * by ProtoN's cardinal element. It says what a number is without saying
 * how much of it there is.
 * <br><br>
 * ProtoN also implements comparisons that can be overridden if deeper
 * comparisons are necessary. For example, equality tests at this level only 
 * test high level references to object and cardinal equality. This is just
 * enough for unit type matching, but not quantity matching.
 * <br><br>
 * The number(s) that make up quantity don't appear except in a child of
 * this class. The number and precision of reals for a division field varies. 
 * Complex numbers require two. Quaternions require four. 
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class ProtoN {
	/**
	 * Static method that creates a new CladosF number with a copy of the parameter.
	 * This copy reuses the cardinal to ensure it will pass a type match test.
	 * <br>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of ProtoN. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of ProtoN implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br>
	 * This can happen if one extends ProtoN creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br>
	 * @param <D> ProtoN number from CladosF with all number interfaces.
	 * @param pF D extends ProtoN and Field
	 * @return Optional D which extends ProtoN and Field (A CladosF number)
	 */
	@SuppressWarnings("unchecked")
	public static final <D extends ProtoN & Field & Normalizable> Optional<D> copyMaybe(D pF) {
		if (pF instanceof RealF)
			return (Optional<D>) Optional.ofNullable(new RealF((RealF) pF));
		else if (pF instanceof RealD)
			return (Optional<D>) Optional.ofNullable(new RealD((RealD) pF));
		else if (pF instanceof ComplexF)
			return (Optional<D>) Optional.ofNullable(new ComplexF((ComplexF) pF));
		else if (pF instanceof ComplexD)
			return (Optional<D>) Optional.ofNullable(new ComplexD((ComplexD) pF));
		else
			return Optional.empty();
	}
	

	/**
	 * Static zero construction method with copied cardinal and real part set to ONE.
	 * <br>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of ProtoN. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of ProtoN implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br>
	 * This can happen if one extends ProtoN creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br>
	 * @param <D> ProtoN number from CladosF with all number interfaces.
	 * @param pR D extends ProtoN and Field
	 * @return D extends ProtoN and Field
	 */
	@SuppressWarnings("unchecked")
	public final static <D extends ProtoN & Field & Normalizable> Optional<D> copyMaybeONE(D pR) {
		if (pR instanceof RealF)
			return (Optional<D>) Optional.ofNullable(new RealF(pR.getCardinal(), 1.0f));
		else if (pR instanceof RealD)
			return (Optional<D>) Optional.ofNullable(new RealD(pR.getCardinal(), 1.0d));
		else if (pR instanceof ComplexF)
			return (Optional<D>) Optional.ofNullable(new ComplexF(pR.getCardinal(), 1.0f, 0.0f));
		else if (pR instanceof ComplexD)
			return (Optional<D>) Optional.ofNullable(new ComplexD(pR.getCardinal(), 1.0d, 0.0d));
		else
			return Optional.empty();
	}
	
	/**
	 * Static zero construction method with copied cardinal and real part set to ONE.
	 * <br>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of ProtoN. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of ProtoN implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br>
	 * This can happen if one extends ProtoN creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br>
	 * @param pR D extends ProtoN and Field
	 * @param <D> ProtoN number from CladosF with all number interfaces.
	 * @return D extends ProtoN and Field
	 */
	@SuppressWarnings("unchecked")
	public final static <D extends ProtoN & Field & Normalizable> Optional<D> copyMaybeZERO(D pR) {
		if (pR instanceof RealF)
			return (Optional<D>) Optional.ofNullable(new RealF(pR.getCardinal(), 0.0f));
		else if (pR instanceof RealD)
			return (Optional<D>) Optional.ofNullable(new RealD(pR.getCardinal(), 0.0d));
		else if (pR instanceof ComplexF)
			return (Optional<D>) Optional.ofNullable(new ComplexF(pR.getCardinal(), 0.0f, 0.0f));
		else if (pR instanceof ComplexD)
			return (Optional<D>) Optional.ofNullable(new ComplexD(pR.getCardinal(), 0.0d, 0.0d));
		else
			return Optional.empty();
	}
	
	/**
	 * Check to see if the two argument are of the same cardinal.
	 * <br>
	 * @param pE ProtoN
	 * @param pF ProtoN
	 * @return boolean
	 */
	public static final boolean isTypeMatch(ProtoN pE, ProtoN pF) {
		if (pE.card == null && pF.card == null)
			return true;
		if (pE.card != null && pF.card == null)
			return false;
		if (pE.card == null && pF.card != null)
			return false;
		return pE.card.getUnit() == pF.card.getUnit();
	}

	/**
	 * Object for the cardinal. A string used to be used here, but a Cardinal 
	 * object enables reuse through reference allowing all coefficients in monads
	 * to point to the same place.
	 * <br>
	 * This Cardinal is what gives a proto number a sense of what the number means.
	 */
	protected Cardinal card;

	/**
	 * Construct a simple ProtoN using the Cardinal offered.
	 * <br>
	 * @param pCard Cardinal to re-use.
	 */
	public ProtoN(Cardinal pCard) {
		setCardinal(pCard);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtoN other = (ProtoN) obj;
		if (this.getCardinal() == null) {
			if (other.getCardinal() == null)
				return true;
			else 
				return false;
		}
		else { 
			if (other.getCardinal() == null)
			return false;
		}
		return getCardinal().equals(other.getCardinal());
		
		
	}

	/**
	 * Get method for _card
	 * <br>
	 * @return Cardinal (A cardinal name for a ProtoN)
	 */
	public Cardinal getCardinal() {
		return card;
	}

	/**
	 * Get method for _card
	 * <br>
	 * @return Cardinal (A cardinal name for a ProtoN)
	 */
	public String getCardinalString() {
		return card.getUnit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCardinal() == null) ? 0 : getCardinal().hashCode());
		return result;
	}

	/**
	 * Return a string representation of the field element.
	 * <br>
	 * @param pA ProtoN to be exported as XML... which just means the Cardinal right now.
	 * @return String Flat string representation of this Field float(s) field
	 */
	public static String toXMLString(ProtoN pA) {
		return ("<ProtoN cardinal=\"" + pA.getCardinalString() + "\" />");
	}
	
	/**
	 * Set method for the cardinal. Mess with this at your peril. Seriously. Altering
	 * it after construction leads to a redefining of what the number means and in 
	 * multivectors it HAS to be done consistently or you'll get reference match failures.
	 * <br><br>
	 * @param pType Cardinal
	 */
	public void setCardinal(Cardinal pType) {
		card = pType;
	}
}
