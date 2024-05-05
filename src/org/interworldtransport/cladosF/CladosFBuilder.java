/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
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
 * This builder gets basic information and constructs any of the children of 
 * UnitAbstract and the supporting classes like a Cardinal. Some features are 
 * supported by the CladosField enumeration.
 * <p>
 * This class has ONLY STATIC features hooked to each of the enumerations, thus
 * NO INTERNAL STATE to can change. This trick is how we get different responses
 * from the builder because there are as many of them as there are enumerated 
 * entries without anything having to be instantiated to do it.
 * <p>
 * This builder currently comes in four flavors. If we ever get around to 
 * including quaternions as descendents of UnitAbstract, this enumeration must 
 * be expanded to include them.
 * <p>
 * @version 2.0
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
	 * Method creates a new Cardinal using the string provided IF one by that name
	 * is not present in the cache. If it IS in the cache, the cached Cardinal is
	 * returned instead.
	 * <p>
	 * NOTE that Cardinal has a static method that does something similar. The
	 * difference is this one also caches the cardinal.
	 * <p>
	 * Nothing about this method relies on the mode of the builder because Cardinals
	 * aren't aware of UnitAbstract children.
	 * <p>
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
	 * cardinal provided. Its way of doing this switches on the offered CladosField 
	 * instead of using this builder's mode.
	 * <p>
	 * Nothing about this method relies on the mode of the builder. 
	 * <p>
	 * @param pMode CladosField mode to use when creating UnitAbstract numbers
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static UnitAbstract createZERO(CladosField pMode, Cardinal pCard) {
		CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
		switch (pMode) {
		case REALF -> {
			return CladosFBuilder.REALF.createZERO(pCard);
		}
		case REALD -> {
			return CladosFBuilder.REALD.createZERO(pCard);
		}
		case COMPLEXF -> {
			return CladosFBuilder.COMPLEXF.createZERO(pCard);
		}
		case COMPLEXD -> {
			return CladosFBuilder.COMPLEXD.createZERO(pCard);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * cardinal provided. Its way of doing this switches on the offered CladosField 
	 * instead of using this builder's mode.
	 * <p>
	 * Nothing about this method relies on the mode of the builder.
	 * <p>
	 * @param pMode CladosField mode to use when creating UnitAbstract numbers
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static UnitAbstract createONE(CladosField pMode, Cardinal pCard) {
		CladosFCache.INSTANCE.appendCardinal(pCard); // just in case.
		switch (pMode) {
		case REALF -> {
			return CladosFBuilder.REALF.createONE(pCard);
		}
		case REALD -> {
			return CladosFBuilder.REALD.createONE(pCard);
		}
		case COMPLEXF -> {
			return CladosFBuilder.COMPLEXF.createONE(pCard);
		}
		case COMPLEXD -> {
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
	 * <p>
	 * NOTE this one makes no attempt to update the cardinal cache. It is assumed to
	 * have been done while constructing the number passed in as a parameter.
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
	 * Nothing about this method relies on the mode of the builder.
	 * <p>
	 * @param pDiv A UnitAbstract child number to be copied
	 * @param <T>  UnitAbstract child number with the Field and Normalizable
	 *             interfaces too.
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends UnitAbstract & Field & Normalizable> T copyOf(T pDiv) {
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
	 * <p>
	 * This method relies on the mode of the builder called to create the number.
	 * <p>
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
	 * <p>
	 * This method relies on the mode of the builder called to create the number.
	 * <p>
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
	 * <p>
	 * This method relies on the mode of the builder called to create the number.
	 * <p>
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
	 * <p>
	 * This method relies on the mode of the builder called to create the number.
	 * <p>
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
	 * <p>
	 * This method relies on the mode of the builder called to create the number.
	 * <p>
	 * @param pS  String name for the associated Cardinal
	 * @param <T> UnitAbstract child number with the Field interface too.
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
