/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.GeneratorRangeException<br>
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
 * ---org.interworldtransport.cladosGExceptions.GeneratorRangeException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

/**
 * This class is designed to be the handler of generator range exceptions. Basis
 * can be built using just an integer, but that integer must not be so large as
 * to overwhelm the machine running the code.
 * <br><br>
 * At present, clados uses short integers (16 bits) to track many internal
 * details. That limits the number of blades in a basis to 2^15-1 which means we
 * can support a maximum of 14 generators. Using full integers (32 bits) was
 * supported for a time, but hardware limitations make this unlikely to be used
 * in the next couple decades.
 * <br><br>
 * In a practical sense, a Basis making use of 14 generators is very large with
 * 2^14 (16,384) blades. Considerable time is required to generate it. The
 * related GProduct has 2^28 (268,435,456) entries in the product table.
 * Considerably more time is required to generate that. Work HAS been put into
 * making the sort algorithm for Basis and GProduct reasonably efficient, but
 * asset horsepower will matter a great deal more at this end of the range.
 * <br><br>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class GeneratorRangeException extends Exception {
	private static final long serialVersionUID = -5624045099644092353L;
	/**
	 * The source message is the reason given by the originating Monad for the
	 * exception.
	 */
	protected String SourceMessage;

	/**
	 * Construct this exception. This exception fabricates a message complaining
	 * about the expectations of the source.
	 */
	//public GeneratorRangeException() {
	//	super();
	//	SourceMessage = "Likely blade construction attempt failed.";
	//}

	/**
	 * Construct this exception. This exception must have a message complaining
	 * about the expectations of the source.
	 * <br>
	 * @param pMessage String
	 */
	public GeneratorRangeException(String pMessage) {
		super();
		SourceMessage = pMessage;
	}

	/**
	 * This method delivers a the explanation from the source code that originated
	 * the Exception
	 * <br>
	 * @return String
	 */
	public String getSourceMessage() {
		return this.SourceMessage;
	}
}