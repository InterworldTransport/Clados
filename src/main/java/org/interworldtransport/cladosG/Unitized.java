/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Unitized<br>
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
 * ---org.interworldtransport.cladosG.Unitized<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;

/**
 * Anything implementing this interface has "units" in the physical sense.
 * <br>
 * Nothing about Clifford Algebras requires they represent quanities with
 * 'units', so this contract makes a statement that a particular class adds this
 * sense of meaning on top of any others.
 * <br>
 * This gets used with the scaling factors in Monads for now because that is the
 * part where 'weight' is applied to geometry. What units do is allow for
 * different kinds of weights.
 * <br>
 * In a nutshell, Unitized says a Scale represents units while a Cardinal
 * associated with the UnitAbstract children names the units.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public interface Unitized {

	/**
	 * This is just a gettor, but it returns the protoNumber's Cardinal.
	 * <br>
	 * @return Cardinal of the protoNumber
	 */
	public abstract Cardinal getCardinal();

}
