/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.UnitAbstract<br>
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
 * ---org.interworldtransport.cladosF.UnitAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.Optional;

/**
 * This class supports the concept of a Division Field from mathematics. Field
 * objects within the clados packages are used as 'numbers' in the definition of
 * an algebra. All Clados objects use UnitAbstract descendants as a result.
 * <p>
 * UnitAbstract's capture only a reference frame type, so they appear to be named.
 * However, they are named with Cardinal objects so their names can be shared as
 * references, thus survive reference frame match tests.
 * <p>
 * The number to be plugged in, though, doesn't appear until later in a child of
 * this class. This matters because the number of reals involved in the field
 * varies. Complex numbers require two. Quaternions would require four.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class UnitAbstract {
	/**
	 * Static method that creates a new CladosF number with a copy of the parameter.
	 * This copy reuses the cardinal to ensure it will pass a type match test.
	 * <p>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <p>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <p>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <p>
	 * @param <D> UnitAbstract number from CladosF with all number interfaces.
	 * @param pF D extends UnitAbstract and Field
	 * @return Optional D which extends UnitAbstract and Field (A CladosF number)
	 */
	@SuppressWarnings("unchecked")
	public static final <D extends UnitAbstract & Field & Normalizable> Optional<D> copyMaybe(D pF) {
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
	 * <p>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <p>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <p>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <p>
	 * @param <D> UnitAbstract number from CladosF with all number interfaces.
	 * @param pR D extends UnitAbstract and Field
	 * @return D extends UnitAbstract and Field
	 */
	@SuppressWarnings("unchecked")
	public final static <D extends UnitAbstract & Field & Normalizable> Optional<D> copyMaybeONE(D pR) {
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
	 * <p>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <p>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <p>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <p>
	 * @param pR D extends UnitAbstract and Field
	 * @param <D> UnitAbstract number from CladosF with all number interfaces.
	 * @return D extends UnitAbstract and Field
	 */
	@SuppressWarnings("unchecked")
	public final static <D extends UnitAbstract & Field & Normalizable> Optional<D> copyMaybeZERO(D pR) {
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
	 * <p>
	 * @param pE UnitAbstract
	 * @param pF UnitAbstract
	 * @return boolean
	 */
	public static final boolean isTypeMatch(UnitAbstract pE, UnitAbstract pF) {
		if (pE._card == null && pF._card == null)
			return true;
		return pE._card.getUnit() == pF._card.getUnit();
	}

	/**
	 * Object for the cardinal. A string used to be used here, but an object lets us
	 * reuse the object through a reference allowing all coefficients in the monads
	 * of a nyad to point to the same place.
	 */
	protected Cardinal _card;

	/**
	 * Construct a simple UnitAbstract using the Cardinal offered.
	 * <p>
	 * @param pCard Cardinal to re-use.
	 */
	public UnitAbstract(Cardinal pCard) {
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
		UnitAbstract other = (UnitAbstract) obj;
		if (_card == null) {
			if (other._card != null)
				return false;
		} else if (!_card.equals(other._card))
			return false;
		return true;
	}

	/**
	 * Get method for _card
	 * <p>
	 * @return Cardinal (A cardinal name for a UnitAbstract)
	 */
	public Cardinal getCardinal() {
		return _card;
	}

	/**
	 * Get method for _card
	 * <p>
	 * @return Cardinal (A cardinal name for a UnitAbstract)
	 */
	public String getCardinalString() {
		return _card.getUnit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_card == null) ? 0 : _card.hashCode());
		return result;
	}

	/**
	 * Return a string representation of the field element.
	 * <p>
	 * @return String Flat string representation of this Field float(s) field
	 */
	public String toXMLString() {
		return ("<UnitAbstract cardinal=\"" + getCardinalString() + "\" />");
	}
	
	/**
	 * Set method for _card
	 * <p>
	 * @param pType Cardinal
	 */
	protected void setCardinal(Cardinal pType) {
		_card = pType;
	}
}
