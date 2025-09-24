/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosField<br>
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
 * ---org.interworldtransport.cladosF.CladosField<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * DivFields currently come in four varieties. RealF, RealD, ComplexF, ComplexD
 * To facilitate a Builder class we would give basic information and construct
 * any of them. This would be supported by an enumeration type that can't be
 * UnitAbstract itself because UnitAbstract is subclassed to make the four field
 * classes.
 * <br>
 * This enum doubles up a bit as a builder. Each of its instances rely on the
 * shared static methods and have a few instance methods of their own that
 * 'switch' on their identity to determine what gets built and returned.
 * <br>
 * This enumeration has non-static methods for each instance, but they don't
 * cause a state change. CladosField HAS NO INTERNAL STATE to change unlike the
 * Builder in the same package.
 * <br>
 * NOTE that the methods capable of creating Cardinals in this enumeration do
 * NOT cache them. Similar methods in CladosFBuilder DO cache them.
 * <br>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosField {
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	REALF,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	REALD,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	COMPLEXF,
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	COMPLEXD;

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using the CladosField hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pField CladosField enumeration to be used as description of UnitAbstract
	 *               child to be created.
	 * @param pCard  Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final static UnitAbstract createONE(CladosField pField, Cardinal pCard) {
		switch (pField) {
		case REALF:
			return new RealF(pCard, 1f);
		case REALD:
			return new RealD(pCard, 1d);
		case COMPLEXF:
			return new ComplexF(pCard, 1f, 0f);
		case COMPLEXD:
			return new ComplexD(pCard, 1d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using the CladosField hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pField CladosField enumeration to be used as description of UnitAbstract
	 *               child to be created.
	 * @param pCard  Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final static UnitAbstract createZERO(CladosField pField, Cardinal pCard) {
		switch (pField) {
		case REALF:
			return new RealF(pCard, 0f);
		case REALD:
			return new RealD(pCard, 0d);
		case COMPLEXF:
			return new ComplexF(pCard, 0f, 0f);
		case COMPLEXD:
			return new ComplexD(pCard, 0d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createONE(Cardinal pCard) {
		switch (this) {
		case REALF:
			return new RealF(pCard, 1f);
		case REALD:
			return new RealD(pCard, 1d);
		case COMPLEXF:
			return new ComplexF(pCard, 1f, 0f);
		case COMPLEXD:
			return new ComplexD(pCard, 1d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pDiv Source of the Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createONE(UnitAbstract pDiv) {
		switch (this) {
		case REALF:
			return new RealF(pDiv.getCardinal(), 1f);
		case REALD:
			return new RealD(pDiv.getCardinal(), 1d);
		case COMPLEXF:
			return new ComplexF(pDiv.getCardinal(), 1f, 0f);
		case COMPLEXD:
			return new ComplexD(pDiv.getCardinal(), 1d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pCard String name of the Cardinal to be created.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createONE(String pCard) {
		switch (this) {
		case REALF:
			return new RealF(Cardinal.generate(pCard), 1f);
		case REALD:
			return new RealD(Cardinal.generate(pCard), 1d);
		case COMPLEXF:
			return new ComplexF(Cardinal.generate(pCard), 1f, 0f);
		case COMPLEXD:
			return new ComplexD(Cardinal.generate(pCard), 1d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createZERO(Cardinal pCard) {
		switch (this) {
		case REALF:
			return new RealF(pCard, 0f);
		case REALD:
			return new RealD(pCard, 0d);
		case COMPLEXF:
			return new ComplexF(pCard, 0f, 0f);
		case COMPLEXD:
			return new ComplexD(pCard, 0d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pDiv Source of the Cardinal to be re-used.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createZERO(UnitAbstract pDiv) {
		switch (this) {
		case REALF:
			return new RealF(pDiv.getCardinal(), 0f);
		case REALD:
			return new RealD(pDiv.getCardinal(), 0d);
		case COMPLEXF:
			return new ComplexF(pDiv.getCardinal(), 0f, 0f);
		case COMPLEXD:
			return new ComplexD(pDiv.getCardinal(), 0d, 0d);
		default:
			return null;
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular UnitAbstract
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pCard String name of the Cardinal to be created.
	 * @return UnitAbstract Newly constructed 'zero' number returned as a UnitAbstract, but
	 *         it will always be one of the UnitAbstract children.
	 */
	public final UnitAbstract createZERO(String pCard) {
		switch (this) {
		case REALF:
			return new RealF(Cardinal.generate(pCard), 0f);
		case REALD:
			return new RealD(Cardinal.generate(pCard), 0d);
		case COMPLEXF:
			return new ComplexF(Cardinal.generate(pCard), 0f, 0f);
		case COMPLEXD:
			return new ComplexD(Cardinal.generate(pCard), 0d, 0d);
		default:
			return null;
		}
	}
}
