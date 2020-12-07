/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Scale<br>
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
 * ---org.interworldtransport.cladosF.Scale<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

import java.util.ArrayList;

/**
 * This class is definitely a work in progress. It is intended to replace an
 * array of DivFields that store the coefficients in a Monad. So... inside this
 * class is a list of RealF, RealD, ComplexF, or ComplexD.
 * 
 * Java's ArrayList is directly extended for now, so all of it's behaviors
 * should do the heavy liftfing for this class. The difference will be the
 * limited types that may be found in the list AND how they may not be mixed.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class Scale<T extends DivField & Divisible> extends ArrayList<T> {
	/**
	 * This is the type of DivField that should be present in the list held by this
	 * class. For example, if mode = CladosField.REALF, then all elements in the
	 * list will be the RealF child of DivField.
	 */
	private CladosField mode;

	/**
	 * This is about as close to a default constructor as can be done. Only the
	 * Scale's mode gets set.
	 * 
	 * @param pMode CladosField element hinting at what list types are expected
	 *              internally.
	 */
	public Scale(CladosField pMode) {
		mode = pMode;
	}

	/**
	 * Simple gettor method reporting the Scale's internal mode.
	 * 
	 * @return CladosField element reporting which DivField child is expected in the
	 *         list of this Scale.
	 */
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Simple settor method altering the Scale's internal mode. IF the new mode
	 * involves a change, the internal list is cleared too.
	 * 
	 * @param pMode CladosField element hinting at what list types are expected
	 *              internally.
	 * @return Scale of DivField children of the type named in the mode.
	 */
	public Scale<T> setMode(CladosField pMode) {
		if (this.mode != pMode) {
			clear();
			this.mode = pMode;
		}
		return this;
	}
}