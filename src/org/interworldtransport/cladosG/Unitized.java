/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Unitized<br>
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
 * ---org.interworldtransport.cladosG.Unitized<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;

/**
 * Anything implementing this interface has "units" in the physical sense.
 * <p>
 * Nothing about Clifford Algebras requires they represent quanities with
 * 'units', so this contract makes a statement that a particular class adds this
 * sense of meaning on top of any others.
 * <p>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public interface Unitized {

	/**
	 * This is just a gettor, but it returns the protoNumber's Cardinal.
	 * <p>
	 * @return Cardinal of the protoNumber
	 */
	public abstract Cardinal getCardinal();

}
