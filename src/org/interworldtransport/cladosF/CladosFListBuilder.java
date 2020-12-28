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

import java.util.List;

/**
 * This builder gets basic information and constructs any of the DivFields and
 * their supporting classes like a Cardinal. The builder can return arrays or
 * ArrayLists.
 * 
 * This is facilitated by the CladosField enumeration.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum CladosFListBuilder {
	/**
	 * The implicit private constructor will not be overridden.
	 */
	COMPLEXD,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	COMPLEXF,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	REALD,
	/**
	 * The implicit private constructor will not be overridden.
	 */
	REALF;

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pField CladosField enumeration hint for UnitAbstract child to be
	 *               created.
	 * @param pD     List of UnitAbstract Numbers to be copied.
	 * @param <T> UnitAbstract number from CladosF with Field interface.
	 * @return List of Numbers holds constructed copies of incoming numbers
	 */
	public final static <T extends UnitAbstract & Field> List<T> copyOf(CladosField pField, List<T> pD) {
		switch (pField) {
		case REALF -> {
			return CladosFListBuilder.REALF.copyListOf(pD);
		}
		case REALD -> {
			return CladosFListBuilder.REALD.copyListOf(pD);
		}
		case COMPLEXF -> {
			return CladosFListBuilder.COMPLEXF.copyListOf(pD);
		}
		case COMPLEXD -> {
			return CladosFListBuilder.COMPLEXD.copyListOf(pD);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * @param pField CladosField enumeration hint for UnitAbstract child to be
	 *               created.
	 * @param pD     List of UnitAbstract Numbers to be copied.
	 * @param <T> UnitAbstract number from CladosF with all number interfaces.
	 * @return List of Numbers holds constructed copies of incoming numbers
	 */
	public final static <T extends UnitAbstract & Field & Normalizable> T[] copyOf(CladosField pField, T[] pD) {
		switch (pField) {
		case REALF -> {
			return (T[]) CladosFListBuilder.REALF.copyOf(pD);
		}
		case REALD -> {
			return (T[]) CladosFListBuilder.REALD.copyOf(pD);
		}
		case COMPLEXF -> {
			return (T[]) CladosFListBuilder.COMPLEXF.copyOf(pD);
		}
		case COMPLEXD -> {
			return (T[]) CladosFListBuilder.COMPLEXD.copyOf(pD);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * 
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * 
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * 
	 * @param pDV List of CladosF Numbers to be copied.
	 * @param <T> UnitAbstract number from CladosF with Field interface.
	 * @return List of UnitAbstract children Newly constructed copies of incoming
	 *         numbers
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field> List<T> copyListOf(List<T> pDV) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = CladosFBuilder.copyOf((RealF) pDV.get(j));
			return (List<T>) List.of(tSpot);
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = CladosFBuilder.copyOf((RealD) pDV.get(j));
			return (List<T>) List.of(tSpot);
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = CladosFBuilder.copyOf((ComplexF) pDV.get(j));
			return (List<T>) List.of(tSpot);
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pDV.size()];
			for (int j = 0; j < pDV.size(); j++)
				tSpot[j] = CladosFBuilder.copyOf((ComplexD) pDV.get(j));
			return (List<T>) List.of(tSpot);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * Method copies the incoming numbers into a distinct objects ensuring the ==
	 * operation fails but equals() does not.
	 * 
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * 
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * 
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * 
	 * @param pDV Array of Numbers to be copied.
	 * @param <T> UnitAbstract number from CladosF with all number interfaces.
	 * @return UnitAbstract[] Newly constructed copies of incoming numbers
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field & Normalizable> T[] copyOf(T[] pDV) {
		switch (this) {
		case REALF -> {
			T[] tSpot = (T[]) new RealF[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = CladosFBuilder.copyOf((T) pDV[j]);
			return tSpot;
		}
		case REALD -> {
			T[] tSpot = (T[]) new RealD[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = CladosFBuilder.copyOf((T) pDV[j]);
			return tSpot;
		}
		case COMPLEXF -> {
			T[] tSpot = (T[]) new ComplexF[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = CladosFBuilder.copyOf((T) pDV[j]);
			return tSpot;
		}
		case COMPLEXD -> {
			T[] tSpot = (T[]) new ComplexD[pDV.length];
			for (int j = 0; j < pDV.length; j++)
				tSpot[j] = CladosFBuilder.copyOf((T) pDV[j]);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all UnitAbstract child objects
	 * @param pSize The size of the array to create.
	 * @return UnitAbstract[] Newly constructed ZEROS using incoming cardinal.
	 */
	public UnitAbstract[] create(Cardinal pCard, int pSize) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createZERO(pCard);
			return tSpot;
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createZERO(pCard);
			return tSpot;
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createZERO(pCard);
			return tSpot;
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createZERO(pCard);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pSize The size oF the array to create.
	 * @return UnitAbstract[] Newly constructed ZEROS with default cardinals.
	 */
	public UnitAbstract[] create(int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(CladosField.REALF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(CladosField.REALD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pS    The String name for a new cardinal to use in UnitAbstract
	 *              children
	 * @param pSize The size oF the array to create.
	 * @return UnitAbstract[] Newly constructed ZEROS with default cardinals.
	 */
	public UnitAbstract[] create(String pS, int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return create(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * 
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * 
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * 
	 * @param pCard The cardinal to re-use in all UnitAbstract child objects
	 * @param pSize The size of the array to create.
	 * @param <T> UnitAbstract number from CladosF with Field interface.
	 * @return List of UnitAbstract children set to ZERO using incoming cardinal.
	 */
	@SuppressWarnings("unchecked")
	public <T extends UnitAbstract & Field> List<T> createListOf(Cardinal pCard, int pSize) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createZERO(pCard);
			return (List<T>) List.of(tSpot);
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createZERO(pCard);
			return (List<T>) List.of(tSpot);
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createZERO(pCard);
			return (List<T>) List.of(tSpot);
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createZERO(pCard);
			return (List<T>) List.of(tSpot);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pSize The size oF the array to create.
	 * @param <T> UnitAbstract number from CladosF with Field interface.
	 * @return List of UnitAbstract children as ZEROS with default cardinals.
	 */
	public <T extends UnitAbstract & Field> List<T> createListOf(int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(CladosField.REALF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(CladosField.REALD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXF);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXD);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the default Cardinal.
	 * 
	 * @param pS    String name of a new cardinal to use in all UnitAbstract
	 *              children
	 * @param pSize The size oF the array to create.
	 * @param <T> UnitAbstract number from CladosF with Field interface.
	 * @return List of UnitAbstract children as ZEROS with default cardinals.
	 */
	public <T extends UnitAbstract & Field> List<T> createListOf(String pS, int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			return createListOf(def, pSize);
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pCard The cardinal to re-use in all UnitAbstract child objects
	 * @param pSize The size of the array to create.
	 * @return UnitAbstract[] Newly constructed ONEs using incoming cardinal.
	 */
	public UnitAbstract[] createONE(Cardinal pCard, int pSize) {
		switch (this) {
		case REALF -> {
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createONE(pCard);
			return tSpot;
		}
		case REALD -> {
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createONE(pCard);
			return tSpot;
		}
		case COMPLEXF -> {
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createONE(pCard);
			return tSpot;
		}
		case COMPLEXD -> {
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createONE(pCard);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pSize The size of the array to create.
	 * @return UnitAbstract[] Newly constructed ONEs using incoming cardinal.
	 */
	public UnitAbstract[] createONE(int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(CladosField.REALF);
			CladosFCache.INSTANCE.appendCardinal(def);
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createONE(def);
			return tSpot;
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(CladosField.REALD);
			CladosFCache.INSTANCE.appendCardinal(def);
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createONE(def);
			return tSpot;
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXF);
			CladosFCache.INSTANCE.appendCardinal(def);
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createONE(def);
			return tSpot;
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(CladosField.COMPLEXD);
			CladosFCache.INSTANCE.appendCardinal(def);
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createONE(def);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method returns an array of numbers using the offered Cardinal.
	 * 
	 * @param pS    String name for new cardinal to use in UnitAbstract children.
	 * @param pSize The size of the array to create.
	 * @return UnitAbstract[] Newly constructed ONEs using incoming cardinal.
	 */
	public UnitAbstract[] createONE(String pS, int pSize) {
		switch (this) {
		case REALF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			RealF[] tSpot = new RealF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealF) CladosFBuilder.REALF.createONE(def);
			return tSpot;
		}
		case REALD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			RealD[] tSpot = new RealD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (RealD) CladosFBuilder.REALD.createONE(def);
			return tSpot;
		}
		case COMPLEXF -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			ComplexF[] tSpot = new ComplexF[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexF) CladosFBuilder.COMPLEXF.createONE(def);
			return tSpot;
		}
		case COMPLEXD -> {
			Cardinal def = Cardinal.generate(pS);
			CladosFCache.INSTANCE.appendCardinal(def);
			ComplexD[] tSpot = new ComplexD[pSize];
			for (int j = 0; j < pSize; j++)
				tSpot[j] = (ComplexD) CladosFBuilder.COMPLEXD.createONE(def);
			return tSpot;
		}
		default -> {
			return null;
		}
		}
	}
}
