/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Modal<br>
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
 * ---org.interworldtransport.cladosG.Modal<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.CladosField;

/**
 * Anything implementing this interface has CladosF numbers that are expected
 * all to be of the same type.
 * <br>
 * Nothing about Clifford Algebras requires this interface. It is about
 * computation models where one expects type consistency to avoid loss of
 * precision.
 * <br>
 * NOTE | The primary point for this interface is to mark objects that are at
 * risk if new CladosF numbers are created. Objects implementing Modal likely
 * have methods that switch internally on CladosF.CladosField or examine number
 * objects using the 'instanceof' operator.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public interface Modal {

	/**
	 * This is just a gettor, but it returns the objects modality. Is it using real
	 * or complex numbers? Is it using single or double precision floating decimals?
	 * <br>
	 * @return CladosField Mode of the object
	 */
	public abstract CladosField getMode();

}
