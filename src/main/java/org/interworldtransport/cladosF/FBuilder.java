/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.FBuilder<br>
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
 * ---org.interworldtransport.cladosF.FBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.Optional;

/**
 * This builder gets basic information and constructs any of the children of 
 * UnitAbstract and the supporting classes like a Cardinal. Some features are 
 * supported by the CladosField enumeration.
 * <br>
 * This class has ONLY STATIC features hooked to each of the enumerations, thus
 * NO INTERNAL STATE to can change. This trick is how we get different responses
 * from the builder because there are as many of them as there are enumerated 
 * entries without anything having to be instantiated to do it.
 * <br>
 * This builder currently comes in four flavors. If we ever get around to 
 * including quaternions as descendents of UnitAbstract, this enumeration must 
 * be expanded to include them.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public enum FBuilder {
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
	 * <br>
	 * NOTE that Cardinal has a static method that does something similar. The
	 * difference is this one also caches the cardinal.
	 * <br>
	 * Nothing about this method relies on the mode of the builder because Cardinals
	 * aren't aware of UnitAbstract children.
	 * <br>
	 * @param pName String name for the associated Cardinal
	 * @return Cardinal unit cardinal created or retrieved
	 */
	public final static Cardinal createCardinal(String pName) {
		Optional<Cardinal> test = FCache.INSTANCE.findCardinal(pName);
		if (test.isEmpty()) {
			test = Optional.ofNullable(Cardinal.generate(pName));
			FCache.INSTANCE.appendCardinal(test.get());
		}
		return test.get();
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * cardinal provided. Its way of doing this switches on the offered CladosField 
	 * instead of using this builder's mode.
	 * <br>
	 * Nothing about this method relies on the mode of the builder. 
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pMode CladosField mode to use when creating UnitAbstract numbers
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static <D extends UnitAbstract & Field & Normalizable> D createZERO(CladosField pMode, Cardinal pCard) {
		FCache.INSTANCE.appendCardinal(pCard); // just in case.
		switch (pMode) {
		case REALF : return FBuilder.REALF.createZERO(pCard);
		case REALD : return FBuilder.REALD.createZERO(pCard);
		case COMPLEXF : return FBuilder.COMPLEXF.createZERO(pCard);
		case COMPLEXD : return FBuilder.COMPLEXD.createZERO(pCard);
		default : return null;
		}
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * cardinal provided. Its way of doing this switches on the offered CladosField 
	 * instead of using this builder's mode.
	 * <br>
	 * Nothing about this method relies on the mode of the builder.
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pMode CladosField mode to use when creating UnitAbstract numbers
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	public final static <D extends UnitAbstract & Field & Normalizable> D createONE(CladosField pMode, Cardinal pCard) {
		FCache.INSTANCE.appendCardinal(pCard); // just in case.
		switch (pMode) {
			case REALF : return FBuilder.REALF.createONE(pCard);
			case REALD : return FBuilder.REALD.createONE(pCard);
			case COMPLEXF : return FBuilder.COMPLEXF.createONE(pCard);
			case COMPLEXD : return FBuilder.COMPLEXD.createONE(pCard);
			default : return null;
		}
	}

	/**
	 * Method copies the incoming number into a distinct object ensuring the ==
	 * operation fails but equals() does not.
	 * <br>
	 * NOTE this one makes no attempt to update the cardinal cache. It is assumed to
	 * have been done while constructing the number passed in as a parameter.
	 * <br>
	 * NOTE about suppressed type cast warnings | This method sifts through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br>
	 * Nothing about this method relies on the mode of the builder.
	 * <br>
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
	 * <br>
	 * This method relies on the mode of the builder called to create the number.
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pS String name for the associated Cardinal
	 * @return  UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public <D extends UnitAbstract & Field & Normalizable> D createONE(String pS) {
		Cardinal toCache = createCardinal(pS);
		switch (this) {
			case REALF : return (D) RealF.newONE(toCache);
			case REALD : return (D) RealD.newONE(toCache);
			case COMPLEXF : return (D) ComplexF.newONE(toCache);
			case COMPLEXD :	return (D) ComplexD.newONE(toCache);
		default : return null;
		}
	}

	/**
	 * Method creates a new number object with a real value set to ONE using the
	 * offered Cardinal.
	 * <br>
	 * This method relies on the mode of the builder called to create the number.
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pCard Cardinal to use in construction
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public <D extends UnitAbstract & Field & Normalizable> D createONE(Cardinal pCard) {
		switch (this) {
			case REALF : return (D) RealF.newONE(pCard);
			case REALD : return (D) RealD.newONE(pCard);
			case COMPLEXF : return (D) ComplexF.newONE(pCard);
			case COMPLEXD :	return (D) ComplexD.newONE(pCard);
			default : return null;
		}
	}

	/**
	 * Method creates a number as distinct ZERO object using default cardinal name.
	 * <br>
	 * This method relies on the mode of the builder called to create the number.
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public <D extends UnitAbstract & Field & Normalizable> D createZERO() {
		switch (this) {
		case REALF -> {
			Cardinal toCache = createCardinal(CladosField.REALF.name());
			return (D) RealF.newZERO(toCache);
		}
		case REALD -> {
			Cardinal toCache = createCardinal(CladosField.REALD.name());
			return (D) RealD.newZERO(toCache);
		}
		case COMPLEXF -> {
			Cardinal toCache = createCardinal(CladosField.COMPLEXF.name());
			return (D) ComplexF.newZERO(toCache);
		}
		case COMPLEXD -> {
			Cardinal toCache = createCardinal(CladosField.COMPLEXD.name());
			return (D) ComplexD.newZERO(toCache);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define and cache a Cardinal.
	 * <br>
	 * This method relies on the mode of the builder called to create the number.
	 * <br>
	 * @param <D>  UnitAbstract child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pCard Cardinal to be re-used.
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public <D extends UnitAbstract & Field & Normalizable> D createZERO(Cardinal pCard) {
		switch (this) {
		case REALF -> {
			FCache.INSTANCE.appendCardinal(pCard); // just in case.
			return (D) RealF.newZERO(pCard);
		}
		case REALD -> {
			FCache.INSTANCE.appendCardinal(pCard); // just in case.
			return (D) RealD.newZERO(pCard);
		}
		case COMPLEXF -> {
			FCache.INSTANCE.appendCardinal(pCard); // just in case.
			return (D) ComplexF.newZERO(pCard);
		}
		case COMPLEXD -> {
			FCache.INSTANCE.appendCardinal(pCard); // just in case.
			return (D) ComplexD.newZERO(pCard);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method creates a new number object with a real value set to ZERO using the
	 * string provided to define and cache a Cardinal.
	 * <br>
	 * This method relies on the mode of the builder called to create the number.
	 * <br>
	 * @param pS  String name for the associated Cardinal
	 * @param <D> UnitAbstract child number with the Field interface too.
	 * @return UnitAbstract child number created
	 */
	@SuppressWarnings("unchecked")
	public <D extends UnitAbstract & Field> D createZERO(String pS) {
		Cardinal toCache = createCardinal(pS);
		switch (this) {
			case REALF : return (D) RealF.newZERO(toCache);
			case REALD : return (D) RealD.newZERO(toCache);
			case COMPLEXF : return (D) ComplexF.newZERO(toCache);
			case COMPLEXD : return (D) ComplexD.newZERO(toCache);
			default : return null;
		}
	}
}
