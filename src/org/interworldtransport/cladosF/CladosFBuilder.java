/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
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
 * ---org.interworldtransport.cladosF.CladosFBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.Optional;

/**
 * This builder gets basic information and constructs any of the DivFields and
 * their supporting classes like a Cardinal. Some features are supported by the
 * CladosField enumeration.
 * 
 * The UnitAbstract builder is a singleton enforced as an enumeration.
 * 
 * This enumeration has NO non-static element for the instance, thus
 * CladosFBuilder HAS NO INTERNAL STATE that can change.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFBuilder {
	/**
	 * The implicit private constructor IS NOT overridden.
	 */
	COMPLEXD,
	/**
	 * The implicit private constructor IS NOT overridden.
	 */
	COMPLEXF,
	/**
	 * The implicit private constructor IS NOT overridden.
	 */
	REALD,
	/**
	 * The implicit private constructor IS NOT overridden.
	 */
	REALF;
	/**
	 * The implicit private constructor IS NOT overridden.
	 */
	//DIVFIELD;

	/**
	 * Method creates a new Cardinal using the string provided IF one by that name
	 * is not present in the cache. If it IS in the cache, the cached Cardinal is
	 * returned instead.
	 * 
	 * NOTE that Cardinal has a static method that does something similar. The
	 * difference is this one also caches the cardinal.
	 * 
	 * @param pName String name for the associated Cardinal
	 * @return Cardinal unit cardinal created or retrieved
	 */
	public final static Cardinal createCardinal(String pName) {
		Optional<Cardinal> test = CladosFCache.INSTANCE.findCardinal(pName);
		if (test.isEmpty()) {
			test = Optional.ofNullable(Cardinal.generate(pName));
			CladosFCache.INSTANCE.appendCardinal(test.get());
		}
		return test.get();
	}
	

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define and cache a Cardinal.
	 * 
	 * @param pMode 
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static UnitAbstract createZERO(CladosField pMode, Cardinal pCard) {
		switch (pMode) {
		case REALF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.REALF.createZERO(pCard);
		}
		case REALD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.REALD.createZERO(pCard);
		}
		case COMPLEXF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.COMPLEXF.createZERO(pCard);
		}
		case COMPLEXD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.COMPLEXD.createZERO(pCard);
		}
		default -> {
			return null;
		}
		}
	}
	
	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define and cache a Cardinal.
	 * 
	 * @param pMode 
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static UnitAbstract createONE(CladosField pMode, Cardinal pCard) {
		switch (pMode) {
		case REALF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.REALF.createONE(pCard);
		}
		case REALD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.REALD.createONE(pCard);
		}
		case COMPLEXF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.COMPLEXF.createONE(pCard);
		}
		case COMPLEXD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return CladosFBuilder.COMPLEXD.createONE(pCard);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * NOTE this one makes no attempt to update the cardinal cache. It is assumed to
	 * have been done while constructing the number passed in as a parameter.
	 * 
	 * @param pDiv A UnitAbstract child number to be copied
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends UnitAbstract & Field> T copyOf(T pDiv) {
		if (pDiv instanceof RealF) {
			return (T) new RealF(pDiv.getCardinal(), ((RealF) pDiv).getReal());
		} else if (pDiv instanceof RealD) {
			return (T) new RealD(pDiv.getCardinal(), ((RealD) pDiv).getReal());
		} else if (pDiv instanceof ComplexF) {
			return (T) new ComplexF(pDiv.getCardinal(), ((ComplexF) pDiv).getReal(), ((ComplexF) pDiv).getImg());
		} else if (pDiv instanceof ComplexD) {
			return (T) new ComplexD(pDiv.getCardinal(), ((ComplexD) pDiv).getReal(), ((ComplexD) pDiv).getImg());
		} else {
			return null;
		}
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * string provided to define and cache a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return UnitAbstract child number created
	 */
	public UnitAbstract createONE(String pS) {
		switch (this) {
		case REALF -> {
			Cardinal toCache = createCardinal(pS);
			return RealF.newONE(toCache);
		}
		case REALD -> {
			Cardinal toCache = createCardinal(pS);
			return RealD.newONE(toCache);
		}
		case COMPLEXF -> {
			Cardinal toCache = createCardinal(pS);
			return ComplexF.newONE(toCache);
		}
		case COMPLEXD -> {
			Cardinal toCache = createCardinal(pS);
			return ComplexD.newONE(toCache);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * offered Cardinal.
	 * 
	 * @param pCard Cardinal to use in construction
	 * @return UnitAbstract child number created
	 */
	public UnitAbstract createONE(Cardinal pCard) {
		switch (this) {
		case REALF -> {
			return RealF.newONE(pCard);
		}
		case REALD -> {
			return RealD.newONE(pCard);
		}
		case COMPLEXF -> {
			return ComplexF.newONE(pCard);
		}
		case COMPLEXD -> {
			return ComplexD.newONE(pCard);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a number as distinct ZERO object using default cardinal name.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public UnitAbstract createZERO() {
		switch (this) {
		case REALF -> {
			Cardinal toCache = createCardinal(CladosField.REALF.name());
			return RealF.newZERO(toCache);
		}
		case REALD -> {
			Cardinal toCache = createCardinal(CladosField.REALD.name());
			return RealD.newZERO(toCache);
		}
		case COMPLEXF -> {
			Cardinal toCache = createCardinal(CladosField.COMPLEXF.name());
			return ComplexF.newZERO(toCache);
		}
		case COMPLEXD -> {
			Cardinal toCache = createCardinal(CladosField.COMPLEXD.name());
			return ComplexD.newZERO(toCache);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define and cache a Cardinal.
	 * 
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public UnitAbstract createZERO(Cardinal pCard) {
		switch (this) {
		case REALF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return RealF.newZERO(pCard);
		}
		case REALD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return RealD.newZERO(pCard);
		}
		case COMPLEXF -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return ComplexF.newZERO(pCard);
		}
		case COMPLEXD -> {
			CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
			return ComplexD.newZERO(pCard);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define and cache a Cardinal.
	 * 
	 * @param pS String name for the associated Cardinal
	 * @return UnitAbstract child number created
	 */
	public <T extends UnitAbstract & Field> UnitAbstract createZERO(String pS) {
		switch (this) {
		case REALF -> {
			Cardinal toCache = createCardinal(pS);
			return RealF.newZERO(toCache);
		}
		case REALD -> {
			Cardinal toCache = createCardinal(pS);
			return RealD.newZERO(toCache);
		}
		case COMPLEXF -> {
			Cardinal toCache = createCardinal(pS);
			return ComplexF.newZERO(toCache);
		}
		case COMPLEXD -> {
			Cardinal toCache = createCardinal(pS);
			return ComplexD.newZERO(toCache);
		}
		default -> {
			return null;
		}
		}
	}
}
