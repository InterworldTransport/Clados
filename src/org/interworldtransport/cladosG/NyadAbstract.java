/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.NyadAbstract<br>
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
 * ---org.interworldtransport.cladosG.NyadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Many math objects within the cladosG package have a number of attributes in
 * common. They are named objects from named algebras and with named feet. The
 * abstracted nyad covers those common elements and methods shared by objects in
 * potentially more than one algebra.
 * <p>
 * (Single monad nyads are essentially monads, but can be expanded.)
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class NyadAbstract {
	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named in
	 * the parameter. Coverage is true if a monad can be found in the nyad that
	 * belongs to the algebra.
	 * 
	 * @param pN   NyadAbstract
	 * @param pAlg String
	 * @return boolean
	 */
	public static final boolean hasAlgebra(NyadAbstract pN, Algebra pAlg) {
		for (Algebra pM : pN.getAlgebraList())
			if (pAlg.equals(pM))
				return true;
		return false;
	}

	/**
	 * If the monads listed within a nyad are all of the same algebra, the
	 * strongFlag should be set to false AND the oneAlgebra flag should be set to
	 * True. This method returns that the oneAlgebra flag.
	 * 
	 * In the future, the Frame classes will override this as it is likely that
	 * other tests are required to ensure a monad list is actually a reference
	 * frame. At the NyadAbstract leve, therefore, it is best to to think of a true
	 * response to this method as suggesting the nyad is a frame candidate.
	 * 
	 * @param pN NyadAbstract to be tested
	 * @return boolean True if nyad's monads are all of the same algebra
	 */
	public static final boolean isFrame(NyadAbstract pN) {
		if (pN._strongFlag)
			return !pN._strongFlag;
		return pN._oneAlgebra;
	}

	/**
	 * If the monads listed within a nyad are all of a different algebra, the
	 * strongFlag should be set to true. This method returns that flag.
	 * 
	 * @param pN NyadAbstract to be tested
	 * @return boolean True if nyad is strong meaning each Monad is of a different
	 *         algebra False if nyad's monads double up on any particular algebra
	 */
	public static final boolean isStrong(NyadAbstract pN) {
		return pN._strongFlag;
	}

	/**
	 * If the monads listed within a nyad are all of a different algebra, the
	 * strongFlag should be set to true. This method returns that the inverse of
	 * that flag.
	 * 
	 * @param pN NyadAbstract to be tested
	 * @return boolean False if nyad is strong meaning each Monad is of a different
	 *         algebra True if nyad's monads double up on any particular algebra
	 */
	public static final boolean isWeak(NyadAbstract pN) {
		return !pN._strongFlag;
	}

	/**
	 * This is a boolean flag set to True when the monads ALL refer to the same
	 * algebra. Otherwise it should be false.
	 */
	protected boolean _oneAlgebra = false;

	/**
	 * This is a boolean flag set to True when the monads ALL refer to DIFFERENT
	 * algebras. Otherwise it should be false.
	 */
	protected boolean _strongFlag;

	/**
	 * This array is the list of algebras used in the NyadComplexF.
	 */
	protected ArrayList<Algebra> algebraList;
	
	protected ArrayList<? extends MonadAbstract> mList;

	/**
	 * This is the Foot to which all the algebras of all monads should reference
	 */
	protected Foot footPoint;

	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String Name;

	/**
	 * This is just an alias for algebraList.stream().
	 * 
	 * @return Stream of distinct algebras in use in this Nyad.
	 */
	public Stream<Algebra> algebraStream() {
		return algebraList.stream();
	}
	
	/**
	 * Return the array of Algebras
	 * 
	 * @return ArrayList (of Algebras)
	 */
	public ArrayList<Algebra> getAlgebraList() {
		return algebraList;
	}

	/**
	 * Return the element of the array of Algebras at the jth index.
	 * 
	 * @param pj int
	 * @return Algebra
	 */
	public Algebra getAlgebra(int pj) {
		return algebraList.get(pj);
	}

	/**
	 * Simple getter for the Foot for which the nyad relies
	 * 
	 * @return Foot
	 */
	public Foot getFoot() {
		return footPoint;
	}

	/**
	 * Simple getter method of the name of a nyad.
	 * 
	 * @return String name of the nyad.
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Return the algebra order of this Nyad
	 * 
	 * @return short
	 */
	public int getNyadAlgebraOrder() {
		return algebraList.size();
	}

	/**
	 * Set the name of this NyadRealD
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		Name = name;
	}

	protected <T extends MonadAbstract> void resetAlgebraList(ArrayList<T> pMLIn) {
		algebraList.clear();
		algebraList.ensureCapacity(pMLIn.size());
		for (MonadAbstract point : pMLIn)
			if (!algebraList.contains(point.getAlgebra()))
				algebraList.add(point.getAlgebra());
		// 1 <= algebraList.size() <= monadList.size()
		// AlgebraList is reset to show which algebras are used by monads in this nyad

		Collections.sort(algebraList); // and now that list is sorted by name

		if (pMLIn.size() == 1) {
			_strongFlag = true;
			_oneAlgebra = true;
		} else if (algebraList.size() == 1) {
			_strongFlag = false;
			_oneAlgebra = true;
		} else if (pMLIn.size() == algebraList.size()) {
			_strongFlag = true;
			_oneAlgebra = false;
		} else {// We know monadList.size()>algebraList.size()>1 at this point
			_strongFlag = false;
			_oneAlgebra = false;
		}
	}

	/**
	 * Set the Foot for the nyad using this method. A Foot merely labels where an
	 * algebra is expected to be tangent to an underlying manifold.
	 * 
	 * @param pF Foot to set for the nyad.
	 */
	protected void setFoot(Foot pF) {
		footPoint = pF;
	}
}
