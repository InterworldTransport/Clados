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
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosField<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * ProtoN currently come in four varieties. RealF, RealD, ComplexF, ComplexD. This enumeration
 * ensures other classes can know what ProtoN children are supported. Adjustments here
 * (for example... adding in quaternions) will cause all dependent classes to complain at 
 * compile time that their switch statements are incomplete. This class also facilitates clados 
 * builder classes in a way that ProtoN children can't do directly. ProtoN children are subclassed 
 * while builders are also enumerations.
 * <br><br>
 * This enumeration also doubles up as a small builder. Each of its instances can call methods 
 * for creating ones and zeroes of the right ProtoN child type with the offered input. 
 * They 'switch' on their identity to determine what gets returned. While these ARE instance
 * methods, the instances themselves have NO INTERNAL STATE to change unlike some of the other
 * clados builders.
 * <br><br>
 * NOTE the methods capable of creating Cardinals DO CACHE THEM.
 * <br><br>
 * @version 2.0
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
	COMPLEXD//,
	/**
	 * This is a test for extending the enumeration. Curious to see what complains. 8)
	 */
	//NADA
	;

	/**
	 * Method re-uses the incoming cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pCard Cardinal to be re-used.
	 * @return D child of ProtoN newly constructed 'one'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createONE(Cardinal pCard) {
		switch (this) {
		case REALF:
			return (D) new RealF(pCard, 1f);
		case REALD:
			return (D) new RealD(pCard, 1d);
		case COMPLEXF:
			return (D) new ComplexF(pCard, 1f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(pCard, 1d, 0d);
		default:
			return (D) new ProtoN(pCard);
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pNumber Source of the Cardinal to be re-used.
	 * @return D child of ProtoN newly constructed 'one'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createONE(ProtoN pNumber) {
		switch (this) {
		case REALF:
			return (D) new RealF(pNumber.getCardinal(), 1f);
		case REALD:
			return (D) new RealD(pNumber.getCardinal(), 1d);
		case COMPLEXF:
			return (D) new ComplexF(pNumber.getCardinal(), 1f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(pNumber.getCardinal(), 1d, 0d);
		default:
			return (D) new ProtoN(pNumber.getCardinal());
		}
	}

	/**
	 * Method constructs a cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ONE.
	 * <br>
	 * @param pCard String name of the Cardinal to be created.
	 * @return D child of ProtoN newly constructed 'one'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createONE(String pCard) {
		switch (this) {
		case REALF:
			return (D) new RealF(Cardinal.generate(pCard), 1f);
		case REALD:
			return (D) new RealD(Cardinal.generate(pCard), 1d);
		case COMPLEXF:
			return (D) new ComplexF(Cardinal.generate(pCard), 1f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(Cardinal.generate(pCard), 1d, 0d);
		default:
			return (D) new ProtoN(Cardinal.generate(pCard));
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pCard Cardinal to be re-used.
	 * @return D child of ProtoN newly constructed 'zero'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createZERO(Cardinal pCard) {
		switch (this) {
		case REALF:
			return (D) new RealF(pCard, 0f);
		case REALD:
			return (D) new RealD(pCard, 0d);
		case COMPLEXF:
			return (D) new ComplexF(pCard, 0f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(pCard, 0d, 0d);
		default:
			return (D) new ProtoN(pCard);
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pDiv Source of the Cardinal to be re-used.
	 * @return D child of ProtoN newly constructed 'zero'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createZERO(ProtoN pDiv) {
		switch (this) {
		case REALF:
			return (D) new RealF(pDiv.getCardinal(), 0f);
		case REALD:
			return (D) new RealD(pDiv.getCardinal(), 0d);
		case COMPLEXF:
			return (D) new ComplexF(pDiv.getCardinal(), 0f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(pDiv.getCardinal(), 0d, 0d);
		default:
			return (D) new ProtoN(pDiv.getCardinal());
		}
	}

	/**
	 * Method re-uses the incoming cardinal and constructs a particular ProtoN
	 * child object using this CladosField enumeration as the hint.
	 * <br>
	 * Number created has a real value of ZERO.
	 * <br>
	 * @param pCard String name of the Cardinal to be created.
	 * @return D child of ProtoN newly constructed 'zero'.
	 */
	public final <D extends ProtoN & Field & Normalizable> D createZERO(String pCard) {
		switch (this) {
		case REALF:
			return (D) new RealF(Cardinal.generate(pCard), 0f);
		case REALD:
			return (D) new RealD(Cardinal.generate(pCard), 0d);
		case COMPLEXF:
			return (D) new ComplexF(Cardinal.generate(pCard), 0f, 0f);
		case COMPLEXD:
			return (D) new ComplexD(Cardinal.generate(pCard), 0d, 0d);
		default:
			return (D) new ProtoN(Cardinal.generate(pCard));
		}
	}
}