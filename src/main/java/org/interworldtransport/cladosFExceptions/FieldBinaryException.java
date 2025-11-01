/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosFExceptions.FieldBinaryException<br>
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
 * ---org.interworldtransport.cladosFExceptions.FieldBinaryException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosFExceptions;

import org.interworldtransport.cladosF.*;

/**
 * This class is designed to be the top of the binary Field exception family.
 * All instances of descendants are assumed to originate from Field related
 * problems. Common elements from each exception class are found here.
 * <br><br>
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */

public class FieldBinaryException extends FieldException {
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -8985905609966643248L;
	/**
	 * This is the second object involved in the Exception if the operation is a
	 * binary operation.
	 */
	private ProtoN Second;

	/**
	 * This method is the main constructor of all Clados Field Binary Exceptions.
	 * <br>
	 * @param pSource  ProtoN
	 * @param pMessage String
	 * @param pSecond  ProtoN
	 */
	public FieldBinaryException(ProtoN pSource, String pMessage, ProtoN pSecond) {
		super(pSource, pMessage);
		Second = pSecond;
	}

	/**
	 * This method elivers a reference to the object that originated the Exception
	 * <br>
	 * @return ProtoN
	 */
	public ProtoN getSecond() {
		return Second;
	}

}
